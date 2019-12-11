package com.openjava.datatag.log.service;

import java.util.List;

import com.openjava.datatag.log.domain.DtTaggChooseLog;
import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.log.query.DtTaggChooseLogDBParam;

/**
 * DT_TAGG_CHOOSE_LOG业务层接口
 * @author lch
 *
 */
public interface DtTaggChooseLogService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTaggChooseLog> query(DtTaggChooseLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTaggChooseLog> queryDataOnly(DtTaggChooseLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTaggChooseLog get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtTaggChooseLog doSave(DtTaggChooseLog m);

	/**
	 *
	 * @param userId
	 * @param copiedTaggId
	 * @return
	 */
	Long countChooseToday(Long userId,Long copiedTaggId);

	/**
	 *
	 * @param userId
	 * @param copiedTaggId
	 * @return
	 */
	Long countChoose(Long userId,Long copiedTaggId);

	/**
	 *
	 * @param fromTaggId
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTaggChooseLog loggingChoose(Long fromTaggId, DtTagGroup db, Long userId, String ip);
	
}
