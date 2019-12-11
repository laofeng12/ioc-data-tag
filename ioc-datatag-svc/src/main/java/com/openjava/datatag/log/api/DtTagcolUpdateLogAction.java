package com.openjava.datatag.log.api;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.common.file.ContentType;
import org.ljdp.common.file.POIExcelBuilder;
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

import com.openjava.datatag.log.domain.DtTagcolUpdateLog;
import com.openjava.datatag.log.service.DtTagcolUpdateLogService;
import com.openjava.datatag.log.query.DtTagcolUpdateLogDBParam;


/**
 * api接口
 * @author zmk
 *
 */
//@Api(tags="字段表日志")
@RestController
@RequestMapping("/datatag/tagmodel/dtTagcolUpdateLog")
public class DtTagcolUpdateLogAction {
	
	@Resource
	private DtTagcolUpdateLogService dtTagcolUpdateLogService;//

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	@ApiOperation(value = "列表分页查询", notes = "{total：总数量，totalPage：总页数，rows：结果对象数组}", nickname="search")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "eq_colId", value = "字段表主键=", required = false, dataType = "Long", paramType = "query"),
		@ApiImplicitParam(name = "eq_modifyType", value = "修改类型(修改或删除)=", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "size", value = "每页显示数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
	})
	@Security(session=true)
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public TablePage<DtTagcolUpdateLog> doSearch(@ApiIgnore() DtTagcolUpdateLogDBParam params, @ApiIgnore() Pageable pageable){
		Page<DtTagcolUpdateLog> result =  dtTagcolUpdateLogService.query(params, pageable);
		
		return new TablePageImpl<>(result);
	}
	

	/**
	 * 导出Excel文件
	 */
	@Security(session=true)
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void doExport(HttpServletRequest request, HttpServletResponse response,
			DtTagcolUpdateLogDBParam params) throws Exception{
		try {
			Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
			Page<DtTagcolUpdateLog> result = dtTagcolUpdateLogService.query(params, pageable);
			
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
			
			myBuilder.buildSheet("字段表日志", result.getContent());//放到第一个sheet
			
			String filename = "字段表日志("+DateFormater.formatDatetime_SHORT(new Date())+").xlsx";
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
