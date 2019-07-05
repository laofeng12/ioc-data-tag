package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.common.Constants;
import com.openjava.datatag.common.MyErrorConstants;
import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.dto.DtTagDTO;
import com.openjava.datatag.tagmanage.domain.DtTagGroup;
import com.openjava.datatag.tagmanage.repository.DtShareTagGroupRepository;
import com.openjava.datatag.tagmanage.repository.DtTagGroupRepository;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import com.openjava.datatag.utils.tree.TagDTOTreeNode;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    public Page<DtShareTagGroup> findList(String searchKey, Pageable pageable){
        return dtShareTagGroupRepository.findList("%" + searchKey+ "%", pageable);
    }

    public void choose(Long id,Long userId) throws APIException {
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
        newTgg.setTagsName(tgg.getTagsName());
        newTgg.setIsShare(Constants.DT_TG_PRIVATE);
        newTgg.setSynopsis(tgg.getSynopsis());
        newTgg.setPopularity(0L);
        newTgg.setCreateUser(userId);
        Date now = new Date();
        newTgg.setCreateTime(now);
        newTgg.setModifyTime(now);
        newTgg.setIsDeleted(Constants.DT_TG_EXIST);
        dtTagGroupRepository.save(newTgg);
        List<DtTag> tagList = dtTagRepository.findByTagsIdAndIsDeleted(id,Constants.DT_TG_EXIST);
        DtTagDTO root = new DtTagDTO();
        root.setId(TagDTOTreeNode.ROOT_ID);
        TagDTOTreeNode tagTreeNode = new TagDTOTreeNode(TagDTOTreeNode.toDtTagDTO(tagList),root);
        //先序遍历标签树，新建保存一棵结构一样而id不同的树
        setNewIdAndSave(tagTreeNode,null,newId,now);
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
            newRoot.setIsDeleted(Constants.DT_TG_EXIST);
            newRoot.setIsNew(true);
            newRoot.setPreaTagId(pId);
            dtTagRepository.save(newRoot);
            //子标签的父标签id设置为新的标签的id
            for (TagDTOTreeNode cTree: tree.getChildrenNode()){
                setNewIdAndSave(cTree,newRoot.getId(),tagsId,now);
            }
        }
    }

}
