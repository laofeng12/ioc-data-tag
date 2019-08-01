package com.openjava.datatag.tagmodel.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtWaitUpdateIndex;

import java.util.List;

/**
 * 更新索引表数据库访问层
 * @author zmk
 *
 */
public interface DtWaitUpdateIndexRepository extends DynamicJpaRepository<DtWaitUpdateIndex, Long>, DtWaitUpdateIndexRepositoryCustom{

    List<DtWaitUpdateIndex> finaByRunState(Long runState);
}
