package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DtTagConditionDTO {
    private Long tagConditionId;

    private Long colId;

    private Long tagId;

    private String filterExpression;
    private String sourceCol;

    private String showCol;

    private Long createUser;

    private Date createTime;

    private Long modifyUser;

    private Date modifyTime;

    private Long isDeleted;
}
