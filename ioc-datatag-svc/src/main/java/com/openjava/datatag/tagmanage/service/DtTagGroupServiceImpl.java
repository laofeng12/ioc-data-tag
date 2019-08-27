package com.openjava.datatag.tagmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.log.service.DtTaggUpdateLogService;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagGroupRepository;
import org.apache.commons.collections.CollectionUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
	private DtTaggUpdateLogService dtTaggUpdateLogService;
	
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
		db.setIsDeleted(Constants.PUBLIC_YES);
		//批量修改标签表的删除标识
		dtTagService.doSoftDeleteByTagsID(db.getId(),db.getModifyTime());
		//修改标签组表的删除标识
		doSave(db);
		//日志记录
		dtTaggUpdateLogService.loggingDelete(db,userId,ip);
	}

	public DtTagGroup doNew(DtTagGroup body,Long userId,String ip){
		if (body.getTagsName() == null){
			body.setTagsName("新建标签组");
		}
		if (body.getSynopsis() == null){
			body.setSynopsis("未填写简介");
		}
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
		body.setIsDeleted(Constants.PUBLIC_NO);
		body.setIsShare(Constants.PUBLIC_NO);
		body.setPopularity(0L);
		DtTagGroup db = dtTagGroupRepository.save(body);

		//日志记录
		dtTaggUpdateLogService.loggingNew(modifyContent,db,userId,ip);

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
		dtTaggUpdateLogService.loggingUpdate(modifyContent,oldContent,db,userId,ip);
		return newdb;
	}

	public List<DtTagGroup> getMyTagGroup(Long createUser){
		return dtTagGroupRepository.getMyTagGroup(createUser);
	}

	public Page<DtTagGroup> searchMyTagGroup(DtTagGroupDBParam params, Pageable pageable){
		Page<DtTagGroup> result = query(params,pageable);
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		if (CollectionUtils.isNotEmpty(result.getContent())){
			Long maxPopularity = dtTagGroupRepository.findMaxPopularityBytagsIdAAndIsDeleted(Long.valueOf(userInfo.getUserId()),Constants.PUBLIC_NO);
			for (DtTagGroup tgg: result){
//				Long plvl = dtTagGroupRepository.findPopuLvlByTagsId(tgg.getId());
//				tgg.setPopularityLevel(plvl);
				if (tgg.getPopularity()==null){
					tgg.setPercentage(0L);
				}else {
					BigDecimal big = new BigDecimal(tgg.getPopularity()).divide(new BigDecimal(getDenominator(tgg.getPopularity())) ,BigDecimal.ROUND_UP).setScale(0,BigDecimal.ROUND_UP);
					tgg.setPercentage(big.longValueExact());
				}
			}

		}
		return result;
	}

	/**
	 * 工具数字获取随动分母
	 */
	private static long getDenominator(Long maxPopularity){
		long denominator = 10L;
		if (maxPopularity==null){
			return denominator;
		}
		while (maxPopularity/10>=10 || (maxPopularity>10 && maxPopularity%10>0)){
			maxPopularity = maxPopularity/10+maxPopularity%10;
			denominator = denominator*10;
		}
		return denominator;
	}

	public static void main(String[] args) {
		System.out.println(getDenominator(10001L));
	}

}
