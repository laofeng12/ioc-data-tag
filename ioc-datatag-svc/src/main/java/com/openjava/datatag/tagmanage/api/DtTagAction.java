package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagcol.service.DtCooperationService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.dto.DtTagDTO;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.dto.DtTagTableDTO;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.utils.IpUtil;
import com.openjava.datatag.utils.VoUtils;
import com.openjava.datatag.utils.tree.TagDTOTreeNode;
import com.openjava.datatag.utils.tree.TagDTOTreeNodeShow;
import com.openjava.datatag.utils.tree.TagDTOTreeNodeShow2;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
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
    private DtCooperationService dtCooperationService;//协作打标业务层接口
    @Resource
    private DtTagService dtTagService;//标签业务层接口

    @Resource
    private DtTagGroupService dtTagGroupService;//表签组业务层接口


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
    @Security(session = true,allowResources = {"tagManage"})
    @RequestMapping(method = RequestMethod.POST)
    public SuccessMessage doSaveOrEdit(@RequestBody DtTag body,
                                 HttpServletRequest request) throws Exception {
        String ip = IpUtil.getRealIP(request);//获取ip
        return dtTagService.doSaveOrEdit(body,ip);//修改标签
    }

    /**
     *
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
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
    @Security(session = true,allowResources = {"tagManage"})
    @RequestMapping(method = RequestMethod.DELETE)
    public SuccessMessage doDelete(
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request) throws Exception {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取用户信息
        Long userId = Long.valueOf(userInfo.getUserId());//获取用户id
        String ip = IpUtil.getRealIP(request);//获取ip
        DtTag db = dtTagService.get(id);//获取标签
        if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) {
            throw new APIException(MyErrorConstants.TAG_NOT_FOUND, "无此标签或已被删除");
        }
        DtTagGroup tagGroupdb = dtTagGroupService.get(db.getTagsId());//获取标签组
        if (userInfo.getUserId().equals(tagGroupdb.getCreateUser().toString())) {
            dtTagService.doSoftDeleteByDtTag(db, userId, ip);//软删除
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
    @Security(session = true,allowResources = {"tagManage"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TagDTOTreeNodeShow getTree(@PathVariable("id") Long id) throws Exception {
        return dtTagService.getTree(id);//根据标签组ID获取
    }

    /**
     * 获取所有标签组和树
     *
     * @return
     */
    @ApiOperation(value = "获取所有标签组和树", notes = "单个对象查询", nickname = "tagsId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效"),
    })
    @Security(session = true,allowResources = {"lableImage","tagManage"})
    @RequestMapping(value = "/getAllTree", method = RequestMethod.GET)
    public List<TagDTOTreeNodeShow2> getTreeByTagsId(@ApiIgnore() Pageable pageable) throws Exception {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取用户信息
        Long id = Long.parseLong(userInfo.getUserId());//获取当前登录用户id
        DtTagGroupDBParam params = new DtTagGroupDBParam();//创建查询的params
        params.setEq_createUser(id);//设置创建者为当前登录id
        params.setEq_isDeleted(Constants.PUBLIC_NO);//非删除状态
        Pageable mypage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("modifyTime")).and(Sort.by(Sort.Order.desc("createTime"))));//根据修改时间和创建时间倒叙排序
        Page<DtTagGroup> results =  dtTagGroupService.searchMyTagGroup(params, mypage);//获取标签组数据
        List<TagDTOTreeNodeShow2> resultsList = new ArrayList<>();
        if (results!=null && CollectionUtils.isNotEmpty(results.getContent())){
            for (DtTagGroup group:results.getContent()) {
                Long tagsId = group.getId();//标签组id
                DtTagGroup db = dtTagGroupService.get(tagsId);//获取标签组
                //查找当前用户是否配置有该标签组的协作权限
                Long cooUserTagGroupCount = dtCooperationService.findCooUserTagGroup(VoUtils.toLong(userInfo.getUserId()), tagsId);
                //自己的和共享的标签组可以查看
                if (userInfo.getUserId().equals(db.getCreateUser().toString()) || db.getIsShare().equals(Constants.PUBLIC_YES) || cooUserTagGroupCount > 0) {
                    List<DtTag> tagList = dtTagService.findByTagsId(tagsId);
                    //适配前端要求，第一节不能选，初始化一个空的list给前端
                    if (CollectionUtils.isEmpty(tagList)){
                        TagDTOTreeNodeShow2 father = new TagDTOTreeNodeShow2();
                        father.setLabel(group.getTagsName());//标签名
                        father.setValue(group.getId().toString());//标签id
                        father.setChildren(new ArrayList<>());//初始化空list
                        resultsList.add(father);//
                        continue;
                    }
                    DtTagDTO root = new DtTagDTO();//
                    root.setId(TagDTOTreeNode.ROOT_ID);//
                    TagDTOTreeNode treeNode = new TagDTOTreeNode(TagDTOTreeNode.toDtTagDTO(tagList), root);
                    TagDTOTreeNodeShow2 treeNodeShow = new TagDTOTreeNodeShow2(treeNode);
                    treeNodeShow.setValue(group.getId().toString());//设置组id
                    treeNodeShow.setLabel(group.getTagsName());//设置标签组名称
                    resultsList.add(treeNodeShow);
                }
            }
        }
        return resultsList;
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
    @Security(session = true,allowResources = {"tagManage"})
    @RequestMapping(value = "/table/{id}", method = RequestMethod.GET)
    public DtTagTableDTO get(@PathVariable("id") Long id) throws APIException {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取用户信息
        DtTag db = dtTagService.get(id);//获取标签id
        if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) {
            throw new APIException(MyErrorConstants.TAG_NOT_FOUND, "无此标签或已被删除");//
        }
        DtTagGroup gdb = dtTagGroupService.get(db.getTagsId());//获取标签组id
        if (gdb == null || gdb.getIsDeleted().equals(Constants.PUBLIC_YES)) {
            throw new APIException(MyErrorConstants.TAG_GROUP_NOT_FOUND, "无此标签组或已被删除");//
        }
        //自己的和共享的标签组可以查看
        if (userInfo.getUserId().equals(gdb.getCreateUser().toString()) || gdb.getIsShare().equals(Constants.PUBLIC_YES)) {
            List<DtTag> tagList = dtTagService.findByPreaTagId(id);//根据父标签获取标签
            DtTag root = dtTagService.get(id);//获取父标签
            DtTagTableDTO dto = new DtTagTableDTO();//
            dto.setParentTag(root);//父标签
            dto.setChildrenTag(tagList);//孩子标签
            return dto;
        } else {
            throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY, "无权限查看");
        }

    }

}
