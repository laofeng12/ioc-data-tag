package com.openjava.datatag.tagmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.log.domain.DtTagUpdateLog;
import com.openjava.datatag.log.service.DtTagUpdateLogService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import com.openjava.datatag.log.repository.DtTagUpdateLogRepository;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
		dtTagRepository.doSoftDeleteByTagsID(id,now);
	}

	public List<DtTag> findByTagsId(Long tagsId){
		//查询所有未删除的标签list
		return dtTagRepository.findByTagsIdAndIsDeleted(tagsId, Constants.DT_TG_EXIST);
	}

	public void doNew(DtTag body,Long userId,String ip){
		String modifyContent = JSONObject.toJSONString(body);
		//新增，记录创建时间等
		//设置主键(请根据实际情况修改)
		SequenceService ss = ConcurrentSequence.getInstance();
		body.setId(ss.getSequence());
		body.setIsDeleted(Constants.DT_TG_EXIST);
		body.setIsNew(true);//执行insert
		Date now = new Date();
		body.setCreateTime(now);
		body.setModifyTime(now);
		DtTag db = doSave(body);
		dtTagUpdateLogService.loggingNew(modifyContent,db,userId,ip);
	}

	public void doUpdate(DtTag body,DtTag db,Long userId, String ip){
		String oldContent = JSONObject.toJSONString(db);
		String modifyContent = JSONObject.toJSONString(body);
		//不允许修改父节点，层级和创建时间
		body.setPreaTagId(null);
		body.setCreateTime(null);
		body.setLvl(null);
		body.setModifyTime(new Date());
		MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);//执行update
		doSave(db);
		dtTagUpdateLogService.loggingUpdate(modifyContent,oldContent,db,userId,ip);

	}

	public void doSoftDeleteByDtTag(DtTag db,Long userId,String ip){
		Date now = new Date();
		//先删除子节点
		doSoftDeleteByRootID(db.getId(),now);
		//再删除本节点
		db.setIsDeleted(Constants.DT_TG_DELETED);
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



}
