package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel("标签模型")
@Data
public class SelectColDTO {
    @ApiModelProperty("标签模型主键")
    @Length(min=1, max=18)
    private Long taggingModelId;
    @Length(min=1, max=18)
    @ApiModelProperty("打标目标表id")
    private String dataSetId;
    @Length(min=1, max=200)
    @ApiModelProperty("打标源表名称")
    private String dataSetName;
    @NotNull
    @ApiModelProperty("选择的字段")
    private List<DtSetCol> colList = new ArrayList<>();
}
