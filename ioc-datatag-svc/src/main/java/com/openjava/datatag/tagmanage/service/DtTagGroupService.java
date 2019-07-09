package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * DT_TAG_GROUP业务层接口
 * @author lch
 *
 */
public interface DtTagGroupService {
	Page<DtTagGroup> query(DtTagGroupDBParam params, Pageable pageable);
	
	List<DtTagGroup> queryDataOnly(DtTagGroupDBParam params, Pageable pageable);
	
	DtTagGroup get(Long id);
	
	DtTagGroup doSave(DtTagGroup m);

	void doSoftDelete(DtTagGroup db,Long userId,String ip);

	DtTagGroup doNew(DtTagGroup body,Long userId,String ip);

	DtTagGroup doUpdate(DtTagGroup body,DtTagGroup db,Long userId,String ip);
	
}
