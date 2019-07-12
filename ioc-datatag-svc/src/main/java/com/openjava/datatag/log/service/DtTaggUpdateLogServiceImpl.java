package com.openjava.datatag.log.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.openjava.datatag.log.query.DtTaggUpdateLogDBParam;
import com.openjava.datatag.log.repository.DtTaggUpdateLogRepository;
/**
 * DT_TAGG_UPDATE_LOG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTaggUpdateLogServiceImpl implements DtTaggUpdateLogService {
	
	@Resource
	private DtTaggUpdateLogRepository dtTaggUpdateLogRepository;
	
	public Page<DtTaggUpdateLog> query(DtTaggUpdateLogDBParam params, Pageable pageable){
		Page<DtTaggUpdateLog> pageresult = dtTaggUpdateLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTaggUpdateLog> queryDataOnly(DtTaggUpdateLogDBParam params, Pageable pageable){
		return dtTaggUpdateLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTaggUpdateLog get(Long id) {
		Optional<DtTaggUpdateLog> o = dtTaggUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTaggUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTaggUpdateLog："+id);
		return null;
	}
	
	public DtTaggUpdateLog doSave(DtTaggUpdateLog m) {
		return dtTaggUpdateLogRepository.save(m);
	}

	public DtTaggUpdateLog loggingUpdate(String modifyContent, String oldContent,
										 DtTagGroup db, Long userId, String ip){
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
		return dtTaggUpdateLogRepository.save(log);
	}

	public DtTaggUpdateLog loggingNew(String modifyContent,DtTagGroup db,Long userId,String ip){
		DtTaggUpdateLog log = new DtTaggUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(userId);
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_NEW);
		log.setModifyTime(db.getModifyTime());
		log.setTaggId(db.getId());
		log.setModifyContent(modifyContent);
		log.setIsNew(true);
		return dtTaggUpdateLogRepository.save(log);
	}

	public DtTaggUpdateLog loggingDelete(DtTagGroup db,Long userId,String ip){
		DtTaggUpdateLog log = new DtTaggUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUser(userId);
		log.setModifyUserip(ip);
		log.setModifyType(Constants.DT_TG_LOG_DELETE);
		log.setModifyTime(db.getModifyTime());
		log.setTaggId(db.getId());
		//log.setModifyContent(modifyContent);//删除就不需要详情了
		log.setIsNew(true);
		return dtTaggUpdateLogRepository.save(log);
	}

}
