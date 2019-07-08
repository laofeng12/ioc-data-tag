package com.openjava.datatag.statistic.service;


import java.util.List;

public interface TagDashboardService {
    public String  getDataSetIncreasePercentage();
    public int getLastMonthTagSum();
    public double getTagThanDataSet();
    public List<Object> getMonthlyLabelChanges();
    public List<Object> getAllYearMonth();
    public List<Object> getHotTags();

}
