package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.exception.APIException;
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
		return dtTagRepository.findByTagsIdAndIsDeleted(tagsId,0L);
	}

	public void doNew(DtTag tag){
		//新增，记录创建时间等
		//设置主键(请根据实际情况修改)
		SequenceService ss = ConcurrentSequence.getInstance();
		tag.setId(ss.getSequence());
		tag.setIsDeleted(0L);
		tag.setIsNew(true);//执行insert
		Date now = new Date();
		tag.setCreateTime(now);
		tag.setModifyTime(now);
		doSave(tag);
	}

	public void doUpdate(DtTag tag,DtTag db){
		//不允许修改父节点，层级和创建时间
		tag.setPreaTagId(null);
		tag.setCreateTime(null);
		tag.setLvl(null);
		tag.setModifyTime(new Date());
		MyBeanUtils.copyPropertiesNotBlank(db, tag);
		db.setIsNew(false);//执行update
		doSave(db);
	}


	public void doSoftDeleteByRootID(Long id,Date now){
		//-为了减少数据库io，先查询该节点下的所有节点的父节点集，在逐层伪删除
		List<Long> pIds = dtTagRepository.findPIdByRootId(id);
		for (Long pId : pIds){
		    dtTagRepository.doSoftDeleteByPreaTagId(pId,now);
        }
	}
	public void doSoftDeleteByDtTag(DtTag tag){
		Date now = new Date();
		//先删除子节点
		doSoftDeleteByRootID(tag.getId(),now);
		//再删除本节点
		tag.setIsDeleted(1L);
		tag.setModifyTime(now);
		doSave(tag);
	}


}
