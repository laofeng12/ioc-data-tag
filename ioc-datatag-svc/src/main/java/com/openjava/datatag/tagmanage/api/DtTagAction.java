package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.dto.DtTagDTO;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.utils.tree.TagDTOTreeNode;
import io.swagger.annotations.*;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;


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

	@Resource
	private DtTagGroupService dtTagGroupService;


	/**
	 * 保存
	 */
	@ApiOperation(value = "修改标签:标签名-简介/新增标签:标签名-简介-上级标签id-标签组id", nickname="save", notes = "报文格式：content-type=application/json")
	@ApiResponses({
			@io.swagger.annotations.ApiResponse(code=20020, message="会话失效"),
			@io.swagger.annotations.ApiResponse(code=(int)MyErrorConstants.TAG_NOT_FOUND, message="无此标签或已被删除"),
			@io.swagger.annotations.ApiResponse(code=(int)MyErrorConstants.PUBLIC_NO_AUTHORITY, message="无权限查看")
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody DtTag body) throws APIException {
		//修改，记录更新时间等
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtTagGroup tagGroup = dtTagGroupService.get(body.getTagsId());
		if(userInfo.getUserId().equals(tagGroup.getCreateUser().toString())){
			if(body.getIsNew() == null || body.getIsNew()) {
				dtTagService.doNew(body);
				return new SuccessMessage("新建成功");
			} else {
				DtTag db = dtTagService.get(body.getId());
				if((db == null || db.getIsDeleted().equals(Constants.DT_TG_DELETED)) && body.getIsNew()){
					throw new APIException(MyErrorConstants.TAG_NOT_FOUND,"无此标签或已被删除");
				}
				if (body.getIsDeleted()!= null && body.getIsDeleted().equals(Constants.DT_TG_DELETED)){
					throw new APIException(MyErrorConstants.PUBLIC_ERROE,"请不要用此方法进行删除操作，请用DELETE方法");
				}
				dtTagService.doUpdate(body,db);
				return new SuccessMessage("修改成功");
			}
		}else{
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"无权限修改");
		}
	}


	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
			@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用 , 分隔", required = false, paramType = "delete"),
	})
	@ApiResponses({
			@io.swagger.annotations.ApiResponse(code=20020, message="会话失效"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.TAG_NOT_FOUND, message="无此标签或已被删除"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.PUBLIC_NO_AUTHORITY, message="无权限查看")
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id) throws APIException {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtTag tag = dtTagService.get(id);
		if(tag == null || tag.getIsDeleted().equals(Constants.DT_TG_DELETED)){
			throw new APIException(MyErrorConstants.TAG_NOT_FOUND,"无此标签或已被删除");
		}
		DtTagGroup tagGroup = dtTagGroupService.get(tag.getTagsId());
		if(userInfo.getUserId().equals(tagGroup.getCreateUser().toString())){
			dtTagService.doSoftDeleteByDtTag(tag);
			return new SuccessMessage("删除成功");
		}else{
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"无权限删除");
		}
	}

	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据标签组ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "标签组编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
			@io.swagger.annotations.ApiResponse(code=20020, message="会话失效"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.TAG_GROUP_NOT_FOUND, message="无此标签组或已被删除"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.PUBLIC_NO_AUTHORITY, message="无权限查看")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public TagDTOTreeNode get(@PathVariable("id")Long id) throws APIException {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		DtTagGroup db = dtTagGroupService.get(id);
		if(db == null || db.getIsDeleted().equals(Constants.DT_TG_DELETED)){
			throw new APIException(MyErrorConstants.TAG_GROUP_NOT_FOUND,"无此标签组或已被删除");
		}
		//自己的和共享的标签组可以查看
		if(userInfo.getUserId().equals(db.getCreateUser().toString()) || db.getIsShare().equals(Constants.DT_TG_SHARED)){
			List<DtTag> tagList = dtTagService.findByTagsId(id);
			DtTagDTO root = new DtTagDTO();
			root.setId(TagDTOTreeNode.ROOT_ID);
			TagDTOTreeNode treeNode = new TagDTOTreeNode(TagDTOTreeNode.toDtTagDTO(tagList),root);
			return treeNode;
		}else{
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"无权限查看");
		}

	}

	
}
