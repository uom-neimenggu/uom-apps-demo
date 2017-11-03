package cn.ffcs.uom.politicallocation.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;

public class PoliticalLocationTree extends Tree implements IdSpace {
	/**
	 * 构造函数.
	 */
	public PoliticalLocationTree() {
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
		PoliticalLocation politicalLocation = new PoliticalLocation();
		politicalLocation.setIsRoot(true);
		final TreeNodeImpl<PoliticalLocation> treeNode = new TreeNodeImpl<PoliticalLocation>(
				politicalLocation);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
}
