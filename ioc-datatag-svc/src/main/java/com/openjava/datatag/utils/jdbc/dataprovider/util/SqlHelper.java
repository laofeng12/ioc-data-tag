package com.openjava.datatag.utils.jdbc.dataprovider.util;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.utils.jdbc.dataprovider.DataProvider;
import com.openjava.datatag.utils.jdbc.dataprovider.config.*;
import com.openjava.datatag.utils.jdbc.dataprovider.dto.Column;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by zyong on 2017/9/15.
 */
public class SqlHelper {

    private String tableName;
    private boolean isSubquery;
    private SqlSyntaxHelper sqlSyntaxHelper = new SqlSyntaxHelper();

    public SqlHelper() {
    }

    public SqlHelper(String tableName, boolean isSubquery) {
        this.tableName = tableName;
        this.isSubquery = isSubquery;
    }

    public String assembleFilterSql(AggConfig config) {
        String whereStr = null;
        if (config != null) {
            Stream<DimensionConfig> c = config.getColumns().stream();
            Stream<DimensionConfig> r = config.getRows().stream();
            Stream<ConfigComponent> f = config.getFilters().stream();
            Stream<ConfigComponent> filters = Stream.concat(Stream.concat(c, r), f);
            whereStr = filterSql(filters, "WHERE");
        }
        return whereStr;
    }

    public String assembleFilterSql(Stream<ConfigComponent> filters) {
        return filterSql(filters, "WHERE");
    }

    public String assembleAggDataSql(AggConfig config) throws Exception {
        if (config.getDirectQuery() == true) {
            return tableName;
        }
        //处理postsql的情况
        dealPostgresqlAggConfig(config);

        Stream<DimensionConfig> c = config.getColumns().stream();
        Stream<DimensionConfig> r = config.getRows().stream();
        Stream<ConfigComponent> f = config.getFilters().stream();
        Stream<ConfigComponent> filters = Stream.concat(Stream.concat(c, r), f);
        Stream<DimensionConfig> dimStream = Stream.concat(config.getColumns().stream(), config.getRows().stream());


        String dimColsStr = assembleDimColumns(dimStream);
        String aggColsStr = assembleAggValColumns(config.getValues().stream());

        String whereStr = filterSql(filters, "WHERE ");
        String groupByStr = StringUtils.isBlank(dimColsStr) ? "" : "GROUP BY " + dimColsStr;
        String orderByStr = sortSql(config.getColumns().stream(), config.getRows().stream(), config.getValues().stream(), "ORDER BY ");

        StringJoiner selectColsStr = new StringJoiner(",");
        if (!StringUtils.isBlank(dimColsStr)) {
            selectColsStr.add(dimColsStr);
        }
        if (!StringUtils.isBlank(aggColsStr)) {
            selectColsStr.add(aggColsStr);
        }/* else {// TODO 加上了这个空列时，select * from （会不会原来的逻辑就是一定要选列名）
            selectColsStr.add("*");
        }*/
        String fsql = null;
        if (isSubquery) {
            fsql = "\nSELECT %s \n FROM (\n%s\n) cb_view \n %s \n %s \n %s";
        } else {
            fsql = "\nSELECT %s \n FROM %s \n %s \n %s \n %s";
        }

        //分页
        fillPageSql(config.getDbType(), config.getPage(), config.getSize());
        String exec = String.format(fsql, selectColsStr, tableName, whereStr, groupByStr, orderByStr);
        return exec;
    }

    public String assembleFilterDataSql(String filter) throws Exception {

        String fsql = null;
        if (isSubquery) {
            fsql = "\nSELECT DISTINCT %s \n FROM (\n%s\n) cb_view ";
        } else {
            fsql = "\nSELECT DISTINCT %s \n FROM %s ";
        }

        String exec = String.format(fsql, filter, tableName);
        return exec;
    }

    /**
     * 处理postgresql的字段名，加双引号
     * @param config
     */
    private void dealPostgresqlAggConfig(AggConfig config) {
        if (!Constants.DB_TYPE_LONG_POSTGRE.equals(config.getDbType())) {
            return;
        }

        List<DimensionConfig> columns = config.getColumns();
        List<DimensionConfig> rows = config.getRows();
        List<ValueConfig> values = config.getValues();
        List<ConfigComponent> filters = config.getFilters();

        if (!CollectionUtils.isEmpty(columns)) {
            for (DimensionConfig column : columns) {
                String columnName = column.getColumnName();
                columnName = "\"" + columnName + "\"";
                column.setColumnName(columnName);
            }
        }

        if (!CollectionUtils.isEmpty(rows)) {
            for (DimensionConfig column : rows) {
                String columnName = column.getColumnName();
                columnName = "\"" + columnName + "\"";
                column.setColumnName(columnName);
            }
        }

        if (!CollectionUtils.isEmpty(values)) {
            for (ValueConfig value : values) {
                String column = value.getColumn();
                column = "\"" + column + "\"";
                value.setColumn(column);
            }
        }

        /*if (!CollectionUtils.isEmpty(filters)) {
            for (ConfigComponent filter : filters) {
                String name = filter.getName();
                name = "\"" + name + "\"";
                filter.set
            }
        }*/
    }

