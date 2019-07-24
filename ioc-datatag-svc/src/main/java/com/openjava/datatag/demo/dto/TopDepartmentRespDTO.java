package com.openjava.datatag.demo.dto;

import lombok.Data;

@Data
public class TopDepartmentRespDTO extends BaseResp{
    private String orgid;
    private String orgname;
    private String orgdesc;
    private String orgsupid;
    private String path;
    private String orgtype;
    private String createtime;
    private String updatetime;
    private String orgpathname;
    private String deptcode;
    private String orgcode;

}
