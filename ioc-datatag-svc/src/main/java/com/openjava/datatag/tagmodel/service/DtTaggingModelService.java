package com.openjava.datatag.tagmodel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;

/**
 * 标签模型业务层接口
 * @author zmk
 *
 */
public interface DtTaggingModelService {
	Page<DtTaggingModel> query(DtTaggingModelDBParam params, Pageable pageable);
	
	List<DtTaggingModel> queryDataOnly(DtTaggingModelDBParam params, Pageable pageable);
	
	DtTaggingModel get(Long id);
	
	DtTaggingModel doSave(DtTaggingModel m);
	
	void doDelete(Long id);
	void doRemove(String ids);

	/**
	 * 克隆模型
	 * @param id
	 */
	void copy(Long id)throws Exception;

	void doSoftDelete(DtTaggingModel taggingModel);
}
