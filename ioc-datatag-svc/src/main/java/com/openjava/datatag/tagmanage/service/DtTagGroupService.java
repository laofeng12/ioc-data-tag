package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.dto.ShareTopDTO;
import com.openjava.datatag.tagmanage.dto.ShareTopListDTO;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * DT_TAG_GROUP表签组业务层接口
 * @author lch
 *
 */
public interface DtTagGroupService {
	Page<DtTagGroup> query(DtTagGroupDBParam params, Pageable pageable);

	List<DtTagGroup> queryDataOnly(DtTagGroupDBParam params, Pageable pageable);

	/**
	 * 用主键获取标签组
	 * @param id
	 * @return
	 */
	DtTagGroup get(Long id);
	
	DtTagGroup doSave(DtTagGroup m);//保存

	void doSoftDelete(DtTagGroup db,Long userId,String ip)throws Exception ;

	DtTagGroup doNew(DtTagGroup body,Long userId,String ip)throws Exception;

	DtTagGroup doUpdate(DtTagGroup body,DtTagGroup db,Long userId,String ip)throws Exception;
	
//	void doUpdate(DtTagGroup body,DtTagGroup db);

	List<DtTagGroup> getMyTagGroup(Long createUser);

	Page<DtTagGroup> searchMyTagGroup(DtTagGroupDBParam params, Pageable pageable)throws Exception;

	List<ShareTopDTO> getShareTopList(int top);
}
