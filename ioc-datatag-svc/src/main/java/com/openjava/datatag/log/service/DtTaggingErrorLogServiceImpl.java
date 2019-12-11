package com.openjava.datatag.log.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.log.domain.DtTaggingErrorLog;
import com.openjava.datatag.log.query.DtTaggingErrorLogDBParam;
import com.openjava.datatag.log.repository.DtTaggingErrorLogRepository;
/**
 * 模型调度错误日志业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtTaggingErrorLogServiceImpl implements DtTaggingErrorLogService {
	
	@Resource
	private DtTaggingErrorLogRepository dtTaggingErrorLogRepository;//

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<DtTaggingErrorLog> query(DtTaggingErrorLogDBParam params, Pageable pageable){
		Page<DtTaggingErrorLog> pageresult = dtTaggingErrorLogRepository.query(params, pageable);
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DtTaggingErrorLog> queryDataOnly(DtTaggingErrorLogDBParam params, Pageable pageable){
		return dtTaggingErrorLogRepository.queryDataOnly(params, pageable);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public DtTaggingErrorLog get(Long id) {
		Optional<DtTaggingErrorLog> o = dtTaggingErrorLogRepository.findById(id);
		if(o.isPresent()) {
			DtTaggingErrorLog m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTaggingErrorLog："+id);
		return null;
	}

	/**
	 *
	 * @param m
	 * @return
	 */
	public DtTaggingErrorLog doSave(DtTaggingErrorLog m) {
		return dtTaggingErrorLogRepository.save(m);
	}

	/**
	 *
	 * @param id
	 */
	public void doDelete(Long id) {
		dtTaggingErrorLogRepository.deleteById(id);
	}

	/**
	 *
	 * @param ids
	 */
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTaggingErrorLogRepository.deleteById(new Long(items[i]));
		}
	}

	/**
	 *
	 * @param taggingModelId
	 * @return
	 */
	public DtTaggingErrorLog getByTaggingModelIdOrderByErrorTimeDesc(Long taggingModelId){
		return dtTaggingErrorLogRepository.getByTaggingModelIdOrderByErrorTimeDesc(taggingModelId);
	}
}
