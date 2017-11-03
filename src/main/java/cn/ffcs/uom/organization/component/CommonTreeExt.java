package cn.ffcs.uom.organization.component;

import java.util.ArrayList;
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
import org.zkoss.zul.Combobox;
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
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ComboboxUtils;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public class CommonTreeExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 532521498062036747L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/common_tree_ext.zul";
	/**
	 * 组织树
	 */
	private OrganizationRelationTree agentOrganizationRelationTree;
	/**
	 * 组织树下拉框
	 */
	@Getter
	@Setter
	private Combobox orgTreeComb;
	/**
	 * 组织树根节点
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

	public CommonTreeExt() {
		// 1. Create components (optional)
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		this.setButtonValid(true, false, false);
		try {
		    bindCombobox();
        } catch (Exception e) {
            e.printStackTrace();        
        }
	}

	public void onCreate() {
		this.agentOrganizationRelationTree.setVisible(false);
	}
    /**
     * 绑定下拉框
     * 
     * @throws Exception
     */
    private void bindCombobox() throws Exception {
        String sql = "SELECT * FROM ORG_TREE T WHERE T.STATUS_CD=? AND T.ORG_TREE_ID IN(SELECT ORG_TREE_ID FROM ORG_REL_ROOT_CONFIG A WHERE A.STATUS_CD=?)";
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        List<OrgTree> orgTreeList = OrgTree.repository().jdbcFindList(sql,
                params, OrgTree.class);
        List<NodeVo> nodeVoList = new ArrayList<NodeVo>();
        for (int i = 0; i < orgTreeList.size(); i++) {
            OrgTree orgTree = orgTreeList.get(i);
            NodeVo nodeVo = new NodeVo();
            nodeVo.setId(StrUtil.strnull(orgTree.getOrgTreeId()));
            nodeVo.setName(orgTree.getOrgTreeName());
            nodeVoList.add(nodeVo);
        }
        ComboboxUtils.rendererForQuery(this.getOrgTreeComb(), nodeVoList);
        //this.getOrgTreeComb().setSelectedIndex(0);
    }
    public void onSelectOrgTree(){
        Long orgTreeId =  Long.parseLong((String) this.getOrgTreeComb().getSelectedItem().getValue());
        OrganizationRelation orgRel = this.organizationRelationManager.getOrgRelByTreeId(orgTreeId);
        if (orgTreeId != null) {
            agentOrganizationRelationTree.setRootId(orgRel.getOrgId());
            agentOrganizationRelationTree.setRelaCdStr(orgRel.getRelaCd());
            agentOrganizationRelationTree.bindTree();
            agentOrganizationRelationTree.setVisible(true);
        } else {
            agentOrganizationRelationTree.setVisible(false);
        }
    }
	/**
	 * 选择根节点
	 * 
	 * @throws Exception
	 */
	public void onChanging$organizationRoot() throws Exception {
		this.setButtonValid(true, false, false);
		organization = this.organizationRoot.getOrganization();
		if (organization != null) {
			agentOrganizationRelationTree.setRootId(organization.getOrgId());
			agentOrganizationRelationTree.bindTree();
			agentOrganizationRelationTree.setVisible(true);
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
		this.setButtonValid(true, true, true);
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
				ActionKeys.DATA_OPERATING)){
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
			ZkUtil.showQuestion("你确定要删除该节点吗？", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						organizationRelationManager
								.removeOrganizationRelation(organizationRelation);
						Treechildren treechildren = (Treechildren) agentOrganizationRelationTree
								.getSelectedItem().getParent();
						treechildren.removeChild(agentOrganizationRelationTree
								.getSelectedItem());
						if (treechildren.getChildren().size() == 0) {
							treechildren.getParent().removeChild(treechildren);
						}
						organizationRelation = null;
						/**
						 * 抛出删除节点成功事件
						 */
						Events.postEvent(OrganizationConstant.ON_DEL_NODE_OK,
								CommonTreeExt.this, null);
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
		} else if ("commonTreePage".equals(page)) {
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
		this.getAddRootButton().setVisible(false);
		this.getAddChildButton().setVisible(false);
		this.getDelChildButton().setVisible(false);
	}

}
