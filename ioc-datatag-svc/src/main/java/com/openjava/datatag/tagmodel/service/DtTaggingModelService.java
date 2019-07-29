package com.openjava.datatag.tagmodel.service;

import java.util.List;

import com.openjava.datatag.tagmodel.dto.DtTaggingDispatchDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
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
	DtTaggingModel doNew(DtTaggingModel m,BaseUserInfo userInfo, String ip);
	DtTaggingModel doNew(DtTaggingModelDTO m,BaseUserInfo userInfo, String ip) throws APIException;
	DtTaggingModel doUpdate(DtTaggingModel body, DtTaggingModel db, BaseUserInfo userInfo,String ip);


	void doDispatch(DtTaggingDispatchDTO body, DtTaggingModel db, Long userId, String ip) throws APIException;
	void doDelete(Long id);
	void doRemove(String ids);

	/**
	 * 克隆模型
	 * @param id
	 */
	void copy(Long id,String ip)throws Exception;

	void doSoftDelete(DtTaggingModel taggingModel,Long userId,String ip);
	/**
	 * 根据runState获取型
	 */
	List<DtTaggingModel> getModelByRunStates(List<Long> runStates);
	/**
	 * 设置模型调度的方法（核心方法）
	 */
	void setToJob();
	/**
	 * 获取打标数据并根据规制自动打标（核心方法）
	 */
	void calculation(Long taggingModelId);

	/**
	 * 获取数据集数据（核心方法）
	 */
	Object getDataFromDataSet(Long taggingModelId,Pageable pageable);

	/**
	 * 获取可执行执行的打标sql，用去mpp自动打标
	 */
	List<String> getMarkingSQL(Long taggingModelId);
}
