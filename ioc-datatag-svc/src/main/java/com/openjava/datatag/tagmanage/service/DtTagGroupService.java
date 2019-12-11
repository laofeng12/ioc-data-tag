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
	/**
	 * 分页查询
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTagGroup> query(DtTagGroupDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTagGroup> queryDataOnly(DtTagGroupDBParam params, Pageable pageable);

	/**
	 * 用主键获取标签组
	 * @param id
	 * @return
	 */
	DtTagGroup get(Long id);

	/**
	 * 保存标签组
	 * @param m
	 * @return
	 */
	DtTagGroup doSave(DtTagGroup m);//保存

	/**
	 *
	 * @param db
	 * @param userId
	 * @param ip
	 * @throws Exception
	 */
	void doSoftDelete(DtTagGroup db,Long userId,String ip)throws Exception ;

	/**
	 *
	 * @param body
	 * @param userId
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	DtTagGroup doNew(DtTagGroup body,Long userId,String ip)throws Exception;

	/**
	 *
	 * @param body
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	DtTagGroup doUpdate(DtTagGroup body,DtTagGroup db,Long userId,String ip)throws Exception;
	
//	void doUpdate(DtTagGroup body,DtTagGroup db);

	/**
	 *
	 * @param createUser
	 * @return
	 */
	List<DtTagGroup> getMyTagGroup(Long createUser);

	/**
	 * 分页查询标签组数据
	 * @param params
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	Page<DtTagGroup> searchMyTagGroup(DtTagGroupDBParam params, Pageable pageable)throws Exception;

	/**
	 *
	 * @param top
	 * @return
	 */
	List<ShareTopDTO> getShareTopList(int top);
}
