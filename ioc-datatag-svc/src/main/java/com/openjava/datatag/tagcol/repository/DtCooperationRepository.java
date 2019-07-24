package com.openjava.datatag.tagcol.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagcol.domain.DtCooperation;

/**
 * tagcol数据库访问层
 * @author hyq
 *
 */
public interface DtCooperationRepository extends DynamicJpaRepository<DtCooperation, Long>, DtCooperationRepositoryCustom{
	
}
