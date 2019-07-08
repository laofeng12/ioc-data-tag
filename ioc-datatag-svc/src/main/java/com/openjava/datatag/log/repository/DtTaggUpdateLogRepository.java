package com.openjava.datatag.log.repository;

import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import org.ljdp.core.spring.data.DynamicJpaRepository;



/**
 * DT_TAGG_UPDATE_LOG数据库访问层
 * @author lch
 *
 */
public interface DtTaggUpdateLogRepository extends DynamicJpaRepository<DtTaggUpdateLog, Long>, DtTaggUpdateLogRepositoryCustom{
	
}
