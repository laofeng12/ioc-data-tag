package com.openjava.datatag.statistic.api;

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
 * 热门标签top
 * 1.获取top5的相关热门标签
 *
 */

@RestController
@RequestMapping("/api/tagDashboard1")
public class HotTagsTop {
    @Resource
    private com.openjava.datatag.statistic.service.TagDashboardService TagDashboardService;
    /*
     *
     *获取top 5的热门标签
     * */
    @ResponseBody
    @RequestMapping(value = "getHotTags" ,method = RequestMethod.GET)
    public  Object getHotTags(){
        List<Object> list = TagDashboardService.getHotTags();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

}
