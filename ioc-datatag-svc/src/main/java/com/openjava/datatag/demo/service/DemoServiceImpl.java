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
     * type 事件类型（1:管理操作、2：数据查询、3：数据导出、4：数据导入,必传）
     * @param type
     */
    public void testAudit(Long type) throws APIException{
        AuditLogVO vo = new AuditLogVO();
        vo.setType(type);
        vo.setOperationService("标签与画像");
        vo.setOperationModule("模型调度");
        vo.setFunctionLev1("设置");
        vo.setFunctionLev2("确认调度");
        vo.setRecordId(12345L);
        vo.setDataBeforeOperat("这里填操作前数据");
        vo.setDataAfterOperat("这里填操作后数据");
        vo.setFileId(111L);
        vo.setFileUrl("xxx/file.excel");
        try {
            auditComponet.saveAuditLog(vo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(-200100,"保存审计日志失败");
        }
        System.out.println("保存审计日志成功");
    }

    public static void main(String[] args) {

    }
}
