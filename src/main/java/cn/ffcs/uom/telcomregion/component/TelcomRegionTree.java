package cn.ffcs.uom.telcomregion.component;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class TelcomRegionTree extends Tree implements IdSpace {
	
	/**
	 * 是否是配置界面(忽略数据权时都可使用)
	 */
	@Getter
	@Setter
	private Boolean isConfigPage;
	
	/**
	 * 构造函数.
	 */
	public TelcomRegionTree() {
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
		TelcomRegion telcomRegion = new TelcomRegion();
		telcomRegion.setIsRoot(true);
		telcomRegion.setIsConfigPage(isConfigPage);
		final TreeNodeImpl<TelcomRegion> treeNode = new TreeNodeImpl<TelcomRegion>(
				telcomRegion);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
		this.setTreeitemRenderer(new BaseTreeitemRenderer());
	}
}
