package com.openjava.datatag.log.api;

import javax.annotation.Resource;

import com.openjava.datatag.log.domain.DtTaggUpdateLog;
import org.ljdp.component.exception.APIException;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import com.openjava.datatag.log.service.DtTaggUpdateLogService;
import com.openjava.datatag.log.query.DtTaggUpdateLogDBParam;


/**
 * api接口
 * @author lch
 *
 */
@Api(tags="标签组日志-不可用")
@RestController
@RequestMapping("/datatag/tagmanage/dtTaggUpdateLog")
public class DtTaggUpdateLogAction {
	
	@Resource
	private DtTaggUpdateLogService dtTaggUpdateLogService;
	
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
	public DtTaggUpdateLog get(@PathVariable("id")Long id) {
		DtTaggUpdateLog m = dtTaggUpdateLogService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询-日志查询暂时不开放", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_taggId", value = "标签组编号=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_modifyUser", value = "修改者=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_modifyType", value = "修改类型(修改或删除)=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_taggId", value = "标签组编号=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_modifyUser", value = "修改者=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_modifyType", value = "修改类型(修改或删除)=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "le_modifyTime", value = "修改时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_modifyTime", value = "修改时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtTaggUpdateLog> doSearch(@ApiIgnore() DtTaggUpdateLogDBParam params, @ApiIgnore() Pageable pageable) throws APIException {
		Page<DtTaggUpdateLog> result =  dtTaggUpdateLogService.query(params, pageable);
		throw new APIException(500,"暂时不提供");
		//return new TablePageImpl<>(result);
	}
	

	
	
}
