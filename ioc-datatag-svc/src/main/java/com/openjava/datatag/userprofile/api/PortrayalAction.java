package com.openjava.datatag.userprofile.api;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.userprofile.dto.PortrayalDetailDTO;
import com.openjava.datatag.user.service.SysUserService;
import com.openjava.datatag.userprofile.service.PortrayalService;
import com.openjava.datatag.utils.VoUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.secure.annotation.Security;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * api接口
 *
 * @author hyq
 */
@Api(tags = "画像相关接口管理")
@RestController
@RequestMapping("/datatag/portrayal")
public class PortrayalAction {

    @Resource
    private SysUserService sysUserService;//系统用户业务层接口
    @Resource
    private PortrayalService portrayalService;//画像查询模块业务层接口
    @Resource
    private DtTaggingModelService dtTaggingModelService;//标签模型业务层接口

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据画像id查询画像列表", notes = "结果对象数组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "画像ID", required = false, dataType = "String", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效")
    })
    @Security(session = true,allowResources = {"portraitQuery"})
    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public DataApiResponse<PortrayalDetailDTO> doSearchCool(@PathVariable(value = "id") String id) throws Exception{
        DataApiResponse<PortrayalDetailDTO> resp = new DataApiResponse<>();//创建画像列表返回结构
        resp.setRows(portrayalService.searchPortrayal(id,1));//设置画像列表结果
        return resp;
    }

    /**
     *
     * @param tableName
     * @param pKey
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询画像详情", notes = "查询画像详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName", value = "表名", dataType ="String", paramType = "path"),
            @ApiImplicitParam(name = "pKey", value = "表主键", dataType ="String", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效")
    })
    @Security(session = true,allowResources = {"lableImage","portraitQuery"})
    @RequestMapping(value = "/getCoolDetail/{tableName}/{pKey}", method = RequestMethod.GET)
    public DataApiResponse<PortrayalDetailDTO> getCoolDetail(
            @PathVariable(value = "tableName") String tableName,
            @PathVariable(value = "pKey") String pKey
    ) throws Exception{
        DataApiResponse<PortrayalDetailDTO> resp = new DataApiResponse<>();//新建画像详情结果集
        resp.setData(portrayalService.portrayal(tableName,1,pKey,true));//设画像详情结果集
        return resp;
    }

}
