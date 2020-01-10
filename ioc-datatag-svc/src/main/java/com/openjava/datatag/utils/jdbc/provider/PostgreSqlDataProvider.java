package com.openjava.datatag.utils.jdbc.provider;

import com.openjava.datatag.common.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import java.sql.*;
import java.util.*;

/**
 * @author: lsw
 * @Date: 2019/5/23 9:34
 */
//@ProviderName(name = "postgreSql")
public class PostgreSqlDataProvider extends JdbcDataProvider {
    private static final Logger LOG = LoggerFactory.getLogger(PostgreSqlDataProvider.class);

    /**
     * 驱动
     */
    protected String postgreJdbcDriver = "org.postgresql.Driver";

    /**
     * postgresql的字段int型
     */
    final protected String postgreColumnTypeInt = "int";

    /**
     * postgresql的字段date型
     */
    final protected String postgreColumnTypeDate = "date";

    @Override
    public String getJdbcDriver() {
        return postgreJdbcDriver;
    }

    /*@Override
    public List<Map<String, String>> getColumnList(String tableName)  throws Exception {
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
                itemMap.put("dataType", resultSet.getString(3));
//                itemMap.put("dataLength", resultSet.getString(2));
                result.add(itemMap);
            }

            return result;
        } catch (Exception e) {
            LOG.error("Error when execute: {}",  queryStr);
            throw new Exception("ERROR:" + e.getMessage(), e);
        }
    }*/

    /**
     * 获取连接用URL
     * @return
     */
    @Override
    protected String getConnectUrl() {
        // jdbc:postgresql://19.104.59.17:25308/ioc_resourcedirect
        StringBuilder sb = new StringBuilder();
        String ip = dataSource.get("ip");
        String port = dataSource.get("port");
        String dataBaseName = dataSource.get("dataBaseName");
        String myschema = dataSource.get("schema");
        sb.append("jdbc:postgresql://");
        sb.append(ip);
        sb.append(":");
        sb.append(port);
        sb.append("/");
        sb.append(dataBaseName);
        sb.append("?currentSchema=");
        sb.append(myschema);
        return sb.toString();
    }

    @Override
    public String getTableListSql(String tableNameLike) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT tablename,obj_description(relfilenode,'pg_class') FROM pg_tables a, pg_class b WHERE a.tablename = b.relname ");
        sb.append(" and a.tablename NOT LIKE 'pg%' AND a.tablename NOT LIKE 'sql_%'");

        if (StringUtils.isNotBlank(tableNameLike)) {
            //拼接相关sql
            sb.append(" where a.tablename like '%");
            sb.append(tableNameLike);
            sb.append("%' ");
        }

        return sb.toString();
    }

    /**
     * 获取数据表列信息sql
     * @return
     */
    @Override
    public String getColumnListSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.attname as name,col_description(a.attrelid,a.attnum) as comment,format_type(a.atttypid,a.atttypmod) as type, a.attnotnull as notnull ");
        sb.append(" FROM pg_class as c,pg_attribute as a where c.relname = '");
        sb.append(tableName);
        sb.append("' and a.attrelid = c.oid and a.attnum>0 ");

        return sb.toString();
    }

    @Override
    public String getQueryTableDataSql(String columns, String tableName, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (StringUtils.isNotBlank(columns)) {
            String noSpaceColumns = columns.replace(" ", "");
            String[] columnArray = noSpaceColumns.split(",");
            sb.append(StringUtils.join(columnArray, ","));
        } else {
            sb.append("*");
        }
        sb.append(" from");
        sb.append(tableName);
        if (pageable != null && pageable.getPageSize() != 0) {
            sb.append(" limit ");
            sb.append(pageable.getPageSize());
            sb.append(" offset ");
            sb.append(pageable.getOffset());
        }
        return sb.toString();
    }

    @Override
    public String dealColumnType(String orgColumnType) {
       String columnType;
        //postgresql带int的都是nubmer型，其他都视为string
        if (StringUtils.containsIgnoreCase(orgColumnType, postgreColumnTypeInt)) {
            columnType = COLUMN_TYPE_NUMBER;
        } else if (postgreColumnTypeDate.equals(orgColumnType)) {
            columnType = COLUMN_TYPE_DATE;
        } else {
            columnType = COLUMN_TYPE_STRING;
        }

        return columnType;
    }

    @Override
    public List<Map<String, String>> getDataLakeTableList(Long categoryId) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ");
        sb.append(Constants.DATA_LAKE_TABLE);
        if (categoryId != null) {
            sb.append(" where ");
            sb.append(Constants.DATA_LAKE_CATEGORY);
            sb.append(" =");
            sb.append(categoryId);
        }

        String exec = sb.toString();
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
                            row[j] = rs.getDate(j + 1).toString();
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

        List tableList = new LinkedList<Map<String, String>>();
        for (String[] table : list) {
            Map map = new HashMap<String, String>(2);
            map.put(table[1], table[2]);
            tableList.add(map);
        }

        return tableList;
    }

    @Override
    public String getValidationQuery() {
        return "select 'Hello World' as hello";
    }

    @Override
    public String[][] getData() throws Exception{
        String sql = getAsSubQuery(query.get(SQL));
        List<String[]> list = new LinkedList<>();
        LOG.info("SQL String: " + sql);
        try (
                Connection connection = getConnection();
                Statement stat = connection.createStatement();
                ResultSet rs = stat.executeQuery(sql)
        ) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            String[] headerrow = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                headerrow[i] = metaData.getColumnLabel(i + 1);
            }

            String[] header = headerrow;
            list.add(header);
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    int columType = metaData.getColumnType(j + 1);
                    switch (columType) {
                        case Types.DATE:
                            if(rs.getDate(j + 1)!=null){
                                row[j] = rs.getDate(j + 1).toString();
                            }
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
        if (CollectionUtils.isNotEmpty(list)){
            String[][] result = new String[list.size()][list.get(0).length];
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).length; j++) {
                    result[i][j] = list.get(i)[j];
                }
            }
            return result;
        }
        return null;
    }
}
