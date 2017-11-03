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
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

@Controller
@Scope("prototype")
public class RbacPermissionRelationTreeBandboxExt extends Bandbox implements
		IdSpace {
	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_permission_relation_tree_bandbox_ext.zul";

	@Getter
	@Setter
	private RbacPermissionRelationTree rbacPermissionRelationTree;

	/**
	 * 选择权限
	 */
	@Getter
	private RbacPermissionRelation rbacPermissionRelation;

	/**
	 * 选择权限列表
	 */
	@Getter
	private List<RbacPermissionRelation> rbacPermissionRelationList;

	// @Getter
	// @Setter
	// private boolean endpoint;

	/**
	 * 构造函数
	 */
	public RbacPermissionRelationTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听树选择事件
		 */
		// this.rbacPermissionRelationTree.addForward(Events.ON_SELECT, this,
		// "onSelectRbacPermissionRelationReponse");
	}

	// public Object getAssignObject() {
	// return this.getRbacPermissionRelation();
	// }
	//
	// public void setAssignObject(Object assignObject) {
	// if (assignObject == null || assignObject instanceof RbacPermission) {
	// this.setRbacPermissionRelation((RbacPermissionRelation) assignObject);
	// }
	// }

	public void setRbacPermissionRelation(
			RbacPermissionRelation rbacPermissionRelation) {
		this.setValue(rbacPermissionRelation == null ? ""
				: rbacPermissionRelation.getRbacPermission()
						.getRbacPermissionName());
		this.rbacPermissionRelation = rbacPermissionRelation;
	}

	/**
	 * 多选：设置权限
	 */
	public void setRbacPermissionRelationList(
			List<RbacPermissionRelation> rbacPermissionRelationList) {
		String rbacPermissionName = "";
		if (rbacPermissionRelationList != null
				&& rbacPermissionRelationList.size() > 0) {
			for (int i = 0; i < rbacPermissionRelationList.size(); i++) {
				if (i == rbacPermissionRelationList.size() - 1) {
					rbacPermissionName += rbacPermissionRelationList.get(i)
							.getRbacPermission().getRbacPermissionName();
				} else {
					rbacPermissionName += rbacPermissionRelationList.get(i)
							.getRbacPermission().getRbacPermissionName()
							+ ",";
				}
			}
		}
		this.setValue(rbacPermissionName);
		this.rbacPermissionRelationList = rbacPermissionRelationList;
	}

	/**
	 * 选择权限
	 */
	// public void onSelectRbacPermissionRelationReponse(final ForwardEvent
	// event)
	// throws Exception {
	// if (this.rbacPermissionRelationTree.getSelectedItem() != null) {
	// if (endpoint) {
	// Treeitem ti = this.rbacPermissionRelationTree.getSelectedItem();
	// if (ti.getTreechildren() != null) {
	// ZkUtil.showError("只能选择根节点", "提示信息");
	// return;
	// }
	// }
	// Object data =
	// this.rbacPermissionRelationTree.getSelectedItem().getValue();
	// this.setRbacPermissionRelation((RbacPermissionRelation) ((TreeNodeImpl)
	// data).getEntity());
	// }
	// this.close();
	// }

	/**
	 * 点击确定按钮
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.rbacPermissionRelationTree
				.getSelectedItems();
		List<RbacPermissionRelation> list = new ArrayList<RbacPermissionRelation>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				// if (ti.getTreechildren() != null) {
				// // 不是末级节点
				// continue;
				// }
				RbacPermissionRelation rbacPermissionRelation = (RbacPermissionRelation) ((TreeNodeImpl) ti
						.getValue()).getEntity();
				if (rbacPermissionRelation != null) {
					list.add(rbacPermissionRelation);
				}
			}
		}
		this.setRbacPermissionRelationList(list);
		this.close();
	}

	/**
	 * 点击取消按钮
	 */
	public void onClick$cancelButton() {
		this.close();
	}
}
