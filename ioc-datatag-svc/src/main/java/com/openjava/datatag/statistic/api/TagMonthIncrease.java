package com.openjava.datatag.statistic.api;

import com.openjava.datatag.statistic.domain.DtTagTemp;
import com.openjava.datatag.statistic.domain.DtTagThanDataSet;
import com.openjava.datatag.statistic.domain.DtTaggingModelTemp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.secure.annotation.Security;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**

 * @author : maliang
 * creatdate:2019/06/27
 * 1.提供上个月数据集的增长变化百分比查询
 * 2.提供上个月所有标签对应所有数据集数据的均数查询
 * 3.提供上个月标签数据量统计
 *
 */

@RestController
@RequestMapping("/datatag/statistic")
public class TagMonthIncrease {
    @Resource
    private com.openjava.datatag.statistic.service.TagDashboardService TagDashboardService;

    /*
     * 获取上个月数据集增长百分比
     *
     * */

    private String growth_rate;
    @ApiOperation(value = "标签/数据集增长变化:最近一个月数据集增长百分比", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    @Security(session=true)

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
    @ApiOperation(value = "标签/数据集增长变化:最近一个月标签新增数量", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    //验证用户登录
    //@Security(session=true)
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
    @ApiOperation(value = "标签/数据集增长变化:最近一个月每个数据集对应的标签的平均数", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    //验证用户登录
    //@Security(session=true)
    @ResponseBody
    @RequestMapping(value = "/getTagThanDataSet",method = RequestMethod.GET)
    public Object getTagThanDataSet(){
        numbers = TagDashboardService.getTagThanDataSet();
        DtTagThanDataSet dtTagThanDataSet = new DtTagThanDataSet(numbers);
        DataApiResponse<DtTagThanDataSet> apiResp = new DataApiResponse<>();
        apiResp.setData(dtTagThanDataSet);
        return apiResp;
    }

}
