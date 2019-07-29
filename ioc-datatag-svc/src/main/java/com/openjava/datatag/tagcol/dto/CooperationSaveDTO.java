package com.openjava.datatag.tagcol.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：hyq
 * 日期：2019/7/22
 * 描述：ioc-datatag-svc
 * 版本：1.0
 */
@Data
public class CooperationSaveDTO {
    @ApiModelProperty("协作表主键:非必填,当为空或0时为添加操作")
    private Long id;
    @ApiModelProperty("协作用户")
    private Long cooUser;
    @ApiModelProperty("标签模型主键")
    private Long taggmId;


}
