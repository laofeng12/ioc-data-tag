package com.openjava.datatag.tagmodel.repository;

import com.openjava.datatag.common.Constants;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtSetCol;

import java.util.List;

/**
 * 字段表数据库访问层
 * @author zmk
 *
 */
public interface DtSetColRepository extends DynamicJpaRepository<DtSetCol, Long>, DtSetColRepositoryCustom{
    /**
     * 根据字段名查询同一个模型中的克隆字段
     * @param taggingModelId
     * @param sourceCol
     * @return
     */
    @Query(value="from DtSetCol t where t.isSource=0 and t.isDeleted=0 and t.taggingModelId=:taggingModelId and t.sourceCol=:sourceCol")
    List<DtSetCol> getCloneClo(@Param("taggingModelId")Long taggingModelId,@Param("sourceCol")String sourceCol);
}
