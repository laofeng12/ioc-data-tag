package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagcol.service.DtCooperationService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.dto.DtTagDTO;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.dto.DtTagTableDTO;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.utils.IpUtil;
import com.openjava.datatag.utils.VoUtils;
import com.openjava.datatag.utils.tree.TagDTOTreeNode;
import com.openjava.datatag.utils.tree.TagDTOTreeNodeShow;
import io.swagger.annotations.*;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * api接口
 *
 * @author lch
 */
@Api(tags = "标签增删改/标签树查")
@RestController
@RequestMapping("/datatag/tagmanage/dtTag")
public class DtTagAction {
    @Resource
    private DtCooperationService dtCooperationService;
    @Resource
    private DtTagService dtTagService;

    @Resource
    private DtTagGroupService dtTagGroupService;


    /**
     * 保存
     */
    @ApiOperation(value = "修改标签:标签名-简介/新增标签:标签名-简介-上级标签id-标签组id", nickname = "save", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.TAG_NOT_FOUND, message = "无此标签或已被删除"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.PUBLIC_NO_AUTHORITY, message = "无权限查看"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.PUBLIC_ERROE, message = "其他异常"),
    })
    @Security(session = true)
    @RequestMapping(method = RequestMethod.POST)
    public SuccessMessage doSave(@RequestBody DtTag body,
                                 HttpServletRequest request) throws APIException {
        //修改，记录更新时间等
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long userId = Long.valueOf(userInfo.getUserId());
        String ip = IpUtil.getRealIP(request);
        DtTagGroup tagGroup = dtTagGroupService.get(body.getTagsId());
        if (userInfo.getUserId().equals(tagGroup.getCreateUser().toString())) {
            if (body.getIsNew() == null || body.getIsNew()) {
                dtTagService.doNew(body, userId, ip);
                return new SuccessMessage("新建成功");
            } else {
                DtTag db = dtTagService.get(body.getId());
                if ((db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) && body.getIsNew()) {
                    throw new APIException(MyErrorConstants.TAG_NOT_FOUND, "无此标签或已被删除");
                }
                if (body.getIsDeleted() != null && body.getIsDeleted().equals(Constants.PUBLIC_YES)) {
                    throw new APIException(MyErrorConstants.PUBLIC_ERROE, "请不要用此方法进行删除操作，请用DELETE方法");
                }
                dtTagService.doUpdate(body, db, userId, ip);
                return new SuccessMessage("修改成功");
            }
        } else {
            throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY, "无权限修改");
        }
    }


    @ApiOperation(value = "删除", nickname = "delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
            @ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用 , 分隔", required = false, paramType = "delete"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.TAG_NOT_FOUND, message = "无此标签或已被删除"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.PUBLIC_NO_AUTHORITY, message = "无权限查看")
    })
    @Security(session = true)
    @RequestMapping(method = RequestMethod.DELETE)
    public SuccessMessage doDelete(
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request) throws APIException {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long userId = Long.valueOf(userInfo.getUserId());
        String ip = IpUtil.getRealIP(request);
        DtTag db = dtTagService.get(id);
        if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) {
            throw new APIException(MyErrorConstants.TAG_NOT_FOUND, "无此标签或已被删除");
        }
        DtTagGroup tagGroupdb = dtTagGroupService.get(db.getTagsId());
        if (userInfo.getUserId().equals(tagGroupdb.getCreateUser().toString())) {
            dtTagService.doSoftDeleteByDtTag(db, userId, ip);
            return new SuccessMessage("删除成功");
        } else {
            throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY, "无权限删除");
        }
    }

    /**
     * 用主键获取数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据标签组ID获取", notes = "单个对象查询", nickname = "id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签组编码", required = true, dataType = "string", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.TAG_GROUP_NOT_FOUND, message = "无此标签组或已被删除"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.PUBLIC_NO_AUTHORITY, message = "无权限查看")
    })
    @Security(session = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TagDTOTreeNodeShow getTree(@PathVariable("id") Long id) throws APIException {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        DtTagGroup db = dtTagGroupService.get(id);
        if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) {
            throw new APIException(MyErrorConstants.TAG_GROUP_NOT_FOUND, "无此标签组或已被删除");
        }
        //查找当前用户是否配置有该标签组的协作权限
        Long cooUserTagGroupCount = dtCooperationService.findCooUserTagGroup(VoUtils.toLong(userInfo.getUserId()), id);
        //自己的和共享的标签组可以查看
        if (userInfo.getUserId().equals(db.getCreateUser().toString()) || db.getIsShare().equals(Constants.PUBLIC_YES) || cooUserTagGroupCount > 0) {
            List<DtTag> tagList = dtTagService.findByTagsId(id);
            DtTagDTO root = new DtTagDTO();
            root.setId(TagDTOTreeNode.ROOT_ID);
            TagDTOTreeNode treeNode = new TagDTOTreeNode(TagDTOTreeNode.toDtTagDTO(tagList), root);
            TagDTOTreeNodeShow treeNodeShow = new TagDTOTreeNodeShow(treeNode);
            return treeNodeShow;
        } else {
            throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY, "无权限查看");
        }

    }

    /**
     * 用主键获取数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据标签编码获得其与其子标签", notes = "单个对象查询", nickname = "id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签编码", required = true, dataType = "string", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.TAG_GROUP_NOT_FOUND, message = "无此标签组或已被删除"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.TAG_NOT_FOUND, message = "无此标签或已被删除"),
            @io.swagger.annotations.ApiResponse(code = MyErrorConstants.PUBLIC_NO_AUTHORITY, message = "无权限查看")
    })
    @Security(session = true)
    @RequestMapping(value = "/table/{id}", method = RequestMethod.GET)
    public DtTagTableDTO get(@PathVariable("id") Long id) throws APIException {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        DtTag db = dtTagService.get(id);
        if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) {
            throw new APIException(MyErrorConstants.TAG_NOT_FOUND, "无此标签或已被删除");
        }
        DtTagGroup gdb = dtTagGroupService.get(db.getTagsId());
        if (gdb == null || gdb.getIsDeleted().equals(Constants.PUBLIC_YES)) {
            throw new APIException(MyErrorConstants.TAG_GROUP_NOT_FOUND, "无此标签组或已被删除");
        }
        //自己的和共享的标签组可以查看
        if (userInfo.getUserId().equals(gdb.getCreateUser().toString()) || gdb.getIsShare().equals(Constants.PUBLIC_YES)) {
            List<DtTag> tagList = dtTagService.findByPreaTagId(id);
            DtTag root = dtTagService.get(id);
            DtTagTableDTO dto = new DtTagTableDTO();
            dto.setParentTag(root);
            dto.setChildrenTag(tagList);
            return dto;
        } else {
            throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY, "无权限查看");
        }

    }

}
