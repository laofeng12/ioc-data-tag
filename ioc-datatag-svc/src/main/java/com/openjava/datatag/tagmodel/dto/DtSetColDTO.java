package com.openjava.datatag.tagmodel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class DtSetColDTO {

    private Long colId;

    private Long taggingModelId;

    private String sourceCol;

    private String showCol;

    private Long createUser;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    private Long modifyUser;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date modifyTime;

    private Long isDeleted;

    private Long isHandle;

    private Long isMarking;

    private Long isSource;

}
