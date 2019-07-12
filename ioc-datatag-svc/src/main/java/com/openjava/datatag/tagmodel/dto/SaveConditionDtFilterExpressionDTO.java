package com.openjava.datatag.tagmodel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
@Data
public class SaveConditionDtFilterExpressionDTO {


    @ApiModelProperty("符号")
    @Length(min=0, max=32)
    private String symbol;

    @ApiModelProperty("条件值")
    @Length(min=0, max=4000)
    private String theValues;

    @ApiModelProperty("值类型")
    @Length(min=0, max=32)
    private String valuesType;

    @ApiModelProperty("是否连接符")
    @Max(1L)
    private Long isConnectSymbol;

    @ApiModelProperty("排序")
    @Max(9999L)
    private Integer sort;
}
