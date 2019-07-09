package com.openjava.datatag.tagmodel.service;

import java.util.*;
import javax.annotation.Resource;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.log.domain.DtTagcolUpdateLog;
import com.openjava.datatag.log.domain.DtTagmUpdateLog;
import com.openjava.datatag.log.repository.DtTagmUpdateLogRepository;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.tagmodel.domain.*;
import com.openjava.datatag.tagmodel.dto.DtTagConditionDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.log.repository.DtTagcolUpdateLogRepository;
import com.openjava.datatag.utils.EntityClassUtil;
import com.openjava.datatag.tagmodel.dto.GetHistoryColDTO;
import com.openjava.datatag.utils.EntityClassUtil;
import com.openjava.datatag.utils.TagConditionUtils;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import com.alibaba.fastjson.JSONObject;
import org.openjava.boot.conf.aop.ApiExceptionAOP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.query.DtSetColDBParam;
import com.openjava.datatag.tagmodel.repository.DtSetColRepository;
import org.springframework.web.bind.annotation.RequestBody;

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
	@Resource
	private DtTagGroupService dtTagGroupService;
	@Resource
	private DtTagService dtTagService;
	@Resource
	private SysCodeService sysCodeService;
	@Resource
	private DtFilterExpressionService dtFilterExpressionService;

	@Resource
	private DtTagmUpdateLogRepository dtTagmUpdateLogRepository;


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
	
	public void doDelete(Long id,String ip) throws Exception{
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


			//日志记录
			DtTagcolUpdateLog log = new DtTagcolUpdateLog();
			log.setId(ConcurrentSequence.getInstance().getSequence());
			log.setModifyUserip(ip);
			log.setModifyTime(dtSetCol.getModifyTime());
			log.setModifyUser(Long.valueOf(userInfo.getUserId()));
			log.setColId(dtSetCol.getColId());
			log.setModifyType(Constants.DT_TG_LOG_DELETE);
			//log.setModifyContent();//删除就不需要保存内容了
			log.setIsNew(true);
			dtTagcolUpdateLogRepository.save(log);

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
	public DtTaggingModelDTO selectCol(DtTaggingModelDTO body,String ip)throws Exception{
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
			//只有创建者有修改模型显示打标字段的权限
			if (! taggingModel.getCreateUser().equals(Long.parseLong(userInfo.getUserId()))){
				throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"无修改模型显示打标字段的权限");
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

		//日志记录
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(body.getModifyTime());
		log.setModifyUser(Long.valueOf(userInfo.getUserId()));
		log.setTaggingModelId(body.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);
		log.setModifyContent(reqParams);
		log.setIsNew(true);
		dtTagmUpdateLogRepository.save(log);

		return body;
	}
	public List<DtSetCol>  getBySourceColAndTaggingModelId(String sourceCol,Long taggingModelId){
		return dtSetColRepository.getBySourceColAndTaggingModelId( sourceCol, taggingModelId);
	}

	/**
	 * 克隆字段
	 */
	public void clone(Long colId,String ip)throws Exception{
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
		clone.setIsSource(Constants.PUBLIC_NO);//非源字段
		if (CollectionUtils.isNotEmpty(cols)) {
			clone.setShowCol("copy"+col.getSourceCol()+(cols.size()+1));
		}else{
			clone.setShowCol("copy"+col.getSourceCol()+"1");
		}
		doSave(clone);

		//日志记录
		DtTagcolUpdateLog log = new DtTagcolUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(clone.getModifyTime());
		log.setModifyUser(Long.valueOf(userInfo.getUserId()));
		log.setColId(clone.getColId());
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);
		log.setModifyContent("{\"from\":"+ col +"}");
		log.setIsNew(true);
		dtTagcolUpdateLogRepository.save(log);
	}

	/**
	 * 查询字段打标历史
	 */
	public GetHistoryColDTO getHistoryCol(Long colId)throws Exception{
		GetHistoryColDTO result = new GetHistoryColDTO();
		result.setColId(colId);
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		result.setTagGroups(dtTagGroupService.getMyTagGroup(Long.valueOf(userInfo.getUserId())));//默认展示的标签组
		List<DtTagCondition> conditions = dtTagConditionService.findByColId(colId);
		List<DtTagConditionDTO> conditionsDTOs = new ArrayList<>();
		conditions.forEach(record->{
			DtTagConditionDTO dto = new DtTagConditionDTO();
			MyBeanUtils.copyPropertiesNotNull(dto,record);
			List<DtFilterExpression> conditionSetting = dtFilterExpressionService.findByTagConditionId(record.getTagConditionId());
			dto.setConditionSetting(conditionSetting);
			conditionsDTOs.add(dto);
		});
		result.setCondtion(conditionsDTOs);//打标的条件设置列表
		if (CollectionUtils.isNotEmpty(conditions)) {
			DtTagCondition condition =conditions.get(0);
			DtTag selectTag = dtTagService.get(condition.getTagId());//用于前端展示选中的（默认用第一个）
			DtTagGroup selectTagGroups = dtTagGroupService.get(selectTag.getTagsId());//用于前端展示选中的
			List<DtTag> tagList= dtTagService.findByTagsId(selectTagGroups.getId());//默认选中标签组的所有标签
			result.setSelectTags(selectTag);
			result.setSelectTagGroup(selectTagGroups);
			result.setTags(tagList);
		}
		return result;
	}
	/**
	 *  确认打标保存接口
	 */
	public void saveCondition(GetHistoryColDTO req)throws Exception{
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		List<DtTagConditionDTO> saveconditions = req.getCondtion();
		List<DtTagCondition> conditions = dtTagConditionService.findByColId(req.getColId());
		if (CollectionUtils.isEmpty(saveconditions)) {
			return ;
		}
		if (get(req.getColId())==null) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此字段，colId参数错误");
		}
		for (int i = 0; i < saveconditions.size(); i++) {
			DtTagConditionDTO record =saveconditions.get(i);
			//校验条件是否合法
			String filterExpression = null;
			try {
				filterExpression= check(req.getColId(),record);
			}catch (Exception e){
				e.printStackTrace();
				throw new APIException(MyErrorConstants.TAG_TAGGING_GRAMMAR_ERROR,"条件设置语法错误:"+i);
			}
			//新增和修改
			DtTagCondition newTagCondition = new DtTagCondition();
			if (record.getColId()!=null) {
				if (!record.getColId().equals(req.getColId())) {
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"conditions参数的colI错误");
				}
				DtTagCondition dbData = dtTagConditionService.get(record.getTagConditionId());
				MyBeanUtils.copyPropertiesNotNull(dbData,record);
				EntityClassUtil.dealModifyInfo(dbData,userInfo);
				dbData.setFilterExpression(filterExpression);
				dtTagConditionService.doSave(dbData);
			}else{
				MyBeanUtils.copyPropertiesNotNull(newTagCondition,record);
				EntityClassUtil.dealCreateInfo(newTagCondition,userInfo);
				newTagCondition.setFilterExpression(filterExpression);
				dtTagConditionService.doSave(newTagCondition);
			}
			//规制表达式表
			dtFilterExpressionService.doRemoveByTagConditionId(record.getTagConditionId());
			for (int j = 0; j < record.getConditionSetting().size(); j++) {
				DtFilterExpression expression = record.getConditionSetting().get(j);
				expression.setSort(j);
			}

		}
		//删除
		for (DtTagCondition record:conditions) {
			if (!saveconditions.contains(record)){
				doDelete(record.getId(),null);
			}
		}
	}

	/**
	 * 校验条件是否合法
	 */
	public String check(Long colId,DtTagConditionDTO condtion) throws Exception{
		DtSetCol col =  get(colId);
		if (condtion==null) {
			return null;
		}
		String checkSql = " ";
		String resultSql = "" ;
		List<DtFilterExpression> list = condtion.getConditionSetting();
		for (int j = 0; j < list.size() ; j++) {
			DtFilterExpression expression = list.get(j);
			if (expression.getSymbol()!=null && expression.getIsConnectSymbol().equals(Constants.PUBLIC_YES)) {
				checkSql += expression.getSymbol();
				resultSql += expression.getSymbol();
			}else{
				if (StringUtils.isNotBlank(expression.getSymbol())) {
					if (expression.getValuesType()==null) {
						throw new APIException(MyErrorConstants.PUBLIC_ERROE,"数据类型为空");
					}
					if (TagConditionUtils.isIntType(expression.getValuesType())) {
						checkSql += " TAG_CONDITION_ID "+TagConditionUtils.toSqlSymbol(expression.getSymbol());
					}else{
						checkSql += " SHOW_COL "+TagConditionUtils.toSqlSymbol(expression.getSymbol());
					}
					resultSql += col.getShowCol() +TagConditionUtils.toSqlSymbol(expression.getSymbol());
				}
				if (StringUtils.isBlank(expression.getTheValues())) {
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"值不能为空");
				}
				String values[] = expression.getTheValues().split(",");
				for (int k = 0; k < values.length; k++) {
					checkSql += " "+TagConditionUtils.initValues(values[k],expression.getValuesType(),expression.getSymbol())+" ";
					resultSql += " "+TagConditionUtils.initValues(values[k],expression.getValuesType(),expression.getSymbol())+" ";
				}
			}
		}
		dtSetColRepository.check(checkSql);
		return resultSql;
	}
	public static void main(String[] args) {
		System.out.println(RandomStringUtils.random(27,true,false).toUpperCase());
		String kk = null;
		System.out.println("sss"+kk);
	}
}
