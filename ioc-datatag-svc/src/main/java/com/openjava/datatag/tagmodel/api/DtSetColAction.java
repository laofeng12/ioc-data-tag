package com.openjava.datatag.tagmodel.api;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.dto.GetHistoryColDTO;
import com.openjava.datatag.tagmodel.dto.SaveConditionDTO;
import com.openjava.datatag.tagmodel.dto.SelectColDTO;
import com.openjava.datatag.utils.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.secure.annotation.Security;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
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
	 * 用主键获取数据
	 * @param id
	 * @return
	 *//*
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public DtSetCol get(@PathVariable("id")Long id) {
		DtSetCol m = dtSetColService.get(id);
		return m;
	}*/
	
	/*@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_taggingModelId", value = "标签模型主键=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_taggingModelId", value = "标签模型编号=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_sourceCol", value = "源字段名=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_showCol", value = "显示字段名=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_isMlanual", value = "是否手动打标字段=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_isMarking", value = "是否打标字段=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_isSource", value = "是否源字段=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtSetCol> doSearch(@ApiIgnore() DtSetColDBParam params, @ApiIgnore() Pageable pageable){
		Page<DtSetCol> result =  dtSetColService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}*/
	

	
	/**
	 * 保存
	 *//*
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody DtSetCol body

			) {
		if(body.isNew()) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			body.setColId(ss.getSequence());
			body.setIsNew(true);//执行insert
			DtSetCol dbObj = dtSetColService.doSave(body);
		} else {
			//修改，记录更新时间等
			DtSetCol db = dtSetColService.get(body.getColId());
			MyBeanUtils.copyPropertiesNotBlank(db, body);
			db.setIsNew(false);//执行update
			dtSetColService.doSave(db);
		}
		
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
	}*/

	/**
	 * 字段设置确认选择接口
	 */
	@ApiOperation(value = "字段设置确认选择接口", nickname="save", notes = "新增格式：{\"dataSetId\":1,\"dataSetName\":\"高考数据\",\"pKey\":\"user_name\",\"colList\":[{\"sourceCol\":\"user_name\",\"sourceDataType\":\"String\",\"isMarking\":1}]}" +
			"修改的格式：{\"taggingModelId\":1,\"dataSetId\":1,\"dataSetName\":\"高考数据233\",\"colList\":[{\"colId\":1,\"taggingModelId\":1,\"sourceCol\":\"name\",\"sourceDataType\":\"String\",\"isMarking\":1},{\"sourceCol\":\"userId2\",\"sourceDataType\":\"Long\",\"isMarking\":1}]}")
	@Security(session=true)
	@RequestMapping(value="/selectCol", method=RequestMethod.POST)
	public SuccessMessage selectCol(@RequestBody DtTaggingModelDTO body,
									HttpServletRequest request) throws Exception{
		String ip = IpUtil.getRealIP(request);
		if (body.getDataSetId() == null) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"打标目标表id不能为空");
		}
		if (StringUtils.isBlank(body.getDataSetName())) {
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"打标源表名称不能为空");
		}
		if (StringUtils.isBlank(body.getPkey())){
			throw new APIException(MyErrorConstants.PUBLIC_ERROE,"数据源主键不能指定为空");
			//这里应该添加验证主键唯一性约束
		}
		dtSetColService.selectCol(body,ip);
		return new SuccessMessage("保存成功");
	}
	
	@ApiOperation(value = "字段清除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
		@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id,
			@RequestParam(value="ids",required=false)String ids,
			HttpServletRequest request) throws Exception{
		String ip = IpUtil.getRealIP(request);
		if(id != null) {
			dtSetColService.doDelete(id,ip);
		} else if(ids != null) {
			dtSetColService.doRemove(ids);
		}
		return new SuccessMessage("清除字段成功");
	}

	@ApiOperation(value = "克隆字段", nickname="delete")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "colId", value = "主键编码", required = true, paramType = "clone"),
	})
	@Security(session=true)
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
	@Security(session=true)
	@RequestMapping(value="/getHistoryCol",method=RequestMethod.POST)
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
	@Security(session=true)
	@RequestMapping(value="/saveCondition",method=RequestMethod.POST)
	public SuccessMessage saveCondition(@RequestBody SaveConditionDTO req)throws Exception{
		dtSetColService.saveCondition(req);
		return new SuccessMessage("保存成功");
	}

	/**
	 * 导出Excel文件
	 *//*
	@Security(session=true)
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			DtSetColDBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<DtSetCol> result = dtSetColService.query(params, pageable);
			
			POIExcelBuilder myBuilder = new POIExcelBuilder(response.getOutputStream());
			//设置导出字段，以下是示例，请自行编写
//			myBuilder.addProperty("useraccount", "账号");
//			myBuilder.addProperty("username", "用户姓名");
//			myBuilder.addProperty("creatime", "创建时间", FieldType.BASE_DATE, "yyyy-MM-dd");//设置时间格式
//			myBuilder.addProperty("userStatus", "用户状态", SysCodeUtil.codeToMap("sys.user.status"));//自动数据字典【tsys_code】翻译
//			Map<K, V> tfMap1 = new HashMap();
//			tfMap1.put(1, "状态1");
//			tfMap1.put(2, "状态2");
//			myBuilder.addProperty("userStatus", "用户状态",tfMap1);//写死静态字典翻译
			
			myBuilder.buildSheet("字段表", result.getContent());//放到第一个sheet
			
			String filename = "字段表("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
			response.setContentType(ContentType.EXCEL);
			response.addHeader("Content-disposition", "attachment;filename="
					+ new String(filename.getBytes("GBK"), "iso-8859-1"));
			//开始导出
			myBuilder.finish();
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(e.getMessage());
			} catch (Exception e2) {
			}
		}
	}*/
}
