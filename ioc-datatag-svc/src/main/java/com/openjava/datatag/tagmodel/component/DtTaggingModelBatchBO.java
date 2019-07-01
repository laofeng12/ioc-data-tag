package com.openjava.datatag.tagmodel.component;

import java.util.Date;
import javax.annotation.Resource;

import org.ljdp.component.result.BatchResult;
import org.ljdp.component.result.GeneralBatchResult;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.util.DateFormater;
import org.ljdp.plugin.batch.bo.BaseFileImportBO;
import org.springframework.stereotype.Component;

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;

/**
 * 标签模型文件导入的业务对象（前台处理）
 * 运行顺序如下：
 * initialization();
 * for(读取文件每行数据->record){
 *    doProcessRecord(record);
 * }
 * finalWork();
 * destory();
 * 
 * @author zmk
 */
@Component
public class DtTaggingModelBatchBO extends BaseFileImportBO {

	@Resource
	private DtTaggingModelService dtTaggingModelService;
	
	@Override
	public String getTitle() {
		return "模型名字|打标目标表|打标临时表/模型表|声明的主键|创建用户|创建时间|修改用户名|修改时间|运行开始时间|调度运行周期|运行状态:未运行/运行中/运行出错/运行结束|删除标记|";
	}
	
	@Override
	protected BatchResult doProcessRecord(String record) {
		BatchResult result = new GeneralBatchResult();
		try {
			String[] items = record.split("\\|");
			int i = 0;
			//读取文件字段
			String modelName = items[i++];
			String taggingTable = items[i++];
			String taggingModelTable = items[i++];
			String pKey = items[i++];
			Long createUser = new Long(items[i++]);
			Date createTime = DateFormater.praseDate(items[i++]);
			String modifyUser = items[i++];
			Date modifyTime = DateFormater.praseDate(items[i++]);
			Date startTime = DateFormater.praseDate(items[i++]);
			String cycle = items[i++];
			Long runState = new Long(items[i++]);
			Long isDeleted = new Long(items[i++]);
			
			//新增记录
			DtTaggingModel j = new DtTaggingModel();
			SequenceService ss = ConcurrentSequence.getInstance();
			j.setTaggingModelId(ss.getSequence());
			j.setModelName(modelName);
			j.setTaggingTable(taggingTable);
			j.setTaggingModelTable(taggingModelTable);
			j.setPKey(pKey);
			j.setCreateUserId(createUser);
			j.setCreateTime(createTime);
			j.setModifyUser(modifyUser);
			j.setModifyTime(modifyTime);
			j.setStartTime(startTime);
			j.setCycle(cycle);
			j.setRunState(runState);
			j.setIsDeleted(isDeleted);
			
			dtTaggingModelService.doSave(j);
			
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}
}
