package com.ioc.datatag.job;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class TagModelImmediatelyRun  extends Thread  {
    private Logger logger= LoggerFactory.getLogger(TagModelImmediatelyRun.class);
    @Resource
    private DtTaggingModelService dtTaggingModelService;


//    @Scheduled(cron = "${schedule.datatagImmediatelyRunJob}")
//    public void cronJob() throws Exception{
//        List<DtTaggingModel> list  = dtTaggingModelService.findByCycleEnum(Constants.DT_DISPATCH_NOW);
//        for (DtTaggingModel model:list
//             ) {
//            model.setRunState(Constants.DT_MODEL_RUNNING);
//            dtTaggingModelService.calculation(model);
//        }
//    }

}
