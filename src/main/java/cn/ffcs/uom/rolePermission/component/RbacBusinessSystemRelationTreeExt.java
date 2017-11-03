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
import cn.ffcs.uom.rolePermission.manager.RbacBusinessSystemRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystemRelation;

public class RbacBusinessSystemRelationTreeExt extends Div implements IdSpace {

	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_business_system_relation_tree_ext.zul";

	/**
	 * manager
	 */
	private RbacBusinessSystemRelationManager rbacBusinessSystemRelationManager = (RbacBusinessSystemRelationManager) ApplicationContextUtil
			.getBean("rbacBusinessSystemRelationManager");

	/**
	 * 业务系统树
	 */
	private RbacBusinessSystemRelationTree rbacBusinessSystemRelationTree;

	/**
	 * 选中的业务系统TreeItem
	 */
	@SuppressWarnings("unused")
	private Treeitem selectedTreeitem;

	/**
	 * 选中的业务系统
	 */
	private RbacBusinessSystem rbacBusinessSystem;

	/**
	 * 选中的业务系统关系
	 */
	private RbacBusinessSystemRelation rbacBusinessSystemRelation;

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

	public RbacBusinessSystemRelationTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 业务系统名称修改要改业务系统树节点名称
		 */
		RbacBusinessSystemRelationTreeExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_BUSINESS_SYSTEM_INFO,
				RbacBusinessSystemRelationTreeExt.this,
				"onSaveRbacBusinessSystemInfoResponse");
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
			RbacBusinessSystemRelation rbacBusinessSystemRelation)
			throws Exception {
		Treechildren treechildren = this.rbacBusinessSystemRelationTree
				.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(rbacBusinessSystemRelation.getLabel());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
				rbacBusinessSystemRelation);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}

	/**
	 * 增加子节点
	 */
	public void onAddChildNode() throws Exception {
		if (this.rbacBusinessSystemRelationTree.getSelectedItem() != null) {
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
			RbacBusinessSystemRelation rbacBusinessSystemRelation)
			throws Exception {

		Treechildren treechildren = this.rbacBusinessSystemRelationTree
				.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.rbacBusinessSystemRelationTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.rbacBusinessSystemRelationTree.getSelectedItem().setValue(
					parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacBusinessSystemRelation.getLabel());

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacBusinessSystemRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.rbacBusinessSystemRelationTree
					.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacBusinessSystemRelation.getLabel());
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacBusinessSystemRelation);
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
		map.put("rbacBusinessSystem", rbacBusinessSystem);
		Window win = (Window) Executions
				.createComponents(
						"/pages/rolePermission/rbac_business_system_relation_tree_node_edit.zul",
						this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

				Map dataMap = (Map) event.getData();

				RbacBusinessSystemRelation rbacBusinessSystemRelation = (RbacBusinessSystemRelation) dataMap
						.get("rbacBusinessSystemRelation");

				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(rbacBusinessSystemRelation);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(rbacBusinessSystemRelation);
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

		if (this.rbacBusinessSystemRelationTree.getSelectedItem() != null) {
			Treechildren treechildren = this.rbacBusinessSystemRelationTree
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
						rbacBusinessSystemRelationManager
								.removeRbacBusinessSystemRelation(rbacBusinessSystemRelation);
						Treechildren treechildren = (Treechildren) rbacBusinessSystemRelationTree
								.getSelectedItem().getParent();
						treechildren.removeChild(rbacBusinessSystemRelationTree
								.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						rbacBusinessSystemRelation = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(
								RolePermissionConstants.ON_DEL_NODE_OK,
								RbacBusinessSystemRelationTreeExt.this, null);
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
	public void onSelect$rbacBusinessSystemRelationTree() throws Exception {
		selectedTreeitem = this.rbacBusinessSystemRelationTree
				.getSelectedItem();
		rbacBusinessSystemRelation = (RbacBusinessSystemRelation) ((TreeNodeImpl) this.rbacBusinessSystemRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (rbacBusinessSystemRelation != null
				&& rbacBusinessSystemRelation.getRbacBusinessSystemRelaId() != null) {
			rbacBusinessSystem = rbacBusinessSystemRelation
					.getRbacBusinessSystem();
		}
		Events.postEvent(
				RolePermissionConstants.ON_SELECT_RBAC_BUSINESS_SYSTEM_TREE_REQUEST,
				this, rbacBusinessSystemRelation);
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
	 * 供外层主动获取控件选择的业务系统
	 */
	public RbacBusinessSystemRelation getSelectRbacBusinessSystemRelation() {
		return rbacBusinessSystemRelation;
	}

	/**
	 * 判断名称是否要修改
	 */
	public void onSaveRbacBusinessSystemInfoResponse(ForwardEvent event)
			throws Exception {

		if (event.getOrigin().getData() != null) {

			RbacBusinessSystem rbacBusinessSystem = (RbacBusinessSystem) event
					.getOrigin().getData();
			/**
			 * 修改名称
			 */
			this.rbacBusinessSystemRelationTree.getSelectedItem().setLabel(
					rbacBusinessSystem.getRbacBusinessSystemName());
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
		} else if ("rbacBusinessSystemTreePage".equals(page)) {
			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.RBAC_BUSINESS_SYSTEM_TREE_RBAC_BUSINESS_SYSTEM_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.RBAC_BUSINESS_SYSTEM_TREE_RBAC_BUSINESS_SYSTEM_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil
					.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.RBAC_BUSINESS_SYSTEM_TREE_RBAC_BUSINESS_SYSTEM_DEL)) {
				canDel = true;
			}
		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(canDel);
	}
}
