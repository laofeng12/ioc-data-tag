package com.openjava.admin.job.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
@ApiModel("待办任务")
@Entity
@Table(name = "SYS_WORKFLOW_TASK")
public class SysWorkflowTask extends BasicApiResponse implements ApiResponse {
    @ApiModelProperty("任务id")
    private Long f_id;//任务id
    @ApiModelProperty("任务名称")
    private String f_taskname;//任务名称
    @ApiModelProperty("任务类型")
    private Short f_tasktype;//任务类型
    @ApiModelProperty("当前任务是否执行完了 0 未处理 1 已处理 2 关闭")
    private Short f_isfinished;//当前任务是否执行完了 0 未处理 1 已处理 2 关闭
    @ApiModelProperty("审核者主键")
    private String f_auditorid;//审核者主键
    @ApiModelProperty("审核者名称")
    private String f_auditorname;//审核者名称
    @ApiModelProperty("公司主键")
    private String f_orgid;//公司主键
    @ApiModelProperty("部门主键")
    private String f_deptid;//部门主键
    @ApiModelProperty("创建时间")
    private Date f_createdate;//创建时间
    @ApiModelProperty("创建用户主键")
    private String f_createuserid;//创建用户主键
    @ApiModelProperty("创建用户名称")
    private String f_createusername;//创建用户名称
    @ApiModelProperty("修改时间")
    private Date f_modifydate;//修改时间
    @ApiModelProperty("完成人员主键")
    private String f_modifyuserid;//完成人员主键
    @ApiModelProperty("完成人员名称")
    private String f_modifyusername;//完成人员名称
    @ApiModelProperty("任务标题")
    private String f_tasktitle;//任务标题
    @ApiModelProperty("任务详情")
    private String f_taskdetails;//任务详情
    @ApiModelProperty("删除标识(0正常1删除)")
    private Short f_isdelete;//删除标识(0正常1删除)
    @ApiModelProperty("状态")
    private Short f_status;//状态

    @Id
    @Column(name = "F_ID")
    public Long getF_id() {
        return f_id;
    }
    public void setF_id(Long f_id) {
        this.f_id = f_id;
    }

    @Column(name = "F_TASKNAME")
    public String getF_taskname() {
        return f_taskname;
    }
    public void setF_taskname(String f_taskname) {
        this.f_taskname = f_taskname;
    }

    @Column(name = "F_TASKTYPE")
    public Short getF_tasktype() {
        return f_tasktype;
    }
    public void setF_tasktype(Short f_tasktype) {
        this.f_tasktype = f_tasktype;
    }

    @Column(name = "F_ISFINISHED")
    public Short getF_isfinished() {
        return f_isfinished;
    }
    public void setF_isfinished(Short f_isfinished) {
        this.f_isfinished = f_isfinished;
    }

    @Column(name = "F_AUDITORID")
    public String getF_auditorid() {
        return f_auditorid;
    }
    public void setF_auditorid(String f_auditorid) {
        this.f_auditorid = f_auditorid;
    }

    @Column(name = "F_AUDITORNAME")
    public String getF_auditorname() {
        return f_auditorname;
    }
    public void setF_auditorname(String f_auditorname) {
        this.f_auditorname = f_auditorname;
    }

    @Column(name = "F_ORGID")
    public String getF_orgid() {
        return f_orgid;
    }
    public void setF_orgid(String f_orgid) {
        this.f_orgid = f_orgid;
    }

    @Column(name = "F_DEPTID")
    public String getF_deptid() {
        return f_deptid;
    }
    public void setF_deptid(String f_deptid) {
        this.f_deptid = f_deptid;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "F_CREATEDATE")
    public Date getF_createdate() {
        return f_createdate;
    }
    public void setF_createdate(Date f_createdate) {
        this.f_createdate = f_createdate;
    }

    @Column(name = "F_CREATEUSERID")
    public String getF_createuserid() {
        return f_createuserid;
    }
    public void setF_createuserid(String f_createuserid) {
        this.f_createuserid = f_createuserid;
    }

    @Column(name = "F_CREATEUSERNAME")
    public String getF_createusername() {
        return f_createusername;
    }
    public void setF_createusername(String f_createusername) {
        this.f_createusername = f_createusername;
    }

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "F_MODIFYDATE")
    public Date getF_modifydate() {
        return f_modifydate;
    }
    public void setF_modifydate(Date f_modifydate) {
        this.f_modifydate = f_modifydate;
    }

    @Column(name = "F_MODIFYUSERID")
    public String getF_modifyuserid() {
        return f_modifyuserid;
    }
    public void setF_modifyuserid(String f_modifyuserid) {
        this.f_modifyuserid = f_modifyuserid;
    }

    @Column(name = "F_MODIFYUSERNAME")
    public String getF_modifyusername() {
        return f_modifyusername;
    }
    public void setF_modifyusername(String f_modifyusername) {
        this.f_modifyusername = f_modifyusername;
    }

    @Column(name = "F_TASKTITLE")
    public String getF_tasktitle() {
        return f_tasktitle;
    }
    public void setF_tasktitle(String f_tasktitle) {
        this.f_tasktitle = f_tasktitle;
    }

    @Column(name = "F_TASKDETAILS")
    public String getF_taskdetails() {
        return f_taskdetails;
    }
    public void setF_taskdetails(String f_taskdetails) {
        this.f_taskdetails = f_taskdetails;
    }

    @Column(name = "F_ISDELETE")
    public Short getF_isdelete() {
        return f_isdelete;
    }
    public void setF_isdelete(Short f_isdelete) {
        this.f_isdelete = f_isdelete;
    }

    @Column(name = "F_STATUS")
    public Short getF_status() {
        return f_status;
    }
    public void setF_status(Short f_status) {
        this.f_status = f_status;
    }

}
