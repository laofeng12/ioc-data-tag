package com.openjava.datatag.tagmanage.dto;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Max;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Persistable;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体
 * @author lch
 *
 */
@ApiModel("DT_TAG_DTO")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
public class DtTagDTO implements Serializable{

    @ApiModelProperty("标签编号")
    @Id
    @Column(name = "ID")
    private Long id;//标签编号"

    @ApiModelProperty("标签组编号")
    @Max(9223372036854775806L)
    @Column(name = "TAGS_ID")
    private Long tagsId;//标签组编号

    @ApiModelProperty("父标签编号")
    @Max(9223372036854775806L)
    @Column(name = "PREA_TAG_ID")
    private Long preaTagId;//父标签编号

    @ApiModelProperty("标签名")
    @Length(min=0, max=32)
    @Column(name = "TAG_NAME")
    private String tagName;//标签名

    @ApiModelProperty("标签说明")
    @Length(min=0, max=2000)
    @Column(name = "SYNOPSIS")
    private String synopsis;//标签说明

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Column(name = "CREATE_TIME")
    private Date createTime;//创建时间

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;//修改时间

//    @ApiModelProperty("删除标记")
//    @Max(1L)
//    @Column(name = "IS_DELETED")
//    private Long isDeleted;

    @ApiModelProperty("层级")
    @Max(9L)
    @Column(name = "LVL")
    private Long lvl;//层级


}