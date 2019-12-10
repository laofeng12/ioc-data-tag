package com.openjava.datatag.statistic.api;

import com.openjava.datatag.common.MyErrorConstants;
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


/**
 * @author : maliang
 * creatdate:2019/06/27
 * 热门标签top
 * 1.获取top5的相关热门标签
 *
 */
@Api(tags = "标签仪表盘-热门标签top")
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
    @Security(session=true,allowResources = {"tagPanel"})
    @ResponseBody
    @RequestMapping(value = "getsamedayhottags" ,method = RequestMethod.GET)
    public  Object getHotTags()throws Exception{
        List<Object> list = TagDashboardService.getSamedayHotTags();//获取今日 top5热门标签
        DataApiResponse<Object> apiResp = new DataApiResponse<>();//构造返回结构
        apiResp.setData(list);//设置返回数据
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
    @Security(session=true,allowResources = {"tagPanel"})
    @ResponseBody
    @RequestMapping(value = "getyesterdayhottags" ,method = RequestMethod.GET)
    public  Object getYesterdayHotTags()throws Exception{
        List<Object> list = TagDashboardService.getYesterdayHotTags();//获取昨日 top5热门标签
        DataApiResponse<Object> apiResp = new DataApiResponse<>();//构造返回结构
        apiResp.setData(list);//设置返回数据
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
    @Security(session=true,allowResources = {"tagPanel"})
    @ResponseBody
    @RequestMapping(value = "getlastweekhottags" ,method = RequestMethod.GET)
    public  Object getLastweekHotTags()throws Exception{
        List<Object> list = TagDashboardService.getLastweekHotTags();//获取最近一周 top5热门标签
        DataApiResponse<Object> apiResp = new DataApiResponse<>();//构造返回结构
        apiResp.setData(list);//设置返回数据
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
    @Security(session=true,allowResources = {"tagPanel"})
    @ResponseBody
    @RequestMapping(value = "getlastmonthhottags" ,method = RequestMethod.GET)
    public  Object getLastMonthHotTags()throws Exception{
        List<Object> list = TagDashboardService.getLastMonthHotTags();//获取最近一周 top5热门标签
        DataApiResponse<Object> apiResp = new DataApiResponse<>();//构造返回结构
        apiResp.setData(list);//设置返回数据
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
    @Security(session=true,allowResources = {"tagPanel"})
    @ResponseBody
    @RequestMapping(value = "getlastyearhottags" ,method = RequestMethod.GET)
    public  Object getLastYearHotTags()throws Exception{
        List<Object> list = TagDashboardService.getLastYearHotTags();//获取最近一周 top5热门标签
        DataApiResponse<Object> apiResp = new DataApiResponse<>();//构造返回结构
        apiResp.setData(list);//设置返回数据
        return apiResp;
    }

}
