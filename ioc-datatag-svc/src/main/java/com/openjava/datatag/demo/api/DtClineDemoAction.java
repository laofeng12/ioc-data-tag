package com.openjava.datatag.demo.api;

import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.log.domain.DtTagcolUpdateLog;
import com.openjava.datatag.log.query.DtTagcolUpdateLogDBParam;
import com.openjava.datatag.log.service.DtTagcolUpdateLogService;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.utils.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * api接口
 * @author zmk
 *
 */
@Api(tags="字段表日志")
@RestController
@RequestMapping("/datatag/demo/cline")
public class DtClineDemoAction {

	@ApiOperation(value = "调用其他组件的例子", nickname="save", notes = "调用其他组件的例子")
	@Security(session=true)
	@RequestMapping(value="/doPost", method=RequestMethod.POST)
	public SuccessMessage selectCol(@RequestBody DtTaggingModelDTO body,
									HttpServletRequest request) throws Exception{

		return new SuccessMessage("保存成功");
	}
	

}
