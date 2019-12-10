package com.openjava.datatag.tagmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.log.domain.DtTagUpdateLog;
import com.openjava.datatag.log.service.DtTagUpdateLogService;
import com.openjava.datatag.tagcol.service.DtCooperationService;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.dto.DtTagDTO;
import com.openjava.datatag.tagmanage.query.DtTagDBParam;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import com.openjava.datatag.log.repository.DtTagUpdateLogRepository;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.utils.IpUtil;
import com.openjava.datatag.utils.VoUtils;
import com.openjava.datatag.utils.tree.TagDTOTreeNode;
import com.openjava.datatag.utils.tree.TagDTOTreeNodeShow;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.SuccessMessage;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.sequence.SequenceService;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * DT_TAG业务层
 * @author lch
 *
 */
@Service
@Transactional
public class DtTagServiceImpl implements DtTagService {
	
	@Resource
	private DtTagRepository dtTagRepository;//DT_TAG数据库访问层

	@Resource
	private DtTagUpdateLogService dtTagUpdateLogService;//DT_TAG_UPDATE_LOG业务层接口
	@Resource
	DtTaggingModelService dtTaggingModelService;//标签模型业务层接口
	@Resource
	private DtTagGroupService dtTagGroupService;//DT_TAG_GROUP表签组业务层接口
	@Resource
	private DtCooperationService dtCooperationService;//tagcol协作打标业务层接口
	@Resource
	private AuditComponet auditComponet;//审计组件

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public Page<DtTag> query(DtTagDBParam params, Pageable pageable){
		Page<DtTag> pageresult = dtTagRepository.query(params, pageable);//获取数据
		return pageresult;
	}

	/**
	 *
	 * @param params
	 * @param pageable
	 * @return
	 */
	public List<DtTag> queryDataOnly(DtTagDBParam params, Pageable pageable){
		return dtTagRepository.queryDataOnly(params, pageable);//获取数据
	}

	/**
	 * 根据主键获取标签
	 * @param id
	 * @return
	 */
	public DtTag get(Long id) {
		Optional<DtTag> o = dtTagRepository.findById(id);//获取标签
		if(o.isPresent()) {
			DtTag m = o.get();//
			return m;
		}
		System.out.println("找不到记录DtTag："+id);
		return null;
	}

	/**
	 * 保存
	 * @param m
	 * @return
	 */
	public DtTag doSave(DtTag m) {
		return dtTagRepository.saveAndFlush(m);//保存
	}

	/**
	 *
	 * @param id
	 * @param now
	 */
	public void doSoftDeleteByTagsID(Long id, Date now){
		// TODO: 2019/9/16  删除标签组时删除画像
		List<Long> ids = findIdsByTagsId(id);
		dtTaggingModelService.stopModelByColIds(ids);//停止模型删除画像
		dtTagRepository.doSoftDeleteByTagsID(id,now);//
	}

	/**
	 *
	 * @param tagsId
	 * @return
	 */
	public List<DtTag> findByTagsId(Long tagsId){
		//查询所有未删除的标签list
		return dtTagRepository.findByTagsIdAndIsDeleted(tagsId, Constants.PUBLIC_NO);//
	}

	/**
	 * 根据标签组ID获取(标签树)
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TagDTOTreeNodeShow getTree(Long id)throws Exception{
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取用户信息
		DtTagGroup db = dtTagGroupService.get(id);//获取标签组
		if (db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) {
			throw new APIException(MyErrorConstants.TAG_GROUP_NOT_FOUND, "无此标签组或已被删除");
		}
		//查找当前用户是否配置有该标签组的协作权限
		Long cooUserTagGroupCount = dtCooperationService.findCooUserTagGroup(VoUtils.toLong(userInfo.getUserId()), id);
		//自己的和共享的标签组可以查看
		if (userInfo.getUserId().equals(db.getCreateUser().toString()) || db.getIsShare().equals(Constants.PUBLIC_YES) || cooUserTagGroupCount > 0) {
			List<DtTag> tagList = this.findByTagsId(id);//根据标签组id获取所属标签
			DtTagDTO root = new DtTagDTO();//创建
			root.setId(TagDTOTreeNode.ROOT_ID);//设置id
			TagDTOTreeNode treeNode = new TagDTOTreeNode(TagDTOTreeNode.toDtTagDTO(tagList), root);//初始化树结构
			TagDTOTreeNodeShow treeNodeShow = new TagDTOTreeNodeShow(treeNode);//构造造成前端需要的
			AuditLogVO vo = new AuditLogVO();//审计日志
			vo.setType(2L);//数据查询
			vo.setOperationService("标签与画像");//必传
			vo.setOperationModule("标签管理");//必传
			vo.setFunctionLev1("共享标签组");//必传
			vo.setFunctionLev2("查看");//必传
			vo.setRecordId(id+"");//
			auditComponet.saveAuditLog(vo);//保存审计日志
			return treeNodeShow;
		} else {
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY, "无权限查看");//
		}

	}

	/**
	 * 根据父标签id获取标签
	 * @param pId
	 * @return
	 */
	public List<DtTag> findByPreaTagId(Long pId){
		return dtTagRepository.findByPreaTagIdAndIsDeleted(pId,Constants.PUBLIC_NO);//根据父标签id获取标签
	}

