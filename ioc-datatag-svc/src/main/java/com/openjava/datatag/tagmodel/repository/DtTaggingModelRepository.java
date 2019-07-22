package com.openjava.datatag.tagmodel.repository;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    @Query(value="from DtTaggingModel t where t.isDeleted=0 and t.runState in(:runStates)")
    List<DtTaggingModel> getModelByRunStates(@Param("runStates")List<Long> runStates);
}
