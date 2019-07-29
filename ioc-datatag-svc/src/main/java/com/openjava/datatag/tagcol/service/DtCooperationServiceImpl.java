package com.openjava.datatag.tagcol.service;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.commons.utils.VoUtils;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagcol.domain.DtCooTagcolLimit;
import com.openjava.datatag.tagcol.domain.DtTagmCooLog;
import com.openjava.datatag.tagcol.dto.*;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.query.DtTagGroupDBParam;
import com.openjava.datatag.tagmanage.service.DtTagGroupService;
import com.openjava.datatag.tagmodel.domain.DtTaggingModel;
import com.openjava.datatag.tagmodel.dto.DtTaggingModelDTO;
import com.openjava.datatag.tagmodel.service.DtTaggingModelService;
import com.openjava.datatag.utils.TimeUtil;
import com.openjava.datatag.utils.user.service.SysUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.common.bean.MyBeanUtils;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.util.DateFormater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private DtCooperationRepository dtCooperationRepository;

    @Resource
    private DtCooTagcolLimitService dtCooTagcolLimitService;
    @Resource
    private DtTagmCooLogService dtTagmCooLogService;
    @Resource
    private DtTaggingModelService dtTaggingModelService;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private DtTagGroupService dtTagGroupService;


    public Page<DtCooperation> query(DtCooperationDBParam params, Pageable pageable) {
        Page<DtCooperation> pageresult = dtCooperationRepository.query(params, pageable);
        return pageresult;
    }

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
        if(userId==null){
            userId= currentuserId;
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
            dto.setDataSetId(VoUtils.toLong(map.get("DATA_SET_ID")));
            dto.setDataSetName(map.get("DATA_SET_NAME"));
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
        if(userId==null){
            userId= currentuserId;
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
            dto.setShowCol(map.get("SHOW_COL"));
            dto.setSourceCol(map.get("SOURCE_COL"));
            dto.setSourceDataType(map.get("SOURCE_DATA_TYPE"));
            dto.setTaggingModelId(VoUtils.toLong(map.get("TAGGING_MODEL_ID")));
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 描述：根据协作模型Id及协作打标字段名称查询当前用户的协作打标的标签组记录
     */
    public List<DtTagGroup> findCurrentUserTagGroup(Long modelId, String colField) {
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

    public DtCooperation get(Long id) {
        Optional<DtCooperation> o = dtCooperationRepository.findById(id);
        if (o.isPresent()) {
            DtCooperation m = o.get();
            return m;
        }
        System.out.println("找不到记录DtCooperation：" + id);
        return null;
    }

    public DtCooperation doSave(DtCooperation m) {
        return dtCooperationRepository.save(m);
    }

    /**
     * 描述：新增、修改协作用户
     */
    public void doCoolListSave(DtCooperationListDTO list) throws Exception {
        String reqParams = JSONObject.toJSONString(list);
        DtTagmCooLog log = new DtTagmCooLog();
        BaseUserInfo userInfo = (BaseUserInfo) SsoContext.getUser();
        Long userId = Long.valueOf(userInfo.getUserId());
        Long taggmId=list.getTaggmId();
        for (CooperationDTO req : list.getCooperaList()) {
            List<DtCooTagcolLimitDTO> colLimit = req.getCooTagcolLimitList();
            DtTaggingModel tag = dtTaggingModelService.get(taggmId);

            if (CollectionUtils.isEmpty(colLimit)) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "cooTagcolLimitList参数必传");
            }
            if (tag == null) {
                throw new APIException(MyErrorConstants.PUBLIC_ERROE, "查无此数据,taggmId参数错误");
            }
            //新增和修改
            DtCooperation col = get(req.getId());
            if (col != null) {
                col.setTaggmId(taggmId);
                col.setCooUser(req.getCooUser());
                col.setIsNew(false);
                col.setModifyTime(new Date());
                //MyBeanUtils.copyPropertiesNotNull(col,req);
                //EntityClassUtil.dealModifyInfo(col,userInfo);
                col = dtCooperationRepository.save(col);
            } else {
                col = new DtCooperation();
                col.setTaggmId(taggmId);
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
            throw new APIException(MyErrorConstants.PUBLIC_ERROE, "cooTagcolLimitList参数必传");
        }
        if (tag == null) {
            throw new APIException(MyErrorConstants.PUBLIC_ERROE, "查无此数据,taggmId参数错误");
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

    public void doDelete(Long id) {
        //根据Id删除协作成员表记录
        dtCooperationRepository.deleteById(id);
        //根据协作成员Id删除协作字段表记录
        dtCooTagcolLimitService.deleteBycoolId(id);
    }

    public void doRemove(String ids) {
        String[] items = ids.split(",");
        for (int i = 0; i < items.length; i++) {
            dtCooperationRepository.deleteById(new Long(items[i]));
        }
    }
}
