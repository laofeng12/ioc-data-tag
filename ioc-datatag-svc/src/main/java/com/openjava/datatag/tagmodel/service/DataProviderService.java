package com.openjava.datatag.tagmodel.service;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProvider;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProviderManager;
import com.openjava.datatag.utils.jdbc.dataprovider.DsDataSource;
import com.openjava.datatag.utils.jdbc.dataprovider.result.AggregateResult;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yfyuan on 2016/8/15.
 */
@Repository
public class DataProviderService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * 连接表主表别名
     */
    private final String mainTableAlias = "table_0";

    /**
     * 连接表前缀
     */
    private final String tableAliasPre = "itb_";

    /**
     * 使用系统数据源ID获取dataProvider
     * @param dsDataSource
     * @param query
     * @param
     * @return
     * @throws Exception
     */
    private DataProvider getDataProviderWithDsDataSourceId(DsDataSource dsDataSource, Map<String, String> query, Boolean isAssociatedQuery) throws Exception {
        DataProvider dataProvider = DataProviderManager.getDataProvider(dsDataSource, query, false);//
        return dataProvider;
    }

    /**
     *
     * @param dsDataSource
     * @param columns
     * @param tableName
     * @param pageable
     * @return
     */
    public AggregateResult queryTableDataWithDsDataSourceId(DsDataSource dsDataSource,String columns, String tableName, Pageable pageable) {
        if (StringUtils.isBlank(tableName)) {
            return null;//
        }

        if (StringUtils.isBlank(columns)) {
            columns = "*";//
        }

        DataProvider dataProvider = null;
        try {
            Map queryMap = new HashMap<String, String>(2);//
            queryMap.put("sql", " select " + columns + " from " + tableName);//
            dataProvider = getDataProviderWithDsDataSourceId(dsDataSource, queryMap, false);//
            return dataProvider.getTableData(columns, tableName, pageable);//
        } catch (Exception e) {
            LOG.error("", e);//
            throw new RuntimeException(e.getMessage());//
        }
    }

}
