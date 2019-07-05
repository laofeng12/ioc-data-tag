package com.openjava.datatag.statistic.api;

import com.openjava.datatag.statistic.domain.*;
import io.swagger.annotations.Api;
import org.ljdp.component.result.DataApiResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * 外部访问，请求层
 * @author : maliang
 * creatdate:2019/06/27
 * 用途：
 * 1.提供上个月数据集的增长变化百分比查询
 * 2.提供上个月所有标签对应所有数据集数据的均数查询
 * 3.提供上个月标签数据量统计
 *
 */

@Api(tags="标签仪表盘表数据集增长统计")
@RestController
@RequestMapping("/api/tagDashboard")
public class LableDashboardLableDataSetCount {

    @Resource
    private com.openjava.datatag.statistic.service.TagDashboardService TagDashboardService;


    /*
    * 获取上个月数据集增长百分比
    *
    * */

    private String growth_rate;

    @ResponseBody
    @RequestMapping(value = "/datasetgrowth",method = RequestMethod.GET)
    public Object getDataSetGrowth(){

        growth_rate = TagDashboardService.getDataSetIncreasePercentage();
        DtTaggingModelTemp dtTaggingModelTemp = new DtTaggingModelTemp(growth_rate);
        DataApiResponse<DtTaggingModelTemp> apiResp = new DataApiResponse<>();
        apiResp.setData(dtTaggingModelTemp);
        return apiResp;
    }

    /*
     * 获取上个月唯一标签新增数量
     *
     * */
    private int tagsum;
    @ResponseBody
    @RequestMapping(value = "/getLastMonthTagSum",method = RequestMethod.GET)
    public Object getLastMonthTagSum(){
        tagsum = TagDashboardService.getLastMonthTagSum();
        DtTagTemp dtTagTemp = new DtTagTemp(tagsum);
        DataApiResponse<DtTagTemp> apiResp = new DataApiResponse<>();
        apiResp.setData(dtTagTemp);
        return apiResp;
    }

    /*
     * 获取上个月每个数据集对应的标签的平均数
     *
     * */
    private  double numbers;
    @ResponseBody
    @RequestMapping(value = "/getTagThanDataSet",method = RequestMethod.GET)
    public Object getTagThanDataSet(){
        numbers = TagDashboardService.getTagThanDataSet();
        DtTagThanDataSet dtTagThanDataSet = new DtTagThanDataSet(numbers);
        DataApiResponse<DtTagThanDataSet> apiResp = new DataApiResponse<>();
        apiResp.setData(dtTagThanDataSet);
        return apiResp;
    }

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

    /*
     *
     *获取top 5的热门标签
     * */
    @ResponseBody
    @RequestMapping(value = "getHotTags" ,method = RequestMethod.GET)
    public  Object getHotTags(){
        List<Object>  list = TagDashboardService.getHotTags();
        DataApiResponse<Object> apiResp = new DataApiResponse<>();
        apiResp.setData(list);
        return apiResp;
    }



}
