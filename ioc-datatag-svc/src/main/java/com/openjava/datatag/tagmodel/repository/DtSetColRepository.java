package com.openjava.datatag.tagmodel.repository;

import com.openjava.datatag.common.Constants;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    /**
     * 根据标签模型Id获取字段表
     * @param taggingModelId
     * @return
     */
    List<DtSetCol> getByTaggingModelIdAndIsDeleted(Long taggingModelId,Long isDeleted);
    /**
    *根据源表数据获取字段
    */
    List<DtSetCol>  getByTaggingModelIdAndSourceColAndIsDeleted(Long TaggingModelId,String sourceCol,Long isDeleted);
    /**
     * 根据源表数据获取源字段列
     */
    List<DtSetCol>  getBySourceColAndTaggingModelIdAndIsSourceAndIsDeleted(String sourceCol,Long taggingModelId,Long isSource,Long isDeleted);

    //获取字段表中的源字段列
    List<DtSetCol>  getByTaggingModelIdAndIsSourceAndIsDeleted(Long TaggingModelId,Long isSource,Long isDeleted);



    //根据父节点id伪删除子节点
    @Transactional
    @Modifying
    @Query("update DtSetCol set isDeleted = 1, modifyTime = :now, modifyUser= :user where taggingModelId = :id")
    void doSoftDeleteByTaggingModelId(@Param("id") Long id,@Param("now") Date now,@Param("user") Long user);
}
