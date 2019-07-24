package com.openjava.datatag.utils.jdbc.dataprovider.aggregator;


import com.openjava.datatag.utils.jdbc.dataprovider.config.AggConfig;
import com.openjava.datatag.utils.jdbc.dataprovider.result.AggregateResult;
import com.openjava.datatag.utils.jdbc.dataprovider.result.JdbcAggregateResult;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * Created by yfyuan on 2017/1/13.
 */
public interface Aggregatable {

    /**
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @param columnName
     * @return
     */
    String[] queryDimVals(String columnName, AggConfig config) throws Exception;

    /**
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @return
     */
    String[] getColumn() throws Exception;

    /**
     * The data provider that support DataSource side Aggregation must implement this method.
     *
     * @param ac aggregate configuration
     * @return
     */
    AggregateResult queryAggData(AggConfig ac) throws Exception;


    JdbcAggregateResult queryAggDataByJdbcTemplate(AggConfig config) throws Exception;

    List<String> queryFilterData(String filter) throws Exception;

    AggregateResult queryTableData(String columns, String tableName, Pageable pageable) throws Exception;

    default String viewAggDataQuery(AggConfig ac) throws Exception {
        return "Not Support";
    }

}
