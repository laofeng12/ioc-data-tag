package com.openjava.datatag.log.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.log.domain.DtTagmUpdateLog;
import com.openjava.datatag.log.query.DtTagmUpdateLogDBParam;
import com.openjava.datatag.log.repository.DtTagmUpdateLogRepository;
/**
 * 标签模型日志业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtTagmUpdateLogServiceImpl implements DtTagmUpdateLogService {
	
	@Resource
	private DtTagmUpdateLogRepository dtTagmUpdateLogRepository;
	
	public Page<DtTagmUpdateLog> query(DtTagmUpdateLogDBParam params, Pageable pageable){
		Page<DtTagmUpdateLog> pageresult = dtTagmUpdateLogRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagmUpdateLog> queryDataOnly(DtTagmUpdateLogDBParam params, Pageable pageable){
		return dtTagmUpdateLogRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagmUpdateLog get(Long id) {
		Optional<DtTagmUpdateLog> o = dtTagmUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagmUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagmUpdateLog："+id);
		return null;
	}
	
	public DtTagmUpdateLog doSave(DtTagmUpdateLog m) {
		return dtTagmUpdateLogRepository.save(m);
	}


	public DtTagmUpdateLog loggingUpdate(String modifyContent,String oldContent, DtTaggingModel db, String ip){
		//日志记录
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(db.getModifyTime());
		log.setModifyUser(db.getModifyUser());
		log.setTaggingModelId(db.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);
		log.setModifyContent("{\"old\":"+oldContent+ ",\"newRep\":"+ modifyContent+"}");
		log.setIsNew(true);
		return dtTagmUpdateLogRepository.save(log);
	}

	public DtTagmUpdateLog loggingNew(String modifyContent,DtTaggingModel db,String ip){
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(db.getModifyTime());
		log.setModifyUser(db.getCreateUser());
		log.setTaggingModelId(db.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_NEW);
		log.setModifyContent(modifyContent);
		log.setIsNew(true);
		return dtTagmUpdateLogRepository.save(log);
	}

	public DtTagmUpdateLog loggingDelete(DtTaggingModel db,String ip){
		DtTagmUpdateLog log = new DtTagmUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setModifyUserip(ip);
		log.setModifyTime(db.getModifyTime());
		log.setModifyUser(db.getModifyUser());
		log.setTaggingModelId(db.getTaggingModelId());
		log.setModifyType(Constants.DT_TG_LOG_DELETE);
		//log.setModifyContent();//删除就不需要保存内容了
		log.setIsNew(true);
		return dtTagmUpdateLogRepository.save(log);
	}


}
