package com.openjava.datatag.tagmodel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;

import com.openjava.datatag.utils.EntityClassUtil;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import com.openjava.datatag.tagmodel.query.DtTagConditionDBParam;
import com.openjava.datatag.tagmodel.repository.DtTagConditionRepository;
/**
 * 条件设置表业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtTagConditionServiceImpl implements DtTagConditionService {
	
	@Resource
	private DtTagConditionRepository dtTagConditionRepository;//条件设置表数据库访问层


	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<DtTagCondition> query(DtTagConditionDBParam params, Pageable pageable){
		Page<DtTagCondition> pageresult = dtTagConditionRepository.query(params, pageable);//
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DtTagCondition> queryDataOnly(DtTagConditionDBParam params, Pageable pageable){
		return dtTagConditionRepository.queryDataOnly(params, pageable);//
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public DtTagCondition get(Long id) {
		Optional<DtTagCondition> o = dtTagConditionRepository.findById(id);//
		if(o.isPresent()) {
			DtTagCondition m = o.get();//
			return m;
		}
		System.out.println("找不到记录DtTagCondition："+id);
		return null;
	}

	/**
	 *
	 * @param m
	 * @return
	 */
	public DtTagCondition doSave(DtTagCondition m) {
		DtTagCondition db =  dtTagConditionRepository.save(m);//
		//dtTagConditionUpdateLogService.loggingUpdate()
		return db;
	}

	/**
	 *
	 * @param id
	 * @throws Exception
	 */
	public void doDelete(Long id) throws Exception{
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//
		DtTagCondition model = get(id);//
		if (model==null||model.getIsDeleted().equals(Constants.PUBLIC_YES)) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"无此字段或已删除");//
		}
		model.setIsDeleted(Constants.PUBLIC_YES);//
		EntityClassUtil.dealModifyInfo(model,userInfo);//
		dtTagConditionRepository.save(model);//
	}

	/**
	 *
	 * @param ids
	 * @throws Exception
	 */
	public void doRemove(String ids) throws Exception{
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTagConditionRepository.deleteById(new Long(items[i]));//
		}
	}

	/**
	 *
	 * @param colId
	 * @return
	 */
	public List<DtTagCondition> findByColId(Long colId){
		return dtTagConditionRepository.findByColId(colId);//
	}

	/**
	 *
	 * @param colIds
	 * @return
	 */
	public List<DtTagCondition> findByColIds(List<Long> colIds){
		return dtTagConditionRepository.findByColIds(colIds);//
	}

	/**
	 *
	 * @param tagIds
	 * @return
	 */
	public List<DtTagCondition> findByTagIds(List<Long> tagIds){
		return dtTagConditionRepository.findByTagIds(tagIds);//
	}

	/**
	 * 根据模型id和标签id获取条件设置
	 * @param taggingModelId
	 * @param tagId
	 * @return
	 */
	public List<DtTagCondition> findByTaggingModelIdAndColId(Long taggingModelId ,Long tagId){
		return dtTagConditionRepository.findByTaggingModelIdAndColId(taggingModelId,tagId);//
	}
}
