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

    //根据父节点id伪删除子节点
    @Transactional
    @Modifying
    @Query("update DtTag set isDeleted = 1, modifyTime = :now  where preaTagId = :id")
    void doSoftDeleteByPreaTagId(@Param("id") Long id, @Param("now") Date now);


    @Query(value = "SELECT distinct PREA_TAG_ID\n" +
            "FROM DT_TAG\n" +
            "where ID != :rootId \n" +
            "START WITH ID = :rootId\n" +
            "CONNECT BY PRIOR ID = PREA_TAG_ID",nativeQuery = true)
    List<Long> findPIdByRootId(@Param("rootId") Long rootId);

    List<DtTag> findByTagsIdAndIsDeleted(Long tagsID,Long isDeleted);

    List<DtTag> findByPreaTagIdAndIsDeleted(Long preaTagId,Long isDeteled);
}
