package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.log.domain.DtTagcolUpdateLog;
import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtSetCol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.query.DtTaggUpdateLogDBParam;

/**
 * DT_TAGG_UPDATE_LOG业务层接口
 * 仅供查询
 * @author lch
 *
 */
public interface DtTaggUpdateLogService {
	Page<DtTaggUpdateLog> query(DtTaggUpdateLogDBParam params, Pageable pageable);
	
	List<DtTaggUpdateLog> queryDataOnly(DtTaggUpdateLogDBParam params, Pageable pageable);
	
	DtTaggUpdateLog get(Long id);
	
	//DtTaggUpdateLog doSave(DtTaggUpdateLog m);
	DtTaggUpdateLog loggingUpdate(String modifyContent, String oldContent, DtTagGroup db, Long userId, String ip);

	DtTaggUpdateLog loggingNew(String modifyContent,DtTagGroup db,Long userId,String ip);

	DtTaggUpdateLog loggingDelete(DtTagGroup db,Long userId,String ip);

}