	/**
	 *
	 * @param body
	 * @param userId
	 * @param ip
	 * @return
	 */
	public DtTag doNew(DtTag body,Long userId,String ip){
		String modifyContent = JSONObject.toJSONString(body);//body变为json格式
		//新增，记录创建时间等
		SequenceService ss = ConcurrentSequence.getInstance();
		body.setId(ss.getSequence());//设置主键(请根据实际情况修改)
		body.setIsDeleted(Constants.PUBLIC_NO);//非删除状态
		body.setIsNew(true);//执行insert
		Date now = new Date();//当前时间
		body.setCreateTime(now);//设置创建时间
		body.setModifyTime(now);//设置修改时间
		DtTag db = doSave(body);//保存
		dtTagUpdateLogService.loggingNew(modifyContent,db,userId,ip);//记录日志
		return db;
	}

	/**
	 * 修改标签
	 * @param body
	 * @param db
	 * @param userId
	 * @param ip
	 * @return
	 */
	public DtTag doUpdate(DtTag body,DtTag db,Long userId, String ip){
		String oldContent = JSONObject.toJSONString(db);//把db转化为json格式
		String modifyContent = JSONObject.toJSONString(body);//把body转化为json格式
		//不允许修改父节点，层级和创建时间
		body.setPreaTagId(null);
		body.setCreateTime(null);
		body.setLvl(null);
		body.setModifyTime(new Date());
		MyBeanUtils.copyPropertiesNotBlank(db, body);//复制对象
		db.setIsNew(false);//执行update
		db = doSave(db);//保存
		dtTagUpdateLogService.loggingUpdate(modifyContent,oldContent,db,userId,ip);//记录日志
		return db;
	}

	/**
	 * 软删除
	 * @param db
	 * @param userId
	 * @param ip
	 * @throws Exception
	 */
	public void doSoftDeleteByDtTag(DtTag db,Long userId,String ip)throws Exception{
		// TODO: 2019/9/16   删除标签时删除画像
		List<Long> ids =  findAllIdsByTagId(db.getId());//根据标签id获取整颗数的节点id
		dtTaggingModelService.stopModelByColIds(ids);//停止模型删除画像
		Date now = new Date();//当前时间
		//先删除子节点
		doSoftDeleteByRootID(db.getId(),now);
		//再删除本节点
		db.setIsDeleted(Constants.PUBLIC_YES);//设置是否删除
		db.setModifyTime(now);//设置修改时间
		doSave(db);//保存

		AuditLogVO vo = new AuditLogVO();//审计日志
		vo.setType(1L);//管理操作
		vo.setOperationService("标签与画像");//必传
		vo.setOperationModule("标签管理");//必传
		vo.setFunctionLev1("编辑");//必传
		vo.setFunctionLev2("删除");//必传
		vo.setRecordId(db.getId()+"");
		auditComponet.saveAuditLog(vo);//保存审计日志
		dtTagUpdateLogService.loggingDelete(db,userId,ip);//保存删除日志
	}

	/**
	 *
	 * @param id
	 * @param now
	 */
	public void doSoftDeleteByRootID(Long id,Date now){
		//-为了减少数据库io，先查询该节点下的所有节点的父节点集，在逐层伪删除
		List<Long> pIds = dtTagRepository.findPIdByRootId(id);
		for (Long pId : pIds){
		    dtTagRepository.doSoftDeleteByPreaTagId(pId,now);//软删除
        }
	}

	/**
	 *
	 * @param tagIds
	 * @return
	 */
	public List<DtTag> findByTagIds(List<Long> tagIds){
		return dtTagRepository.findByTagIds(tagIds);//
	}

