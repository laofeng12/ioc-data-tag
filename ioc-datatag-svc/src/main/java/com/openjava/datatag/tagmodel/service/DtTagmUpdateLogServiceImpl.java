package com.openjava.datatag.tagmodel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.domain.DtTagmUpdateLog;
import com.openjava.datatag.tagmodel.query.DtTagmUpdateLogDBParam;
import com.openjava.datatag.tagmodel.repository.DtTagmUpdateLogRepository;
/**
 * 标签模型日志业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtTagmUpdateLogServiceImpl implements DtTagmUpdateLogService {
	
	@Resource
	private DtTagmUpdateLogRepository dtTagmUpdateLogRepository;
	
	public Page<DtTagmUpdateLog> query(DtTagmUpdateLogDBParam params, Pageable pageable){
		Page<DtTagmUpdateLog> pageresult = dtTagmUpdateLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagmUpdateLog> queryDataOnly(DtTagmUpdateLogDBParam params, Pageable pageable){
		return dtTagmUpdateLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagmUpdateLog get(Long id) {
		Optional<DtTagmUpdateLog> o = dtTagmUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagmUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagmUpdateLog："+id);
		return null;
	}
	
	public DtTagmUpdateLog doSave(DtTagmUpdateLog m) {
		return dtTagmUpdateLogRepository.save(m);
	}
	
	public void doDelete(Long id) {
		dtTagmUpdateLogRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTagmUpdateLogRepository.deleteById(new Long(items[i]));
		}
	}
}
