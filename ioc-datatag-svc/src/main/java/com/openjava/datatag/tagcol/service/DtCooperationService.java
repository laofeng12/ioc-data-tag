package com.openjava.datatag.tagcol.service;

import java.util.List;
import java.util.Map;

import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.dto.DtCooperationDTO;
import com.openjava.datatag.tagcol.dto.DtCooperationListDTO;
import com.openjava.datatag.tagcol.dto.DtCooperationModelDTO;
import com.openjava.datatag.tagcol.dto.DtCooperationSetCol;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
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
	List<DtCooperationModelDTO> findUserModelByUserId(Long userId);
	List<DtCooperationSetCol> findUserModelCooField(Long userId,Long modelId);
	List<DtTagGroup>findCurrentUserTagGroup(Long modelId,String colField);
	DtCooperation get(Long id);
	Long findCooUserTagGroup(Long userId,Long tagGroupId);
	DtCooperation doSave(DtCooperation m);
	void doCoolSave(DtCooperationDTO m)throws Exception;
	void doCoolListSave(List<DtCooperationListDTO> list)throws Exception;
	void doDelete(Long id);
	void doRemove(String ids);

}
