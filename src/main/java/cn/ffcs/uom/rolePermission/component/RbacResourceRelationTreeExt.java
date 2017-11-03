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
import cn.ffcs.uom.rolePermission.manager.RbacResourceRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacResource;
import cn.ffcs.uom.rolePermission.model.RbacResourceRelation;

public class RbacResourceRelationTreeExt extends Div implements IdSpace {

	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_resource_relation_tree_ext.zul";

	/**
	 * manager
	 */
	private RbacResourceRelationManager rbacResourceRelationManager = (RbacResourceRelationManager) ApplicationContextUtil
			.getBean("rbacResourceRelationManager");

	/**
	 * 资源树
	 */
	private RbacResourceRelationTree rbacResourceRelationTree;

	/**
	 * 选中的资源TreeItem
	 */
	@SuppressWarnings("unused")
	private Treeitem selectedTreeitem;

	/**
	 * 选中的资源
	 */
	private RbacResource rbacResource;

	/**
	 * 选中的资源关系
	 */
	private RbacResourceRelation rbacResourceRelation;

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

	public RbacResourceRelationTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 资源名称修改要改资源树节点名称
		 */
		RbacResourceRelationTreeExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_RESOURCE_INFO,
				RbacResourceRelationTreeExt.this,
				"onSaveRbacResourceInfoResponse");
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
	private void onAddRootNodeResponse(RbacResourceRelation rbacResourceRelation)
			throws Exception {
		Treechildren treechildren = this.rbacResourceRelationTree
				.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(rbacResourceRelation.getLabel());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
				rbacResourceRelation);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}

	/**
	 * 增加子节点
	 */
	public void onAddChildNode() throws Exception {
		if (this.rbacResourceRelationTree.getSelectedItem() != null) {
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
			RbacResourceRelation rbacResourceRelation) throws Exception {

		Treechildren treechildren = this.rbacResourceRelationTree
				.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.rbacResourceRelationTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.rbacResourceRelationTree.getSelectedItem().setValue(
					parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacResourceRelation.getLabel());

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacResourceRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.rbacResourceRelationTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacResourceRelation.getLabel());
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacResourceRelation);
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
		map.put("rbacResource", rbacResource);
		Window win = (Window) Executions
				.createComponents(
						"/pages/rolePermission/rbac_resource_relation_tree_node_edit.zul",
						this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

				Map dataMap = (Map) event.getData();

				RbacResourceRelation rbacResourceRelation = (RbacResourceRelation) dataMap
						.get("rbacResourceRelation");

				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(rbacResourceRelation);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(rbacResourceRelation);
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

		if (this.rbacResourceRelationTree.getSelectedItem() != null) {
			Treechildren treechildren = this.rbacResourceRelationTree
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
						rbacResourceRelationManager
								.removeRbacResourceRelation(rbacResourceRelation);
						Treechildren treechildren = (Treechildren) rbacResourceRelationTree
								.getSelectedItem().getParent();
						treechildren.removeChild(rbacResourceRelationTree
								.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						rbacResourceRelation = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(
								RolePermissionConstants.ON_DEL_NODE_OK,
								RbacResourceRelationTreeExt.this, null);
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
	public void onSelect$rbacResourceRelationTree() throws Exception {
		selectedTreeitem = this.rbacResourceRelationTree.getSelectedItem();
		rbacResourceRelation = (RbacResourceRelation) ((TreeNodeImpl) this.rbacResourceRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (rbacResourceRelation != null
				&& rbacResourceRelation.getRbacResourceRelaId() != null) {
			rbacResource = rbacResourceRelation.getRbacResource();
		}
		Events.postEvent(
				RolePermissionConstants.ON_SELECT_RBAC_RESOURCE_TREE_REQUEST,
				this, rbacResourceRelation);
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
	 * 供外层主动获取控件选择的资源
	 */
	public RbacResourceRelation getSelectRbacResourceRelation() {
		return rbacResourceRelation;
	}

	/**
	 * 判断名称是否要修改
	 */
	public void onSaveRbacResourceInfoResponse(ForwardEvent event)
			throws Exception {

		if (event.getOrigin().getData() != null) {

			RbacResource rbacResource = (RbacResource) event.getOrigin()
					.getData();
			/**
			 * 修改名称
			 */
			this.rbacResourceRelationTree.getSelectedItem().setLabel(
					rbacResource.getRbacResourceName());
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
		} else if ("rbacResourceTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_RESOURCE_TREE_RBAC_RESOURCE_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_RESOURCE_TREE_RBAC_RESOURCE_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_RESOURCE_TREE_RBAC_RESOURCE_DEL)) {
				canDel = true;
			}

		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(canDel);
	}
}
