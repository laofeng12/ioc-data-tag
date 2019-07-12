package com.openjava.datatag.tagmodel.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtFilterExpression;

import java.util.List;

/**
 * 规制表达式数据库访问层
 * @author zmk
 *
 */
public interface DtFilterExpressionRepository extends DynamicJpaRepository<DtFilterExpression, Long>, DtFilterExpressionRepositoryCustom{
    List<DtFilterExpression> findByTagConditionId(Long tagConditionId);

    @Modifying
    @Query(value = "delete from DtFilterExpression t where t.tagConditionId=:tagConditionId")
    void doRemoveByTagConditionId(@Param(value="tagConditionId")Long tagConditionId);
}
