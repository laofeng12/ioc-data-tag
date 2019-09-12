package com.openjava.datatag.statistic.service;


import java.util.List;
import java.util.Map;

public interface TagDashboardService {
    Long  lastMonthDataSetCount();
    int getLastMonthTagSum();
    String getTagThanDataSet();
    List<Object> getMonthlyLabelChanges()throws Exception;
    Map<String,String> getAllYearMonth();
    List<Object> getSamedayHotTags();
    List<Object> getYesterdayHotTags();
    List<Object> getLastweekHotTags();
    List<Object> getLastMonthHotTags();
    List<Object> getLastYearHotTags();

}
