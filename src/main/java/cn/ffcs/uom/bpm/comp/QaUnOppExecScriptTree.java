package cn.ffcs.uom.bpm.comp;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;

public class QaUnOppExecScriptTree extends Tree implements IdSpace {
	
	private static final long serialVersionUID = -2078254800757571960L;
	
	private String flag;
	
	/**
	 * 构造函数.
	 */
	public QaUnOppExecScriptTree() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 创建
	 */
	public void onCreate() {
		this.setSclass("zt-tree-scroll");//可滚动
		if (this != null) {
			this.bindTree();
		}
	}

	/**
	 * 绑定树
	 */
	@SuppressWarnings("deprecation")
	public void bindTree() {
		QaUnOppExecScript qaUnOppExecScript = new QaUnOppExecScript();
		qaUnOppExecScript.setIsRoot(true);
		qaUnOppExecScript.setFlag(flag);
		final TreeNodeImpl<QaUnOppExecScript> treeNode = new TreeNodeImpl<QaUnOppExecScript>(
				qaUnOppExecScript);
		treeNode.readChildren();//注意！
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
		bindTree();
	}

}
