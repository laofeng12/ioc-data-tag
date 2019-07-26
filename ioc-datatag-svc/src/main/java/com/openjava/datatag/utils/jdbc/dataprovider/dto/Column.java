package com.openjava.datatag.utils.jdbc.dataprovider.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 列
 * @author: lsw
 * @Date: 2019/6/13 11:09
 */
public class Column {

    /**
     * 列ID
     */
    private String columnId;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列名别名
     */
    private String columnAlias;

    /**
     * 注释
     */
    private String comments;

    /**
     * 展示用的注释
     */
    private String showComments;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 表别名ID
     * 数据集为数据集ID
     * 关联表为关联表ID
     */
    private Long tableAliasId;

    /**
     * 类型
     */
    private String columnType;

    /**
     * 是否选中
     */
    private Boolean selected;

    /**
     * 过滤类型
     */
    private String filterType;

    /**
     * 过滤类型具体值
     */
    private List<Object> filterTypeValues;

    /**
     * 计算类型（如sum）
     */
    private String aggType;

    /**
     * 计算类型别名
     */
    private String aggTypeAlias;


    public String getColumnId() {
        if (StringUtils.isBlank(columnId)) {
            StringBuilder sb = new StringBuilder();
            sb.append(tableAliasId);
            sb.append("_");
            sb.append(columnName);
            columnId = sb.toString();
        }
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getShowComments() {
        if (StringUtils.isBlank(this.showComments)) {
            this.showComments = this.comments;
        }

        return showComments;
    }

    public void setShowComments(String showComments) {
        this.showComments = showComments;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public Long getTableAliasId() {
        return tableAliasId;
    }

    public void setTableAliasId(Long tableAliasId) {
        this.tableAliasId = tableAliasId;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public List<Object> getFilterTypeValues() {
        return filterTypeValues;
    }

    public void setFilterTypeValues(List<Object> filterTypeValues) {
        this.filterTypeValues = filterTypeValues;
    }

    public String getAggType() {
        return aggType;
    }

    public void setAggType(String aggType) {
        this.aggType = aggType;
    }

    public String getAggTypeAlias() {
        return aggTypeAlias;
    }

    public void setAggTypeAlias(String aggTypeAlias) {
        this.aggTypeAlias = aggTypeAlias;
    }
}
