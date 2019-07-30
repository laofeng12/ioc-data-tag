package com.openjava.datatag.tagcol.repository;


import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.tagcol.domain.DtCooperation;

import java.util.List;
import java.util.Map;

/**
 * tagcol数据库访问层
 * @author hyq
 *
 */
public interface DtCooperationRepository extends DynamicJpaRepository<DtCooperation, Long>, DtCooperationRepositoryCustom{

    /**
     *获取协作模型记录集，包含模型配置的协作用户
     */
    @Query(value =" select t.*,o.COO_USER from DT_TAGGING_MODEL t, DT_COOPERATION o where t.TAGGING_MODEL_ID=o.TAGGM_ID"
            ,nativeQuery = true)
    List<Map<String,String>> findUserModelBy();
    /**
     *根据用户Id查找该用户要协作的模型记录集
     */
    @Query(value =" select t.*,o.COO_USER from DT_TAGGING_MODEL t, DT_COOPERATION o where t.TAGGING_MODEL_ID=o.TAGGM_ID and o.COO_USER=:userId"
            ,nativeQuery = true)
    List<Map<String,String>> findUserModelByUserId(@Param("userId")Long userId);
    /**
     *根据模型Id查找该用户要协作的字段记录集
     */
    @Query(value ="select t.*,o.ID,o.COO_USER,o.cooFieldId,(case when o.COO_USER=:userId and t.SOURCE_COL=o.TAG_COL_NAME then '1' else '0' end ) IsCooField from\n" +
            "DT_SET_COL t left JOIN (select o.*,l.ID as cooFieldId,l.TAG_COL_NAME from DT_COOPERATION o left join DT_COO_TAGCOL_LIMIT l on o.ID=l.COO_ID) o \n" +
            "on t.TAGGING_MODEL_ID=o.TAGGM_ID and t.SOURCE_COL=o.TAG_COL_NAME\n" +
            "where TAGGING_MODEL_ID=:modelId"
            ,nativeQuery = true)
    List<Map<String,String>> findUserModelCooField(@Param("userId")Long userId, @Param("modelId") Long modelId);
    /**
     *根据模型Id/打标字段名查找该用户配置的协作标签组
     */
    @Query(value ="select * from DT_TAG_GROUP t where t.ID in(\n" +
            "select l.USE_TAG_GROUP from DT_COO_TAGCOL_LIMIT l left join DT_COOPERATION o on l.COO_ID=o.ID\n" +
            "where l.TAG_COL_NAME=:colField and o.COO_USER=:userId and o.TAGGM_ID=:modelId)"
            ,nativeQuery = true)
    List<Map<String, String>> findCurrentUserTagGroup(@Param("userId")Long userId, @Param("modelId") Long modelId, @Param("colField") String colField);
}
