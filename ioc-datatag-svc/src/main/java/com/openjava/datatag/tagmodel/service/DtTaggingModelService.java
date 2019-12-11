package com.openjava.datatag.tagmodel.service;

import java.util.List;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.dto.DtTaggingDispatchDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelCopyDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelRenameDTO;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.user.BaseUserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 标签模型业务层接口
 * @author zmk
 *
 */
public interface DtTaggingModelService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	Page<DtTaggingModel> query(DtTaggingModelDBParam params, Pageable pageable)throws Exception;

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTaggingModel> queryDataOnly(DtTaggingModelDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTaggingModel get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtTaggingModel doSave(DtTaggingModel m);
	//DtTaggingModel doNew(DtTaggingModel m,BaseUserInfo userInfo, String ip);

	/**
	 *
	 * @param m
	 * @param userInfo
	 * @param ip
	 * @return
	 * @throws APIException
	 */
	DtTaggingModel doNew(DtTaggingModelDTO m,BaseUserInfo userInfo, String ip) throws APIException;

	/**
	 *
	 * @param body
	 * @param db
	 * @param userInfo
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	DtTaggingModel doRename(DtTaggingModelRenameDTO body, DtTaggingModel db, BaseUserInfo userInfo, String ip)throws Exception;

	/**
	 *
	 * @param body
	 * @param db
	 * @param userId
	 * @param ip
	 * @throws Exception
	 */
	void doDispatch(DtTaggingDispatchDTO body, DtTaggingModel db, Long userId, String ip) throws Exception;

	/**
	 *
	 * @param id
	 */
	void doDelete(Long id);

	/**
	 *
	 * @param ids
	 */
	void doRemove(String ids);

	/**
	 * 克隆模型
	 * @param id
	 */
	void copy(DtTaggingModelCopyDTO id, String ip)throws Exception;

	/**
	 *
	 * @param taggingModel
	 * @param userId
	 * @param ip
	 * @throws Exception
	 */
	void doSoftDelete(DtTaggingModel taggingModel,Long userId,String ip)throws Exception;
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
	void calculation(DtTaggingModel tagModel);

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

	/**
	 * 停止模型核心代码（删除画像）
	 * @param taggingModelId
	 * @param colId
	 */
	void stopModel(Long taggingModelId,Long colId);
	/**
	 * 根据标签停止模型删除画像
	 */
	void stopModelByColIds(List<Long> colIds);

	/**
	 *
	 * @param number
	 * @param taggingModelId
	 * @return
	 * @throws Exception
	 */
	SuccessMessage beginDowload(Long number,Long taggingModelId)throws Exception;
}
