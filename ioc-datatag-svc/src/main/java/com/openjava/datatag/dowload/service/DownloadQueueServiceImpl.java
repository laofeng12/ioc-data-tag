package com.openjava.datatag.dowload.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.dowload.domain.DownloadQueue;
import com.openjava.datatag.dowload.query.DownloadQueueDBParam;
import com.openjava.datatag.dowload.repository.DownloadQueueRepository;
import com.openjava.framework.sys.domain.SysCode;
import com.openjava.framework.sys.service.SysCodeService;
/**
 * 下载列表业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DownloadQueueServiceImpl implements DownloadQueueService {
	
	@Resource
	private DownloadQueueRepository downloadQueueRepository;
	@Resource
	private SysCodeService sysCodeService;
	
	public Page<DownloadQueue> query(DownloadQueueDBParam params, Pageable pageable){
//		params.setTableAlias("t");
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Order.desc("createTime")));
		Page<DownloadQueue> pageresult = downloadQueueRepository.query(params, pageable);
		Map<String, SysCode> dtdowloadtype = sysCodeService.getCodeMap("dt.dowload.type");
		for (DownloadQueue m : pageresult.getContent()) {
			if(m.getState() != null) {
				SysCode c = dtdowloadtype.get(m.getState().toString());
				if(c != null) {
					m.setStateName(c.getCodename());
				}
			}
		}
		return pageresult;
	}
	
	public List<DownloadQueue> queryDataOnly(DownloadQueueDBParam params, Pageable pageable){
		return downloadQueueRepository.queryDataOnly(params, pageable);
	}
	
	public DownloadQueue get(Long id) {
		Optional<DownloadQueue> o = downloadQueueRepository.findById(id);
		if(o.isPresent()) {
			DownloadQueue m = o.get();
			if(m.getState() != null) {
				Map<String, SysCode> dtdowloadtype = sysCodeService.getCodeMap("dt.dowload.type");
				SysCode c = dtdowloadtype.get(m.getState().toString());
				if(c != null) {				
					m.setStateName(c.getCodename());
				}
			}
			return m;
		}
		System.out.println("找不到记录DownloadQueue："+id);
		return null;
	}
	
	public DownloadQueue doSave(DownloadQueue m) {
		return downloadQueueRepository.save(m);
	}
	
	public void doDelete(Long id) {
		downloadQueueRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			downloadQueueRepository.deleteById(new Long(items[i]));
		}
	}

	/**
	 * 单独事务的保存。会立即更新到数据库
	 * @param m
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
	public DownloadQueue doSaveNow(DownloadQueue m){
		return downloadQueueRepository.save(m);
	}
	public  DownloadQueue findBybtypeAndBid(String btype,String bid){
		return  downloadQueueRepository.findBybtypeAndBid(btype,bid);
	}
	public List<DownloadQueue> findByState(Long state){
		return  downloadQueueRepository.findByState(state);
	}
}
