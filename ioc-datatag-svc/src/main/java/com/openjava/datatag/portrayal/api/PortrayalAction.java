package com.openjava.datatag.portrayal.api;

import com.openjava.datatag.portrayal.dto.PortrayalDetailDTO;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.user.service.SysUserService;
import com.openjava.datatag.utils.VoUtils;
import io.swagger.annotations.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
            @ApiImplicitParam(name = "Id", value = "画像ID", required = false, dataType = "String", paramType = "query"),
    })
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code = 20020, message = "会话失效")
    })
    @Security(session = true)
    @RequestMapping(value = "/search", method = RequestMethod.POST)

    public DataApiResponse<PortrayalDetailDTO> doSearchCool(@RequestParam(value = "Id") Long Id) {
        List<PortrayalDetailDTO> result = new ArrayList<>();
        int i = 0;
        if(Id!=null)
        {
            i=2;
        }
        while (i <= 2) {
            String tag = VoUtils.toString(i);
            PortrayalDetailDTO por = new PortrayalDetailDTO();
            por.setId("9527" + tag);
            por.setTitle("测试南城区公民信息数据" + tag);
            Map<String, String> param = new HashMap<>();
            param.put("name", "张晓菲" + tag);
            param.put("sex", "女" );
            param.put("age", 17+ tag);
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


}
