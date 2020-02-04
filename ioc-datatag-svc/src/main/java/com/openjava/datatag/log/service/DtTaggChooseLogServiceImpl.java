package com.openjava.datatag.log.service;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import com.openjava.admin.user.vo.OaUserVO;
import com.openjava.datatag.log.domain.DtTaggChooseLog;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import org.apache.commons.collections.CollectionUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.plugin.sys.vo.OrgVO;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.log.query.DtTaggChooseLogDBParam;
import com.openjava.datatag.log.repository.DtTaggChooseLogRepository;
/**
 * DT_TAGG_CHOOSE_LOG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTaggChooseLogServiceImpl implements DtTaggChooseLogService {
	
	@Resource
	private DtTaggChooseLogRepository dtTaggChooseLogRepository;//

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<DtTaggChooseLog> query(DtTaggChooseLogDBParam params, Pageable pageable){
		Page<DtTaggChooseLog> pageresult = dtTaggChooseLogRepository.query(params, pageable);
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DtTaggChooseLog> queryDataOnly(DtTaggChooseLogDBParam params, Pageable pageable){
		return dtTaggChooseLogRepository.queryDataOnly(params, pageable);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public DtTaggChooseLog get(Long id) {
		Optional<DtTaggChooseLog> o = dtTaggChooseLogRepository.findById(id);
		if(o.isPresent()) {
			DtTaggChooseLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTaggChooseLog："+id);
		return null;
	}

	/**
	 *
	 * @param m
	 * @return
	 */
	public DtTaggChooseLog doSave(DtTaggChooseLog m) {
		return dtTaggChooseLogRepository.save(m);
	}

	/**
	 *
	 * @param userId
	 * @param copiedTaggId
	 * @return
	 */
	public Long countChooseToday(Long userId,Long copiedTaggId){
		return dtTaggChooseLogRepository.countChooseToday(userId,copiedTaggId);
	}

	/**
	 *
	 * @param userId
	 * @param copiedTaggId
	 * @return
	 */
	public Long countChoose(Long userId,Long copiedTaggId){
		return dtTaggChooseLogRepository.countChoose(userId,copiedTaggId);
	}

	/**
	 *
	 * @param fromTaggId
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	public DtTaggChooseLog loggingChoose(Long fromTaggId, DtTagGroup db,Long userId,String ip){
		//日志记录
		OaUserVO userVO = (OaUserVO) SsoContext.getUser();
		DtTaggChooseLog log = new DtTaggChooseLog();
		log.setId(ConcurrentSequence.getInstance().getSequence());
		log.setChooserIp(ip);
		log.setChooseTime(db.getCreateTime());
		log.setChooseUser(userId);
		log.setCopiedTagg(fromTaggId);
		log.setCopyTagg(db.getId());
		log.setChooseUserName(userVO.getUserName());//选用人姓名
		log.setChooseOrgid(userVO.getOrgid());//选用者所在部门
		log.setChooseOrgNmae(userVO.getOrgname());//选用者所在部门名称

		log.setIsNew(true);
		return dtTaggChooseLogRepository.save(log);
	}
}
