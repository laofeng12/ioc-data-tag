package com.openjava.datatag.utils.jdbc.excuteUtil;

import org.ljdp.component.result.Result;

import java.util.List;
import java.util.Map;

/**
 * 上传文件
 * @author: zmk
 * @Date: 2019/7/22 14:17
 */
public abstract class ExecuteUtil {

    /**
     * 数据库表前缀
     */
    final protected static String TABLE_PRE = "DT_";

    /**
     * 处理文件（核心方法）
     * @return
     */
    public abstract  Result dealFile();

    /*==================================处理表==============================*/
    /**
     * 创建数据库表
     * @return
     */
    protected abstract Result createTable(Map<String,String> columnMap);
    /**
     * 获取建表语句
     * @return
     */
    protected abstract List<String> getCreateTableSqlList();

    /**
     * 删表
     * @return
     */
    protected abstract Result dropTable();

    /**
     * 获取删表语句
     * @return
     */
    protected abstract String getDropTableSql();

    /*==================================处理数据==============================*/

    /**
     * 插入数据
     * @return
     */
    protected abstract Result insertDataList();

    /**
     * 获取插入数据的sql
     * @return
     */
    protected abstract List<String> getInsertDataSqlList();
}
