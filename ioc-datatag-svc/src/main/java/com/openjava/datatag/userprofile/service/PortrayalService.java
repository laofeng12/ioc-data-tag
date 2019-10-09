package com.openjava.datatag.userprofile.service;


import com.openjava.datatag.userprofile.dto.PortrayalDetailDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PortrayalService {
    /**
     * 获取画像结果
     * type写死为1
     */
    PortrayalDetailDTO portrayal(String tableName, int type, String pKey)throws Exception;
    /**
     * 画像查询
     * type写死为1
     */
    List<PortrayalDetailDTO> searchPortrayal(String id, int type)throws Exception;

    /**
     * 清除画像结果
     */
    void clearPortrayal(String tableName);

    /**
     * 下载画像结果
     * @param number 下载数量为空默认全部
     * @param taggingModelId 模型id
     */
    void dowloadRunResult(Long number,Long taggingModelId);

}
