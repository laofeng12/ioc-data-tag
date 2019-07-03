package com.openjava.datatag.tagmodel.service;

import java.util.*;
import javax.annotation.Resource;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import com.openjava.datatag.tagmodel.domain.DtTagConditionUpdateLog;
import com.openjava.datatag.tagmodel.domain.DtTagcolUpdateLog;
import com.openjava.datatag.tagmodel.repository.DtTagcolUpdateLogRepository;
import com.openjava.datatag.utils.EntityClassUtil;
import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
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
}
