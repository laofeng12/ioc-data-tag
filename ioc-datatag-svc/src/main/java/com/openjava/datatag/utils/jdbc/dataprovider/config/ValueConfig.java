package com.openjava.datatag.utils.jdbc.dataprovider.config;

/**
 * Created by zyong on 2017/1/9.
 */
public class ValueConfig {
    private String column;
    private String aggType;
    private String sortType;
    private String alias;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getAggType() {
        return aggType;
    }

    public void setAggType(String aggType) {
        this.aggType = aggType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

