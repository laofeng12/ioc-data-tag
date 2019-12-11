package com.openjava.datatag.tagmodel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 */
@Data
public class CopyDtFilterExpressionDTO {
    @ApiModelProperty("规制表达式表ID")
    private Long filterExpressionID;//规制表达式表ID

    @ApiModelProperty("条件设置主键")
    private Long tagConditionId;//条件设置主键

    @ApiModelProperty("符号")
    private String symbol;//符号

    @ApiModelProperty("条件值")
    private String theValues;//条件值

    @ApiModelProperty("值类型")
    private String valuesType;//值类型

    @ApiModelProperty("是否连接符")
    private Long isConnectSymbol;//是否连接符

    @ApiModelProperty("排序")
    private Integer sort;//排序

}
