package cn.ffcs.uom.rolePermission.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Treeitem;

import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

@Controller
@Scope("prototype")
public class RbacRoleRelationTreeBandboxExt extends Bandbox implements IdSpace {
	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_role_relation_tree_bandbox_ext.zul";

	@Getter
	@Setter
	private RbacRoleRelationTree rbacRoleRelationTree;

	/**
	 * 选择角色
	 */
	@Getter
	private RbacRoleRelation rbacRoleRelation;

	/**
	 * 选择角色列表
	 */
	@Getter
	private List<RbacRoleRelation> rbacRoleRelationList;

	@Getter
	@Setter
	private boolean endpoint;

	/**
	 * 构造函数
	 */
	public RbacRoleRelationTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听树选择事件
		 */
		// this.rbacRoleRelationTree.addForward(Events.ON_SELECT, this,
		// "onSelectRbacRoleRelationReponse");
	}

	// public Object getAssignObject() {
	// return this.getRbacRoleRelation();
	// }
	//
	// public void setAssignObject(Object assignObject) {
	// if (assignObject == null || assignObject instanceof RbacRole) {
	// this.setRbacRoleRelation((RbacRoleRelation) assignObject);
	// }
	// }

	public void setRbacRoleRelation(RbacRoleRelation rbacRoleRelation) {
		this.setValue(rbacRoleRelation == null ? "" : rbacRoleRelation
				.getRbacRole().getRbacRoleName());
		this.rbacRoleRelation = rbacRoleRelation;
	}

	/**
	 * 多选：设置角色
	 */
	public void setRbacRoleRelationList(
			List<RbacRoleRelation> rbacRoleRelationList) {
		String rbacRoleName = "";
		if (rbacRoleRelationList != null && rbacRoleRelationList.size() > 0) {
			for (int i = 0; i < rbacRoleRelationList.size(); i++) {
				if (i == rbacRoleRelationList.size() - 1) {
					rbacRoleName += rbacRoleRelationList.get(i).getRbacRole()
							.getRbacRoleName();
				} else {
					rbacRoleName += rbacRoleRelationList.get(i).getRbacRole()
							.getRbacRoleName()
							+ ",";
				}
			}
		}
		this.setValue(rbacRoleName);
		this.rbacRoleRelationList = rbacRoleRelationList;
	}

	/**
	 * 选择角色
	 */
	// public void onSelectRbacRoleRelationReponse(final ForwardEvent event)
	// throws Exception {
	// if (this.rbacRoleRelationTree.getSelectedItem() != null) {
	// if (endpoint) {
	// Treeitem ti = this.rbacRoleRelationTree.getSelectedItem();
	// if (ti.getTreechildren() != null) {
	// ZkUtil.showError("只能选择根节点", "提示信息");
	// return;
	// }
	// }
	// Object data = this.rbacRoleRelationTree.getSelectedItem().getValue();
	// this.setRbacRoleRelation((RbacRoleRelation) ((TreeNodeImpl)
	// data).getEntity());
	// }
	// this.close();
	// }

	/**
	 * 点击确定按钮
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.rbacRoleRelationTree.getSelectedItems();
		List<RbacRoleRelation> list = new ArrayList<RbacRoleRelation>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				// if (ti.getTreechildren() != null) {
				// // 不是末级节点
				// continue;
				// }
				RbacRoleRelation rbacRoleRelation = (RbacRoleRelation) ((TreeNodeImpl) ti
						.getValue()).getEntity();
				if (rbacRoleRelation != null) {
					list.add(rbacRoleRelation);
				}
			}
		}
		this.setRbacRoleRelationList(list);
		this.close();
	}

	/**
	 * 点击取消按钮
	 */
	public void onClick$cancelButton() {
		this.close();
	}
}
