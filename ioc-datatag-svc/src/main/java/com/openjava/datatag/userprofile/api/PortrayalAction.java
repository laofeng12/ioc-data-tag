package com.openjava.datatag.userprofile.api;

import com.openjava.datatag.userprofile.dto.PortrayalDetailDTO;
import com.openjava.datatag.user.service.SysUserService;
import com.openjava.datatag.utils.VoUtils;
import io.swagger.annotations.*;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.secure.annotation.Security;
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

    @ApiOperation(value = "查询画像记录集", notes = "结果对象数组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "画像ID", required = false, dataType = "String", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效")
    })
    @Security(session = true)
    @RequestMapping(value = "/search/{id}", method = RequestMethod.GET)
    public DataApiResponse<PortrayalDetailDTO> doSearchCool(@PathVariable(value = "id") String id) {
        List<PortrayalDetailDTO> result = new ArrayList<>();
        int i = 0;
        if (id.equals("") || id == null) {
            i = 2;
        }
        while (i <= 2) {
            String tag = VoUtils.toString(i);
            PortrayalDetailDTO por = new PortrayalDetailDTO();
            por.setId("9527" + tag);
            por.setDetailId("100"+tag);
            por.setTitle("测试南城区公民信息数据" + tag);
            List<String> param = new ArrayList<>();

            param.add("张晓菲" + tag);
            param.add("女");
            param.add(17 + tag);
            por.setProperty(param);
            List<String> list = new ArrayList<>();
            list.add("美容顾问" + tag);
            list.add("租房" + tag);
            list.add("工作5年" + tag);
            list.add("乐观派" + tag);
            list.add("月入20000元" + tag);
            list.add("未婚" + tag);
            list.add("单身" + tag);
            por.setLists(list);
            i = i + 1;
            result.add(por);
        }
        DataApiResponse<PortrayalDetailDTO> resp = new DataApiResponse<>();
        resp.setRows(result);
        return resp;
    }

    @ApiOperation(value = "查询画像详情", notes = "查询画像详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "画像详情ID", required = false, dataType = "String", paramType = "path"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效")
    })
    @Security(session = true)
    @RequestMapping(value = "/getCoolDetail/{detailId}", method = RequestMethod.GET)
    public DataApiResponse<PortrayalDetailDTO> getCoolDetail(@PathVariable(value = "detailId") String detailId) {
        PortrayalDetailDTO por = new PortrayalDetailDTO();
        por.setId("9527");
        por.setDetailId("10001");
        por.setTitle("测试南城区公民信息数据" );
        List<String> param = new ArrayList<>();
        param.add("张晓菲");
        param.add("女");
        param.add(17+"");
        por.setProperty(param);
        List<String> list = new ArrayList<>();
        list.add("美容顾问");
        list.add("租房");
        list.add("工作5年");
        list.add("乐观派");
        list.add("月入20000元");
        list.add("未婚");
        list.add("单身");
        por.setLists(list);
        DataApiResponse<PortrayalDetailDTO> resp = new DataApiResponse<>();
        resp.setData(por);
        return resp;
    }

}
