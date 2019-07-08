package com.openjava.datatag.log.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.openjava.datatag.log.domain.DtTagUpdateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.openjava.datatag.log.query.DtTagUpdateLogDBParam;
import com.openjava.datatag.log.repository.DtTagUpdateLogRepository;
/**
 * DT_TAG_UPDATE_LOG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTagUpdateLogServiceImpl implements DtTagUpdateLogService {
	
	@Resource
	private DtTagUpdateLogRepository dtTagUpdateLogRepository;
	
	public Page<DtTagUpdateLog> query(DtTagUpdateLogDBParam params, Pageable pageable){
		Page<DtTagUpdateLog> pageresult = dtTagUpdateLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagUpdateLog> queryDataOnly(DtTagUpdateLogDBParam params, Pageable pageable){
		return dtTagUpdateLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagUpdateLog get(Long id) {
		Optional<DtTagUpdateLog> o = dtTagUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagUpdateLog："+id);
		return null;
	}
	
	public DtTagUpdateLog doSave(DtTagUpdateLog m) {
		return dtTagUpdateLogRepository.save(m);
	}
	
}
