package com.openjava.datatag.utils.jdbc.excuteUtil;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProvider;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProviderManager;
import com.openjava.datatag.utils.jdbc.dataprovider.DsDataSource;
import com.openjava.datatag.utils.jdbc.dataprovider.Initializing;
import com.openjava.datatag.utils.jdbc.provider.PostgreSqlDataProvider;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 导入到MPP（PG）数据仓库中
 * @author: zmk
 * @Date: 2019/7/22
 */
@Data
public class MppPgExecuteUtil extends ExecuteUtil {

    /**
     * 表名
     */
    private String tableName;
    /**
     *  表主键
     */

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 列名列表
     */
    private List<String> columnList;

    /**
     * map<列名, 列备注>
     */
    private Map<String, String> columnMap = new HashMap<>(16);

    /**
     * 表主键
     */
    private String tableKey;

    /**
     * 数据列表
     */
    private List<Object> dataList;

    private DsDataSource dsDataSource;

    private PostgreSqlDataProvider dataProvider= new PostgreSqlDataProvider();
    @Override
    public Result dealFile() {
        return null;
    }

    @Override
    protected Result createTable(Map<String,String> map) {
        Result result = new GeneralResult();
        result.setSuccess(true);
        if (CollectionUtils.isEmpty(map)) {
            result.setSuccess(false);
            result.setMsg("表数据列不能为空！");
            return result;
        }
//        columnList = new LinkedList();
        columnMap = map;

        List<String> createTableSqlList = getCreateTableSqlList();
        for (String createTableSql : createTableSqlList) {
            Map<String, String> sqlMap = new HashMap<>(2);
            sqlMap.put("sql", createTableSql);
            result.setSuccess(false);
            try {
                initDataProvider(this.getValidDataSource(), sqlMap, false);
                boolean createTableResult = dataProvider.executeUpdate();
                result.setSuccess(createTableResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected List<String> getCreateTableSqlList() {
        List<String> sqlList = new LinkedList<>();
        String schema = "public";

        //建表语句
        StringBuilder createTableSql = new StringBuilder();
        createTableSql.append("CREATE TABLE \"");
        createTableSql.append(schema);
        createTableSql.append("\".");
        createTableSql.append("\"");
        createTableSql.append(tableName);
        createTableSql.append("\"");
        createTableSql.append(" ( ");
        for (Map.Entry<String, String> entry : columnMap.entrySet()) {
            createTableSql.append("\"");
            createTableSql.append(entry.getKey());
            if (entry.getKey().equals(getTableKey())) {
                createTableSql.append("\" varchar primary key,");
            }else {
                createTableSql.append("\" varchar,");
            }
        }
        String tableSql0 = createTableSql.toString();
        tableSql0 = tableSql0.substring(0, tableSql0.length() - 1);
        tableSql0 += ")";
        sqlList.add(tableSql0);

        //表注释
        StringBuilder tableCommentsSqlSb = new StringBuilder();
        tableCommentsSqlSb.append("COMMENT ON TABLE \"");
        tableCommentsSqlSb.append(schema);
        tableCommentsSqlSb.append("\".\"");
        tableCommentsSqlSb.append(tableName);
        tableCommentsSqlSb.append("\" IS '");
        tableCommentsSqlSb.append(tableComment);
        tableCommentsSqlSb.append("'");
        sqlList.add(tableCommentsSqlSb.toString());

        //字段注释
        for (Map.Entry<String, String> entry : columnMap.entrySet()) {
            StringBuilder commentSql = new StringBuilder();
            commentSql.append(" COMMENT ON COLUMN \"");
            commentSql.append(schema);
            commentSql.append("\".\"");
            commentSql.append(tableName);
            commentSql.append("\".\"");
            commentSql.append(entry.getKey());
            commentSql.append("\" IS '");
            commentSql.append(entry.getValue());
            commentSql.append("'");

            sqlList.add(commentSql.toString());
        }

        return sqlList;
    }

    @Override
    protected Result dropTable() {
        Result result = new GeneralResult();
        String dropTableSql = this.getDropTableSql();
        Map<String, String> sqlMap = new HashMap<>(2);
        sqlMap.put("sql", dropTableSql);
        result.setSuccess(false);
        try {
            initDataProvider(this.getValidDataSource(), sqlMap, false);
            boolean createTableResult = dataProvider.executeUpdate();
            result.setSuccess(createTableResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected String getDropTableSql() {
        StringBuilder dropTableSqlSb = new StringBuilder();
        dropTableSqlSb.append("DROP TABLE if exists \"");
        dropTableSqlSb.append(tableName);
        dropTableSqlSb.append("\"");

        return dropTableSqlSb.toString();
    }

    @Override
    protected Result insertDataList() {
        Result result = new GeneralResult();
        List<String> insertDataSqlList = this.getInsertDataSqlList();
        Map<String, String> sqlMap = new HashMap<>(2);
        for (String insertDataSql : insertDataSqlList) {
            sqlMap.put("sql", insertDataSql);
            result.setSuccess(false);
            try {
                initDataProvider(this.getValidDataSource(), sqlMap, false);
                boolean createTableResult = dataProvider.executeUpdate();
                result.setSuccess(createTableResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected List<String> getInsertDataSqlList() {
        List sqlList = new LinkedList();
        StringBuilder insertDataSqlSb = new StringBuilder();
        insertDataSqlSb.append("INSERT INTO \"");
        insertDataSqlSb.append(tableName);
        insertDataSqlSb.append("\" (");

        StringBuilder columnSb = new StringBuilder();
        for (Map.Entry<String, String> entry : columnMap.entrySet()) {
            columnSb.append("\"");
            columnSb.append(entry.getKey());
            columnSb.append("\",");
        }
        String columns = columnSb.toString();
        columns = columns.substring(0, columns.length() - 1);

        insertDataSqlSb.append(columns);
        insertDataSqlSb.append(") VALUES ");

        String sqlPre = insertDataSqlSb.toString();
        //分批插入
//        this.getDataList().remove(0);
        List<List<Object>> partitionList = Lists.partition(this.getDataList(), 300);
        for (List<Object> objectList : partitionList) {
            StringBuilder dataInsertSqlSb = new StringBuilder();
            dataInsertSqlSb.append(sqlPre);
            List<String> valueList = new LinkedList<>();
            for (Object object : objectList) {
                StringBuilder valuesSb = new StringBuilder();
                List<String> item = (List<String>) object;
                for (String v : item) {
                    valuesSb.append("'");
                    valuesSb.append(v);
                    valuesSb.append("',");
                }
                String values = valuesSb.toString();
                values = values.substring(0, values.length() - 1);
                values = "(" + values + ")";
                valueList.add(values);
            }

            String partitionInsertSql = String.join(",", valueList);
            dataInsertSqlSb.append(partitionInsertSql);
            sqlList.add(dataInsertSqlSb.toString());
        }

        return sqlList;
    }

    public List<Object> getDataList() {
        return dataList;
    }

    private DsDataSource getValidDataSource() {
        if (this.dsDataSource == null) {
            DsDataSource dataSource = new DsDataSource();
            dataSource.setDbType(3L);
            dataSource.setConfigJson("{\"password\":\"ioc123456\",\"username\":\"mppuser\",\"ip\":\"183.6.55.26\",\"port\":\"5432\",\"dataBaseName\":\"postgres\",\"pooled\":true}");
            this.dsDataSource = dataSource;
        }

        return this.dsDataSource;
    }
    private void initDataProvider(DsDataSource dataSource,Map<String, String> sqlMap,Boolean isUserForTest )throws Exception{
        dataProvider.setQuery(sqlMap);
        JSONObject configJson = JSONObject.parseObject(dataSource.getConfigJson());
        Map<String, String> configMap = Maps.transformValues(configJson, Functions.toStringFunction());
        dataProvider.setDataSource(configMap);
        dataProvider.setUsedForTest(isUserForTest);
        if (dataProvider instanceof Initializing) {
            ((Initializing) dataProvider).afterPropertiesSet();
        }
    }

    public static void main(String[] args) {
        MppPgExecuteUtil u= new MppPgExecuteUtil();
        u.setTableName("zmk_test");//表名
        u.setTableKey("name");//主键
        u.dropTable();//删表
        Map<String,String> map  = new HashMap<>();
        map.put("id","主键");
        map.put("name","名字");
        u.createTable(map);//建表
        List<Object> dataList = new ArrayList<>();
        List<String> values = new ArrayList<>();
        values.add("1");
        values.add("名字1");
        dataList.add(values);
        List<String> values2 = new ArrayList<>();
        values2.add("2");
        values2.add("名字2");
        dataList.add(values2);
        u.setDataList(dataList);
        u.insertDataList();//load数据
        u.dropTable();//删表
    }
}