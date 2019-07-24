package com.openjava.datatag.tagcol.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagcol.domain.DtTagmCooLog;
import com.openjava.datatag.tagcol.query.DtTagmCooLogDBParam;

/**
 * tagcol业务层接口
 * @author hyq
 *
 */
public interface DtTagmCooLogService {
	Page<DtTagmCooLog> query(DtTagmCooLogDBParam params, Pageable pageable);
	
	List<DtTagmCooLog> queryDataOnly(DtTagmCooLogDBParam params, Pageable pageable);
	
	DtTagmCooLog get(Long id);
	
	DtTagmCooLog doSave(DtTagmCooLog m);
	List<DtTagmCooLog> findByColId(Long colId);
	void doDelete(Long id);
	void doRemove(String ids);
}
