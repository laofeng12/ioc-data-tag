package com.openjava.datatag.userprofile.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.component.PostgreSqlConfig;
import com.openjava.datatag.dowload.domain.DownloadQueue;
import com.openjava.datatag.dowload.service.DownloadQueueService;
import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.userprofile.dto.PortrayalDetailDTO;
import com.openjava.datatag.utils.export.ExportUtil;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
/**
 * 画像查询模块业务层接口实现类
 */
public class PortrayalServiceImpl implements PortrayalService {
    Logger logger = LogManager.getLogger(getClass());//日志
    @Resource
    private DtTaggingModelService dtTaggingModelService;//标签模型业务层接口
    @Resource
    private PostgreSqlConfig postgreSqlConfig;//pgSQL配置
    @Resource
    private DownloadQueueService downloadQueueService;//下载列表业务层接口
    @Resource
    private AuditComponet auditComponet;//审计日志组件

    /**
     * 画像查询
     * type写死为1（type为数据格式的类型，适配前端的要求定义了多种）
     */
    public List<PortrayalDetailDTO> searchPortrayal(String id, int type)throws Exception{
        MppPgExecuteUtil mppPgExecuteUtil = new MppPgExecuteUtil();//创建pgSQL工具
        mppPgExecuteUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
        String alias = "t";//别名
        Map<String, String> tableNameForQuery = new LinkedHashMap<>(1);//表名
        tableNameForQuery.put(Constants.DT_SEARCH_TABLE_NAME,alias);//设置别名
        mppPgExecuteUtil.setTableNameForQuery(tableNameForQuery);//设置表名
        mppPgExecuteUtil.setCondition(" where "+alias+".\""+Constants.DT_SEARCH_MODEL_PKEY+"\"='"+id+"' ORDER BY \"create_time\" desc nulls last");
        Map<String, String> columnMapForQuery  = new LinkedHashMap<>();//列名
        columnMapForQuery.put(Constants.DT_SEARCH_MODEL_TABLE_NAME,alias);
        columnMapForQuery.put(Constants.DT_SEARCH_MODEL_PKEY,alias);
        mppPgExecuteUtil.setColumnMapForQuery(columnMapForQuery);//设置查询语句
        String[][] data = mppPgExecuteUtil.getData();//第一个为表头
        List<PortrayalDetailDTO> result = new LinkedList<>();//创建画像结果列表
        for (int i = 1; i < data.length; i++) {
            result.add(portrayal(data[i][0],1,data[i][1],false));//遍历添加画像查询结果
        }
        AuditLogVO vo = new AuditLogVO();//创建审计日志
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("画像查询");//必传
        vo.setFunctionLev1("查询");//必传
        vo.setFunctionLev2("查询搜索");//必传
        vo.setRecordId(id);//设置查询记录
        auditComponet.saveAuditLog(vo);//保存审计日志
        return result;
    }

    /**
     * 获取画像结果
     * type写死为1（type为数据格式的类型，适配前端的要求定义了多种）
     */
    public PortrayalDetailDTO portrayal(String tableName, int type, String pKey,boolean isLog)throws Exception{
        String [] prefixAndId = tableName.split(Constants.DT_TABLE_PREFIX);//切割获取模型id，pg的表名为：DT_模型id
        DtTaggingModel taggingModel = dtTaggingModelService.get(Long.valueOf(prefixAndId[1]));//获取模型
        String alias = "t";//别名
        MppPgExecuteUtil mppPgExecuteUtil = new MppPgExecuteUtil();//pgSQL工具
        mppPgExecuteUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
        Map<String, String> tableNameForQuery = new LinkedHashMap<>(1);
        tableNameForQuery.put(tableName,alias);//设置表名和表别名
        mppPgExecuteUtil.setTableNameForQuery(tableNameForQuery);//设置查询语句
        mppPgExecuteUtil.setCondition(" where "+alias+".\""+taggingModel.getPkey()+"\"='"+pKey+"'");
        String[][] data = mppPgExecuteUtil.getData();//第一个为表头
        List<Object> result = getRebulitResult(data,type);//根据type获取相应的数据结构
        List<PortrayalDetailDTO> list =  initResultDate(result,taggingModel,pKey);//初始化数据
        PortrayalDetailDTO portrayal = new PortrayalDetailDTO();//新建画像结果对象
        if (!CollectionUtils.isEmpty(list)) {
            portrayal = list.get(0);//获取第一个，
        }
        if (isLog) {
            AuditLogVO vo = new AuditLogVO();
            vo.setType(2L);//数据查询
            vo.setOperationService("标签与画像");//必传
            vo.setOperationModule("画像查询");//必传
            vo.setFunctionLev1("查询");//必传
            vo.setFunctionLev2("查询详情");//必传
            vo.setRecordId(taggingModel.getTaggingModelId()+"");
            auditComponet.saveAuditLog(vo);
        }
        //日志记录
        return portrayal;
    }

