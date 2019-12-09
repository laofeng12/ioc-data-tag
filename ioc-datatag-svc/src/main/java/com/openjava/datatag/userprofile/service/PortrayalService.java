package com.openjava.datatag.userprofile.service;


import com.openjava.datatag.userprofile.dto.PortrayalDetailDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 画像查询模块业务层接口
 */
public interface PortrayalService {
    /**
     * 获取画像结果
     * type写死为1（type为数据格式的类型，适配前端的要求定义了多种）
     */
    PortrayalDetailDTO portrayal(String tableName, int type, String pKey,boolean isLog)throws Exception;
    /**
     * 画像查询
     * type写死为1
     */
    List<PortrayalDetailDTO> searchPortrayal(String id, int type)throws Exception;

    /**
     * 清除画像结果
     */
    void clearPortrayal(String tableName);

}
