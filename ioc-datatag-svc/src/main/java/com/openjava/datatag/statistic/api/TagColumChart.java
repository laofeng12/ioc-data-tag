package com.openjava.datatag.statistic.api;


import com.openjava.datatag.statistic.service.TagDashboardService;
import org.ljdp.component.result.DataApiResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**

 * @author : maliang
 * creatdate:2019/06/27
 * 标签变化模块
 * 1.提供柱形图，去年每个月标签变化数量查询
 * 2.提供全部标签数量，去年，上个月标签数据查询
 *
 */

@RestController
@RequestMapping("/api/tagDashboard")
public class TagColumChart {

    @Resource
    private com.openjava.datatag.statistic.service.TagDashboardService TagDashboardService;

    /*
     * 获取去年每个月标签变化
     *
     * */

    @ResponseBody
    @RequestMapping(value = "getMonthlyLabelChanges",method = RequestMethod.GET)
    public Object getMonthlyLabelChanges(){
        List<Object> list = TagDashboardService.getMonthlyLabelChanges();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

    /*
     *
     *获取标签变化左边数据列表
     * */
    @ResponseBody
    @RequestMapping(value = "getAllYearMonth",method = RequestMethod.GET)
    public Object getLeftListData(){
        List<Object>  list = TagDashboardService.getAllYearMonth();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

}
