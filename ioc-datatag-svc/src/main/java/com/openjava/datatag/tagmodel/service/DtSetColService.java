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
	Page<DtSetCol> query(DtSetColDBParam params, Pageable pageable);
	
	List<DtSetCol> queryDataOnly(DtSetColDBParam params, Pageable pageable);
	
	DtSetCol get(Long id);
	
	DtSetCol doSave(DtSetCol m);
	
	void doDelete(Long id,String ip)throws Exception;
	void doRemove(String ids)throws Exception;
	List<DtSetCol> getByTaggingModelId(Long taggingModelId);
	/**
	 * 字段设置-确认选择
	 */
	DtTaggingModelDTO selectCol(DtTaggingModelDTO body,String id)throws Exception;
	/**
	 * 根据源表数据获取字段表
	 */
	List<DtSetCol>  getBySourceColAndTaggingModelId(String sourceCol,Long taggingModelId);
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
}
