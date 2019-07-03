package com.openjava.datatag.tagmodel.repository;

import com.openjava.datatag.tagmodel.domain.DtTagcolUpdateLog;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtTagCondition;

import java.util.List;

/**
 * 条件设置表数据库访问层
 * @author zmk
 *
 */
public interface DtTagConditionRepository extends DynamicJpaRepository<DtTagCondition, Long>, DtTagConditionRepositoryCustom{
    /**
     * 根据字段表主键获取条件设置表
     * @param colId
     * @return
     */
    List<DtTagCondition> findByColId(@Param("colId")Long colId);
    /**
     * 根据字段表主键批量获取条件设置表
     */
    @Query(value = "from DtTagCondition t where t.colId in(:colIds)")
    List<DtTagCondition> findByColIds(@Param("colIds")List<Long> colIds);

}
