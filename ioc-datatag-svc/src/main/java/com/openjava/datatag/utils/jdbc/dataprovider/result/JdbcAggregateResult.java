package com.openjava.datatag.utils.jdbc.dataprovider.result;

import java.util.List;
import java.util.Map;

/**
 * @author: lsw
 * @Date: 2019/6/15 13:59
 */
public class JdbcAggregateResult {
    private List<Map<String, Object>> data;

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
