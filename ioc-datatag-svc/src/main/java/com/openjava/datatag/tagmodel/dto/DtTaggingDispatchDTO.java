package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DtTaggingDispatchDTO {
    private Long id;
    private Date startTime;
    private String CYCLE;
}
