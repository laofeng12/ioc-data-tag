package com.openjava.datatag.tagmodel.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtTagConditionUpdateLog;

/**
 * 条件设置表的设置日志数据库访问层
 * @author zmk
 *
 */
public interface DtTagConditionUpdateLogRepository extends DynamicJpaRepository<DtTagConditionUpdateLog, Long>, DtTagConditionUpdateLogRepositoryCustom{
	
}
