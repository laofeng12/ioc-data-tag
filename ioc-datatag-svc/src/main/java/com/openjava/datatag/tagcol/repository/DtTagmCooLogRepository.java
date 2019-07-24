package com.openjava.datatag.tagcol.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagcol.domain.DtTagmCooLog;

import java.util.List;

/**
 * tagcol数据库访问层
 * @author hyq
 *
 */
public interface DtTagmCooLogRepository extends DynamicJpaRepository<DtTagmCooLog, Long>, DtTagmCooLogRepositoryCustom{
    /**
     * 根据字段表主键获取条件设置表
     * @param cooId
     * @return
     */
    @Query(value = "from DtTagmCooLog t where t.cooId =:cooId")
    List<DtTagmCooLog> findByColId(@Param("cooId")Long cooId);
}
