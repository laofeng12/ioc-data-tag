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

	/**
	 * 记录标签修改日志
	 * @param modifyContent
	 * @param oldContent
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTagUpdateLog loggingUpdate(String modifyContent,String oldContent,DtTag db,Long userId, String ip);//记录修改日志

	/**
	 * 标签新增日志
	 * @param modifyContent
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTagUpdateLog loggingNew(String modifyContent,DtTag db,Long userId,String ip);//标签新增日志

	/**
	 * 保存删除日志
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTagUpdateLog loggingDelete(DtTag db,Long userId,String ip);
	
}
