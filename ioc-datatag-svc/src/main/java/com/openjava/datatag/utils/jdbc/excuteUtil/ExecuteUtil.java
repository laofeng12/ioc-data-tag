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
    public abstract Result createTable(Map<String,String> columnMap,Map<String,String> columnTypeMap);
    /**
     * 获取建表语句
     * @return
     */
    public abstract List<String> getCreateTableSqlList();

    /**
     * 删表
     * @return
     */
    public abstract Result dropTable();

    /**
     * 获取删表语句
     * @return
     */
    public abstract String getDropTableSql();

    /*==================================处理数据==============================*/

    /**
     * 插入数据
     * @return
     */
    public abstract Result insertDataList()throws Exception;

    /**
     * 获取插入数据的sql
     * @return
     */
    public abstract List<String> getInsertDataSqlList();

    /**
     * 更新数据
     */
    public abstract Result updateDataList();

    /**
     * 获取更新语句sql
     */
    public abstract  List<String> getUpdateDataList();

    /**
     * 查询数据（分页）
     */
    public abstract String[][] getData();
    /**
     * 查询数据(不分页)
     */
    public abstract String[][] getData2();

    /**
     * 获取查询语句SQL
     */
    public abstract String getQuerySql();
}
