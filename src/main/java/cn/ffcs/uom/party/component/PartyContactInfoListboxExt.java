package cn.ffcs.uom.party.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.party.component.bean.PartyContactInfoListboxBean;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 参与人联系人方式列表 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-5
 * @功能说明：
 * 
 */
@SuppressWarnings({ "unused" })
public class PartyContactInfoListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	PartyContactInfoListboxBean bean = new PartyContactInfoListboxBean();

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	/**
	 * Manager.
	 */
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	@Getter
	@Setter
	private Party party;
	/**
	 * 组织树员工组织关系TAB页面中员工参与人标识【目的区分组织树中组织参与人】
	 */
	@Getter
	@Setter
	private Boolean isOrgTreeStaffParty = false;
	/**
	 * 树TAB区分
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;
	/**
	 * 组织树中选中的organization
	 */
	@Setter
	private Organization organization;

	private PartyContactInfo partyConInfo;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	/**
	 * 是否是只有身份证查询条件的参与人管理页面
	 */
	@Getter
	@Setter
	private Boolean isIdNoQueryPage = false;

	/**
	 * 页面定位
	 */
	@Getter
	@Setter
	private String variablePageLocation;

	public PartyContactInfoListboxExt() {
		Executions.createComponents(
				SffOrPtyCtants.ZUL_PARTYCONTACTINFO_LISTBOX_EXT, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		setPartyContactInfoButtonValid(true, true, true, true);
		this.addForward(SffOrPtyCtants.ON_CLEAN_PARTY_CON, this,
				SffOrPtyCtants.ON_CLEAN_PARTY_CON_RES);

	}

	public void onCreate() throws Exception {
		/**
		 * 只有身份证查询条件的参与人管理页面，隐藏CURD功能
		 */
		if (isIdNoQueryPage) {
			this.bean.getPartyContactInfoBandboxDiv().setVisible(false);
		}
	}

	/**
	 * 参与人联系人选择 .
	 * 
	 * @param event
	 * @author Wong 2013-6-8 Wong
	 */
	public void onPartyContactInfoSelectRequest(final ForwardEvent event) {
		if (bean.getPartyContactInfoListbox().getSelectedCount() > 0) {
			partyConInfo = (PartyContactInfo) bean.getPartyContactInfoListbox()
					.getSelectedItem().getValue();
			setPartyContactInfoButtonValid(false, false, false, false);
		}
	}

	public void quertyPartyContactInfo() {

		if (null != partyConInfo) {
			partyConInfo.setContactName(this.bean.getContactName().getValue());
			PageInfo pageInfo = partyManager
					.getPartyContactInfos(partyConInfo,
							bean.getPartyContactInfoListboxPaging()
									.getActivePage() + 1, bean
									.getPartyContactInfoListboxPaging()
									.getPageSize());

			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getPartyContactInfoListbox().setModel(dataList);
			bean.getPartyContactInfoListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryPartyContactInfo() throws Exception {
		this.bean.getPartyContactInfoListboxPaging().setActivePage(0);
		this.quertyPartyContactInfo();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetPartyContactInfo() throws Exception {

		this.bean.getContactName().setValue("");

	}

	/**
	 * 清空选中的员工.
	 */
	public void onCleaningPartyContactInfo() throws Exception {
		this.bean.getContactName().setValue("");
		ListboxUtils.clearListbox(bean.getPartyContactInfoListbox());
	}

	/**
	 * 分页显示 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	public void onPartyContactInfoListboxPaging() {
		quertyPartyContactInfo();
	}

	/**
	 * 控件初始化 .
	 * 
	 * @throws Exception
	 *             异常
	 * @author
	 */
	public void init() throws Exception {

		if (party != null) {

			if (!isOrgTreeStaffParty) {

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
			}

			if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
				this.setOrgTreeTabName(variableOrgTreeTabName);
			}

		}

		setPartyContactInfoButtonValid(false, true, true, true);
		this.bean.getContactName().setValue("");
		if (this.getParty() != null) {
			if (null == partyConInfo) {
				partyConInfo = new PartyContactInfo();
			}
			partyConInfo.setPartyId(party.getPartyId());
			this.quertyPartyContactInfo();
		}
	}

	/**
	 * 新增参与人联系人 .
	 * 
	 * @author Wong 2013-6-7 Wong
	 */
	public void onPartyContactInfoAdd() {
		openPartyContactInfoEditWin(SffOrPtyCtants.ADD);
	}

	/**
	 * 修改参与人联系人 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 */
	public void onPartyContactInfoEdit() {
		openPartyContactInfoEditWin(SffOrPtyCtants.MOD);
	}

	public void onPartyContactInfoDel() {
		try {
			Window window = (Window) Executions.createComponents(
					"/pages/common/del_reason_input.zul", this, null);
			window.doModal();
			window.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						setPartyContactInfoButtonValid(true, false, false,
								false);
						String reason = (String) event.getData();
						partyConInfo.setReason(reason);
						PartyContactInfo queryContactInfo = new PartyContactInfo();
						queryContactInfo.setPartyId(partyConInfo.getPartyId());
						List<PartyContactInfo> pcList = partyManager
								.getPartyContInfoList(queryContactInfo);
						if (pcList != null && pcList.size() > 1) {
							partyManager.removePartyContactInfo(partyConInfo);
						} else {
							Messagebox.show("您至少得保留一个联系人！");
							return;
						}

						if (SffOrPtyCtants.HEADFLAG.equals(partyConInfo
								.getHeadFlag())) {
							pcList = partyManager
									.getPartyContInfoList(queryContactInfo);
							if (pcList != null && pcList.size() > 0) {
								pcList.get(0).setHeadFlag(
										SffOrPtyCtants.HEADFLAG);
								partyManager.update(pcList.get(0));
							}
						}
						Messagebox.show("联系人删除成功！");
						PubUtil.reDisplayListbox(
								bean.getPartyContactInfoListbox(),
								partyConInfo, "del");
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void openPartyContactInfoEditWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			arg.put("variablePageLocation", variablePageLocation);
			arg.put("portletInfoProvider", portletInfoProvider);
			if (opType.equals("mod") || opType.equals("pwd")) {
				arg.put("partyContactInfo", partyConInfo);
			}
			arg.put("party", party);
			Window win = (Window) Executions.createComponents(
					SffOrPtyCtants.PARTYCONTACTINFO_EDIT_ZUL, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					setPartyContactInfoButtonValid(true, false, false, false);
					partyConInfo = (PartyContactInfo) event.getData();
					if (partyConInfo != null) {
						// PubUtil.reDisplayListbox(bean.getPartyContactInfoListbox(),(PartyContactInfo)
						// event.getData(), type);
						if (type.equals(SffOrPtyCtants.ADD)
								&& !StrUtil.isEmpty(partyConInfo
										.getContactName())) {
							bean.getContactName().setValue(
									partyConInfo.getContactName().trim());
						}
						quertyPartyContactInfo();
					}
				}
			});
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception ec) {
			ec.printStackTrace();
		}
	}

	/**
	 * 设置按钮的状态 .
	 * 
	 * @param badd
	 * @param bDel
	 * @param bUpdate
	 * @param bClean
	 * @author Wong 2013-6-5 Wong
	 */
	private void setPartyContactInfoButtonValid(boolean badd, boolean bDel,
			boolean bUpdate, boolean bClean) {
		if (isDuceTree) {
			return;
		}
		bean.getAddPartyContactInfoButton().setDisabled(badd);
		bean.getEditPartyContactInfoButton().setDisabled(bUpdate);
		bean.getDelPartyContactInfoButton().setDisabled(bDel);
	}

	/**
	 * 
	 * .
	 * 
	 * @author wangyong 2013-6-13 wangyong
	 */
	public void onCleanonCleanPartyConRespons() {
		this.bean.getContactName().setValue("");
		party = null;
		partyConInfo = null;
		ListboxUtils.clearListbox(bean.getPartyContactInfoListbox());
		setPartyContactInfoButtonValid(true, true, true, true);
	}

	/**
	 * 是否是推导树
	 */
	@Getter
	private boolean isDuceTree = false;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setPartyContactInfoButtonValid(false, false, false, false);
		}
		this.isDuceTree = isDuceTree;
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canUpdate = false;
		boolean canDel = false;

		variablePageLocation = page;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
		} else if ("orgTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("partyPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("orgTreePage_orgParty".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CONTACT_DEL)) {
				canDel = true;
			}
		}

		bean.getAddPartyContactInfoButton().setVisible(canAdd);
		bean.getEditPartyContactInfoButton().setVisible(canUpdate);
		bean.getDelPartyContactInfoButton().setVisible(canDel);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canUpdate = false;
		boolean canDel = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;
		variablePageLocation = orgTreeTabName;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {

			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CONTACT_DEL)) {
					canDel = true;
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
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_DEL)) {
						canDel = true;
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
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_DEL)) {
						canDel = true;
					}

				}

			} else if ("newSeventeenMarketingTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CONTACT_DEL)) {
						canDel = true;
					}

				}

			} else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("politicalTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("agentTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("ossTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("edwTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("marketingTab_orgParty".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_DEL)) {
						canDel = true;
					}

				}

			} else if ("newMarketingTab_orgParty".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_DEL)) {
						canDel = true;
					}

				}

			} else if ("newSeventeenMarketingTab_orgParty"
					.equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);

				if (!StrUtil.isNullOrEmpty(organization)
						&& aroleOrganizationLevelManager
								.aroleOrganizationLevelValid(
										aroleOrganizationLevel, organization)) {

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CONTACT_DEL)) {
						canDel = true;
					}

				}

			} else if ("costTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			} else if ("multidimensionalTab".equals(orgTreeTabName)) {
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CONTACT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CONTACT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CONTACT_DEL)) {
					canDel = true;
				}
			}
		}
		bean.getAddPartyContactInfoButton().setVisible(canAdd);
		bean.getEditPartyContactInfoButton().setVisible(canUpdate);
		bean.getDelPartyContactInfoButton().setVisible(canDel);
	}
}
