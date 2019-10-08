package com.openjava.datatag.dowload.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.dowload.domain.DownloadQueue;

/**
 * 下载列表数据库访问层
 * @author zmk
 *
 */
public interface DownloadQueueRepository extends DynamicJpaRepository<DownloadQueue, Long>, DownloadQueueRepositoryCustom{
	
}
