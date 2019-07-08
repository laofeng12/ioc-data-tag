package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagGroupRepository;
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
 * DT_TAG_GROUP业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTagGroupServiceImpl implements DtTagGroupService {
	
	@Resource
	private DtTagGroupRepository dtTagGroupRepository;

	@Resource
	private  DtTagService dtTagService;
	
	public Page<DtTagGroup> query(DtTagGroupDBParam params, Pageable pageable){
		Page<DtTagGroup> pageresult = dtTagGroupRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagGroup> queryDataOnly(DtTagGroupDBParam params, Pageable pageable){
		return dtTagGroupRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagGroup get(Long id) {
		Optional<DtTagGroup> o = dtTagGroupRepository.findById(id);
		if(o.isPresent()) {
			DtTagGroup m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagGroup："+id);
		return null;
	}
	
	public DtTagGroup doSave(DtTagGroup m) {
		return dtTagGroupRepository.save(m);
	}

	@Transactional
	public void doSoftDelete(DtTagGroup m){
		m.setIsDeleted(1L);
		//批量修改标签表的删除标识
		dtTagService.doSoftDeleteByTagsID(m.getId(),m.getModifyTime());
		//修改标签组表的删除标识
		doSave(m);
	}

	public void doNew(DtTagGroup body,Long userId){
		//新增，记录创建时间等
		//设置主键(请根据实际情况修改)
		SequenceService ss = ConcurrentSequence.getInstance();
		body.setId(ss.getSequence());
		body.setIsNew(true);//执行insert
		body.setCreateUser(userId);
		Date now = new Date();
		body.setCreateTime(now);
		body.setModifyTime(now);
		body.setIsDeleted(0L);
		body.setIsShare(0L);
		body.setPopularity(0L);
		doSave(body);
	}

	public void doUpdate(DtTagGroup body,DtTagGroup db){
		//Create* 应该保持不变，Modify更新
		body.setCreateUser(db.getCreateUser());
		body.setCreateTime(db.getCreateTime());
		body.setModifyTime(new Date());
		MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);
		doSave(db);
	}

	public List<DtTagGroup> getMyTagGroup(Long createUser){
		return dtTagGroupRepository.getMyTagGroup(createUser);
	}

}
