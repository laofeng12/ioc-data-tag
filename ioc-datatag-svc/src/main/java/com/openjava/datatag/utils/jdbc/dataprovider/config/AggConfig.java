package com.openjava.datatag.utils.jdbc.dataprovider.config;

import java.util.List;

/**
 * Created by zyong on 2017/1/9.
 */
public class AggConfig {

    private Boolean directQuery = false;
    private List<DimensionConfig> rows;
    private List<DimensionConfig> columns;
    private List<ConfigComponent> filters;
    private List<ValueConfig> values;

    /**
     * 数据库类型
     */
    private Long dbType;

    /**
     * 页码
     */
    private Long page;

    /**
     * 每页显示数量
     */
    private Long size;

    public List<DimensionConfig> getRows() {
        return rows;
    }

    public void setRows(List<DimensionConfig> rows) {
        this.rows = rows;
    }

    public List<DimensionConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<DimensionConfig> columns) {
        this.columns = columns;
    }

    public List<ConfigComponent> getFilters() {
        return filters;
    }

    public void setFilters(List<ConfigComponent> filters) {
        this.filters = filters;
    }

    public List<ValueConfig> getValues() {
        return values;
    }

    public void setValues(List<ValueConfig> values) {
        this.values = values;
    }

    public Boolean getDirectQuery() {
        return directQuery;
    }

    public void setDirectQuery(Boolean directQuery) {
        this.directQuery = directQuery;
    }

    public Long getDbType() {
        return dbType;
    }

    public void setDbType(Long dbType) {
        this.dbType = dbType;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
