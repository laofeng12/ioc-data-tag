package com.openjava.datatag.tagcol.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：hyq
 * 日期：2019/7/22
 * 描述：ioc-datatag-svc
 * 版本：1.0
 */
@Data
public class DtCooperationListDTO {
    @ApiModelProperty("标签模型主键Id")
    private Long taggmId;
    @ApiModelProperty("协作用户及对应协作字段记录集")
    private List<CooperationDTO> cooperaList=new ArrayList<>();


}
