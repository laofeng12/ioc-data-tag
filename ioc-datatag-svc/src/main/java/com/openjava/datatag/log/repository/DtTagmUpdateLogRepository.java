package com.openjava.datatag.log.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;

import com.openjava.datatag.log.domain.DtTagmUpdateLog;

/**
 * 标签模型日志数据库访问层
 * @author zmk
 *
 */
public interface DtTagmUpdateLogRepository extends DynamicJpaRepository<DtTagmUpdateLog, Long>, DtTagmUpdateLogRepositoryCustom{
	
}
