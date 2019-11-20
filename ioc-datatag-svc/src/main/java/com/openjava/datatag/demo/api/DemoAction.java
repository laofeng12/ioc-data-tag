package com.openjava.datatag.demo.api;


import com.openjava.datatag.common.Constants;
import com.openjava.datatag.component.WebsocketServer;
import com.openjava.datatag.demo.service.DemoService;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.userprofile.service.PortrayalService;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.secure.annotation.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * api接口
 * @author zmk
 *
 */
@Api(tags="demo")
@RestController
@RequestMapping("/datatag/demo/cline")
public class DemoAction {

	@Resource
	WebsocketServer websocketServer;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Resource
	private PortrayalService portrayalService;
	@Resource
	private DemoService demoService;

	@ApiOperation(value = "调用其他组件的例子", nickname="save", notes = "调用其他组件的例子")
	@Security(session=false)
	@RequestMapping(value="/doPost", method=RequestMethod.POST)
	public DataApiResponse<Object> selectCol(@RequestBody DtTaggingModelDTO body,
											 HttpServletRequest request) throws Exception{
		MppPgExecuteUtil u= new MppPgExecuteUtil();
		u.setSQL("select * from \"DT_1\"  t, \"zmk_test\" t2 where t.tb_0_id = t2.id");
		String[][] data = u.getData();
		DataApiResponse<Object> resp = new DataApiResponse();
		resp.setData(data);
		return resp;
	}

	@ApiOperation(value = "websocket", nickname="save", notes = "websocket调试")
	@Security(session=false)
	@RequestMapping(value="/sendWebSocket", method=RequestMethod.POST)
	public DataApiResponse<Object> sendWebSocket(@RequestBody DtTaggingModelDTO body,
											 HttpServletRequest request) throws Exception{
		websocketServer.sendMessage(1L,666);
		DataApiResponse<Object> resp = new DataApiResponse();
		return resp;
	}

	@ApiOperation(value = "redisMessageQueue", nickname="save", notes = "redisMessageQueue调试")
	@Security(session=false)
	@RequestMapping(value="/redisMessageQueue", method=RequestMethod.GET)
	public void sendMessage(){
//		stringRedisTemplate.convertAndSend("pmp",String.valueOf(Math.random()));
		stringRedisTemplate.convertAndSend(Constants.DT_REDIS_MESSAGE_QUEUE_CHANL,String.valueOf(Math.random()));
	}

	@ApiOperation(value = "testDowload", nickname="save", notes = "redisMessageQueue调试")
	@Security(session=false)
	@RequestMapping(value="/testDowload", method=RequestMethod.GET)
	public void testDowload(Long taggingModelId){
		portrayalService.dowloadRunResult(100L,taggingModelId);
	}

	@ApiOperation(value = "testAudit", nickname="testAudit", notes = "testAudit")
	@Security(session=false)
	@RequestMapping(value="/testAudit", method=RequestMethod.GET)
	public void testAudit(Long type) throws Exception
	{
		if (type==1){
			demoService.testAudit1();
		}
		if (type==2){
			demoService.testAudit2();
		}
		if (type==3){
			demoService.testAudit3();
		}
		if (type==4){
			demoService.testAudit4();
		}
	}
}