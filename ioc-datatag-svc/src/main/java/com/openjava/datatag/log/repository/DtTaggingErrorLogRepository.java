package com.openjava.datatag.log.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.log.domain.DtTaggingErrorLog;

import java.util.List;

/**
 * 模型调度错误日志数据库访问层
 * @author zmk
 *
 */
public interface DtTaggingErrorLogRepository extends DynamicJpaRepository<DtTaggingErrorLog, Long>, DtTaggingErrorLogRepositoryCustom{

    @Query(value = "select * from (select * from DT_TAGGING_ERROR_LOG  where TAGGING_MODEL_ID=:taggingModelId order by ERROR_TIME desc) where rownum <= 1 ",nativeQuery = true)
    DtTaggingErrorLog getByTaggingModelIdOrderByErrorTimeDesc(Long taggingModelId);
}
