package com.openjava.admin.job.service;

import com.openjava.admin.job.domain.SysWorkflowTask;

public interface SysWorkflowTaskService {
//    public Page<SysJobLog> query(SysJobLogDBParam params, Pageable pageable);

//    public List<SysJobLog> queryDataOnly(SysJobLogDBParam params, Pageable pageable);

    public SysWorkflowTask get(Long id);

    public SysWorkflowTask doSave(SysWorkflowTask m);
}
