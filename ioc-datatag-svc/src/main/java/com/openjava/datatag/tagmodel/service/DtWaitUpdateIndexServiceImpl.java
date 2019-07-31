package com.openjava.datatag.tagmodel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.domain.DtWaitUpdateIndex;
import com.openjava.datatag.tagmodel.query.DtWaitUpdateIndexDBParam;
import com.openjava.datatag.tagmodel.repository.DtWaitUpdateIndexRepository;
/**
 * 更新索引表业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtWaitUpdateIndexServiceImpl implements DtWaitUpdateIndexService {
	
	@Resource
	private DtWaitUpdateIndexRepository dtWaitUpdateIndexRepository;
	
	public Page<DtWaitUpdateIndex> query(DtWaitUpdateIndexDBParam params, Pageable pageable){
		Page<DtWaitUpdateIndex> pageresult = dtWaitUpdateIndexRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtWaitUpdateIndex> queryDataOnly(DtWaitUpdateIndexDBParam params, Pageable pageable){
		return dtWaitUpdateIndexRepository.queryDataOnly(params, pageable);
	}
	
	public DtWaitUpdateIndex get(Long id) {
		Optional<DtWaitUpdateIndex> o = dtWaitUpdateIndexRepository.findById(id);
		if(o.isPresent()) {
			DtWaitUpdateIndex m = o.get();
			return m;
		}
		System.out.println("找不到记录DtWaitUpdateIndex："+id);
		return null;
	}
	
	public DtWaitUpdateIndex doSave(DtWaitUpdateIndex m) {
		return dtWaitUpdateIndexRepository.save(m);
	}
	
	public void doDelete(Long id) {
		dtWaitUpdateIndexRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtWaitUpdateIndexRepository.deleteById(new Long(items[i]));
		}
	}
}
