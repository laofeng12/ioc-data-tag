package com.openjava.datatag.utils.jdbc.dataprovider;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.openjava.datatag.utils.jdbc.dataprovider.aggregator.InnerAggregator;
import com.openjava.datatag.utils.jdbc.dataprovider.aggregator.h2.H2Aggregator;
import com.openjava.datatag.utils.jdbc.dataprovider.annotation.DatasourceParameter;
import com.openjava.datatag.utils.jdbc.dataprovider.annotation.ProviderName;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by yfyuan on 2016/8/15.
 */
@Service
public class DataProviderManager implements ApplicationContextAware {

    private static Logger LOG = LoggerFactory.getLogger(DataProviderManager.class);

    private static Map<String, Class<? extends DataProvider>> providers = new HashMap<>();

    /**
     * Map<数据源ID, 数据提供类>
     * ConcurrentHashMap防止并发
     */
    private static Map<String, DataProvider> dsProviderMap = new ConcurrentHashMap<>();

    private static ApplicationContext applicationContext;

    static {
        Set<Class<?>> classSet = new Reflections("com.openjava.datatag").getTypesAnnotatedWith(ProviderName.class);
        for (Class c : classSet) {
            if (!c.isAssignableFrom(DataProvider.class)) {
                providers.put(((ProviderName) c.getAnnotation(ProviderName.class)).name(), c);
            } else {
                System.out.println("自定义DataProvider需要继承org.cboard.dataprovider.DataProvider");
            }
        }
    }

    public static Set<String> getProviderList() {
        return providers.keySet();
    }

    /*public static DataProvider getDataProvider(String type) throws Exception {
        return getDataProvider(type, null, null);
    }*/

    public static DataProvider getDataProvider(
            String type, Map<String, String> dataSource,
            Map<String, String> query) throws Exception {
        return getDataProvider(type, dataSource, query, false);
    }

    public static DataProvider getDataProvider(
            String type, Map<String, String> dataSource,
            Map<String, String> query,
            boolean isUseForTest) throws Exception {
        /**
         * TODO 无论是ID还是name都不是很合适
         * 难道要用URL + username？最好还是字段规范统一用dataSourceId
         * 测试连接（可能此时还没有dataSourceId）最好用dataSourceName来拼接
         */
        String dataSourceId = dataSource.get("id");
        if (StringUtils.isBlank(dataSourceId)) {
            dataSourceId = "test_" + dataSource.get("name");
        }

        DataProvider dataProvider = dsProviderMap.get(dataSourceId);
        if (dataProvider != null) {
            InnerAggregator innerAggregator = dataProvider.getInnerAggregator();
            innerAggregator.setQuery(query);
            return dataProvider;
        }

        Class c = providers.get(type);
        ProviderName providerName = (ProviderName) c.getAnnotation(ProviderName.class);
        if (providerName.name().equals(type)) {
            DataProvider provider = (DataProvider) c.newInstance();
            provider.setQuery(query);
            provider.setDataSource(dataSource);
            provider.setUsedForTest(isUseForTest);
            if (provider instanceof Initializing) {
                ((Initializing) provider).afterPropertiesSet();
            }
            applicationContext.getAutowireCapableBeanFactory().autowireBean(provider);
            InnerAggregator innerAggregator = applicationContext.getBean(H2Aggregator.class);
            innerAggregator.setDataSource(dataSource);
            innerAggregator.setQuery(query);
            provider.setInnerAggregator(innerAggregator);

            dsProviderMap.put(dataSourceId, provider);

            return provider;
        }
        return null;
    }

    /**
     * 根据系统数据源格式去获取dataProvider
     * @param dsDataSource
     * @param query
     * @param isUseForTest
     * @return
     * @throws Exception
     */
    public static DataProvider getDataProvider(DsDataSource dsDataSource, Map<String, String> query, boolean isUseForTest)
            throws Exception {
        String type = getTypeByDbType(dsDataSource.getDbType());
//        0:Oracle;1:MySql高版本;2;Mysql低版本;3:PostgreSql
        Class c = providers.get(type);
        ProviderName providerName = (ProviderName) c.getAnnotation(ProviderName.class);
        if (providerName.name().equals(type)) {
            DataProvider provider = (DataProvider) c.newInstance();
            provider.setQuery(query);
            JSONObject configJson = JSONObject.parseObject(dsDataSource.getConfigJson());
            Map<String, String> configMap = Maps.transformValues(configJson, Functions.toStringFunction());
            provider.setDataSource(configMap);
            provider.setUsedForTest(isUseForTest);
            if (provider instanceof Initializing) {
                ((Initializing) provider).afterPropertiesSet();
            }
//            applicationContext.getAutowireCapableBeanFactory().autowireBean(provider);
//            InnerAggregator innerAggregator = applicationContext.getBean(H2Aggregator.class);
//            innerAggregator.setDataSource(configMap);
//            innerAggregator.setQuery(query);
//            provider.setInnerAggregator(innerAggregator);

            return provider;
        }

        return null;
    }

    protected static Class<? extends DataProvider> getDataProviderClass(String type) {
        return providers.get(type);
    }

    public static List<String> getProviderFieldByType(String providerType, DatasourceParameter.Type type) throws IllegalAccessException, InstantiationException {
        Class clz = getDataProviderClass(providerType);
        Object o = clz.newInstance();
        Set<Field> fieldSet = ReflectionUtils.getAllFields(clz, ReflectionUtils.withAnnotation(DatasourceParameter.class));
        return fieldSet.stream().filter(e ->
                e.getAnnotation(DatasourceParameter.class).type().equals(type)
        ).map(e -> {
            try {
                e.setAccessible(true);
                return e.get(o).toString();
            } catch (IllegalAccessException e1) {
                LOG.error("" , e);
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static String getTypeByDbType(Long dbType) {
        String type = "none";
//        0:Oracle;1:MySql高版本;2;Mysql低版本;3:PostgreSql
        switch (dbType.intValue()) {
            case 0:
                type = "oracle";
                break;
            case 1:
                type = "mysqlHigh";
                break;
            case 2:
                type = "mysqlLow";
                break;
            case 3:
                type = "postgreSql";
                break;
            case 4:
                type = "hive";
                break;
            default:
                break;
        }

        return type;
    }
}
