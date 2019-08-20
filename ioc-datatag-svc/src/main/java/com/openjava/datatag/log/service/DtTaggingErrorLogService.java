package com.openjava.datatag.log.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.domain.DtTaggingErrorLog;
import com.openjava.datatag.log.query.DtTaggingErrorLogDBParam;

/**
 * 模型调度错误日志业务层接口
 * @author zmk
 *
 */
public interface DtTaggingErrorLogService {
	Page<DtTaggingErrorLog> query(DtTaggingErrorLogDBParam params, Pageable pageable);
	
	List<DtTaggingErrorLog> queryDataOnly(DtTaggingErrorLogDBParam params, Pageable pageable);
	
	DtTaggingErrorLog get(Long id);
	
	DtTaggingErrorLog doSave(DtTaggingErrorLog m);
	
	void doDelete(Long id);
	void doRemove(String ids);
}
