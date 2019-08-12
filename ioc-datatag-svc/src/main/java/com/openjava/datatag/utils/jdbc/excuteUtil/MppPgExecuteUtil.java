package com.openjava.datatag.utils.jdbc.excuteUtil;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.openjava.datatag.component.PostgreSqlConfig;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProvider;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProviderManager;
import com.openjava.datatag.utils.jdbc.dataprovider.DsDataSource;
import com.openjava.datatag.utils.jdbc.dataprovider.Initializing;
import com.openjava.datatag.utils.jdbc.provider.PostgreSqlDataProvider;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * 导入到MPP（PG）数据仓库中
 * @author: zmk
 * @Date: 2019/7/22
 */
@Data
@Component
public class MppPgExecuteUtil extends ExecuteUtil {

    private PostgreSqlConfig postgreSqlConfig;

    /**
     * 表名
     */
    private String tableName;
    /**
     * 表名(用于多表查询)Map<表名, 表别名>
     */
    private Map<String, String> tableNameForQuery = new LinkedHashMap<>(16);
    /**
     *  表主键
     */
    private String tableKey;
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
    private Map<String, String> columnMap = new LinkedHashMap<>(16);
    /**
     * 列名(用于多表查询)Map<列名, 表别名>
     */
    private Map<String, String> columnMapForQuery = new LinkedHashMap<>(16);
    /**
     * map<列名, 列类型>
     */
    private Map<String, String> columnTypeMap = new LinkedHashMap<>(16);

    /**
     * 数据列表
     */
    private List<Object> dataList;

    /**
     * 更新语句
     */
    private List<String> updateSqlList;

    /**
     * 分页器
     */
    private Pageable pageable;

    /**
     * 查询语句
     */
    private String SQL;

    /**
     * 查询条件
     */
    private String condition;

    private DsDataSource dsDataSource;

    private PostgreSqlDataProvider dataProvider= new PostgreSqlDataProvider();
    @Override
    public Result dealFile() {
        return null;
    }

    @Override
    public Result createTable(Map<String,String> columnMap,Map<String,String> columnTypeMap) {
        Result result = new GeneralResult();
        result.setSuccess(true);
        if (CollectionUtils.isEmpty(columnMap)) {
            result.setSuccess(false);
            result.setMsg("表数据列不能为空！");
            return result;
        }
//        columnList = new LinkedList();
        this.columnMap = columnMap;
        this.columnTypeMap = columnTypeMap;
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
    public List<String> getCreateTableSqlList() {
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
                createTableSql.append("\" "+ columnTypeMap.get(entry.getKey()) +" primary key,");
            }else {
                createTableSql.append("\" "+ columnTypeMap.get(entry.getKey()) +",");
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
    public Result dropTable() {
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
    public String getDropTableSql() {
        StringBuilder dropTableSqlSb = new StringBuilder();
        dropTableSqlSb.append("DROP TABLE if exists \"");
        dropTableSqlSb.append(tableName);
        dropTableSqlSb.append("\"");

        return dropTableSqlSb.toString();
    }

    @Override
    public Result insertDataList() {
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
    public List<String> getInsertDataSqlList() {
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
        List<List<Object>> partitionList = Lists.partition(this.getDataList(), 10000);
        for (List<Object> objectList : partitionList) {
            StringBuilder dataInsertSqlSb = new StringBuilder();
            dataInsertSqlSb.append(sqlPre);
            List<String> valueList = new LinkedList<>();
            for (Object object : objectList) {
                StringBuilder valuesSb = new StringBuilder();
                List<Object> item = (List<Object>) object;
                for (Object v : item) {
                    if (StringUtils.isBlank(String.valueOf(v))){
                        valuesSb.append("null");
                        valuesSb.append(",");
                    }else{
                        valuesSb.append("'");
                        valuesSb.append(String.valueOf(v));
                        valuesSb.append("',");
                    }
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

    @Override
    public Result updateDataList(){
        Result result = new GeneralResult();
        result.setSuccess(false);
        List<String> updateSqlList = getUpdateDataList();
        Map<String, String> sqlMap = new HashMap<>(2);
        for (String updateSql : updateSqlList) {
            sqlMap.put("sql", updateSql);
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

    /**
     * 获取更新语句sql
     */
    @Override
    public List<String> getUpdateDataList(){
        //分批更新
//        List<List<String>> updateList = Lists.partition(this.getUpdateSqlList(), 300);
        return this.getUpdateSqlList();
    }

    /**
     * 查询数据
     */
    @Override
    public String[][] getData(){
        try {
            String querySql = getQuerySql();
            Map<String, String> sqlMap = new HashMap<>(2);
            sqlMap.put("sql", querySql);
            initDataProvider(this.getValidDataSource(), sqlMap, false);
            return dataProvider.getData();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询数据
     */
    @Override
    public String[][] getData2(){
        try {
            Map<String, String> sqlMap = new HashMap<>(2);
            sqlMap.put("sql", this.SQL);
            initDataProvider(this.getValidDataSource(), sqlMap, false);
            return dataProvider.getData2();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取查询语句SQL
     * @return
     */
    public String getQuerySql(){
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (columnMapForQuery.size()<=0){
            sb.append(" * ");
        }else{
            StringBuilder columnSb = new StringBuilder();
            for (Map.Entry<String, String> entry : columnMapForQuery.entrySet()) {
                columnSb.append(entry.getValue()+".");//别名
                columnSb.append("\"");
                columnSb.append(entry.getKey());//表名
                columnSb.append("\",");
            }
            String columns = columnSb.toString();
            columns = columns.substring(0, columns.length() - 1);
            sb.append(columns);
        }
        sb.append(" from ");
        StringBuilder tableSb = new StringBuilder();
        for (Map.Entry<String, String> entry : tableNameForQuery.entrySet()) {
            tableSb.append("\"");
            tableSb.append(entry.getKey()+"\" "+entry.getValue());//表名加别名
            tableSb.append(",");
        }
        String tableNames = tableSb.toString();
        tableNames = tableNames.substring(0, tableNames.length() - 1);
        sb.append(tableNames);
        if (StringUtils.isNotBlank(condition)){
            sb.append(" "+condition+" ");
        }
        if (pageable != null && pageable.getPageSize() != 0) {
            sb.append(" limit ");
            sb.append(pageable.getPageSize());
            sb.append(" offset ");
            sb.append(pageable.getOffset());
        }
        return sb.toString();
    }

    public List<Object> getDataList() {
        return dataList;
    }

    public void initValidDataSource (PostgreSqlConfig postgreSqlConfig){
        this.postgreSqlConfig = postgreSqlConfig;
    }

    public DsDataSource getValidDataSource() {
        if (this.dsDataSource == null) {
            DsDataSource dataSource = new DsDataSource();
            dataSource.setDbType(3L);
//            dataSource.setConfigJson("{\"password\":\"ioc123456\",\"username\":\"mppuser\",\"ip\":\"183.6.55.26\",\"port\":\"5432\",\"dataBaseName\":\"postgres\",\"pooled\":true}");
            dataSource.setConfigJson(JSONObject.toJSONString(postgreSqlConfig));
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
        System.out.println();
    }
}
