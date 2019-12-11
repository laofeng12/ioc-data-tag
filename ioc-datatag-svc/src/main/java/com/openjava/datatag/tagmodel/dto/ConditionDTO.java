package com.openjava.datatag.tagmodel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class ConditionDTO {
    @ApiModelProperty("符号")
    private String conditionCode;//符号
    @ApiModelProperty("条件值")
    private List<String> conditionValue;//条件值
    @ApiModelProperty("值类型")
    private Long valueType;//值类型
    @ApiModelProperty("是否连接符号")
    private String isConnectCode;//是否连接符号
    @ApiModelProperty("排序")
    private Long sort;//排序
}
