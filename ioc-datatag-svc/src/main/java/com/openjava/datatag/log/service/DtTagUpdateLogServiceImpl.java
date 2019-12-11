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
	private DtTagUpdateLogRepository dtTagUpdateLogRepository;//

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<DtTagUpdateLog> query(DtTagUpdateLogDBParam params, Pageable pageable){
		Page<DtTagUpdateLog> pageresult = dtTagUpdateLogRepository.query(params, pageable);
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DtTagUpdateLog> queryDataOnly(DtTagUpdateLogDBParam params, Pageable pageable){
		return dtTagUpdateLogRepository.queryDataOnly(params, pageable);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public DtTagUpdateLog get(Long id) {
		Optional<DtTagUpdateLog> o = dtTagUpdateLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagUpdateLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagUpdateLog："+id);
		return null;
	}

	/**
	 *
	 * @param m
	 * @return
	 */
	public DtTagUpdateLog doSave(DtTagUpdateLog m) {
		return dtTagUpdateLogRepository.save(m);
	}

	/**
	 * 记录标签修改日志
	 * @param modifyContent
	 * @param oldContent
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	public DtTagUpdateLog loggingUpdate(String modifyContent,String oldContent,DtTag db,Long userId, String ip){
		//日志记录
		DtTagUpdateLog log = new DtTagUpdateLog();//新建日志
		log.setId(ConcurrentSequence.getInstance().getSequence());//生成并设置主键
		log.setModifyUser(userId);//设置用户id
		log.setModifyUserip(ip);//设置ip
		log.setModifyType(Constants.DT_TG_LOG_UPDATE);//设置修改类型
		log.setModifyTime(db.getModifyTime());//设置修改时间
		log.setTagId(db.getId());//设置
		log.setModifyContent("{\"old\":"+oldContent+ ",\"newRep\":"+ modifyContent+"}");
		log.setIsNew(true);
		return dtTagUpdateLogRepository.save(log);//保存并返回日志
	}

	/**
	 * 标签新增日志
	 * @param modifyContent
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	public DtTagUpdateLog loggingNew(String modifyContent,DtTag db,Long userId,String ip){
		//日志记录
		DtTagUpdateLog log = new DtTagUpdateLog();//新建标签日志对象
		log.setId(ConcurrentSequence.getInstance().getSequence());//获取并设置id
		log.setModifyUser(userId);//设置修改用户id
		log.setModifyUserip(ip);//设置ip
		log.setModifyType(Constants.DT_TG_LOG_NEW);//设置修改类型
		log.setModifyTime(db.getModifyTime());//设置修改时间
		log.setTagId(db.getId());//设置标签id
		log.setModifyContent(modifyContent);//设置修改内容
		log.setIsNew(true);
		return dtTagUpdateLogRepository.save(log);//保存并返回日志对象
	}
	/**
	 * 保存删除日志
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	public DtTagUpdateLog loggingDelete(DtTag db,Long userId,String ip){
		//日志记录
		DtTagUpdateLog log = new DtTagUpdateLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());//获取并设置id
		log.setModifyUser(userId);//设置修改用户
		log.setModifyUserip(ip);//设置ip
		log.setModifyType(Constants.DT_TG_LOG_DELETE);//设置修改类型
		log.setModifyTime(db.getModifyTime());//设置修改时间
		log.setTagId(db.getId());//设置标签id
		//log.setModifyContent(modifyContent);//删除就不需要详情了
		log.setIsNew(true);
		return dtTagUpdateLogRepository.save(log);//保存并放回日志
	}


	
}
