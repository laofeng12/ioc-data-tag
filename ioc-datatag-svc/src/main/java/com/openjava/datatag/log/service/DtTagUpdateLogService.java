package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.log.domain.DtTagUpdateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.query.DtTagUpdateLogDBParam;

/**
 * DT_TAG_UPDATE_LOG业务层接口
 * @author lch
 *
 */
public interface DtTagUpdateLogService {
	Page<DtTagUpdateLog> query(DtTagUpdateLogDBParam params, Pageable pageable);
	
	List<DtTagUpdateLog> queryDataOnly(DtTagUpdateLogDBParam params, Pageable pageable);
	
	DtTagUpdateLog get(Long id);
	
	DtTagUpdateLog doSave(DtTagUpdateLog m);
	
}
