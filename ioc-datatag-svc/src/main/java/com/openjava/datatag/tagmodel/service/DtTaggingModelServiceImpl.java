package com.openjava.datatag.tagmodel.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.log.service.DtTagmUpdateLogService;
import com.openjava.datatag.schedule.domain.TaskInfo;
import com.openjava.datatag.schedule.service.TaskService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.tagmodel.domain.DtFilterExpression;
import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.dto.*;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;
import com.openjava.datatag.tagmodel.repository.DtSetColRepository;
import com.openjava.datatag.tagmodel.repository.DtTagConditionRepository;
import com.openjava.datatag.tagmodel.repository.DtTaggingModelRepository;
import com.openjava.datatag.utils.EntityClassUtil;
import com.openjava.datatag.utils.MyTimeUtil;
import com.openjava.datatag.utils.TagConditionUtils;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 标签模型业务层
 * @author zmk
 *
 */
@Service
@Transactional
@Data
public class DtTaggingModelServiceImpl implements DtTaggingModelService {
	Logger logger = LogManager.getLogger(getClass());
	@Resource
	private DtTaggingModelRepository dtTaggingModelRepository;
	@Resource
	private DtSetColRepository dtSetColRepository;
	@Resource
	private DtTagService dtTagService;
	@Resource
	private DtTagConditionRepository dtTagConditionRepository;
	@Resource
	private DtFilterExpressionService dtFilterExpressionService;
	@Resource
	private DtTagmUpdateLogService dtTagmUpdateLogService;
	@Resource
	private TaskService taskService;
	@Resource
	private DtSetColService dtSetColService;
	@Resource
	private DtTagConditionService dtTagConditionService;

	private static String colJson ="";

	private static String valueJson="";

	public void copy(Long id,String ip)throws Exception{
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtTaggingModel model = get(id);
		if (model==null) {
			throw new APIException(MyErrorConstants.TAG_MODEL_NO_FIND,"此Id查无模型");
		}
		String content = JSONObject.toJSONString(model);

		//克隆模型
		DtTaggingModelDTO tempDTO = new DtTaggingModelDTO();
		DtTaggingModel clone = new DtTaggingModel();
		BeanUtilsBean.getInstance().getConvertUtils()
				.register(new SqlDateConverter(null), Date.class);//解决时间空复制时出现异常
		BeanUtils.copyProperties(tempDTO,model);
		BeanUtils.copyProperties(clone,tempDTO);
		clone.setTaggingModelId(null);
		EntityClassUtil.dealCreateInfo(clone,userInfo);
		clone = doSave(clone);
		clone.setDataTableName("DT_"+clone.getTaggingModelId());//@TODO 这里要注意
		//克隆字段
		List<DtSetCol> cloList =
				dtSetColRepository.getByTaggingModelIdAndIsDeleted(model.getTaggingModelId(),Constants.PUBLIC_NO);
		for (int i = 0; i <cloList.size() ; i++) {
			DtSetCol record = cloList.get(i);
			DtSetColDTO colDTO = new DtSetColDTO();
			DtSetCol colClone = new DtSetCol();
			BeanUtils.copyProperties(colDTO,record);
			BeanUtils.copyProperties(colClone,colDTO);
			colClone.setColId(null);
			colClone.setTaggingModelId(clone.getTaggingModelId());
			EntityClassUtil.dealCreateInfo(colClone,userInfo);
			colClone = dtSetColRepository.save(colClone);
			List<DtTagCondition> conditions = dtTagConditionRepository.findByColId(record.getColId());
			//克隆字段条件设置
			for (int j = 0; j <conditions.size() ; j++) {
				DtTagCondition condition = conditions.get(j);
				DtTagConditionDTO conditionDTO = new DtTagConditionDTO();
				DtTagCondition conditionClone =new DtTagCondition();
				BeanUtils.copyProperties(conditionDTO,condition);
				BeanUtils.copyProperties(conditionClone,conditionDTO);
				conditionClone.setTagConditionId(null);
				conditionClone.setColId(colClone.getColId());
				EntityClassUtil.dealCreateInfo(conditionClone,userInfo);
				conditionClone = dtTagConditionRepository.save(conditionClone);
				//克隆规制表达式
				List<DtFilterExpression> expressions = dtFilterExpressionService.findByTagConditionId(condition.getTagConditionId());
				for (DtFilterExpression expression: expressions) {
					DtFilterExpression copyExpression = new DtFilterExpression();
					CopyDtFilterExpressionDTO copyExpressionDTO = new CopyDtFilterExpressionDTO();
					BeanUtils.copyProperties(copyExpressionDTO,expression);
					BeanUtils.copyProperties(copyExpression,copyExpressionDTO);
					copyExpression.setFilterExpressionID(null);
					copyExpression.setTagConditionId(conditionClone.getTagConditionId());
					dtFilterExpressionService.doSave(copyExpression);
				}
			}
		}

		//日志记录
		dtTagmUpdateLogService.loggingNew("{ \"from\": "+content+"}",clone,ip);

	}


