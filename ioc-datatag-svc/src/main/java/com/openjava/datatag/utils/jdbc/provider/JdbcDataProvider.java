package com.openjava.datatag.utils.jdbc.provider;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Stopwatch;
import com.google.common.hash.Hashing;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProvider;
import com.openjava.datatag.utils.jdbc.dataprovider.Initializing;
import com.openjava.datatag.utils.jdbc.dataprovider.aggregator.Aggregatable;
import com.openjava.datatag.utils.jdbc.dataprovider.annotation.DatasourceParameter;
import com.openjava.datatag.utils.jdbc.dataprovider.annotation.QueryParameter;
import com.openjava.datatag.utils.jdbc.dataprovider.cache.CacheManager;
import com.openjava.datatag.utils.jdbc.dataprovider.cache.HeapCacheManager;
import com.openjava.datatag.utils.jdbc.dataprovider.config.AggConfig;
import com.openjava.datatag.utils.jdbc.dataprovider.exception.CBoardException;
import com.openjava.datatag.utils.jdbc.dataprovider.result.AggregateResult;
import com.openjava.datatag.utils.jdbc.dataprovider.result.JdbcAggregateResult;
import com.openjava.datatag.utils.jdbc.dataprovider.util.DPCommonUtils;
import com.openjava.datatag.utils.jdbc.dataprovider.util.SqlHelper;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by yfyuan on 2016/8/17.
 */
