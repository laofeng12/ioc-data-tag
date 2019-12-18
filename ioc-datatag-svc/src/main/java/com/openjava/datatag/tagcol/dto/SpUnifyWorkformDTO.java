package com.openjava.datatag.tagcol.dto;

import lombok.Data;

/**
 * 工单
 */
@Data
public class SpUnifyWorkformDTO {
    private String businessId;//关联系统业务id，如模型id

    private String content="标签模型需要协助打标";//工单内容 暂时写死

    private Long formType = 1L;//工单类型（1：协助打标；2：..）暂时写死为1

    private String jobPath;//工单跳转地址

    private String orgid;//工单指派的机构id

    private String roleid;//工单指派的部门id

    private String serviceName = "数据标签与画像";//模块名称，如数据标签与画像  暂时写死

    private String title = "请协助打标";//工单标题 暂时写死

    private String userAccount;//工单指派的用户账号

    private String userId;//工单指派的   用户id
    private Long jobStatus=1L;//1：待处理，2：已完成，3：已作废  默认待处理
}
