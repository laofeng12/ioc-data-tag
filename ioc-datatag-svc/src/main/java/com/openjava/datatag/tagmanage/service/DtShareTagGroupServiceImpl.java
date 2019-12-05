package com.openjava.datatag.tagmanage.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.audit.auditManagement.component.AuditComponet;
import com.openjava.audit.auditManagement.vo.AuditLogVO;
import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.log.service.DtTaggChooseLogService;
import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.dto.DtTagDTO;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.repository.DtShareTagGroupRepository;
import com.openjava.datatag.tagmanage.repository.DtTagGroupRepository;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import com.openjava.datatag.utils.tree.TagDTOTreeNode;
import org.apache.commons.collections.CollectionUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.ljdp.component.user.BaseUserInfo;
import org.ljdp.secure.sso.SsoContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DtShareTagGroupServiceImpl implements DtShareTagGroupService{
    @Resource
    private DtShareTagGroupRepository dtShareTagGroupRepository;

    @Resource
    private DtTagGroupRepository dtTagGroupRepository;

    @Resource
    private DtTagRepository dtTagRepository;

    @Resource
    private DtTaggChooseLogService dtTaggChooseLogService;
    @Resource
    private DtTagService dtTagService;
    @Resource
    private AuditComponet auditComponet;

    public Page<DtShareTagGroup> findList(String searchKey, Pageable pageable)throws Exception{
        Page<DtShareTagGroup> result = dtShareTagGroupRepository.findList("%" + searchKey+ "%", pageable);
        if (CollectionUtils.isNotEmpty(result.getContent())){
            Long maxPopularity = dtTagGroupRepository.findMaxPopularityBytagsIdAAndIsDeletedAAndIsShare(Constants.PUBLIC_NO,Constants.PUBLIC_YES);
            for (DtShareTagGroup tgg: result){
                if (tgg.getPopularity()==null){
                    tgg.setPercentage(0L);
                }else {
                    BigDecimal big = new BigDecimal(tgg.getPopularity()).divide(new BigDecimal(DtTagGroupServiceImpl.getDenominator(maxPopularity)) ,2,BigDecimal.ROUND_UP).multiply(new BigDecimal(100));
                    tgg.setPercentage(big.longValueExact());
                }
            }

        }
        AuditLogVO vo = new AuditLogVO();
        vo.setType(2L);//数据查询
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签管理");//必传
        vo.setFunctionLev1("共享标签组");//必传
        vo.setFunctionLev2("查询");//必传
        auditComponet.saveAuditLog(vo);
        return result;
    }

    public void choose(Long id,Long userId,String ip) throws Exception {
        Optional<DtTagGroup> o = dtTagGroupRepository.findById(id);
        DtTagGroup tgg = null;
        if(o.isPresent()){
            tgg = o.get();
        }else{
            //正常状况这个异常不可能报,因为在控制层做过验证了
            throw new APIException(MyErrorConstants.TAG_GROUP_NOT_FOUND, "未找到该标签组");
        }
        Long newId = ConcurrentSequence.getInstance().getSequence();
        DtTagGroup newTgg = new DtTagGroup();
        newTgg.setIsNew(true);
        newTgg.setId(newId);
        newTgg.setTagsName(tgg.getTagsName()+"(选用)");
        newTgg.setIsShare(Constants.PUBLIC_NO);
        newTgg.setSynopsis(tgg.getSynopsis());
        newTgg.setPopularity(0L);
        newTgg.setCreateUser(userId);
        Date now = new Date();
        newTgg.setCreateTime(now);
        newTgg.setModifyTime(now);
        newTgg.setIsDeleted(Constants.PUBLIC_NO);
        newTgg = dtTagGroupRepository.save(newTgg);
        List<DtTag> tagList = dtTagRepository.findByTagsIdAndIsDeleted(id,Constants.PUBLIC_NO);
        DtTagDTO root = new DtTagDTO();
        root.setId(TagDTOTreeNode.ROOT_ID);
        TagDTOTreeNode tagTreeNode = new TagDTOTreeNode(TagDTOTreeNode.toDtTagDTO(tagList),root);
        //先序遍历标签树，新建保存一棵结构一样而id不同的树
        setNewIdAndSave(tagTreeNode,null,newId,now);

        //选用日志记录
        dtTaggChooseLogService.loggingChoose(id,newTgg,userId,ip);

        //热度增加-每天每人只能加一次
        Long c = dtTaggChooseLogService.countChooseToday(userId,id);
        if(c.equals(1L)){
           tgg.setPopularity(tgg.getPopularity()+1L);
           dtTagGroupRepository.save(tgg);
        }
        AuditLogVO vo = new AuditLogVO();
        vo.setType(1L);//管理操作
        vo.setOperationService("标签与画像");//必传
        vo.setOperationModule("标签管理");//必传
        vo.setFunctionLev1("共享标签组");//必传
        vo.setFunctionLev2("选用为我的");//必传
        vo.setDataAfterOperat(JSONObject.toJSONString(newTgg));
        auditComponet.saveAuditLog(vo);
    }

    private void setNewIdAndSave(TagDTOTreeNode tree, Long pId, Long tagsId, Date now){
        DtTagDTO root = tree.getTag();
        if(root.getId() == null || root.getId().equals(TagDTOTreeNode.ROOT_ID)){
            //整棵树的根节点不需要保存，第一层标签的父标签id设置为null
            for (TagDTOTreeNode cTree: tree.getChildrenNode()){
                setNewIdAndSave(cTree,null,tagsId,now);
            }
        }else {
            //子树的根节点和叶子节点(标签)进行保存
            Long newId = ConcurrentSequence.getInstance().getSequence();
            DtTag newRoot = new DtTag();
            newRoot.setId(newId);
            newRoot.setTagName(root.getTagName());
            newRoot.setSynopsis(root.getSynopsis());
            newRoot.setTagsId(tagsId);
            newRoot.setLvl(root.getLvl());
            newRoot.setCreateTime(now);
            newRoot.setModifyTime(now);
            newRoot.setIsDeleted(Constants.PUBLIC_NO);
            newRoot.setIsNew(true);
            newRoot.setPreaTagId(pId);
            newRoot = dtTagRepository.save(newRoot);
            newRoot.setIdPath(dtTagService.getIdpPath(newRoot.getId()));
            //子标签的父标签id设置为新的标签的id
            for (TagDTOTreeNode cTree: tree.getChildrenNode()){
                setNewIdAndSave(cTree,newRoot.getId(),tagsId,now);
            }
        }
    }

}
