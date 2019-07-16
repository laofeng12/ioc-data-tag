package com.openjava.datatag.tagmanage.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Date;


@ApiModel("共享标签组")
@Data
@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@Entity
public class DtShareTagGroup {

    @Id
    @ApiModelProperty("标签组编号")
    private Long tagsId;

    @ApiModelProperty("标签组名")
    private String tagsName;

    @ApiModelProperty("共享者编号")
    private Long shareUserId;

    @ApiModelProperty("共享者名")
    private String shareUserName;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    @ApiModelProperty("标签组简介")
    private String synopsis;

    @ApiModelProperty("使用热度")
    private Long popularity;

    @ApiModelProperty("OA-所在单位")
    private String level1;

    @ApiModelProperty("用户名")
    private String fullname;

    @ApiModelProperty("热度等级-从0~4")
    private String popularityLevel;



}
