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
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

@Controller
@Scope("prototype")
public class RbacResourceRelationTreeBandboxExt extends Bandbox implements
		IdSpace {
	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_resource_relation_tree_bandbox_ext.zul";

	@Getter
	@Setter
	private RbacResourceRelationTree rbacResourceRelationTree;

	/**
	 * 选择角色
	 */
	@Getter
	private RbacResourceRelation rbacResourceRelation;

	/**
	 * 选择角色列表
	 */
	@Getter
	private List<RbacResourceRelation> rbacResourceRelationList;

	@Getter
	@Setter
	private boolean endpoint;

	/**
	 * 构造函数
	 */
	public RbacResourceRelationTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听树选择事件
		 */
		// this.rbacResourceRelationTree.addForward(Events.ON_SELECT, this,
		// "onSelectRbacResourceRelationReponse");
	}

	// public Object getAssignObject() {
	// return this.getRbacResourceRelation();
	// }
	//
	// public void setAssignObject(Object assignObject) {
	// if (assignObject == null || assignObject instanceof RbacResource) {
	// this.setRbacResourceRelation((RbacResourceRelation) assignObject);
	// }
	// }

	public void setRbacResourceRelation(
			RbacResourceRelation rbacResourceRelation) {
		this.setValue(rbacResourceRelation == null ? "" : rbacResourceRelation
				.getRbacResource().getRbacResourceName());
		this.rbacResourceRelation = rbacResourceRelation;
	}

	/**
	 * 多选：设置角色
	 */
	public void setRbacResourceRelationList(
			List<RbacResourceRelation> rbacResourceRelationList) {
		String rbacResourceName = "";
		if (rbacResourceRelationList != null
				&& rbacResourceRelationList.size() > 0) {
			for (int i = 0; i < rbacResourceRelationList.size(); i++) {
				if (i == rbacResourceRelationList.size() - 1) {
					rbacResourceName += rbacResourceRelationList.get(i)
							.getRbacResource().getRbacResourceName();
				} else {
					rbacResourceName += rbacResourceRelationList.get(i)
							.getRbacResource().getRbacResourceName()
							+ ",";
				}
			}
		}
		this.setValue(rbacResourceName);
		this.rbacResourceRelationList = rbacResourceRelationList;
	}

	/**
	 * 选择角色
	 */
	// public void onSelectRbacResourceRelationReponse(final ForwardEvent event)
	// throws Exception {
	// if (this.rbacResourceRelationTree.getSelectedItem() != null) {
	// if (endpoint) {
	// Treeitem ti = this.rbacResourceRelationTree.getSelectedItem();
	// if (ti.getTreechildren() != null) {
	// ZkUtil.showError("只能选择根节点", "提示信息");
	// return;
	// }
	// }
	// Object data = this.rbacResourceRelationTree.getSelectedItem().getValue();
	// this.setRbacResourceRelation((RbacResourceRelation) ((TreeNodeImpl)
	// data).getEntity());
	// }
	// this.close();
	// }

	/**
	 * 点击确定按钮
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.rbacResourceRelationTree
				.getSelectedItems();
		List<RbacResourceRelation> list = new ArrayList<RbacResourceRelation>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				// if (ti.getTreechildren() != null) {
				// // 不是末级节点
				// continue;
				// }
				RbacResourceRelation rbacResourceRelation = (RbacResourceRelation) ((TreeNodeImpl) ti
						.getValue()).getEntity();
				if (rbacResourceRelation != null) {
					list.add(rbacResourceRelation);
				}
			}
		}
		this.setRbacResourceRelationList(list);
		this.close();
	}

	/**
	 * 点击取消按钮
	 */
	public void onClick$cancelButton() {
		this.close();
	}
}
