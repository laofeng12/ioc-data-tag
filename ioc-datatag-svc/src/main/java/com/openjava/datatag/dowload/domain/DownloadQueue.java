package com.openjava.datatag.dowload.domain;

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
 * @author zmk
 *
 */
@ApiModel("下载列表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DOWNLOAD_QUEUE")
public class DownloadQueue implements Persistable<Long>,Serializable {
	
	@ApiModelProperty("ID")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commonseq")
	@SequenceGenerator(name = "commonseq", sequenceName = "SEQ_COMMON_ID", allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
	@ApiModelProperty("业务类型(标签与画像、数据集、数据碰撞等)")
	@Length(min=0, max=32)
	@Column(name = "BTYPE")
	private String btype;
	
	@ApiModelProperty("业务ID")
	@Length(min=0, max=32)
	@Column(name = "BID")
	private String bid;
	
	@ApiModelProperty("下载数据量")
	@Max(999999999L)
	@Column(name = "DOWNLOAD_NUM")
	private Long downloadNum;
	
	@ApiModelProperty("文件大小")
	@Length(min=0, max=32)
	@Column(name = "FILE_SIZE")
	private String fileSize;
	
	@ApiModelProperty("进度")
	@Length(min=0, max=8)
	@Column(name = "SPEED_OF_PROGRESS")
	private String speedOfProgress;
	
	@ApiModelProperty("状态(下载中、下载失败)")
	@Max(99L)
	@Column(name = "STATE")
	private Long state;
	@ApiModelProperty("状态(下载中、下载失败、下载成功)名称")
	@Transient
	private String stateName;
	
	@ApiModelProperty("文件下载地址")
	@Length(min=0, max=128)
	@Column(name = "DOWNLOAD_URL")
	private String downloadUrl;
	
	@ApiModelProperty("创建时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_TIME")
	private Date createTime;
	
	@ApiModelProperty("创建用户")
	@Max(9223372036854775806L)
	@Column(name = "CREATE_USER")
	private Long createUser;
	
	@ApiModelProperty("业务名称")
	@Length(min=0, max=100)
	@Column(name = "BNAME")
	private String bname;
	
	
	@ApiModelProperty("是否新增")
	@JsonIgnore
	@Transient
    private Boolean isNew;
	
	@Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.id;
	}
    
    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
    	if(isNew != null) {
    		return isNew;
    	}
    	if(this.id != null) {
    		return false;
    	}
    	return true;
    }
    
}