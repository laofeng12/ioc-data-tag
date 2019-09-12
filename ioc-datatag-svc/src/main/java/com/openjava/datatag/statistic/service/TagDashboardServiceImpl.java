package com.openjava.datatag.statistic.service;

import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.statistic.repository.TagDashboardRepository;
import com.openjava.datatag.utils.MyTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TagDashboardServiceImpl implements TagDashboardService {
    @Resource
    private TagDashboardRepository tagDashboardRepository;

    @Override
    public Long lastMonthDataSetCount() {
        return tagDashboardRepository.lastMonthDataSetCount();
    }

    @Override
    public int getLastMonthTagSum(){
        return tagDashboardRepository.getLastMonthTagSum();
    }

    @Override
    public  String getTagThanDataSet(){
        String tagThanDataSet = "0%";
        Long lastMonthTagCount = tagDashboardRepository.lastMonthTagCount();
        Long lastMonthDataSetCount = tagDashboardRepository.lastMonthDataSetCount();
        if (lastMonthDataSetCount!=0) {
            BigDecimal bigDecimal = new  BigDecimal(lastMonthTagCount)
                    .divide(new BigDecimal(lastMonthDataSetCount),2,BigDecimal.ROUND_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(0,BigDecimal.ROUND_UP);
            tagThanDataSet = bigDecimal.toString()+"%";
        }
        return tagThanDataSet;
    }

    @Override
    public List<Object> getMonthlyLabelChanges()throws Exception{
        List<Object> list = new ArrayList<>();
//        return tagDashboardRepository.getMonthlyLabelChanges();
        Date[][] dates = MyTimeUtil.getThisYearEveryMoth(new Date());
        for (Date[] beginAndEnd:dates) {
            list.add(tagDashboardRepository.countByCreateTime(beginAndEnd[0],beginAndEnd[1]));
        }
        return list ;
    }

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
