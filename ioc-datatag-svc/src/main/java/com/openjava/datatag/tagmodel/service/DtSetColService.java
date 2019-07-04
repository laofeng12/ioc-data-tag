package com.openjava.datatag.tagmodel.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.query.DtSetColDBParam;

/**
 * 字段表业务层接口
 * @author zmk
 *
 */
public interface DtSetColService {
	Page<DtSetCol> query(DtSetColDBParam params, Pageable pageable);
	
	List<DtSetCol> queryDataOnly(DtSetColDBParam params, Pageable pageable);
	
	DtSetCol get(Long id);
	
	DtSetCol doSave(DtSetCol m);
	
	void doDelete(Long id)throws Exception;
	void doRemove(String ids)throws Exception;
}
