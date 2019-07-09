package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.tagmodel.domain.DtFilterExpression;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class DtTagConditionDTO {
    @ApiModelProperty("条件设置主键")
    private Long tagConditionId;
    @ApiModelProperty("字段表主键")
    private Long colId;
    @ApiModelProperty("标签id")
    private Long tagId;
    @ApiModelProperty("标签名")
    private String tagName;
    @ApiModelProperty("是否手动设置")
    private String isHandle;
    @ApiModelProperty("条件设置")
    private List<DtFilterExpression> conditionSetting = new ArrayList<>();
}
