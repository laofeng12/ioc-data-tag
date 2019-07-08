package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmodel.domain.DtTagCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetHistoryColDTO {
    @ApiModelProperty("标签组数据")
    private List<DtTagGroup> tagGroups = new ArrayList<>();
    @ApiModelProperty("被选的标签组")
    private DtTagGroup selectTagGroup;
    @ApiModelProperty("标签层数据")
    private List<DtTag> tags = new ArrayList<>();
    @ApiModelProperty("被选标签层")
    private DtTag selectTags;
    @ApiModelProperty("打标规制数据")
    private List<DtTagCondition> condtion = new ArrayList<>();
}
