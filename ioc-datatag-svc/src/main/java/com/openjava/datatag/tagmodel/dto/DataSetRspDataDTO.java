package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class DataSetRspDataDTO {
    private List<Object> columnList;//
    private List<List<Object>> data;//
    private long page;//页面
    private long size;//每页大小
    private long total;//总记录数
    private long totalPage;//总页数
}
