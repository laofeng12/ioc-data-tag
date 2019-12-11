package com.openjava.datatag.tagmodel.dto;

import lombok.Data;

import java.util.List;

/**
 *
 */
@Data
public class DataSetReqDTO {
    private Object[] columnList;//
    private Object[] columnIdList;//
    private int page;//页码
    private int size;//每页大小
}
