package com.openjava.datatag.tagcol.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：hyq
 * 日期：2019/7/22
 * 描述：ioc-datatag-svc
 * 版本：1.0
 */
@Data
public class DtCooperationDTO {
    @ApiModelProperty("协作表主键")
    private Long id;
    @ApiModelProperty("创建用户名")
    private String createUserName;
    @ApiModelProperty("创建用户ID")
    private Long createUser;
    @ApiModelProperty("发起时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty("最后一次修改时间（非必填）")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date modifyTime;
    @ApiModelProperty("标签模型主键")
    private Long taggmId;
    @ApiModelProperty("协作用户Id")
    private Long cooUser;
    @ApiModelProperty("协作用户名称")
    private String cooUserName;
    @ApiModelProperty("协作成员限制的打标字段")
    private List<DtCooTagcolLimitDTO> cooTagcolLimitList=new ArrayList<>();


}
