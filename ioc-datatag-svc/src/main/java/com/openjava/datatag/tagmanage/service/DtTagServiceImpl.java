package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * DT_TAG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTagServiceImpl implements DtTagService {
	
	@Resource
	private DtTagRepository dtTagRepository;
	
	public Page<DtTag> query(DtTagDBParam params, Pageable pageable){
		Page<DtTag> pageresult = dtTagRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtTag> queryDataOnly(DtTagDBParam params, Pageable pageable){
		return dtTagRepository.queryDataOnly(params, pageable);
	}
	
	public DtTag get(Long id) {
		Optional<DtTag> o = dtTagRepository.findById(id);
		if(o.isPresent()) {
			DtTag m = o.get();
			return m;
		}
		System.out.println("找不到记录DtTag："+id);
		return null;
	}
	
	public DtTag doSave(DtTag m) {
		return dtTagRepository.save(m);
	}

	public void doSoftDeleteByTagsID(Long id, Date now){
		dtTagRepository.doSoftDeleteByTagsID(id,now);
	}

	public List<DtTag> findByTagsId(Long tagsId){
		//查询所有未删除的标签list
		return dtTagRepository.findByTagsIdAndIsDeleted(tagsId,0L);
	}


	public void doDelete(Long id) {
		dtTagRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtTagRepository.deleteById(new Long(items[i]));
		}
	}
}
