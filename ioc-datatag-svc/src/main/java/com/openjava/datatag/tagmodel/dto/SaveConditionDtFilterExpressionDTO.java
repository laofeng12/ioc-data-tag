package com.openjava.datatag.tagmodel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 *
 */
@Data
public class SaveConditionDtFilterExpressionDTO {


    @ApiModelProperty("符号")
    @Length(min=0, max=32)
    private String symbol;//符号

    @ApiModelProperty("条件值")
    @Length(min=0, max=4000)
    private String theValues;//条件值

    @ApiModelProperty("值类型")
    @Length(min=0, max=32)
    private String valuesType;//值类型

    @ApiModelProperty("是否连接符")
    @Max(1L)
    private Long isConnectSymbol;//是否连接符

    @ApiModelProperty("排序")
    @Max(9999L)
    private Integer sort;//排序
}
