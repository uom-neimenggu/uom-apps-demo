package cn.ffcs.uom.organization.action;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.action.bean.OrganizationPartyAttrExtBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

public class OrganizationPartyAttrExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 页面
	 */
	private final String zul = "/pages/organization/organization_party_ext.zul";
	/**
	 * 页面bean
	 */
	@Getter
	private OrganizationPartyAttrExtBean bean = new OrganizationPartyAttrExtBean();

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");
	/**
	 * Manager.
	 */
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	/**
	 * 当前party
	 */
	private Party party;
	/**
	 * 组织树中选中的organization
	 */
	private Organization organization;
	/**
	 * 树TAB区分
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;

	/**
	 * 是否是推导树
	 */
	@Getter
	private Boolean isDuceTree = false;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public OrganizationPartyAttrExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	public void onCreate() throws Exception {
		this.bindBean();
		this.setButtonValid(true, false, false);
		if (portletInfoProvider != null) {
			this.bean.getPartyContactInfoListboxExt().setPortletInfoProvider(
					portletInfoProvider);
			this.bean.getPartyCertificationListboxExt().setPortletInfoProvider(
					portletInfoProvider);
			this.bean.getPartyRoleListboxExt().setPortletInfoProvider(
					portletInfoProvider);
		}
	}

	private void bindBean() {
		/**
		 * 隐藏证件
		 */
		this.bean.getPartyInfoExt().getBean().getPartyCertGroupbox()
				.setVisible(false);
		/**
		 * 隐藏联系人
		 */
		this.bean.getPartyInfoExt().getBean().getPartyContactGroupbox()
				.setVisible(false);
		/**
		 * 隐藏是否新增员工
		 */
		this.bean.getPartyInfoExt().getBean().getIsAddStaffRow()
				.setVisible(false);
		/**
		 * 隐藏角色类型
		 */
		this.bean.getPartyInfoExt().getBean().getRoleType().setVisible(false);
		this.bean.getPartyInfoExt().getBean().getRoleTypeLab()
				.setVisible(false);
	}

	/**
	 * 设置初始状态
	 * 
	 * @param party
	 */
	public void setParty(Party party) throws Exception {
		this.setButtonValid(true, false, false);
		if (party != null) {
			this.party = party;
			this.bean.getPartyInfoExt().setParty(party, "mod");
			if (party.getPartyId() != null) {
				organization = new Organization();
				organization.setPartyId(party.getPartyId());
				List<Organization> organizationList = organizationManager
						.quertyOrganizationList(organization);
				if (organizationList != null && organizationList.size() > 0) {
					organization = organizationList.get(0);
				} else {
					organization = null;
				}

			}
			this.callTab();
			if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
				this.setOrgTreeTabName(variableOrgTreeTabName);
			}
		}
	}

	/**
	 * 直接点击Tab页. .
	 */
	public void onClickTab(ForwardEvent event) throws Exception {
		Event origin = event.getOrigin();
		if (origin != null) {
			Component comp = origin.getTarget();
			if (comp != null && comp instanceof Tab) {
				bean.setTab((Tab) comp);
				if (party != null) {
					callTab();
				}
			}
		}
	}

	public void callTab() throws Exception {
		if (this.bean.getTab() == null) {
			this.bean.setTab(this.bean.getTabBox().getSelectedTab());
		}
		String tab = this.bean.getTab().getId();
		if ("partyContactInfoTab".equals(tab)) {
			bean.getPartyContactInfoListboxExt().setParty(party);
			bean.getPartyContactInfoListboxExt().setOrganization(organization);
			bean.getPartyContactInfoListboxExt().init();
		}
		if ("partyCertificationTab".equals(tab)) {
			bean.getPartyCertificationListboxExt().setParty(party);
			bean.getPartyContactInfoListboxExt().setOrganization(organization);
			bean.getPartyCertificationListboxExt().init();
		}
		if ("partyRoleTab".equals(tab)) {
			bean.getPartyContactInfoListboxExt().setOrganization(organization);
			Events.postEvent(SffOrPtyCtants.ON_PARTY_ROLE_QUERY,
					this.bean.getPartyRoleListboxExt(), party);
		}
	}

	/**
	 * 设置按键
	 * 
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	public void setButtonValid(boolean canEdit, boolean canSave,
			boolean canRecover) {
		/**
		 * 推导树默认不让编辑且不然修改
		 */
		if (isDuceTree) {
			return;
		}
		this.bean.getEditButton().setDisabled(!canEdit);
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canRecover);
	}

	/**
	 * 保存
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		this.setButtonValid(false, true, true);
	}

	/**
	 * 修改
	 * 
	 * @throws Exception
	 */
	public void onSave() throws Exception {
		this.bean.getPartyInfoExt().getParty();
		Party pat = this.bean.getPartyInfoExt().getOutsideParty();
		String msg = "";
		if (StrUtil.isNullOrEmpty(pat.getPartyName())) {
			msg = "请填写参与人名称";
		}
		if (StrUtil.checkBlank(pat.getPartyName())) {
			msg = "参与人名称中有空格";
		}
		if (StrUtil.isNullOrEmpty(pat.getPartyType())) {
			msg = "请选择参与人类型";
		}
		if (SffOrPtyCtants.CONST_INDIVIDUAL.equals(pat.getPartyType())) {// 个人员工用户
			if (pat.getIndividual() == null
					&& StrUtil.isNullOrEmpty(pat.getIndividual().getBirthday())) {
				msg = "请选择出生日期";
			}
			if (pat.getIndividual() == null
					|| StrUtil.isNullOrEmpty(pat.getIndividual().getGender())) {
				msg = "请选择性别";
			}
		} else {
			if (pat.getPartyOrganization() == null
					|| StrUtil.isNullOrEmpty(pat.getPartyOrganization()
							.getOrgType())) {
				msg = "请选择组织类型";
			}
		}
		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		partyManager.updateOrganizationParty(pat);
		this.setButtonValid(true, false, false);
	}

	/**
	 * 恢复
	 * 
	 * @throws Exception
	 */
	public void onRecover() throws Exception {
		this.bean.getPartyInfoExt().setParty(this.party, "mod");
		this.setButtonValid(true, false, false);
		this.callTab();
	}

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setButtonValid(false, false, false);
			this.bean.getPartyCertificationListboxExt().setDuceTree(isDuceTree);
			this.bean.getPartyContactInfoListboxExt().setDuceTree(isDuceTree);
			this.bean.getPartyRoleListboxExt().setDuceTree(isDuceTree);
		}
		this.isDuceTree = isDuceTree;
	}

	/**
	 * 是否是代理商管理页面
	 * 
	 * @param isAgent
	 */
	public void setIsAgentTab(boolean isAgent) {
		if (isAgent) {
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(true);
			this.bean.getPartyInfoExt().getBean().getPartyType()
					.setDisabled(true);
		} else {
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(false);
			this.bean.getPartyInfoExt().getBean().getPartyType()
					.setDisabled(false);
		}
	}

	/**
	 * 是否是内部经营实体管理页面
	 * 
	 * @param isIbe
	 */
	public void setIsIbeTab(boolean isIbe) {
		if (isIbe) {
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(true);
			this.bean.getPartyInfoExt().getBean().getPartyType()
					.setDisabled(true);
		} else {
			this.bean.getPartyInfoExt().getBean().getRoleType()
					.setDisabled(false);
			this.bean.getPartyInfoExt().getBean().getPartyType()
					.setDisabled(false);
		}
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;

		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else if ("orgTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_PARTY_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_PARTY_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_ORG_PARTY_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
		/**
		 * 组织员工和组织参与人中用来区分参与人相关TAB页面
		 */
		this.bean.getPartyContactInfoListboxExt().setPagePosition(
				page + "_orgParty");
		this.bean.getPartyCertificationListboxExt().setPagePosition(
				page + "_orgParty");
		this.bean.getPartyRoleListboxExt().setPagePosition(page + "_orgParty");
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			} else if ("marketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_SAVE)) {
						canSave = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_RECOVER)) {
						canRecover = true;
					}
				}

			} else if ("newMarketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_EDIT)) {
						canEdit = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_SAVE)) {
						canSave = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_RECOVER)) {
						canRecover = true;
					}
				}

            } else if ("newSeventeenMarketingTab".equals(orgTreeTabName)) {
                
                aroleOrganizationLevel = new AroleOrganizationLevel();
                aroleOrganizationLevel
                    .setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
                aroleOrganizationLevel
                    .setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
                
                if (!StrUtil.isNullOrEmpty(organization)
                    && aroleOrganizationLevelManager.aroleOrganizationLevelValid(
                        aroleOrganizationLevel, organization)) {
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_EDIT)) {
                        canEdit = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_SAVE)) {
                        canSave = true;
                    }
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_RECOVER)) {
                        canRecover = true;
                    }
                }
                
            } else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			} else if ("multidimensionalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_EDIT)) {
					canEdit = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_SAVE)) {
					canSave = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_RECOVER)) {
					canRecover = true;
				}
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
		/**
		 * 组织员工和组织参与人中用来区分参与人相关TAB页面
		 */
		this.bean.getPartyContactInfoListboxExt().setOrgTreeTabName(
				orgTreeTabName + "_orgParty");
		this.bean.getPartyCertificationListboxExt().setOrgTreeTabName(
				orgTreeTabName + "_orgParty");
		this.bean.getPartyRoleListboxExt().setOrgTreeTabName(
				orgTreeTabName + "_orgParty");
	}
}
