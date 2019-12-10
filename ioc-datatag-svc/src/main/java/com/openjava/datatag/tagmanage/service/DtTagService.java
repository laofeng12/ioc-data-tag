package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.utils.tree.TagDTOTreeNodeShow;
import io.lettuce.core.dynamic.annotation.Param;
import org.ljdp.component.result.SuccessMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * DT_TAG标签业务层接口
 * @author lch
 *
 */
public interface DtTagService {
	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<DtTag> query(DtTagDBParam params, Pageable pageable);

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	List<DtTag> queryDataOnly(DtTagDBParam params, Pageable pageable);

	/**
	 * 根据标签组ID获取
	 * @param id
	 * @return
	 * @throws Exception
	 */
	TagDTOTreeNodeShow getTree(Long id) throws Exception;

	/**
	 * 根据id获取标签
	 * @param id
	 * @return
	 */
	DtTag get(Long id);

	/**
	 * 根据标签组id获取标齐全
	 * @param id
	 * @return
	 */
	List<DtTag> findByTagsId(Long id);

	/**
	 * 根据父标签id获取标签
	 * @param pId
	 * @return
	 */
	List<DtTag> findByPreaTagId(Long pId);

	/**
	 * 保存
	 * @param m
	 * @return
	 */
	DtTag doSave(DtTag m);

	/**
	 * 修改
	 * @param body
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	SuccessMessage doSaveOrEdit(DtTag body,String ip)throws Exception;//修改标签

	/**
	 *
	 * @param tag
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTag doNew(DtTag tag,Long userId,String ip);

	/**
	 *
	 * @param tag
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	DtTag doUpdate(DtTag tag,DtTag db,Long userId,String ip);

	/**
	 *
	 * @param id
	 * @param now
	 */
	void doSoftDeleteByTagsID(Long id, Date now);

	/**
	 *
	 * @param id
	 * @param now
	 */
	void doSoftDeleteByRootID(Long id,Date now);

	/**
	 * 软删除
	 * @param tag
	 * @param userId
	 * @param ip
	 * @throws Exception
	 */
	void doSoftDeleteByDtTag(DtTag tag,Long userId,String ip)throws Exception;

	/**
	 * 根据主键批量获取标签
	 * @param tagIds
	 * @return
	 */
	List<DtTag> findByTagIds(List<Long> tagIds);

	/**
	 * 根据标签id获取整颗数的节点id
	 */
	List<Long> findAllIdsByTagId(Long tagId);
	/**
	 * 根据标签组获取节点ID
	 */
	List<Long> findIdsByTagsId( Long tagsId);
	/**
	 * 获取标签所在的id路径
	 * 例如:标签的id：3，父级id：2,2的父级id:1,则路径为：1,2,3
	 */
	String getIdpPath(Long tagId);
}
