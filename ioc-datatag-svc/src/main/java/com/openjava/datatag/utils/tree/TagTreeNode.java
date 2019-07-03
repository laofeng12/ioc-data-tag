package com.openjava.datatag.utils.tree;

import com.openjava.datatag.tagmanage.domain.DtTag;
import io.swagger.annotations.ApiModel;

import java.util.ArrayList;
import java.util.List;
@ApiModel("标签树节点")
public class TagTreeNode{
	private DtTag tag;
	private List<TagTreeNode> childrenNode;
	public TagTreeNode(DtTag tag){
		this.tag = tag;
		childrenNode = new ArrayList<TagTreeNode>();
	}

	public void addChildrenNode(TagTreeNode node){
		childrenNode.add(node);
	}

	public void setTag(DtTag tag){
		this.tag = tag;
	}
	public DtTag getTag() {
		return tag;
	}

	public List<TagTreeNode> getChildrenNode() {
		return childrenNode;
	}

	public boolean isleaf(){
		return childrenNode.isEmpty();
	}

	public List<DtTag> toList(){
		List<DtTag> list = new ArrayList<>();
		list.add(tag);
		for(TagTreeNode childrenTree: this.childrenNode){
			list.addAll(childrenTree.toList());
		}
		return list;
	}

	public TagTreeNode(List<DtTag> taglist,DtTag rootTag){
		this(rootTag);
		for (DtTag tag : taglist) {
			if(tag.getPreaTagId() == null){
				tag.setPreaTagId(0L);
			}
			if(tag.getPreaTagId().equals(this.getTag().getId())) {
				TagTreeNode child = new TagTreeNode(taglist,tag);
				this.addChildrenNode(child);
			}
		}
	}

}
