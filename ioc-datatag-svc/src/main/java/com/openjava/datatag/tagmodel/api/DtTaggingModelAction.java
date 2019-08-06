package com.openjava.datatag.tagmodel.api;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmodel.dto.DtTaggingDispatchDTO;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.service.DtSetColService;
import com.openjava.datatag.utils.IpUtil;
import com.openjava.datatag.user.service.SysUserService;
import com.openjava.datatag.utils.jdbc.excuteUtil.MppPgExecuteUtil;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

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

	@Resource
	private DtSetColService dtSetColService;

	@Resource
	private SysUserService sysUserService;
	@Resource
	private MppPgExecuteUtil mppPgExecuteUtil;
	/**
	 * 用主键获取数据
	 * @return
	 */
	@ApiOperation(value = "获取标签模型数据", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taggingModelId", value = "标签模型id", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "dataSetId", value = "打标目的表id", required = false, dataType = "string", paramType = "query"),
	})
	@ApiResponses({
		@io.swagger.annotations.ApiResponse(code=20020, message="会话失效")
	})
	@Security(session=true)
	@RequestMapping(value="/getModel",method=RequestMethod.GET)
	public DtTaggingModelDTO get(
			@RequestParam(value="taggingModelId",required=true)Long taggingModelId,
			@RequestParam(value="dataSetId",required=false)Long resourceId) throws Exception{
		DtTaggingModel m = dtTaggingModelService.get(taggingModelId);
		if (m == null || m.getIsDeleted().equals(Constants.PUBLIC_YES)){
			throw new APIException(MyErrorConstants.TAG_MODEL_NO_FIND,"无此标签模型或已被删除");
		}
		if (resourceId!=null) {
			if (!resourceId.equals(m.getResourceId())) {
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"请选择："+m.getResourceName()+"进行打标");
			}
		}
		DtTaggingModelDTO result = new DtTaggingModelDTO();
		MyBeanUtils.copyPropertiesNotBlank(result,m);
		result.setColList(dtSetColService.getByTaggingModelId(result.getTaggingModelId()));
		result.setModifyUserName(sysUserService.get(result.getModifyUser()).getFullname());
		result.setCreateUserName(sysUserService.get(result.getCreateUser()).getFullname());
		return result;
	}
	
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "like_modelName", value = "模型名字like", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "eq_runState", value = "运行状态:未运行/运行中/运行出错/运行结束/运行停止=", required = false, dataType = "Long", paramType = "query"),
		//@ApiImplicitParam(name = "eq_isDeleted", value = "删除标记=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "le_startTime", value = "运行开始时间<=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "ge_startTime", value = "运行开始时间>=", required = false, dataType = "Date", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtTaggingModelDTO> doSearch(@ApiIgnore() DtTaggingModelDBParam params, @ApiIgnore() Pageable pageable){
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		params.setEq_isDeleted(Constants.PUBLIC_NO);
		params.setEq_createUser(Long.parseLong(userInfo.getUserId()));
		Pageable mypage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Order.desc("modifyTime")).and(Sort.by(Sort.Order.desc("createTime"))));
		Page<DtTaggingModel> results =  dtTaggingModelService.query(params, mypage);
		List<DtTaggingModelDTO> showList = new ArrayList<>();
		for (DtTaggingModel tgm: results){
			DtTaggingModelDTO dto = new DtTaggingModelDTO();
			MyBeanUtils.copyPropertiesNotBlank(dto,tgm);
			dto.setCreateUserName(sysUserService.get(dto.getCreateUser()).getFullname());
			dto.setModifyUserName(sysUserService.get(dto.getModifyUser()).getFullname());
			showList.add(dto);
		}
		Page<DtTaggingModelDTO> showResult = new PageImpl<>(showList,pageable,showList.size());
		return new TablePageImpl<>(showResult);
	}
	
	/**
	 * 保存
	 */
	@ApiOperation(value = "保存", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody DtTaggingModel body,
								 HttpServletRequest request) throws APIException {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		String ip = IpUtil.getRealIP(request);
		if(body.isNew()) {
			//新增，记录创建时间等
			dtTaggingModelService.doNew(body,userInfo,ip);
		} else {
			//修改，记录更新时间等
			DtTaggingModel db = dtTaggingModelService.get(body.getTaggingModelId());
			if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)){
				throw new APIException(MyErrorConstants.TAG_MODEL_NO_FIND,"找不到该模型或模型已经被删除");
			}
			if(body.getIsDeleted().equals(Constants.PUBLIC_YES)){
				throw new APIException(MyErrorConstants.PUBLIC_ERROE,"本接口不可用来删除");
			}
			dtTaggingModelService.doUpdate(body,db,userInfo,ip);
		}
		
		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
	}

	/**
	 * 调度
	 */
	@ApiOperation(value = "设置调度", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(value="/Dispatch",method=RequestMethod.POST)
	@ApiResponses({
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.TAG_MODEL_NO_FIND, message="找不到该模型或模型已经被删除"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.PUBLIC_NO_AUTHORITY, message="没有权限修改本模型"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.TAGM_DISPATCH_NONE_START_TIME, message="未设置开始时间"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.TAGM_DISPATCH_CYCLE_ERROR, message="Cycle不合法")
	})
	public SuccessMessage doDispatch(@RequestBody DtTaggingDispatchDTO body,
									 HttpServletRequest request) throws APIException {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		Long userId = Long.parseLong(userInfo.getUserId());
		String ip = IpUtil.getRealIP(request);

		DtTaggingModel db = dtTaggingModelService.get(body.getId());
		if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)){
			throw new APIException(MyErrorConstants.TAG_MODEL_NO_FIND,"找不到该模型或模型已经被删除");
		}
		if(db.getCreateUser() != null && db.getCreateUser().equals(userId)){
			dtTaggingModelService.doDispatch(body,db,userId,ip);
		}else{
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"没有权限修改本模型");
		}
		return new SuccessMessage("修改调度成功");
	}

	/**
	 * 用主键获取数据
	 * @return
	 */
	@ApiOperation(value = "获取标签模型调度数据", notes = "单个对象查询", nickname="id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "taggingModelId", value = "标签模型id", required = true, dataType = "string", paramType = "query"),
	})
	@ApiResponses({
			@io.swagger.annotations.ApiResponse(code=20020, message="会话失效"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.TAG_MODEL_NO_FIND, message="找不到该模型或模型已经被删除"),
			@io.swagger.annotations.ApiResponse(code=MyErrorConstants.PUBLIC_NO_AUTHORITY, message="无此标签模型权限"),
	})
	@Security(session=true)
	@RequestMapping(value="/Dispatch",method=RequestMethod.GET)
	public DtTaggingDispatchDTO getDispatch(
			@RequestParam(value="taggingModelId",required=true)Long taggingModelId) throws Exception{
		DtTaggingModel m = dtTaggingModelService.get(taggingModelId);
		if (m == null || m.getIsDeleted().equals(Constants.PUBLIC_YES)){
			throw new APIException(MyErrorConstants.TAG_MODEL_NO_FIND,"无此标签模型或已被删除");
		}
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		Long userId = Long.parseLong(userInfo.getUserId());
		if (!m.getCreateUser().equals(userId)){
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"无此标签模型权限");
		}
		DtTaggingDispatchDTO dto = new DtTaggingDispatchDTO();
		dto.setCycleEnum(m.getCycleEnum());
		dto.setStartTime(m.getStartTime());
		dto.setId(m.getTaggingModelId());
		return dto;
	}

	/**
	 * 另存
	 */
	@ApiOperation(value = "另存", nickname="clone", notes = "报文格式：content-type=application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "taggingModelId", value = "主键编码", required = true, paramType = "path"),
	})
	@Security(session=true)
	@RequestMapping(value="/copy/{taggingModelId}",method=RequestMethod.POST)
	public SuccessMessage clone(@PathVariable(value="taggingModelId")Long id,
								HttpServletRequest request) throws Exception {
		String ip = IpUtil.getRealIP(request);
		dtTaggingModelService.copy(id,ip);
		return new SuccessMessage("另存成功");
	}

	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
		//@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用,分隔", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id,
			HttpServletRequest request) throws APIException {
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
		Long userId = Long.parseLong(userInfo.getUserId());
		String ip = IpUtil.getRealIP(request);
		DtTaggingModel db = dtTaggingModelService.get(id);
		if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)){
			throw new APIException(MyErrorConstants.TAG_MODEL_NO_FIND,"找不到该模型或模型已经被删除");
		}
		if(db.getCreateUser() != null && db.getCreateUser().equals(userId)){
			dtTaggingModelService.doSoftDelete(db,userId,ip);
		}else{
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY,"没有权限修改本模型");
		}
		return new SuccessMessage("删除成功");
	}


	@ApiOperation(value = "获取数据集数据", nickname="getDataSetData")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "taggingModelId", value = "模型主键编码", dataType ="String", paramType = "path"),
			@ApiImplicitParam(name = "type", value = "数据结构类型：1返回key:value，0返回value", dataType ="String", paramType = "path"),
			@ApiImplicitParam(name = "size", value = "每页显示数量", dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "page", value = "页码", dataType = "String", paramType = "path"),
	})
	@Security(session=true)
	@RequestMapping(value="/{taggingModelId}/{page}/{size}/{type}",method=RequestMethod.GET)
	public DataApiResponse<Object> getDataSetData(
			@PathVariable(value="taggingModelId")Long taggingModelId,
			@PathVariable(value="type")int type,
			@PathVariable(value="page")int page,
			@PathVariable(value="size")int size) throws Exception {
		DataApiResponse response = new DataApiResponse();
		Pageable pageable = PageRequest.of(page,size);
		Object  data= dtTaggingModelService.getDataFromDataSet(taggingModelId,type,pageable);
		response.setData(data);
		return response;
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

	@ApiOperation(value = "PG测试", nickname="PG测试")
	@Security(session=false)
	@RequestMapping(value="/test",method=RequestMethod.GET)
	public DataApiResponse<Object> test() throws Exception {
		DataApiResponse response = new DataApiResponse();
		mppPgExecuteUtil.setTableName("zmk_test");//表名
		mppPgExecuteUtil.setTableKey("id");//主键
		mppPgExecuteUtil.dropTable();//删表
        Map<String,String> map  = new LinkedHashMap<>();
        Map<String,String> mapType  = new LinkedHashMap<>();
        map.put("id","主键");
        map.put("name","名字");
        map.put("create_time","创建时间");
        mapType.put("id","bigint");
        mapType.put("name","varchar");
        mapType.put("create_time","date");
		mppPgExecuteUtil.createTable(map,mapType);//建表
        List<Object> dataList = new ArrayList<>();
        List<String> values = new ArrayList<>();
        values.add("1");
        values.add("名字1");
        values.add("2018-07-09 00:00:00");
        dataList.add(values);
        List<String> values2 = new ArrayList<>();
        values2.add("2");
        values2.add("名字2");
        values2.add("2018-07-09 00:00:00");
        dataList.add(values2);
		mppPgExecuteUtil.setDataList(dataList);
		mppPgExecuteUtil.insertDataList();//load数据
		mppPgExecuteUtil.dropTable();//删表
		mppPgExecuteUtil.setSQL("select * from \"DT_1\"  t ");
        String[][] data = mppPgExecuteUtil.getData();
        System.out.println(data.length);
		return response;
	}
}
