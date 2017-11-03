package cn.ffcs.uom.organization.component;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.organization.model.Organization;

public class OrganizationTree extends Tree implements IdSpace {
	@Getter
	@Setter
	private String orgTreeId;

	/**
	 * 构造函数.
	 */
	public OrganizationTree() {
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
		Organization orgRel = new Organization();
		orgRel.setIsRoot(true);
		orgRel.setOrgTreeId(this.getOrgTreeId());
		final TreeNodeImpl<Organization> treeNode = new TreeNodeImpl<Organization>(
				orgRel);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
}