	/**
	 * 根据标签id获取整颗数的节点id
	 */
	public List<Long> findAllIdsByTagId(Long tagId){
		List<Long> tagIds = dtTagRepository.findAllIdsByRootId(tagId);//根据标签id获取整颗数的节点id
		return tagIds;
	}
	/**
	 * 根据标签组获取节点ID
	 */
	public List<Long> findIdsByTagsId( Long tagsId){
		return dtTagRepository.findIdsByTagsId(tagsId);
	}
	/**
	 * 获取标签所在的id路径
	 * 例如:标签的id：3，父级id：2,2的父级id:1,则路径为：1,2,3
	 */
	public String getIdpPath(Long tagId){
		String idpath = null;
		DtTag tag = get(tagId);//获取标签
		if (tag != null){
			if (tag.getPreaTagId()!=null){
				idpath = this.getIdpPath(tag.getPreaTagId());//获取标签树路径
				idpath += ","+tag.getId();
			}else {
				DtTagGroup tagGroup = dtTagGroupService.get(tag.getTagsId());//获取标签组
				idpath = tagGroup.getId()+","+tag.getId();
			}

		}
		return idpath;
	}

	/**
	 * 修改标签
	 * @param body
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public SuccessMessage doSaveOrEdit(DtTag body,String ip)throws Exception{
		//修改，记录更新时间等
		BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取用户信息
		Long userId = Long.valueOf(userInfo.getUserId());//获取用户id
//		String ip = IpUtil.getRealIP(request);
		DtTagGroup tagGroup = dtTagGroupService.get(body.getTagsId());//获取表签组
		if (userInfo.getUserId().equals(tagGroup.getCreateUser().toString())) {
			String idpath = null;
			if (body.getIsNew() == null || body.getIsNew()) {
				//新建标签
				body = this.doNew(body, userId, ip);
				tagGroup.setModifyTime(new Date());//修改日期
				dtTagGroupService.doSave(tagGroup);//保存标签组
				idpath = this.getIdpPath(body.getId());//标签路径
				body.setIdPath(idpath);//设置标签路径
				body = this.doSave(body);//保存
				AuditLogVO vo = new AuditLogVO();//新建审计日志
				vo.setType(1L);//管理操作
				vo.setOperationService("标签与画像");//必传
				vo.setOperationModule("标签管理");//必传
				vo.setFunctionLev1("编辑");//必传
				vo.setFunctionLev2("添加标签");//必传
				vo.setDataAfterOperat(JSONObject.toJSONString(body));//body转化为json格式
				vo.setRecordId(body.getId()+"");//记录id
				auditComponet.saveAuditLog(vo);//保存审计日志
				return new SuccessMessage("新建成功");
			} else {
				DtTag db = this.get(body.getId());//获取标签
				String beforeUpdataJson = JSONObject.toJSONString(db);//db转化为json格式
				if ((db == null || db.getIsDeleted().equals(Constants.PUBLIC_YES)) && body.getIsNew()) {
					throw new APIException(MyErrorConstants.TAG_NOT_FOUND, "无此标签或已被删除");
				}
				if (body.getIsDeleted() != null && body.getIsDeleted().equals(Constants.PUBLIC_YES)) {
					throw new APIException(MyErrorConstants.PUBLIC_ERROE, "请不要用此方法进行删除操作，请用DELETE方法");
				}
				body = this.doUpdate(body, db, userId, ip);//更新标签
				idpath = this.getIdpPath(body.getId());//获取标签树路径
				body.setIdPath(idpath);//设置标签数据路径
				body = this.doSave(body);//保存
				tagGroup.setModifyTime(new Date());//设置标签组修改日期
				dtTagGroupService.doSave(tagGroup);//保存标签组
				AuditLogVO vo = new AuditLogVO();//成绩审计日志
				vo.setType(1L);//管理操作
				vo.setOperationService("标签与画像");//必传
				vo.setOperationModule("标签管理");//必传
				vo.setFunctionLev1("编辑");//必传
				vo.setFunctionLev2("保存");//必传
				vo.setDataBeforeOperat(beforeUpdataJson);//修改之前的数据（json格式）
				vo.setDataAfterOperat(JSONObject.toJSONString(body));//修改之后的数据（json格式）
				vo.setRecordId(body.getId()+"");
				auditComponet.saveAuditLog(vo);//保存审计日志
				return new SuccessMessage("修改成功");
			}

		} else {
			throw new APIException(MyErrorConstants.PUBLIC_NO_AUTHORITY, "无权限修改");
		}
	}
}
