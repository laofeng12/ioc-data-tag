package com.openjava.datatag.log.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;

import com.openjava.datatag.log.domain.DtTagcolUpdateLog;

/**
 * 字段表日志数据库访问层
 * @author zmk
 *
 */
public interface DtTagcolUpdateLogRepository extends DynamicJpaRepository<DtTagcolUpdateLog, Long>, DtTagcolUpdateLogRepositoryCustom{

}