    private List<Object> getRebulitResult(String[][] data,int type){
        List<List<Object>> dataList = new LinkedList<>();//数据
        for (int i = 1; i < data.length; i++) {
            List<Object> list = new LinkedList<>();
            for (int j = 0; j < data[i].length; j++) {
                list.add(data[i][j]);//遍历设置
            }
            dataList.add(list);//添加结果
        }
        List<DtSetCol> cols = new LinkedList<>();//字段List
        for (int i = 0; i < data[0].length; i++) {
            DtSetCol col = new DtSetCol();//新建字段表对象
            col.setSourceCol(data[0][i]);//设置源字段名
            col.setShowCol(data[0][i]);//设置显示字段名
            cols.add(col);
        }
        List<Object> result = dtTaggingModelService.rebuiltData(cols,dataList,data[0],type);//重组数据
        return result;
    }

    /**
     * 处理成前端想要的数据结构
     * @param result
     * @param taggingModel
     * @param pKey
     * @return
     */
    private List<PortrayalDetailDTO> initResultDate(List<Object> result, DtTaggingModel taggingModel, String pKey){
        List<PortrayalDetailDTO> list = new ArrayList<>();//新建画像结果列表
        if (CollectionUtils.isEmpty(result)) {
            return list;
        }
        List<String> removeCol = new ArrayList<>();//不需要展示的字段
        List<String> tagCol = new ArrayList<>();//标签属性字段
        List<String> propertyCol = new ArrayList<>();//基础属性字段
        Map<String,Object> map = (Map<String, Object>) result.get(0);//获取表头
        for(String key:map.keySet()){
            if (taggingModel.getPkey().equals(key)) {
                removeCol.add(key);//排除主键
            }else if (key.indexOf(Constants.DT_COL_PREFIX)!=-1){
                removeCol.add(key);//添加需要排除的非基础属性
                removeCol.add(key.split(Constants.DT_COL_PREFIX)[1]);//添加需要排除的非基础属性
                tagCol.add(key);//打标的字段
            }else{
                propertyCol.add(key);//基础字段
            }
        }
        for (Object ob:result) {
            Map<String,Object> dataMap = (Map<String, Object>) ob;//
            PortrayalDetailDTO por = new PortrayalDetailDTO();//新建画像结果对象
            List<String> property = new LinkedList<>();//基础属性
            Map<String,String> mapProperty = new LinkedHashMap<>();//带键值的基础属性
            List<String> lists =new LinkedList<>();//画像属性
            Map<String,String> mapLists= new LinkedHashMap<>();//带键值的画像属性
            for(String key:dataMap.keySet()){
                String value = dataMap.get(key).toString();//获取值
                if (!removeCol.contains(key)){
                    property.add(value);//添加基础属性
                    mapProperty.put(key,value);//添加带键值的基础属性
                }
                if (tagCol.contains(key)) {
                    if (StringUtils.isNotBlank(value)) {
                        lists.add(value);//添加画像属性
                        mapLists.put(key.split(Constants.DT_COL_PREFIX)[1],value);//添加带键值的画像属性
                    }
                }
            }
            por.setId(pKey);//设置主键
            por.setTitle(taggingModel.getModelName());//设置画像标题（模型名称）
            por.setTableName(Constants.DT_TABLE_PREFIX + taggingModel.getTaggingModelId());//设置表名
            por.setLists(lists);//设置画像属性
            por.setMapLists(mapLists);//设置带键值画像属性
            por.setProperty(property);//设置基础属性
            por.setMapProperty(mapProperty);//设置带键值的基础属性
            list.add(por);
        }
        return list;
    }
    /**
     * 清除画像结果
     */
    public void clearPortrayal(String tableName){
        MppPgExecuteUtil mppUtil = new MppPgExecuteUtil();//mpp工具
        mppUtil.initValidDataSource(postgreSqlConfig);//初始化数据库
        //第一步，先删中间表数据
        String deleteSql = "delete from \""+Constants.DT_SEARCH_TABLE_NAME+"\" t where t.model_table_name = '"+tableName+"'";
        List<String> deleteSqlList = new LinkedList<>();
        deleteSqlList.add(deleteSql);
        mppUtil.setUpdateSqlList(deleteSqlList);
        mppUtil.updateDataList();
        //第二步删除模型表
        mppUtil.setTableName(tableName);//表名
        mppUtil.dropTable();//删表
    }

}
