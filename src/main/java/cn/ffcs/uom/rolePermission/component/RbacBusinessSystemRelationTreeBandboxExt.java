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
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

@Controller
@Scope("prototype")
public class RbacBusinessSystemRelationTreeBandboxExt extends Bandbox implements
		IdSpace {
	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_business_system_relation_tree_bandbox_ext.zul";

	@Getter
	@Setter
	private RbacBusinessSystemRelationTree rbacBusinessSystemRelationTree;

	/**
	 * 选择系统
	 */
	@Getter
	private RbacBusinessSystemRelation rbacBusinessSystemRelation;

	/**
	 * 选择系统列表
	 */
	@Getter
	private List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList;

	@Getter
	@Setter
	private boolean endpoint;

	/**
	 * 构造函数
	 */
	public RbacBusinessSystemRelationTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听树选择事件
		 */
		// this.rbacBusinessSystemRelationTree.addForward(Events.ON_SELECT,
		// this,
		// "onSelectRbacBusinessSystemRelationReponse");
	}

	// public Object getAssignObject() {
	// return this.getRbacBusinessSystemRelation();
	// }
	//
	// public void setAssignObject(Object assignObject) {
	// if (assignObject == null || assignObject instanceof RbacBusinessSystem) {
	// this.setRbacBusinessSystemRelation((RbacBusinessSystemRelation)
	// assignObject);
	// }
	// }

	public void setRbacBusinessSystemRelation(
			RbacBusinessSystemRelation rbacBusinessSystemRelation) {
		this.setValue(rbacBusinessSystemRelation == null ? ""
				: rbacBusinessSystemRelation.getRbacBusinessSystem()
						.getRbacBusinessSystemName());
		this.rbacBusinessSystemRelation = rbacBusinessSystemRelation;
	}

	/**
	 * 多选：设置系统
	 */
	public void setRbacBusinessSystemRelationList(
			List<RbacBusinessSystemRelation> rbacBusinessSystemRelationList) {
		String rbacBusinessSystemName = "";
		if (rbacBusinessSystemRelationList != null
				&& rbacBusinessSystemRelationList.size() > 0) {
			for (int i = 0; i < rbacBusinessSystemRelationList.size(); i++) {
				if (i == rbacBusinessSystemRelationList.size() - 1) {
					rbacBusinessSystemName += rbacBusinessSystemRelationList
							.get(i).getRbacBusinessSystem()
							.getRbacBusinessSystemName();
				} else {
					rbacBusinessSystemName += rbacBusinessSystemRelationList
							.get(i).getRbacBusinessSystem()
							.getRbacBusinessSystemName()
							+ ",";
				}
			}
		}
		this.setValue(rbacBusinessSystemName);
		this.rbacBusinessSystemRelationList = rbacBusinessSystemRelationList;
	}

	/**
	 * 选择系统
	 */
	// public void onSelectRbacBusinessSystemRelationReponse(final ForwardEvent
	// event)
	// throws Exception {
	// if (this.rbacBusinessSystemRelationTree.getSelectedItem() != null) {
	// if (endpoint) {
	// Treeitem ti = this.rbacBusinessSystemRelationTree.getSelectedItem();
	// if (ti.getTreechildren() != null) {
	// ZkUtil.showError("只能选择根节点", "提示信息");
	// return;
	// }
	// }
	// Object data =
	// this.rbacBusinessSystemRelationTree.getSelectedItem().getValue();
	// this.setRbacBusinessSystemRelation((RbacBusinessSystemRelation)
	// ((TreeNodeImpl)
	// data).getEntity());
	// }
	// this.close();
	// }

	/**
	 * 点击确定按钮
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.rbacBusinessSystemRelationTree
				.getSelectedItems();
		List<RbacBusinessSystemRelation> list = new ArrayList<RbacBusinessSystemRelation>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				// if (ti.getTreechildren() != null) {
				// // 不是末级节点
				// continue;
				// }
				RbacBusinessSystemRelation rbacBusinessSystemRelation = (RbacBusinessSystemRelation) ((TreeNodeImpl) ti
						.getValue()).getEntity();
				if (rbacBusinessSystemRelation != null) {
					list.add(rbacBusinessSystemRelation);
				}
			}
		}
		this.setRbacBusinessSystemRelationList(list);
		this.close();
	}

	/**
	 * 点击取消按钮
	 */
	public void onClick$cancelButton() {
		this.close();
	}
}
