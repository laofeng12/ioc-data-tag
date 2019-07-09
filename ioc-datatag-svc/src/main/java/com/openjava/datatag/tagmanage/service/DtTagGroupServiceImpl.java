package com.openjava.datatag.tagmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagGroupRepository;
import com.openjava.datatag.log.repository.DtTaggUpdateLogRepository;
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

	@Resource
	private DtTaggUpdateLogRepository dtTaggUpdateLogRepository;
	
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


	public void doSoftDelete(DtTagGroup db,Long userId,String ip){
		db.setModifyTime(new Date());
		db.setIsDeleted(Constants.DT_TG_DELETED);
		//批量修改标签表的删除标识
		dtTagService.doSoftDeleteByTagsID(db.getId(),db.getModifyTime());
		//修改标签组表的删除标识
		doSave(db);

		//日志记录
		DtTaggUpdateLog log = new DtTaggUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(userId);
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_DELETE);
		log.setModifyTime(db.getModifyTime());
		log.setTaggId(db.getId());
		//log.setModifyContent(modifyContent);//删除就不需要详情了
		log.setIsNew(true);
		dtTaggUpdateLogRepository.save(log);
	}

	public DtTagGroup doNew(DtTagGroup body,Long userId,String ip){
		String modifyContent = JSONObject.toJSONString(body);
		//新增，记录创建时间等
		//设置主键(请根据实际情况修改)
		SequenceService ss = ConcurrentSequence.getInstance();
		body.setId(ss.getSequence());
		body.setIsNew(true);//执行insert
		body.setCreateUser(userId);
		Date now = new Date();
		body.setCreateTime(now);
		body.setModifyTime(now);
		body.setIsDeleted(Constants.DT_TG_EXIST);
		body.setIsShare(Constants.DT_TG_PRIVATE);
		body.setPopularity(0L);
		DtTagGroup db = dtTagGroupRepository.save(body);

		//日志记录
		DtTaggUpdateLog log = new DtTaggUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(body.getCreateUser());
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_NEW);
		log.setModifyTime(body.getModifyTime());
		log.setTaggId(body.getId());
		log.setModifyContent(modifyContent);
		log.setIsNew(true);
		dtTaggUpdateLogRepository.save(log);

		return  db;
	}

	public DtTagGroup doUpdate(DtTagGroup body,DtTagGroup db,Long userId,String ip){
		String oldContent = JSONObject.toJSONString(db);
		String modifyContent = JSONObject.toJSONString(body);
		//Create* 应该保持不变，Modify更新
		body.setCreateUser(db.getCreateUser());
		body.setCreateTime(db.getCreateTime());
		body.setModifyTime(new Date());
		MyBeanUtils.copyPropertiesNotBlank(db, body);
		db.setIsNew(false);
		DtTagGroup newdb = doSave(db);

		//日志记录
		DtTaggUpdateLog log = new DtTaggUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(userId);
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);
		log.setModifyTime(db.getModifyTime());
		log.setTaggId(db.getId());
		log.setModifyContent("{\"old\":"+oldContent+ ",\"newRep\":"+ modifyContent+"}");
		log.setIsNew(true);
		dtTaggUpdateLogRepository.save(log);

		return newdb;
	}


}
