package com.openjava.datatag.log.api;

import javax.annotation.Resource;

import com.openjava.datatag.log.domain.DtTaggChooseLog;
import org.ljdp.component.exception.APIException;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import com.openjava.datatag.log.service.DtTaggChooseLogService;
import com.openjava.datatag.log.query.DtTaggChooseLogDBParam;


/**
 * api接口
 * @author lch
 *
 */
@Api(tags="共享标签组选用日志-不可用")
@RestController
@RequestMapping("/datatag/tagmanage/dtTaggChooseLog")
public class DtTaggChooseLogAction {
	
	@Resource
	private DtTaggChooseLogService dtTaggChooseLogService;

	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_copiedTagg", value = "被拷贝的标签组编号=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_copyTagg", value = "拷贝的标签组编号=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_chooseUser", value = "选用者=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "le_chooseTime", value = "选用时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_chooseTime", value = "选用时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtTaggChooseLog> doSearch(@ApiIgnore() DtTaggChooseLogDBParam params, @ApiIgnore() Pageable pageable) throws APIException {
		Page<DtTaggChooseLog> result =  dtTaggChooseLogService.query(params, pageable);
		throw new APIException(500,"暂时不提供");
		//return new TablePageImpl<>(result);
	}


}