    private String filterSql(Stream<ConfigComponent> filterStream, String prefix) {
        StringJoiner where = new StringJoiner("\nAND ", prefix, "");
        where.setEmptyValue("");
        filterStream.map(e -> DataProvider.separateNull(e))
                .map(e -> configComponentToSql(e))
                .filter(e -> e != null)
                .forEach(where::add);
        return where.toString();
    }

    private String sortSql(Stream<DimensionConfig> columnStream, Stream<DimensionConfig> rowStream, Stream<ValueConfig> valueStream, String prefix) {
        StringJoiner orderBy = new StringJoiner(", ", prefix, " ");
        orderBy.setEmptyValue("");
        columnStream.filter(e ->StringUtils.isNotEmpty(e.getSortType())).forEach(e -> orderBy.add(e.getColumnName() + " " + e.getSortType()));
        rowStream.filter(e ->StringUtils.isNotEmpty(e.getSortType())).forEach(e -> orderBy.add(e.getColumnName() + " " + e.getSortType()));
        valueStream.filter(e -> StringUtils.isNotEmpty(e.getSortType())).forEach(e -> orderBy.add(e.getColumn() + " " + e.getSortType()));
        return orderBy.toString();
    }

    /**
     * 填充分页sql到原sql
     *
     * @param dbType
     * @param page
     * @param size
     */
    private void fillPageSql(Long dbType, Long page, Long size) {
        StringBuilder sb = new StringBuilder();
        if (dbType == null || page == null || size == null) {
            return;
        }

        Long index = page * size;
        switch (dbType.intValue()) {
            case Constants.DB_TYPE_INT_ORACLE:
                // select * from (select ROWNUM as rowno, t.* from (select * from ds_data_source) t) l where l.rowno between 2 and 3
                sb.append(" select * from (select ROWNUM as rowno, t_n.* from (");
                sb.append(tableName);
                sb.append(") t_n) t_n2 where t_n2.rowno between ");
                sb.append(index);
                sb.append(" and ");
                sb.append(size);
                // oracle的分页要嵌套好几层
                tableName = sb.toString();
                break;
            case Constants.DB_TYPE_INT_MYSQL_HIGH:
            case Constants.DB_TYPE_INT_MYSQL_LOW:
                sb.append(" limit ");
                sb.append(index);
                sb.append(",");
                sb.append(size);
                sb.append(" ");
                tableName += sb.toString();
                break;
            case Constants.DB_TYPE_INT_POSTGRE:
                sb.append(" limit ");
                sb.append(size);
                sb.append(" offset ");
                sb.append(index);
                sb.append(" ");
                tableName += sb.toString();
                break;
            case Constants.DB_TYPE_INT_HIVE:
                //select * from (select row_number() over (order by create_time desc) as rownum,u.* from user u) mm where mm.rownum between 10 and 15;
                sb.append("select * from (select row_number() over as rownum,t_n.* from( ");
                sb.append(tableName);
                sb.append(") t_n) t_n1 where t_n1.rownum between");
                sb.append(index);
                sb.append(" and ");
                sb.append(size);
                sb.append(" ");
                tableName += sb.toString();
                break;
            default:
                break;
        }
    }

    private String configComponentToSql(ConfigComponent cc) {
        if (cc instanceof DimensionConfig) {
            return filter2SqlCondtion.apply((DimensionConfig) cc);
        } else if (cc instanceof CompositeConfig) {
            CompositeConfig compositeConfig = (CompositeConfig) cc;
            String sql = compositeConfig.getConfigComponents().stream()
                    .map(e -> DataProvider.separateNull(e))
                    .map(e -> configComponentToSql(e))
                    .collect(Collectors.joining(" " + compositeConfig.getType() + " "));
            return "(" + sql + ")";
        }
        return null;
    }

