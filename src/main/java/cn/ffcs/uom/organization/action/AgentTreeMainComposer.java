package cn.ffcs.uom.organization.action;

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
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.action.bean.AgentTreeMainBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

@Controller
@Scope("prototype")
public class AgentTreeMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8064419972929637883L;

	/**
	 * 页面bean
	 */
	private AgentTreeMainBean bean = new AgentTreeMainBean();
	/**
	 * 选中的组织
	 */
	private Organization organization;

	/**
	 * 组织信息窗口
	 */
	// private Window organizationAtrrWindow;

	/**
	 * 组织参与人信息窗口
	 */
	// private Window organizationPartyAtrrWindow;

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

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
		this.bean.getAgentTreeExt().setPortletInfoProvider(this);
		this.bean.getOrganizationInfoEditExt().setPortletInfoProvider(this);
		this.bean.getOrganizationRelationListbox().setPortletInfoProvider(this);
		this.bean.getOrganizationTranListboxExt().setPortletInfoProvider(this);
		this.bean.getUomGroupOrgTranListboxExt().setPortletInfoProvider(this);
		this.bean.getStaffOrganizationListbox().setPortletInfoProvider(this);
		this.bean.getStaffOrganizationListbox().setIsOrgTreePage(true);
		this.bean.getOrganizationRelationListbox().setIsOrgTreePage(true);
		this.bean.getOrganizationTranListboxExt().setIsOrgTreePage(true);
		this.bean.getUomGroupOrgTranListboxExt().setIsOrgTreePage(true);
		this.bean.getOrganizationPositionListbox().setPortletInfoProvider(this);
		this.bean.getOrganizationPartyAttrExt().setPortletInfoProvider(this);
		this.bean.getPartyContactInfoListboxExt().setPortletInfoProvider(this);
		this.bean.getPartyCertificationListboxExt()
				.setPortletInfoProvider(this);
		this.bean.getStaffPositionListboxExt().setPortletInfoProvider(this);

		/**
		 * 选中组织树上的组织
		 */
		this.bean.getAgentTreeExt().addForward(
				OrganizationConstant.ON_SELECT_AGENT_ORGANIZATION_TREE_REQUEST,
				this.self, "onSelectOrganizationRelationTreeResponse");
		/**
		 * 删除节点成功事件
		 */
		this.bean.getAgentTreeExt().addForward(
				OrganizationConstant.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");
		/**
		 * 选择代理树
		 */
		this.bean.getAgentTreeExt().addForward(
				OrganizationConstant.ON_SELECT_TREE_TYPE, this.self,
				"onSelectTreeTypeResponse");

		/**
		 * 组织信息保存事件
		 */
		this.bean.getOrganizationInfoEditExt().addForward(
				OrganizationConstant.ON_SAVE_ORGANIZATION_INFO, this.self,
				"onSaveOrganizationInfoResponse");
		/**
		 * 组织员工信息选择事件
		 */
		this.bean.getStaffOrganizationListbox().addForward(
				OrganizationConstant.ON_STAFFORGANIZATION_SELECT, this.self,
				"onSelectStaffOrganizationResponse");
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$agentTreeMainWindow() throws Exception {
		initLeftTab();
		initPage();
		initLeftTabControlRightPage();
		callLeftTab();// 当只有一个代理商TAB页面时，解决用工性质无法选择代理商员工的BUG
	}

	/**
	 * 设置页面不区分组织树TAB页
	 */
	private void initPage() throws Exception {
		// this.bean.getAgentTreeExt().setPagePosition("agentTreePage");
		/**
		 * 未区分组织树各TAB页的权限分离
		 */
		// this.bean.getOrganizationInfoEditExt().setPagePosition("orgTreePage");
		// this.bean.getOrganizationRelationListbox().setPagePosition(
		// "orgTreePage");
		// this.bean.getOrganizationTranListboxExt()
		// .setPagePosition("orgTreePage");
		// this.bean.getUomGroupOrgTranListboxExt().setPagePosition("orgTreePage");
		// this.bean.getStaffOrganizationListbox().setPagePosition("orgTreePage");
		// this.bean.getOrganizationPositionListbox().setPagePosition(
		// "orgTreePage");
		// this.bean.getOrganizationPartyAttrExt().setPagePosition("orgTreePage");
		// this.bean.getPartyContactInfoListboxExt()
		// .setPagePosition("orgTreePage");
		// this.bean.getPartyCertificationListboxExt().setPagePosition(
		// "orgTreePage");
		// this.bean.getStaffPositionListboxExt().setPagePosition("orgTreePage");

		/**
		 * 组织业务关系
		 */
		List<String> optionAttrValueList = new ArrayList<String>();
		optionAttrValueList
				.add(OrganizationTranConstant.DEPARTMENT_NETWORK_MANY_TO_ONE);

		this.bean.getOrganizationTranListboxExt().getBean()
				.getTranRelaTypeDiv().setVisible(false);
		this.bean.getOrganizationTranListboxExt().getBean()
				.getTelcomRegionDiv().setVisible(false);
		this.bean.getUomGroupOrgTranListboxExt().getBean().getTelcomRegionDiv()
				.setVisible(false);
		this.bean.getOrganizationTranListboxExt().getBean().getTranRelaType()
				.setInitialValue(optionAttrValueList);
	}

	/**
	 * 20140612设置tab页用来区分不同tab页的功能权
	 */
	private void initLeftTabControlRightPage() throws Exception {
		if (this.bean.getLeftSelectTab() != null) {
			this.bean.getOrganizationInfoEditExt().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getOrganizationRelationListbox().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getOrganizationTranListboxExt().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getUomGroupOrgTranListboxExt().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getStaffOrganizationListbox().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getOrganizationPositionListbox().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getOrganizationPartyAttrExt().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getPartyContactInfoListboxExt().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getPartyCertificationListboxExt().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
			this.bean.getStaffPositionListboxExt().setOrgTreeTabName(
					this.bean.getLeftTabbox().getSelectedTab().getId());
		}
	}

	/**
	 * 初始化tab页权限
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void initLeftTab() throws Exception {

		boolean checkAgentTabResult = PlatformUtil.checkHasPermission(this,
				ActionKeys.AGENT_TAB);

		if (!checkAgentTabResult) {
			this.bean.getTempTab().setVisible(true);
			this.bean.getTempTab().setSelected(true);
			ZkUtil.showExclamation("您没有任何菜单权限,请配置", "警告");
		} else {
			if (!checkAgentTabResult) {
				this.bean.getAgentTab().setVisible(false);
			}

			if (checkAgentTabResult) {
				this.bean.getAgentTab().setSelected(true);
				this.bean.setLeftSelectTab(bean.getAgentTab());// 当只有一个代理商TAB页面时，解决用工性质无法选择代理商员工的BUG
			}
		}
	}

	/**
	 * 绑定页面集成的页面控件
	 */
	// private void bindComponet() {
	// if (this.self.getFellow("tabBox") != null) {
	// if (this.self.getFellow("tabBox").getFellow("partyAttr") != null) {
	// organizationPartyAtrrWindow = (Window) this.self
	// .getFellow("tabBox").getFellow("partyAttr")
	// .getFellow("partyEditWindow");
	// }
	// }
	// }

	/**
	 * 选择员工组织列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author faq 2013-7-25 faq
	 */
	public void onSelectStaffOrganizationResponse(final ForwardEvent event)
			throws Exception {
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
					Staff staff = staffManager.getStaffByPartyRoleId(Long
							.parseLong(partyRoleId));
					Events.postEvent(SffOrPtyCtants.ON_STAFF_POSITION_QUERY,
							this.bean.getStaffPositionListboxExt(), staff);
				}
			}
		} else {
			bean.getPartyContactInfoListboxExt().onCleaningPartyContactInfo();
			bean.getPartyCertificationListboxExt()
					.onCleaningPartyCertification();
			bean.getStaffPositionListboxExt().onCleanStaffPositiRespons(null);
		}
	}

	/**
	 * 直接点击Tab页. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author faq 2013-7-25 faq
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
				Events.postEvent(
						OrganizationConstant.ON_SELECT_TREE_ORGANIZATION,
						this.bean.getOrganizationInfoEditExt(), organization);
			}
			if ("orgRelaTab".equals(this.bean.getSelectTab().getId())) {
				OrganizationRelation organizationRelation = new OrganizationRelation();
				organizationRelation.setOrgId(organization.getOrgId());
				organizationRelation.setIsAgent("agentTab".equals(bean
						.getLeftSelectTab().getId()));
				organizationRelation.setIsIbe("ibeTab".equals(bean
						.getLeftSelectTab().getId()));
				Events.postEvent(
						OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY,
						this.bean.getOrganizationRelationListbox(),
						organizationRelation);

			}
			if ("orgTranTab".equals(this.bean.getSelectTab().getId())) {

				OrganizationTran organizationTran = new OrganizationTran();
				organizationTran.setOrgId(organization.getOrgId());

				if ("politicalTab".equals(bean.getLeftSelectTab().getId())) {

					this.bean.getOrganizationTranListboxExt()
							.setVariableOrgTreeTabName("politicalTab");

				} else {

					this.bean.getOrganizationTranListboxExt()
							.setVariableOrgTreeTabName("otherTab");
				}

				Events.postEvent(
						OrganizationTranConstant.ON_ORGANIZATION_TRAN_QUERY,
						this.bean.getOrganizationTranListboxExt(),
						organizationTran);

			}
			if ("uomGroupOrgTranTab".equals(this.bean.getSelectTab().getId())) {

				UomGroupOrgTran uomGroupOrgTran = new UomGroupOrgTran();
				uomGroupOrgTran.setOrgId(organization.getOrgId());

				if ("agentTab".equals(bean.getLeftSelectTab().getId())) {

					this.bean.getUomGroupOrgTranListboxExt()
							.setVariableOrgTreeTabName("agentTab");

				} else {

					this.bean.getUomGroupOrgTranListboxExt()
							.setVariableOrgTreeTabName("otherTab");
				}

				Events.postEvent(
						OrganizationTranConstant.ON_UOM_GROUP_ORG_TRAN_QUERY,
						this.bean.getUomGroupOrgTranListboxExt(),
						uomGroupOrgTran);
			}
			if ("orgStaffTab".equals(this.bean.getSelectTab().getId())) {
				StaffOrganization staffOrganization = new StaffOrganization();
				staffOrganization.setOrgId(organization.getOrgId());
				/**
				 * 推导树组织员工使用(员工信息在treeCalcVo中)
				 */
				staffOrganization.setTreeCalcVo(treeCalcVo);
				Events.postEvent(
						StaffOrganizationConstant.ON_STAFF_ORGANIZATION_QUERY,
						this.bean.getStaffOrganizationListbox(),
						staffOrganization);
			}
			if ("orgPositionTab".equals(this.bean.getSelectTab().getId())) {
				Events.postEvent(
						OrganizationConstant.ON_ORGANIZATION_POSITION_QUERY,
						this.bean.getOrganizationPositionListbox(),
						organization);
			}
			// if ("orgPartyTab".equals(this.bean.getSelectTab().getId())) {
			// if (organizationPartyAtrrWindow != null && party != null) {
			// Events.postEvent(
			// OrganizationConstant.ON_SELECT_TREE_ORGANIZATION,
			// organizationPartyAtrrWindow, party);
			// }
			// }
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
				Events.postEvent(
						OrganizationRelationConstant.ON_ORGANIZATION_RELATION_QUERY,
						this.bean.getOrganizationRelationListbox(), null);

			}

			if ("orgTranTab".equals(this.bean.getSelectTab().getId())) {

				if ("politicalTab".equals(bean.getLeftSelectTab().getId())) {

					this.bean.getOrganizationTranListboxExt()
							.setVariableOrgTreeTabName("politicalTab");

				} else {

					this.bean.getOrganizationTranListboxExt()
							.setVariableOrgTreeTabName("otherTab");
				}
				Events.postEvent(
						OrganizationTranConstant.ON_ORGANIZATION_TRAN_QUERY,
						this.bean.getOrganizationTranListboxExt(), null);

			}
			if ("uomGroupOrgTranTab".equals(this.bean.getSelectTab().getId())) {

				if ("agentTab".equals(bean.getLeftSelectTab().getId())) {
					this.bean.getUomGroupOrgTranListboxExt()
							.setVariableOrgTreeTabName("agentTab");
				} else {
					this.bean.getUomGroupOrgTranListboxExt()
							.setVariableOrgTreeTabName("otherTab");
				}

				Events.postEvent(
						OrganizationTranConstant.ON_UOM_GROUP_ORG_TRAN_QUERY,
						this.bean.getUomGroupOrgTranListboxExt(), null);
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
			// if ("orgPartyTab".equals(this.bean.getSelectTab().getId())) {
			// if (organizationPartyAtrrWindow != null && party != null) {
			// Events.postEvent(
			// OrganizationConstant.ON_SELECT_TREE_ORGANIZATION,
			// organizationPartyAtrrWindow, new Party());
			// }
			// }
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
		OrganizationRelation organizationRelation = (OrganizationRelation) event
				.getOrigin().getData();
		/**
		 * 推导树节点数据,其他为null
		 */
		treeCalcVo = organizationRelation.getTreeCalcVo();
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
			if (organization != null) {
				party = organization.getParty();
				if (party != null) {
					// this.bean.getOrgPartyTab().setVisible(true);
					this.bean.getOrgPartyAttrTab().setVisible(true);
				} else {
					// 如果选中的参与人tab要隐藏则默认选中到组织信息页面
					if (this.bean.getSelectTab() != null
							&& "orgPartyAttrTab".equals(this.bean
									.getSelectTab().getId())) {
						this.bean.getOrgAttrTab().setSelected(true);
						this.bean.setSelectTab(this.bean.getOrgAttrTab());
					}
					// this.bean.getOrgPartyTab().setVisible(false);
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
				/**
				 * 20140612设置tab页用来区分不同tab页的功能权
				 */
				initLeftTabControlRightPage();
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
		if ("agentTab".equals(bean.getLeftSelectTab().getId())) {
			organizationRelation = this.bean.getAgentTreeExt()
					.getSelectOrganizationOrganization();
			this.setTreeTypeToRightTab(false);
			this.bean.getOrganizationPartyAttrExt().setIsAgentTab(true);
			this.bean.getStaffOrganizationListbox().setIsPoliticalTab(false);
			this.bean.getStaffOrganizationListbox().setIsAgentTab(true);
			this.bean.getStaffOrganizationListbox().setIsIbeTab(false);
			this.bean.getOrganizationRelationListbox().setIsAgentTab(true);
			this.bean.getOrganizationRelationListbox().setIsIbeTab(false);
			this.bean.getOrganizationInfoEditExt().setIsAgentTab(true);
			this.bean.getOrganizationInfoEditExt().setIsIbeTab(false);
			this.bean.getUomGroupOrgTranListboxExt().setIsAgentTab(true);
			this.bean.getUomGroupOrgTranListboxExt().setIsIbeTab(false);
			this.bean.getOrganizationInfoEditExt()
					.restoreOrganizationInfoExtStyle("class",
							"z-info-required", "* 地址");
			// 是否显示新增删除按钮
			/*
			 * Events.postEvent(OrganizationConstant.ON_IS_VISIBLE,
			 * this.bean.getOrganizationRelationListbox(), false);
			 */
		}
		if (organizationRelation != null
				&& organizationRelation.getOrgId() != null) {
			organization = organizationRelation.getOrganization();
		} else {
			organization = null;
			// 如果选中的参与人tab要隐藏则默认选中到组织信息页面
			if (this.bean.getSelectTab() != null
					&& "orgPartyAttrTab".equals(this.bean.getSelectTab()
							.getId())) {
				this.bean.getOrgAttrTab().setSelected(true);
				this.bean.setSelectTab(this.bean.getOrgAttrTab());
			}
			// this.bean.getOrgPartyTab().setVisible(false);
			this.bean.getOrgPartyAttrTab().setVisible(false);
		}
		if (organization != null) {
			party = organization.getParty();
			if (party != null) {
				// this.bean.getOrgPartyTab().setVisible(true);
				this.bean.getOrgPartyAttrTab().setVisible(true);
			} else {
				if (this.bean.getSelectTab() != null
						&& "orgPartyAttrTab".equals(this.bean.getSelectTab()
								.getId())) {
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
		this.bean.getOrganizationTranListboxExt().setDuceTree(isDuceTree);
		this.bean.getUomGroupOrgTranListboxExt().setDuceTree(isDuceTree);
		this.bean.getStaffOrganizationListbox().setDuceTree(isDuceTree);
		this.bean.getOrganizationPositionListbox().setDuceTree(isDuceTree);
		// /**
		// * 参与人
		// */
		// if (organizationPartyAtrrWindow != null) {
		// Events.postEvent("onSetTreeTypeToRightTab",
		// organizationPartyAtrrWindow, isDuceTree);
		// }
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
			}
		}
	}
}
