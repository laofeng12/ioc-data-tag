package com.openjava.datatag.utils.jdbc.dataprovider;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class DsDataSource {
    @NotNull(message = "不能为空")
    @ApiModelProperty("数据库类型(0:Oracle;1:MySql高版本;2;Mysql低版本;3:PostgreSql;4:hive)")
    private Long dbType;

    @NotNull(message = "不能为空")
    @ApiModelProperty("参数配置")
    private String configJson;

}
