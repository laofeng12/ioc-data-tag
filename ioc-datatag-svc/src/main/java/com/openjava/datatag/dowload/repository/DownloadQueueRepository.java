package com.openjava.datatag.dowload.repository;

import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import com.openjava.datatag.dowload.domain.DownloadQueue;

import java.util.List;

/**
 * 下载列表数据库访问层
 * @author zmk
 *
 */
public interface DownloadQueueRepository extends DynamicJpaRepository<DownloadQueue, Long>, DownloadQueueRepositoryCustom{
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
    @Query(value = "from DownloadQueue t where t.state=:state order by t.createTime desc")
    List<DownloadQueue> findByState(Long state);
}
