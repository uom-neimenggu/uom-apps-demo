package cn.ffcs.uom.organization.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public class AgentTreeExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/agent_tree_ext.zul";
	/**
	 * 组织树
	 */
	private OrganizationRelationTree agentOrganizationRelationTree;
	/**
	 * 组织树跟节点
	 */
	private OrganizationBandboxExt organizationRoot;
	/**
	 * 根组织
	 */
	private Organization organization;
	/**
	 * 选中的组织
	 */
	private OrganizationRelation organizationRelation;
	/**
	 * manager
	 */
	private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
			.getBean("organizationRelationManager");

	private MdsionOrgRelationManager mdsionOrgRelationManager = (MdsionOrgRelationManager) ApplicationContextUtil
			.getBean("mdsionOrgRelationManager");

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

	public AgentTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		// this.setButtonValid(true, false, false);
	}

	public void onCreate() {
		this.agentOrganizationRelationTree.setVisible(false);
	}

	/**
	 * 选择根节点
	 * 
	 * @throws Exception
	 */
	public void onChanging$organizationRoot() throws Exception {
		// this.setButtonValid(true, false, false);
		organization = this.organizationRoot.getOrganization();
		OrgType orgType = new OrgType();
		OrgType orgType1 = new OrgType();
		OrgType orgType2 = new OrgType();
		OrgType orgType3 = new OrgType();
		OrgType orgType4 = new OrgType();
		OrgType orgType5 = new OrgType();
		OrgType orgType6 = new OrgType();
		orgType.setOrgTypeCd(OrganizationConstant.ORG_TYPE_AGENT);
		orgType1.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202010000);
		orgType2.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202020000);
		orgType3.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202030000);
		orgType4.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202040000);
		orgType5.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202050000);
		orgType6.setOrgTypeCd(OrganizationConstant.ORG_TYPE_N0202060000);
		List<OrgType> list = organization.getOrgTypeList();
		if (organization != null) {
			if (list.contains(orgType)) {
				agentOrganizationRelationTree
						.setRootId(organization.getOrgId());
				agentOrganizationRelationTree.bindTree();
				agentOrganizationRelationTree.setVisible(true);
			} else if (list.contains(orgType1) || list.contains(orgType2)
					|| list.contains(orgType3) || list.contains(orgType4)
					|| list.contains(orgType5) || list.contains(orgType6)) {
				OrganizationRelation or = new OrganizationRelation();
				or.setOrgId(organization.getOrgId());
				or.setRelaCd(OrganizationConstant.RELA_CD_EXTER);
				List<Organization> orgList = organization.getParentOrgList(or);
				if (orgList != null && orgList.size() > 0) {
					agentOrganizationRelationTree.setRootId(orgList.get(0)
							.getOrgId());
					agentOrganizationRelationTree.bindTree();
					agentOrganizationRelationTree.setVisible(true);
				} else {
					agentOrganizationRelationTree.setVisible(false);
				}
			} else {
				agentOrganizationRelationTree
						.setRootId(organization.getOrgId());
				agentOrganizationRelationTree.bindTree();
				agentOrganizationRelationTree.setVisible(true);
			}
		} else {
			agentOrganizationRelationTree.setVisible(false);
		}
		/**
		 * 切换树时选择的组织节点清空
		 */
		Events.postEvent(OrganizationConstant.ON_SELECT_TREE_TYPE, this, false);
		organizationRelation = null;

	}

	/**
	 * 选择树
	 * 
	 * @throws Exception
	 */
	public void onSelect$agentOrganizationRelationTree() throws Exception {
		organizationRelation = (OrganizationRelation) ((TreeNodeImpl) this.agentOrganizationRelationTree
				.getSelectedItem().getValue()).getEntity();
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
		}
		Events.postEvent(
				OrganizationConstant.ON_SELECT_AGENT_ORGANIZATION_TREE_REQUEST,
				this, organizationRelation);
		// this.setButtonValid(true, true, true);
	}

	/**
	 * 供外层主动获取控件选择的组织
	 */
	public OrganizationRelation getSelectOrganizationOrganization() {
		return organizationRelation;
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	public void onAddRootNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		String opType = "addAgentRootNode";
		this.openAddNodeWindow(opType);
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	private void onAddRootNodeResponse(OrganizationRelation organizationRelation)
			throws Exception {
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			this.agentOrganizationRelationTree.setRootId(organizationRelation
					.getOrgId());
			this.agentOrganizationRelationTree.bindTree();
			this.agentOrganizationRelationTree.setVisible(true);
		}
	}

	/**
	 * 增加子节点
	 * 
	 * @throws Exception
	 */
	public void onAddChildNode() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		if (this.agentOrganizationRelationTree.getSelectedItem() != null) {
			String opType = "addAgentChildNode";
			this.openAddNodeWindow(opType);
		} else {
			ZkUtil.showError("请选择节点", "提示信息");
		}
	}

	/**
	 * 增加根节点
	 * 
	 * @throws Exception
	 */
	private void onAddChildNodeResponse(
			OrganizationRelation organizationRelation) throws Exception {
		Treechildren treechildren = this.agentOrganizationRelationTree
				.getSelectedItem().getTreechildren();
		// 没有下级
		if (treechildren == null) {
			/**
			 * 父节点设置下级孩子为null让其查库，避免增加了节点不展示的问题
			 */
			TreeNodeImpl parentTreeNodeImpl = (TreeNodeImpl) this.agentOrganizationRelationTree
					.getSelectedItem().getValue();
			parentTreeNodeImpl.setChildren(null);
			this.agentOrganizationRelationTree.getSelectedItem().setValue(
					parentTreeNodeImpl);

			Treechildren tchild = new Treechildren();
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(organizationRelation.getLabel());
			/**
			 * 单位label样式
			 */
			if (organizationRelation.isCompany()) {
				tcell.setStyle("color:blue");
			}
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					organizationRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(tchild);
			tchild.setParent(this.agentOrganizationRelationTree
					.getSelectedItem());
		} else {
			// 已存在下级
			Treeitem titem = new Treeitem();
			Treerow trow = new Treerow();
			Treecell tcell = new Treecell(organizationRelation.getLabel());
			/**
			 * 单位类label样式
			 */
			if (organizationRelation.isCompany()) {
				tcell.setStyle("color:blue");
			}
			tcell.setParent(trow);
			trow.setParent(titem);
			TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
					organizationRelation);
			titem.setValue(treeNodeImpl);
			titem.setParent(treechildren);
		}
	}

	/**
	 * 打开界面
	 * 
	 * @param opType
	 */
	private void openAddNodeWindow(String opType) throws Exception {
		final Map map = new HashMap();
		map.put("opType", opType);
		map.put("organization", organization);
		Window win = (Window) Executions.createComponents(
				"/pages/organization/organization_tree_node_edit.zul", this,
				map);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Map dataMap = (Map) event.getData();
				OrganizationRelation organizationRelation = (OrganizationRelation) dataMap
						.get("organizationRelation");
				if (map.get("opType").equals("addAgentRootNode")) {
					onAddRootNodeResponse(organizationRelation);
				} else if (map.get("opType").equals("addAgentChildNode")) {
					onAddChildNodeResponse(organizationRelation);
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
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		if (this.agentOrganizationRelationTree.getSelectedItem() != null) {
			Treechildren treechildren = this.agentOrganizationRelationTree
					.getSelectedItem().getTreechildren();
			if (treechildren != null) {
				ZkUtil.showError("存在下级节点,不能删除", "提示信息");
				return;
			}

			Window window = (Window) Executions.createComponents(
					"/pages/common/del_reason_input.zul", this, null);
			window.doModal();
			window.addEventListener(Events.ON_OK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						String reason = (String) event.getData();

						organizationRelation.setReason(reason);

						organizationRelationManager
								.removeOrganizationRelation(organizationRelation);
						/**
						 * 删除多维组织关系
						 */
						MdsionOrgRelation orgRela = MdsionOrgRelation
								.newInstance();
						orgRela.setRelaCd(organizationRelation.getRelaCd());

						MdsionOrgTree mdsionOrgTree = mdsionOrgRelationManager
								.getMdsionOrgTree(orgRela);

						if (mdsionOrgTree != null
								&& mdsionOrgTree.getMdsionOrgRelTypeCd()
										.equals(organizationRelation
												.getRelaCd())) {
							orgRela.setOrgId(organizationRelation.getOrgId());
							orgRela.setRelaOrgId(organizationRelation
									.getRelaOrgId());
							orgRela = mdsionOrgRelationManager
									.queryMdsionOrgRelation(orgRela);
							if (orgRela != null
									&& orgRela.getMdsionOrgRelId() != null) {
								mdsionOrgRelationManager
										.removeMdsionOrgRelation(orgRela);
							}
						}
						Treechildren treechildren = (Treechildren) agentOrganizationRelationTree
								.getSelectedItem().getParent();
						treechildren.removeChild(agentOrganizationRelationTree
								.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						organizationRelation = null;
						
						Messagebox.show("删除节点成功");
						
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(OrganizationConstant.ON_DEL_NODE_OK,
								AgentTreeExt.this, null);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的节点", "提示信息");
		}
	}

	/**
	 * 设置按钮状态
	 * 
	 * @param canAddRoot
	 * @param canAddChild
	 * @param canDel
	 */
	private void setButtonValid(boolean canAddRoot, boolean canAddChild,
			boolean canDel) {
		this.addRootButton.setDisabled(!canAddRoot);
		this.addChildButton.setDisabled(!canAddChild);
		this.delChildButton.setDisabled(!canDel);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAddRoot = false;
		boolean canAddChild = false;
		boolean candelChild = false;

		if (PlatformUtil.isAdmin()) {
			canAddRoot = true;
			canAddChild = true;
			candelChild = true;
		} else if ("agentTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.AGENT_TREE_ORG_ADD_ROOT)) {
				canAddRoot = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.AGENT_TREE_ORG_ADD_CHILD)) {
				canAddChild = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.AGENT_TREE_ORG_DEL)) {
				candelChild = true;
			}
		}
		// this.getAddRootButton().setVisible(canAddRoot);
		// this.getAddChildButton().setVisible(canAddChild);
		// this.getDelChildButton().setVisible(candelChild);
	}

}
