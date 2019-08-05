package com.openjava.datatag.tagmodel.service;

import java.util.*;
import javax.annotation.Resource;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;

import com.openjava.datatag.log.service.DtTagcolUpdateLogService;
import com.openjava.datatag.log.service.DtTagmUpdateLogService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.tagmodel.domain.*;
import com.openjava.datatag.tagmodel.dto.*;
import com.openjava.datatag.utils.EntityClassUtil;
import com.openjava.datatag.utils.TagConditionUtils;
import com.openjava.framework.sys.service.SysCodeService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
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
	private DtTagcolUpdateLogService dtTagcolUpdateLogService;

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
	private DtTagmUpdateLogService dtTagmUpdateLogService;


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
		if (dtSetCol==null||dtSetCol.getIsDeleted().equals(Constants.PUBLIC_YES)){
			throw new APIException(MyErrorConstants.TAG_COL_NO_FIND,"无此字段或字段已删除");
		}
		if (!userInfo.getUserId().equals(dtSetCol.getCreateUser().toString())){
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"您没有权限");
		}
		//删除字段
		dtSetCol.setIsDeleted(Constants.PUBLIC_YES);
		EntityClassUtil.dealModifyInfo(dtSetCol,userInfo);
		dtSetColRepository.save(dtSetCol);

		//级联删除条件设置表
		List<DtTagCondition> conditions = dtTagConditionService.findByColId(dtSetCol.getColId());
		conditions.forEach(record->{
			record.setIsDeleted(Constants.PUBLIC_YES);
			EntityClassUtil.dealModifyInfo(record,userInfo);
			dtTagConditionService.doSave(record);
		});
		String content = "{\"delConditions\":" + JSONObject.toJSONString(conditions)+"}";
		//记录（打标显示）字段的删除
		dtTagcolUpdateLogService.loggingDelete(content,dtSetCol,ip);
	}
	public void doRemove(String ids) throws Exception{
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtSetColRepository.deleteById(new Long(items[i]));
		}
	}
	public List<DtSetCol> getByTaggingModelId(Long taggingModelId){
		return dtSetColRepository.getByTaggingModelIdAndIsDeleted(taggingModelId,Constants.PUBLIC_NO);
	}
	/**
	 * 字段设置-确认选择
	 */
	public DtTaggingModelDTO selectCol(DtTaggingModelDTO body,String ip)throws Exception{
		String reqParams = JSONObject.toJSONString(body);//用来保存前端请求参数，保存在日志里，方便排查
		List<DtSetCol> removeCols = new ArrayList<>(); //保存软删除的显示列
		String oldtagModelContent = null;
		String oldColsContent = null;//保存旧的显示列

		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtTaggingModel taggingModel = new DtTaggingModel();//模型
		List<DtSetCol> colList = body.getColList();//字段表

		//如果taggingModelId为空则新建模型
		if(body.getTaggingModelId()==null){
			taggingModel = dtTaggingModelService.doNew(body,userInfo,ip);
		}else{
			taggingModel = dtTaggingModelService.get(body.getTaggingModelId());
			if (taggingModel==null) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此标签模型,taggingModelId无效");
			}
			//只有创建者有修改模型显示打标字段的权限
			if (! taggingModel.getCreateUser().equals(Long.parseLong(userInfo.getUserId()))){
				throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"无修改模型显示打标字段的权限");
			}
			if (!taggingModel.getResourceId().equals(body.getResourceId())) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"请选"+taggingModel.getResourceName()+"进行字段选择");
			}
			//记录以前的模样
			oldColsContent = JSONObject.toJSONString(getByTaggingModelId(taggingModel.getTaggingModelId()));
			oldtagModelContent = JSONObject.toJSONString(taggingModel);

			MyBeanUtils.copyPropertiesNotNull(taggingModel,body);
			EntityClassUtil.dealModifyInfo(taggingModel,userInfo);
			taggingModel = dtTaggingModelService.doSave(taggingModel);

			//处理 显示/打标列部分
			List<DtSetCol> dbSourceSetCol = getSourceSetColByTaggingModelId(taggingModel.getId());
			//遍历修改/新增 显示/打标列
			for (DtSetCol col : colList){
				//新建
				if (col.getColId()==null){
					List<DtSetCol> list = getSourceSetColBySourceColAndTaggingModelId(col.getSourceCol(),taggingModel.getTaggingModelId());
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
					DtSetCol oldCol = get(col.getColId());
					//移除掉报文里有的，剩下被取消掉的
					dbSourceSetCol.remove(oldCol);
					if (oldCol == null){
						throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此字段，colId无效");
					}
					if (!oldCol.getTaggingModelId().equals(col.getTaggingModelId())) {
						throw new APIException(MyErrorConstants.PUBLIC_ERROE,"taggingModelId不匹配");
					}
					MyBeanUtils.copyPropertiesNotNull(oldCol,col);
					EntityClassUtil.dealModifyInfo(oldCol,userInfo);
					col = doSave(oldCol);
					//显示列未勾选 允许打标的，克隆的列删除
					if (col.getIsMarking().equals(Constants.PUBLIC_NO)){
						List<DtSetCol> delCols =
								getBySourceColAndTaggingModelId(taggingModel.getTaggingModelId(),col.getSourceCol());
						for (DtSetCol delCol:delCols){
							if (!delCol.getColId().equals(col.getColId())){
								doDelete(delCol.getColId(),ip);
								removeCols.add(delCol);//日志记录
							}
						}
					}
				}
			}
			//未勾选显示列的(包括其复制列)，全部删除
			for (DtSetCol delSCol: dbSourceSetCol){
				if(delSCol.getSourceCol().equals(taggingModel.getPkey())){
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"主键必须勾选！");
				}
				List<DtSetCol> delCols =
						getBySourceColAndTaggingModelId(taggingModel.getTaggingModelId(),delSCol.getSourceCol());
				for (DtSetCol delCol:delCols){
					doDelete(delCol.getColId(),ip);
				}
				removeCols.addAll(delCols);//日志记录
			}
			//日志记录
			String content = "{\"reqParam\":" + reqParams + ",\"removeCol\":" + JSONObject.toJSONString(removeCols) + "}";
			String oldContent = "{\"model\":" + oldtagModelContent + ",\"setCol\":"+ oldColsContent +"}";
			dtTagmUpdateLogService.loggingUpdate(content,oldContent,taggingModel,ip);
		}
		body.setTaggingModelId(taggingModel.getTaggingModelId());
		return body;
	}


	public  List<DtSetCol> getBySourceColAndTaggingModelId(Long taggingModelId,String SourceCol){
		return dtSetColRepository
				.getByTaggingModelIdAndSourceColAndIsDeleted(taggingModelId,SourceCol,Constants.PUBLIC_NO);
	}

	public List<DtSetCol>  getSourceSetColBySourceColAndTaggingModelId(String sourceCol,Long taggingModelId){
		return dtSetColRepository
				.getBySourceColAndTaggingModelIdAndIsSourceAndIsDeleted(sourceCol, taggingModelId,Constants.PUBLIC_YES,Constants.PUBLIC_NO);
	}
	public List<DtSetCol> getSourceSetColByTaggingModelId(Long taggingModelId){
		return dtSetColRepository
				.getByTaggingModelIdAndIsSourceAndIsDeleted(taggingModelId,Constants.PUBLIC_YES,Constants.PUBLIC_NO);
	}

	//获取打标列(包括克隆)
	public List<DtSetCol> getMarkingSetColByTaggingModelId(Long taggingModelId){
		return dtSetColRepository
				.getByTaggingModelIdAndIsMarkingAndIsDeleted(taggingModelId,Constants.PUBLIC_YES,Constants.PUBLIC_NO);
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
		List<DtSetCol> cols = getSourceSetColBySourceColAndTaggingModelId(col.getSourceCol(),col.getTaggingModelId());
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
		dtTagcolUpdateLogService.loggingNew("{ \"cloneFrom\":" + col + "}",clone,ip);

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
			List<SaveConditionDtFilterExpressionDTO> conditionSettingDTO = new ArrayList<>();
			conditionSetting.forEach(setting->{
				SaveConditionDtFilterExpressionDTO settingDTO = new SaveConditionDtFilterExpressionDTO();
				MyBeanUtils.copyProperties(settingDTO,setting);
				conditionSettingDTO.add(settingDTO);
			});
			dto.setConditionSetting(conditionSettingDTO);
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
	public void saveCondition(SaveConditionDTO req)throws Exception{
		String reqParams = JSONObject.toJSONString(req);
        //记录新增的日志
        List<DtTagCondition> addLog = new ArrayList<>();
        //记录删除的日志
        List<DtTagCondition> delLog = new ArrayList<>();
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		List<DtTagConditionDTO> saveconditions = req.getCondtion();
		DtSetCol col = get(req.getColId());
		List<DtTagCondition> conditions = dtTagConditionService.findByColId(req.getColId());
		if (CollectionUtils.isEmpty(saveconditions)) {
			return ;
		}
		if (col==null) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此字段，colId参数错误");
		}
		for (int i = 0; i < saveconditions.size(); i++) {
			DtTagConditionDTO record =saveconditions.get(i);
			if (record.getTagId()==null||dtTagService.get(record.getTagId())==null) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"condtion参数的tagId为空或查无此标签");
			}
			if (!record.getColId().equals(req.getColId())) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"conditions参数的colI错误");
			}
			//校验条件是否合法
			String filterExpression = null;
			try {
				filterExpression= check(req.getColId(),record);
			}catch (Exception e){
				e.printStackTrace();
				throw new APIException(MyErrorConstants.TAG_TAGGING_GRAMMAR_ERROR,"条件设置语法错误:"+i);//i参数可以告知前端第几个条件设置错误了
			}

			//新增和修改
			DtTagCondition newTagCondition = new DtTagCondition();
			if (record.getTagConditionId()!=null) {
				newTagCondition = dtTagConditionService.get(record.getTagConditionId());
				MyBeanUtils.copyPropertiesNotNull(newTagCondition,record);
				EntityClassUtil.dealModifyInfo(newTagCondition,userInfo);
				newTagCondition.setFilterExpression(filterExpression);
				newTagCondition = dtTagConditionService.doSave(newTagCondition);
			}else{
				MyBeanUtils.copyPropertiesNotNull(newTagCondition,record);
				EntityClassUtil.dealCreateInfo(newTagCondition,userInfo);
				newTagCondition.setFilterExpression(filterExpression);
				newTagCondition.setIsDeleted(Constants.PUBLIC_NO);
				newTagCondition.setSourceCol(col.getSourceCol());
				newTagCondition.setShowCol(col.getShowCol());
				newTagCondition = dtTagConditionService.doSave(newTagCondition);
				addLog.add(newTagCondition);
			}
			//重新保存规制达式表
			dtFilterExpressionService.doRemoveByTagConditionId(newTagCondition.getTagConditionId());
			for (int j = 0; j < record.getConditionSetting().size(); j++) {
				SaveConditionDtFilterExpressionDTO expressionDTO = record.getConditionSetting().get(j);
				DtFilterExpression expression = new DtFilterExpression();
				MyBeanUtils.copyProperties(expression,expressionDTO);
				expression.setTagConditionId(newTagCondition.getTagConditionId());
				expression.setSort(j);
				expression.setIsNew(true);
				dtFilterExpressionService.doSave(expression);
			}
		}

		//删除
		for (DtTagCondition record:conditions) {
			DtTagConditionDTO d = new  DtTagConditionDTO();
			d.setTagConditionId(record.getTagConditionId());
			if (!saveconditions.contains(d)){
				delLog.add(record);
				dtTagConditionService.doDelete(record.getId());
			}
		}

		EntityClassUtil.dealModifyInfo(col,userInfo);
		String content = "{\"req\":" + reqParams
                + ",\"delCondition\":" + JSONObject.toJSONString(delLog)
                + ",\"addCondition\":" + JSONObject.toJSONString(addLog) + "}";
		dtTagcolUpdateLogService.loggingUpdate(content,col,req.getIp());
	}

	/**
	 * 校验条件是否合法并返回sql语句
	 */
	public String check(Long colId,DtTagConditionDTO condtion) throws Exception{
		DtSetCol col =  get(colId);
		if (condtion==null) {
			return null;
		}
		String checkSql = " ";
		String resultSql = "" ;
		List<SaveConditionDtFilterExpressionDTO> list = condtion.getConditionSetting();
		for (int j = 0; j < list.size() ; j++) {
			SaveConditionDtFilterExpressionDTO expression = list.get(j);
			//自动打标
			if (condtion.getIsHandle()!=null&&condtion.getIsHandle().equals(Constants.PUBLIC_YES)) {
				if (TagConditionUtils.isIntType(expression.getValuesType())) {
					checkSql += " TAG_CONDITION_ID "+TagConditionUtils.toSqlSymbol(expression.getSymbol())+" ";
				}else if (TagConditionUtils.isDateType(expression.getValuesType())){
					checkSql += " CREATE_TIME "+TagConditionUtils.toSqlSymbol(expression.getSymbol())+" ";
				}else {
					checkSql += " SHOW_COL "+TagConditionUtils.toSqlSymbol(expression.getSymbol())+" ";
				}
				resultSql += col.getShowCol() +" "+TagConditionUtils.toSqlSymbol(expression.getSymbol())+" ";
				if (StringUtils.isBlank(expression.getTheValues())) {
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"值不能为空");
				}
				checkSql+=" ( ";
				resultSql+=" ( ";
				String values[] = expression.getTheValues().split(",");
				for (int k = 0; k < values.length; k++) {
					checkSql += " "+TagConditionUtils.initValues(values[k],expression.getValuesType(),expression.getSymbol(),TagConditionUtils.DB_TYPE_ORACLE)+" ";
					resultSql += " "+TagConditionUtils.initValues(values[k],expression.getValuesType(),expression.getSymbol(),TagConditionUtils.DB_TYPE_PG)+" ";
					if (k!=values.length-1) {
						checkSql+=",";
						resultSql+=",";
					}
				}
				checkSql+=" ) ";
				resultSql+=" ) ";
			}else {
				if (expression.getSymbol()!=null && expression.getIsConnectSymbol().equals(Constants.PUBLIC_YES)) {
					checkSql += expression.getSymbol();
					resultSql += expression.getSymbol();
				}else{
					//运算符
					if (StringUtils.isNotBlank(expression.getSymbol())) {
						if (expression.getValuesType()==null) {
							throw new APIException(MyErrorConstants.PUBLIC_ERROE,"数据类型为空");
						}
						if (TagConditionUtils.isIntType(expression.getValuesType())) {
							checkSql += " TAG_CONDITION_ID "+TagConditionUtils.toSqlSymbol(expression.getSymbol())+" ";
						} else if (TagConditionUtils.isDateType(expression.getValuesType())) {
							checkSql += " CREATE_TIME "+TagConditionUtils.toSqlSymbol(expression.getSymbol())+" ";
						} else {
							checkSql += " SHOW_COL " + TagConditionUtils.toSqlSymbol(expression.getSymbol()) + " ";
						}
						resultSql += " "+col.getShowCol() +" "+TagConditionUtils.toSqlSymbol(expression.getSymbol());
					}
					if (StringUtils.isBlank(expression.getTheValues())) {
						throw new APIException(MyErrorConstants.PUBLIC_ERROE,"值不能为空");
					}
					String values[] = expression.getTheValues().split(",");
					for (int k = 0; k < values.length; k++) {
						checkSql += " "+TagConditionUtils.initValues(values[k],expression.getValuesType(),expression.getSymbol(),TagConditionUtils.DB_TYPE_ORACLE)+" ";
						resultSql += " "+TagConditionUtils.initValues(values[k],expression.getValuesType(),expression.getSymbol(),TagConditionUtils.DB_TYPE_PG)+" ";
					}
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
