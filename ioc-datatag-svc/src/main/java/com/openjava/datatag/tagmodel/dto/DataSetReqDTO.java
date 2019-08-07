package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataSetReqDTO {
    private Object[] columnList;
    private int page;
    private int size;
}
