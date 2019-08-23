package com.openjava.datatag.common;

import io.swagger.models.auth.In;

public class MyErrorConstants {
    //===========公共错误错误代码=========
    public static final int PUBLIC_ERROE = -200;//通用错误代码
    public static final int PUBLIC_NO_AUTHORITY= -10001;//没有权限
    //==========打标字段相关代码============
    public static final int TAG_COL_NO_FIND= -20001;//无此字段或字段已删除

    //=======标签模型相关错误代码============
    public static final int TAG_MODEL_NO_FIND = -30001;//此Id查无模型

    //=======标签管理相关错误代码============
    public static final int TAG_NOT_FOUND = -40001;//无此标签
    public static final int TAG_GROUP_NOT_FOUND = -40002;//无此标签组
    public static final int SHARE_TAG_GROUP_NOT_FOUND = -40003;//无此共享标签组
    public static final int CAN_NOT_CHOOSE = -40004;//不能选用自己的标签组
    //=======字段打标相关错误代码===========
    public static final int TAG_TAGGING_GRAMMAR_ERROR= -30002;//条件设置语法错误

    //=======调度参数错误代码===============
    public static final int TAGM_DISPATCH_CYCLE_ERROR = -40004;
    public static final int TAGM_DISPATCH_NONE_START_TIME = -40005;
}
