package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataSetRspDataDTO {
    private List<Object> columnList;
    private List<List<Object>> data;
}
