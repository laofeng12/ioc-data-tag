package com.openjava.datatag.demo.dto;

import lombok.Data;

@Data
public class BaseResp {
    private Long requestId;
    private Long code;
    private String message;
}
