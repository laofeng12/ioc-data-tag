package com.openjava.datatag.tagmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.log.domain.DtTagUpdateLog;
import com.openjava.datatag.log.service.DtTagUpdateLogService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import com.openjava.datatag.log.repository.DtTagUpdateLogRepository;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * DT_TAG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTagServiceImpl implements DtTagService {
	
	@Resource
	private DtTagRepository dtTagRepository;

	@Resource
	private DtTagUpdateLogService dtTagUpdateLogService;
	@Resource
	DtTaggingModelService dtTaggingModelService;
	@Resource
	private DtTagGroupService dtTagGroupService;
	public Page<DtTag> query(DtTagDBParam params, Pageable pageable){
		Page<DtTag> pageresult = dtTagRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTag> queryDataOnly(DtTagDBParam params, Pageable pageable){
		return dtTagRepository.queryDataOnly(params, pageable);
	}
	
	public DtTag get(Long id) {
		Optional<DtTag> o = dtTagRepository.findById(id);
		if(o.isPresent()) {
			DtTag m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTag："+id);
		return null;
	}
	
	public DtTag doSave(DtTag m) {
		return dtTagRepository.saveAndFlush(m);
	}

	public void doSoftDeleteByTagsID(Long id, Date now){
		// TODO: 2019/9/16  删除标签组时删除画像
		List<Long> ids = findIdsByTagsId(id);
		dtTaggingModelService.stopModelByColIds(ids);//停止模型删除画像
		dtTagRepository.doSoftDeleteByTagsID(id,now);
	}

	public List<DtTag> findByTagsId(Long tagsId){
		//查询所有未删除的标签list
		return dtTagRepository.findByTagsIdAndIsDeleted(tagsId, Constants.PUBLIC_NO);
	}
	public List<DtTag> findByPreaTagId(Long pId){
		return dtTagRepository.findByPreaTagIdAndIsDeleted(pId,Constants.PUBLIC_NO);
	}

	public DtTag doNew(DtTag body,Long userId,String ip){
		String modifyContent = JSONObject.toJSONString(body);
		//新增，记录创建时间等
		//设置主键(请根据实际情况修改)
		SequenceService ss = ConcurrentSequence.getInstance();
		body.setId(ss.getSequence());
		body.setIsDeleted(Constants.PUBLIC_NO);
		body.setIsNew(true);//执行insert
		Date now = new Date();
		body.setCreateTime(now);
		body.setModifyTime(now);
		DtTag db = doSave(body);
		dtTagUpdateLogService.loggingNew(modifyContent,db,userId,ip);
		return db;
	}

	public DtTag doUpdate(DtTag body,DtTag db,Long userId, String ip){
		String oldContent = JSONObject.toJSONString(db);
		String modifyContent = JSONObject.toJSONString(body);
		//不允许修改父节点，层级和创建时间
		body.setPreaTagId(null);
		body.setCreateTime(null);
		body.setLvl(null);
		body.setModifyTime(new Date());
		MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);//执行update
		db = doSave(db);
		dtTagUpdateLogService.loggingUpdate(modifyContent,oldContent,db,userId,ip);
		return db;
	}

	public void doSoftDeleteByDtTag(DtTag db,Long userId,String ip){
		// TODO: 2019/9/16   删除标签时删除画像
		List<Long> ids =  findAllIdsByTagId(db.getId());
		dtTaggingModelService.stopModelByColIds(ids);//停止模型删除画像
		Date now = new Date();
		//先删除子节点
		doSoftDeleteByRootID(db.getId(),now);
		//再删除本节点
		db.setIsDeleted(Constants.PUBLIC_YES);
		db.setModifyTime(now);
		doSave(db);
		dtTagUpdateLogService.loggingDelete(db,userId,ip);
	}


	public void doSoftDeleteByRootID(Long id,Date now){
		//-为了减少数据库io，先查询该节点下的所有节点的父节点集，在逐层伪删除
		List<Long> pIds = dtTagRepository.findPIdByRootId(id);
		for (Long pId : pIds){
		    dtTagRepository.doSoftDeleteByPreaTagId(pId,now);
        }
	}
	public List<DtTag> findByTagIds(List<Long> tagIds){
		return dtTagRepository.findByTagIds(tagIds);
	}

	/**
	 * 根据标签id获取整颗数的节点id
	 */
	public List<Long> findAllIdsByTagId(Long tagId){
		List<Long> tagIds = dtTagRepository.findAllIdsByRootId(tagId);
		return tagIds;
	}
	/**
	 * 根据标签组获取节点ID
	 */
	public List<Long> findIdsByTagsId( Long tagsId){
		return dtTagRepository.findIdsByTagsId(tagsId);
	}
	/**
	 * 获取标签所在的id路径
	 * 例如:标签的id：3，父级id：2,2的父级id:1,则路径为：1,2,3
	 */
	public String getIdpPath(Long tagId){
		String idpath = null;
		DtTag tag = get(tagId);
		if (tag != null){
			if (tag.getPreaTagId()!=null){
				idpath = this.getIdpPath(tag.getPreaTagId());
				idpath += ","+tag.getId();
			}else {
				DtTagGroup tagGroup = dtTagGroupService.get(tag.getTagsId());
				idpath = tagGroup.getId()+","+tag.getId();
			}

		}
		return idpath;
	}
}
