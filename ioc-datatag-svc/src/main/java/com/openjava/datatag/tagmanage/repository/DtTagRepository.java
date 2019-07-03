package com.openjava.datatag.tagmanage.repository;

import com.openjava.datatag.tagmanage.domain.DtTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * DT_TAG数据库访问层
 * @author lch
 *
 */
public interface DtTagRepository extends DynamicJpaRepository<DtTag, Long>, DtTagRepositoryCustom{
    @Transactional
    @Modifying
    @Query("update DtTag set isDeleted = 1, modifyTime = :now  where tagsId = :id")
    void doSoftDeleteByTagsID(@Param("id") Long id, @Param("now") Date now);

    List<DtTag> findByTagsIdAndIsDeleted(Long tagsID,Long isDeleted);
}
