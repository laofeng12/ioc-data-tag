package com.openjava.datatag.schedule.api;


import com.alibaba.fastjson.JSONObject;
import com.openjava.datatag.schedule.domain.TaskInfo;
import com.openjava.datatag.schedule.service.TaskServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.ljdp.component.result.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Api(tags="调度接口")
@RestController
@RequestMapping("")
public class JobAction {

    @Autowired
    private TaskServiceImpl taskServiceImpl;

    @RequestMapping(value="/",method= RequestMethod.GET)
    public String Hello() {
        return "hello world";
    }


    /**
    * 任务列表
    */
    @ApiOperation(value = "调度列表", notes = "展示所有调度信息", nickname="list")
    @RequestMapping(value="list", method=RequestMethod.POST)
    public String list() throws Exception{
        Map<String, Object> map = new HashMap<>();
        List<TaskInfo> infos = taskServiceImpl.list();
        map.put("rows", infos);
        map.put("total", infos.size());
        return JSONObject.toJSONString(map);
    }


    /**
     * 保存定时任务
     */
    @ApiOperation(value = "保存定时任务", notes = "保存定时任务", nickname="save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "不填默认为0，表示创建", required = false, dataType = "String", paramType = "post"),
            @ApiImplicitParam(name = "jobName", value = "模型id", required = true, dataType = "String", paramType = "post"),
            @ApiImplicitParam(name = "jobGroup", value = "用户id", required = true, dataType = "String", paramType = "post"),
            @ApiImplicitParam(name = "jobDescription", value = "相关参数", required = true, dataType = "String", paramType = "post"),
            @ApiImplicitParam(name = "cronExpression", value = "cron表达式", required = true, dataType = "String", paramType = "post"),
    })
    @RequestMapping(value="save", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public SuccessMessage save(TaskInfo info) throws Exception{
        if(info.getId() == 0) {
            taskServiceImpl.addJob(info);
        }else{
            taskServiceImpl.edit(info);
        }
        return new SuccessMessage("保存成功");
    }


    /**
     *删除定时任务
     */
    @ApiOperation(value = "删除定时任务", notes = "删除定时任务", nickname="delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "模型id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "jobGroup", value = "用户id", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value="delete/{jobName}/{jobGroup}", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public SuccessMessage delete(@PathVariable String jobName, @PathVariable String jobGroup) throws Exception{
        taskServiceImpl.delete(jobName, jobGroup);
        return new SuccessMessage("删除成功");
    }

    /**
    * 暂停定时任务
    */
    @ApiOperation(value = "暂停定时任务", notes = "暂停定时任务", nickname="pause")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "模型id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "jobGroup", value = "用户id", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value="pause/{jobName}/{jobGroup}", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public SuccessMessage pause(@PathVariable String jobName, @PathVariable String jobGroup) throws Exception{
            taskServiceImpl.pause(jobName, jobGroup);
        return new SuccessMessage("暂定成功");
    }

    /**
    * 重新开始定时任务
    */
    @ApiOperation(value = "重新开始定时任务", notes = "重新开始定时任务", nickname="resume")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobName", value = "模型id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "jobGroup", value = "用户id", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value="resume/{jobName}/{jobGroup}", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public SuccessMessage resume(@PathVariable String jobName, @PathVariable String jobGroup) throws Exception{
        taskServiceImpl.resume(jobName, jobGroup);
        return new SuccessMessage("重启成功");
    }


}
