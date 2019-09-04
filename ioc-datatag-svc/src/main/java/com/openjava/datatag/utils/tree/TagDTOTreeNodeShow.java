package com.openjava.datatag.utils.tree;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/***
 *
 * 由于前后端交流问题，导致用这个类来更改TagDTOTreeNode的形式 传输给前端
 *
 */
public class TagDTOTreeNodeShow {

    private Long id;

    private Long tagsId;

    private Long preaTagId;

    private String tagName;

    private String synopsis;

    private Boolean isLeaf;

    private Boolean isLeafParent;

    private Boolean isNotLeafParent;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date modifyTime;

    private Long lvl;

    private List<TagDTOTreeNodeShow> childrenNode;

    public TagDTOTreeNodeShow(TagDTOTreeNode tree){
        id = tree.getTag().getId();
        tagsId = tree.getTag().getTagsId();
        preaTagId = tree.getTag().getPreaTagId();
        tagName = tree.getTag().getTagName();
        synopsis = tree.getTag().getSynopsis();
        createTime = tree.getTag().getCreateTime();
        modifyTime = tree.getTag().getModifyTime();
        lvl = tree.getTag().getLvl();
        isLeaf = tree.isLeaf();
        isLeafParent = tree.isLeafParent();
        isNotLeafParent = !tree.isLeafParent();//应前端要求给个取反的字段
        childrenNode = new ArrayList<>();
        for (TagDTOTreeNode ctree: tree.getChildrenNode()){
            TagDTOTreeNodeShow ctreeShow = new TagDTOTreeNodeShow(ctree);
            childrenNode.add(ctreeShow);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagsId() {
        return tagsId;
    }

    public void setTagsId(Long tagsId) {
        this.tagsId = tagsId;
    }

    public Long getPreaTagId() {
        return preaTagId;
    }

    public void setPreaTagId(Long preaTagId) {
        this.preaTagId = preaTagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getLvl() {
        return lvl;
    }

    public void setLvl(Long lvl) {
        this.lvl = lvl;
    }

    public List<TagDTOTreeNodeShow> getChildrenNode() {
        return childrenNode;
    }

    public void setChildrenNode(List<TagDTOTreeNodeShow> childrenNode) {
        this.childrenNode = childrenNode;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }

    public Boolean getLeafParent() {
        return isLeafParent;
    }

    public void setLeafParent(Boolean leafParent) {
        isLeafParent = leafParent;
    }

    public Boolean getNotLeafParent() {
        return isNotLeafParent;
    }

    public void setNotLeafParent(Boolean notLeafParent) {
        isNotLeafParent = notLeafParent;
    }
}
