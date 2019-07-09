package com.openjava.datatag.tagmodel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ConditionDTO {
    @ApiModelProperty("符号")
    private String conditionCode;
    @ApiModelProperty("条件值")
    private List<String> conditionValue;
    @ApiModelProperty("值类型")
    private Long valueType;
    @ApiModelProperty("是否连接符号")
    private String isConnectCode;
    @ApiModelProperty("排序")
    private Long sort;
}
