package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

@Data
public class DataSetReqDTO {
    private Object columnList;
    private int page;
    private int size;
}
