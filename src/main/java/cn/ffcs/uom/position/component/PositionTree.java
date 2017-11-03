package cn.ffcs.uom.position.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.position.model.Position;

public class PositionTree extends Tree implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数.
	 */
	public PositionTree() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 创建
	 */
	public void onCreate() {
		this.setSclass("zt-tree-scroll");
		if (this != null) {
			this.bindTree();
		}
	}

	/**
	 * 绑定树
	 */
	public void bindTree() {
		Position position = new Position();
		position.setIsRoot(true);
		final TreeNodeImpl<Position> treeNode = new TreeNodeImpl<Position>(
				position);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
}
