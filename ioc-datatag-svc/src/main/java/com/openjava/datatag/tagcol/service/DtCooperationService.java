package com.openjava.datatag.tagcol.service;

import java.util.List;

import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.dto.DtCooperationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.openjava.datatag.tagcol.domain.DtCooperation;
import com.openjava.datatag.tagcol.query.DtCooperationDBParam;

/**
 * tagcol业务层接口
 * @author hyq
 *
 */
public interface DtCooperationService {
	Page<DtCooperation> query(DtCooperationDBParam params, Pageable pageable);
	
	List<DtCooperation> queryDataOnly(DtCooperationDBParam params, Pageable pageable);
	
	DtCooperation get(Long id);
	
	DtCooperation doSave(DtCooperation m);
	void doCoolSave(DtCooperationDTO m)throws Exception;
	void doDelete(Long id);
	void doRemove(String ids);
}
