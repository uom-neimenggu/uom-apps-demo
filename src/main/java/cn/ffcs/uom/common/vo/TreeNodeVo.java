/**
 * 
 */
package cn.ffcs.uom.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点对象。
 * 
 * @author wuzhb
 * 
 */
public class TreeNodeVo {
	private String id;
	private String name;
	private boolean isParent;
	private boolean loaded;
	private List<TreeNodeVo> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsParent() {
		return isParent;
	}
	
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public List<TreeNodeVo> getChildren() {
		return children;
	}
	
	public void addChild(TreeNodeVo node) {
		if (this.children == null) {
			this.children = new ArrayList<TreeNodeVo>();
		}
		this.children.add(node);
	}

	public void setChildren(List<TreeNodeVo> children) {
		this.children = children;
	}
	
	public static class Factory {
		public static TreeNodeVo instanceLeafNode(String id, String name) {
			TreeNodeVo node = new TreeNodeVo();
			node.setId(id);
			node.setName(name);
			node.setIsParent(false);
			node.setLoaded(true);
			return node;
		}
		
		public static TreeNodeVo instanceParentNode(String id, String name) {
			TreeNodeVo node = new TreeNodeVo();
			node.setId(id);
			node.setName(name);
			node.setIsParent(true);
			node.setLoaded(false);
			return node;
		}
	}

}
