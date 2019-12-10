package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.log.service.DtTaggChooseLogService;
import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.service.DtShareTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmanage.service.DtTagService;
import com.openjava.datatag.utils.IpUtil;
import io.swagger.annotations.*;
import org.apache.commons.collections.CollectionUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags="共享标签组列表")
@RestController
@RequestMapping("/datatag/tagmanage/shareDtTagGroup")
public class ShareDtTagGroupAction {
    @Resource
    private DtShareTagGroupService dtShareTagGroupService;//共享数据

    @Resource
    private DtTagGroupService dtTagGroupService;//DT_TAG_GROUP表签组业务层接口

    @Resource
    private DtTagService dtTagService;//DT_TAG标签业务层接口
    @Resource
    private DtTaggChooseLogService dtTaggChooseLogService;//DT_TAGG_CHOOSE_LOG业务层接口

    @ApiOperation(value = "标签组列表分页查询(共享)", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchKey", value = "查询关键字(单位/名称/简介)like", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
    })
    @Security(session=true,allowResources = {"tagManage"})
    @RequestMapping(method= RequestMethod.GET)
    public Page<DtShareTagGroup> doSearchShare(@ApiIgnore @RequestParam(value = "searchKey",required = false) String searchKey,
                                               @ApiIgnore() Pageable pageable)throws Exception{
        if (searchKey == null){
            searchKey = "";
        }
        Page<DtShareTagGroup> result =  dtShareTagGroupService.findList(searchKey,pageable);//标签组列表分页查询(共享)
        return result;
    }

    @ApiOperation(value = "选用标签组", nickname="choose")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "共享标签组编码", required = false, paramType = "query"),
            //@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=20020, message="会话失效"),
            @io.swagger.annotations.ApiResponse(code= MyErrorConstants.SHARE_TAG_GROUP_NOT_FOUND, message="无此标签组或未共享")
    })
    @Security(session=true,allowResources = {"tagManage"})
    @RequestMapping(method=RequestMethod.POST)
    public SuccessMessage doChooseShareTagGroup(
            @RequestParam(value="id",required=false)Long id,
            HttpServletRequest request) throws Exception {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取当前用户信息
        String ip = IpUtil.getRealIP(request);//获取请求ip
        DtTagGroup db = dtTagGroupService.get(id);//获取标签组
        if(db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES) || db.getIsShare().equals(Constants.PUBLIC_NO)) {
            throw new APIException(MyErrorConstants.SHARE_TAG_GROUP_NOT_FOUND, "无此标签组或未共享");
        }
        if (db.getCreateUser().equals(Long.valueOf(userInfo.getUserId()))){
            throw new APIException(MyErrorConstants.CAN_NOT_CHOOSE, "不能选用自己的标签组");
        }

        Long count = dtTaggChooseLogService.countChoose(Long.valueOf(userInfo.getUserId()),id);//查询当前用户标签组选用次数
        if (count!=null && count>0){
            throw new APIException(MyErrorConstants.CAN_NOT_CHOOSE, "该标签组已选用，不能重复选用");
        }
        dtShareTagGroupService.choose(id,Long.parseLong(userInfo.getUserId()),ip);
        return new SuccessMessage("选用成功");
    }


}
