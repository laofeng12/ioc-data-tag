package com.openjava.datatag.log.repository;

import com.openjava.datatag.log.domain.DtTagUpdateLog;
import org.ljdp.core.spring.data.DynamicJpaRepository;



/**
 * DT_TAG_UPDATE_LOG数据库访问层
 * @author lch
 *
 */
public interface DtTagUpdateLogRepository extends DynamicJpaRepository<DtTagUpdateLog, Long>, DtTagUpdateLogRepositoryCustom{
	
}
