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
	Page<DtWaitUpdateIndex> query(DtWaitUpdateIndexDBParam params, Pageable pageable);
	
	List<DtWaitUpdateIndex> queryDataOnly(DtWaitUpdateIndexDBParam params, Pageable pageable);
	
	DtWaitUpdateIndex get(Long id);
	
	DtWaitUpdateIndex doSave(DtWaitUpdateIndex m);
	
	void doDelete(Long id);
	void doRemove(String ids);
	/**
	 * 更新模型打标临时表的索引
	 */
	void updateModelIndex();
}
