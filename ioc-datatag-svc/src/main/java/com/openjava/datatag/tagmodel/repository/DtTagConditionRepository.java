package com.openjava.datatag.tagmodel.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 条件设置表数据库访问层
 * @author zmk
 *
 */
public interface DtTagConditionRepository extends DynamicJpaRepository<DtTagCondition, Long>, DtTagConditionRepositoryCustom{
    /**
     * 根据字段表主键获取条件设置表
     * @param colId
     * @return
     */
    @Query(value = "from DtTagCondition t where t.isDeleted=0 and t.colId =:colId")
    List<DtTagCondition> findByColId(@Param("colId")Long colId);
    /**
     * 根据字段表主键批量获取条件设置表
     */
    @Query(value = "from DtTagCondition t where t.isDeleted=0 and t.colId in(:colIds)")
    List<DtTagCondition> findByColIds(@Param("colIds")List<Long> colIds);

    /**
     * 根据标签主键批量获取条件设置表
     */
    @Query(value = "from DtTagCondition t where t.tagId in(:tagIds)")
    List<DtTagCondition> findByTagIds(@Param("tagIds")List<Long> tagIds);

    /**
     *
     * @param id
     * @param now
     * @param user
     */
    @Transactional
    @Modifying
    @Query("update DtTagCondition set isDeleted = 1, modifyTime = :now,modifyUser= :user  where colId = :id")
    void doSoftDeleteByColId(@Param("id") Long id,@Param("now") Date now,@Param("user") Long user);

    /**
     * 根据模型还有字段id获取条件设置
     */
    @Query(value = "from DtTagCondition t,DtTaggingModel m,DtSetCol c where m.taggingModelId=c.taggingModelId and c.colId=t.colId and t.tagId=:tagId and m.taggingModelId=:taggingModelId")
    List<DtTagCondition> findByTaggingModelIdAndColId(@Param("taggingModelId")Long taggingModelId ,@Param("tagId")Long tagId);
}
