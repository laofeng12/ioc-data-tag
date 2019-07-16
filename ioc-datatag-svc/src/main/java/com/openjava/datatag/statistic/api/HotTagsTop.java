package com.openjava.datatag.statistic.api;

import com.openjava.datatag.common.MyErrorConstants;
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


/**
 * @author : maliang
 * creatdate:2019/06/27
 * 热门标签top
 * 1.获取top5的相关热门标签
 *
 */

@RestController
@RequestMapping("/datatag/statistic")
public class HotTagsTop {
    @Resource
    private com.openjava.datatag.statistic.service.TagDashboardService TagDashboardService;
    /*
     *
     *获取今日 top5热门标签
     * */
    @ApiOperation(value = "热门标签:获取今日热门标签-top5", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    //验证用户登录
    //@Security(session=true)
    @ResponseBody
    @RequestMapping(value = "getsamedayhottags" ,method = RequestMethod.GET)
    public  Object getHotTags(){
        List<Object> list = TagDashboardService.getSamedayHotTags();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

    /*
     *
     *获取昨日 top5热门标签
     * */
    @ApiOperation(value = "热门标签:获取昨日热门标签-top5", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    //验证用户登录
    //@Security(session=true)
    @ResponseBody
    @RequestMapping(value = "getyesterdayhottags" ,method = RequestMethod.GET)
    public  Object getYesterdayHotTags(){
        List<Object> list = TagDashboardService.getYesterdayHotTags();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

    /*
     *
     *获取最近一周 top5热门标签
     * */
    @ApiOperation(value = "热门标签:获取最近一周热门标签-top5", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    //验证用户登录
    //@Security(session=true)
    @ResponseBody
    @RequestMapping(value = "getlastweekhottags" ,method = RequestMethod.GET)
    public  Object getLastweekHotTags(){
        List<Object> list = TagDashboardService.getLastweekHotTags();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

    /*
     *
     *获取最近一个月 top5热门标签
     * */
    @ApiOperation(value = "热门标签:获取最近一个月热门标签-top5", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    //验证用户登录
    //@Security(session=true)
    @ResponseBody
    @RequestMapping(value = "getlastmonthhottags" ,method = RequestMethod.GET)
    public  Object getLastMonthHotTags(){
        List<Object> list = TagDashboardService.getLastMonthHotTags();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

    /*
     *
     *获取最近一个年 top5热门标签
     * */
    @ApiOperation(value = "热门标签:获取最近一年热门标签-top5", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    //验证用户登录
    //@Security(session=true)
    @ResponseBody
    @RequestMapping(value = "getlastyearhottags" ,method = RequestMethod.GET)
    public  Object getLastYearHotTags(){
        List<Object> list = TagDashboardService.getLastYearHotTags();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }

}
