package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataSetRspDataDTO {
    private List<Object> columnList;
    private List<List<Object>> data;
    private long page;
    private long size;
    private long total;
    private long totalPage;
}
