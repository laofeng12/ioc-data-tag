package com.openjava.datatag.utils.jdbc.dataprovider.util;

import com.openjava.datatag.utils.jdbc.dataprovider.config.DimensionConfig;
import com.openjava.datatag.utils.jdbc.dataprovider.config.ValueConfig;

import java.sql.Types;
import java.util.Map;

/**
 * Created by zyong on 2017/9/18.
 */
public class SqlSyntaxHelper {

    private Map<String, Integer> columnTypes;

    public String getProjectStr(DimensionConfig config) {
        return config.getColumnName();
    }

    public String getColumnNameInFilter(DimensionConfig config) {
        return this.getProjectStr(config);
    }

    public String getDimMemberStr(DimensionConfig config, int index) {
        // TODO 这里由于postgresql加了双引号，所以不能toUpperCase
        if (config.getColumnName().startsWith("\"")) {
            return config.getValues().get(index);
        }

        switch (columnTypes.get(config.getColumnName().toUpperCase())) {
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NVARCHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.DATE:
            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return "'" + config.getValues().get(index) + "'";
            default:
                return config.getValues().get(index);
        }
    }

    public String getAggStr(ValueConfig vConfig) {
        String aggExp = vConfig.getColumn();
        switch (vConfig.getAggType()) {
            case "sum":
                return "SUM(" + aggExp + ") "+ aggExp ;
            case "avg":
                return "AVG(" + aggExp + ") "+ aggExp ;
            case "max":
                return "MAX(" + aggExp + ") "+ aggExp ;
            case "min":
                return "MIN(" + aggExp + ") "+ aggExp ;
            case "distinct":
                return "COUNT(DISTINCT " + aggExp + ") "+ aggExp ;
            default:
                return "COUNT(" + aggExp + ") "+ aggExp ;
        }
    }

    public SqlSyntaxHelper setColumnTypes(Map<String, Integer> columnTypes) {
        this.columnTypes = columnTypes;
        return this;
    }


}
