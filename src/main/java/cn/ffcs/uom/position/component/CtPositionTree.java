package cn.ffcs.uom.position.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.treechooser.component.AttrValueTreeitemRenderer;
import cn.ffcs.uom.common.treechooser.model.Node;
import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.position.model.CtPositionNode;

@SuppressWarnings("serial")
public class CtPositionTree extends Tree implements IdSpace {
	private CtPositionTreeBandbox ctPositionTreeBandbox;

	public CtPositionTreeBandbox getTreeChooserBandbox() {
		return ctPositionTreeBandbox;
	}

	public void setTreeChooserBandbox(CtPositionTreeBandbox treeChooserBandbox) {
		this.ctPositionTreeBandbox = treeChooserBandbox;
	}

	public CtPositionTree() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}

	public void onCreate() {
		this.setSclass("zt-tree-scroll");
		CtPositionNode node = new CtPositionNode();
		node.setGetRoot(true);
		node.setComponent(ctPositionTreeBandbox);
		final TreeNodeImpl<CtPositionNode> treeNode = new TreeNodeImpl<CtPositionNode>(node);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setItemRenderer(new CtPositionTreeitemRenderer(ctPositionTreeBandbox));
	}

}
