package cn.ffcs.uom.common.treechooser.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.treechooser.model.Node;
import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;

public class AttrValueTree extends Tree implements IdSpace {
	private TreeChooserBandbox treeChooserBandbox;

	public TreeChooserBandbox getTreeChooserBandbox() {
		return treeChooserBandbox;
	}

	public void setTreeChooserBandbox(TreeChooserBandbox treeChooserBandbox) {
		this.treeChooserBandbox = treeChooserBandbox;
	}

	public AttrValueTree() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}

	public void onCreate() {
		this.setSclass("zt-tree-scroll");
		Node node = new Node();
		node.setGetRoot(true);
		node.setComponent(treeChooserBandbox);
		final TreeNodeImpl<Node> treeNode = new TreeNodeImpl<Node>(node);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setItemRenderer(new AttrValueTreeitemRenderer(treeChooserBandbox));
	}

}
