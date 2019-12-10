package com.openjava.datatag.statistic.service;


import java.util.List;
import java.util.Map;

/**
 * 标签仪表盘业务层
 */
public interface TagDashboardService {
    /**
     * 使用标签/使用数据集
     * @return
     * @throws Exception
     */
    Long  lastMonthDataSetCount()throws Exception;//上个月数据集使用个数

    /**
     * 查询上个月新增唯一标签数量
     * @return
     * @throws Exception
     */
    int getLastMonthTagSum()throws Exception;//查询上个月新增唯一标签数量

    /**
     * 使用标签/使用数据集
     * @return
     * @throws Exception
     */
    String getTagThanDataSet()throws Exception;//使用标签/使用数据集

    /**
     *
     * @return
     * @throws Exception
     */
    List<Object> getMonthlyLabelChanges()throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    Map<String,String> getAllYearMonth()throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<Object> getSamedayHotTags()throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<Object> getYesterdayHotTags()throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<Object> getLastweekHotTags()throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<Object> getLastMonthHotTags()throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<Object> getLastYearHotTags()throws Exception;

}
