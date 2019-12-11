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
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTaggUpdateLog> query(DtTaggUpdateLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTaggUpdateLog> queryDataOnly(DtTaggUpdateLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTaggUpdateLog get(Long id);

	/**
	 *
	 * @param modifyContent
	 * @param oldContent
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	//DtTaggUpdateLog doSave(DtTaggUpdateLog m);
	DtTaggUpdateLog loggingUpdate(String modifyContent, String oldContent, DtTagGroup db, Long userId, String ip);

	/**
	 *
	 * @param modifyContent
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTaggUpdateLog loggingNew(String modifyContent,DtTagGroup db,Long userId,String ip);

	/**
	 *
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTaggUpdateLog loggingDelete(DtTagGroup db,Long userId,String ip);

}
