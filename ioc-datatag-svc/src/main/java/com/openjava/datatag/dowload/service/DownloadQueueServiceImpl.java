package com.openjava.datatag.dowload.service;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.component.FtpUtil;
import org.apache.commons.lang3.StringUtils;
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
	private DownloadQueueRepository downloadQueueRepository;//
	@Resource
	private SysCodeService sysCodeService;//
	@Resource
	private AuditComponet auditComponet;//
	@Resource
	private FtpUtil ftpUtil;//

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 * @throws Exception
	 */
	public Page<DownloadQueue> query(DownloadQueueDBParam params, Pageable pageable)throws Exception{
//		params.setTableAlias("t");
		if (StringUtils.isNotBlank(params.getLike_bname())){
			params.setLike_bname("%"+params.getLike_bname()+"%");
		}
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
		AuditLogVO vo = new AuditLogVO();
		vo.setType(2L);//数据查询
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("模型部署");//必传
		vo.setFunctionLev1("下载管理");//必传
		vo.setFunctionLev2("查询");//必传
		auditComponet.saveAuditLog(vo);
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DownloadQueue> queryDataOnly(DownloadQueueDBParam params, Pageable pageable){
		return downloadQueueRepository.queryDataOnly(params, pageable);
	}

	/**
	 *
	 * @param id
	 * @return
	 */
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

	/**
	 *
	 * @param m
	 * @return
	 */
	public DownloadQueue doSave(DownloadQueue m) {
		return downloadQueueRepository.save(m);
	}

	/**
	 *
	 * @param id
	 */
	public void doDelete(Long id) {
		downloadQueueRepository.deleteById(id);
	}

	/**
	 *
	 * @param ids
	 */
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

	/**
	 *
	 * @param state
	 * @return
	 */
	public List<DownloadQueue> findByState(Long state){
		return  downloadQueueRepository.findByState(state);
	}

	/**
	 *
	 * @param taggingModelId
	 * @param response
	 */
	public void doExport(Long taggingModelId, HttpServletResponse response){
		try {


			// 清空response
			response.reset();
			String path = ftpUtil.getLocalPath()+"\\"+ Constants.DT_BTYPE_DATATAG+"\\"+taggingModelId.toString();
			String fileName = taggingModelId.toString()+".zip";
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			boolean result =  ftpUtil.downloadFile(path,fileName,toClient);
			if (!result){
				throw new ApiException("下载失败！");
			}
			toClient.flush();
			toClient.close();
			AuditLogVO vo = new AuditLogVO();
			vo.setType(2L);//数据查询
			vo.setOperationService("标签与画像");//必传
			vo.setOperationModule("模型部署");//必传
			vo.setFunctionLev1("下载管理");//必传
			vo.setFunctionLev2("导出到本地");//必传
			vo.setRecordId(taggingModelId+"");
			auditComponet.saveAuditLog(vo);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().write(e.getMessage());
			} catch (Exception e2) {
			}
		}
	}
}
