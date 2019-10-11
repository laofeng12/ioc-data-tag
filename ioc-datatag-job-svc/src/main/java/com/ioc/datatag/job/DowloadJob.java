package com.ioc.datatag.job;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.component.FtpUtil;
import com.openjava.datatag.component.PostgreSqlConfig;
import com.openjava.datatag.dowload.domain.DownloadQueue;
import com.openjava.datatag.dowload.service.DownloadQueueService;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.userprofile.service.PortrayalService;
import com.openjava.datatag.utils.export.ExportUtil;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DowloadJob {
    private Logger logger= LoggerFactory.getLogger(TestJob.class);

    @Resource
    private DownloadQueueService downloadQueueService;
    @Resource
    private PortrayalService portrayalService;
    @Resource
    private DtTaggingModelService dtTaggingModelService;
    @Autowired
    private PostgreSqlConfig postgreSqlConfig;
    @Autowired
    private FtpUtil ftpUtil;
    @Scheduled(cron = "${schedule.dowload}")
    public void cronJob() throws Exception {
        List<DownloadQueue> downloadQueueList = downloadQueueService.findByState(Constants.DT_DOWLOAD_STATE_DOWLOADING);
        if (CollectionUtils.isNotEmpty(downloadQueueList)){
            return;
        }
        List<DownloadQueue> waitList = downloadQueueService.findByState(Constants.DT_DOWLOAD_STATE_WAIT);
        if (CollectionUtils.isNotEmpty(waitList)){
            DownloadQueue queue = waitList.get(0);
            logger.info("开始导出模型id为："+queue.getBid()+"的画像结果");
            DtTaggingModel model = dtTaggingModelService.get(Long.valueOf(queue.getBid()));
            queue.setState(Constants.DT_DOWLOAD_STATE_DOWLOADING);
            queue = downloadQueueService.doSaveNow(queue);
            Long number = queue.getDownloadNum();
            try{
                //第一步先删文件
                ExportUtil.deleFile(Constants.DT_BTYPE_DATATAG,model.getTaggingModelId().toString());
                String modelTableName = Constants.DT_TABLE_PREFIX+model.getTaggingModelId();
                MppPgExecuteUtil mppUtil = new MppPgExecuteUtil();
                mppUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
                long totalCount = 0;
                int pageSize = Constants.DT_RESULT_DOWLOAD_LIMIT;
                Pageable pageable  = PageRequest.of(0, pageSize);
                mppUtil.setSQL("select count(1) from \""+modelTableName+"\"");
                String[][] count = mppUtil.getData2();
                totalCount = Long.valueOf(count[1][0]);//总记录数
                if (number!=null && number<=totalCount) {
                    totalCount = number;
                }
                List<Object> data = new ArrayList<>();
                String alias = "t";//别名
                Map<String, String> tableNameForQuery = new LinkedHashMap<>(1);
                tableNameForQuery.put(modelTableName,alias);
                mppUtil.setTableNameForQuery(tableNameForQuery);
                Page page = new PageImpl<>(data, pageable, totalCount);
                for (int j = 0; j < page.getTotalPages(); j++) {
                    Pageable nextPage  = PageRequest.of(j, pageSize);
                    mppUtil.setPageable(nextPage);
                    String[][] firstPageData = mppUtil.getData();//第一个为表头
                    //TODO 导出excel
                    ExportUtil.export(firstPageData,Constants.DT_BTYPE_DATATAG,model.getTaggingModelId().toString(),j);
                    //TODO 更新进度
                    String speedOfProgress = new BigDecimal(j+1).divide(new BigDecimal(page.getTotalPages()),2,BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_UP).toString();
                    queue.setSpeedOfProgress(speedOfProgress);
                    queue.setDownloadNum(totalCount);
                    queue = downloadQueueService.doSaveNow(queue);
                }
                //TODO 压缩文件并保存文件
                ExportUtil.toZip(Constants.DT_BTYPE_DATATAG,model.getTaggingModelId().toString());
                File file = ExportUtil.getZipLocalFile(Constants.DT_BTYPE_DATATAG,model.getTaggingModelId().toString());
                //上传到ftp
                String path =ftpUtil.getLocalPath()+"\\"+Constants.DT_BTYPE_DATATAG+"\\"+model.getTaggingModelId().toString();
                String fileName  = file.getName();
                String localPath = file.getPath();
                boolean uploadFtp =  ftpUtil.uploadFile(path,fileName,localPath);
                if (!uploadFtp){
                    throw new ApiException("画像结果压缩包上传到ftp失败！");
                }
                queue.setFileSize(file.length()/1024+"KB") ;
                queue.setSpeedOfProgress("100");
                queue.setState(Constants.DT_DOWLOAD_STATE_SUCCESS);
                queue = downloadQueueService.doSaveNow(queue);
                logger.info("导出模型id为："+queue.getBid()+",画像结果成功");
            }catch (Exception e ){
                e.printStackTrace();
                logger.info("导出失败");
                //TODO 下载异常之后的操作
                queue.setState(Constants.DT_DOWLOAD_STATE_ERROR);
                queue = downloadQueueService.doSaveNow(queue);
            }
        }
    }

}
