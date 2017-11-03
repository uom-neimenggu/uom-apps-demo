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
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
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
import cn.ffcs.uom.party.component.bean.PartyCertificationListboxExtBean;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 参与人证件列表 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-7
 * @功能说明：
 * 
 */
public class PartyCertificationListboxExt extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private PartyCertificationListboxExtBean bean = new PartyCertificationListboxExtBean();

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

	private PartyCertification partyCertifiQue;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	/**
	 * 是否是只有身份证查询条件的参与人管理页面
	 */
	@Getter
	@Setter
	private Boolean isIdNoQueryPage = false;

	public PartyCertificationListboxExt() {
		Executions.createComponents(
				SffOrPtyCtants.ZUL_PARTYCERTIFICATION_LISTBOX_EXT, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		setPartyContactInfoButtonValid(true, false, false, false);
		this.addForward(SffOrPtyCtants.ON_PARTYCERTIFICATION_QUERY, this,
				SffOrPtyCtants.ON_PARTYCERTIFICATION_QUERY_RESPONSE);

		this.addForward(SffOrPtyCtants.ON_CLEAN_PARTY_CER, this,
				SffOrPtyCtants.ON_CLEAN_PARTY_CER_RES);

	}

	public void onCreate() throws Exception {
		/**
		 * 只有身份证查询条件的参与人管理页面，隐藏CURD功能
		 */
		if (isIdNoQueryPage) {
			this.bean.getPartyCertificationBandboxDiv().setVisible(false);
		}
	}

	public void onQueryPartyCertificationResponse(final ForwardEvent event) {
		bean.getPartyCertificationListboxPaging().setActivePage(0);
		party = (Party) event.getOrigin().getData();
		partyCertifiQue = new PartyCertification();
		partyCertifiQue.setPartyId(party.getPartyId());
		queryPartyCertificationForPaging();
	}

	/**
	 * 参与人证件选择 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 */
	public void onPartyCertificationSelectRequest() {
		if (bean.getPartyCertificationListbox().getSelectedCount() > 0) {
			partyCertifiQue = (PartyCertification) bean
					.getPartyCertificationListbox().getSelectedItem()
					.getValue();
			setPartyContactInfoButtonValid(true, true, true, true);
		}
	}

	/**
	 * 打开导入页面 zhanglu
	 */
	public void onPartyCertificationImport() throws Exception {
		Window win = (Window) Executions.createComponents(
				"/pages/party/party_certification_import.zul", null, null);
		win.doModal();
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

		setPartyContactInfoButtonValid(true, true, false, false);
		this.bean.getCertNumber().setValue("");
		this.bean.getCertOrg().setValue("");
		if (this.getParty() != null) {
			if (null == partyCertifiQue) {
				partyCertifiQue = new PartyCertification();
			}
			partyCertifiQue.setPartyId(party.getPartyId());
			queryPartyCertificationForPaging();
		}
	}

	/**
	 * 新增 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 */
	public void onPartyCertificationAdd() {
		openPartyCertificationEditWin(SffOrPtyCtants.ADD);
	}

	/**
	 * 修改 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 */
	public void onPartyCertificationEdit() {
		openPartyCertificationEditWin(SffOrPtyCtants.MOD);
	}

	/**
	 * 删除 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 */
	public void onPartyCertificationDel() {
		try {
			Window window = (Window) Executions.createComponents(
					"/pages/common/del_reason_input.zul", this, null);
			window.doModal();
			window.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						String reason = (String) event.getData();
						partyCertifiQue.setReason(reason);
						boolean blRm = partyManager
								.removePartyCertification(partyCertifiQue);
						if (blRm) {
							ZkUtil.showError("参与人证件不能为空，最少保留一个", "提示信息");
						} else {
							Messagebox.show("参与人证件删除成功！");
							setPartyContactInfoButtonValid(true, true, false,
									false);
							PubUtil.reDisplayListbox(
									bean.getPartyCertificationListbox(),
									partyCertifiQue, "del");
							queryPartyCertificationForPaging();
						}
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void openPartyCertificationEditWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			if (opType.equals("mod") || opType.equals("pwd")) {
				arg.put("partyCertification", partyCertifiQue);
			}
			arg.put("party", party);
			Window win = (Window) Executions.createComponents(
					SffOrPtyCtants.PARTYCERTIFICATION_EDIT_ZUL, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					setPartyContactInfoButtonValid(true, true, false, false);
					partyCertifiQue = (PartyCertification) event.getData();
					if (partyCertifiQue != null) {
						// PubUtil.reDisplayListbox(bean.getPartyCertificationListbox(),(PartyCertification)
						// event.getData(), type);

						if (type.equals(SffOrPtyCtants.ADD)) {
							if (!StrUtil.isEmpty(partyCertifiQue
									.getCertNumber())) {
								bean.getCertNumber().setValue(
										partyCertifiQue.getCertNumber().trim());
							}
							if (!StrUtil.isEmpty(partyCertifiQue.getCertOrg())) {
								bean.getCertOrg().setValue(
										partyCertifiQue.getCertOrg().trim());
							}
						}

						queryPartyCertificationForPaging();
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
	 * 清空选中的员工.
	 */
	public void onCleaningPartyCertification() throws Exception {
		ListboxUtils.clearListbox(bean.getPartyCertificationListbox());
	}

	/**
	 * 分页显示 .
	 * 
	 * @author Wong 2013-6-5 Wong
	 */
	public void onPartyContactInfoListboxPaging() {
		queryPartyCertificationForPaging();
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
	private void setPartyContactInfoButtonValid(boolean bImp, boolean badd,
			boolean bDel, boolean bUpdate) {
		if (isDuceTree) {
			return;
		}

		bean.getImportPartyCertificationButton().setDisabled(!bImp);
		bean.getAddPartyCertificationButton().setDisabled(!badd);
		bean.getEditPartyCertificationButton().setDisabled(!bUpdate);
		bean.getDelPartyCertificationButton().setDisabled(!bDel);
	}

	/**
	 * 选择性分页 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 */
	private void queryPartyCertificationForPaging() {

		if (null != partyCertifiQue) {
			partyCertifiQue.setCertNumber(this.bean.getCertNumber().getValue());
			partyCertifiQue.setCertOrg(this.bean.getCertOrg().getValue());
			Paging pagings = bean.getPartyCertificationListboxPaging();
			PageInfo pageInfo = partyManager.getPartyCertifications(
					partyCertifiQue, pagings.getActivePage() + 1,
					pagings.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getPartyCertificationListbox().setModel(dataList);
			bean.getPartyCertificationListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}

	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryPartyCert() throws Exception {
		this.bean.getPartyCertificationListboxPaging().setActivePage(0);
		this.queryPartyCertificationForPaging();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetPartyCertification() throws Exception {

		this.bean.getCertNumber().setValue("");

		this.bean.getCertOrg().setValue("");

	}

	/**
	 * 清理查询时候Listbox .
	 * 
	 * @author wangyong 2013-6-13 wangyong
	 */
	public void onCleanPartyCerRespons() {
		party = null;
		partyCertifiQue = null;
		ListboxUtils.clearListbox(bean.getPartyCertificationListbox());
		setPartyContactInfoButtonValid(true, false, false, false);
		this.bean.getCertNumber().setValue("");
		this.bean.getCertOrg().setValue("");
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
			this.setPartyContactInfoButtonValid(true, true, true, true);
		}
		this.isDuceTree = isDuceTree;
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canImport = false;
		boolean canAdd = false;
		boolean canUpdate = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
			if ("staffPage".equals(page)) {
				canImport = true;
			}

		} else if ("orgTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CERT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CERT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_PARTY_CERT_DEL)) {
				canDel = true;
			}
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CERT_IMPORT)) {
				canImport = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CERT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CERT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_CERT_DEL)) {
				canDel = true;
			}
		} else if ("partyPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CERT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CERT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_CERT_DEL)) {
				canDel = true;
			}
		} else if ("orgTreePage_orgParty".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CERT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CERT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_CERT_DEL)) {
				canDel = true;
			}
		} else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CERT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CERT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_CERT_DEL)) {
				canDel = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CERT_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CERT_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_CERT_DEL)) {
				canDel = true;
			}
		}

		bean.getImportPartyCertificationButton().setVisible(canImport);
		bean.getAddPartyCertificationButton().setVisible(canAdd);
		bean.getEditPartyCertificationButton().setVisible(canUpdate);
		bean.getDelPartyCertificationButton().setVisible(canDel);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canImport = false;
		boolean canAdd = false;
		boolean canUpdate = false;
		boolean canDel = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {

			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_PARTY_CERT_DEL)) {
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
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_DEL)) {
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
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_DEL)) {
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
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_PARTY_CERT_DEL)) {
						canDel = true;
					}

				}

			} else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("politicalTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("agentTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("ossTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("edwTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_CERT_DEL)) {
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

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_DEL)) {
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

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_DEL)) {
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

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_CERT_DEL)) {
						canDel = true;
					}

				}

			} else if ("costTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_CERT_DEL)) {
					canDel = true;
				}
			} else if ("multidimensionalTab".equals(orgTreeTabName)) {
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CERT_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CERT_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil
						.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.ORG_TREE_MULTIDIMENSIONAL_ORG_PARTY_CERT_DEL)) {
					canDel = true;
				}
			}
		}

		bean.getImportPartyCertificationButton().setVisible(canImport);
		bean.getAddPartyCertificationButton().setVisible(canAdd);
		bean.getEditPartyCertificationButton().setVisible(canUpdate);
		bean.getDelPartyCertificationButton().setVisible(canDel);
	}
}
