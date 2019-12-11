package com.openjava.datatag.dowload.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.dowload.domain.DownloadQueue;
import com.openjava.datatag.dowload.query.DownloadQueueDBParam;

import javax.servlet.http.HttpServletResponse;

/**
 * 下载列表业务层接口
 * @author zmk
 *
 */
public interface DownloadQueueService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	Page<DownloadQueue> query(DownloadQueueDBParam params, Pageable pageable)throws Exception;

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DownloadQueue> queryDataOnly(DownloadQueueDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DownloadQueue get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DownloadQueue doSave(DownloadQueue m);

	/**
	 *
	 * @param id
	 */
	void doDelete(Long id);

	/**
	 *
	 * @param ids
	 */
	void doRemove(String ids);

	/**
	 * 单独事务的保存。会立即更新到数据库
	 * @param m
	 */
	DownloadQueue doSaveNow(DownloadQueue m);

	/**
	 *
	 * @param btype
	 * @param bid
	 * @return
	 */
	DownloadQueue findBybtypeAndBid(String btype,String bid);

	/**
	 *
	 * @param state
	 * @return
	 */
	List<DownloadQueue> findByState(Long state);

	/**
	 *
	 * @param taggingModelId
	 * @param response
	 */
	void doExport(Long taggingModelId, HttpServletResponse response);
}
