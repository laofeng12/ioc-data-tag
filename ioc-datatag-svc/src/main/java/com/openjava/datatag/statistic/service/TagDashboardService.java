package com.openjava.datatag.statistic.service;


import java.util.List;
import java.util.Map;

public interface TagDashboardService {
    Long  lastMonthDataSetCount()throws Exception;
    int getLastMonthTagSum()throws Exception;
    String getTagThanDataSet()throws Exception;
    List<Object> getMonthlyLabelChanges()throws Exception;
    Map<String,String> getAllYearMonth()throws Exception;
    List<Object> getSamedayHotTags()throws Exception;
    List<Object> getYesterdayHotTags()throws Exception;
    List<Object> getLastweekHotTags()throws Exception;
    List<Object> getLastMonthHotTags()throws Exception;
    List<Object> getLastYearHotTags()throws Exception;

}
