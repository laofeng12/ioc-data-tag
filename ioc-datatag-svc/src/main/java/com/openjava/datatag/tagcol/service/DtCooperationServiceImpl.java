package com.openjava.datatag.tagcol.service;

import java.util.*;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.VoUtils;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.component.PlatformCompent;
import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.domain.DtTagmCooLog;
import com.openjava.datatag.tagcol.dto.*;
import com.openjava.datatag.tagcol.query.DtCooTagcolLimitDBParam;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmodel.domain.DtSetCol;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.query.DtTaggingModelDBParam;
import com.openjava.datatag.tagmodel.service.DtSetColService;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.user.domain.SysUser;
import com.openjava.datatag.user.repository.SysUserRepository;
import com.openjava.datatag.user.service.SysUserService;
import com.openjava.datatag.utils.TimeUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.core.spring.data.JpaMultiDynamicQueryDAO;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.ui.bootstrap.TablePage;
import org.ljdp.ui.bootstrap.TablePageImpl;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openjava.datatag.tagcol.domain.DtCooperation;
import com.openjava.datatag.tagcol.query.DtCooperationDBParam;
import com.openjava.datatag.tagcol.repository.DtCooperationRepository;

/**
 * tagcol业务层
 *
 * @author hyq
 */
@Service
@Transactional
public class DtCooperationServiceImpl implements DtCooperationService {


    @Resource
    private DtCooperationRepository dtCooperationRepository;//

