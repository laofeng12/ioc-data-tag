package com.openjava.datatag.tagmodel.service;

import java.util.List;

import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.dto.GetHistoryColDTO;
import com.openjava.datatag.tagmodel.dto.SaveConditionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.query.DtSetColDBParam;

/**
 * 字段表业务层接口
 * @author zmk
 *
 */
public interface DtSetColService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtSetCol> query(DtSetColDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtSetCol> queryDataOnly(DtSetColDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtSetCol get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtSetCol doSave(DtSetCol m);

	/**
	 *
	 * @param id
	 * @param ip
	 * @throws Exception
	 */
	void doDelete(Long id,String ip)throws Exception;

	/**
	 *
	 * @param ids
	 * @throws Exception
	 */
	void doRemove(String ids)throws Exception;

	/**
	 *
	 * @param taggingModelId
	 * @return
	 */
	List<DtSetCol> getByTaggingModelId(Long taggingModelId);
	/**
	 * 根据taggingModelId获取源字段（非删除）
	 */
	List<DtSetCol> getSourceColByTaggingModelId(Long taggingModelId);

	/**
	 * 字段设置-确认选择
	 */
	DtTaggingModelDTO selectCol(DtTaggingModelDTO body,String id)throws Exception;

	/**
	 * 根据源表数据获取单个源字段,正常应该只有一个元素
	 */
	List<DtSetCol>  getSourceSetColBySourceColAndTaggingModelId(String sourceCol,Long taggingModelId);
	/**
	 * 根据源表数据获取所有源字段
	 */
	List<DtSetCol> getSourceSetColByTaggingModelId(Long taggingModelId);


	/**
	 * 克隆字段
	 */
	void clone(Long colId,String ip)throws Exception;
//	void clone(Long colId)throws Exception;
	/**
	 * 查询字段打标历史
	 */
	GetHistoryColDTO getHistoryCol(Long colId)throws Exception;
	/**
	 *  确认打标保存接口
	 */
	void saveCondition(SaveConditionDTO req)throws Exception;

	/**
	 * 获取字段源字段+克隆字段的所有历史数量
	 * @param sourceCol
	 * @param taggingModelId
	 * @return
	 */
	Long countBySourceColAndTaggingModelId(String sourceCol,Long taggingModelId);
}
