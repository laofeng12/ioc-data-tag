package com.openjava.datatag.tagmodel.service;

import java.util.*;
import javax.annotation.Resource;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.utils.jdbc.dataprovider.result.AggregateResult;
import com.openjava.datatag.utils.jdbc.dataprovider.result.ColumnIndex;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
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
	private DataProviderService dataProviderService;

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
	public void updateModelIndex(){
		List<DtWaitUpdateIndex> waitList =  dtWaitUpdateIndexRepository.getByRunState(Constants.DT_INDEX_RUN_STATUS_WAIT);
		MppPgExecuteUtil pg = new MppPgExecuteUtil();
		if (CollectionUtils.isNotEmpty(waitList)) {
			for (int i = 0; i < waitList.size() ; i++) {
				try	{
					String modelTableName = waitList.get(i).getTableName();
					String modelKeyColName = waitList.get(i).getModelKeyColName();
					//第一步，先删索引
					String deleteSql = "delete from \"DT_SEARCH\" t where t.model_table_name = '"+modelTableName+"'";
					List<String> deleteSqlList = new LinkedList<>();
					deleteSqlList.add(deleteSql);
					pg.setUpdateSqlList(deleteSqlList);
					pg.updateDataList();
					Pageable pageable  = PageRequest.of(0, 100000);
					AggregateResult aggResult = dataProviderService.queryTableDataWithDsDataSourceId(pg.getValidDataSource(), modelKeyColName, modelTableName, pageable);
					if (aggResult.getData()!=null) {
						continue;
					}
					pg.setTableName("DT_SEARCH");
					List<Object> data = new LinkedList<>();
					data = Arrays.asList(aggResult.getData());
					pg.setDataList(data);
					pg.insertDataList();
				}catch (Exception e){
					logger.info(e.getMessage());
				}
			}
		}

	}
}
