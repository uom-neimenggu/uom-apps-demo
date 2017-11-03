package cn.ffcs.uom.rolePermission.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

public class RbacPermissionRelationTree extends Tree implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数.
	 */
	public RbacPermissionRelationTree() {
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
		RbacPermissionRelation rbacPermissionRelation = new RbacPermissionRelation();
		rbacPermissionRelation.setIsRoot(true);
		final TreeNodeImpl<RbacPermissionRelation> treeNode = new TreeNodeImpl<RbacPermissionRelation>(
				rbacPermissionRelation);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
}
