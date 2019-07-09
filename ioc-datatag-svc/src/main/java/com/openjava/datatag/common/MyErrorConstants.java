package com.openjava.datatag.common;

import io.swagger.models.auth.In;

public class MyErrorConstants {
    //===========公共错误错误代码=========
    public static final Integer PUBLIC_ERROE = -200;//通用错误代码
    public static final Integer PUBLIC_NO_AUTHORITY= -10001;//没有权限
    //==========打标字段相关代码============
    public static final Integer TAG_COL_NO_FIND= -20001;//无此字段或字段已删除

    //=======标签模型相关错误代码============
    public static final Integer TAG_MODEL_NO_FIND = -30001;//此Id查无模型
    //=======字段打标相关错误代码===========
    public static final Integer  TAG_TAGGING_GRAMMAR_ERROR= -30002;//条件设置语法错误
}
