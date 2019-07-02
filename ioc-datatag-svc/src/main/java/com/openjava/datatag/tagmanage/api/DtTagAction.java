package com.openjava.datatag.tagmanage.api;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.tagmanage.service.DtTagService;
import io.swagger.annotations.*;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.ApiResponse;
import org.ljdp.component.result.BasicApiResponse;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.annotation.Security;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;


/**
 * api接口
 * @author lch
 *
 */
@Api(tags="DT_TAG")
@RestController
@RequestMapping("/datatag/tagmanage/dtTag")
public class DtTagAction {
	
	@Resource
	private DtTagService dtTagService;


	/**
	 * 保存
	 */
	@ApiOperation(value = "新增或修改", nickname="save", notes = "报文格式：content-type=application/json")
	@Security(session=true)
	@RequestMapping(method=RequestMethod.POST)
	public SuccessMessage doSave(@RequestBody DtTag body) throws APIException {
		if(body.getIsNew() == null || body.getIsNew()) {
			//新增，记录创建时间等
			//设置主键(请根据实际情况修改)
			SequenceService ss = ConcurrentSequence.getInstance();
			body.setId(ss.getSequence());
			body.setIsDeleted(0L);
			body.setIsNew(true);//执行insert
			Date now = new Date();
			body.setCreateTime(now);
			body.setModifyTime(now);
			dtTagService.doSave(body);
		} else {
			//修改，记录更新时间等
			DtTag db = dtTagService.get(body.getId());
			body.setPreaTagId(null);
			if (body.getIsDeleted().equals(1L)){
				throw new APIException(500,"请不要用此方法进行删除操作，请用DELETE方法");
			}
			//不允许修改层级和创建时间
			body.setCreateTime(null);
			body.setLvl(null);

			body.setModifyTime(new Date());
			MyBeanUtils.copyPropertiesNotBlank(db, body);
			db.setIsNew(false);//执行update
			dtTagService.doSave(db);
		}

		//没有需要返回的数据，就直接返回一条消息。如果需要返回错误，可以抛异常：throw new APIException(错误码，错误消息)，如果涉及事务请在service层抛;
		return new SuccessMessage("保存成功");
	}


	@ApiOperation(value = "删除", nickname="delete")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "主键编码", required = false, paramType = "delete"),
			@ApiImplicitParam(name = "ids", value = "批量删除用，多个主键编码用 , 分隔", required = false, paramType = "delete"),
	})
	@Security(session=true)
	@RequestMapping(method=RequestMethod.DELETE)
	public SuccessMessage doDelete(
			@RequestParam(value="id",required=false)Long id,
			@RequestParam(value="ids",required=false)String ids) {
		if(id != null) {
			dtTagService.doDelete(id);
		} else if(ids != null) {
			dtTagService.doRemove(ids);
		}
		return new SuccessMessage("删除成功");//没有需要返回的数据，就直接返回一条消息
	}
	

	
}
