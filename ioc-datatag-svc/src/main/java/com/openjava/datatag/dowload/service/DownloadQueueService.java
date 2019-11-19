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
	Page<DownloadQueue> query(DownloadQueueDBParam params, Pageable pageable)throws Exception;
	
	List<DownloadQueue> queryDataOnly(DownloadQueueDBParam params, Pageable pageable);
	
	DownloadQueue get(Long id);
	
	DownloadQueue doSave(DownloadQueue m);
	
	void doDelete(Long id);
	void doRemove(String ids);

	/**
	 * 单独事务的保存。会立即更新到数据库
	 * @param m
	 */
	DownloadQueue doSaveNow(DownloadQueue m);

	DownloadQueue findBybtypeAndBid(String btype,String bid);

	List<DownloadQueue> findByState(Long state);
	void doExport(Long taggingModelId, HttpServletResponse response);
}
