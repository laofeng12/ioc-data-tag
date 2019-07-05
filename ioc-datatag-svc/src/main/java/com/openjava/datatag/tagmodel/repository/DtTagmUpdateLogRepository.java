package com.openjava.datatag.tagmodel.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtTagmUpdateLog;

/**
 * 标签模型日志数据库访问层
 * @author zmk
 *
 */
public interface DtTagmUpdateLogRepository extends DynamicJpaRepository<DtTagmUpdateLog, Long>, DtTagmUpdateLogRepositoryCustom{
	
}
