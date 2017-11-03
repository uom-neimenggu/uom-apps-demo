package cn.ffcs.uom.roleauth.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.roleauth.model.StaffAuthority;

public class AuthorityTree extends Tree implements IdSpace {
	
	private static final long serialVersionUID = 1L;
	public AuthorityTree() {
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
		StaffAuthority authority = new StaffAuthority();
		authority.setIsRoot(true);
		final TreeNodeImpl<StaffAuthority> treeNode = new TreeNodeImpl<StaffAuthority>(authority);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
}
