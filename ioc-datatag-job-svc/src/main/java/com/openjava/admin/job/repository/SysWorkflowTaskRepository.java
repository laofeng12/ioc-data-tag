package com.openjava.admin.job.repository;

import com.openjava.admin.job.domain.SysWorkflowTask;
import org.ljdp.core.spring.data.DynamicJpaRepository;

public interface SysWorkflowTaskRepository extends DynamicJpaRepository<SysWorkflowTask, Long>, SysWorkflowTaskRepositoryCustom {

}
