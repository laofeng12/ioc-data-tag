package com.openjava.datatag.tagmodel.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.component.PlatformCompent;
import com.openjava.datatag.component.PostgreSqlConfig;
import com.openjava.datatag.component.TokenGenerator;
import com.openjava.datatag.demo.dto.BaseResp;
import com.openjava.datatag.demo.dto.TopDepartmentReqReqDTO;
import com.openjava.datatag.dowload.domain.DownloadQueue;
import com.openjava.datatag.dowload.service.DownloadQueueService;
import com.openjava.datatag.log.domain.DtTaggingErrorLog;
import com.openjava.datatag.log.service.DtTaggingErrorLogService;
import com.openjava.datatag.log.service.DtTagmUpdateLogService;
import com.openjava.datatag.schedule.domain.TaskInfo;
import com.openjava.datatag.schedule.service.TaskService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.tagmodel.domain.*;
import com.openjava.datatag.tagmodel.dto.*;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;
import com.openjava.datatag.tagmodel.repository.DtSetColRepository;
import com.openjava.datatag.tagmodel.repository.DtTagConditionRepository;
import com.openjava.datatag.tagmodel.repository.DtTaggingModelRepository;
import com.openjava.datatag.user.domain.SysUser;
import com.openjava.datatag.user.repository.SysUserRepository;
import com.openjava.datatag.userprofile.service.PortrayalService;
import com.openjava.datatag.utils.EntityClassUtil;
import com.openjava.datatag.utils.MyTimeUtil;
import com.openjava.datatag.utils.TagConditionUtils;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.http.HttpClientUtils;
import org.ljdp.common.http.LjdpHttpClient;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.Result;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
	@Value("${dataSet.resourceDataUrl}")
	private String resourceDataUrl;//数据集接口地址
	Logger logger = LogManager.getLogger(getClass());

	@Autowired
	private StringRedisTemplate stringRedisTemplate;//
	@Resource
	private DtTaggingModelRepository dtTaggingModelRepository;//标签模型数据库访问层
	@Resource
	private DtSetColRepository dtSetColRepository;//字段表数据库访问层
	@Resource
	private DtTagService dtTagService;//DT_TAG标签业务层接口
	@Resource
	private DtTagConditionRepository dtTagConditionRepository;//条件设置表数据库访问层
	@Resource
	private DtFilterExpressionService dtFilterExpressionService;//规制表达式业务层接口
	@Resource
	private DtTagmUpdateLogService dtTagmUpdateLogService;//标签模型日志业务层接口
	@Resource
	private TaskService taskService;//
	@Resource
	private DtSetColService dtSetColService;//字段表业务层接口
	@Resource
	private DtTagConditionService dtTagConditionService;//条件设置表业务层接口
	@Resource
	private TokenGenerator tokenGenerator;//
	@Resource
    private  DtWaitUpdateIndexService dtWaitUpdateIndexService;//更新索引表业务层接口
	@Resource
	private RedisTemplate<String, Object> redisTemplate;//
	@Resource
	private PostgreSqlConfig postgreSqlConfig;//mpp配置信息
	@Resource
	private DtTaggingErrorLogService dtTaggingErrorLogService;//模型调度错误日志业务层接口
	@Resource
	private PortrayalService portrayalService;//画像查询模块业务层接口
	@Resource
	private AuditComponet auditComponet;//
	@Resource
	private DownloadQueueService downloadQueueService;//下载列表业务层接口
	@Resource
	private PlatformCompent platformCompent;//首页对接消息组件
	@Resource
	private SysUserRepository sysUserRepository;//

	/**
	 *
	 * @param copy
	 * @param ip
	 * @throws Exception
	 */
	public void copy(DtTaggingModelCopyDTO copy,String ip)throws Exception{
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取用户信息
		DtTaggingModel model = get(copy.getTaggingModelId());//获取模型
		if (model==null) {
			throw new APIException(MyErrorConstants.TAG_MODEL_NO_FIND,"此Id查无模型");//
		}
		String content = JSONObject.toJSONString(model);//

		//克隆模型
		DtTaggingModelDTO tempDTO = new DtTaggingModelDTO();
		DtTaggingModel clone = new DtTaggingModel();//
		BeanUtilsBean.getInstance().getConvertUtils()
				.register(new SqlDateConverter(null), Date.class);//解决时间空复制时出现异常
		BeanUtils.copyProperties(tempDTO,model);//
		BeanUtils.copyProperties(clone,tempDTO);//
		clone.setModelName(copy.getModelName());//
		clone.setModelDesc(copy.getModelDesc());//
		clone.setTaggingModelId(null);//
		clone.setCycle(null);//
		clone.setStartTime(null);//
		clone.setCycleEnum(null);//
		clone.setRunState(Constants.DT_MODEL_NO_BEGIN);//
		EntityClassUtil.dealCreateInfo(clone,userInfo);//
		clone = doSave(clone);//
		clone.setDataTableName("DT_"+clone.getTaggingModelId());// 这里要注意
		//克隆字段
		List<DtSetCol> cloList =
				dtSetColRepository.getByTaggingModelIdAndIsDeleted(model.getTaggingModelId(),Constants.PUBLIC_NO);//
		for (int i = 0; i <cloList.size() ; i++) {
			DtSetCol record = cloList.get(i);//
			DtSetColDTO colDTO = new DtSetColDTO();//
			DtSetCol colClone = new DtSetCol();//
			BeanUtils.copyProperties(colDTO,record);//
			BeanUtils.copyProperties(colClone,colDTO);//
			colClone.setColId(null);//
			colClone.setTaggingModelId(clone.getTaggingModelId());//
			EntityClassUtil.dealCreateInfo(colClone,userInfo);//
			colClone = dtSetColRepository.save(colClone);//
			List<DtTagCondition> conditions = dtTagConditionRepository.findByColId(record.getColId());//
			//克隆字段条件设置
			for (int j = 0; j <conditions.size() ; j++) {
				DtTagCondition condition = conditions.get(j);//
				DtTagConditionDTO conditionDTO = new DtTagConditionDTO();//
				DtTagCondition conditionClone =new DtTagCondition();//
				BeanUtils.copyProperties(conditionDTO,condition);//
				BeanUtils.copyProperties(conditionClone,conditionDTO);//
				conditionClone.setTagConditionId(null);//
				conditionClone.setColId(colClone.getColId());//
				EntityClassUtil.dealCreateInfo(conditionClone,userInfo);//
				conditionClone = dtTagConditionRepository.save(conditionClone);//
				//克隆规制表达式
				List<DtFilterExpression> expressions = dtFilterExpressionService.findByTagConditionId(condition.getTagConditionId());//
				for (DtFilterExpression expression: expressions) {
					DtFilterExpression copyExpression = new DtFilterExpression();//
					CopyDtFilterExpressionDTO copyExpressionDTO = new CopyDtFilterExpressionDTO();//
					BeanUtils.copyProperties(copyExpressionDTO,expression);//
					BeanUtils.copyProperties(copyExpression,copyExpressionDTO);//
					copyExpression.setFilterExpressionID(null);//
					copyExpression.setTagConditionId(conditionClone.getTagConditionId());//
					dtFilterExpressionService.doSave(copyExpression);//
				}
			}
		}

		AuditLogVO vo = new AuditLogVO();//
		vo.setType(1L);//管理操作
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("模型部署");//必传
		vo.setFunctionLev1("编辑");//必传
		vo.setFunctionLev2("另存模型");//必传
		vo.setRecordId(clone.getTaggingModelId()+"");//
		vo.setDataAfterOperat(JSONObject.toJSONString(clone));//
		auditComponet.saveAuditLog(vo);//
		//日志记录
//		dtTagmUpdateLogService.loggingNew("{ \"from\": "+content+"}",clone,ip);

	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Page<DtTaggingModel> query(DtTaggingModelDBParam params, Pageable pageable)throws Exception{
		AuditLogVO vo = new AuditLogVO();//
		vo.setType(2L);//数据查询
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("模型部署");//必传
		vo.setFunctionLev1("我的模型");//必传
		vo.setFunctionLev2("查询");//必传
		auditComponet.saveAuditLog(vo);//
		Page<DtTaggingModel> pageresult = dtTaggingModelRepository.query(params, pageable);//
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DtTaggingModel> queryDataOnly(DtTaggingModelDBParam params, Pageable pageable){
		return dtTaggingModelRepository.queryDataOnly(params, pageable);//
	}

	/**
	 * 根据主键获取模型
	 * @param id
	 * @return
	 */
	public DtTaggingModel get(Long id) {
		Optional<DtTaggingModel> o = dtTaggingModelRepository.findById(id);//根据主键获取模型
		if(o.isPresent()) {
			DtTaggingModel m = o.get();//
			return m;
		}
		System.out.println("找不到记录DtTaggingModel："+id);
		return null;
	}

	/**
	 *
	 * @param m
	 * @return
	 */
	public DtTaggingModel doSave(DtTaggingModel m) {
		return dtTaggingModelRepository.save(m);
	}

	/**
	 *
	 * @param body
	 * @param userInfo
	 * @param ip
	 * @return
	 */
	public DtTaggingModel doNew(DtTaggingModel body,BaseUserInfo userInfo,String ip){
		String content = JSONObject.toJSONString(body);//
		EntityClassUtil.dealCreateInfo(body,userInfo);//
		if (body.getModelName() == null){
			body.setModelName("新建模型");//
		}
		if (body.getModelDesc() == null){
			body.setModelDesc("新建模型");//
		}
		body.setRunState(Constants.DT_MODEL_NO_BEGIN);//未开始
		body.setIsNew(true);//执行insert
		body.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
		DtTaggingModel dbObj = dtTaggingModelRepository.save(body);//

		//日志记录
		dtTagmUpdateLogService.loggingNew(content,dbObj,ip);//

		return dbObj;
	}

	/**
	 *
	 * @param body
	 * @param userInfo
	 * @param ip
	 * @return
	 * @throws APIException
	 */
	public DtTaggingModel doNew(DtTaggingModelDTO body,BaseUserInfo userInfo,String ip) throws APIException {
		String reqParams = JSONObject.toJSONString(body);//
		List<DtSetCol> colList = body.getColList();//字段表
		if (body.getModelName() == null){
			body.setModelName("新建模型");//
		}
		if (body.getModelDesc() == null){
			body.setModelDesc("新建模型");//
		}
		body.setRunState(Constants.DT_MODEL_NO_BEGIN);//未开始
		body.setIsNew(true);//执行insert
		body.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
		DtTaggingModel taggingModel = new DtTaggingModel();//
		MyBeanUtils.copyPropertiesNotNull(taggingModel,body);//
		EntityClassUtil.dealCreateInfo(taggingModel,userInfo);//
		taggingModel = doSave(taggingModel);//
		taggingModel.setDataTableName("DT_"+ taggingModel.getTaggingModelId());//RandomStringUtils.random(27,true,false).toUpperCase()不合适，用模型id为后缀,这样比较有意义，表名就能看出是哪个模型的
		taggingModel = doSave(taggingModel);//
		for (DtSetCol col :colList){
			if ((!col.isNew()) || col.getColId() != null){
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"新建模型时选择字段必须为新");//
			}
			col.setIsSource(Constants.PUBLIC_YES);//源字段
			col.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
			EntityClassUtil.dealCreateInfo(col,userInfo);//
			col.setTaggingModelId(taggingModel.getTaggingModelId());//
			col.setShowCol(col.getSourceCol());//
			col = dtSetColRepository.save(col);//
		}
		dtTagmUpdateLogService.loggingNew(reqParams,taggingModel,ip);//
		return taggingModel;
	}

	/**
	 *
	 * @param body
	 * @param db
	 * @param userInfo
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public DtTaggingModel doRename(DtTaggingModelRenameDTO body,DtTaggingModel db,BaseUserInfo userInfo,String ip)throws Exception{
		String oldContent = JSONObject.toJSONString(db);//
		String modifyContent = JSONObject.toJSONString(body);//
		EntityClassUtil.dealModifyInfo(db,userInfo);//
		//MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);//执行update
		db.setTaggingModelId(body.getTaggingModelId());//
		db.setModelName(body.getModelName());//
		DtTaggingModel dbObj =dtTaggingModelRepository.save(db);//

		//日志记录
//		dtTagmUpdateLogService.loggingUpdate(modifyContent,oldContent,db,ip);
		AuditLogVO vo = new AuditLogVO();//
		vo.setType(1L);//管理操作
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("模型部署");//必传
		vo.setFunctionLev1("编辑");//必传
		vo.setFunctionLev2("修改模型名称");//必传
		vo.setRecordId(body.getTaggingModelId()+"");//
		vo.setDataBeforeOperat(oldContent);//
		vo.setDataAfterOperat(JSONObject.toJSONString(dbObj));//
		auditComponet.saveAuditLog(vo);//
		return dbObj;
	}


	/**
	 * 设置调度
	 */
	public void doDispatch(DtTaggingDispatchDTO body, DtTaggingModel db, Long userId, String ip) throws Exception {
		String oldContent = JSONObject.toJSONString(db);//
		String modifyContent = JSONObject.toJSONString(body);//
		db.setModifyTime(new Date());//
		db.setModifyUser(userId);//
		//检查Cycle 字段是否合理
		if(checkCycle(body.getCycleEnum())){
			if (body.getCycleEnum().equals(Constants.DT_DISPATCH_STOP)){
//				db.setRunState(Constants.DT_MODEL_END);
//				db.setStartTime(null);
//				db.setCycle(null);
//				db.setCycleEnum(null);
				stopModel(db.getTaggingModelId(),null);//
			}else{
				if (body.getStartTime() == null){
					throw new APIException(MyErrorConstants.TAGM_DISPATCH_NONE_START_TIME,"未设置开始时间");//
				}
				db.setRunState(Constants.DT_MODEL_WAIT);//
				db.setStartTime(body.getStartTime());//
				db.setCycle(toCron(body.getCycleEnum(),body.getStartTime()));//
				db.setCycleEnum(body.getCycleEnum());//
			}
		}else{
			throw new APIException(MyErrorConstants.TAGM_DISPATCH_CYCLE_ERROR,"Cycle不合法");//
		}

		db = dtTaggingModelRepository.save(db);//
		AuditLogVO vo = new AuditLogVO();//
		vo.setType(1L);//管理操作
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("模型部署");//必传
		vo.setFunctionLev1("我的模型");//必传
		vo.setFunctionLev2("调度");//必传
		vo.setRecordId(db.getId()+"");//
		vo.setDataBeforeOperat(oldContent);//
		vo.setDataAfterOperat(JSONObject.toJSONString(db));//
		auditComponet.saveAuditLog(vo);//
		//日志记录
//		dtTagmUpdateLogService.loggingUpdate(modifyContent,oldContent,db,ip);
	}

	/**
	 * 检查Cycle是否合法
	 * @param cycle
	 * @return
	 */
	private boolean checkCycle(Long cycle){
		return cycle >= Constants.DT_DISPATCH_STOP && cycle <= Constants.DT_DISPATCH_EACH_YEAR;
	}

	/**
	 * 将前端传进来的数据转换成cron表达式
	 * @param cycle
	 * @param startTime
	 * @return
	 * @throws APIException
	 */
	private String toCron(Long cycle,Date startTime) throws APIException {
		Calendar cal = Calendar.getInstance();//
		cal.setTime(startTime);//
		int second = cal.get(Calendar.SECOND);//
		int minute = cal.get(Calendar.MINUTE);//
		int hour = cal.get(Calendar.HOUR_OF_DAY);//
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);//
		int month = cal.get(Calendar.MONTH)+1;//
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;//
		int year = cal.get(Calendar.YEAR);//
		String[] cron = {"*","*","*","?","*","?","*"};//
		cron[0] = String.valueOf(second);//
		cron[1] = String.valueOf(minute);//
		cron[2] = String.valueOf(hour);//
		if (cycle.equals(Constants.DT_DISPATCH_ONCE)) {
			cron[0] = String.valueOf(second);//
			cron[1] = String.valueOf(minute);//
			cron[2] = String.valueOf(hour);//
			cron[3]=String.valueOf(dayOfMonth);//
			cron[4] = String.valueOf(month);//
			cron[6] = String.valueOf(year) + "/20";//设置大一点（20年一次）就可以看成只执行一次
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_DAY)){
			//月份中的某一天
			cron[3] = "*";//
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_WEEK)){
			cron[3]="?";//
			cron[5] = MyTimeUtil.getWeekStr(startTime); // @TODO dayOfMonth/7 不准确
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_MONTH)){
		    cron[3] = String.valueOf(dayOfMonth);//
			cron[4] = String.valueOf(month)+"/1";//
		}else if (cycle.equals(Constants.DT_DISPATCH_EACH_YEAR)){
			cron[3]=String.valueOf(dayOfMonth);//
			cron[4] = String.valueOf(month);//
//			cron[6] = String.valueOf(year) + "/1";//
		}else {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"cycle in toCron must in{DT_DISPATCH_EACH_DAY/WEEK/MONTH/YEAR}");//
		}
        StringBuilder cronEx = new StringBuilder();
		for (int i=0;i<cron.length;i++){
			if (i==0){
				cronEx.append(cron[i]);
			}else{
				cronEx.append(" ").append(cron[i]);
			}
		}
		return cronEx.toString();//
	}

	/**
	 *
	 * @param db
	 * @param userId
	 * @param ip
	 * @throws Exception
	 */
	public void doSoftDelete(DtTaggingModel db,Long userId,String ip)throws Exception{
		String oldJson = JSONObject.toJSONString(db);//
		Date now = new Date();//
		List<DtSetCol> list = dtSetColRepository.getByTaggingModelIdAndIsDeleted(db.getId(),Constants.PUBLIC_NO);//
		for (DtSetCol col: list){
			dtTagConditionRepository.doSoftDeleteByColId(col.getColId(),now,db.getCreateUser());//
		}
		dtSetColRepository.doSoftDeleteByTaggingModelId(db.getId(),now,db.getCreateUser());//
		db.setIsDeleted(Constants.PUBLIC_YES);//
		db.setModifyTime(now);//
		db = dtTaggingModelRepository.save(db);//
		AuditLogVO vo = new AuditLogVO();//
		vo.setType(1L);//管理操作
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("模型部署");//必传
		vo.setFunctionLev1("我的模型	");//必传
		vo.setFunctionLev2("删除");//必传
		vo.setRecordId(db.getTaggingModelId()+"");///
		vo.setDataBeforeOperat(oldJson);//
		vo.setDataAfterOperat(JSONObject.toJSONString(db));//
		auditComponet.saveAuditLog(vo);//
		//日志记录
//		dtTagmUpdateLogService.loggingDelete(db, ip);
		//@TODO  删除画像
	}

	/**
	 *
	 * @param id
	 */
	public void doDelete(Long id) {
		dtTaggingModelRepository.deleteById(id);
	}

	/**
	 *
	 * @param ids
	 */
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
		List<Long> allRunState = new ArrayList<>();//
		allRunState.add(Constants.DT_MODEL_WAIT);//
		allRunState.add(Constants.DT_MODEL_RUNNING);//
		allRunState.add(Constants.DT_MODEL_SUCCESS);//
		allRunState.add(Constants.DT_MODEL_ERROR);//
		allRunState.add(Constants.DT_MODEL_END);//
		List<DtTaggingModel> allModels =  getModelByRunStates(allRunState);//
		Map<Long, List<DtTaggingModel>> modelGroup = allModels.stream().collect(Collectors.groupingBy(DtTaggingModel::getRunState));//
		//等待执行调度的任务
		List<DtTaggingModel> waitModels =  modelGroup.get(Constants.DT_MODEL_WAIT);//
		if (CollectionUtils.isNotEmpty(waitModels)) {
			for (DtTaggingModel model:waitModels) {
				try {
					//新建
					TaskInfo taskInfo = new TaskInfo();//
					taskInfo.setJobGroup(Constants.DT_SCHEDULE_GROUP);//必传
					taskInfo.setJobName(model.getTaggingModelId().toString());//必传
					taskInfo.setCronExpression(model.getCycle());//必传
					taskInfo.setJobMethod(Constants.DT_SCHEDULE_CORE_JOB_CLASS);//必传
					taskInfo.setCreateTime(new Date().toString());//
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
		List<DtTaggingModel> deleteModels = modelGroup.get(Constants.DT_MODEL_END);//
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
		List<DtTaggingModel> reRunModels =  modelGroup.get(Constants.DT_MODEL_RUNNING);//
		List<DtTaggingModel> successModels =  modelGroup.get(Constants.DT_MODEL_SUCCESS);//
		if (CollectionUtils.isEmpty(reRunModels)) {
			reRunModels = successModels;//
			if (CollectionUtils.isEmpty(reRunModels)){
				return;
			}
		}else {
			if (CollectionUtils.isNotEmpty(successModels)){
				for (DtTaggingModel model:successModels) {
					reRunModels.add(model);//
				}
			}
		}
		reRunModels.forEach(record->{
			try {
				if (!taskService.checkExists(record.getTaggingModelId().toString(),Constants.DT_SCHEDULE_GROUP)) {
					//新建
					TaskInfo taskInfo = new TaskInfo();//
					taskInfo.setJobGroup(Constants.DT_SCHEDULE_GROUP);//必传
					taskInfo.setJobName(record.getTaggingModelId().toString());//必传
					taskInfo.setCronExpression(record.getCycle());//必传
					taskInfo.setJobMethod(Constants.DT_SCHEDULE_CORE_JOB_CLASS);//必传
					taskInfo.setCreateTime(new Date().toString());//
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
	public void calculation(DtTaggingModel tagModel) {
		Long taggingModelId = tagModel.getTaggingModelId();//
		Result mppResult;//
		List<DtSetCol> cols= dtSetColService.getByTaggingModelId(taggingModelId);//
		if (CollectionUtils.isEmpty(cols)){
			return ;
		}
//		Map<Long, List<DtSetCol>> colGroup = cols.stream().collect(Collectors.groupingBy(DtSetCol::getColId));
		//第一步、若存在删表
		MppPgExecuteUtil mppPgExecuteUtil = new MppPgExecuteUtil();//
		mppPgExecuteUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
		mppPgExecuteUtil.setTableName(tagModel.getDataTableName());//表名
		mppResult = mppPgExecuteUtil.dropTable();//删表
		//第二步、建表
		mppPgExecuteUtil.setTableKey(tagModel.getPkey());//主键
		Map<String,String> colmap  = new LinkedHashMap<>();//字段，注释(用于建表)
		Map<String,String> insertColmap  = new LinkedHashMap<>();//字段，注释(用于插入数据)
		Map<String,String> cloTypeMap  = new LinkedHashMap<>();//字段，字段类型(用于建表)
		Map<String,String> insertCloTypeMap  = new LinkedHashMap<>();//字段，字段类型(用于插入数据)
		mppPgExecuteUtil.setTableName(tagModel.getDataTableName());
		cols.forEach(record->{
			colmap.put(record.getShowCol(),"");//源字段
			insertColmap.put(record.getShowCol(),"");
			if (TagConditionUtils.isDateType(record.getSourceDataType())) {
				cloTypeMap.put(record.getShowCol(),"date");//
				insertCloTypeMap.put(record.getShowCol(),"date");//
			}else if(TagConditionUtils.isIntType(record.getSourceDataType())){
				if (record.getSourceDataType().indexOf(",")==-1) {
					cloTypeMap.put(record.getShowCol(),"decimal");
					insertCloTypeMap.put(record.getShowCol(),"decimal");//
				}else{
					cloTypeMap.put(record.getShowCol(),"bigint");//
					insertCloTypeMap.put(record.getShowCol(),"bigint");//
				}
			}else{
				cloTypeMap.put(record.getShowCol(),"varchar");//
				insertCloTypeMap.put(record.getShowCol(),"varchar");//
			}
			if (Constants.PUBLIC_YES.equals(record.getIsMarking())) {
				colmap.put(Constants.DT_COL_PREFIX+record.getShowCol(),"");//打标结果字段
				cloTypeMap.put(Constants.DT_COL_PREFIX+record.getShowCol(),"varchar");//
			}
		});
		mppResult = mppPgExecuteUtil.createTable(colmap,cloTypeMap);//
		//第三步、获取数据集数据并同步到mpp
		mppPgExecuteUtil.setColumnMap(insertCloTypeMap);//
		mppPgExecuteUtil.setColumnTypeMap(insertCloTypeMap);//
		Date begin = new Date();//
		long totalCount = 0;//更新总数
		long successCount = 0;//成功数
		long totalPage = 0;//
		int size = 5000;//
		try	{
			List<Object> data = new LinkedList<>();//
			Pageable pageable = PageRequest.of(0,size);//
			Page<Object> result  = (Page<Object>) getDataFromDataSet(taggingModelId,0,pageable);//
			data.addAll(result.getContent());//
			mppPgExecuteUtil.setDataList(data);//
			mppPgExecuteUtil.insertDataList();//
			totalPage = result.getTotalPages();//
			totalCount = result.getTotalElements();//
			if (totalCount<size){
				successCount+=totalCount;//
			}else {
				successCount+=size;//
			}
			if (totalPage>1){
				for (int i = 1; i <totalPage ; i++) {
					pageable = PageRequest.of(i,size);//
					Page<Object> nextResult  = (Page<Object>) getDataFromDataSet(taggingModelId,0,pageable);//
					List<Object> nextData = new LinkedList<>();//
					nextData.addAll(nextResult.getContent());//
					mppPgExecuteUtil.setDataList(nextData);//
					mppResult = mppPgExecuteUtil.insertDataList();//
					successCount+=nextResult.getNumberOfElements();//
				}
			}
		}catch (Exception e){
			e.printStackTrace();//
			logger.info(e.getMessage());//
			DtTaggingErrorLog errorLog = new DtTaggingErrorLog();//
//			Base64.deco
//			decoder.de
			try {
                errorLog.setErrorInfo(e.getMessage());//
            }catch (Exception e2){
                errorLog.setErrorInfo(e.getMessage());//
            }
			errorLog.setErrorTime(new Date());//
			errorLog.setTaggingModelId(taggingModelId);//
			dtTaggingErrorLogService.doSave(errorLog);//
		}
		Date end = new Date();//
		//第四步update数据（传sql进mpp,在mpp计算）
		List<String> markingSql = getMarkingSQL(taggingModelId);
		if (CollectionUtils.isNotEmpty(markingSql)){
			mppPgExecuteUtil.setUpdateSqlList(markingSql);//
			mppPgExecuteUtil.updateDataList();//
		}
		//第五步，记录更新结果
		DtWaitUpdateIndex waitUpdateIndex = new DtWaitUpdateIndex();//
		waitUpdateIndex.setIsNew(true);//
		waitUpdateIndex.setCreateTime(new Date());//
		waitUpdateIndex.setRunState(0L);//
		waitUpdateIndex.setTableName(tagModel.getDataTableName());//
		waitUpdateIndex.setModelKeyColName(tagModel.getPkey());//
		waitUpdateIndex.setTaggingModelId(taggingModelId);//
		dtWaitUpdateIndexService.doSave(waitUpdateIndex);//
		tagModel.setRunState(Constants.DT_MODEL_SUCCESS);//
		if (successCount<totalCount||successCount==0||(mppResult!=null && mppResult.getCode()!=200)) {
			tagModel.setRunState(Constants.DT_MODEL_ERROR);//
			if (StringUtils.isNotBlank(mppResult.getMessage())){
				DtTaggingErrorLog errorLog = new DtTaggingErrorLog();//
				try {
					errorLog.setErrorInfo(mppResult.getMessage());//
				}catch (Exception e2){
					errorLog.setErrorInfo(mppResult.getMessage());//
				}
				errorLog.setErrorTime(new Date());//
				errorLog.setTaggingModelId(taggingModelId);//
				dtTaggingErrorLogService.doSave(errorLog);//
			}
			//发送告警信息  todo
			try {
				SysUser sysUser = sysUserRepository.getOne(tagModel.getCreateUser());
				platformCompent.spUnifyMsgNotice(tagModel.getTaggingModelId()+"",tagModel.getCreateUser()+"",sysUser.getAccount());//发送告警信息
			}catch (Exception e){
				System.out.println("告警失败");
				e.printStackTrace();
			}

		}
		tagModel.setUpdateNum(totalCount);//
		tagModel.setSuccessNum(successCount);//
		tagModel = doSave(tagModel);//
		stringRedisTemplate.convertAndSend(Constants.DT_REDIS_MESSAGE_QUEUE_CHANL,JSONObject.toJSONString(tagModel));//
		logger.info(String.format("模型：{%s}打标成功,总记录数数:{%s},成功数{%s},总耗时:{%s}毫秒",taggingModelId,totalCount,successCount,end.getTime()-begin.getTime()));
	}


	/**
	 * 获取数据集数据（核心方法）
	 */
	public Object getDataFromDataSet(Long taggingModelId,int type,Pageable pageable)throws Exception{
		List<DtSetCol> cols= dtSetColService.getByTaggingModelId(taggingModelId);//
		Map<String,Object> sourceMap  = new LinkedHashMap<>();//表头
		DtTaggingModel taggingModel = get(taggingModelId);
		for (int i = 0; i < cols.size(); i++) {
//			if (cols.get(i).getIsSource()==1) {
				sourceMap.put(cols.get(i).getSourceColId(),cols.get(i).getSourceCol());//
//			}
		}
		LjdpHttpClient client = new LjdpHttpClient();
		String  token= tokenGenerator.createToken(taggingModel.getCreateUser());//模型创建者才有权限调用数据集接口
		client.setHeader("authority-token",token);//
		client.setHeader("User-Agent","platform-schedule-job");//
		DataSetReqDTO req = new DataSetReqDTO();//
		req.setColumnList(sourceMap.values().stream().toArray());//
		req.setColumnIdList(sourceMap.keySet().stream().toArray());//
		req.setPage(pageable.getPageNumber());//
		req.setSize(pageable.getPageSize());//
        logger.info("url:"+resourceDataUrl+taggingModel.getResourceId()+"-"+taggingModel.getResourceType()+"\n"+JSONObject.toJSONString(req));
		HttpResponse resp = client.postJSON(resourceDataUrl+taggingModel.getResourceId()+"-"+taggingModel.getResourceType(), JSONObject.toJSONString(req));
		String jsontext = HttpClientUtils.getContentString(resp.getEntity(), "utf-8");
        System.out.println(jsontext);
		DataSetRspDTO data = JSONObject.parseObject(jsontext, DataSetRspDTO.class);//
		if (resp.getStatusLine().getStatusCode()==200 && data.getCode()==200) {
			//重组数据
			List<List<Object>> dataList =data.getData().getData();//原始数据
			Object[] columnList =  sourceMap.values().stream().toArray();//表头
            List result= rebuiltData(cols,dataList,columnList,type);//处理数据
            return new PageImpl<>(result, pageable, data.getData().getTotal());
		}else {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,data.getMessage());
		}
	}

    /**
     * 重组数据
     * @param cols 字段表（包括克隆的）
     * @param dataList 原始数据
     * @param columnList 源表头
     * @param type 重组类型，1：键值对的数据；其他：只返回值
     * @return List
     */
	public List rebuiltData(List<DtSetCol> cols,List<List<Object>> dataList,Object[] columnList,int type){
        //重组数据
        List<List<Object>> resultList = new LinkedList<>();//重组后的数据
        for (int i = 0; i < dataList.size(); i++) {
            List<Object> result = new LinkedList<>();//单表数据
            for (int k = 0; k < cols.size(); k++) {
                int index = Arrays.asList(columnList).indexOf(cols.get(k).getSourceCol());
                result.add(dataList.get(i).get(index));//
            }
            resultList.add(result);//
        }
        //type=1返回key+Value
        if (type==1) {
            List<Object> tempData =new LinkedList<>();//
            for (int i = 0; i < resultList.size(); i++) {
                String ob = "";//
                for (int j = 0; j < resultList.get(i).size(); j++) {
                	String value = "";//
                	if (resultList.get(i).get(j)!=null){
						value = resultList.get(i).get(j).toString();//
					}
                    ob += "\""+cols.get(j).getShowCol()+"\":\""+ StringEscapeUtils.escapeJava(value) +"\",";//
                }
                ob="{"+ob.substring(0,ob.length()-1)+"}";//
                tempData.add(JSONObject.parseObject(ob,Object.class));//
            }
            return tempData;
        }else {
            return resultList;
        }
    }

	/**
	 * 获取可执行执行的打标sql，用去mpp自动打标(核心方法)
	 */
	public List<String> getMarkingSQL(Long taggingModelId){
		DtTaggingModel tagModel = get(taggingModelId);//模型
		if (tagModel==null) {
			return null;//
		}
		List<DtSetCol> cols = dtSetColService.getByTaggingModelId(taggingModelId);//模型字段列表
		if (CollectionUtils.isEmpty(cols)){
			return null;//
		}
		List<Long> colIds = new ArrayList<>();//
		cols.forEach(record->{
			colIds.add(record.getColId());//
		});
		List<DtTagCondition>  conditions =  dtTagConditionService.findByColIds(colIds);//条件设置
		if (CollectionUtils.isEmpty(conditions)) {
			return null;//
		}
		List<Long> tagIds = new ArrayList<>();//
		conditions.forEach(record->{
			tagIds.add(record.getTagId());//
		});
		List<DtTag> dtTags = dtTagService.findByTagIds(tagIds);//标签
		if (CollectionUtils.isEmpty(dtTags)){
			return null;//
		}
		Map<Long, List<DtTag>> tagGroup = dtTags.stream().collect(Collectors.groupingBy(DtTag::getId));//
		List<String> markingSQL = new ArrayList<>();//
		for (DtTagCondition condition:conditions) {
			if (StringUtils.isBlank(condition.getFilterExpression()) ) {
				continue;
			}
			StringBuilder updateBuff = new StringBuilder();//
			updateBuff.append(" UPDATE \"");//
			updateBuff.append(tagModel.getDataTableName());//
			updateBuff.append("\" ");//
			updateBuff.append(" SET \"");//
			updateBuff.append(Constants.DT_COL_PREFIX+condition.getShowCol());//
			updateBuff.append("\" ");//
			updateBuff.append(" = '");//
			updateBuff.append(tagGroup.get(condition.getTagId()).get(0).getTagName());//
			updateBuff.append("' where ");//
			updateBuff.append(condition.getFilterExpression());//
			markingSQL.add(updateBuff.toString());//
		}
		return markingSQL;
	}
    /**
     * 获取模型打标结果数据列表
	 * type=1时返回key和value
     */
    public Object getTaggingResultData(Long taggingModelId,int type,Pageable pageable)throws Exception{
		DtTaggingModel taggingModel = get(taggingModelId);//
		String alias = "t";//别名
		String tableName = Constants.DT_TABLE_PREFIX+taggingModel.getTaggingModelId();//
		MppPgExecuteUtil mppPgExecuteUtil = new MppPgExecuteUtil();//
		mppPgExecuteUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
		mppPgExecuteUtil.setSQL("select count(1) from \""+tableName+"\"");//
        long totalCount = 0;
        try {
            String[][] count = mppPgExecuteUtil.getData2();//
            totalCount = Long.valueOf(count[1][0]);//
        }catch (Exception e){
            e.printStackTrace();//
            logger.info(e.getMessage());//
        }
		if (totalCount<=0) {
			Map<String,Object> map = new HashMap<>();//
			map.put("pKey",taggingModel.getPkey());//
			map.put("tableName",Constants.DT_TABLE_PREFIX+taggingModel.getTaggingModelId());//
			map.put("result",new PageImpl<>(new ArrayList<>(), pageable, 0));//
			map.put("cols","");//
			return map;
		}
		Map<String, String> tableNameForQuery = new LinkedHashMap<>(1);//
		tableNameForQuery.put(tableName,alias);//
		mppPgExecuteUtil.setTableNameForQuery(tableNameForQuery);//
		mppPgExecuteUtil.setPageable(pageable);//
		String[][] data = mppPgExecuteUtil.getData();//第一个为表头
		List<List<Object>> dataList = new LinkedList<>();//数据
		for (int i = 1; i < data.length; i++) {
			List<Object> list = new LinkedList<>();
			for (int j = 0; j < data[i].length; j++) {
				list.add(data[i][j]);//
			}
			dataList.add(list);//
		}
		List<DtSetCol> cols = new LinkedList<>();
		for (int i = 0; i < data[0].length; i++) {
			DtSetCol col = new DtSetCol();//
			col.setSourceCol(data[0][i]);//
			col.setShowCol(data[0][i]);//
			cols.add(col);//
		}
		List<Object> result = rebuiltData(cols,dataList,data[0],type);//重组数据
		Map<String,Object> map = new HashMap<>();//
		map.put("pKey",taggingModel.getPkey());//
		map.put("tableName",Constants.DT_TABLE_PREFIX+taggingModel.getTaggingModelId());//
		map.put("cols",data[0]);//
		if (data!=null) {
			map.put("result",new PageImpl<>(result, pageable, totalCount));//
		}else{
			map.put("result",new PageImpl<>(result, pageable, 0));//
		}
		return map;
	}

	/**
	 * 停止模型核心代码（删除画像）
	 * @param taggingModelId
	 */
	public void stopModel(Long taggingModelId,Long tagId){
		DtTaggingModel model = get(taggingModelId);//获取模型
		model.setRunState(Constants.DT_MODEL_END);//设置运行状态
		model.setStartTime(null);//清空开始时间
		model.setCycle(null);//清空调度设置
		model.setCycleEnum(null);//
		model.setUpdateNum(0L);//清空更新数据
		model.setSuccessNum(0L);//清空成功数据
		//删除字段的打标设置
		if (tagId!=null) {
			List<DtTagCondition> conditionList = dtTagConditionService.findByTaggingModelIdAndColId(taggingModelId,tagId);//获取条件设置
			if (CollectionUtils.isNotEmpty(conditionList)){
				for (DtTagCondition condition:conditionList) {
					condition.setIsDeleted(Constants.PUBLIC_YES);//设置是否删除
					dtTagConditionService.doSave(condition);//保存
				}
			}
		}
		//删除画像
		portrayalService.clearPortrayal(Constants.DT_TABLE_PREFIX+model.getTaggingModelId());
	}

	/**
	 * 停止模型删除画像
	 * @param tagids
	 */
	public void stopModelByColIds(List<Long> tagids){
		if (CollectionUtils.isEmpty(tagids)){
			return;
		}
		List<DtTaggingModel> models =  dtTaggingModelRepository.getModelByTagIds(tagids);//获取使用改标签的模型
		if (CollectionUtils.isNotEmpty(models)){
			//停止模型删除画像
			models.forEach(model->{
				stopModel(model.getTaggingModelId(),null);//
			});
			//删除字段的打标设置
			List<DtTagCondition> conditionList = dtTagConditionService.findByTagIds(tagids);//获取该标签的所有打标设置
			if (CollectionUtils.isNotEmpty(conditionList)){
				conditionList.forEach(condition->{
					condition.setIsDeleted(Constants.PUBLIC_YES);//
					dtTagConditionService.doSave(condition);//软删除
				});
			}
		}
	}

	/**
	 *
	 * @param number
	 * @param taggingModelId
	 * @return
	 * @throws Exception
	 */
	public SuccessMessage beginDowload(Long number, Long taggingModelId)throws Exception{
		DtTaggingModel model =  this.get(taggingModelId);//
		if (model==null){
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此模型");//
		}
		DownloadQueue queue = downloadQueueService.findBybtypeAndBid(Constants.DT_BTYPE_DATATAG,taggingModelId.toString());//
		if (queue==null){
			queue = new DownloadQueue();//
			queue.setIsNew(true);//
		}else {
			queue.setIsNew(false);//
		}
		queue.setState(Constants.DT_DOWLOAD_STATE_WAIT);//
		queue.setSpeedOfProgress("0");//
		queue.setBtype(Constants.DT_BTYPE_DATATAG);//
		queue.setBid(taggingModelId.toString());//
		queue.setBname(model.getModelName());//
		queue.setCreateTime(new Date());//
		queue.setCreateUser(SsoContext.getUserId());//
		queue.setFileSize(null);//
		queue.setDownloadUrl(null);//
		queue.setDownloadNum(number);//
		queue = downloadQueueService.doSave(queue);//
		AuditLogVO vo = new AuditLogVO();//
		vo.setType(1L);//管理操作
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("模型部署");//必传
		vo.setFunctionLev1("我的模型	");//必传
		vo.setFunctionLev2("导出数据");//必传
		vo.setRecordId(taggingModelId+"");
		auditComponet.saveAuditLog(vo);
		return new SuccessMessage("已开始导出");//
	}

	public static void main(String[] args) {
		/*String colJson = "[{\"index\":0,\"aggType\":null,\"name\":\"tb_0_MODIFY_ID\"},{\"index\":1,\"aggType\":null,\"name\":\"tb_0_CREATE_NAME\"},{\"index\":2,\"aggType\":null,\"name\":\"tb_0_SOURCE_NAME\"}]";
		List<Map<String,String>> colList = new LinkedList<>();
		colList = JSONObject.parseObject(colJson,List.class);
		String vauleJson  = "[[\"2\",\"1\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"本地测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-OracleBigData\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"],[\"#NULL\",\"#NULL\",\"服务器测试用-数据湖-postgres\"]]";
		List<Array> valueList = JSONObject.parseObject(vauleJson,List.class);
		Map<String,String> values =  new LinkedHashMap<>();*/
		String[] columnList = new String[6];
		columnList[0]="tb_0_book_name";//
		columnList[1]="tb_0_covers";//
		columnList[2]="tb_0_registration_id";//
		columnList[3]="tb_0_exam_manager_id";//
		columnList[4]="tb_0_exam_paper_id";//
		columnList[5]="tb_0_id";//
		List<String> list = Arrays.asList(columnList);//
		System.out.println(Arrays.binarySearch(columnList,0,6,"tb_0_id"));//
		System.out.println(list.indexOf("tb_0_id"));//
	}
}
