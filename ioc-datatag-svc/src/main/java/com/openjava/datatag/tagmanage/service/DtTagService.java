package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * DT_TAG业务层接口
 * @author lch
 *
 */
public interface DtTagService {
	Page<DtTag> query(DtTagDBParam params, Pageable pageable);
	
	List<DtTag> queryDataOnly(DtTagDBParam params, Pageable pageable);
	
	DtTag get(Long id);
	List<DtTag> findByTagsId(Long id);
	List<DtTag> findByPreaTagId(Long pId);

	DtTag doSave(DtTag m);

	void doNew(DtTag tag,Long userId,String ip);

	void doUpdate(DtTag tag,DtTag db,Long userId,String ip);

	void doSoftDeleteByTagsID(Long id, Date now);

	void doSoftDeleteByRootID(Long id,Date now);

	void doSoftDeleteByDtTag(DtTag tag,Long userId,String ip);
}
