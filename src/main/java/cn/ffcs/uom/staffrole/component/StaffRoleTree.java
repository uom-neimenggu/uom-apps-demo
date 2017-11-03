package cn.ffcs.uom.staffrole.component;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Tree;

import cn.ffcs.uom.common.treechooser.component.AttrValueTreeitemRenderer;
import cn.ffcs.uom.common.zkplus.zul.tree.model.BaseTreeModel;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.common.zkplus.zul.tree.render.BaseTreeitemRenderer;
import cn.ffcs.uom.staffrole.model.StaffRole;

public class StaffRoleTree extends Tree implements IdSpace {
	
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private StaffRoleTreeBandboxExt staffRoleTreeBandboxExt;
	
	public StaffRoleTree() {
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
		StaffRole staffRole = new StaffRole();
		staffRole.setIsRoot(true);
		if (staffRoleTreeBandboxExt != null) {
		    staffRole.setComponent(staffRoleTreeBandboxExt);
        }
		
		final TreeNodeImpl<StaffRole> treeNode = new TreeNodeImpl<StaffRole>(staffRole);
		treeNode.readChildren();
		this.setModel(new BaseTreeModel(treeNode));
	    if (staffRoleTreeBandboxExt != null) {
	        this.setItemRenderer(new StaffRoleTreeitemRenderer(staffRoleTreeBandboxExt));
	    } else {
	      this.setTreeitemRenderer(new BaseTreeitemRenderer());
        }
	}
}
