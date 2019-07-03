package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.Date;
@Data
public class DtSetColDTO {

    private Long colId;

    private Long taggingModelId;

    private String sourceCol;

    private String showCol;

    private Long createUser;

    private Date createTime;

    private Long modifyUser;

    private Date modifyTime;

    private Long isDeleted;

    private Long isHandle;

    private Long isMarking;

    private Long isSource;

}
