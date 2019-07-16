package com.openjava.datatag.utils.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 实体
 * @author 黄文鑫
 *
 */
@ApiModel("用户")
@Entity
@Table(name = "SYS_USER")
public class SysUser extends BasicApiResponse implements ApiResponse {
	
	@ApiModelProperty("用户id")
	private Long userid;//用户id
	@ApiModelProperty("名称")
	private String fullname;//名称
	@ApiModelProperty("帐号类型")
	private String accounttype;//帐号类型(SYS.AccountType)
	private String accounttypeName;
	@ApiModelProperty("登录账号")
	private String account;//登录账号
	private String password;//密码
	@ApiModelProperty("是否过期")
	private Short isexpired;//是否过期
	private String isexpiredName;
	@ApiModelProperty("是否锁定")
	private Short islock;//是否锁定
	private String islockName;
	@ApiModelProperty("创建时间")
	private Date createtime;//创建时间
	@ApiModelProperty("状态")
	private Short status;//状态
	private String statusName;
	@ApiModelProperty("邮箱")
	private String email;//邮箱
	@ApiModelProperty("手机号码")
	private String mobile;//手机号码
	@ApiModelProperty("电话")
	private String phone;//电话
	@ApiModelProperty("性别")
	private String sex;//性别
	@ApiModelProperty("头像")
	private String picture;//头像
	@ApiModelProperty("来源类型")
	private Short fromtype;//来源类型
	private String fromtypeName;
	@ApiModelProperty("短信验证码")
	private String scode;
	@ApiModelProperty("OA-单位组织机构代码")
	private String orgcode;
	@ApiModelProperty("OA-统一认证平台中机构编码")
	private String deptcode;
	@ApiModelProperty("OA-用户uid")
	private String oarelationid;
	@ApiModelProperty("OA-部门ID")
	private String deptid;
	@ApiModelProperty("OA-机构名")
	private String orgname;
	@ApiModelProperty("OA-所在单位科室")
	private String level0;
	@ApiModelProperty("OA-所在单位")
	private String level1;
	@ApiModelProperty("OA-最后更新时间")
	private Date updatetime;
	@ApiModelProperty("OA-组织机构ID")
	private String orgid;

	@Id
	@Column(name = "USERID")
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	

	@Column(name = "FULLNAME")
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	

	@Column(name = "ACCOUNTTYPE")
	public String getAccounttype() {
		return accounttype;
	}
	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}
	
	@Transient
	public String getAccounttypeName() {
		return accounttypeName;
	}
	public void setAccounttypeName(String accounttypeName) {
		this.accounttypeName = accounttypeName;
	}

	@Column(name = "ACCOUNT")
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

	@Column(name = "ISEXPIRED")
	public Short getIsexpired() {
		return isexpired;
	}
	public void setIsexpired(Short isexpired) {
		this.isexpired = isexpired;
	}
	
	@Transient
	public String getIsexpiredName() {
		return isexpiredName;
	}
	public void setIsexpiredName(String isexpiredName) {
		this.isexpiredName = isexpiredName;
	}

	@Column(name = "ISLOCK")
	public Short getIslock() {
		return islock;
	}
	public void setIslock(Short islock) {
		this.islock = islock;
	}
	
	@Transient
	public String getIslockName() {
		return islockName;
	}
	public void setIslockName(String islockName) {
		this.islockName = islockName;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	

	@Column(name = "STATUS")
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	
	@Transient
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	

	@Column(name = "MOBILE")
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	

	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	@Column(name = "SEX")
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	

	@Column(name = "PICTURE")
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	

	@Column(name = "FROMTYPE")
	public Short getFromtype() {
		return fromtype;
	}
	public void setFromtype(Short fromtype) {
		this.fromtype = fromtype;
	}
	
	@Transient
	public String getFromtypeName() {
		return fromtypeName;
	}
	public void setFromtypeName(String fromtypeName) {
		this.fromtypeName = fromtypeName;
	}

	@Transient
	public String getScode() {
		return scode;
	}
	public void setScode(String scode) {
		this.scode = scode;
	}

	@Column(name = "ORGCODE")
	public String getOrgcode() {
		return orgcode;
	}
	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	@Column(name = "DEPTCODE")
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	@Column(name = "OARELATIONID")
	public String getOarelationid() {
		return oarelationid;
	}
	public void setOarelationid(String oarelationid) {
		this.oarelationid = oarelationid;
	}

	@Column(name = "DEPTID")
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	@Column(name = "ORGNAME")
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	@Column(name = "LEVEL0")
	public String getLevel0() {
		return level0;
	}
	public void setLevel0(String level0) {
		this.level0 = level0;
	}

	@Column(name = "LEVEL1")
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	@Column(name = "UPDATETIME")
	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "ORGID")
	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

}