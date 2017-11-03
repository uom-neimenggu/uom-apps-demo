package cn.ffcs.uom.gridUnit.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.gridUnit.action.bean.GridUnitTreeMainBean;
import cn.ffcs.uom.gridUnit.model.GridUnitRef;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;

@Controller
@Scope("prototype")
public class GridUnitTreeMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8064419972929637883L;

	/**
	 * 页面bean
	 */
	private GridUnitTreeMainBean bean = new GridUnitTreeMainBean();
	/**
	 * 选中的组织
	 */
	private Organization organization;
	/**
	 * 选中的组织关系
	 */
	private GridUnitRef gridUnitRef;

	/**
	 * 推导树节点控制
	 */
	private treeCalcAction treeCalcVo;

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		this.bean.getMarketingTreeExt().setPortletInfoProvider(this);
		this.bean.getMarketingTreeExt().setVariablePagePosition(
				"gridUnitTreePage");
		this.bean.getOrganizationInfoEditExt().setPortletInfoProvider(this);
		this.bean.getGridUnitRefListboxExt().setPortletInfoProvider(this);
		this.bean.getStaffOrgTranListboxExt().setPortletInfoProvider(this);
		this.bean.getStaffOrgTranListboxExt().setIsOrgTreePage(true);

		/**
		 * 选中组织树上的组织
		 */
		this.bean
				.getMarketingTreeExt()
				.addForward(
						OrganizationConstant.ON_SELECT_MARKETING_ORGANIZATION_TREE_REQUEST,
						this.self, "onSelectOrganizationRelationTreeResponse");
		/**
		 * 删除节点成功事件
		 */
		this.bean.getMarketingTreeExt().addForward(
				OrganizationConstant.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 组织关系信息选择事件
		 */
		this.bean.getGridUnitRefListboxExt().addForward(
				OrganizationConstant.ON_GIRD_UNIT_REF_SELECT, this.self,
				"onSelectGirdUnitRefResponse");

	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$gridUnitTreeMainWindow() throws Exception {
		initLeftTab();
		initPage();
		initLeftTabControlRightPage();
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getMarketingTreeExt().setPagePosition("gridUnitTreePage");
		/**
		 * 组织信息编辑页面只显示营销组织的类型
		 */
		List<String> optionAttrValueList = new ArrayList<String>();
		optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101040000);
		optionAttrValueList.add(OrganizationConstant.ORG_TYPE_N1101050000);

		this.bean.getOrganizationInfoEditExt().getBean()
				.getOrganizationInfoExt().getBean().getOrgTypeCd()
				.setOptionNodes(optionAttrValueList);
	}

	/**
	 * 20140612设置tab页用来区分不同tab页的功能权
	 */
	private void initLeftTabControlRightPage() throws Exception {
		String selectedTabId = "gridUnitTab";
		// 控制组织层级权限
		this.bean.getMarketingTreeExt().setPagePosition("gridUnitTreePage");
		this.bean.getOrganizationInfoEditExt().setOrgTreeTabName(selectedTabId);
		this.bean.getStaffOrgTranListboxExt().setOrgTreeTabName(selectedTabId);
	}

	/**
	 * 初始化tab页权限
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void initLeftTab() throws Exception {
		boolean checkMarketingTabResult = PlatformUtil.checkHasPermission(this,
				ActionKeys.GRID_UNIT_TAB);
		if (!checkMarketingTabResult) {
			this.bean.getTempTab().setVisible(true);
			this.bean.getTempTab().setSelected(true);
			this.bean.getGridUnitTab().setVisible(false);
			ZkUtil.showExclamation("您没有任何菜单权限,请配置", "警告");
		} else {
			this.bean.getGridUnitTab().setSelected(true);
		}
		bean.setLeftSelectTab(this.bean.getLeftTabbox().getSelectedTab());
	}

	/**
	 * 选择员工组织列表的响应处理
	 */
	public void onSelectGirdUnitRefResponse(final ForwardEvent event)
			throws Exception {
		gridUnitRef = (GridUnitRef) event.getOrigin().getData();
		callGridUnitTab();
	}

	public void callGridUnitTab() throws Exception {
		if (this.bean.getTab() == null) {
			this.bean.setTab(this.bean.getTabBoxGridUnit().getSelectedTab());
		}
		if (gridUnitRef != null) {
			String tab = this.bean.getTab().getId();
			if ("staffOrgTranTab".equals(tab)) {
				bean.getStaffOrgTranListboxExt().setGridUnitRef(gridUnitRef);
				bean.getStaffOrgTranListboxExt().setOrganization(organization);
				this.bean.getStaffOrgTranListboxExt()
						.setVariableOrgTreeTabName("gridUnitTreeTab");
				StaffOrganizationTran staffOrganizationTran = new StaffOrganizationTran();
				staffOrganizationTran.setOrgId(gridUnitRef.getOrgId());
				bean.getStaffOrgTranListboxExt().setStaffOrgTrantButtonValid(
						true, false, false);
				Events.postEvent(
						OrganizationConstant.ON_STAFF_ORGANIZATION_TRAN_QUERY,
						this.bean.getStaffOrgTranListboxExt(),
						staffOrganizationTran);
			}
		} else {
			bean.getStaffOrgTranListboxExt().setStaffOrgTrantButtonValid(false,
					false, false);
			bean.getStaffOrgTranListboxExt().onCleaningStaffOrgTran();
		}
	}

	/**
	 * 直接点击Tab页. .
	 */
	public void onClickGridUnitTab(ForwardEvent event) throws Exception {
		Event origin = event.getOrigin();
		if (origin != null) {
			Component comp = origin.getTarget();
			if (comp != null && comp instanceof Tab) {
				bean.setTab((Tab) comp);
				callGridUnitTab();
			}
		}
	}

	/**
	 * 点击tab
	 * 
	 * @throws Exception
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setSelectTab(clickTab);
				callTab();
			}
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void callTab() throws Exception {
		if (this.bean.getSelectTab() == null) {
			bean.setSelectTab(this.bean.getTabBox().getSelectedTab());
		}
		if (organization != null) {
			if ("orgAttrTab".equals(this.bean.getSelectTab().getId())) {
				this.bean.getOrganizationInfoEditExt()
						.setVariableOrgTreeTabName("gridUnitTreeTab");
				Events.postEvent(
						OrganizationConstant.ON_SELECT_TREE_ORGANIZATION,
						this.bean.getOrganizationInfoEditExt(), organization);
			}
			if ("gridUnitRefTab".equals(this.bean.getSelectTab().getId())) {
				this.bean.getGridUnitRefListboxExt().setVariableOrgTreeTabName(
						"gridUnitTreeTab");
				GridUnitRef gridUnitRef = new GridUnitRef();
				gridUnitRef.setRelaOrgId(organization.getOrgId());
				Events.postEvent(OrganizationConstant.ON_GRID_UNIT_REF_QUERY,
						this.bean.getGridUnitRefListboxExt(), gridUnitRef);
				this.bean.getStaffOrgTranListboxExt().onCleaningStaffOrgTran();
				bean.getStaffOrgTranListboxExt().setStaffOrgTrantButtonValid(
						false, false, false);

			}
		} else {
			/**
			 * 切换左边tab页的时候，未选择组织树上的组织，清理数据等操作
			 */
			if ("orgAttrTab".equals(this.bean.getSelectTab().getId())) {
				this.bean.getOrganizationInfoEditExt()
						.setVariableOrgTreeTabName("gridUnitTreeTab");
				Events.postEvent(
						OrganizationConstant.ON_SELECT_TREE_ORGANIZATION,
						this.bean.getOrganizationInfoEditExt(), null);
			}
			if ("gridUnitRefTab".equals(this.bean.getSelectTab().getId())) {
				this.bean.getGridUnitRefListboxExt().setVariableOrgTreeTabName(
						"gridUnitTreeTab");
				Events.postEvent(OrganizationConstant.ON_GRID_UNIT_REF_QUERY,
						this.bean.getGridUnitRefListboxExt(), null);
				this.bean.getStaffOrgTranListboxExt().onCleaningStaffOrgTran();
				bean.getStaffOrgTranListboxExt().setStaffOrgTrantButtonValid(
						false, false, false);

			}
		}
	}

	/**
	 * 选择组织树
	 * 
	 * @param event
	 */
	public void onSelectOrganizationRelationTreeResponse(ForwardEvent event)
			throws Exception {
		OrganizationRelation organizationRelation = (OrganizationRelation) event
				.getOrigin().getData();
		/**
		 * 推导树节点数据,其他为null
		 */
		treeCalcVo = organizationRelation.getTreeCalcVo();
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
			callTab();
		}
	}

	/**
	 * 点击左边的tab
	 * 
	 * @param forwardEvent
	 * @throws Exception
	 */
	public void onClickLeftTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				bean.setLeftSelectTab(clickTab);
				callLeftTab();
			}
		}
	}

	/**
	 * 
	 */
	public void callLeftTab() throws Exception {

		if (this.bean.getLeftSelectTab() == null) {
			bean.setLeftSelectTab(this.bean.getLeftTabbox().getSelectedTab());
		}

		OrganizationRelation organizationRelation = null;

		if ("gridUnitTab".equals(bean.getLeftSelectTab().getId())) {
			organizationRelation = this.bean.getMarketingTreeExt()
					.getSelectOrganizationOrganization();
		}

		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
		}

		callTab();
	}

	/**
	 * 删除节点事件,属性tab清空
	 * 
	 * @throws Exception
	 */
	public void onDelNodeResponse() throws Exception {
		this.callLeftTab();
	}

	/**
	 * 选择推导树
	 * 
	 * @throws Exception
	 */
	public void onSelectTreeTypeResponse(ForwardEvent event) throws Exception {
		boolean isDuceTree = false;
		if (event.getOrigin().getData() != null) {
			isDuceTree = (Boolean) event.getOrigin().getData();
		}
		this.setTreeTypeToRightTab(isDuceTree);
		callLeftTab();
	}

	/**
	 * 设置右边属性tab是否是推导树
	 */
	private void setTreeTypeToRightTab(boolean isDuceTree) {
		this.bean.getOrganizationInfoEditExt().setDuceTree(isDuceTree);
		this.bean.getGridUnitRefListboxExt().setDuceTree(isDuceTree);
	}

}
