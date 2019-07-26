package com.openjava.datatag.tagcol.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * tagcol数据库访问层
 * @author hyq
 *
 */
public interface DtCooTagcolLimitRepository extends DynamicJpaRepository<DtCooTagcolLimit, Long>, DtCooTagcolLimitRepositoryCustom{
    /**
    *作者：hyq
    *描述：根据协作成员ID查找协作字段
    */
    @Query(value = "from DtCooTagcolLimit t where t.cooId =:cooId")
    List<DtCooTagcolLimit> findByColId(@Param("cooId")Long cooId);
    /**
     * 根据协作成员ID删除
     */
    @Transactional
    @Modifying
    @Query("delete DtCooTagcolLimit t where t.cooId=:cooId")
    int deleteBycoolId(@Param("cooId")Long cooId);
}
