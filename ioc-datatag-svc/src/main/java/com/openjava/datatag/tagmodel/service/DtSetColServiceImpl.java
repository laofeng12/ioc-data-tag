package com.openjava.datatag.tagmodel.service;

import java.util.*;
import javax.annotation.Resource;
import javax.persistence.Column;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmodel.domain.*;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.repository.DtTagcolUpdateLogRepository;
import com.openjava.datatag.utils.EntityClassUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.query.DtSetColDBParam;
import com.openjava.datatag.tagmodel.repository.DtSetColRepository;
/**
 * 字段表业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtSetColServiceImpl implements DtSetColService {
	
	@Resource
	private DtSetColRepository dtSetColRepository;
	@Resource
	private DtTagConditionService dtTagConditionService;
	@Resource
	private DtTagcolUpdateLogRepository dtTagcolUpdateLogRepository;
	@Resource
	private DtTagConditionUpdateLogService DtTagConditionUpdateLogService;
	@Resource
	private DtTaggingModelService dtTaggingModelService;

	public Page<DtSetCol> query(DtSetColDBParam params, Pageable pageable){
		Page<DtSetCol> pageresult = dtSetColRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtSetCol> queryDataOnly(DtSetColDBParam params, Pageable pageable){
		return dtSetColRepository.queryDataOnly(params, pageable);
	}
	
	public DtSetCol get(Long id) {
		Optional<DtSetCol> o = dtSetColRepository.findById(id);
		if(o.isPresent()) {
			DtSetCol m = o.get();
			return m;
		}
		System.out.println("找不到记录DtSetCol："+id);
		return null;
	}
	
	public DtSetCol doSave(DtSetCol m) {
		return dtSetColRepository.save(m);
	}
	
	public void doDelete(Long id) throws Exception{
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtSetCol dtSetCol = get(id);
		if (dtSetCol==null||dtSetCol.getIsDeleted()== Constants.PUBLIC_YES){
			throw new APIException(MyErrorConstants.TAG_COL_NO_FIND,"无此字段或字段已删除");
		}
		if (!userInfo.getUserId().equals(dtSetCol.getCreateUser().toString())){
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"您没有权限");
		}
		//删除字段和克隆的字段并记录删除字段的记录
		List<DtSetCol>  clones = dtSetColRepository.getCloneClo(dtSetCol.getTaggingModelId(),dtSetCol.getSourceCol());
		clones.add(dtSetCol);
		List<Long> cloneCloIds = new ArrayList<>();
		clones.forEach(record->{
			//更新为删除状态
			record.setIsDeleted(Constants.PUBLIC_YES);
			EntityClassUtil.dealModifyInfo(record,userInfo);
			dtSetColRepository.save(record);
			cloneCloIds.add(record.getColId());
			//记录删除日志
			DtTagcolUpdateLog dtTagcolUpdateLog = new DtTagcolUpdateLog();
			EntityClassUtil.dealModifyInfo(dtTagcolUpdateLog,userInfo);
			dtTagcolUpdateLog.setColId(dtSetCol.getColId());
			dtTagcolUpdateLog.setModifyType(Constants.PUBLIC_MODIFY_TYPE_DELETE);
			dtTagcolUpdateLogRepository.save(dtTagcolUpdateLog);
		});
		//级联删除条件设置表
		List<DtTagCondition> conditions = dtTagConditionService.findByColIds(cloneCloIds);
		conditions.forEach(record->{
			record.setIsDeleted(Constants.PUBLIC_YES);
			EntityClassUtil.dealModifyInfo(record,userInfo);
			dtTagConditionService.doSave(record);
			//添加删除记录
			DtTagConditionUpdateLog conditionLog = new DtTagConditionUpdateLog();
			EntityClassUtil.dealModifyInfo(conditionLog,userInfo);
			conditionLog.setModifyType(Constants.PUBLIC_MODIFY_TYPE_DELETE);
			conditionLog.setTagConditionId(record.getTagConditionId());
			DtTagConditionUpdateLogService.doSave(conditionLog);
		});
	}
	public void doRemove(String ids) throws Exception{
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtSetColRepository.deleteById(new Long(items[i]));
		}
	}
	public List<DtSetCol> getByTaggingModelId(Long taggingModelId){
		return dtSetColRepository.getByTaggingModelId(taggingModelId);
	}
	/**
	 * 字段设置-确认选择
	 */
	public DtTaggingModelDTO selectCol(DtTaggingModelDTO body)throws Exception{
		String reqParams = JSONObject.toJSONString(body);//用来保存前端请求参数，保存在日志里，方便排查
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		if (body.getDataSetId() == null) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"打标目标表id不能为空");
		}
		if (StringUtils.isBlank(body.getDataSetName())) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"打标源表名称不能为空");
		}
		DtTaggingModel taggingModel = new DtTaggingModel();//模型
		List<DtSetCol> colList = body.getColList();//字段表
		//如果taggingModelId为空则新建模型
		if(body.getTaggingModelId()==null){
			body.setModelName("新建模型");
			body.setModelDesc("新建模型");
			body.setRunState(Constants.TG_MODEL_NO_BEGIN);//未开始
			body.setIsNew(true);//执行insert
			body.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
			MyBeanUtils.copyPropertiesNotNull(taggingModel,body);
			EntityClassUtil.dealCreateInfo(taggingModel,userInfo);
			taggingModel.setDataTableName("DT_"+RandomStringUtils.random(27,true,false).toUpperCase());//生成随机表名
			taggingModel = dtTaggingModelService.doSave(taggingModel);
		}else{
			taggingModel = dtTaggingModelService.get(body.getTaggingModelId());
			if (taggingModel==null) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此标签模型,taggingModelId无效");
			}
			if (!taggingModel.getDataSetId().equals(body.getDataSetId())) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"请选"+taggingModel.getDataSetName()+"进行字段选择");
			}
			MyBeanUtils.copyPropertiesNotNull(taggingModel,body);
			EntityClassUtil.dealModifyInfo(taggingModel,userInfo);
			taggingModel = dtTaggingModelService.doSave(taggingModel);
		}
		for (DtSetCol col:colList) {
			if (col.getColId()==null){
				List<DtSetCol> list = getBySourceColAndTaggingModelId(col.getSourceCol(),taggingModel.getTaggingModelId());
				if (CollectionUtils.isNotEmpty(list)) {
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"字段"+col.getSourceCol()+"已存在");
				}
				col.setIsHandle(Constants.PUBLIC_NO);//非手动打标字段
				col.setIsSource(Constants.PUBLIC_YES);//源字段
				col.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
				EntityClassUtil.dealCreateInfo(col,userInfo);
				col.setTaggingModelId(taggingModel.getTaggingModelId());
				col.setShowCol(col.getSourceCol());
				col = doSave(col);
			}else{
				DtSetCol model = get(col.getColId());
				if (!model.getTaggingModelId().equals(col.getTaggingModelId())) {
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"taggingModelId不匹配");
				}
				if (model == null){
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此字段，colId无效");
				}
				MyBeanUtils.copyPropertiesNotNull(model,col);
				EntityClassUtil.dealModifyInfo(model,userInfo);
				col = doSave(model);
			}
		}
		return body;
	}
	public List<DtSetCol>  getBySourceColAndTaggingModelId(String sourceCol,Long taggingModelId){
		return dtSetColRepository.getBySourceColAndTaggingModelId( sourceCol, taggingModelId);
	}

	/**
	 * 克隆字段
	 */
	public void clone(Long colId)throws Exception{
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtSetCol col = get(colId);
		DtSetCol clone = new DtSetCol();
		if (col==null) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此字段，colId无效");
		}
		List<DtSetCol> cols = getBySourceColAndTaggingModelId(col.getSourceCol(),col.getTaggingModelId());
		MyBeanUtils.copyProperties(clone,col);
		EntityClassUtil.dealCreateInfo(clone,userInfo);
		clone.setColId(null);
		clone.setIsNew(true);
		if (CollectionUtils.isNotEmpty(cols)) {
			clone.setShowCol("copy"+col.getSourceCol()+(cols.size()+1));
		}else{
			clone.setShowCol("copy"+col.getSourceCol()+"1");
		}
		doSave(clone);
	}
	public static void main(String[] args) {
		System.out.println(RandomStringUtils.random(27,true,false).toUpperCase());
	}
}
