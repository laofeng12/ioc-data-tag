package com.openjava.datatag.tagcol.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import sun.java2d.pipe.SolidTextRenderer;

import javax.persistence.Column;

@Data
public class ColDTO {

    @ApiModelProperty("打标字段")
    private String tagColName;//打标字段

    @Column(name = "USE_TAG_GROUP")
    private String colTye;//字段类型

    @ApiModelProperty("选用标签组")
    private String colGroupName;//选用标签组
}
