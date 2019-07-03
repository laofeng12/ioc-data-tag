package com.openjava.datatag.tagmodel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

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
	private DtTagConditionRepository dtTagConditionRepository;
	
	public Page<DtTagCondition> query(DtTagConditionDBParam params, Pageable pageable){
		Page<DtTagCondition> pageresult = dtTagConditionRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTagCondition> queryDataOnly(DtTagConditionDBParam params, Pageable pageable){
		return dtTagConditionRepository.queryDataOnly(params, pageable);
	}
	
	public DtTagCondition get(Long id) {
		Optional<DtTagCondition> o = dtTagConditionRepository.findById(id);
		if(o.isPresent()) {
			DtTagCondition m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagCondition："+id);
		return null;
	}
	
	public DtTagCondition doSave(DtTagCondition m) {
		return dtTagConditionRepository.save(m);
	}
	
	public void doDelete(Long id) {
		dtTagConditionRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTagConditionRepository.deleteById(new Long(items[i]));
		}
	}
	public List<DtTagCondition> findByColId(Long colId){
		return dtTagConditionRepository.findByColId(colId);
	}
	public List<DtTagCondition> findByColIds(List<Long> colIds){
		return dtTagConditionRepository.findByColIds(colIds);
	}
}
