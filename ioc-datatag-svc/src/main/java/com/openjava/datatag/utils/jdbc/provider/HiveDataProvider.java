package com.openjava.datatag.utils.jdbc.provider;

import com.openjava.datatag.utils.jdbc.dataprovider.annotation.ProviderName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lsw
 * @Date: 2019/7/1 19:35
 */
@ProviderName(name = "hive")
public class HiveDataProvider extends JdbcDataProvider {
    private static final Logger LOG = LoggerFactory.getLogger(HiveDataProvider.class);

    /**
     * 驱动
     */
    protected String hiveJdbcDriver = "org.apache.hive.jdbc.HiveDriver";

    /**
     * hive的字段数值型
     * INT是一类，包括：TINYINT、SMALLINT、INT、BIGINT
     */
    final protected String hiveColumnTypeInt = "INT";
    final protected String hiveColumnTypeFloat = "FLOAT";
    final protected String hiveColumnTypeDouble = "DOUBLE";
    final protected String hiveColumnTypeDecimal = "DECIMAL";

    /**
     * hive的字段日期型
     */
    final protected String hiveColumnTypeTimeStamp = "TIMESTAMP";
    final protected String hiveColumnTypeDate = "DATE";
    final protected String hiveColumnTypeInterval = "INTERVAL";

    /**
     * hive的字段日期型
     */
    final protected String hiveColumnTypeString = "STRING";
    final protected String hiveColumnTypeVarchar = "VARCHAR";
    final protected String hiveColumnTypeChar = "CHAR";

    //还有其他类型的：Misc类、复合类

    @Override
    public String getJdbcDriver() {
        return hiveJdbcDriver;
    }

    @Override
    public List<Map<String, String>> getTableList(String tableNameLike)  throws Exception {
        String queryStr = getTableListSql(tableNameLike);
        LOG.info("Execute query: {}", queryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            ResultSet resultSet = ps.executeQuery(queryStr);
            List<Map<String, String>> result = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> itemMap = new HashMap<>(2);
                itemMap.put("tableName", resultSet.getString(1));
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
        String queryStr = this.getColumnListSql(tableName);
        LOG.info("Execute query: {}", queryStr);
        try (Connection con = getConnection();
             Statement ps = con.createStatement()) {
            ResultSet resultSet = ps.executeQuery(queryStr);

            List<Map<String, String>> result = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, String> itemMap = new HashMap<>(4);
                String columnName = resultSet.getString(1);
                itemMap.put("columnName", columnName);
                if (StringUtils.isBlank(columnName)) {
                    break;
                }

                itemMap.put("comment", resultSet.getString(3));
                String columnType = this.dealColumnType(resultSet.getString(2));
                itemMap.put("dataType", columnType);
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
    @Override
    protected String getConnectUrl() {
        // jdbc:hive2://localhost:10000/default
        StringBuilder sb = new StringBuilder();
        String ip = dataSource.get("ip");
        String port = dataSource.get("port");
        String dataBaseName = dataSource.get("dataBaseName");

        sb.append("jdbc:hive2://");
        sb.append(ip);
        sb.append(":");
        sb.append(port);
        sb.append("/");
        sb.append(dataBaseName);

        return sb.toString();
    }

    @Override
    public String getTableListSql(String tableNameLike) {
        StringBuilder sb = new StringBuilder();
        sb.append(" show tables ");

        if (StringUtils.isNotBlank(tableNameLike)) {
            //拼接相关sql
            sb.append(" like '*");
            sb.append(tableNameLike);
            sb.append("*' ");
        }

        sb.append(" IN ");
        String dataBaseName = dataSource.get("dataBaseName");
        sb.append(dataBaseName);

        return sb.toString();
    }

    /**
     * 获取数据表列信息sql
     * @return
     */
    @Override
    public String getColumnListSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("DESCRIBE EXTENDED ");
        String dataBaseName = dataSource.get("dataBaseName");
        sb.append(dataBaseName);
        sb.append(".");
        sb.append(tableName);

        return sb.toString();
    }

    @Override
    public String getQueryTableDataSql(String columns, String tableName, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(tableName);
        if (pageable != null && pageable.getPageSize() != 0) {
            sb.append(" limit ");
            sb.append(pageable.getPageSize());
        }

        return sb.toString();
    }

    @Override
    public String dealColumnType(String orgColumnType) {
        String columnType;
        if (StringUtils.equalsIgnoreCase(orgColumnType, hiveColumnTypeInt)) {
            columnType = COLUMN_TYPE_NUMBER;
        } else if (StringUtils.equalsIgnoreCase(orgColumnType, hiveColumnTypeFloat)) {
            columnType = COLUMN_TYPE_NUMBER;
        } else if (StringUtils.equalsIgnoreCase(orgColumnType, hiveColumnTypeDouble)) {
            columnType = COLUMN_TYPE_NUMBER;
        } else if (StringUtils.equalsIgnoreCase(orgColumnType, hiveColumnTypeDecimal)) {
            columnType = COLUMN_TYPE_NUMBER;
        } else if (hiveColumnTypeDate.equals(orgColumnType)) {
            columnType = COLUMN_TYPE_DATE;
        } else {
            columnType = COLUMN_TYPE_STRING;
        }

        return columnType;
    }

    @Override
    public String getValidationQuery() {
        return "select 1";
    }
}
