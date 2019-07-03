package com.openjava.datatag.tagmanage.service;

import com.openjava.datatag.tagmanage.domain.DtShareTagGroup;
import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.repository.DtShareTagGroupRepository;
import com.openjava.datatag.tagmanage.repository.DtTagRepository;
import com.openjava.datatag.utils.tree.TagTreeNode;
import org.ljdp.component.sequence.ConcurrentSequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class DtShareTagGroupServiceImpl implements DtShareTagGroupService{
    @Resource
    private DtShareTagGroupRepository dtShareTagGroupRepository;

    @Resource
    private DtTagRepository dtTagRepository;

    public Page<DtShareTagGroup> findList(String searchKey, Pageable pageable){
        return dtShareTagGroupRepository.findList("%" + searchKey+ "%", pageable);
    }

    @Override
    public List<DtTag> choose(Long id) {
        List<DtTag> tagList = dtTagRepository.findByTagsIdAndIsDeleted(id,0L);
        DtTag root = new DtTag();
        root.setId(0L);
        TagTreeNode tagTreeNode = new TagTreeNode(tagList,root);
        setNewID(tagTreeNode,0L);
        List<DtTag> list = tagTreeNode.toList();
        list.remove(root);
        for (DtTag tag : list){
            if(tag.getPreaTagId().equals(0L)){
                tag.setPreaTagId(null);
            }
        }
        return list;
    }
    private TagTreeNode setNewID(TagTreeNode tree,Long pId){
        DtTag root = tree.getTag();
        Long newId = ConcurrentSequence.getInstance().getSequence();
        if(!(root.getId()==null || root.getId().equals(0L))){
            DtTag newRoot = new DtTag();
            newRoot.setPreaTagId(pId);
            newRoot.setIsNew(true);
            newRoot.setId(newId);


            root.setIsNew(true);
            root.setId(newId);
            root.setPreaTagId(pId);
        }
        for(TagTreeNode cTree: tree.getChildrenNode()){
            setNewID(cTree,root.getId());
        }
        return tree;
    }

}
