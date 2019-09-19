package com.openjava.datatag.common;

/**
 * 常量定义
 */
public class Constants {
    //========公共状态============
    public static final String PUBLIC_YN = "public.YN";//共享数据字典
    public static final String PUBLIC_MODIFY_TYPE = "public.modify.type";//修改类型
    public static final Long PUBLIC_NO = 0L;//否
    public static final Long PUBLIC_YES = 1L;//是
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
    public static final Long DT_MODEL_NO_BEGIN = 0L;//未开始
    public static final Long DT_MODEL_WAIT = 1L;//等待运行
    public static final Long DT_MODEL_RUNNING = 2L;//运行中
    public static final Long DT_MODEL_SUCCESS = 3L;//运行成功
    public static final Long DT_MODEL_ERROR = 4L;//运行失败
    public static final Long DT_MODEL_END = -1L;//运行结束

    //===========  模型（临时表、打标字段前缀）=====================
    public static final String DT_TABLE_PREFIX="DT_";//临时表前缀
    public static final String DT_COL_PREFIX="TAG_";//打标字段前缀
    public static final String DT_COL_COPY="copy_";//字段克隆前缀
    public static final String DT_SEARCH_TABLE_NAME ="DT_SEARCH";//画像结果中间表名称
    public static final String DT_SEARCH_MODEL_TABLE_NAME ="model_table_name";//模型表名称
    public static final String DT_SEARCH_MODEL_PKEY ="model_p_key";//模型的主键
//    public static final String DT_SEARCH_MODEL_PKEY_NAME ="model_p_key_col_name";//模型主键列名
    public static final String DT_SEARCH_CREATE_TIME ="create_time";//模型主键列名


    //===========   协作打标  ===========================================
    public static final Long DT_COOPERATION_NO = 0L;//进行中
    public static final Long DT_COOPERATION_YES = 1L;//完成
    public static final Long DT_COOP_TAGCOL_LIMMIT_NO = 0L;//未完成协作
    public static final Long DT_COOP_TAGCOL_LIMMIT_YES = 1L;//已完成协作

    //===========标签组状态=================
    public static final Long DT_TG_LOG_NEW = 1L;//日志记录为新增
    public static final Long DT_TG_LOG_UPDATE = 0L;//日志记录为更新
    public static final Long DT_TG_LOG_DELETE = -1L;//日志记录为删除
    //===========字段条件设置 =================
    public static final String DT_TAG_CONIDTINS = "dt.tag.conditions";//条件设置条件符号


    //=========调度运行周期=============
    public static final String DT_MODEL_DISPATCH = "dt.model.dispatch";//周期枚举
    public static final Long DT_DISPATCH_STOP = 0L;
    public static final Long DT_DISPATCH_ONCE = 1L;
    public static final Long DT_DISPATCH_EACH_DAY = 2L;
    public static final Long DT_DISPATCH_EACH_WEEK= 3L;
    public static final Long DT_DISPATCH_EACH_MONTH = 4L;
    public static final Long DT_DISPATCH_EACH_YEAR= 5L;

    //==========定时任务调度组件============
    public static final String DT_SCHEDULE_CORE_JOB_CLASS = "com.openjava.datatag.schedule.job.DtTaggingModelCalculationJob";//模型调度核心job
    public static final String DT_SCHEDULE_GROUP = "datatag_job_group";//模型调度任务组名称

    //=======  索引更新状态=================
    public static final Long DT_INDEX_RUN_STATUS_WAIT = 0L;//未开始
    public static final Long DT_INDEX_RUN_STATUS_RUNNING = 1L;//已开始

    //=======  Redis消息订阅通道 ===============
    public static final String DT_REDIS_MESSAGE_QUEUE_CHANL = "channel";
    /**
     * 数据库类型(0:Oracle;1:MySql高版本;2;Mysql低版本;3:PostgreSql)
     */
    public static final Long DB_TYPE_LONG_ORACLE = 0L;

    public static final Long DB_TYPE_LONG_MYSQL_HIGH = 1L;

    public static final Long DB_TYPE_LONG_MYSQL_LOW = 2L;

    public static final Long DB_TYPE_LONG_POSTGRE = 3L;

    public static final Long DB_TYPE_LONG_HIVE = 4L;

    public static final int DB_TYPE_INT_ORACLE = 0;

    public static final int DB_TYPE_INT_MYSQL_HIGH = 1;

    public static final int DB_TYPE_INT_MYSQL_LOW = 2;

    public static final int DB_TYPE_INT_POSTGRE = 3;

    public static final int DB_TYPE_INT_HIVE = 4;

    /**
     * 数据湖获取数据库表所查询的表
     */
    public static final String DATA_LAKE_TABLE = "";
    /**
     * 数据湖获取数据库表所查询的目录ID
     */
    public static final String DATA_LAKE_CATEGORY = "";
}
