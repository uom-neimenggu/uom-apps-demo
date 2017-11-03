package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.action.bean.OrganizationCustmsTreeMainBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.manager.OrgTypeManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

@Controller
@Scope("prototype")
public class OrganizationTreeCustmsMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8064419972929637883L;

	/**
	 * 页面bean
	 */
	private OrganizationCustmsTreeMainBean bean = new OrganizationCustmsTreeMainBean();
	/**
	 * 选中的组织
	 */
	private Organization organization;

	/**
	 * 组织信息窗口
	 */

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil.getBean("staffManager");
	
	@Autowired
	@Qualifier("orgTypeManager")
	private OrgTypeManager orgTypeManager = (OrgTypeManager)ApplicationContextUtil.getBean("orgTypeManager");

	/**
	 * 组织参与人
	 */
	private Party party;

	/**
	 * 选择组织员工返回的组织参与人
	 */
	private Party returParty;

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
		this.bean.getOrganizationInfoEditExt().setPortletInfoProvider(this);
		this.bean.getOrganizationRelationListbox().setPortletInfoProvider(this);
		this.bean.getStaffOrganizationListbox().setPortletInfoProvider(this);
		this.bean.getStaffOrganizationListbox().setIsOrgTreePage(true);
		this.bean.getOrganizationRelationListbox().setIsOrgTreePage(true);
		this.bean.getOrganizationPositionListbox().setPortletInfoProvider(this);
		this.bean.getOrganizationPartyAttrExt().setPortletInfoProvider(this);
		this.bean.getPartyContactInfoListboxExt().setPortletInfoProvider(this);
		this.bean.getPartyCertificationListboxExt().setPortletInfoProvider(this);
		this.bean.getStaffPositionListboxExt().setPortletInfoProvider(this);
		this.bean.getEdwTreeExt().setPortletInfoProvider(this);
		
		/**
		 * 选中组织树上的组织
		 */
		this.bean.getEdwTreeExt().addForward(OrganizationConstant.ON_SELECT_EDW_ORGANIZATION_TREE_REQUEST,
				this.self, "onSelectOrganizationRelationTreeResponse");
		/**
		 * 删除节点成功事件
		 */
		this.bean.getEdwTreeExt().addForward(OrganizationConstant.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 组织信息保存事件
		 */
		this.bean.getOrganizationInfoEditExt().addForward(OrganizationConstant.ON_SAVE_ORGANIZATION_INFO, this.self,
				"onSaveOrganizationInfoResponse");
		/**
		 * 组织员工信息选择事件
		 */
		this.bean.getStaffOrganizationListbox().addForward(OrganizationConstant.ON_STAFFORGANIZATION_SELECT, this.self,
				"onSelectStaffOrganizationResponse");		
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$organizationCustmsTreeMainWindow() throws Exception {
		initLeftTab();
		initPage();
		initLeftTabControlRightPage();
	}
	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getEdwTreeExt().setPagePosition("edwTreePage");
//		this.bean.getOrganizationInfoEditExt().setPagePosition("orgTreePage");
//		this.bean.getOrganizationRelationListbox().setPagePosition("orgTreePage");
//		this.bean.getStaffOrganizationListbox().setPagePosition("orgTreePage");
//		this.bean.getStaffOrganizationListbox().setOrgTreeTabName(this.bean.getLeftTabbox().getSelectedTab().getId());
//		this.bean.getOrganizationPositionListbox().setPagePosition("orgTreePage");
//		this.bean.getOrganizationPartyAttrExt().setPagePosition("orgTreePage");
//		this.bean.getPartyContactInfoListboxExt().setPagePosition("orgTreePage");
//		this.bean.getPartyCertificationListboxExt().setPagePosition("orgTreePage");
//		this.bean.getStaffPositionListboxExt().setPagePosition("orgTreePage");
		/**
		 * 组织信息编辑页面只显示营销组织的类型
		 */
		List<String> optionAttrValueList = new ArrayList<String>();
		TreeOrgTypeRule totr = new TreeOrgTypeRule();
		totr.setOrgTreeId(OrganizationConstant.CUSTMS_TREE_ID);
		List<OrgType> queryOrgTypeList = orgTypeManager.getOrgTypeList(totr);
		if(null != queryOrgTypeList && queryOrgTypeList.size() > 0){
			for (OrgType orgType : queryOrgTypeList) {
				optionAttrValueList.add(orgType.getOrgTypeCd());
			}
		}
		this.bean.getOrganizationInfoEditExt().getBean().getOrganizationInfoExt().getBean().getOrgTypeCd().setOptionNodes(optionAttrValueList);
	}
	/**
	 * 20140612设置tab页用来区分不同tab页的功能权
	 */
	private void initLeftTabControlRightPage() throws Exception {
			String selectedTabId = "edwTab";
			this.bean.getOrganizationInfoEditExt().setOrgTreeTabName(selectedTabId);
			this.bean.getOrganizationRelationListbox().setOrgTreeTabName(selectedTabId);
			this.bean.getStaffOrganizationListbox().setOrgTreeTabName(selectedTabId);
			this.bean.getOrganizationPositionListbox().setOrgTreeTabName(selectedTabId);
			this.bean.getOrganizationPartyAttrExt().setOrgTreeTabName(selectedTabId);
			this.bean.getPartyContactInfoListboxExt().setOrgTreeTabName(selectedTabId);
			this.bean.getPartyCertificationListboxExt().setOrgTreeTabName(selectedTabId);
			this.bean.getStaffPositionListboxExt().setOrgTreeTabName(selectedTabId);
	}

	/**
	 * 初始化tab页权限
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void initLeftTab() throws Exception {
		boolean checkEdwTabResult = PlatformUtil.checkHasPermission(this, ActionKeys.EDW_TAB);
		if (!checkEdwTabResult) {
			this.bean.getTempTab().setVisible(true);
			this.bean.getTempTab().setSelected(true);
			ZkUtil.showExclamation("您没有任何菜单权限,请配置", "警告");
		} else {
			if (!checkEdwTabResult) {
				this.bean.getEdwTab().setVisible(false);
			}
			if (checkEdwTabResult) {
				this.bean.getEdwTab().setSelected(true);
			}
		}
		bean.setLeftSelectTab(this.bean.getLeftTabbox().getSelectedTab());
	}

	/**
	 * 选择员工组织列表的响应处理
	 */
	public void onSelectStaffOrganizationResponse(final ForwardEvent event) throws Exception {
		returParty = (Party) event.getOrigin().getData();
		callPartyTab();
	}

	public void callPartyTab() throws Exception {
		if (this.bean.getTab() == null) {
			this.bean.setTab(this.bean.getTabBoxParty().getSelectedTab());
		}
		if (returParty != null) {
			String tab = this.bean.getTab().getId();
			if ("partyContactInfoTab".equals(tab)) {
				bean.getPartyContactInfoListboxExt().setParty(returParty);
				bean.getPartyContactInfoListboxExt().init();
			}
			if ("partyCertificationTab".equals(tab)) {
				bean.getPartyCertificationListboxExt().setParty(returParty);
				bean.getPartyCertificationListboxExt().init();
			}
			if ("staffPositionTab".equals(tab)) {
				String partyRoleId = returParty.getPartyRoleId();
				if (null != partyRoleId) {
					Staff staff = staffManager.getStaffByPartyRoleId(Long.parseLong(partyRoleId));
					Events.postEvent(SffOrPtyCtants.ON_STAFF_POSITION_QUERY, this.bean.getStaffPositionListboxExt(), staff);
				}
			}
		} else {
			bean.getPartyContactInfoListboxExt().onCleaningPartyContactInfo();
			bean.getPartyCertificationListboxExt().onCleaningPartyCertification();
			bean.getStaffPositionListboxExt().onCleanStaffPositiRespons(null);
		}
	}

	/**
	 * 直接点击Tab页. .
	 */
	public void onClickPartyTab(ForwardEvent event) throws Exception {
		Event origin = event.getOrigin();
		if (origin != null) {
			Component comp = origin.getTarget();
			if (comp != null && comp instanceof Tab) {
				bean.setTab((Tab) comp);
				callPartyTab();
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
				Events.postEvent(OrganizationConstant.ON_SELECT_TREE_ORGANIZATION, this.bean.getOrganizationInfoEditExt(), organization);
			}
			if ("orgRelaTab".equals(this.bean.getSelectTab().getId())) {
				OrganizationRelation organizationRelation = new OrganizationRelation();
				organizationRelation.setOrgId(organization.getOrgId());
				Events.postEvent(OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY, this.bean.getOrganizationRelationListbox(),
								organizationRelation);

			}
			if ("orgStaffTab".equals(this.bean.getSelectTab().getId())) {
				StaffOrganization staffOrganization = new StaffOrganization();
				staffOrganization.setOrgId(organization.getOrgId());
				/**
				 * 推导树组织员工使用(员工信息在treeCalcVo中)
				 */
				staffOrganization.setTreeCalcVo(treeCalcVo);
				Events.postEvent(StaffOrganizationConstant.ON_STAFF_ORGANIZATION_QUERY, this.bean.getStaffOrganizationListbox(),
						staffOrganization);
			}
			if ("orgPositionTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(OrganizationConstant.ON_ORGANIZATION_POSITION_QUERY, this.bean.getOrganizationPositionListbox(),
						organization);
			}
			if ("orgPartyAttrTab".equals(this.bean.getSelectTab().getId())) {
				if (party != null) {
					this.bean.getOrganizationPartyAttrExt().setParty(party);
				}
			}
		} else {
			/**
			 * 切换左边tab页的时候，未选择组织树上的组织，清理数据等操作
			 */
			if ("orgAttrTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(
						OrganizationConstant.ON_SELECT_TREE_ORGANIZATION,
						this.bean.getOrganizationInfoEditExt(), null);
			}
			if ("orgRelaTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY, this.bean.getOrganizationRelationListbox(), null);

			}
			if ("orgStaffTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(
						StaffOrganizationConstant.ON_STAFF_ORGANIZATION_QUERY,
						this.bean.getStaffOrganizationListbox(), null);
			}
			if ("orgPositionTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(
						OrganizationConstant.ON_ORGANIZATION_POSITION_QUERY,
						this.bean.getOrganizationPositionListbox(), null);
			}
			if ("orgPartyAttrTab".equals(this.bean.getSelectTab().getId())) {
				if (party != null) {
					this.bean.getOrganizationPartyAttrExt().setParty(
							new Party());
				}
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
		OrganizationRelation organizationRelation = (OrganizationRelation) event.getOrigin().getData();
		/**
		 * 推导树节点数据,其他为null
		 */
		treeCalcVo = organizationRelation.getTreeCalcVo();
		if (organizationRelation != null && organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
			if (organization != null) {
				party = organization.getParty();
				if (party != null) {
					this.bean.getOrgPartyAttrTab().setVisible(true);
				} else {
					// 如果选中的参与人tab要隐藏则默认选中到组织信息页面
					if (this.bean.getSelectTab() != null && "orgPartyAttrTab".equals(this.bean.getSelectTab().getId())) {
						this.bean.getOrgAttrTab().setSelected(true);
						this.bean.setSelectTab(this.bean.getOrgAttrTab());
					}
					this.bean.getOrgPartyAttrTab().setVisible(false);
				}
			}
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
		
		if ("edwTab".equals(bean.getLeftSelectTab().getId())) {
			organizationRelation = this.bean.getEdwTreeExt().getSelectOrganizationOrganization();
			this.setTreeTypeToRightTab(false);
			this.bean.getOrganizationPartyAttrExt().setIsAgentTab(false);
			this.bean.getStaffOrganizationListbox().setIsAgentTab(false);
			// 是否显示新增删除按钮
			Events.postEvent(OrganizationConstant.ON_IS_VISIBLE, this.bean.getOrganizationRelationListbox(), false);
		}
		if (organizationRelation != null && organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
		} else {
			organization = null;
			// 如果选中的参与人tab要隐藏则默认选中到组织信息页面
			if (this.bean.getSelectTab() != null
					&& "orgPartyAttrTab".equals(this.bean.getSelectTab().getId())) {
				this.bean.getOrgAttrTab().setSelected(true);
				this.bean.setSelectTab(this.bean.getOrgAttrTab());
			}
			this.bean.getOrgPartyAttrTab().setVisible(false);
		}
		if (organization != null) {
			party = organization.getParty();
			if (party != null) {
				this.bean.getOrgPartyAttrTab().setVisible(true);
			} else {
				if (this.bean.getSelectTab() != null && "orgPartyAttrTab".equals(this.bean.getSelectTab().getId())) {
					this.bean.getOrgAttrTab().setSelected(true);
					this.bean.setSelectTab(this.bean.getOrgAttrTab());
				}
				this.bean.getOrgPartyAttrTab().setVisible(false);
			}
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
		this.bean.getOrganizationRelationListbox().setDuceTree(isDuceTree);
		this.bean.getStaffOrganizationListbox().setDuceTree(isDuceTree);
		this.bean.getOrganizationPositionListbox().setDuceTree(isDuceTree);
		this.bean.getOrganizationPartyAttrExt().setDuceTree(isDuceTree);
		this.bean.getPartyCertificationListboxExt().setDuceTree(isDuceTree);
		this.bean.getPartyContactInfoListboxExt().setDuceTree(isDuceTree);
		this.bean.getStaffPositionListboxExt().setDuceTree(isDuceTree);
	}

	/**
	 * 组织信息保存
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSaveOrganizationInfoResponse(ForwardEvent event)
			throws Exception {
		if (event.getOrigin().getData() != null) {
			Organization org = (Organization) event.getOrigin().getData();
			if (org != null) {
				/**
				 * 清楚对象缓存
				 */
				org.setOrgTypeList(null);
				if ("edwTab".equals(bean.getLeftSelectTab().getId())) {
					/**
					 * 是否是单位
					 */
					if (org.isCompany()) {
						Events.postEvent(OrganizationConstant.ON_CHANGE_TREE_STYLE, this.bean.getEdwTreeExt(), true);
					} else {
						Events.postEvent(OrganizationConstant.ON_CHANGE_TREE_STYLE, this.bean.getEdwTreeExt(), false);
					}
					/**
					 * 组织信息保存可能对组织名称进行了修改
					 */
					Events.postEvent(OrganizationConstant.ON_SAVE_ORGANIZATION_INFO, this.bean.getEdwTreeExt(), org);
				}
			}
		}
	}
}
