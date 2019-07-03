package com.openjava.datatag.utils.tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.List;

@ApiModel("树节点")
public class TreeNode<T> {
	private T data;
	private List<TreeNode> childrenNode;
	public TreeNode(T data){
		this.data = data;
		childrenNode = new ArrayList<TreeNode>();
	}

	public void addChildrenNode(TreeNode<T> node){
		childrenNode.add(node);
	}

	public T getData() {
		return data;
	}

	public List<TreeNode> getChildrenNode() {
		return childrenNode;
	}

}
