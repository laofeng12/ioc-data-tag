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

}
