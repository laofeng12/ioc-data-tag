package com.openjava.datatag.tagmanage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import java.util.Date;

@Data
public class DtTagGroupSaveDTO {
    @ApiModelProperty("标签组编号")
    @Id
    @Column(name = "ID")
    private Long id;


    @ApiModelProperty(value = "标签组名",required = true)
    @Length(min=0, max=50)
    @Column(name = "TAGS_NAME")
    private String tagsName;

    @ApiModelProperty("是否共享")
    @Max(1)
    @Column(name = "IS_SHARE")
    private Long isShare;

    @ApiModelProperty("标签组简介")
    @Length(min=0, max=2000)
    @Column(name = "SYNOPSIS")
    private String synopsis;

    @ApiModelProperty("是否新增")
    @Transient
    private Boolean isNew;

//	@Transient
//	@JsonIgnore
//	@Override
//	public Long getId() {
//		return this.id;
//	}
}
