package com.openjava.datatag.tagcol.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import javax.xml.crypto.Data;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.tagcol.domain.DtCooperation;
import com.openjava.datatag.tagcol.repository.DtCooperationRepository;
import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import com.openjava.datatag.tagmodel.service.DtTagConditionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.query.DtCooTagcolLimitDBParam;
import com.openjava.datatag.tagcol.repository.DtCooTagcolLimitRepository;
/**
 * tagcol业务层
 * @author hyq
 *
 */
@Service
@Transactional
public class DtCooTagcolLimitServiceImpl implements DtCooTagcolLimitService {
	
	@Resource
	private DtCooTagcolLimitRepository dtCooTagcolLimitRepository;
	@Resource
	private DtTagConditionService dtTagConditionService;
	@Resource
	private DtCooperationService dtCooperationService;
	public Page<DtCooTagcolLimit> query(DtCooTagcolLimitDBParam params, Pageable pageable){
		Page<DtCooTagcolLimit> pageresult = dtCooTagcolLimitRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtCooTagcolLimit> queryDataOnly(DtCooTagcolLimitDBParam params, Pageable pageable){
		return dtCooTagcolLimitRepository.queryDataOnly(params, pageable);
	}
	public List<DtCooTagcolLimit> findByColId(Long colId){
		return dtCooTagcolLimitRepository.findByColId(colId);
	}
	public int deleteBycoolId(Long colId){
		return dtCooTagcolLimitRepository.deleteBycoolId(colId);
	};
	public DtCooTagcolLimit get(Long id) {
		Optional<DtCooTagcolLimit> o = dtCooTagcolLimitRepository.findById(id);
		if(o.isPresent()) {
			DtCooTagcolLimit m = o.get();
			return m;
		}
		System.out.println("找不到记录DtCooTagcolLimit："+id);
		return null;
	}
	
	public DtCooTagcolLimit doSave(DtCooTagcolLimit m) {
		return dtCooTagcolLimitRepository.save(m);
	}

	public void doDelete(Long id) {
		dtCooTagcolLimitRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtCooTagcolLimitRepository.deleteById(new Long(items[i]));
		}
	}
	public void completeDtcooRagcol(Long colId){
		DtCooTagcolLimit cooTagcolLimit = dtCooTagcolLimitRepository.findByTagColId(colId);
		if (Constants.DT_COOP_TAGCOL_LIMMIT_YES == cooTagcolLimit.getState()){
			return;
		}
		cooTagcolLimit.setState(Constants.DT_COOP_TAGCOL_LIMMIT_YES);
		dtCooTagcolLimitRepository.save(cooTagcolLimit);
		List<DtCooTagcolLimit> cooTagcolLimitList = dtCooTagcolLimitRepository.findByStateAndCooId(Constants.DT_COOP_TAGCOL_LIMMIT_NO,cooTagcolLimit.getCooId());
		if (CollectionUtils.isNotEmpty(cooTagcolLimitList) && cooTagcolLimitList.size()<=1){
			DtCooperation cooperation = dtCooperationService.get(cooTagcolLimit.getCooId());
			cooperation.setState(Constants.DT_COOPERATION_YES);
			cooperation.setCompleteTime(new Date());
			dtCooperationService.doSave(cooperation);
		}
	}

	public static void main(String[] args) {
		Boolean k= Constants.DT_COOP_TAGCOL_LIMMIT_YES==1L;
		System.out.printf(k.toString());

	}
}
