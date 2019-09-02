package com.openjava.datatag.tagmodel.service;

import java.util.List;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.dto.DtTaggingDispatchDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelRenameDTO;
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
	//DtTaggingModel doNew(DtTaggingModel m,BaseUserInfo userInfo, String ip);
	DtTaggingModel doNew(DtTaggingModelDTO m,BaseUserInfo userInfo, String ip) throws APIException;
	DtTaggingModel doRename(DtTaggingModelRenameDTO body, DtTaggingModel db, BaseUserInfo userInfo, String ip);


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
	void calculation(DtTaggingModel tagModel) throws Exception;

	/**
	 * 获取数据集数据（核心方法）
	 */
	Object getDataFromDataSet(Long taggingModelId,int type,Pageable pageable)throws Exception;

	/**
	 * 获取模型打标结果数据
	 */
	Object getTaggingResultData(Long taggingModelId,int type,Pageable pageable)throws Exception;

	/**
	 * 获取可执行执行的打标sql，用去mpp自动打标
	 */
	List<String> getMarkingSQL(Long taggingModelId);
	/**
	 * 重组数据
	 * @param cols 字段表（包括克隆的）
	 * @param dataList 原始数据
	 * @param columnList 源表头
	 * @param type 重组类型，1：键值对的数据；其他：只返回值
	 * @return List
	 */
	List rebuiltData(List<DtSetCol> cols, List<List<Object>> dataList, Object[] columnList, int type);
}
