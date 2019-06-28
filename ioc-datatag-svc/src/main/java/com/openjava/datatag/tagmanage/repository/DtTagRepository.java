package com.openjava.datatag.tagmanage.repository;

import com.openjava.datatag.tagmanage.domain.DtTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * DT_TAG数据库访问层
 * @author lch
 *
 */
public interface DtTagRepository extends DynamicJpaRepository<DtTag, Long>, DtTagRepositoryCustom{
    @Transactional
    @Modifying
    @Query("update DtTag set isDeleted = 1  where tagsId = :id")
    void softDelete(@Param("id") Long id);

}
