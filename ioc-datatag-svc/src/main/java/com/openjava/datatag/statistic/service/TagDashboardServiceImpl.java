package com.openjava.datatag.statistic.service;

import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
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
    @Resource
    private AuditComponet auditComponet;

    @Override
    public Long lastMonthDataSetCount() throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("使用数据集");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.lastMonthDataSetCount();
    }

    @Override
    public int getLastMonthTagSum()throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("新增唯一标签");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.getLastMonthTagSum();
    }

    @Override
    public  String getTagThanDataSet()throws Exception{
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
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("使用标签/使用数据集");//必传
        auditComponet.saveAuditLog(vo);
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
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("今年每个月标签变化");//必传
        auditComponet.saveAuditLog(vo);
        return list ;
    }

    @Override
    //public List<Object> getAllYearMonth(){return tagDashboardRepository.getAllYearMonth();}
    public Map<String,String> getAllYearMonth()throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("左边数据列表");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.getAllYearMonth();
    }


    @Override
    public List<Object> getYesterdayHotTags()throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("昨日热门标签-top5");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.getYesterdayHotTags();
    }

    @Override
    public List<Object> getLastweekHotTags()throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("最近一周热门标签-top5");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.getLastweekHotTags();
    }

    @Override
    public List<Object> getLastMonthHotTags()throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("最近一个月热门标签-top5");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.getLastMonthHotTags();
    }

    @Override
    public List<Object> getLastYearHotTags()throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("最近一年热门标签-top5");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.getLastYearHotTags();
    }

    @Override
    public List<Object> getSamedayHotTags()throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签仪表盘");//必传
        vo.setFunctionLev1("顶部三大数据");//必传
        vo.setFunctionLev2("今日热门标签-top5");//必传
        auditComponet.saveAuditLog(vo);
        return tagDashboardRepository.getSamedayHotTags();
    }
}
