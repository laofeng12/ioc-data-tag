package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataSetReqDTO {
    private Object[] columnList;
    private Object[] columnIdList;
    private int page;
    private int size;
}