//@ProviderName(name = "jdbc")//屏蔽，细化到各种sql
public class JdbcDataProvider extends DataProvider implements Aggregatable, Initializing {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcDataProvider.class);

    @Value("${dataprovider.resultLimit:300000}")
    protected int resultLimit = 300000;

    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.DRIVER'|translate}} *",
            type = DatasourceParameter.Type.Input,
            required = true,
            order = 1)
    protected String DRIVER = "driver";

    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.JDBCURL'|translate}} *",
            type = DatasourceParameter.Type.Input,
            required = true,
            order = 2)
    protected String JDBC_URL = "jdbcurl";

    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.USERNAME'|translate}} *",
            type = DatasourceParameter.Type.Input,
            required = true,
            order = 3)
    protected String USERNAME = "username";

    @DatasourceParameter(label = "{{'DATAPROVIDER.JDBC.PASSWORD'|translate}}",
            type = DatasourceParameter.Type.Password,
            order = 4)
    protected String PASSWORD = "password";

    @DatasourceParameter(label = "{{'DATAPROVIDER.POOLEDCONNECTION'|translate}}",
            checked = true,
            type = DatasourceParameter.Type.Checkbox,
            order = 99)
    protected String POOLED = "pooled";

    @DatasourceParameter(label = "{{'DATAPROVIDER.AGGREGATABLE_PROVIDER'|translate}}",
            checked = true,
            type = DatasourceParameter.Type.Checkbox,
            order = 100)
    protected String aggregateProvider = "aggregateProvider";

    @QueryParameter(label = "{{'DATAPROVIDER.JDBC.SQLTEXT'|translate}}",
            type = QueryParameter.Type.TextArea,
            required = true,
            order = 1)
    protected String SQL = "sql";

    protected static final CacheManager<Map<String, Integer>> typeCahce = new HeapCacheManager<>();

    protected static final ConcurrentMap<String, DataSource> datasourceMap = new ConcurrentHashMap<>();

    protected static final ConcurrentMap<String, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<>();

    protected SqlHelper sqlHelper;

    protected String jdbcDriver = "driver";

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    /**
     * 字段类型：字符串
     */
    protected static final String COLUMN_TYPE_STRING = "string";

    /**
     * 字段类型：数值型
     */
    protected static final String COLUMN_TYPE_NUMBER = "number";

    /**
     * 字段类型：日期
     */
    protected static final String COLUMN_TYPE_DATE = "date";

    /**
     * 字段类型：不支持
     */
    protected static final String COLUMN_TYPE_UNSUPPORT = "unsupport";

    @Override
    public boolean doAggregationInDataSource() {
        //TODO 暂时先不起启用H2缓存
//        String v = dataSource.get(aggregateProvider);
//        return v != null && "true".equals(v);
        return true;
    }

    @Override
    public String[][] getData() throws Exception {
        final int batchSize = 100000;
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOG.debug("Execute JdbcDataProvider.getData() Start!");
        String sql = getAsSubQuery(query.get(SQL));
        List<String[]> list = null;
        LOG.info("SQL String: " + sql);

        try (Connection con = getConnection();
             Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            list = new LinkedList<>();
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = metaData.getColumnLabel(i + 1);
            }

            String[] header = row;
            getInnerAggregator().beforeLoad(header);

            int resultCount = 0;
            int threadId = 0;
            ExecutorService executor = Executors.newFixedThreadPool(5);
            while (rs.next()) {
                resultCount++;
                row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = rs.getString(j + 1);
                }
                list.add(row);

                if (resultCount % batchSize == 0) {
                    LOG.info("JDBC load batch {}", resultCount);
                    final String[][] batchData = list.toArray(new String[][]{});
                    Thread loadThread = new Thread(() -> {
                        getInnerAggregator().loadBatch(header, batchData);
                    }, threadId++ + "");
                    executor.execute(loadThread);
                    list.clear();
                }
                if (resultCount > resultLimit) {
                    throw new CBoardException("Cube result count " + resultCount + ", is greater than limit " + resultLimit);
                }
            }
            executor.shutdown();
            while (!executor.awaitTermination(10, TimeUnit.SECONDS));
            final String[][] batchData = list.toArray(new String[][]{});
            getInnerAggregator().loadBatch(header, batchData);
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        getInnerAggregator().afterLoad();
        stopwatch.stop();
        LOG.info("getData() using time: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return null;
    }

    /**
     * add by zmk
     * @return
     * @throws Exception
     */
    public String[][] getData2() throws Exception {
        final int batchSize = 100000;
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOG.debug("Execute JdbcDataProvider.getData() Start!");
        String sql = getAsSubQuery(query.get(SQL));
        List<String[]> list = null;
        LOG.info("SQL String: " + sql);
        try (Connection con = getConnection();
             Statement ps = con.createStatement();
             ResultSet rs = ps.executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            list = new LinkedList<>();
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = metaData.getColumnLabel(i + 1);
            }

            String[] header = row;
            list.add(header);
//            getInnerAggregator().beforeLoad(header);

            int resultCount = 0;
            int threadId = 0;
            ExecutorService executor = Executors.newFixedThreadPool(5);
            while (rs.next()) {
                resultCount++;
                row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    row[j] = rs.getString(j + 1);
                }
                list.add(row);

                if (resultCount % batchSize == 0) {
                    LOG.info("JDBC load batch {}", resultCount);
                    final String[][] batchData = list.toArray(new String[][]{});
                    Thread loadThread = new Thread(() -> {
//                        getInnerAggregator().loadBatch(header, batchData);
                    }, threadId++ + "");
                    executor.execute(loadThread);
                    list.clear();
                }
                if (resultCount > resultLimit) {
                    throw new CBoardException("Cube result count " + resultCount + ", is greater than limit " + resultLimit);
                }
            }
            executor.shutdown();
