package com.openjava.datatag.tagmodel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.domain.DtTagcolUpdateLog;
import com.openjava.datatag.tagmodel.query.DtTagcolUpdateLogDBParam;
import com.openjava.datatag.tagmodel.repository.DtTagcolUpdateLogRepository;
/**
 * 字段表日志业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtTagcolUpdateLogServiceImpl implements DtTagcolUpdateLogService {
	
	@Resource
	private DtTagcolUpdateLogRepository dtTagcolUpdateLogRepository;
	
	public Page<DtTagcolUpdateLog> query(DtTagcolUpdateLogDBParam params, Pageable pageable){
		Page<DtTagcolUpdateLog> pageresult = dtTagcolUpdateLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagcolUpdateLog> queryDataOnly(DtTagcolUpdateLogDBParam params, Pageable pageable){
		return dtTagcolUpdateLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagcolUpdateLog get(Long id) {
		Optional<DtTagcolUpdateLog> o = dtTagcolUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagcolUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagcolUpdateLog："+id);
		return null;
	}
	
	public DtTagcolUpdateLog doSave(DtTagcolUpdateLog m) {
		return dtTagcolUpdateLogRepository.save(m);
	}
	
	public void doDelete(Long id) {
		dtTagcolUpdateLogRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTagcolUpdateLogRepository.deleteById(new Long(items[i]));
		}
	}
}
