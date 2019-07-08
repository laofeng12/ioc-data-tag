package com.openjava.datatag.log.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.openjava.datatag.log.domain.DtTaggChooseLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.log.query.DtTaggChooseLogDBParam;
import com.openjava.datatag.log.repository.DtTaggChooseLogRepository;
/**
 * DT_TAGG_CHOOSE_LOG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTaggChooseLogServiceImpl implements DtTaggChooseLogService {
	
	@Resource
	private DtTaggChooseLogRepository dtTaggChooseLogRepository;
	
	public Page<DtTaggChooseLog> query(DtTaggChooseLogDBParam params, Pageable pageable){
		Page<DtTaggChooseLog> pageresult = dtTaggChooseLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTaggChooseLog> queryDataOnly(DtTaggChooseLogDBParam params, Pageable pageable){
		return dtTaggChooseLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTaggChooseLog get(Long id) {
		Optional<DtTaggChooseLog> o = dtTaggChooseLogRepository.findById(id);
		if(o.isPresent()) {
			DtTaggChooseLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTaggChooseLog："+id);
		return null;
	}
	
	public DtTaggChooseLog doSave(DtTaggChooseLog m) {
		return dtTaggChooseLogRepository.save(m);
	}

	public Long CountChooseToday(Long userId,Long copiedTaggId){
		return dtTaggChooseLogRepository.CountChooseToday(userId,copiedTaggId);
	}
}