//            while (!executor.awaitTermination(10, TimeUnit.SECONDS));
//            final String[][] batchData = list.toArray(new String[][]{});
//            getInnerAggregator().loadBatch(header, batchData);
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
//        getInnerAggregator().afterLoad();
        stopwatch.stop();
        LOG.info("getData() using time: {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        return list.toArray(new String[][]{});
    }

    @Override
    public void test() throws Exception {
        String queryStr = query.get(SQL);
        LOG.info("Execute query: {}", queryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            ps.executeQuery(queryStr);
        } catch (Exception e) {
            LOG.error("Error when execute: {}",  queryStr);
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
    }


    /**
     * Convert the sql text to subquery string:
     * remove blank line
     * remove end semicolon ;
     *
     * @param rawQueryText
     * @return
     */
    protected String getAsSubQuery(String rawQueryText) {
        String deletedBlankLine = rawQueryText.replaceAll("(?m)^[\\s\t]*\r?\n", "").trim();
        return deletedBlankLine.endsWith(";") ? deletedBlankLine.substring(0, deletedBlankLine.length() - 1) : deletedBlankLine;
    }

    /**
     * 获取数据库连接
     * @return
     * @throws Exception
     */
    protected Connection getConnection() throws Exception {
        String usePool = dataSource.get(POOLED);
        String username = dataSource.get(USERNAME);
        String password = dataSource.get(PASSWORD);
        Connection conn = null;
        if (usePool != null && "true".equals(usePool)) {
            String key = Hashing.md5().newHasher().putString(JSONObject.toJSON(dataSource).toString(), Charsets.UTF_8).hash().toString();
            DataSource ds = datasourceMap.get(key);
            if (ds == null) {
                synchronized (key.intern()) {
                    ds = datasourceMap.get(key);
                    if (ds == null) {
                        Map<String, String> conf = new HashedMap();
                        conf.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, this.getJdbcDriver());
                        conf.put(DruidDataSourceFactory.PROP_URL, this.getConnectUrl());
                        conf.put(DruidDataSourceFactory.PROP_USERNAME, dataSource.get(USERNAME));
                        if (StringUtils.isNotBlank(password)) {
                            conf.put(DruidDataSourceFactory.PROP_PASSWORD, dataSource.get(PASSWORD));
                        }
                        conf.put(DruidDataSourceFactory.PROP_INITIALSIZE, "3");
                        //其他优化参数
                        //最大连接池数量
                        conf.put(DruidDataSourceFactory.PROP_MAXACTIVE, "100");
                        //最小连接池数量
                        conf.put(DruidDataSourceFactory.PROP_MINIDLE, "1");
                        //获取连接时最大等待时间，单位毫秒
                        conf.put(DruidDataSourceFactory.PROP_MAXWAIT, "20000");
                        //是否缓存preparedStatement，也就是PSCache
                        conf.put(DruidDataSourceFactory.PROP_POOLPREPAREDSTATEMENTS, "false");
                        //要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true
                        conf.put(DruidDataSourceFactory.PROP_MAXOPENPREPAREDSTATEMENTS, "20");
                        //用来检测连接是否有效的sql
                        conf.put(DruidDataSourceFactory.PROP_VALIDATIONQUERY, this.getValidationQuery());
                        //接是否有效的sql的最大等待时间
                        conf.put(DruidDataSourceFactory.PROP_VALIDATIONQUERY_TIMEOUT, "1");
                        //申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
                        conf.put(DruidDataSourceFactory.PROP_TESTONBORROW, "false");
                        //归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
                        conf.put(DruidDataSourceFactory.PROP_TESTONRETURN, "false");
                        //建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
                        conf.put(DruidDataSourceFactory.PROP_TESTWHILEIDLE, "true");
                        //属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：
                        //监控统计用的filter:stat日志用的filter:log4j防御sql注入的filter:wall
                        conf.put(DruidDataSourceFactory.PROP_FILTERS, "stat");

                        DruidDataSource druidDS = (DruidDataSource) DruidDataSourceFactory.createDataSource(conf);
                        druidDS.setBreakAfterAcquireFailure(true);
                        druidDS.setConnectionErrorRetryAttempts(2);
                        datasourceMap.put(key, druidDS);
                        ds = datasourceMap.get(key);
                    }
                }
            }
            try {
                conn = ds.getConnection();
            } catch (SQLException e) {
                datasourceMap.remove(key);
                throw e;
            }
            return conn;
        } else {
            String driver = this.getJdbcDriver();
            String jdbcurl = getConnectUrl();

            Class.forName(driver);
            Properties props = new Properties();
            props.setProperty("user", username);
            if (StringUtils.isNotBlank(password)) {
                props.setProperty("password", password);
            }
            return DriverManager.getConnection(jdbcurl, props);
        }
    }

    private JdbcTemplate getJdbcTemplate() throws Exception {
        String password = dataSource.get(PASSWORD);

        String key = Hashing.md5().newHasher().putString(JSONObject.toJSON(dataSource).toString(), Charsets.UTF_8).hash().toString();
        JdbcTemplate jdbcTemplate = jdbcTemplateMap.get(key);
        if (jdbcTemplate != null) {
            return jdbcTemplate;
        }

        DataSource ds = datasourceMap.get(key);
        if (ds == null) {
            synchronized (key.intern()) {
                ds = datasourceMap.get(key);
                if (ds == null) {
                    Map<String, String> conf = new HashedMap();
                    conf.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, this.getJdbcDriver());
                    conf.put(DruidDataSourceFactory.PROP_URL, this.getConnectUrl());
                    conf.put(DruidDataSourceFactory.PROP_USERNAME, dataSource.get(USERNAME));
                    if (StringUtils.isNotBlank(password)) {
                        conf.put(DruidDataSourceFactory.PROP_PASSWORD, dataSource.get(PASSWORD));
                    }
                    conf.put(DruidDataSourceFactory.PROP_INITIALSIZE, "3");
                    DruidDataSource druidDS = (DruidDataSource) DruidDataSourceFactory.createDataSource(conf);
                    druidDS.setBreakAfterAcquireFailure(true);
                    druidDS.setConnectionErrorRetryAttempts(5);
                    datasourceMap.put(key, druidDS);
                    ds = datasourceMap.get(key);
                }

            }
        }
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplateMap.put(key, jdbcTemplate);

        return jdbcTemplate;
    }

    @Override
    public String[] queryDimVals(String columnName, AggConfig config) throws Exception {
        String fsql = null;
        String exec = null;
        String sql = getAsSubQuery(query.get(SQL));
        List<String> filtered = new ArrayList<>();
        String whereStr = "";
        if (config != null) {
            whereStr = sqlHelper.assembleFilterSql(config);
        }
        fsql = "SELECT cb_view.%s FROM (\n%s\n) cb_view %s GROUP BY cb_view.%s";
        exec = String.format(fsql, columnName, sql, whereStr, columnName);
        LOG.info(exec);
        try (Connection connection = getConnection();
             Statement stat = connection.createStatement();
             ResultSet rs = stat.executeQuery(exec)) {
            while (rs.next()) {
                filtered.add(rs.getString(1));
            }
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return filtered.toArray(new String[]{});
    }


    private ResultSetMetaData getMetaData(String subQuerySql, Statement stat) throws Exception {
        ResultSetMetaData metaData;
        try {
            stat.setMaxRows(100);
            String fsql = "\nSELECT * FROM (\n%s\n) cb_view WHERE 1=0";
            String sql = String.format(fsql, subQuerySql);
            LOG.info(sql);
            ResultSet rs = stat.executeQuery(sql);
            metaData = rs.getMetaData();
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return metaData;
    }

    protected Map<String, Integer> getColumnType() throws Exception {
        Map<String, Integer> result = null;
        String key = getLockKey();
        result = typeCahce.get(key);
        if (result != null) {
            return result;
        } else {
            try (
                    Connection connection = getConnection();
                    Statement stat = connection.createStatement()
            ) {
                String subQuerySql = getAsSubQuery(query.get(SQL));
                ResultSetMetaData metaData = getMetaData(subQuerySql, stat);
                int columnCount = metaData.getColumnCount();
                result = new HashedMap();
                for (int i = 0; i < columnCount; i++) {
                    result.put(metaData.getColumnLabel(i + 1).toUpperCase(), metaData.getColumnType(i + 1));
                }
                typeCahce.put(key, result, 12 * 60 * 60 * 1000);
                return result;
            }
        }
    }

    @Override
    public String[] getColumn() throws Exception {
        String subQuerySql = getAsSubQuery(query.get(SQL));
        try (
                Connection connection = getConnection();
                Statement stat = connection.createStatement()
        ) {
            ResultSetMetaData metaData = getMetaData(subQuerySql, stat);
            int columnCount = metaData.getColumnCount();
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = metaData.getColumnLabel(i + 1);
            }
            return row;
        }
    }

    @Override
    public AggregateResult queryAggData(AggConfig config) throws Exception {
        // TODO 核心查询要看这个 就是在这里构造的复杂查询语句
        String exec = sqlHelper.assembleAggDataSql(config);
        List<String[]> list = new LinkedList<>();
        LOG.info(exec);
        try (
                Connection connection = getConnection();
                Statement stat = connection.createStatement();
                ResultSet rs = stat.executeQuery(exec)
        ) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    int columType = metaData.getColumnType(j + 1);
                    switch (columType) {
                    case Types.DATE:
                        Date date = rs.getDate(j + 1);
                        row[j] = (date == null? null : date.toString());
                        break;
                    default:
                        row[j] = rs.getString(j + 1);
                        break;
                    }
                }
                list.add(row);
            }
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return DPCommonUtils.transform2AggResult(config, list);
    }

    @Override
    public JdbcAggregateResult queryAggDataByJdbcTemplate(AggConfig config) throws Exception {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        String exec = sqlHelper.assembleAggDataSql(config);
        List<Map<String, Object>> data = jdbcTemplate.queryForList(exec);
        JdbcAggregateResult result = new JdbcAggregateResult();
        result.setData(data);

        return result;
    }

    @Override
    public List<String> queryFilterData(String filter) throws Exception {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        String exec = sqlHelper.assembleFilterDataSql(filter);
        return jdbcTemplate.queryForList(exec,String.class);
    }
    @Override
    public AggregateResult queryTableData(String columns, String tableName, Pageable pageable) throws Exception {
//        String exec = "select * from " + tableName;
        String exec = this.getQueryTableDataSql(columns, tableName, pageable);
        List<String[]> list = new LinkedList<>();
        LOG.info(exec);
        try (
                Connection connection = getConnection();
                Statement stat = connection.createStatement();
                ResultSet rs = stat.executeQuery(exec)
        ) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    int columType = metaData.getColumnType(j + 1);
                    switch (columType) {
                        case Types.DATE:
                            Date date = rs.getDate(j + 1);
                            row[j] = (date == null? null : date.toString());
                            break;
                        default:
                            row[j] = rs.getString(j + 1);
                            break;
                    }
                }
                list.add(row);
            }
        } catch (Exception e) {
            LOG.error("ERROR:" + e.getMessage());
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
        return DPCommonUtils.transform2AggResult(list);
    }

    @Override
    public String viewAggDataQuery(AggConfig config) throws Exception {
        return sqlHelper.assembleAggDataSql(config);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        String subQuery = null;
        if (query != null) {
            subQuery = getAsSubQuery(query.get(SQL));
        }
        SqlHelper sqlHelper = new SqlHelper(subQuery, true);
        if (!isUsedForTest()) {
            Map<String, Integer> columnTypes = null;
            try {
                // TODO 并不是所有的都要获取列属性
                String orgSql = query.get(SQL);
                if (StringUtils.containsIgnoreCase(orgSql, "select")
                        && !StringUtils.containsIgnoreCase(orgSql, "insert")) {
                    //源sql是查询语句才要获取列类型：包含select且不包含insert
                    columnTypes = getColumnType();
                }
            } catch (Exception e) {
                //  getColumns() and test() methods do not need columnTypes properties,
                // it doesn't matter for these methods even getMeta failed
                LOG.warn("getColumnType failed: {}", e.getMessage());
            }
            sqlHelper.getSqlSyntaxHelper().setColumnTypes(columnTypes);
        }
        this.sqlHelper = sqlHelper;
    }

    @Override
    public List<Map<String, String>> getTableList(String tableNameLike)  throws Exception {
//        String queryStr = query.get(SQL);
        String queryStr = getTableListSql(tableNameLike);
        LOG.info("Execute query: {}", queryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            ResultSet resultSet = ps.executeQuery(queryStr);
            List<Map<String, String>> result = new LinkedList<>();
            while (resultSet.next()) {
                Map<String, String> itemMap = new HashMap<>(2);
                itemMap.put("tableName", resultSet.getString(1));
                itemMap.put("comment", resultSet.getString(2));
                result.add(itemMap);
            }

            return result;
        } catch (Exception e) {
            LOG.error("Error when execute: {}",  queryStr);
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, String>> getColumnList(String tableName)  throws Exception {
//        String queryStr = query.get(SQL);
        String queryStr = this.getColumnListSql(tableName);
        LOG.info("Execute query: {}", queryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            ResultSet resultSet = ps.executeQuery(queryStr);

            List<Map<String, String>> result = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> itemMap = new HashMap<>(4);
                itemMap.put("columnName", resultSet.getString(1));
                itemMap.put("comment", resultSet.getString(2));
//                itemMap.put("dataType", resultSet.getString(3));
                String columnType = this.dealColumnType(resultSet.getString(3));
                if (COLUMN_TYPE_UNSUPPORT.equals(columnType)) {
                    itemMap.put("dataType", COLUMN_TYPE_STRING);
                    itemMap.put("isSupport", "false");
                } else {
                    itemMap.put("dataType", columnType);
                    itemMap.put("isSupport", "true");
                }
//                itemMap.put("dataLength", resultSet.getString(2));
                result.add(itemMap);
            }

            return result;
        } catch (Exception e) {
            LOG.error("Error when execute: {}",  queryStr);
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
    }

    /**
     * 获取连接用URL
     * @return
     */
    protected String getConnectUrl() {
        return null;
    }

    /**
     * 获取数据库表信息sql
     * @param tableNameLike
     * @return
     */
    public String getTableListSql(String tableNameLike) {
        return "";
    }

    /**
     * 获取数据表列信息sql
     * @return
     */
    public String getColumnListSql(String tableName) {
        return "";
    }

    /**
     * 处理字段类型
     * 最终字段类型为：string 或 number
     * @param orgColumnType
     * @return
     */
    public String dealColumnType(String orgColumnType) { return ""; }

    /**
     * 获取查询表数据sql
     * @param columns
     * @param tableName
     * @return
     */
    public String getQueryTableDataSql(String columns, String tableName, Pageable pageable) {
        return "";
    }

    /**
     * 获取验证查询SQL
     * @return
     */
    public String getValidationQuery() {
        return "";
    }

    @Override
    public boolean executeUpdate() throws Exception {
        String queryStr = query.get(SQL);
        String logQueryStr = queryStr;
        if (logQueryStr.length()>1000){
            logQueryStr = logQueryStr.substring(0,1000)+"...";
        }
        LOG.info("Execute update: {}", logQueryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            int flag = ps.executeUpdate(queryStr);
            //PG不同的语句应该是不一样的：insert是返回大于0才是成功，创建表是0才是成功
            if (StringUtils.containsIgnoreCase(queryStr, "insert")) {
                return (flag > 0)? true : false;
            }
            return (flag == 0)? true : false;
        } catch (Exception e) {
            LOG.error("Error when execute: {}",  queryStr);
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
    }
}
