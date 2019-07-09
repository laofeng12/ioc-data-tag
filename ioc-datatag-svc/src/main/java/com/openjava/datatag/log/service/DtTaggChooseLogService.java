package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.log.domain.DtTaggChooseLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.query.DtTaggChooseLogDBParam;

/**
 * DT_TAGG_CHOOSE_LOG业务层接口
 * @author lch
 *
 */
public interface DtTaggChooseLogService {
	Page<DtTaggChooseLog> query(DtTaggChooseLogDBParam params, Pageable pageable);
	
	List<DtTaggChooseLog> queryDataOnly(DtTaggChooseLogDBParam params, Pageable pageable);
	
	DtTaggChooseLog get(Long id);
	
	DtTaggChooseLog doSave(DtTaggChooseLog m);

	Long CountChooseToday(Long userId,Long copiedTaggId);
	
}