	public Page<DtTaggingModel> query(DtTaggingModelDBParam params, Pageable pageable){
		Page<DtTaggingModel> pageresult = dtTaggingModelRepository.query(params, pageable);
		return pageresult;
	}

	public List<DtTaggingModel> queryDataOnly(DtTaggingModelDBParam params, Pageable pageable){
		return dtTaggingModelRepository.queryDataOnly(params, pageable);
	}

	public DtTaggingModel get(Long id) {
		Optional<DtTaggingModel> o = dtTaggingModelRepository.findById(id);
		if(o.isPresent()) {
			DtTaggingModel m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTaggingModel："+id);
		return null;
	}

	public DtTaggingModel doSave(DtTaggingModel m) {
		return dtTaggingModelRepository.save(m);
	}

	public DtTaggingModel doNew(DtTaggingModel body,BaseUserInfo userInfo,String ip){
		String content = JSONObject.toJSONString(body);
		EntityClassUtil.dealCreateInfo(body,userInfo);
		if (body.getModelName() == null){
			body.setModelName("新建模型");
		}
		if (body.getModelDesc() == null){
			body.setModelDesc("新建模型");
		}
		body.setRunState(Constants.TG_MODEL_NO_BEGIN);//未开始
		body.setIsNew(true);//执行insert
		body.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
		DtTaggingModel dbObj = dtTaggingModelRepository.save(body);

		//日志记录
		dtTagmUpdateLogService.loggingNew(content,dbObj,ip);

		return dbObj;
	}

	public DtTaggingModel doNew(DtTaggingModelDTO body,BaseUserInfo userInfo,String ip) throws APIException {
		String reqParams = JSONObject.toJSONString(body);
		List<DtSetCol> colList = body.getColList();//字段表
		if (body.getModelName() == null){
			body.setModelName("新建模型");
		}
		if (body.getModelDesc() == null){
			body.setModelDesc("新建模型");
		}
		body.setRunState(Constants.TG_MODEL_NO_BEGIN);//未开始
		body.setIsNew(true);//执行insert
		body.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
		DtTaggingModel taggingModel = new DtTaggingModel();
		MyBeanUtils.copyPropertiesNotNull(taggingModel,body);
		EntityClassUtil.dealCreateInfo(taggingModel,userInfo);
		taggingModel = doSave(taggingModel);
		taggingModel.setDataTableName("DT_"+ taggingModel.getTaggingModelId());//RandomStringUtils.random(27,true,false).toUpperCase()不合适，用模型id为后缀,这样比较有意义，表名就能看出是哪个模型的
		taggingModel = doSave(taggingModel);
		for (DtSetCol col :colList){
			if ((!col.isNew()) || col.getColId() != null){
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"新建模型时选择字段必须为新");
			}
			col.setIsSource(Constants.PUBLIC_YES);//源字段
			col.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
			EntityClassUtil.dealCreateInfo(col,userInfo);
			col.setTaggingModelId(taggingModel.getTaggingModelId());
			col.setShowCol(col.getSourceCol());
			col = dtSetColRepository.save(col);
		}
		dtTagmUpdateLogService.loggingNew(reqParams,taggingModel,ip);
		return taggingModel;
	}


