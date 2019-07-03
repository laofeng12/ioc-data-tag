package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DtTaggingModelDTO {
    private Long taggingModelId;

    private String modelName;

    private String modelDesc;

    private String taggingTable;

    private String taggingModelTable;

    private String pKey;

    private Long createUser;

    private Date createTime;

    private Long modifyUser;

    private Date modifyTime;

    private Date startTime;

    private String cycle;

    private Long runState;

    private Long isDeleted;


    private Boolean isNew;

}
