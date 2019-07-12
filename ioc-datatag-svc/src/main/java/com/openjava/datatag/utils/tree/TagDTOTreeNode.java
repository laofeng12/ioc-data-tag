package com.openjava.datatag.utils.tree;

import com.openjava.datatag.tagmanage.domain.DtTag;
import com.openjava.datatag.tagmanage.dto.DtTagDTO;

import java.util.ArrayList;
import java.util.List;

public class TagDTOTreeNode {
    private DtTagDTO tag;
    private List<TagDTOTreeNode> childrenNode;
    public static final Long ROOT_ID = 0L;

    public TagDTOTreeNode(DtTagDTO tag){
        this.tag = tag;
        childrenNode = new ArrayList<TagDTOTreeNode>();
    }

    public void addChildrenNode(TagDTOTreeNode node){
        childrenNode.add(node);
    }

    public void setTag(DtTagDTO tag){
        this.tag = tag;
    }
    public DtTagDTO getTag() {
        return tag;
    }

    public List<TagDTOTreeNode> getChildrenNode() {
        return childrenNode;
    }

    public boolean isleaf(){
        return childrenNode.isEmpty();
    }

    public List<DtTagDTO> toList(){
        List<DtTagDTO> list = new ArrayList<>();
        list.add(tag);
        for(TagDTOTreeNode childrenTree: this.childrenNode){
            list.addAll(childrenTree.toList());
        }
        return list;
    }

    public TagDTOTreeNode(List<DtTagDTO> taglist,DtTagDTO rootTag){
        this(rootTag);
        for (DtTagDTO tag : taglist) {
            if(tag.getPreaTagId() == null){
                tag.setPreaTagId(TagDTOTreeNode.ROOT_ID);
            }
            if(tag.getPreaTagId().equals(this.getTag().getId())) {
                TagDTOTreeNode child = new TagDTOTreeNode(taglist,tag);
                this.addChildrenNode(child);
            }
        }
    }

    public static List<DtTagDTO> toDtTagDTO(List<DtTag> tagList){
        List<DtTagDTO> tagDTOlist = new ArrayList<>();
        for (DtTag tag : tagList){
            DtTagDTO tagDTO = new DtTagDTO();
            tagDTO.setId(tag.getId());
            tagDTO.setCreateTime(tag.getCreateTime());
            tagDTO.setLvl(tag.getLvl());
            tagDTO.setModifyTime(tag.getModifyTime());
            tagDTO.setPreaTagId(tag.getPreaTagId());
            tagDTO.setSynopsis(tag.getSynopsis());
            tagDTO.setTagsId(tag.getTagsId());
            tagDTO.setTagName(tag.getTagName());
            tagDTOlist.add(tagDTO);
        }
        return tagDTOlist;
    }
}
