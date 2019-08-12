package com.openjava.datatag.userprofile.service;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.component.PostgreSqlConfig;
import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.userprofile.dto.PortrayalDetailDTO;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
@Data
public class PortrayalServiceImpl implements PortrayalService {
    Logger logger = LogManager.getLogger(getClass());
    @Resource
    private DtTaggingModelService dtTaggingModelService;
    @Resource
    private PostgreSqlConfig postgreSqlConfig;

    public List<PortrayalDetailDTO> searchPortrayal(String id, int type)throws Exception{
        MppPgExecuteUtil mppPgExecuteUtil = new MppPgExecuteUtil();
        mppPgExecuteUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
        String alias = "t";//别名
        Map<String, String> tableNameForQuery = new LinkedHashMap<>(1);//表名
        tableNameForQuery.put(Constants.DT_SEARCH_TABLE_NAME,alias);
        mppPgExecuteUtil.setTableNameForQuery(tableNameForQuery);
        mppPgExecuteUtil.setCondition(" where "+alias+".\""+Constants.DT_SEARCH_MODEL_PKEY+"\"='"+id+"'");
        Map<String, String> columnMapForQuery  = new LinkedHashMap<>();//列名
        columnMapForQuery.put(Constants.DT_SEARCH_MODEL_TABLE_NAME,alias);
        columnMapForQuery.put(Constants.DT_SEARCH_MODEL_PKEY,alias);
        mppPgExecuteUtil.setColumnMapForQuery(columnMapForQuery);
        String[][] data = mppPgExecuteUtil.getData();//第一个为表头
        List<PortrayalDetailDTO> result = new LinkedList<>();
        for (int i = 1; i < data.length; i++) {
            result.add(portrayal(data[i][0],1,data[i][1]));
        }
        return result;
    }

    public PortrayalDetailDTO portrayal(String tableName, int type, String pKey)throws Exception{
        String [] prefixAndId = tableName.split(Constants.DT_TABLE_PREFIX);
        DtTaggingModel taggingModel = dtTaggingModelService.get(Long.valueOf(prefixAndId[1]));
        String alias = "t";//别名
        MppPgExecuteUtil mppPgExecuteUtil = new MppPgExecuteUtil();
        mppPgExecuteUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
        Map<String, String> tableNameForQuery = new LinkedHashMap<>(1);
        tableNameForQuery.put(tableName,alias);
        mppPgExecuteUtil.setTableNameForQuery(tableNameForQuery);
        mppPgExecuteUtil.setCondition(" where "+alias+".\""+taggingModel.getPkey()+"\"="+pKey);
        String[][] data = mppPgExecuteUtil.getData();//第一个为表头
        List<Object> result = getResult(data,type);
        List<PortrayalDetailDTO> list =  initResultDate(result,taggingModel,pKey);
        PortrayalDetailDTO portrayal = new PortrayalDetailDTO();
        if (!CollectionUtils.isEmpty(list)) {
            portrayal = list.get(0);
        }
        return portrayal;
    }

    private List<Object> getResult(String[][] data,int type){
        List<List<Object>> dataList = new LinkedList<>();//数据
        for (int i = 1; i < data.length; i++) {
            List<Object> list = new LinkedList<>();
            for (int j = 0; j < data[i].length; j++) {
                list.add(data[i][j]);
            }
            dataList.add(list);
        }
        List<DtSetCol> cols = new LinkedList<>();
        for (int i = 0; i < data[0].length; i++) {
            DtSetCol col = new DtSetCol();
            col.setSourceCol(data[0][i]);
            col.setShowCol(data[0][i]);
            cols.add(col);
        }
        List<Object> result = dtTaggingModelService.rebuiltData(cols,dataList,data[0],type);//重组数据
        return result;
    }

    private List<PortrayalDetailDTO> initResultDate(List<Object> result, DtTaggingModel taggingModel, String pKey){
        List<PortrayalDetailDTO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(result)) {
            return list;
        }
        List<String> property = new ArrayList<>();
        List<String> lists = new ArrayList<>();
        List<String> removeCol = new ArrayList<>();
        Map<String,Object> map = (Map<String, Object>) result.get(0);
        for(String key:map.keySet()){
            String value = map.get(key).toString();
            if (taggingModel.getPkey().equals(key)) {
                removeCol.add(key);
            }
            if (key.indexOf(Constants.DT_COL_PREFIX)!=-1){
                lists.add(value);
                removeCol.add(key);
                removeCol.add(key.split(Constants.DT_COL_PREFIX)[1]);
            }
        }
        for (Object ob:result) {
            Map<String,Object> dataMap = (Map<String, Object>) ob;
            PortrayalDetailDTO por = new PortrayalDetailDTO();
            for(String key:dataMap.keySet()){
                String value = dataMap.get(key).toString();
                if (!removeCol.contains(key)){
                    property.add(value);
                }
            }
            por.setId(pKey);
            por.setTitle(taggingModel.getModelName());
            por.setTableName(Constants.DT_TABLE_PREFIX + taggingModel.getTaggingModelId());
            por.setLists(lists);
            por.setProperty(property);
            list.add(por);
        }
        return list;
    }

    public static void main(String[] args) {
        String [] prefixAndId = "DT_name".split("DT_");
        System.out.println(prefixAndId[0]);
        System.out.println(prefixAndId[1]);
    }
}
