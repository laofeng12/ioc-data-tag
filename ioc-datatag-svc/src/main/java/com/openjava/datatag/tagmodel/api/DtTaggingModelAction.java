package com.openjava.datatag.tagmodel.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.utils.EntityClassUtil;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.sequence.TimeSequence;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
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

import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;


/**
 * api接口
 * @author zmk
 *
 */
@Api(tags="标签模型")
@RestController
@RequestMapping("/datatag/tagmodel/dtTaggingModel")
public class DtTaggingModelAction {
	
	@Resource
	private DtTaggingModelService dtTaggingModelService;
	
	/**
	 * 用主键获取数据
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据ID获取", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主标识编码", required = true, dataType = "string", paramType = "path"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public DtTaggingModel get(@PathVariable("id")Long id) {
		DtTaggingModel m = dtTaggingModelService.get(id);
		return m;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_modelName", value = "模型名字like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_runState", value = "运行状态:未运行/运行中/运行出错/运行结束=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_isDeleted", value = "删除标记=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "le_startTime", value = "运行开始时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_startTime", value = "运行开始时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtTaggingModel> doSearch(@ApiIgnore() DtTaggingModelDBParam params, @ApiIgnore() Pageable pageable){
		Page<DtTaggingModel> result =  dtTaggingModelService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(value="save",method=RequestMethod.POST)
	public DtTaggingModel doSave(@RequestBody DtTaggingModel body

			) {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		Date now = new Date();
		DtTaggingModel dbObj=null;
		if(body.isNew()) {
			//新增，记录创建时间等
			EntityClassUtil.dealCreateInfo(body,userInfo);
			body.setRunState(Constants.TG_MODEL_NO_BEGIN);//未开始
			body.setIsNew(true);//执行insert
			dbObj = dtTaggingModelService.doSave(body);
		} else {
			//修改，记录更新时间等
			DtTaggingModel db = dtTaggingModelService.get(body.getTaggingModelId());
			EntityClassUtil.dealModifyInfo(db,userInfo);
			db.setModelDesc(body.getModelDesc());
			db.setModelName(body.getModelName());
			db.setIsNew(false);//执行update
			dbObj =dtTaggingModelService.doSave(db);
		}
		
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return dbObj;
	}

	/**
	 * 克隆
	 */
	@ApiOperation(value = "克隆", nickname="clone", notes = "报文格式：content-type=application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(value="/clone",method=RequestMethod.POST)
	public SuccessMessage clone(@RequestParam(value="id",required=false)Long id) throws Exception {
		dtTaggingModelService.clone(id);
		return new SuccessMessage("克隆成功");
	}

	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
		@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id,
			@RequestParam(value="ids",required=false)String ids) {
		if(id != null) {
			dtTaggingModelService.doDelete(id);
		} else if(ids != null) {
			dtTaggingModelService.doRemove(ids);
		}
		return new SuccessMessage("删除成功");//没有需要返回的数据，就直接返回一条消息
	}
	
	/**
	 * 导出Excel文件
	 */
	@Security(session=true)
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			DtTaggingModelDBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<DtTaggingModel> result = dtTaggingModelService.query(params, pageable);
			
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
			
			myBuilder.buildSheet("标签模型", result.getContent());//放到第一个sheet
			
			String filename = "标签模型("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
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
	}
}