    @Resource
    private DtCooTagcolLimitService dtCooTagcolLimitService;//
    @Resource
    private DtTagmCooLogService dtTagmCooLogService;//
    @Resource
    private DtTaggingModelService dtTaggingModelService;//
    @Resource
    private SysUserService sysUserService;//
    @Resource
    private DtTagGroupService dtTagGroupService;//
    //首先初始化JpaMultiDynamicQueryDAO对象。(暂时只支持JPA)
    private EntityManager em;
    private JpaMultiDynamicQueryDAO dao;//
    @Resource
    private AuditComponet auditComponet;//
    @Resource
    private DtSetColService dtSetColService;//
    @Resource
    private PlatformCompent platformCompent;//
    @Resource
    private SysUserRepository sysUserRepository;
    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
        dao = new JpaMultiDynamicQueryDAO(em);
    }

    /**
     *
     * @param item
     * @param pageable
     * @return
     */
    public Page<?>findPageUserModelByUserId(DtCooperationDBParam item, Pageable pageable){
        return dtCooperationRepository.findPageUserModelByUserId(item.getEq_cooUser(),item.getKeyWord(),item.getEq_taggmId(),item.getRunState(),pageable);
    }
    /**
     * 描述：注意Sql与Hql是不一样的，传的查询语句是Hql的
     */
    public Page<?> queryShopItemAndProd(DtCooperationDBParam itemParams, DtTaggingModelDBParam prodPrams, Pageable pageable) {
        //先定义查询对象关联的表
        prodPrams.setTableAlias("t");
        itemParams.setTableAlias("o");

        //params.addQueryCondition(fieldB, "_gt_", value1);
        //编写查询HQL的主部分（不用写查询条件）
        //执行查询
        String multiHql = "select t,o from DtTaggingModel t, DtCooperation o,SysUser u where u.userid=t.modifyUser and t.taggingModelId=o.taggmId and t.isDeleted=0 ";
//        QueryParamsUtil.dealLike(prodPrams);//要用些方法转化like的查询条件才可以模糊查询，不然只能全匹配查询
        if (StringUtils.isNotBlank(itemParams.getKeyWord())){
//            SysUser u = sysUserService.findByFullname(itemParams.getKeyWord());
//            if (u!=null) {
//                multiHql+= " and (t.modifyUser ="+u.getUserid() +" or t.modelName like '%"+itemParams.getKeyWord()+"%')";
//            }else{
                multiHql+= " and ( u.fullname like '%"+itemParams.getKeyWord()+"%' or t.modelName like '%"+itemParams.getKeyWord()+"%')";
//            }
        }
        if(itemParams.getRunState()!=null){
            multiHql+= " and o.state = "+itemParams.getRunState();
        }
        Pageable mypage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("o.modifyTime")));
        Page<?> dbresult = dao.query(multiHql, mypage, prodPrams, itemParams);

        AuditLogVO vo = new AuditLogVO();//审计日志
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("模型部署");//必传
        vo.setFunctionLev1("协作模型");//必传
        vo.setFunctionLev2("查询");//必传
        auditComponet.saveAuditLog(vo);//保存审计日志
        return dbresult;
    }

    /**
     *
     * @param params
     * @param pageable
     * @return
     */
    public Page<DtCooperation> query(DtCooperationDBParam params, Pageable pageable) {
        Page<DtCooperation> pageresult = dtCooperationRepository.query(params, pageable);
        return pageresult;
    }

    /**
     *
     * @param params
     * @param pageable
     * @return
     */
    public List<DtCooperation> queryDataOnly(DtCooperationDBParam params, Pageable pageable) {
        return dtCooperationRepository.queryDataOnly(params, pageable);
    }

    /**
     * 描述：根据用户ID查找该用户的协作模型记录
     */
    public List<DtCooperationModelDTO> findUserModelByUserId(Long userId) {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long currentuserId = Long.valueOf(userInfo.getUserId());
        //userId不传时默认查询当前用户
        if (userId == null) {
            userId = currentuserId;
        }
        List<Map<String, String>> result = dtCooperationRepository.findUserModelByUserId(userId);
        List<DtCooperationModelDTO> dtoList = new ArrayList<>();
        for (Map<String, String> map : result) {
            DtCooperationModelDTO dto = new DtCooperationModelDTO();
            Long createuserId = VoUtils.toLong(map.get("CREATE_USER"));
            Long modifyuserId = VoUtils.toLong(map.get("MODIFY_USER"));
            Long coouserId = VoUtils.toLong(map.get("COO_USER"));
            dto.setCreateUser(createuserId);
            dto.setModifyUser(modifyuserId);
            dto.setCooUser(coouserId);
            if (createuserId != null) {
                if (sysUserService.get(createuserId) != null) {
                    dto.setCreateUserName(sysUserService.get(createuserId).getFullname());
                }
            }
            if (modifyuserId != null) {
                if (sysUserService.get(modifyuserId) != null) {
                    dto.setModifyUserName(sysUserService.get(modifyuserId).getFullname());
                }
            }
            if (coouserId != null) {
                if (sysUserService.get(coouserId) != null) {
                    dto.setCooUserName(sysUserService.get(coouserId).getFullname());
                }
            }
            if (StringUtils.isNotBlank(map.get("CREATE_TIME"))) {
                Date createdate = TimeUtil.StrToDate(map.get("CREATE_TIME"));
                dto.setCreateTime(createdate);
            }
            if (StringUtils.isNotBlank(map.get("MODIFY_TIME"))) {
                Date modifydate = TimeUtil.StrToDate(map.get("MODIFY_TIME"));
                dto.setModifyTime(modifydate);
            }
            if (StringUtils.isNotBlank(map.get("START_TIME"))) {
                Date startydate = TimeUtil.StrToDate(map.get("START_TIME"));
                dto.setStartTime(startydate);
            }
            dto.setCycle(map.get("CYCLE"));
            dto.setCycleEnum(VoUtils.toLong(map.get("CYCLE_ENUM")));
            dto.setResourceId(VoUtils.toLong(map.get("DATA_SET_ID")));
            dto.setResourceName(map.get("DATA_SET_NAME"));
            dto.setDataTableName(map.get("TAGGING_TABLE_NAME"));
            dto.setIsDeleted(VoUtils.toLong(map.get("IS_DELETED")));
            dto.setModelDesc(map.get("MODEL_DESC"));
            dto.setModelName(map.get("MODEL_NAME"));
            dto.setPkey(map.get("P_KEY"));
            dto.setRunState(VoUtils.toLong(map.get("RUN_STATE")));
            dto.setTaggingModelId(VoUtils.toLong(map.get("TAGGING_MODEL_ID")));
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 描述：根据用户ID及协作模型ID查找该用户所在的模型里配置的字段记录，并用IsCooField标注该字段是否该用户的协作打标字段
     */
    public List<DtCooperationSetCol> findUserModelCooField(Long userId, Long modelId) {
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long currentuserId = Long.valueOf(userInfo.getUserId());
        //userId不传时默认查询当前用户
        if (userId == null) {
            userId = currentuserId;
        }
        List<Map<String, String>> result = dtCooperationRepository.findUserModelCooField(userId, modelId);
        List<DtCooperationSetCol> dtoList = new ArrayList<>();
        for (Map<String, String> map : result) {
            DtCooperationSetCol dto = new DtCooperationSetCol();
            Long createuserId = VoUtils.toLong(map.get("CREATE_USER"));
            Long modifyuserId = VoUtils.toLong(map.get("MODIFY_USER"));
            Long coouserId = VoUtils.toLong(map.get("COO_USER"));
            dto.setCreateUser(createuserId);
            dto.setModifyUser(modifyuserId);
            dto.setCooUser(coouserId);
            if (createuserId != null) {
                if (sysUserService.get(createuserId) != null) {
                    dto.setCreateUserName(sysUserService.get(createuserId).getFullname());
                }
            }
            if (modifyuserId != null) {
                if (sysUserService.get(modifyuserId) != null) {
                    dto.setModifyUserName(sysUserService.get(modifyuserId).getFullname());
                }
            }
            if (coouserId != null) {
                if (sysUserService.get(coouserId) != null) {
                    dto.setCooUserName(sysUserService.get(coouserId).getFullname());
                }
            }
            if (StringUtils.isNotBlank(map.get("CREATE_TIME"))) {
                Date createdate = TimeUtil.StrToDate(map.get("CREATE_TIME"));
                dto.setCreateTime(createdate);
            }
            if (StringUtils.isNotBlank(map.get("MODIFY_TIME"))) {
                Date modifydate = TimeUtil.StrToDate(map.get("MODIFY_TIME"));
                dto.setModifyTime(modifydate);
            }
            dto.setColId(VoUtils.toLong(map.get("COL_ID")));
            dto.setIsCooField(VoUtils.toLong(map.get("ISCOOFIELD")));
            dto.setIsDeleted(VoUtils.toLong(map.get("IS_DELETED")));
            dto.setIsMarking(VoUtils.toLong(map.get("IS_MARKING")));
            dto.setIsPKey(VoUtils.toLong(map.get("IS_P_KEY")));
            dto.setIsSource(VoUtils.toLong(map.get("IS_SOURCE")));
            dto.setId(VoUtils.toLong(map.get("ID")));
            dto.setUseTagGroup(VoUtils.toLong(map.get("USE_TAG_GROUP")));
            dto.setCooFieldId(VoUtils.toLong(map.get("COOFIELDID")));
            dto.setShowCol(map.get("SHOW_COL"));
            dto.setSourceCol(map.get("SOURCE_COL"));
            dto.setComment(map.get("COL_COMMENT"));
            dto.setSourceDataType(map.get("SOURCE_DATA_TYPE"));
            dto.setTagColId(VoUtils.toLong(map.get("TAG_COL_ID")));
            dto.setTaggingModelId(VoUtils.toLong(map.get("TAGGING_MODEL_ID")));
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 描述：根据协作模型Id及协作打标字段名称查询当前用户的协作打标的标签组记录
     */
    public List<DtTagGroup> findCurrentUserTagGroup(Long modelId, Long colField) {
        List<DtTagGroup> lst = new ArrayList<>();
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long userId = Long.valueOf(userInfo.getUserId());
        DtTaggingModel model = dtTaggingModelService.get(modelId);
        //如果模型的创建者是当前用户时，要拿该用户创建的所有标签组
        if (model.getCreateUser().equals(userId)) {
            lst = dtTagGroupService.getMyTagGroup(userId);
        } else {
            List<Map<String, String>> result = dtCooperationRepository.findCurrentUserTagGroup(userId, modelId, colField);
            for (Map<String, String> map : result) {
                DtTagGroup group = new DtTagGroup();
                if (StringUtils.isNotBlank(map.get("CREATE_TIME"))) {
                    Date createdate = TimeUtil.StrToDate(map.get("CREATE_TIME"));
                    group.setCreateTime(createdate);
                }
                if (StringUtils.isNotBlank(map.get("MODIFY_TIME"))) {
                    Date modifydate = TimeUtil.StrToDate(map.get("MODIFY_TIME"));
                    group.setModifyTime(modifydate);
                }
                Long createuserId = VoUtils.toLong(map.get("CREATE_USER"));
                group.setCreateUser(createuserId);
                group.setIsDeleted(VoUtils.toLong(map.get("IS_DELETED")));
                group.setPopularity(VoUtils.toLong(map.get("POPULARITY")));
                group.setId(VoUtils.toLong(map.get("ID")));
                group.setIsShare(VoUtils.toLong(map.get("IS_SHARE")));
                group.setSynopsis(map.get("SYNOPSIS"));
                group.setTagsName(map.get("TAGS_NAME"));

                lst.add(group);
            }
        }
        return lst;
    }

    /**
     * 描述：根据协作用户Id和标签组Id查询  协作限制打标字段表 存在记录总数
     */
    public Long findCooUserTagGroup(Long userId, Long tagGroupId) {
        return dtCooperationRepository.findCooUserTagGroup(userId, tagGroupId);

    }

    /**
     *
     * @param id
     * @return
     */
    public DtCooperation get(Long id) {
        Optional<DtCooperation> o = dtCooperationRepository.findById(id);
        if (o.isPresent()) {
            DtCooperation m = o.get();
            return m;
        }
        System.out.println("找不到记录DtCooperation：" + id);
        return null;
    }

    /**
     *
     * @param m
     * @return
     */
    public DtCooperation doSave(DtCooperation m) {
        return dtCooperationRepository.save(m);
    }

    /**
     * 描述：新增、修改协作用户
     */
    public void doCoolListSave(List<DtCooperationListDTO> list) throws Exception {
        String reqParams = JSONObject.toJSONString(list);//请求数据转化为json格式
        DtTagmCooLog log = new DtTagmCooLog();
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();//获取用户信息
        Long userId = Long.valueOf(userInfo.getUserId());//用户id
        for (DtCooperationListDTO dtos : list) {
            boolean submitWorkOrderFlag = false;//是否需要提交工单
            boolean cancleType = false;//是否需要取消工单
            if (dtos.getTaggmId() == null) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "taggmId参数不能为空");
            }
            Long taggmId = dtos.getTaggmId();

            if (dtos.getCooUser() == null) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "cooUser参数不能为空");
            }
            List<DtCooTagcolLimitDTO> colLimit = dtos.getCooTagcolLimitList();
            DtTaggingModel tag = dtTaggingModelService.get(taggmId);
            if (tag == null) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "查无此数据,taggmId参数不能为空");
            }
            /*DtCooperationDBParam params = new DtCooperationDBParam();
            params.setEq_cooUser(dtos.getCooUser());
            params.setEq_taggmId(taggmId);
            Pageable pageable = PageRequest.of(0, 20000);//限制只能导出2w，防止内存溢出
            Page<DtCooperation> result = query(params, pageable);
            if (result.getTotalElements() > 0) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "该协作成员已存在");
            }*/
            //新增和修改
            DtCooperation col = new DtCooperation();

            if (dtos.getId() != null) {
                col = get(dtos.getId());
                col.setTaggmId(taggmId);
                col.setCooUser(dtos.getCooUser());
                col.setIsNew(false);
                col.setModifyTime(new Date());
                col.setState(Constants.DT_COOPERATION_NO);
                //MyBeanUtils.copyPropertiesNotNull(col,req);
                //EntityClassUtil.dealModifyInfo(col,userInfo);
                col = dtCooperationRepository.save(col);
            } else {
                col.setTaggmId(taggmId);
                col.setCooUser(dtos.getCooUser());
                col.setCreateUser(userId);
                col.setIsNew(true);
                col.setModifyTime(new Date());
                col.setCreateTime(new Date());
                //MyBeanUtils.copyPropertiesNotNull(col,req);
                //EntityClassUtil.dealCreateInfo(col,userInfo);
                col.setId(ConcurrentSequence.getInstance().getSequence());
                col = dtCooperationRepository.save(col);
            }

            if (CollectionUtils.isEmpty(colLimit)) {
//                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "请选择用户并添加协作打标字段");
                continue;
            }
            for (int i = 0; i < colLimit.size(); i++) {
                DtCooTagcolLimitDTO record = colLimit.get(i);
                DtCooTagcolLimit newcolLimit = new DtCooTagcolLimit();
                if (record.getCooId() == null) {
                    throw new APIException(MyErrorConstants.PUBLIC_ERROE, "cooTagcolLimitList参数的cooId为空或查无此标签");
                }
                if (!record.getCooId().equals(dtos.getId())) {
                    throw new APIException(MyErrorConstants.PUBLIC_ERROE, "cooTagcolLimitList参数的cooId错误");
                }
                if (record.getId() != null) {
                    if (record.getIsDelete()!=null && record.getIsDelete()==1){
                        dtCooTagcolLimitService.doDelete(record.getId());
                        cancleType = true;//取消字段的打标需要作废工单
                    }else {
                        newcolLimit = dtCooTagcolLimitService.get(record.getId());
                        boolean ischange = ischangeTagcol(newcolLimit,record);//判断是否有改动
                        if (ischange){
                            submitWorkOrderFlag = true;
                            cancleType = true;
                        }
                        newcolLimit.setUseTagGroup(record.getUseTagGroup());
                        newcolLimit.setTagColName(record.getTagColName());
                        newcolLimit.setCooId(col.getId());
                        newcolLimit.setTagColId(record.getTagColId());
                        newcolLimit.setIsNew(false);
                        newcolLimit = dtCooTagcolLimitService.doSave(newcolLimit);
                    }
                } else {
                    submitWorkOrderFlag = true;
                    newcolLimit.setIsNew(true);
                    newcolLimit.setUseTagGroup(record.getUseTagGroup());
                    newcolLimit.setTagColName(record.getTagColName());
                    newcolLimit.setCooId(col.getId());
                    newcolLimit.setTagColId(record.getTagColId());
                    newcolLimit.setId(ConcurrentSequence.getInstance().getSequence());
                    newcolLimit.setState(Constants.DT_COOP_TAGCOL_LIMMIT_NO);
                    newcolLimit = dtCooTagcolLimitService.doSave(newcolLimit);
                }

            }
            //取消工单
            if (cancleType){
                platformCompent.cancel(col.getId()+"",dtos.getCooUser()+"",userInfo.getUserId()+"");//取消工单
            }
            //添加协作成员时添加工单
            if (submitWorkOrderFlag){
                SysUser user =  sysUserRepository.getOne(dtos.getCooUser());
                //platformCompent.spUnifyWorkform(col.getId()+"",dtos.getCooUser()+"",user.getAccount());
                platformCompent.spUnifyWorkform(col.getId()+"",userInfo.getUserId(),dtos.getCooUser()+"",user.getAccount());

            }
        }
        AuditLogVO vo = new AuditLogVO();//
        vo.setType(1L);//管理操作
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("模型部署");//必传
        vo.setFunctionLev1("编辑");//必传
        vo.setFunctionLev2("添加成员");//必传
        vo.setRecordId(userId+"");
        auditComponet.saveAuditLog(vo);

    }

    /**
     * 判断协作字段是有有改动
     * @param db
     * @param record
     * @return
     */
    public boolean ischangeTagcol(DtCooTagcolLimit db,DtCooTagcolLimitDTO record){
        boolean ischange = false;
        if (!record.getUseTagGroup().equals(db.getUseTagGroup()) ) {
            ischange = true;//标签组更换需要取消工单
        }
        if (!record.getTagColId().equals(db.getTagColId())){
            ischange = true;//字段修改需要修改工单
        }
        return ischange;
    }

    /**
     * 描述：新增、修改协作用户
     */
    public void doCoolSave(DtCooperationDTO req) throws Exception {
        String reqParams = JSONObject.toJSONString(req);
        DtTagmCooLog log = new DtTagmCooLog();
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long userId = Long.valueOf(userInfo.getUserId());
        List<DtCooTagcolLimitDTO> colLimit = req.getCooTagcolLimitList();
        DtTaggingModel tag = dtTaggingModelService.get(req.getTaggmId());

        if (CollectionUtils.isEmpty(colLimit)) {
            throw new APIException(MyErrorConstants.PUBLIC_ERROE, "请选择用户并添加协作打标字段");
        }
        if (tag == null) {
            throw new APIException(MyErrorConstants.PUBLIC_ERROE, "查无此数据,taggmId参数不能为空");
        }
        //新增和修改
        DtCooperation col = get(req.getId());
        if (col != null) {
            col.setTaggmId(req.getTaggmId());
            col.setCooUser(req.getCooUser());
            col.setIsNew(false);
            col.setModifyTime(new Date());
            //MyBeanUtils.copyPropertiesNotNull(col,req);
            //EntityClassUtil.dealModifyInfo(col,userInfo);
            col = dtCooperationRepository.save(col);
        } else {
            col = new DtCooperation();
            col.setTaggmId(req.getTaggmId());
            col.setCooUser(req.getCooUser());
            col.setCreateUser(userId);
            col.setIsNew(true);
            col.setModifyTime(new Date());
            col.setCreateTime(new Date());
            //MyBeanUtils.copyPropertiesNotNull(col,req);
            //EntityClassUtil.dealCreateInfo(col,userInfo);
            col.setId(ConcurrentSequence.getInstance().getSequence());
            col = dtCooperationRepository.save(col);
        }
        for (int i = 0; i < colLimit.size(); i++) {
            DtCooTagcolLimitDTO record = colLimit.get(i);
            DtCooTagcolLimit newcolLimit = dtCooTagcolLimitService.get(record.getId());
            if (record.getCooId() == null) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "cooTagcolLimitList参数的cooId为空或查无此标签");
            }
            if (!record.getCooId().equals(req.getId())) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "cooTagcolLimitList参数的cooId错误");
            }
            if (newcolLimit != null) {
                newcolLimit.setUseTagGroup(record.getUseTagGroup());
                newcolLimit.setTagColName(record.getTagColName());
                newcolLimit.setCooId(col.getId());
                newcolLimit.setIsNew(false);
                newcolLimit = dtCooTagcolLimitService.doSave(newcolLimit);
            } else {
                newcolLimit = new DtCooTagcolLimit();
                newcolLimit.setIsNew(true);
                newcolLimit.setUseTagGroup(record.getUseTagGroup());
                newcolLimit.setTagColName(record.getTagColName());
                newcolLimit.setCooId(col.getId());
                newcolLimit.setId(ConcurrentSequence.getInstance().getSequence());
                newcolLimit = dtCooTagcolLimitService.doSave(newcolLimit);
            }

        }
    }

    /**
     *
     * @param id
     */
    public void doDelete(Long id){
        //取消协助工单
        DtCooperation dtCooperation = dtCooperationRepository.getOne(id);
        platformCompent.cancel(dtCooperation.getId()+"",dtCooperation.getCooUser()+"",dtCooperation.getCreateUser()+"");
        //根据Id删除协作成员表记录
        dtCooperationRepository.deleteById(id);
        //根据协作成员Id删除协作字段表记录
        dtCooTagcolLimitService.deleteBycoolId(id);
    }

    /**
     *
     * @param ids
     */
    public void doRemove(String ids) {
        String[] items = ids.split(",");
        for (int i = 0; i < items.length; i++) {
            doDelete(new Long(items[i]));//
        }
    }

    /**
     *
     * @param params
     * @param pageable
     * @return
     * @throws Exception
     */
    public TablePage<DtCooperationDTO> doSearch(DtCooperationDBParam params, Pageable pageable) throws Exception{
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long currentuserId = Long.valueOf(userInfo.getUserId());
        if (params.getEq_createUser() == null) {
            params.setEq_createUser(currentuserId);
        }
        List<DtCooperationDTO> dtoList = new ArrayList<>();
        Page<DtCooperation> result = this.query(params, pageable);

        DtCooTagcolLimitDBParam limitParams = new DtCooTagcolLimitDBParam();
        for (DtCooperation opera : result) {
            DtCooperationDTO dto = new DtCooperationDTO();
            dto.setId(opera.getId());
            dto.setTaggmId(opera.getTaggmId());
            dto.setCooUser(opera.getCooUser());
            dto.setCreateUser(opera.getCreateUser());
            dto.setCreateTime(opera.getCreateTime());
            dto.setModifyTime(opera.getModifyTime());
            dto.setCooUserName(sysUserService.get(opera.getCooUser()).getFullname());
            dto.setCreateUserName(sysUserService.get(dto.getCreateUser()).getFullname());
            List<DtCooTagcolLimit> results = dtCooTagcolLimitService.findByColId(opera.getId());
            List<DtCooTagcolLimitDTO> limtdtoList = new ArrayList<>();
            for (DtCooTagcolLimit limtdto : results) {
                DtCooTagcolLimitDTO ldto = new DtCooTagcolLimitDTO();
                ldto.setId(limtdto.getId());
                ldto.setCooId(dto.getId());
                ldto.setTagColName(limtdto.getTagColName());
                ldto.setUseTagGroup(limtdto.getUseTagGroup());
                limtdtoList.add(ldto);
            }
            dto.setCooTagcolLimitList(limtdtoList);
            dtoList.add(dto);
        }
        Page<DtCooperationDTO> showResult = new PageImpl<>(dtoList, pageable, dtoList.size());
        return new TablePageImpl<>(showResult);
    }

    /**
     *
     * @param id
     * @return
     */
    public ColListDTO getColList(Long id){
        List<DtCooTagcolLimit> results = dtCooTagcolLimitService.findByColId(id);
        String modelName =  dtCooperationRepository.getModelNameBycooId(id);
        ColListDTO colListDTO = new ColListDTO();
        colListDTO.setModelName(modelName);
        List<ColDTO> list = new ArrayList<>();
        for (DtCooTagcolLimit limit:results) {
            ColDTO col = new ColDTO();
            col.setTagColName(limit.getTagColName());//标签名称
            DtSetCol dtSetCol =  dtSetColService.get(limit.getTagColId());
            col.setColTye(dtSetCol.getSourceDataType());//标签类型
            DtTagGroup dtTagGroup = dtTagGroupService.get(limit.getUseTagGroup());//获取标签组
            col.setColGroupName(dtTagGroup.getTagsName());//标签组名称
            list.add(col);
        }
        colListDTO.setColList(list);
        return colListDTO;
    }
}
