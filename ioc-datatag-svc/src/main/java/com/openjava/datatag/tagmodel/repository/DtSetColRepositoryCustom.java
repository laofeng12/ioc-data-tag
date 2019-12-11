package com.openjava.datatag.tagmodel.repository;

/**
 *
 */
public interface DtSetColRepositoryCustom {
    /**
     * 检查打标条件是否合理
     */
    void check(String conditionSql)throws Exception;
}
