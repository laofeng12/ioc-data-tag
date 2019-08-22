package com.openjava.admin.job.service;

import com.openjava.admin.job.domain.SysWorkflowTask;
import com.openjava.admin.job.repository.SysWorkflowTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
@Transactional
public class SysWorkflowTaskServiceImpl implements SysWorkflowTaskService {
    @Resource
    private SysWorkflowTaskRepository sysWorkflowTaskRepository;
    public SysWorkflowTask get(Long id) {
        Optional<SysWorkflowTask> m = sysWorkflowTaskRepository.findById(id);
        return m.get();
    }

    public SysWorkflowTask doSave(SysWorkflowTask m) {
        return sysWorkflowTaskRepository.save(m);
    }
}
