package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.domain.DtTagmUpdateLog;
import com.openjava.datatag.log.query.DtTagmUpdateLogDBParam;

/**
 * 标签模型日志业务层接口
 * @author zmk
 *
 */
public interface DtTagmUpdateLogService {
	Page<DtTagmUpdateLog> query(DtTagmUpdateLogDBParam params, Pageable pageable);
	
	List<DtTagmUpdateLog> queryDataOnly(DtTagmUpdateLogDBParam params, Pageable pageable);
	
	DtTagmUpdateLog get(Long id);
	
	DtTagmUpdateLog doSave(DtTagmUpdateLog m);

	DtTagmUpdateLog loggingUpdate(String modifyContent,String oldContent, DtTaggingModel db, String ip);

	DtTagmUpdateLog loggingNew(String modifyContent, DtTaggingModel db,String ip);

	DtTagmUpdateLog loggingDelete(DtTaggingModel db,String ip);


}
