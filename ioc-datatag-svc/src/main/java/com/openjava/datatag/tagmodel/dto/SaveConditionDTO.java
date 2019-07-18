package com.openjava.datatag.tagmodel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SaveConditionDTO extends BaseIpDTO{
    @NotNull
    @ApiModelProperty("字段表主键")
    private Long colId;
    @ApiModelProperty("打标规制数据")
    private List<DtTagConditionDTO> condtion = new ArrayList<>();
}
