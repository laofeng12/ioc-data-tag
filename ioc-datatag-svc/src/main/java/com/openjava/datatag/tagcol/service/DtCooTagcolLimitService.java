package com.openjava.datatag.tagcol.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.query.DtCooTagcolLimitDBParam;

/**
 * tagcol业务层接口
 * @author hyq
 *
 */
public interface DtCooTagcolLimitService {
	Page<DtCooTagcolLimit> query(DtCooTagcolLimitDBParam params, Pageable pageable);
	
	List<DtCooTagcolLimit> queryDataOnly(DtCooTagcolLimitDBParam params, Pageable pageable);
	
	DtCooTagcolLimit get(Long id);
	
	DtCooTagcolLimit doSave(DtCooTagcolLimit m);
	List<DtCooTagcolLimit> findByColId(Long colId);
	void doDelete(Long id);
	void doRemove(String ids);
}
