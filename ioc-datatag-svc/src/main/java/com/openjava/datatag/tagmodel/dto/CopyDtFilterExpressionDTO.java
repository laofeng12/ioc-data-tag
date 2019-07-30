package com.openjava.datatag.tagmodel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CopyDtFilterExpressionDTO {
    @ApiModelProperty("规制表达式表ID")
    private Long filterExpressionID;

    @ApiModelProperty("条件设置主键")
    private Long tagConditionId;

    @ApiModelProperty("符号")
    private String symbol;

    @ApiModelProperty("条件值")
    private String theValues;

    @ApiModelProperty("值类型")
    private String valuesType;

    @ApiModelProperty("是否连接符")
    private Long isConnectSymbol;

    @ApiModelProperty("排序")
    private Integer sort;

}
