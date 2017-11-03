package cn.ffcs.uom.rolePermission.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

public class RbacResourceRelationTree extends Tree implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数.
	 */
	public RbacResourceRelationTree() {
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
		RbacResourceRelation rbacResourceRelation = new RbacResourceRelation();
		rbacResourceRelation.setIsRoot(true);
		final TreeNodeImpl<RbacResourceRelation> treeNode = new TreeNodeImpl<RbacResourceRelation>(
				rbacResourceRelation);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
}
