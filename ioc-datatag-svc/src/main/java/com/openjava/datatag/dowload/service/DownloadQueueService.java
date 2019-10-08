package com.openjava.datatag.dowload.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.dowload.domain.DownloadQueue;
import com.openjava.datatag.dowload.query.DownloadQueueDBParam;

/**
 * 下载列表业务层接口
 * @author zmk
 *
 */
public interface DownloadQueueService {
	Page<DownloadQueue> query(DownloadQueueDBParam params, Pageable pageable);
	
	List<DownloadQueue> queryDataOnly(DownloadQueueDBParam params, Pageable pageable);
	
	DownloadQueue get(Long id);
	
	DownloadQueue doSave(DownloadQueue m);
	
	void doDelete(Long id);
	void doRemove(String ids);
}
