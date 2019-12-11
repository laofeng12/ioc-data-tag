package com.openjava.datatag.tagmodel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openjava.datatag.tagmanage.dto.BaseMessageDTO;
import com.openjava.datatag.tagmodel.domain.DtSetCol;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Data
public class DtTaggingModelDTO{
    private Long taggingModelId;//

    private String modelName;//

    private String modelDesc;//

    private Long resourceId;//

    private String resourceName;//

    private Long resourceType;//

    private String pkey;//

    private Long createUser;//

    private String createUserName;//

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;//

    private Long modifyUser;//

    private String modifyUserName;//

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;//

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;//

    private String cycle;//

    private Long runState;//

    private Long isDeleted;//

    private String runResult;//

   private Boolean isNew;//

    private List<DtSetCol> colList = new ArrayList<>();//
}