    /**
     * Parser a single filter configuration to sql syntax
     */
    private Function<DimensionConfig, String> filter2SqlCondtion = (config) -> {
        if (config.getValues().size() == 0) {
            return null;
        }

        String fieldName = sqlSyntaxHelper.getColumnNameInFilter(config);
        String v0 = sqlSyntaxHelper.getDimMemberStr(config, 0);
        String v1 = null;
        if (config.getValues().size() == 2) {
            v1 = sqlSyntaxHelper.getDimMemberStr(config, 1);
        }

        if (DataProvider.NULL_STRING.equals(config.getValues().get(0))) {
            switch (config.getFilterType()) {
                case "=":
                case "≠":
                    return config.getColumnName() + ("=".equals(config.getFilterType()) ? " IS NULL" : " IS NOT NULL");
            }
        }

        switch (config.getFilterType()) {
            case "=":
            case "eq":
                return fieldName + " IN (" + valueList(config) + ")";
            case "≠":
            case "ne":
                return fieldName + " NOT IN (" + valueList(config) + ")";
            case ">":
                return rangeQuery(fieldName, v0, null);
            case "<":
                return rangeQuery(fieldName, null, v0);
            case "≥":
                return rangeQuery(fieldName, v0, null, true, true);
            case "≤":
                return rangeQuery(fieldName, null, v0, true, true);
            case "(a,b]":
                return rangeQuery(fieldName, v0, v1, false, true);
            case "[a,b)":
                return rangeQuery(fieldName, v0, v1, true, false);
            case "(a,b)":
                return rangeQuery(fieldName, v0, v1, false, false);
            case "[a,b]":
                return rangeQuery(fieldName, v0, v1, true, true);
        }
        return null;
    };

    private String valueList(DimensionConfig config) {
        String resultList = IntStream.range(0, config.getValues().size())
                .boxed()
                .map(i -> sqlSyntaxHelper.getDimMemberStr(config, i))
                .collect(Collectors
                        .joining(","));
        return resultList;
    }

    private String rangeQuery(String fieldName, Object from, Object to, boolean includeLower, boolean includeUpper) {
        StringBuffer result = new StringBuffer();
        result.append("(");
        final String gt = ">",
                gte = ">=",
                lt = "<",
                lte = "<=";
        if (from != null) {
            String op = includeLower ? gte : gt;
            result.append(fieldName + op + from);
        }
        if (to != null) {
            if (from != null) {
                result.append(" AND ");
            }
            String op = includeUpper ? lte : lt;
            result.append(fieldName + op + to);
        }
        result.append(")");
        return result.toString();
    }

    private String rangeQuery(String fieldName, Object from, Object to) {
        return rangeQuery(fieldName, from, to, false, false);
    }

    public static String surround(String text, String quta) {
        return quta + text + quta;
    }

    private String assembleAggValColumns(Stream<ValueConfig> selectStream) {
        StringJoiner columns = new StringJoiner(", ", "", " ");
        columns.setEmptyValue("");
        selectStream.map(vc -> sqlSyntaxHelper.getAggStr(vc)).filter(e -> e != null).forEach(columns::add);
        return columns.toString();
    }

    private String assembleDimColumns(Stream<DimensionConfig> columnsStream) {
        StringJoiner columns = new StringJoiner(", ", "", " ");
        columns.setEmptyValue("");
        columnsStream.map(g -> sqlSyntaxHelper.getProjectStr(g)).distinct().filter(e -> e != null).forEach(columns::add);
        return columns.toString();
    }

    public SqlHelper setSqlSyntaxHelper(SqlSyntaxHelper sqlSyntaxHelper) {
        this.sqlSyntaxHelper = sqlSyntaxHelper;
        return this;
    }

    public SqlSyntaxHelper getSqlSyntaxHelper() {
        return this.sqlSyntaxHelper;
    }

    public static String getAllSelectColumn(String tableAlias, List<Column> dimensionList, List<Column> measureList) {
        StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isEmpty(dimensionList) && CollectionUtils.isEmpty(measureList)) {
            return sb.toString();
        }

        List<Column> allColumnList = new LinkedList<>();
        allColumnList.addAll(dimensionList);
        allColumnList.addAll(measureList);

        //匹配得到该表别名拥有的列
        allColumnList = allColumnList.stream().filter(m -> tableAlias.equals(m.getTableAlias())).collect(Collectors.toList());

        for (Column column : allColumnList) {
            sb.append(tableAlias);
            sb.append(".");
            sb.append(column.getColumnName());
            sb.append(" as ");
            sb.append(column.getColumnAlias());
            sb.append(",");
        }

        String columns = sb.toString();
        columns = columns.substring(0, columns.length() - 1);
        return columns;
    }
}
