package com.openjava.datatag.tagmodel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.domain.DtTagConditionUpdateLog;
import com.openjava.datatag.tagmodel.query.DtTagConditionUpdateLogDBParam;
import com.openjava.datatag.tagmodel.repository.DtTagConditionUpdateLogRepository;
/**
 * 条件设置表的设置日志业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtTagConditionUpdateLogServiceImpl implements DtTagConditionUpdateLogService {
	
	@Resource
	private DtTagConditionUpdateLogRepository dtTagConditionUpdateLogRepository;
	
	public Page<DtTagConditionUpdateLog> query(DtTagConditionUpdateLogDBParam params, Pageable pageable){
		Page<DtTagConditionUpdateLog> pageresult = dtTagConditionUpdateLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagConditionUpdateLog> queryDataOnly(DtTagConditionUpdateLogDBParam params, Pageable pageable){
		return dtTagConditionUpdateLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagConditionUpdateLog get(Long id) {
		Optional<DtTagConditionUpdateLog> o = dtTagConditionUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagConditionUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagConditionUpdateLog："+id);
		return null;
	}
	
	public DtTagConditionUpdateLog doSave(DtTagConditionUpdateLog m) {
		return dtTagConditionUpdateLogRepository.save(m);
	}
	
	public void doDelete(Long id) {
		dtTagConditionUpdateLogRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTagConditionUpdateLogRepository.deleteById(new Long(items[i]));
		}
	}
}
