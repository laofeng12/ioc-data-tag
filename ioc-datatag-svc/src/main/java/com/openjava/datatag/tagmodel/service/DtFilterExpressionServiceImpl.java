package com.openjava.datatag.tagmodel.service;

import com.openjava.datatag.tagmodel.domain.DtFilterExpression;
import com.openjava.datatag.tagmodel.repository.DtFilterExpressionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
/**
 * 规制表达式业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtFilterExpressionServiceImpl implements DtFilterExpressionService {
	
	@Resource
	private DtFilterExpressionRepository dtFilterExpressionRepository;//规制表达式数据库访问层
	
//	public Page<DtFilterExpression> query(DtFilterExpressionDBParam params, Pageable pageable){
//		Page<DtFilterExpression> pageresult = dtFilterExpressionRepository.query(params, pageable);
//		return pageresult;
//	}
//
//	public List<DtFilterExpression> queryDataOnly(DtFilterExpressionDBParam params, Pageable pageable){
//		return dtFilterExpressionRepository.queryDataOnly(params, pageable);
//	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public DtFilterExpression get(Long id) {
		Optional<DtFilterExpression> o = dtFilterExpressionRepository.findById(id);//根据主键获取规则表达式
		if(o.isPresent()) {
			DtFilterExpression m = o.get();
			return m;
		}
		System.out.println("找不到记录DtFilterExpression："+id);
		return null;
	}

	/**
	 *
	 * @param m
	 * @return
	 */
	public DtFilterExpression doSave(DtFilterExpression m) {
		return dtFilterExpressionRepository.save(m);//保存
	}

	/**
	 *
	 * @param id
	 */
	public void doDelete(Long id) {
		dtFilterExpressionRepository.deleteById(id);//删除
	}

	/**
	 *
	 * @param ids
	 */
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtFilterExpressionRepository.deleteById(new Long(items[i]));//
		}
	}

	/**
	 *
	 * @param tagConditionId
	 * @return
	 */
	public List<DtFilterExpression> findByTagConditionId(Long tagConditionId){
		return dtFilterExpressionRepository.findByTagConditionId(tagConditionId);
	}

	/**
	 *
	 * @param tagConditionId
	 */
	public void doRemoveByTagConditionId(Long tagConditionId){
		dtFilterExpressionRepository.doRemoveByTagConditionId(tagConditionId);
	}
}
