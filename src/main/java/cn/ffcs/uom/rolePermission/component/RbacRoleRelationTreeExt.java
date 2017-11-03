package cn.ffcs.uom.rolePermission.component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.manager.RbacRoleRelationManager;
import cn.ffcs.uom.rolePermission.model.RbacRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;

public class RbacRoleRelationTreeExt extends Div implements IdSpace {

	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/rolePermission/comp/rbac_role_relation_tree_ext.zul";

	/**
	 * manager
	 */
	private RbacRoleRelationManager rbacRoleRelationManager = (RbacRoleRelationManager) ApplicationContextUtil
			.getBean("rbacRoleRelationManager");
	
	
	/**
     * 日志服务队列
     */
    @Qualifier("logService")
    @Autowired
    private LogService logService;
	
	/**
	 * 角色树
	 */
	private RbacRoleRelationTree rbacRoleRelationTree;

	/**
	 * 选中的角色TreeItem
	 */
	@SuppressWarnings("unused")
	private Treeitem selectedTreeitem;

	/**
	 * 选中的角色
	 */
	private RbacRole rbacRole;

	/**
	 * 选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

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

	public RbacRoleRelationTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 角色名称修改要改角色树节点名称
		 */
		RbacRoleRelationTreeExt.this.addForward(
				RolePermissionConstants.ON_SAVE_RBAC_ROLE_INFO,
				RbacRoleRelationTreeExt.this, "onSaveRbacRoleInfoResponse");
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
	private void onAddRootNodeResponse(RbacRoleRelation rbacRoleRelation)
			throws Exception {
		Treechildren treechildren = this.rbacRoleRelationTree.getTreechildren();
		Treeitem treeitem = new Treeitem();
		Treerow treerow = new Treerow();
		Treecell treecell = new Treecell(rbacRoleRelation.getLabel());
		treecell.setParent(treerow);
		treerow.setParent(treeitem);
		TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
				rbacRoleRelation);
		treeitem.setValue(treeNodeImpl);
		treechildren.appendChild(treeitem);
	}

	/**
	 * 增加子节点
	 */
	public void onAddChildNode() throws Exception {
		if (this.rbacRoleRelationTree.getSelectedItem() != null) {
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
	private void onAddChildNodeResponse(RbacRoleRelation rbacRoleRelation)
			throws Exception {

		Treechildren treechildren = this.rbacRoleRelationTree.getSelectedItem()
				.getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.rbacRoleRelationTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.rbacRoleRelationTree.getSelectedItem().setValue(
					parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacRoleRelation.getLabel());

			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacRoleRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.rbacRoleRelationTree.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(rbacRoleRelation.getLabel());
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					rbacRoleRelation);
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
		map.put("rbacRole", rbacRole);
		Window win = (Window) Executions.createComponents(
				"/pages/rolePermission/rbac_role_relation_tree_node_edit.zul",
				this, map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

				Map dataMap = (Map) event.getData();

				RbacRoleRelation rbacRoleRelation = (RbacRoleRelation) dataMap
						.get("rbacRoleRelation");

				if (map.get("opType").equals("addRootNode")) {
					onAddRootNodeResponse(rbacRoleRelation);
				} else if (map.get("opType").equals("addChildNode")) {
					onAddChildNodeResponse(rbacRoleRelation);
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

		if (this.rbacRoleRelationTree.getSelectedItem() != null) {
			Treechildren treechildren = this.rbacRoleRelationTree
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
					    /**
				         * 开始日志添加操作
				         * 添加日志到队列需要：
				         * 业务开始时间，日志消息类型，错误编码和描述
				         */
				        SysLog log = new SysLog();
				        log.startLog(new Date(), SysLogConstrants.ROLE);
						rbacRoleRelationManager
								.removeRbacRoleRelation(rbacRoleRelation);
						
						Treechildren treechildren = (Treechildren) rbacRoleRelationTree
								.getSelectedItem().getParent();
						treechildren.removeChild(rbacRoleRelationTree
								.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						rbacRoleRelation = null;
						Class clazz[] ={RbacRoleRelation.class};
					    log.endLog(logService, clazz, SysLogConstrants.DEL, SysLogConstrants.INFO, "角色信息删除记录日志");
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(
								RolePermissionConstants.ON_DEL_NODE_OK,
								RbacRoleRelationTreeExt.this, null);
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
	public void onSelect$rbacRoleRelationTree() throws Exception {
		selectedTreeitem = this.rbacRoleRelationTree.getSelectedItem();
		rbacRoleRelation = (RbacRoleRelation) ((TreeNodeImpl) this.rbacRoleRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (rbacRoleRelation != null
				&& rbacRoleRelation.getRbacRoleRelaId() != null) {
			rbacRole = rbacRoleRelation.getRbacRole();
		}
		Events.postEvent(
				RolePermissionConstants.ON_SELECT_RBAC_ROLE_TREE_REQUEST, this,
				rbacRoleRelation);
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
	 * 供外层主动获取控件选择的角色
	 */
	public RbacRoleRelation getSelectRbacRoleRelation() {
		return rbacRoleRelation;
	}

	/**
	 * 判断名称是否要修改
	 */
	public void onSaveRbacRoleInfoResponse(ForwardEvent event) throws Exception {

		if (event.getOrigin().getData() != null) {

			RbacRole rbacRole = (RbacRole) event.getOrigin().getData();
			/**
			 * 修改名称
			 */
			this.rbacRoleRelationTree.getSelectedItem().setLabel(
					rbacRole.getRbacRoleName());
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
		} else if ("rbacRoleTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TREE_RBAC_ROLE_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TREE_RBAC_ROLE_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.RBAC_ROLE_TREE_RBAC_ROLE_DEL)) {
				canDel = true;
			}

		}
		this.getAddRootButton().setVisible(canAddRoot);
		this.getAddChildButton().setVisible(canAddChild);
		this.getDelChildButton().setVisible(canDel);
	}
}
