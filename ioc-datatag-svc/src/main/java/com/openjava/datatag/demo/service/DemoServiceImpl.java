package com.openjava.datatag.demo.service;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import org.ljdp.component.exception.APIException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class DemoServiceImpl implements DemoService{
    @Resource
    private AuditComponet auditComponet;

    /**
     * 管理操作
     */
    public void testAudit1() throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(1L);//管理操作
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("模型调度");//必传
        vo.setFunctionLev1("设置");//必传
        vo.setFunctionLev2("确认调度");//必传
        vo.setRecordId("12345");//必传。多个表时传主表的记录ID
        vo.setDataBeforeOperat("这里填操作前数据");//必传。
        vo.setDataAfterOperat("这里填操作后数据");//必传。
        try {
            auditComponet.saveAuditLog(vo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(-200100,"保存审计日志失败");
        }
        System.out.println("保存审计日志成功");
    }

    /**
     * 数据查询
     */
    public void testAudit2() throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("模型调度");//必传
        vo.setFunctionLev1("设置");//必传
        vo.setFunctionLev2("确认调度");//必传
        vo.setRecordId("12345");//必传，多个表时传主表的记录ID
        try {
            auditComponet.saveAuditLog(vo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(-200100,"保存审计日志失败");
        }
        System.out.println("保存审计日志成功");
    }

    /**
     * 数据导出
     */
    public void testAudit3() throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(3L);//数据导出
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("模型调度");//必传
        vo.setFunctionLev1("设置");//必传
        vo.setFunctionLev2("确认调度");//必传
        vo.setFileId(1111L);//非必填
        vo.setFileUrl("xxx/file.excel");//非必填
        try {
            auditComponet.saveAuditLog(vo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(-200100,"保存审计日志失败");
        }
        System.out.println("保存审计日志成功");
    }

    /**
     * 数据导入
     */
    public void testAudit4() throws Exception{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(4L);//数据导出
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("模型调度");//必传
        vo.setFunctionLev1("设置");//必传
        vo.setFunctionLev2("确认调度");//必传
        vo.setFileId(1111L);//非必填
        vo.setFileUrl("xxx/file.excel");//非必填
        try {
            auditComponet.saveAuditLog(vo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(-200100,"保存审计日志失败");
        }
        System.out.println("保存审计日志成功");
    }

}
