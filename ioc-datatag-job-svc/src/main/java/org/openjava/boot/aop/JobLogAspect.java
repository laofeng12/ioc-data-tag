package org.openjava.boot.aop;

import com.openjava.aop.ScheduleJobLogAop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 暂时不启用
 * edit by zmk
 */
//@Component
//@Aspect
//@Order(1)
public class JobLogAspect {

    @Resource
    private ScheduleJobLogAop scheduleJobLogAop;

//    @Pointcut("execution(* com.ioc.sys.job..*.*(..)))")
    public void executeService(){
    }

//    @Around("executeService()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable{
        return scheduleJobLogAop.doAround(point);
    }
}
