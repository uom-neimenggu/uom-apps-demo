package cn.ffcs.uom.organization.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.organization.model.UnitedDirectory;

public class UnitedDirectoryTree extends Tree implements IdSpace {

	private static final long serialVersionUID = -1144093254645307010L;

	/**
	 * 构造函数.
	 */
	public UnitedDirectoryTree() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 创建
	 */
	public void onCreate() {
		this.setSclass("zt-tree-scroll");
		if (this != null) {
			bindTree();
		}
	}

	/**
	 * 非推导树
	 */
	public void bindTree() {
		UnitedDirectory unitedDirectory = new UnitedDirectory();
		unitedDirectory.setIsRoot(true);
		final TreeNodeImpl<UnitedDirectory> treeNode = new TreeNodeImpl<UnitedDirectory>(
				unitedDirectory);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
		// this.setLableStyle();
	}

	// public void setLableStyle() {
	// Collection<Treeitem> tis = this.getItems();
	// for (Treeitem ti : tis) {
	// UnitedDirectory unitedDirectory = (UnitedDirectory) ((TreeNodeImpl) ti
	// .getValue()).getEntity();
	// if (unitedDirectory != null) {
	// Treecell cell = null;
	// List list = ti.getTreerow().getChildren();
	// if (list != null) {
	// cell = (Treecell) list.get(0);
	// }
	// if (unitedDirectory.isCompany()) {
	// if (cell != null) {
	// cell.setStyle("color:blue");
	// }
	// }
	// }
	// }
	// }
}
