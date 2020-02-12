package com.openjava.datatag.tagmodel.repository;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

/**
 * 标签模型数据库访问层
 * @author zmk
 *
 */
public interface DtTaggingModelRepository extends DynamicJpaRepository<DtTaggingModel, Long>, DtTaggingModelRepositoryCustom{
    /**
     * 查询等待设置调度任务的模型
     * @param runStates
     * @return
     */
    @Query(value="from DtTaggingModel t where t.isDeleted=0 and t.runState in(:runStates) and t.cycle is not null")
    List<DtTaggingModel> getModelByRunStates(@Param("runStates")List<Long> runStates);

    /**
     * 根据标签ID获取模型
     * @return
     */
    @Query(value = "select m from DtTaggingModel m,DtSetCol c ,DtTagCondition t where m.taggingModelId = c.taggingModelId and c.colId=t.colId and t.tagId in(:tagIds)")
    List<DtTaggingModel> getModelByTagIds(@Param("tagIds")List<Long> tagIds);

    /**
     * 获取需要立即运行的表情模型
     * @param dtDispatchNow
     * @param isDeleted
     * @param runstate
     * @return
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<DtTaggingModel> findByCycleEnumAndIsDeletedAndRunState(Long dtDispatchNow, Long isDeleted,Long runstate);
}
