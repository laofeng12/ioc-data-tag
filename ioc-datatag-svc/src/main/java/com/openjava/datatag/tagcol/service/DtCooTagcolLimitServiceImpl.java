package com.openjava.datatag.tagcol.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import com.openjava.datatag.tagcol.repository.DtCooperationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.query.DtCooTagcolLimitDBParam;
import com.openjava.datatag.tagcol.repository.DtCooTagcolLimitRepository;
/**
 * tagcol业务层
 * @author hyq
 *
 */
@Service
@Transactional
public class DtCooTagcolLimitServiceImpl implements DtCooTagcolLimitService {
	
	@Resource
	private DtCooTagcolLimitRepository dtCooTagcolLimitRepository;
	
	public Page<DtCooTagcolLimit> query(DtCooTagcolLimitDBParam params, Pageable pageable){
		Page<DtCooTagcolLimit> pageresult = dtCooTagcolLimitRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtCooTagcolLimit> queryDataOnly(DtCooTagcolLimitDBParam params, Pageable pageable){
		return dtCooTagcolLimitRepository.queryDataOnly(params, pageable);
	}
	public List<DtCooTagcolLimit> findByColId(Long colId){
		return dtCooTagcolLimitRepository.findByColId(colId);
	}
	public int deleteBycoolId(Long colId){
		return dtCooTagcolLimitRepository.deleteBycoolId(colId);
	};
	public DtCooTagcolLimit get(Long id) {
		Optional<DtCooTagcolLimit> o = dtCooTagcolLimitRepository.findById(id);
		if(o.isPresent()) {
			DtCooTagcolLimit m = o.get();
			return m;
		}
		System.out.println("找不到记录DtCooTagcolLimit："+id);
		return null;
	}
	
	public DtCooTagcolLimit doSave(DtCooTagcolLimit m) {
		return dtCooTagcolLimitRepository.save(m);
	}

	public void doDelete(Long id) {
		dtCooTagcolLimitRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtCooTagcolLimitRepository.deleteById(new Long(items[i]));
		}
	}
}
