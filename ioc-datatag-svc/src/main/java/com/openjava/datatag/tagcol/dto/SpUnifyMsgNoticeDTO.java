package com.openjava.datatag.tagcol.dto;

import lombok.Data;

/**
 *
 */
@Data
public class SpUnifyMsgNoticeDTO {
    private String businessId;//关联系统业务id，如模型id

    private String content="您有一个标签模型运行失败了";//消息内容

    private Long msgType = 1L;//消息类型（1：告警消息；2：订阅消息）暂时写死为1

    private String path;//消息详情跳转地址

    private Long serviceType = 3L;//消息所属组件类型（1：碰撞；2：挖掘；3：标签；4：API服务；5：公共服务）

    private String title="标签模型运行失败";//消息标题

    private String userAccount;//消息接收的用户账号

    private String userId;//消息接收的用户id

    private Long jobStatus=1L;//1：待处理，2：已完成，3：已作废  默认待处理
}
