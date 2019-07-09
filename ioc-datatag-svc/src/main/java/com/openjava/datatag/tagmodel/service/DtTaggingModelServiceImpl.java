package com.openjava.datatag.tagmodel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.log.domain.DtTagmUpdateLog;
import com.openjava.datatag.log.repository.DtTagmUpdateLogRepository;
import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.dto.DtSetColDTO;
import com.openjava.datatag.tagmodel.dto.DtTagConditionDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingDispatchDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;
import com.openjava.datatag.tagmodel.repository.DtSetColRepository;
import com.openjava.datatag.tagmodel.repository.DtTagConditionRepository;
import com.openjava.datatag.tagmodel.repository.DtTaggingModelRepository;
import com.openjava.datatag.utils.EntityClassUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * 标签模型业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtTaggingModelServiceImpl implements DtTaggingModelService {
	
	@Resource
	private DtTaggingModelRepository dtTaggingModelRepository;
	@Resource
	private DtSetColRepository dtSetColRepository;

	@Resource
	private DtTagConditionRepository dtTagConditionRepository;

	@Resource
	private DtTagmUpdateLogRepository dtTagmUpdateLogRepository;


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
		BeanUtils.copyProperties(tempDTO,model);
		BeanUtils.copyProperties(clone,tempDTO);
		clone.setTaggingModelId(null);
		EntityClassUtil.dealCreateInfo(clone,userInfo);
		clone = doSave(clone);
		//克隆字段
		List<DtSetCol> cloList =  dtSetColRepository.getByTaggingModelId(model.getTaggingModelId());
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
				dtTagConditionRepository.save(conditionClone);
			}
		}

		//日志记录
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(clone.getCreateTime());
		log.setModifyUser(Long.valueOf(userInfo.getUserId()));
		log.setTaggingModelId(clone.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_NEW);
		log.setModifyContent("{ \"from\": "+content+"}");
		log.setIsNew(true);
		dtTagmUpdateLogRepository.save(log);

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
		body.setRunState(Constants.TG_MODEL_NO_BEGIN);//未开始
		body.setIsNew(true);//执行insert
		body.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
		DtTaggingModel dbObj = dtTaggingModelRepository.save(body);

		//日志记录
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(body.getModifyTime());
		log.setModifyUser(Long.valueOf(userInfo.getUserId()));
		log.setTaggingModelId(body.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_NEW);
		log.setModifyContent(content);
		log.setIsNew(true);
		dtTagmUpdateLogRepository.save(log);

		return dbObj;
	}

	public DtTaggingModel doUpdate(DtTaggingModel body,DtTaggingModel db,BaseUserInfo userInfo,String ip){
		String oldContent = JSONObject.toJSONString(db);
		String modifyContent = JSONObject.toJSONString(body);
		EntityClassUtil.dealModifyInfo(db,userInfo);
		MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);//执行update
		DtTaggingModel dbObj =dtTaggingModelRepository.save(db);

		//日志记录
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(db.getModifyTime());
		log.setModifyUser(Long.valueOf(userInfo.getUserId()));
		log.setTaggingModelId(db.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);
		log.setModifyContent("{\"old\":"+oldContent+ ",\"newRep\":"+ modifyContent+"}");
		log.setIsNew(true);
		dtTagmUpdateLogRepository.save(log);

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
		//检查Cycle 字段是否合理--尚不明确前端传入的字段类型(或保存cron表达式或只保存周期标识)
		if(checkCycle(body.getCycle())){
			db.setCycle(body.getCycle());
		}else{
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"Cycle不合法");
		}
		db.setStartTime(body.getStartTime());
		dtTaggingModelRepository.save(db);

		//日志记录
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(db.getModifyTime());
		log.setModifyUser(userId);
		log.setTaggingModelId(db.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);
		log.setModifyContent("{\"old\":"+oldContent+ ",\"newRep\":"+ modifyContent+"}");
		log.setIsNew(true);
		dtTagmUpdateLogRepository.save(log);
	}

	//检查Cycle是否合法
	private boolean checkCycle(String Cycle){
		return true;
	}



	public void doSoftDelete(DtTaggingModel db,Long userId,String ip){
		Date now = new Date();
		List<DtSetCol> list = dtSetColRepository.getByTaggingModelId(db.getId());
		for (DtSetCol col: list){
			dtTagConditionRepository.doSoftDeleteByColId(col.getColId(),now,db.getCreateUser());
		}
		dtSetColRepository.doSoftDeleteByTaggingModelId(db.getId(),now,db.getCreateUser());
		db.setIsDeleted(Constants.PUBLIC_YES);
		db.setModifyTime(now);
		dtTaggingModelRepository.save(db);

		//日志记录
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(db.getModifyTime());
		log.setModifyUser(userId);
		log.setTaggingModelId(db.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_DELETE);
		//log.setModifyContent();//删除就不需要保存内容了
		log.setIsNew(true);
		dtTagmUpdateLogRepository.save(log);
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
}
