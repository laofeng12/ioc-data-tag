package com.openjava.datatag.common;

/**
 * 常量定义
 */
public class Constants {
    //========公共状态============
    public static final String PUBLIC_YN = "public.YN";//共享数据字典
    public static final String PUBLIC_MODIFY_TYPE = "public.modify.type";//修改类型
    public static final Long PUBLIC_NO = 0l;//否
    public static final Long PUBLIC_YES = 1l;//是
    public static final Long PUBLIC_MODIFY_TYPE_DELETE = 1L;//删除
    public static final Long PUBLIC_MODIFY_TYPE_UPDATE = 2L;//修改

    //=========队列状态=========
    public static final Long STATUS_QUEUE_WAIT = 1L;//待执行
    public static final Long STATUS_QUEUE_RUNNING = 2L;//执行中
    public static final Long STATUS_QUEUE_SUCCESS = 3L;//执行成功
    public static final Long STATUS_QUEUE_FAIL = 4L;//执行失败
    public static final Long STATUS_QUEUE_CANCEL = 5L;//取消执行


    //=========服务&API共享======
    public static final String SYSCODE_SHARED_TYPE = "sp.shared.status";//共享数据字典
    public static final Long STATUS_SHARED_NO = 0L;//不共享
    public static final Long STATUS_SHARED_YES = 1L;//共享

    //=========服务发布状态======
    public static final String SYSCODE_RELEASE_STATUS = "sp.release.status";
    public static final Long STATUS_RELEASE_WAIT = 1L;//待审核
    public static final Long STATUS_RELEASE_YES = 2L;//审核通过
    public static final Long STATUS_RELEASE_NO = 3L;//审核不通过

    //=========与kong对接的状态=========
    public static final String SYSCODE_KONG_STATUS = "sp.kong.status";//共享数据字典
    public static final Long STATUS_KONG_CREATING = 1L;//创建中
    public static final Long STATUS_KONG_CREATED = 2L;//正常
    public static final Long STATUS_KONG_CREATE_FAIL = 3L;//创建失败
    public static final Long STATUS_KONG_DELETING = 4L;//删除中
    public static final Long STATUS_KONG_DELETE_FAIL = 5L;//删除失败
    public static final Long STATUS_KONG_UPDATING = 6L;//更新中
    public static final Long STATUS_KONG_UPDATE_FAIL = 7L;//更新失败

    //=========身份验证方式=========
    public static final String SYSCODE_AUTH_TYPE = "sp.auth.type";//共享数据字典
    public static final Long API_AUTH_TYPE_NONE = 0L;//无
    public static final Long API_AUTH_TYPE_JWT = 1L;//jwt

    //=========订阅状态=========
    public static final String SYSCODE_AUDIT_TYPE = "sp.audit.status";//共享数据字典
    public static final Long STATUS_SUB_WAIT = 1L;//待审核
    public static final Long STATUS_SUB_PASS = 2L;//通过
    public static final Long STATUS_SUB_NOTPASS = 3L;//不通过
    public static final Long STATUS_SUB_CANCEL = 4L;//取消
    public static final Long STATUS_SUB_DELETE = 5L;//服务已删除

    //=========服务类型=========
    public static final String SYSCODE_CATALOG_TYPE = "sp.catalog.type";//共享数据字典
    public static final Long CATALOG_TYPE_MIRROR = 1L;//托管
    public static final Long CATALOG_TYPE_NOTMIRROR = 2L;//非托管

    //=========API类型=========
    public static final Long API_TYPE_INDEX = 1L;//首页地址
    public static final Long API_TYPE_API = 2L;//API接口


    //===========附件类型=========
    public static final String SERVICE_LOGO = "logo";//服务logo
    public static final String SERVICE_ATTACHMENT = "attachment";//服务附件

    //===========标签模型运行状态=========
    public static final String DT_TGMODEL_STATUS="dt.tgmode.status";//共享数据字典
    public static final Long TG_MODEL_NO_BEGIN = 0L;//未开始
    public static final Long TG_MODEL_BEGIN = 1L;//开始运行
    public static final Long TG_MODEL_ERROR = 3L;//运行出错
    public static final Long TG_MODEL_END = 4L;//运行结束

    //===========字段条件设置 =================
    public static final String DT_TAG_CONIDTINS = "dt.tag.conditions";//条件设置条件符号

}
