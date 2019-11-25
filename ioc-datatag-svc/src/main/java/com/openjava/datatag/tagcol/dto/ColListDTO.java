package com.openjava.datatag.tagcol.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;

@Data
public class ColListDTO {

    @ApiModelProperty("模型名字")
    private String modelName;//协作模型名称

    @ApiModelProperty("字段列表")
    List<ColDTO> colList = new ArrayList<>();//打标字段列表
}
