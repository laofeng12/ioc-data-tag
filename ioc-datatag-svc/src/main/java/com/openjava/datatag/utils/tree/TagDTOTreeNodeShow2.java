package com.openjava.datatag.utils.tree;

import com.openjava.datatag.tagmanage.dto.DtTagDTO2;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TagDTOTreeNodeShow2 {
    private String value;
    private String label;
    List<TagDTOTreeNodeShow2> children = new ArrayList<>();
    public TagDTOTreeNodeShow2(TagDTOTreeNode tree){
        this.value = tree.getTag2().getValue();
        this.label = tree.getTag2().getLabel();
        children = new ArrayList<>();
        for (TagDTOTreeNode ctree: tree.getChildrenNode()){
            TagDTOTreeNodeShow2 ctreeShow = new TagDTOTreeNodeShow2(ctree);
            children.add(ctreeShow);
        }
    }
}
