package com.openjava.datatag.tagmodel.dto;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class DtTaggingModelDTO {
    private Long taggingModelId;

    private String modelName;

    private String modelDesc;

    private Long dataSetId;

    private String dataSetName;

    private String dataTableName;

    private String pkey;

    private Long createUser;

    private Date createTime;

    private Long modifyUser;

    private Date modifyTime;

    private Date startTime;

    private String cycle;

    private Long runState;

    private Long isDeleted;


    private Boolean isNew;

    private List<DtSetCol> colList = new ArrayList<>();
}
