package com.openjava.datatag.tagcol.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
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

import com.openjava.datatag.tagcol.domain.DtTagmCooLog;
import com.openjava.datatag.tagcol.service.DtTagmCooLogService;
import com.openjava.datatag.tagcol.query.DtTagmCooLogDBParam;


/**
 * api接口
 * @author hyq
 *
 */
@Api(tags="tagcol")
@RestController
@RequestMapping("/openjava/datatag/dtTagmCooLog")
public class DtTagmCooLogAction {
	
	@Resource
	private DtTagmCooLogService dtTagmCooLogService;
	
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
	public DtTagmCooLog get(@PathVariable("id")Long id) {
		DtTagmCooLog m = dtTagmCooLogService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_cooId", value = "协作表主键=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtTagmCooLog> doSearch(@ApiIgnore() DtTagmCooLogDBParam params, @ApiIgnore() Pageable pageable){
		Page<DtTagmCooLog> result =  dtTagmCooLogService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody DtTagmCooLog body

			) {
		if(body.getIsNew() == null || body.getIsNew()) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			body.setId(ss.getSequence());
			body.setIsNew(true);//执行insert
			DtTagmCooLog dbObj = dtTagmCooLogService.doSave(body);
		} else {
			//修改，记录更新时间等
			DtTagmCooLog db = dtTagmCooLogService.get(body.getId());
			MyBeanUtils.copyPropertiesNotBlank(db, body);
			db.setIsNew(false);//执行update
			dtTagmCooLogService.doSave(db);
		}
		
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
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
			dtTagmCooLogService.doDelete(id);
		} else if(ids != null) {
			dtTagmCooLogService.doRemove(ids);
		}
		return new SuccessMessage("删除成功");//没有需要返回的数据，就直接返回一条消息
	}
	
}
