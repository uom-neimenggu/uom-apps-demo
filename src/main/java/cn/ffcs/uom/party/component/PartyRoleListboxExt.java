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
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.party.component.bean.PartyRoleListboxBean;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 参与人角色 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wangyong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-25
 * @Email wangyong@ffcs.cn
 * @功能说明：
 * 
 */
public class PartyRoleListboxExt extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");
	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	private PartyRoleListboxBean bean = new PartyRoleListboxBean();

	private Party party;
	/**
	 * 组织树中选中的organization
	 */
	@Setter
	private Organization organization;

	private PartyRole partyRoleSele;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	/**
	 * 是否是只有身份证查询条件的参与人管理页面
	 */
	@Getter
	@Setter
	private Boolean isIdNoQueryPage = false;

	public PartyRoleListboxExt() {
		Executions.createComponents(SffOrPtyCtants.ZUL_PARTY_ROLE_LISTBOX_EXT,
				this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.bindCombobox();
		setPartyRoleButtonValid(true, true, true);
		this.addForward(SffOrPtyCtants.ON_PARTY_ROLE_QUERY, this,
				SffOrPtyCtants.ON_PARTY_ROLE_QUERY_RES);

	}

	public void onCreate() throws Exception {
		/**
		 * 只有身份证查询条件的参与人管理页面，隐藏CURD功能
		 */
		if (isIdNoQueryPage) {
			this.bean.getPartyRoleBandboxDiv().setVisible(false);
		}

	}

	private void bindCombobox() {
		List<NodeVo> liTp = UomClassProvider.getValuesList("PartyRole",
				"roleType");
		ListboxUtils.rendererForEdit(this.bean.getRoleType(), liTp);
	}

	/**
	 * 新增 .
	 * 
	 * @author wangyong 2013-6-25 wangyong
	 */
	public void onPartyRoleAdd() {
		openPartyRoleWin(SffOrPtyCtants.ADD);
	}

	/**
	 * 修改 .
	 * 
	 * @author wangyong 2013-6-25 wangyong
	 */
	public void onPartyRoleEdit() {
		openPartyRoleWin(SffOrPtyCtants.MOD);
	}

	/**
	 * 删除 .
	 * 
	 * @author wangyong 2013-6-25 wangyong
	 */
	public void onPartyRoleDel() {
		try {
			Messagebox.show("您确定要删除吗？", "提示信息", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.INFORMATION,
					new EventListener() {
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {
								setPartyRoleButtonValid(true, false, false);
								partyManager.removePartyRole(partyRoleSele);
								Messagebox.show("参与人角色删除成功！");
								PubUtil.reDisplayListbox(
										bean.getPartyRoleListbox(),
										partyRoleSele, "del");
							}
						}
					});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择参与人角色 .
	 * 
	 * @author wangyong 2013-6-25 wangyong
	 */
	public void onPartyRoleSelectRequest() {
		Listbox lb = bean.getPartyRoleListbox();
		if (lb.getSelectedCount() > 0) {
			partyRoleSele = (PartyRole) lb.getSelectedItem().getValue();
			setPartyRoleButtonValid(false, false, false);
		}
	}

	/**
	 * 分页信息 .
	 * 
	 * @author wangyong 2013-6-25 wangyong
	 */
	public void onPartyRoleListboxPaging() {
		queryPartyRoleForPaging();
	}

	private void openPartyRoleWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			if (opType.equals("mod") || opType.equals("pwd")) {
				arg.put("partyRole", partyRoleSele);
			}
			arg.put("party", party);
			Window win = (Window) Executions.createComponents(
					SffOrPtyCtants.ZUL_PARTY_ROLE_EDIT, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					setPartyRoleButtonValid(true, false, false);
					if (event.getData() != null) {
						// PubUtil.reDisplayListbox(bean.getPartyRoleListbox(),(PartyRole)
						// event.getData(), type);
						PartyRole partyRole = (PartyRole) event.getData();
						if (type.equals(SffOrPtyCtants.ADD)
								&& !StrUtil.isEmpty(partyRole.getRoleType())) {
							ListboxUtils.selectByCodeValue(bean.getRoleType(),
									partyRole.getRoleType().trim());
						}

						queryPartyRoleForPaging();
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

	public void onPartyRoleQueryRespons(final ForwardEvent event) {
		party = (Party) event.getOrigin().getData();
		this.bean.getRoleType().setSelectedIndex(0);
		if (null != party) {
			setPartyRoleButtonValid(false, true, true);
			queryPartyRoleForPaging();
		}
	}

	/**
	 * 选择性分页 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 */
	private void queryPartyRoleForPaging() {
		if (null != party) {
			party.setRoleType((String) this.bean.getRoleType()
					.getSelectedItem().getValue());
			Paging pagings = bean.getPartyRoleListboxPaging();
			PageInfo pageInfo = partyManager.getPartyRolePageInfo(party,
					pagings.getActivePage() + 1, pagings.getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getPartyRoleListbox().setModel(dataList);
			bean.getPartyRoleListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryPartyRole() throws Exception {
		this.bean.getPartyRoleListboxPaging().setActivePage(0);
		this.queryPartyRoleForPaging();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetPartyRole() throws Exception {

		this.bean.getRoleType().setSelectedIndex(0);

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
	private void setPartyRoleButtonValid(boolean badd, boolean bDel,
			boolean bUpdate) {
		if (isDuceTree) {
			return;
		}
		bean.getAddPartyRoleButton().setDisabled(badd);
		bean.getEditPartyRoleButton().setDisabled(bUpdate);
		bean.getDelPartyRoleButton().setDisabled(bDel);
	}

	public void onCleanPartyRole() {
		ListboxUtils.clearListbox(bean.getPartyRoleListbox());
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
			this.setPartyRoleButtonValid(false, false, false);
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

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_ROLE_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_PARTY_ROLE_DEL)) {
				canDel = true;
			}
		} else if ("partyPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_ROLE_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_ROLE_DEL)) {
				canDel = true;
			}
		} else if ("orgTreePage_orgParty".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_ROLE_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_PARTY_ROLE_DEL)) {
				canDel = true;
			}
		} else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_ROLE_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_PARTY_ROLE_DEL)) {
				canDel = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_ROLE_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_ROLE_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_PARTY_ROLE_DEL)) {
				canDel = true;
			}
		}

		bean.getAddPartyRoleButton().setVisible(canAdd);
		bean.getEditPartyRoleButton().setVisible(canUpdate);
		bean.getDelPartyRoleButton().setVisible(canDel);
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

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpdate = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("politicalTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_ROLE_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_ROLE_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_ORG_PARTY_ROLE_DEL)) {
					canDel = true;
				}
			} else if ("agentTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_ROLE_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_ROLE_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_ORG_PARTY_ROLE_DEL)) {
					canDel = true;
				}
			} else if ("ibeTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_ROLE_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_ROLE_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_ORG_PARTY_ROLE_DEL)) {
					canDel = true;
				}
			} else if ("supplierTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_ROLE_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_ROLE_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_ORG_PARTY_ROLE_DEL)) {
					canDel = true;
				}
			} else if ("ossTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_ROLE_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_ROLE_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_ORG_PARTY_ROLE_DEL)) {
					canDel = true;
				}
			} else if ("edwTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_ROLE_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_ROLE_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_ORG_PARTY_ROLE_DEL)) {
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
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_ROLE_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil
							.checkHasPermission(
									getPortletInfoProvider(),
									ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_ROLE_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_ORG_PARTY_ROLE_DEL)) {
						canDel = true;
					}

				}

			} else if ("costTab_orgParty".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_ROLE_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_ROLE_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_ORG_PARTY_ROLE_DEL)) {
					canDel = true;
				}
			}
		}
		bean.getAddPartyRoleButton().setVisible(canAdd);
		bean.getEditPartyRoleButton().setVisible(canUpdate);
		bean.getDelPartyRoleButton().setVisible(canDel);
	}
}
