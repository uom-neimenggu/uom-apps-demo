package cn.ffcs.uom.accconfig.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;

public class AccConfigTree extends Tree implements IdSpace {
	private static final long serialVersionUID = 1L;
	public AccConfigTree() {
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
	@SuppressWarnings("deprecation")
	public void bindTree() {
		AccConfig accConfig = new AccConfig();
		accConfig.setIsRoot(true);
		final TreeNodeImpl<AccConfig> treeNode = new TreeNodeImpl<AccConfig>(accConfig);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}

}
