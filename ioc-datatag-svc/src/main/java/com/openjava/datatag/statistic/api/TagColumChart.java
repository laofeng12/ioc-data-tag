package com.openjava.datatag.statistic.api;


import com.openjava.datatag.statistic.service.TagDashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.secure.annotation.Security;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**

 * @author : maliang
 * creatdate:2019/06/27
 * 标签变化模块
 * 1.提供柱形图，去年每个月标签变化数量查询
 * 2.提供全部标签数量，去年，上个月标签数据查询
 *
 */
@Api(tags = "标签仪表盘-标签变化模块")
@RestController
@RequestMapping("/datatag/statistic")
public class TagColumChart {

    @Resource
    private com.openjava.datatag.statistic.service.TagDashboardService TagDashboardService;

    /*
     * 获取去年每个月标签变化
     *
     * */
    @ApiOperation(value = "标签变化:今年每个月标签变化柱状图", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    @Security(session=true)
    @ResponseBody
    @RequestMapping(value = "getMonthlyLabelChanges",method = RequestMethod.GET)
    public Object getMonthlyLabelChanges() throws Exception{
        List<Object> list = TagDashboardService.getMonthlyLabelChanges();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

    /*
     *
     *获取标签变化左边数据列表
     * */
    @ApiOperation(value = "标签变化:左边数据列表", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    @Security(session=true)
    @ResponseBody
    @RequestMapping(value = "getAllYearMonth",method = RequestMethod.GET)
    public Object getLeftListData()throws Exception{
        //List<Object>  list = TagDashboardService.getAllYearMonth();
        Map<String,String> map = TagDashboardService.getAllYearMonth();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(map);
        return apiResp;
    }

}
