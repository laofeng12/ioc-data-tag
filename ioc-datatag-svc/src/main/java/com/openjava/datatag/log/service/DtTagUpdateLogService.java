package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.log.domain.DtTagUpdateLog;
import com.openjava.datatag.log.domain.DtTagmUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
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

	DtTagUpdateLog loggingUpdate(String modifyContent,String oldContent,DtTag db,Long userId, String ip);

	DtTagUpdateLog loggingNew(String modifyContent,DtTag db,Long userId,String ip);

	DtTagUpdateLog loggingDelete(DtTag db,Long userId,String ip);
	
}
