package com.openjava.datatag.dowload.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import com.openjava.datatag.dowload.domain.DownloadQueue;
import com.openjava.datatag.dowload.service.DownloadQueueService;
import com.openjava.datatag.dowload.query.DownloadQueueDBParam;


/**
 * api接口
 * @author zmk
 *
 */
@Api(tags="下载列表")
@RestController
@RequestMapping("/datatag/dowload/downloadQueue")
public class DownloadQueueAction {
	
	@Resource
	private DownloadQueueService downloadQueueService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public DownloadQueue get(@PathVariable("id")Long id) {
		DownloadQueue m = downloadQueueService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_btype", value = "业务类型(标签与画像、数据集、数据碰撞等)=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_bid", value = "业务ID=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_state", value = "状态(下载中、下载失败)=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "like_bname", value = "业务名称like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DownloadQueue> doSearch(@ApiIgnore() DownloadQueueDBParam params, @ApiIgnore() Pageable pageable){
		Page<DownloadQueue> result =  downloadQueueService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody DownloadQueue body

			) {
		if(body.getIsNew() == null || body.getIsNew()) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			body.setId(ss.getSequence());
			body.setIsNew(true);//执行insert
			DownloadQueue dbObj = downloadQueueService.doSave(body);
		} else {
			//修改，记录更新时间等
			DownloadQueue db = downloadQueueService.get(body.getId());
			set(body.getBtype() != null, ()->db.setBtype(body.getBtype()));
			set(body.getBid() != null, ()->db.setBid(body.getBid()));
			set(body.getDownloadNum() != null, ()->db.setDownloadNum(body.getDownloadNum()));
			set(body.getFileSize() != null, ()->db.setFileSize(body.getFileSize()));
			set(body.getSpeedOfProgress() != null, ()->db.setSpeedOfProgress(body.getSpeedOfProgress()));
			set(body.getState() != null, ()->db.setState(body.getState()));
			set(body.getDownloadUrl() != null, ()->db.setDownloadUrl(body.getDownloadUrl()));
			set(body.getCreateTime() != null, ()->db.setCreateTime(body.getCreateTime()));
			set(body.getCreateUser() != null, ()->db.setCreateUser(body.getCreateUser()));
			set(body.getBname() != null, ()->db.setBname(body.getBname()));
			db.setIsNew(false);//执行update
			downloadQueueService.doSave(db);
		}
		
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
	}
	private void set(boolean condition, Runnable runnable) {
		if(condition) runnable.run();
	}
	
	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
		@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id,
			@RequestParam(value="ids",required=false)String ids) {
		if(id != null) {
			downloadQueueService.doDelete(id);
		} else if(ids != null) {
			downloadQueueService.doRemove(ids);
		}
		return new SuccessMessage("删除成功");//没有需要返回的数据，就直接返回一条消息
	}
	
}
