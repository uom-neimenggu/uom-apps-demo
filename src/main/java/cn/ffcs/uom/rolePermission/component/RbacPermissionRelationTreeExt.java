package cn.ffcs.uom.rolePermission.component;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRelation;

public class RbacPermissionRelationTreeExt extends Div implements IdSpace {

	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_permission_relation_tree_ext.zul";

	/**
	 * manager
	 */
	private RbacPermissionRelationManager rbacPermissionRelationManager = (RbacPermissionRelationManager) ApplicationContextUtil
			.getBean("rbacPermissionRelationManager");

	/**
	 * 权限树
	 */
	private RbacPermissionRelationTree rbacPermissionRelationTree;

	/**
	 * 选中的权限TreeItem
	 */
	@SuppressWarnings("unused")
	private Treeitem selectedTreeitem;

	/**
	 * 选中的权限
	 */
	private RbacPermission rbacPermission;

	/**
	 * 选中的权限关系
	 */
	private RbacPermissionRelation rbacPermissionRelation;

	/**
	 * 增加根节点按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton addRootButton;

	/**
	 * 增加孩子节点按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton addChildButton;

	/**
	 * 删除节点按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton delChildButton;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public RbacPermissionRelationTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 权限名称修改要改权限树节点名称
		 */
		RbacPermissionRelationTreeExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_PERMISSION_INFO,
				RbacPermissionRelationTreeExt.this,
				"onSaveRbacPermissionInfoResponse");
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	public void onAddRootNode() throws Exception {
		String opType = "addRootNode";
		this.openAddNodeWindow(opType);
	}

	/**
	 * 增加根节点
	 */
	private void onAddRootNodeResponse(
			RbacPermissionRelation rbacPermissionRelation) throws Exception {
		Treechildren treechildren = this.rbacPermissionRelationTree
				.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(rbacPermissionRelation.getLabel());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
				rbacPermissionRelation);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}

	/**
	 * 增加子节点
	 */
	public void onAddChildNode() throws Exception {
		if (this.rbacPermissionRelationTree.getSelectedItem() != null) {
			String opType = "addChildNode";
			this.openAddNodeWindow(opType);
		} else {
			ZkUtil.showError("请选择节点", "提示信息");
		}
	}

	/**
	 * 增加子节点
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void onAddChildNodeResponse(
			RbacPermissionRelation rbacPermissionRelation) throws Exception {

		Treechildren treechildren = this.rbacPermissionRelationTree
				.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.rbacPermissionRelationTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.rbacPermissionRelationTree.getSelectedItem().setValue(
					parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacPermissionRelation.getLabel());

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacPermissionRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.rbacPermissionRelationTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacPermissionRelation.getLabel());
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacPermissionRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(treechildren);
		}
	}

	/**
	 * 打开界面
	 * 
	 * @param opType
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void openAddNodeWindow(String opType) throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("rbacPermission", rbacPermission);
		Window win = (Window) Executions
				.createComponents(
						"/pages/rolePermission/rbac_permission_relation_tree_node_edit.zul",
						this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

				Map dataMap = (Map) event.getData();

				RbacPermissionRelation rbacPermissionRelation = (RbacPermissionRelation) dataMap
						.get("rbacPermissionRelation");

				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(rbacPermissionRelation);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(rbacPermissionRelation);
				}
			}
		});
	}

	/**
	 * 删除节点
	 * 
	 * @throws Exception
	 */
	public void onDelNode() throws Exception {

		if (this.rbacPermissionRelationTree.getSelectedItem() != null) {
			Treechildren treechildren = this.rbacPermissionRelationTree
					.getSelectedItem().getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点,不能删除", "提示信息");
				return;
			}
			ZkUtil.showQuestion("你确定要删除该节点吗？", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						rbacPermissionRelationManager
								.removeRbacPermissionRelation(rbacPermissionRelation);
						Treechildren treechildren = (Treechildren) rbacPermissionRelationTree
								.getSelectedItem().getParent();
						treechildren.removeChild(rbacPermissionRelationTree
								.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						rbacPermissionRelation = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(
								RolePermissionConstants.ON_DEL_NODE_OK,
								RbacPermissionRelationTreeExt.this, null);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}

	/**
	 * 点击时选择树
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void onSelect$rbacPermissionRelationTree() throws Exception {
		selectedTreeitem = this.rbacPermissionRelationTree.getSelectedItem();
		rbacPermissionRelation = (RbacPermissionRelation) ((TreeNodeImpl) this.rbacPermissionRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (rbacPermissionRelation != null
				&& rbacPermissionRelation.getRbacPermissionRelaId() != null) {
			rbacPermission = rbacPermissionRelation.getRbacPermission();
		}
		Events.postEvent(
				RolePermissionConstants.ON_SELECT_RBAC_PERMISSION_TREE_REQUEST,
				this, rbacPermissionRelation);
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAddRoot
	 * @param canAddChild
	 * @param canDel
	 */
	@SuppressWarnings("unused")
	private void setButtonValid(boolean canAddRoot, boolean canAddChild,
			boolean canDel) {
		this.addRootButton.setDisabled(!canAddRoot);
		this.addChildButton.setDisabled(!canAddChild);
		this.delChildButton.setDisabled(!canDel);
	}

	/**
	 * 供外层主动获取控件选择的权限
	 */
	public RbacPermissionRelation getSelectRbacPermissionRelation() {
		return rbacPermissionRelation;
	}

	/**
	 * 判断名称是否要修改
	 */
	public void onSaveRbacPermissionInfoResponse(ForwardEvent event)
			throws Exception {

		if (event.getOrigin().getData() != null) {

			RbacPermission rbacPermission = (RbacPermission) event.getOrigin()
					.getData();
			/**
			 * 修改名称
			 */
			this.rbacPermissionRelationTree.getSelectedItem().setLabel(
					rbacPermission.getRbacPermissionName());
		}
	}

	/**
	 * 设置页面坐标
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAddRoot = false;
		boolean canAddChild = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAddRoot = true;
			canAddChild = true;
			canDel = true;
		} else if ("rbacPermissionTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_TREE_RBAC_PERMISSION_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_TREE_RBAC_PERMISSION_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_PERMISSION_TREE_RBAC_PERMISSION_DEL)) {
				canDel = true;
			}

		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(canDel);
	}
}
