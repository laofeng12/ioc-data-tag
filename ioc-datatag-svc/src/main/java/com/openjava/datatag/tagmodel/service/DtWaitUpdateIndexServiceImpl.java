package com.openjava.datatag.tagmodel.service;

import java.util.*;
import javax.annotation.Resource;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.component.PostgreSqlConfig;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.utils.jdbc.dataprovider.result.AggregateResult;
import com.openjava.datatag.utils.jdbc.dataprovider.result.ColumnIndex;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagmodel.domain.DtWaitUpdateIndex;
import com.openjava.datatag.tagmodel.query.DtWaitUpdateIndexDBParam;
import com.openjava.datatag.tagmodel.repository.DtWaitUpdateIndexRepository;
/**
 * 更新索引表业务层
 * @author zmk
 *
 */
@Service
@Transactional
public class DtWaitUpdateIndexServiceImpl implements DtWaitUpdateIndexService {
	Logger logger = LogManager.getLogger(getClass());
	@Resource
	private DtWaitUpdateIndexRepository dtWaitUpdateIndexRepository;
	@Resource
	private DtTaggingModelService dtTaggingModelService;
	@Resource
	private PostgreSqlConfig postgreSqlConfig;
	public Page<DtWaitUpdateIndex> query(DtWaitUpdateIndexDBParam params, Pageable pageable){
		Page<DtWaitUpdateIndex> pageresult = dtWaitUpdateIndexRepository.query(params, pageable);
		return pageresult;
	}
	
	public List<DtWaitUpdateIndex> queryDataOnly(DtWaitUpdateIndexDBParam params, Pageable pageable){
		return dtWaitUpdateIndexRepository.queryDataOnly(params, pageable);
	}
	
	public DtWaitUpdateIndex get(Long id) {
		Optional<DtWaitUpdateIndex> o = dtWaitUpdateIndexRepository.findById(id);
		if(o.isPresent()) {
			DtWaitUpdateIndex m = o.get();
			return m;
		}
		System.out.println("找不到记录DtWaitUpdateIndex："+id);
		return null;
	}
	
	public DtWaitUpdateIndex doSave(DtWaitUpdateIndex m) {
		return dtWaitUpdateIndexRepository.save(m);
	}
	
	public void doDelete(Long id) {
		dtWaitUpdateIndexRepository.deleteById(id);
	}
	public void doRemove(String ids) {
		String[] items = ids.split(",");
		for (int i = 0; i < items.length; i++) {
			dtWaitUpdateIndexRepository.deleteById(new Long(items[i]));
		}
	}
	public List<DtWaitUpdateIndex> getByRunState(Long runState){
		return dtWaitUpdateIndexRepository.getByRunState(runState);
	}
	public void updateModelIndex(List<DtWaitUpdateIndex> waitList){
		MppPgExecuteUtil mppUtil = new MppPgExecuteUtil();
		mppUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
		if (CollectionUtils.isNotEmpty(waitList)) {
			for (int i = 0; i < waitList.size() ; i++) {
				DtWaitUpdateIndex waitUpdateIndex = waitList.get(i);
				String modelTableName = waitUpdateIndex.getTableName();
				String modelKeyColName = waitUpdateIndex.getModelKeyColName();
				try	{
					//第一步，先删中间表数据
					String deleteSql = "delete from \""+Constants.DT_SEARCH_TABLE_NAME+"\" t where t.model_table_name = '"+modelTableName+"'";
					List<String> deleteSqlList = new LinkedList<>();
					deleteSqlList.add(deleteSql);
					mppUtil.setUpdateSqlList(deleteSqlList);
					mppUtil.updateDataList();
					mppUtil.setSQL("select count(1) from \""+modelTableName+"\"");
					long totalCount = 0;
					try {
						String[][] count = mppUtil.getData2();
						totalCount = Long.valueOf(count[1][0]);
					}catch (Exception e){
						e.printStackTrace();
						logger.info(e.getMessage());
					}
					//第二步更新中间表
					int pageSize = 10000;
					Pageable pageable  = PageRequest.of(0, pageSize);
					String alias = "t";//别名
					Map<String, String> tableNameForQuery = new LinkedHashMap<>(1);
					tableNameForQuery.put(modelTableName,alias);
					mppUtil.setTableNameForQuery(tableNameForQuery);
					Map<String, String> columnMapForQuery = new LinkedHashMap<>(1);
					columnMapForQuery.put(modelKeyColName,alias);
					mppUtil.setColumnMapForQuery(columnMapForQuery);
					mppUtil.setPageable(pageable);
					String[][] firstPageData = mppUtil.getData();//第一个为表头
					List<Object> data = new ArrayList<>();
					for (int j = 1; j <firstPageData.length ; j++) {
						List<Object> oblist = Arrays.asList(firstPageData[j]);
						List<Object> temp = new LinkedList<>();
						oblist.forEach(record->{
							temp.add(record);
						});
						temp.add(modelTableName);
						temp.add(modelTableName);
						data.add(temp);
					}
					Map<String, String> columnMap = new LinkedHashMap<>(1);
					columnMap.put(Constants.DT_SEARCH_MODEL_PKEY,"模型的主键");
					columnMap.put(Constants.DT_SEARCH_MODEL_TABLE_NAME,"模型表名称");
					columnMap.put(Constants.DT_SEARCH_MODEL_PKEY_NAME,"模型主键列名");
					mppUtil.setTableName(Constants.DT_SEARCH_TABLE_NAME);
					mppUtil.setColumnMap(columnMap);
					mppUtil.setDataList(data);
					mppUtil.insertDataList();
					Page firstPage = new PageImpl<>(data, pageable, totalCount);
					if (firstPage.hasNext()) {
						for (int j = 1; j < firstPage.getTotalPages(); j++) {
							Pageable nextPage  = PageRequest.of(j, pageSize);
							try{
								mppUtil.setPageable(nextPage);
								String[][] nextPageData = mppUtil.getData();//第一个为表头
								List<Object> nextData = new ArrayList<>();
								for (int k = 1; k<nextPageData.length ; k++) {
									List<Object> oblist =Arrays.asList(nextPageData[k]);
									List<Object> temp = new LinkedList<>();
									oblist.forEach(record->{
										temp.add(record);
									});
									temp.add(modelTableName);
									temp.add(modelTableName);
									nextData.add(temp);
								}
								mppUtil.setDataList(nextData);
								mppUtil.insertDataList();
							}catch (Exception e){
								e.printStackTrace();
							}
						}
					}
				}catch (Exception e){
					e.printStackTrace();
					logger.info(e.getMessage());
				}
				doDelete(waitUpdateIndex.getId());
			}
		}
	}
}
