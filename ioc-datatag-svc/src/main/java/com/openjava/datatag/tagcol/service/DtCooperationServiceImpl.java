package com.openjava.datatag.tagcol.service;

import java.util.*;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.domain.DtTagmCooLog;
import com.openjava.datatag.tagcol.dto.DtCooTagcolLimitDTO;
import com.openjava.datatag.tagcol.dto.DtCooperationDTO;
import com.openjava.datatag.tagcol.repository.DtCooTagcolLimitRepository;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.utils.EntityClassUtil;
import org.apache.commons.collections.CollectionUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagcol.domain.DtCooperation;
import com.openjava.datatag.tagcol.query.DtCooperationDBParam;
import com.openjava.datatag.tagcol.repository.DtCooperationRepository;
/**
 * tagcol业务层
 * @author hyq
 *
 */
@Service
@Transactional
public class DtCooperationServiceImpl implements DtCooperationService {


	@Resource
	private DtCooperationRepository dtCooperationRepository;
	@Resource
	private DtCooTagcolLimitService dtCooTagcolLimitService;
	@Resource
	private DtTagmCooLogService dtTagmCooLogService;
	@Resource
	private DtTaggingModelService dtTaggingModelService;

	public Page<DtCooperation> query(DtCooperationDBParam params, Pageable pageable){
		Page<DtCooperation> pageresult = dtCooperationRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtCooperation> queryDataOnly(DtCooperationDBParam params, Pageable pageable){
		return dtCooperationRepository.queryDataOnly(params, pageable);
	}
	
	public DtCooperation get(Long id) {
		Optional<DtCooperation> o = dtCooperationRepository.findById(id);
		if(o.isPresent()) {
			DtCooperation m = o.get();
			return m;
		}
		System.out.println("找不到记录DtCooperation："+id);
		return null;
	}
	
	public DtCooperation doSave(DtCooperation m) {
		return dtCooperationRepository.save(m);
	}
	/**
	*描述：新增、修改协作用户
	*/
	public void doCoolSave(DtCooperationDTO req) throws Exception{
		String reqParams = JSONObject.toJSONString(req);
		//记录新增的日志
		List<DtTagmCooLog> addLog = new ArrayList<>();
		//记录删除的日志
		List<DtTagmCooLog> delLog = new ArrayList<>();
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		Long userId = Long.valueOf(userInfo.getUserId());
		List<DtCooTagcolLimitDTO> colLimit = req.getCooTagcolLimitList();
		DtTaggingModel tag = dtTaggingModelService.get(req.getTaggmId());
		List<DtTagmCooLog> conditions = dtTagmCooLogService.findByColId(req.getId());
		if (CollectionUtils.isEmpty(colLimit)) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"cooTagcolLimitList参数必传");
		}
		if (tag==null) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"查无此数据,taggmId参数错误");
		}
		//新增和修改
		DtCooperation col = get(req.getId());
		if(col !=null){
			col.setTaggmId(req.getTaggmId());
			col.setCooUser(req.getCooUser());
			col.setIsNew(false);
			col.setModifyTime(new Date());
			//MyBeanUtils.copyPropertiesNotNull(col,req);
			//EntityClassUtil.dealModifyInfo(col,userInfo);
			col= dtCooperationRepository.save(col);
		}
		else {
			col=new DtCooperation();
			col.setTaggmId(req.getTaggmId());
			col.setCooUser(req.getCooUser());
			col.setCreateUser(userId);
			col.setIsNew(true);
			col.setModifyTime(new Date());
			col.setCreateTime(new Date());
			//MyBeanUtils.copyPropertiesNotNull(col,req);
			//EntityClassUtil.dealCreateInfo(col,userInfo);
			col.setId(ConcurrentSequence.getInstance().getSequence());
			col= dtCooperationRepository.save(col);
		}
		for (int i = 0; i < colLimit.size(); i++) {
			DtCooTagcolLimitDTO record =colLimit.get(i);
			DtCooTagcolLimit newcolLimit =dtCooTagcolLimitService.get(record.getId());
			if (record.getCooId()==null) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"cooTagcolLimitList参数的cooId为空或查无此标签");
			}
			if (!record.getCooId().equals(req.getId())) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"cooTagcolLimitList参数的cooId错误");
			}
			if (newcolLimit !=null) {
				newcolLimit.setUseTagGroup(record.getUseTagGroup());
				newcolLimit.setTagColName(record.getTagColName());
				newcolLimit.setCooId(col.getId());
				newcolLimit.setIsNew(false);
				newcolLimit = dtCooTagcolLimitService.doSave(newcolLimit);
			}else{
				newcolLimit = new DtCooTagcolLimit();
				newcolLimit.setIsNew(true);
				newcolLimit.setUseTagGroup(record.getUseTagGroup());
				newcolLimit.setTagColName(record.getTagColName());
				newcolLimit.setCooId(col.getId());
				newcolLimit.setId(ConcurrentSequence.getInstance().getSequence());
				newcolLimit = dtCooTagcolLimitService.doSave(newcolLimit);
			}

		}
	}
	public void doDelete(Long id) {
		dtCooperationRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtCooperationRepository.deleteById(new Long(items[i]));
		}
	}
}
