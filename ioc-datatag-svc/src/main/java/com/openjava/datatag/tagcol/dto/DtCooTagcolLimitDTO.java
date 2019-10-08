package com.openjava.datatag.tagcol.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Max;

/**
 * 作者：hyq
 * 日期：2019/7/22
 * 描述：ioc-datatag-svc
 * 版本：1.0
 */
@Data
public class DtCooTagcolLimitDTO {
    @ApiModelProperty("协作打标列限制表ID")
    private Long id;

    @ApiModelProperty("协作表主键")
    private Long cooId;

    @ApiModelProperty("协作可打标字段名")
    private String tagColName;

    @ApiModelProperty("规定使用的标签组")
    private Long useTagGroup;
    @ApiModelProperty("模型的字段Id")
    private Long tagColId;
    @ApiModelProperty("是否删除")
    private Long isDelete;
}
