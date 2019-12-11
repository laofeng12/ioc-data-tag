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
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTagmCooLog> query(DtTagmCooLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTagmCooLog> queryDataOnly(DtTagmCooLogDBParam params, Pageable pageable);

	/**
	 *
	 * @param id
	 * @return
	 */
	DtTagmCooLog get(Long id);

	/**
	 *
	 * @param m
	 * @return
	 */
	DtTagmCooLog doSave(DtTagmCooLog m);

	/**
	 *
	 * @param colId
	 * @return
	 */
	List<DtTagmCooLog> findByColId(Long colId);

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
}
