package com.openjava.datatag.statistic.service;


import java.util.List;
import java.util.Map;

public interface TagDashboardService {
    public String  getDataSetIncreasePercentage();
    public int getLastMonthTagSum();
    public double getTagThanDataSet();
    public List<Object> getMonthlyLabelChanges();
    //public List<Object> getAllYearMonth();
    public Map<String,String> getAllYearMonth();
    public List<Object> getSamedayHotTags();
    public List<Object> getYesterdayHotTags();
    public List<Object> getLastweekHotTags();
    public List<Object> getLastMonthHotTags();
    public List<Object> getLastYearHotTags();

}
