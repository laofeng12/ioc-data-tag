package com.openjava.datatag.statistic.service;

import com.openjava.datatag.statistic.repository.TagDashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class TagDashboardServiceImpl implements TagDashboardService {
    @Resource
    private TagDashboardRepository tagDashboardRepository;

    @Override
    public String getDataSetIncreasePercentage() {
        return tagDashboardRepository.getDataSetIncreasePercentage();
    }

    @Override
    public int getLastMonthTagSum(){
        return tagDashboardRepository.getLastMonthTagSum();
    }

    @Override
    public  double getTagThanDataSet(){
        return tagDashboardRepository.getTagThanDataSet();
    }

    @Override
    public List<Object> getMonthlyLabelChanges(){return tagDashboardRepository.getMonthlyLabelChanges();}

    @Override
    public List<Object> getAllYearMonth(){return tagDashboardRepository.getAllYearMonth();}

    @Override
    public List<Object> getHotTags(){return tagDashboardRepository.getHotTags();}
}
