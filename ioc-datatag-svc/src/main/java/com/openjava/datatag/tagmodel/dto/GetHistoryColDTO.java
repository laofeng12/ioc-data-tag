package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Data
public class GetHistoryColDTO {
    @NotNull
    @ApiModelProperty("字段表主键")
    private Long colId;//字段表主键
    @ApiModelProperty("标签组数据")
    private List<DtTagGroup> tagGroups = new ArrayList<>();//标签组数据
    @ApiModelProperty("被选的标签组")
    private DtTagGroup selectTagGroup;//被选的标签组
    @ApiModelProperty("标签层数据")
    private List<DtTag> tags = new ArrayList<>();//标签层数据
    @ApiModelProperty("被选标签层")
    private DtTag selectTags;//被选标签层
    @ApiModelProperty("被选标")
    private DtTag selectTag;//被选标
    @ApiModelProperty("打标规制数据")
    private List<DtTagConditionDTO> condtion = new ArrayList<>();//打标规制数据
}
