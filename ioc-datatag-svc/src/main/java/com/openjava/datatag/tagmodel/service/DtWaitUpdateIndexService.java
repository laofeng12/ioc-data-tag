package com.openjava.datatag.tagmodel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtWaitUpdateIndex;
import com.openjava.datatag.tagmodel.query.DtWaitUpdateIndexDBParam;

/**
 * 更新索引表业务层接口
 * @author zmk
 *
 */
public interface DtWaitUpdateIndexService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtWaitUpdateIndex> query(DtWaitUpdateIndexDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtWaitUpdateIndex> queryDataOnly(DtWaitUpdateIndexDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtWaitUpdateIndex get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtWaitUpdateIndex doSave(DtWaitUpdateIndex m);

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
	 * 获取待更新的模型
	 * @param runState
	 * @return
	 */
	List<DtWaitUpdateIndex> getByRunState(Long runState);
	/**
	 * 更新模型打标临时表的索引
	 */
	void updateModelIndex(List<DtWaitUpdateIndex> waitList);
}
