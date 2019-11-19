package com.openjava.datatag.statistic.api;

import com.openjava.datatag.statistic.domain.DtTagTemp;
import com.openjava.datatag.statistic.domain.DtTagThanDataSet;
import com.openjava.datatag.statistic.domain.DtTaggingModelTemp;
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

/**

 * @author : maliang
 * creatdate:2019/06/27
 * 1.提供上个月数据集的增长变化百分比查询
 * 2.提供上个月所有标签对应所有数据集数据的均数查询
 * 3.提供上个月标签数据量统计
 *
 */
@Api(tags = "标签仪表盘-顶部三大数据")
@RestController
@RequestMapping("/datatag/statistic")
public class TagMonthIncrease {
    @Resource
    private com.openjava.datatag.statistic.service.TagDashboardService TagDashboardService;

    /**
     * 获取上个月使用数据集
     *
     */
    @ApiOperation(value = "使用数据集个数(多个模型使用同一个数据集时算一个使用数据集)", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    @Security(session=true)
    @ResponseBody
    @RequestMapping(value = "/datasetgrowth",method = RequestMethod.GET)
    public DataApiResponse<DtTaggingModelTemp> getDataSetGrowth()throws Exception{
        Long lastMonthDataSetCount = TagDashboardService.lastMonthDataSetCount();
        DtTaggingModelTemp dtTaggingModelTemp = new DtTaggingModelTemp(lastMonthDataSetCount);
        DataApiResponse<DtTaggingModelTemp> apiResp = new DataApiResponse<>();
        apiResp.setData(dtTaggingModelTemp);
        return apiResp;
    }

    /**
     * 上新增唯一标签数量
     *
     * */
    private int tagsum;
    @ApiOperation(value = "上新增唯一标签数量", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    @Security(session=true)
    @ResponseBody
    @RequestMapping(value = "/getLastMonthTagSum",method = RequestMethod.GET)
    public Object getLastMonthTagSum()throws Exception{
        tagsum = TagDashboardService.getLastMonthTagSum();
        DtTagTemp dtTagTemp = new DtTagTemp(tagsum);
        DataApiResponse<DtTagTemp> apiResp = new DataApiResponse<>();
        apiResp.setData(dtTagTemp);
        return apiResp;
    }

    /**
     * 获取上个月使用标签和使用据集的比重
     *
     */
    @ApiOperation(value = "获取上个月使用标签和使用据集的比重", nickname="select", notes = "报文格式：content-type=application/json")
    @ApiResponses({
            @io.swagger.annotations.ApiResponse(code=200, message="数据获取成功"),
    })
    @Security(session=true)
    @ResponseBody
    @RequestMapping(value = "/getTagThanDataSet",method = RequestMethod.GET)
    public Object getTagThanDataSet()throws Exception{
        String percentage = TagDashboardService.getTagThanDataSet();
        DtTagThanDataSet dtTagThanDataSet = new DtTagThanDataSet(percentage);
        DataApiResponse<DtTagThanDataSet> apiResp = new DataApiResponse<>();
        apiResp.setData(dtTagThanDataSet);
        return apiResp;
    }

}
