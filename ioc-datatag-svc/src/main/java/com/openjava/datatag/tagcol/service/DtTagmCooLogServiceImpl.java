package com.openjava.datatag.tagcol.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagcol.domain.DtTagmCooLog;
import com.openjava.datatag.tagcol.query.DtTagmCooLogDBParam;
import com.openjava.datatag.tagcol.repository.DtTagmCooLogRepository;
/**
 * tagcol业务层
 * @author hyq
 *
 */
@Service
@Transactional
public class DtTagmCooLogServiceImpl implements DtTagmCooLogService {
	
	@Resource
	private DtTagmCooLogRepository dtTagmCooLogRepository;//

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<DtTagmCooLog> query(DtTagmCooLogDBParam params, Pageable pageable){
		Page<DtTagmCooLog> pageresult = dtTagmCooLogRepository.query(params, pageable);
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DtTagmCooLog> queryDataOnly(DtTagmCooLogDBParam params, Pageable pageable){
		return dtTagmCooLogRepository.queryDataOnly(params, pageable);
	}

	/**
	 *
	 * @param colId
	 * @return
	 */
	public List<DtTagmCooLog> findByColId(Long colId){
		return dtTagmCooLogRepository.findByColId(colId);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public DtTagmCooLog get(Long id) {
		Optional<DtTagmCooLog> o = dtTagmCooLogRepository.findById(id);
		if(o.isPresent()) {
			DtTagmCooLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTagmCooLog："+id);
		return null;
	}

	/**
	 *
	 * @param m
	 * @return
	 */
	public DtTagmCooLog doSave(DtTagmCooLog m) {
		return dtTagmCooLogRepository.save(m);
	}

	/**
	 *
	 * @param id
	 */
	public void doDelete(Long id) {
		dtTagmCooLogRepository.deleteById(id);
	}

	/**
	 *
	 * @param ids
	 */
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTagmCooLogRepository.deleteById(new Long(items[i]));
		}
	}
}
