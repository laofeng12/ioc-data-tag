package com.openjava.datatag.statistic.service;

import com.openjava.datatag.statistic.repository.TagDashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    //public List<Object> getAllYearMonth(){return tagDashboardRepository.getAllYearMonth();}
    public Map<String,String> getAllYearMonth(){return tagDashboardRepository.getAllYearMonth();}


    @Override
    public List<Object> getYesterdayHotTags(){return tagDashboardRepository.getYesterdayHotTags();}

    @Override
    public List<Object> getLastweekHotTags(){return tagDashboardRepository.getLastweekHotTags();}

    @Override
    public List<Object> getLastMonthHotTags(){return tagDashboardRepository.getLastMonthHotTags();}

    @Override
    public List<Object> getLastYearHotTags(){return tagDashboardRepository.getLastYearHotTags();}

    @Override
    public List<Object> getSamedayHotTags(){return tagDashboardRepository.getSamedayHotTags();}
}
