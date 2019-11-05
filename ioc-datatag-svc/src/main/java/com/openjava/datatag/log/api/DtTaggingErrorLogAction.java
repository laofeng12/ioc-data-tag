package com.openjava.datatag.log.api;

import com.openjava.datatag.log.domain.DtTagcolUpdateLog;
import com.openjava.datatag.log.domain.DtTaggingErrorLog;
import com.openjava.datatag.log.query.DtTagcolUpdateLogDBParam;
import com.openjava.datatag.log.service.DtTagcolUpdateLogService;
import com.openjava.datatag.log.service.DtTaggingErrorLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;


/**
 * api接口
 * @author zmk
 *
 */
@Api(tags="调度错误日志")
@RestController
@RequestMapping("/datatag/tagmodel/dtTaggingErrorlog")
public class DtTaggingErrorLogAction {
	
	@Resource
	private DtTaggingErrorLogService dtTaggingErrorLogService;

	@ApiOperation(value = "获取报错日志", notes = "", nickname="getErrormessage")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taggingModelId", value = "模型主键=", required = false, dataType = "Long", paramType = "path"),
	})
	@Security(session=true)
	@RequestMapping(value="/{taggingModelId}",method=RequestMethod.GET)
	public String doSearch(@PathVariable("taggingModelId") Long taggingModelId){
		DtTaggingErrorLog log=  dtTaggingErrorLogService.getByTaggingModelIdOrderByErrorTimeDesc(taggingModelId);
		String errorLog = null;
		if (log!=null){
			errorLog = String.valueOf(log.getErrorInfo());

		}
		return errorLog;
	}
}