	public DtTaggingModel doUpdate(DtTaggingModel body,DtTaggingModel db,BaseUserInfo userInfo,String ip){
		String oldContent = JSONObject.toJSONString(db);
		String modifyContent = JSONObject.toJSONString(body);
		EntityClassUtil.dealModifyInfo(db,userInfo);
		MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);//执行update
		DtTaggingModel dbObj =dtTaggingModelRepository.save(db);

		//日志记录
		dtTagmUpdateLogService.loggingUpdate(modifyContent,oldContent,db,ip);

		return dbObj;
	}


	/*
	 * 设置调度
	 */
	public void doDispatch(DtTaggingDispatchDTO body, DtTaggingModel db, Long userId, String ip) throws APIException {
		String oldContent = JSONObject.toJSONString(db);
		String modifyContent = JSONObject.toJSONString(body);
		db.setModifyTime(new Date());
		db.setModifyUser(userId);
		//检查Cycle 字段是否合理
		if(checkCycle(body.getCycleEnum())){
			if (body.getCycleEnum().equals(Constants.DT_DISPATCH_STOP)){
				db.setRunState(Constants.TG_MODEL_END);
				db.setStartTime(null);
				db.setCycle(null);
				db.setCycleEnum(null);
			}else{
				if (body.getStartTime() == null){
					throw new APIException(MyErrorConstants.TAGM_DISPATCH_NONE_START_TIME,"未设置开始时间");
				}
				db.setRunState(Constants.TG_MODEL_WAIT);
				db.setStartTime(body.getStartTime());
				db.setCycle(toCron(body.getCycleEnum(),body.getStartTime()));
				db.setCycleEnum(body.getCycleEnum());
			}
		}else{
			throw new APIException(MyErrorConstants.TAGM_DISPATCH_CYCLE_ERROR,"Cycle不合法");
		}

		dtTaggingModelRepository.save(db);

		//日志记录
		dtTagmUpdateLogService.loggingUpdate(modifyContent,oldContent,db,ip);
	}

	//检查Cycle是否合法
	private boolean checkCycle(Long cycle){
		return cycle >= Constants.DT_DISPATCH_STOP && cycle <= Constants.DT_DISPATCH_EACH_YEAR;
	}

	//将前端传进来的数据转换成cron表达式
	private String toCron(Long cycle,Date startTime) throws APIException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		int second = cal.get(Calendar.SECOND);
		int minute = cal.get(Calendar.MINUTE);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH)+1;
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		int year = cal.get(Calendar.YEAR);
		String[] cron = {"*","*","*","?","*","?","*"};
		cron[0] = String.valueOf(second);
		cron[1] = String.valueOf(minute);
		cron[2] = String.valueOf(hour);
		if (cycle.equals(Constants.DT_DISPATCH_ONCE)) {
			cron[0] = String.valueOf(second);
			cron[1] = String.valueOf(minute);
			cron[2] = String.valueOf(hour);
			cron[3]=String.valueOf(dayOfMonth);
			cron[4] = String.valueOf(month);
			cron[6] = String.valueOf(year) + "/20";//设置大一点（20年一次）就可以看成只执行一次
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_DAY)){
			//月份中的某一天
			cron[3] = "*";
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_WEEK)){
			cron[3]="?";
			cron[5] = MyTimeUtil.getWeekStr(startTime); // @TODO dayOfMonth/7 不准确
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_MONTH)){
		    cron[3] = String.valueOf(dayOfMonth);
			cron[4] = String.valueOf(month)+"/1";
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_YEAR)){
			cron[3]=String.valueOf(dayOfMonth);
			cron[4] = String.valueOf(month);
//			cron[6] = String.valueOf(year) + "/1";//
		}else {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"cycle in toCron must in{DT_DISPATCH_EACH_DAY/WEEK/MONTH/YEAR}");
		}
        StringBuilder cronEx = new StringBuilder();
		for (int i=0;i<cron.length;i++){
			if (i==0){
				cronEx.append(cron[i]);
			}else{
				cronEx.append(" ").append(cron[i]);
			}
		}
		return cronEx.toString();
	}

	public void doSoftDelete(DtTaggingModel db,Long userId,String ip){
		Date now = new Date();
		List<DtSetCol> list = dtSetColRepository.getByTaggingModelIdAndIsDeleted(db.getId(),Constants.PUBLIC_NO);
		for (DtSetCol col: list){
			dtTagConditionRepository.doSoftDeleteByColId(col.getColId(),now,db.getCreateUser());
		}
		dtSetColRepository.doSoftDeleteByTaggingModelId(db.getId(),now,db.getCreateUser());
		db.setIsDeleted(Constants.PUBLIC_YES);
		db.setModifyTime(now);
		dtTaggingModelRepository.save(db);

		//日志记录
		dtTagmUpdateLogService.loggingDelete(db, ip);
	}

	public void doDelete(Long id) {
		dtTaggingModelRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTaggingModelRepository.deleteById(new Long(items[i]));
		}
	}
	/**
	 * 根据runState获取型
	 */
	public List<DtTaggingModel> getModelByRunStates(List<Long> runStates){
		List<DtTaggingModel> waitList = dtTaggingModelRepository.getModelByRunStates(runStates);
		return waitList;
	}
	/**
	 * 设置模型调度的方法（核心方法）
	 */
	public void setToJob(){
		List<Long> allRunState = new ArrayList<>();
		allRunState.add(Constants.TG_MODEL_WAIT);
		allRunState.add(Constants.TG_MODEL_RUNNING);
		allRunState.add(Constants.TG_MODEL_SUCCESS);
		allRunState.add(Constants.TG_MODEL_ERROR);
		allRunState.add(Constants.TG_MODEL_END);
		List<DtTaggingModel> allModels =  getModelByRunStates(allRunState);
		Map<Long, List<DtTaggingModel>> modelGroup = allModels.stream().collect(Collectors.groupingBy(DtTaggingModel::getRunState));
		//等待执行调度的任务
		List<DtTaggingModel> waitModels =  modelGroup.get(Constants.TG_MODEL_WAIT);
		if (CollectionUtils.isNotEmpty(waitModels)) {
			for (DtTaggingModel model:waitModels) {
				try {
					//新建
					TaskInfo taskInfo = new TaskInfo();
					taskInfo.setJobGroup(Constants.DT_SCHEDULE_GROUP);//必传
					taskInfo.setJobName(model.getTaggingModelId().toString());//必传
					taskInfo.setCronExpression(model.getCycle());//必传
					taskInfo.setJobMethod(Constants.DT_SCHEDULE_CORE_JOB_CLASS);//必传
					taskInfo.setCreateTime(new Date().toString());
					if (!taskService.checkExists(model.getTaggingModelId().toString(),Constants.DT_SCHEDULE_GROUP)) {
						taskInfo.setId(1);//1为新增0为修改
						taskService.addJob(taskInfo);// @TODO 新增
					}else{
						//任务修改或者删除
						taskInfo.setId(0);//1为新增0为修改
						List<TaskInfo> taskInfos =  taskService.list();
						for (TaskInfo t:taskInfos) {
							if (t.getJobName().equals(taskInfo.getJobName()) && !t.getCronExpression().equals(taskInfo.getCronExpression())) {
								taskService.edit(taskInfo);// @TODO 修改
							}
						}

					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		//要删除的任务
		List<DtTaggingModel> deleteModels = modelGroup.get(Constants.TG_MODEL_END);
		if (CollectionUtils.isNotEmpty(deleteModels)) {
			for (DtTaggingModel model:deleteModels) {
				try {
					if (taskService.checkExists(model.getTaggingModelId().toString(),Constants.DT_SCHEDULE_GROUP)) {
						taskService.delete(model.getTaggingModelId().toString(),Constants.DT_SCHEDULE_GROUP);// @TODO 删除
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		//要重新启动的任务（服务器重启时要重新设置：2运行中、3运行成功状态的定时任务）
		List<DtTaggingModel> reRunModels =  modelGroup.get(Constants.TG_MODEL_RUNNING);
		List<DtTaggingModel> successModels =  modelGroup.get(Constants.TG_MODEL_SUCCESS);
		if (CollectionUtils.isEmpty(reRunModels)) {
			reRunModels = successModels;
			if (CollectionUtils.isEmpty(reRunModels)){
				return;
			}
		}else {
			if (CollectionUtils.isNotEmpty(successModels)){
				for (DtTaggingModel model:successModels) {
					reRunModels.add(model);
				}
			}
		}
		reRunModels.forEach(record->{
			try {
				if (!taskService.checkExists(record.getTaggingModelId().toString(),Constants.DT_SCHEDULE_GROUP)) {
					//新建
					TaskInfo taskInfo = new TaskInfo();
					taskInfo.setJobGroup(Constants.DT_SCHEDULE_GROUP);//必传
					taskInfo.setJobName(record.getTaggingModelId().toString());//必传
					taskInfo.setCronExpression(record.getCycle());//必传
					taskInfo.setJobMethod(Constants.DT_SCHEDULE_CORE_JOB_CLASS);//必传
					taskInfo.setCreateTime(new Date().toString());
					taskInfo.setId(1);//1为新增0为修改
					taskService.addJob(taskInfo);// @TODO 启动服务器时重新设置
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		});
	}
	/**
	 * 获取打标数据并根据规制自动打标（核心方法）
	 */
	public void calculation(Long taggingModelId){
		DtTaggingModel tagModel = get(taggingModelId);
		List<DtSetCol> cols= dtSetColService.getByTaggingModelId(taggingModelId);
		if (CollectionUtils.isEmpty(cols)){
			return ;
		}
//		Map<Long, List<DtSetCol>> colGroup = cols.stream().collect(Collectors.groupingBy(DtSetCol::getColId));
		//第一步、若存在删表
		MppPgExecuteUtil mppUtil = new MppPgExecuteUtil();
		mppUtil.setTableName(tagModel.getDataTableName());//表名
		mppUtil.dropTable();//删表
		//第二步、建表
		mppUtil.setTableKey(tagModel.getPkey());//主键
		Map<String,String> colmap  = new LinkedHashMap<>();//字段，注释
		Map<String,String> cloTypeMap  = new LinkedHashMap<>();//字段，字段类型
		mppUtil.setTableName(tagModel.getDataTableName());
		cols.forEach(record->{
			colmap.put(record.getShowCol(),"");
			if (TagConditionUtils.isDateType(record.getSourceDataType()) && !Constants.PUBLIC_YES.equals(record.getIsMarking())) {
				cloTypeMap.put(record.getShowCol(),"date");
			}else if(TagConditionUtils.isIntType(record.getSourceDataType()) && !Constants.PUBLIC_YES.equals(record.getIsMarking())){
				cloTypeMap.put(record.getShowCol(),"bigint");
			}else{
				cloTypeMap.put(record.getShowCol(),"varchar");
			}
		});
		mppUtil.createTable(colmap,cloTypeMap);
		//第三步、获取数据集数据并同步到mpp
		Date begin = new Date();
		int successCount = 0;
		try	{
			for (int i = 0; i < 10; i++) {
				++successCount;
				logger.info("第"+successCount+"次");
				List<Object> data = new LinkedList<>();
				Pageable pageable = PageRequest.of(i,100000);
				data.addAll((Collection<?>) getDataFromDataSet(taggingModelId,pageable));
				mppUtil.setDataList(data);
				mppUtil.insertDataList();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		Date end = new Date();
		//第四步update数据（传sql进mpp,在mpp计算）
		List<String> markingSql = getMarkingSQL(taggingModelId);
		if (CollectionUtils.isNotEmpty(markingSql)){
			mppUtil.setUpdateSqlList(markingSql);
			mppUtil.updateDataList();
		}
		//第五步，记录更新结果

		logger.info(String.format("模型：{%s}打标成功,总记录数数:{%s},总耗时:{%s}毫秒",taggingModelId,10000*successCount,end.getTime()-begin.getTime()));
	}


	/**
	 * 获取数据集数据（核心方法）
	 */
	public Object getDataFromDataSet(Long taggingModelId,Pageable pageable){
		List<DtSetCol> cols= dtSetColService.getByTaggingModelId(taggingModelId);
		List<List<Object>> data = new LinkedList<>();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (int j = 0; j < pageable.getPageSize(); j++) {
			List<Object> kk = new LinkedList<>();
			for (int i = 0; i < cols.size(); i++) {
				if (TagConditionUtils.isDateType(cols.get(i).getSourceDataType())) {
					kk.add(f.format(new Date()));
				}else if(TagConditionUtils.isIntType(cols.get(i).getSourceDataType())){
					kk.add(i+j);
				}else if (TagConditionUtils.isStringType(cols.get(i).getSourceDataType())){
					kk.add(RandomStringUtils.random(4,true,false).toUpperCase());
				}else {
					kk.add(null);
				}
			}
			data.add(kk);
		}
		return data;
	}

	/**
	 * 获取可执行执行的打标sql，用去mpp自动打标(核心方法)
	 */
	public List<String> getMarkingSQL(Long taggingModelId){
		DtTaggingModel tagModel = get(taggingModelId);//模型
		if (tagModel==null) {
			return null;
		}
		List<DtSetCol> cols = dtSetColService.getByTaggingModelId(taggingModelId);//模型字段列表
		if (CollectionUtils.isEmpty(cols)){
			return null;
		}
		List<Long> colIds = new ArrayList<>();
		cols.forEach(record->{
			colIds.add(record.getColId());
		});
		List<DtTagCondition>  conditions =  dtTagConditionService.findByColIds(colIds);//条件设置
		if (CollectionUtils.isEmpty(conditions)) {
			return null;
		}
		List<Long> tagIds = new ArrayList<>();
		conditions.forEach(record->{
			tagIds.add(record.getTagId());
		});
		List<DtTag> dtTags = dtTagService.findByTagIds(tagIds);//标签
		if (CollectionUtils.isEmpty(dtTags)){
			return null;
		}
		Map<Long, List<DtTag>> tagGroup = dtTags.stream().collect(Collectors.groupingBy(DtTag::getId));
		List<String> markingSQL = new ArrayList<>();
		for (DtTagCondition condition:conditions) {
			if (StringUtils.isBlank(condition.getFilterExpression()) ) {
				continue;
			}
			StringBuilder updateBuff = new StringBuilder();
			updateBuff.append(" UPDATE \"");
			updateBuff.append(tagModel.getDataTableName());
			updateBuff.append("\" ");
			updateBuff.append(" SET ");
			updateBuff.append(condition.getShowCol());
			updateBuff.append(" = '");
			updateBuff.append(tagGroup.get(condition.getTagId()).get(0).getTagName());
			updateBuff.append("' where ");
			updateBuff.append(condition.getFilterExpression());
			markingSQL.add(updateBuff.toString());
		}
		return markingSQL;
	}

	public static void main(String[] args) {
		String colJson = "[{\"index\":0,\"aggType\":null,\"name\":\"tb_0_MODIFY_ID\"},{\"index\":1,\"aggType\":null,\"name\":\"tb_0_CREATE_NAME\"},{\"index\":2,\"aggType\":null,\"name\":\"tb_0_SOURCE_NAME\"}]";
		List<Map<String,String>> colList = new LinkedList<>();
		colList = JSONObject.parseObject(colJson,List.class);
		String vauleJson  = "[[\"2\",\"1\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"本地测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"]]";
		List<Array> valueList = JSONObject.parseObject(vauleJson,List.class);
		Map<String,String> values =  new LinkedHashMap<>();

	}
}
