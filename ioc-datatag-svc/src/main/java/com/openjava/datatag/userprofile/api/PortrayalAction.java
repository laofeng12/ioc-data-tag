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
    private SysUserService sysUserService;
    @Resource
    private PortrayalService portrayalService;
    @Resource
    private DtTaggingModelService dtTaggingModelService;

    @ApiOperation(value = "查询画像记录集", notes = "结果对象数组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "画像ID", required = false, dataType = "String", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效")
    })
    @Security(session = true,allowResources = {"portraitQuery"})
    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public DataApiResponse<PortrayalDetailDTO> doSearchCool(@PathVariable(value = "id") String id) throws Exception{
        DataApiResponse<PortrayalDetailDTO> resp = new DataApiResponse<>();
        resp.setRows(portrayalService.searchPortrayal(id,1));
        return resp;
    }

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
        DataApiResponse<PortrayalDetailDTO> resp = new DataApiResponse<>();
        resp.setData(portrayalService.portrayal(tableName,1,pKey,true));
        return resp;
    }

}
