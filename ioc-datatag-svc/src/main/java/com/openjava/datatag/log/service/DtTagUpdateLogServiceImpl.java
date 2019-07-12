package com.openjava.datatag.log.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.log.domain.DtTagUpdateLog;
import com.openjava.datatag.log.domain.DtTaggChooseLog;
import com.openjava.datatag.tagmanage.domain.DtTag;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.openjava.datatag.log.query.DtTagUpdateLogDBParam;
import com.openjava.datatag.log.repository.DtTagUpdateLogRepository;
/**
 * DT_TAG_UPDATE_LOG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTagUpdateLogServiceImpl implements DtTagUpdateLogService {
	
	@Resource
	private DtTagUpdateLogRepository dtTagUpdateLogRepository;
	
	public Page<DtTagUpdateLog> query(DtTagUpdateLogDBParam params, Pageable pageable){
		Page<DtTagUpdateLog> pageresult = dtTagUpdateLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagUpdateLog> queryDataOnly(DtTagUpdateLogDBParam params, Pageable pageable){
		return dtTagUpdateLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagUpdateLog get(Long id) {
		Optional<DtTagUpdateLog> o = dtTagUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagUpdateLog："+id);
		return null;
	}
	
	public DtTagUpdateLog doSave(DtTagUpdateLog m) {
		return dtTagUpdateLogRepository.save(m);
	}

	public DtTagUpdateLog loggingUpdate(String modifyContent,String oldContent,DtTag db,Long userId, String ip){
		//日志记录
		DtTagUpdateLog log = new DtTagUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(userId);
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);
		log.setModifyTime(db.getModifyTime());
		log.setTagId(db.getId());
		log.setModifyContent("{\"old\":"+oldContent+ ",\"newRep\":"+ modifyContent+"}");
		log.setIsNew(true);
		return dtTagUpdateLogRepository.save(log);
	}

	public DtTagUpdateLog loggingNew(String modifyContent,DtTag db,Long userId,String ip){
		//日志记录
		DtTagUpdateLog log = new DtTagUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(userId);
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_NEW);
		log.setModifyTime(db.getModifyTime());
		log.setTagId(db.getId());
		log.setModifyContent(modifyContent);
		log.setIsNew(true);
		return dtTagUpdateLogRepository.save(log);
	}

	public DtTagUpdateLog loggingDelete(DtTag db,Long userId,String ip){
		//日志记录
		DtTagUpdateLog log = new DtTagUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(userId);
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_DELETE);
		log.setModifyTime(db.getModifyTime());
		log.setTagId(db.getId());
		//log.setModifyContent(modifyContent);//删除就不需要详情了
		log.setIsNew(true);
		return dtTagUpdateLogRepository.save(log);
	}


	
}
