package com.openjava.datatag.tagmodel.api;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmodel.dto.*;
import com.openjava.datatag.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.service.DtSetColService;
import com.openjava.datatag.tagmodel.query.DtSetColDBParam;


/**
 * api接口
 * @author zmk
 *
 */
@Api(tags="字段设置")
@RestController
@RequestMapping("/datatag/tagmodel/dtSetCol")
public class DtSetColAction {
	
	@Resource
	private DtSetColService dtSetColService;

	/**
	 * 字段设置确认选择接口
	 */
	@ApiOperation(value = "字段设置确认选择接口", nickname="save", notes = "新增格式：{\"resourceId\":1,\"resourceName\":\"高考数据\",\"resourceType\":1,\"pkey\":\"user_name\",\"colList\":[{\"sourceCol\":\"user_name\",\"sourceDataType\":\"String\",\"isMarking\":1}]}" +
			"修改的格式：{\"taggingModelId\":1,\"pkey\":\"userId2\",\"resourceId\":1,\"resourceName\":\"高考数据233\",\"resourceType\":1,\"colList\":[{\"colId\":1,\"taggingModelId\":1,\"sourceCol\":\"name\",\"sourceDataType\":\"String\",\"isMarking\":1},{\"sourceCol\":\"userId2\",\"sourceDataType\":\"Long\",\"isMarking\":1}]}")
	@Security(session=true,allowResources = {"lableImage"})
	@RequestMapping(value="/selectCol", method=RequestMethod.POST)
	public SelectColSuccessDTO selectCol(@RequestBody DtTaggingModelDTO body,
									HttpServletRequest request) throws Exception{
		String ip = IpUtil.getRealIP(request);
		if (body.getResourceId() == null) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"打标目标表id不能为空");
		}
		if (StringUtils.isBlank(body.getResourceName())) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"打标源表名称不能为空");
		}
		if (StringUtils.isBlank(body.getPkey())){
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"数据源主键不能指定为空");
			//这里应该添加验证主键唯一性约束
		}
		DtTaggingModelDTO dtTaggingModelDTO =  dtSetColService.selectCol(body,ip);
		Long id = dtTaggingModelDTO.getTaggingModelId();
		SelectColSuccessDTO success = new SelectColSuccessDTO();
		success.setCode(200L);
		success.setMessage("保存成功");
		success.setTaggingModelId(id);
		return success;
	}
	
	@ApiOperation(value = "字段清除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
		@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
	})
	@Security(session=true,allowResources = {"lableImage"})
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id,
			//@RequestParam(value="ids",required=false)String ids,
			HttpServletRequest request) throws Exception{
		String ip = IpUtil.getRealIP(request);
		if(id != null) {
			dtSetColService.doDelete(id,ip);
		}
		return new SuccessMessage("清除字段成功");
	}

	@ApiOperation(value = "克隆字段", nickname="delete")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "colId", value = "主键编码", required = true, paramType = "clone"),
	})
	@Security(session=true,allowResources = {"lableImage"})
	@RequestMapping(value="/clone",method=RequestMethod.POST)
	public SuccessMessage clone(@RequestParam(value="colId",required=true)Long colId,
								HttpServletRequest request)throws Exception{
		String ip = IpUtil.getRealIP(request);
		dtSetColService.clone(colId,ip);
		return new SuccessMessage("克隆字段成功");
	}

	@ApiOperation(value = "确认打标-查询打标历史接口", nickname="getHistoryCol")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "colId", value = "主键编码", required = true, paramType = "clone"),
	})
	@Security(session=true,allowResources = {"lableImage"})
	@RequestMapping(value="/getHistoryCol",method=RequestMethod.GET)
	public GetHistoryColDTO getHistoryCol(@RequestParam(value="colId",required=true)Long colId)throws Exception{
		return dtSetColService.getHistoryCol(colId);
	}


	@ApiOperation(value = "确认打标-保存接口", nickname="getHistoryCol",
			notes="数字类型修改：\n{\"colId\":2,\"condtion\":[{\"colId\":2,\"isHandle\":0,\"tagConditionId\":1000001,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\">\",\"theValues\":\"1\",\"valuesType\":\"NUMBER\"},{\"isConnectSymbol\":1,\"symbol\":\"AND\",\"theValues\":\"\",\"valuesType\":\"\"},{\"isConnectSymbol\":0,\"symbol\":\"<\",\"theValues\":\"5\",\"valuesType\":\"NUMBER\"}]},{\"colId\":2,\"isHandle\":1,\"tagConditionId\":1000000,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\"IN\",\"theValues\":\"1,2,3,4\",\"valuesType\":\"NUMBER\"}]}]}\n" +
					"字符串类型修改：\n{\"colId\":2,\"condtion\":[{\"colId\":2,\"isHandle\":0,\"tagConditionId\":1000001,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\"=\",\"theValues\":\"王同学\",\"valuesType\":\"VARCHAR2\"},{\"isConnectSymbol\":1,\"symbol\":\"AND\",\"theValues\":\"\",\"valuesType\":\"\"},{\"isConnectSymbol\":0,\"symbol\":\"≠\",\"theValues\":\"李同学\",\"valuesType\":\"VARCHAR2\"}]},{\"colId\":2,\"isHandle\":1,\"tagConditionId\":1000000,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\"IN\",\"theValues\":\"王同学,李同学,吴同学\",\"valuesType\":\"VARCHAR2\"}]}]}\n" +
					"数字类型新增：\n{\"colId\":1,\"condtion\":[{\"colId\":1,\"isHandle\":0,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\"=\",\"theValues\":\"123\",\"valuesType\":\"NUMBER\"},{\"isConnectSymbol\":1,\"symbol\":\"AND\",\"theValues\":\"\",\"valuesType\":\"\"},{\"isConnectSymbol\":0,\"symbol\":\"≠\",\"theValues\":\"2333\",\"valuesType\":\"NUMBER\"}]},{\"colId\":1,\"isHandle\":1,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\"IN\",\"theValues\":\"1,2,3\",\"valuesType\":\"NUMBER\"}]}]}\n" +
					"字符串类型新增：\n{\"colId\":1,\"condtion\":[{\"colId\":1,\"isHandle\":0,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\"=\",\"theValues\":\"王同学\",\"valuesType\":\"VARCHAR2\"},{\"isConnectSymbol\":1,\"symbol\":\"AND\",\"theValues\":\"\",\"valuesType\":\"\"},{\"isConnectSymbol\":0,\"symbol\":\"≠\",\"theValues\":\"李同学\",\"valuesType\":\"VARCHAR2\"}]},{\"colId\":1,\"isHandle\":1,\"tagId\":790991990471247,\"conditionSetting\":[{\"isConnectSymbol\":0,\"symbol\":\"IN\",\"theValues\":\"王同学,李同学,吴同学\",\"valuesType\":\"VARCHAR2\"}]}]}\n" +
					"")
	@ApiImplicitParams({
//			@ApiImplicitParam(name = "colId", value = "主键编码", required = true, paramType = "clone"),
	})
	@Security(session=true,allowResources = {"lableImage"})
	@RequestMapping(value="/saveCondition",method=RequestMethod.POST)
	public DataApiResponse<Object> saveCondition(@RequestBody SaveConditionDTO req,
												 HttpServletRequest request)throws Exception{
		req.setIp(request);
		DataApiResponse<Object> result = new DataApiResponse<>();
		try{
			dtSetColService.saveCondition(req);
			result.setMessage("保存成功");
		}catch (APIException e){
			if (MyErrorConstants.TAG_TAGGING_GRAMMAR_ERROR==e.getCode()){
				result.setCode(MyErrorConstants.TAG_TAGGING_GRAMMAR_ERROR);
				result.setData(e.getMessage());
				result.setMessage("第"+e.getMessage()+"条数据设置语法错误，请重新设置");
			}
		}
		return result;
	}
}
