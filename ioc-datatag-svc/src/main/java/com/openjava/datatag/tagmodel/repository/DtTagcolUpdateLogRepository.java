package com.openjava.datatag.tagmodel.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtTagcolUpdateLog;

/**
 * 字段表日志数据库访问层
 * @author zmk
 *
 */
public interface DtTagcolUpdateLogRepository extends DynamicJpaRepository<DtTagcolUpdateLog, Long>, DtTagcolUpdateLogRepositoryCustom{

}
