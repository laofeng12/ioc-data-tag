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

    private Date createTime;

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
}
