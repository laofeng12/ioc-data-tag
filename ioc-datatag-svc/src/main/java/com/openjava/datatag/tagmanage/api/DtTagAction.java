package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.tagmanage.service.DtTagService;
import io.swagger.annotations.*;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


import javax.annotation.Resource;


/**
 * api接口
 * @author lch
 *
 */
@Api(tags="DT_TAG")
@RestController
@RequestMapping("/datatag/tagmanage/dtTag")
public class DtTagAction {
	
	@Resource
	private DtTagService dtTagService;
	

	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "标签编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
			@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public DtTag get(@PathVariable("id")Long id) {
		BaseUserInfo user = (BaseUserInfo) SsoContext.getUser();
		System.out.println(user.getUserId());
		DtTag m = dtTagService.get(id);
		return m;
	}


	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "eq_tagsId", value = "标签组编号=", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "eq_preaTagId", value = "父标签编号=", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "like_tagName", value = "标签名like", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "like_synopsis", value = "标签说明like", required = false, dataType = "String", paramType = "query"),
			//@ApiImplicitParam(name = "eq_isDeleted", value = "删除标记=", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "eq_lvl", value = "层级=", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtTag> doSearch(@ApiIgnore() DtTagDBParam params, @ApiIgnore() Pageable pageable){
		params.setEq_isDeleted(0L);
		Page<DtTag> result =  dtTagService.query(params, pageable);

		return new TablePageImpl<>(result);
	}


	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody DtTag body) {
		if(body.getIsNew() == null || body.getIsNew()) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			body.setId(ss.getSequence());
			body.setIsNew(true);//执行insert
			DtTag dbObj = dtTagService.doSave(body);
		} else {
			//修改，记录更新时间等
			DtTag db = dtTagService.get(body.getId());
			MyBeanUtils.copyPropertiesNotBlank(db, body);
			db.setIsNew(false);//执行update
			dtTagService.doSave(db);
		}

		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
	}


	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
			@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用 , 分隔", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id,
			@RequestParam(value="ids",required=false)String ids) {
		if(id != null) {
			dtTagService.doDelete(id);
		} else if(ids != null) {
			dtTagService.doRemove(ids);
		}
		return new SuccessMessage("删除成功");//没有需要返回的数据，就直接返回一条消息
	}
	

	
}
