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


    /**
     * 查询该节点下的所有节点的父节点集（自己是叶子节点时不包括自己）
     * @param rootId
     * @return
     */
    @Query(value = "SELECT distinct PREA_TAG_ID\n" +
            "FROM DT_TAG\n" +
            "where ID != :rootId \n" +
            "START WITH ID = :rootId\n" +
            "CONNECT BY PRIOR ID = PREA_TAG_ID",nativeQuery = true)
    List<Long> findPIdByRootId(@Param("rootId") Long rootId);

    /**
     * 查询整颗表情树的所有id
     * @param rootId
     * @return
     */
    @Query(value = "SELECT ID\n" +
            "FROM DT_TAG\n" +
            "START WITH ID = :rootId\n" +
            "CONNECT BY PRIOR ID = PREA_TAG_ID",nativeQuery = true)
    List<Long> findAllIdsByRootId(@Param("rootId") Long rootId);

    List<DtTag> findByTagsIdAndIsDeleted(Long tagsID,Long isDeleted);

    List<DtTag> findByPreaTagIdAndIsDeleted(Long preaTagId,Long isDeteled);

    @Query("from DtTag t where t.isDeleted = 0 and t.id in(:ids)")
    List<DtTag> findByTagIds(@Param("ids") List<Long> ids);

    /**
     * 根据父节点获取子节点ID
     * @param preaTagId
     * @return
     */
    @Query("select t.id from DtTag t where t.isDeleted = 0 and t.preaTagId =:preaTagId")
    List<Long> findIdsByPreaTagId(@Param("preaTagId") Long preaTagId);
    /**
     * 根据标签组获取节点ID
     */
    @Query("select t.id from DtTag t where t.isDeleted = 0 and tagsId = :tagsId")
    List<Long> findIdsByTagsId(@Param("tagsId") Long tagsId);
}
