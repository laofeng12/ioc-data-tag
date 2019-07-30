package com.openjava.datatag.portrayal.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 作者：hyq
 * 日期：2019/7/29
 * 描述：ioc-datatag-svc
 */
@Data
public class PortrayalDetailDTO {
    @ApiModelProperty("主键Id")
    private String id;
    @ApiModelProperty("画像标题")
    private  String title;
    @ApiModelProperty("画像属性1")
    private Map<String,String> property;
    @ApiModelProperty("画像属性2")
    private List<String> lists;
}
