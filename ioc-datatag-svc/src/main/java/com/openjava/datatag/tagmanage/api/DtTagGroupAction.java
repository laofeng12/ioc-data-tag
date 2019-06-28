package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import io.swagger.annotations.*;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
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
@Api(tags="DT_TAG_GROUP")
@RestController
@RequestMapping("/datatag/tagmanage/dtTagGroup")
public class DtTagGroupAction {
	
	@Resource
	private DtTagGroupService dtTagGroupService;

	@Resource
	private DtTagService dtTagService;
	
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
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效"),
		@io.swagger.annotations.ApiResponse(code=10001, message="无权限查看")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public DataApiResponse<DtTagGroup> get(@PathVariable("id")Long id) {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtTagGroup m = dtTagGroupService.get(id);
		DataApiResponse<DtTagGroup> apiResp = new DataApiResponse<>();
		if(m == null){
			apiResp.setData(m);
			return apiResp;
		}
		if(userInfo.getUserId().equals(m.getCreateUser().toString())){
			apiResp.setData(m);
			return apiResp;
		}else{
			apiResp.setCode(10001);
			apiResp.setMessage("无权限查看");
			return apiResp;
		}
	}
	
	@ApiOperation(value = "标签组列表分页查询(我的)", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_tagsName", value = "标签组名like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_isShare", value = "是否共享=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "like_synopsis", value = "标签组简介like", required = false, dataType = "String", paramType = "query"),
		//@ApiImplicitParam(name = "eq_user", value = "创建者=", required = false, dataType = "Long", paramType = "query"),
		//@ApiImplicitParam(name = "eq_modifyUser", value = "修改者名=", required = false, dataType = "String", paramType = "query"),
		//@ApiImplicitParam(name = "eq_isDeleted", value = "删除标记=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/MyTagGroupList",method= RequestMethod.GET)
	public TablePage<DtTagGroup> getMyTagGroupList(@ApiIgnore() DtTagGroupDBParam params, @ApiIgnore() Pageable pageable){
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		Long id = Long.parseLong(userInfo.getUserId());
		params.setEq_createUser(id);
		params.setEq_isDeleted(0L);
		Page<DtTagGroup> result =  dtTagGroupService.query(params, pageable);
		return new TablePageImpl<>(result);
	}

	@ApiOperation(value = "标签组列表分页查询(共享)-未完成-需要连接用户表", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "like_tagsName", value = "标签组名like", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "eq_isShare", value = "是否共享=", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "like_synopsis", value = "标签组简介like", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "eq_createUser", value = "创建者=", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "eq_modifyUser", value = "修改者名=", required = false, dataType = "String", paramType = "query"),
			//@ApiImplicitParam(name = "eq_isDeleted", value = "删除标记=", required = false, dataType = "Long", paramType = "query"),
			@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/searchShare",method= RequestMethod.GET)
	public TablePage<DtTagGroup> doSearchShare(@ApiIgnore() DtTagGroupDBParam params, @ApiIgnore() Pageable pageable){
		params.setEq_isDeleted(0L);
		params.setEq_isShare(1L);
		Page<DtTagGroup> result =  dtTagGroupService.query(params, pageable);

		return new TablePageImpl<>(result);
	}

	/**
	 * 软删除标签模型和下属的tag
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID删除", notes = "软删除标签组和下属的标签", nickname="delete")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "post"),
	})
	@ApiResponses({
			@io.swagger.annotations.ApiResponse(code=20020, message="会话失效"),
			@io.swagger.annotations.ApiResponse(code=10002, message="无此标签或已被删除"),
			@io.swagger.annotations.ApiResponse(code=10003, message="无权限删除")
	})
	@Security(session=true)
	@RequestMapping(value="/deleteTagGroup",method= RequestMethod.POST)
	public ApiResponse doSoftDelete(@RequestParam("id")Long id) {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtTagGroup tagGroup = dtTagGroupService.get(id);
		ApiResponse apiResp = new BasicApiResponse();
		if(tagGroup == null || tagGroup.getIsDeleted().equals(1L)){
			apiResp.setCode(10002);
			apiResp.setMessage("无此标签组或已被删除");
			return apiResp;
		}
		if(userInfo.getUserId().equals(tagGroup.getCreateUser().toString())){
			dtTagGroupService.doSoftDelete(tagGroup);//级联软删除，子标签也会被删除
			apiResp.setCode(200);
			return apiResp;
		}else{
			apiResp.setCode(10003);
			apiResp.setMessage("无权限删除");
			return apiResp;
		}
	}

	/**
	 * 设置共享或取消共享
	 */
//	@ApiOperation(value = "设置共享或取消共享", nickname="save", notes = "报文格式：content-type=application/json")
//	@Security(session=true)
//	@RequestMapping(method= RequestMethod.POST)
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "String", paramType = "post"),
//			@ApiImplicitParam(name = "shared", value = "是否共享标识", required = true, dataType = "Long", paramType = "post"),
//	})
//	public ApiResponse setShare(@RequestParam("id") Long id,@RequestParam("shared") Long shared) {
//		//DtTagGroup tagGroup =
//		;
//	}
	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存操作", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(method= RequestMethod.POST)
	public ApiResponse doSave(@RequestBody DtTagGroup body) {

		if(body.getIsNew() == null || body.getIsNew()) {
			//取登录系统session的ID而不是前端传过来的
			Long SessionUserID = 770403422520146L;
			body.setCreateUser(SessionUserID);

			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			body.setId(ss.getSequence());
			body.setIsNew(true);//执行insert
			DtTagGroup dbObj = dtTagGroupService.doSave(body);
		} else {

			//修改，记录更新时间等--验证是否已经删除
			DtTagGroup db = dtTagGroupService.get(body.getId());
			if (db.getIsDeleted().equals(0L)){
				//取登录系统的ID而不是前端传过来的
				//不可以更新创建者
				body.setCreateUser(db.getCreateUser());
				MyBeanUtils.copyPropertiesNotBlank(db, body);
				db.setIsNew(false);//执行update
				dtTagGroupService.doSave(db);
			}else {
				return  new BasicApiResponse(500,"该标签已被删除");

			}
		}
		BasicApiResponse resp = new BasicApiResponse();
		return resp;
	}
	
	
}
