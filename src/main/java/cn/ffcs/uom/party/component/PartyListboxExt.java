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

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
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
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.component.bean.PartyListboxExtBean;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

public class PartyListboxExt extends Div implements IdSpace {
	private static final long serialVersionUID = 1L;

	PartyListboxExtBean bean = new PartyListboxExtBean();

	private Party party;

	private Party partyQu;

	/**
	 * 参与人类型是否是个人
	 */
	@Getter
	@Setter
	private Boolean isPartyTypePersonal = false;

	/**
	 * 参与人是否是游离状态
	 */
	@Getter
	@Setter
	private Boolean isFreeParty = false;

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private GroupMailManager groupMailManager = (GroupMailManager) ApplicationContextUtil
			.getBean("groupMailManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * zul.
	 */
	private final String zul = "/pages/party/comp/party_listbox_ext.zul";

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

	public PartyListboxExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_PARTY_QUERY, this,
				SffOrPtyCtants.ON_PARTY_QUERY_RESPONSE);
		bindCombobox();
		this.setPartyButtonValid(true, false, false, false, false);

	}

	public void onCreate() throws Exception {
		/**
		 * 只有身份证查询条件的参与人管理页面，隐藏其他查询条件和CURD功能
		 */
		if (isIdNoQueryPage) {
			this.bean.getPartyNameLab().setVisible(false);
			this.bean.getPartyName().setVisible(false);
			this.bean.getStaffAccountLab().setVisible(false);
			this.bean.getStaffAccount().setVisible(false);
			// this.bean.getMobilePhoneLab().setVisible(false);
			// this.bean.getMobilePhone().setVisible(false);
			this.bean.getPartyTypeLab().setVisible(false);
			this.bean.getPartyType().setVisible(false);
			this.bean.getFreePartyLab().setVisible(false);
			this.bean.getFreeParty().setVisible(false);
			this.bean.getPartyWindowDiv().setVisible(false);
			this.bean.getCertNumber().setWidth("220px");
		} else {
			// 去除默认查询onPartyQuery();
		}
	}

	/**
	 * 绑定下拉框. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-30 Wong
	 */
	private void bindCombobox() {
		try {
			List<NodeVo> liTp = UomClassProvider.getValuesList("Party",
					"partyType");
			ListboxUtils.rendererForEdit(bean.getPartyType(), liTp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onQueryPartyResponse(final ForwardEvent event) throws Exception {
		bean.getPartyListboxPaging().setActivePage(0);
		partyQu = (Party) event.getOrigin().getData();
		this.queryParty();
		if (this.bean.getPartyListbox().getItemCount() == 0) {
			this.setPartyButtonValid(true, false, false, false, false);
		}
	}

	/**
	 * 参与人选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPartySelectRequest() throws Exception {
		if (bean.getPartyListbox().getSelectedCount() > 0) {
			party = (Party) bean.getPartyListbox().getSelectedItem().getValue();
			this.setPartyButtonValid(true, true, true, true, true);
			Events.postEvent(SffOrPtyCtants.ON_PARTY_SELECT, this, party);
		}
	}

	/**
	 * 设置参与人按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canSelect
	 *            选择按钮
	 */
	private void setPartyButtonValid(final Boolean canAdd,
			final Boolean BlView, final Boolean canEdit,
			final Boolean canDelete, final Boolean canSelect) {
		if (canAdd != null) {
			bean.getAddPartyButton().setDisabled(!canAdd);
		}
		bean.getViewButton().setDisabled(!BlView);
		bean.getEditPartyButton().setDisabled(!canEdit);
		bean.getDelPartyButton().setDisabled(!canDelete);
		bean.getSelectPartyButton().setDisabled(!canSelect);
	}

	/**
	 * 查询实体 .
	 * 
	 * @author Wong 2013-5-27 Wong
	 */
	public void queryParty() {
		if (this.partyQu != null) {
			String saStr = partyQu.getStaffAccount();
			if (!StrUtil.isNullOrEmpty(saStr)) {
				StaffAccount sa = new StaffAccount();
				sa.setStaffAccount(saStr);
				Staff staff = staffManager.getStaffByStaffId(sa);
				if (null != staff) {
					partyQu.setStaff(staff);
				}
			}
			/**
			 * 员工账号查询
			 */
			if (!StrUtil.isNullOrEmpty(saStr) && null == partyQu.getStaff()) {
				ListboxUtils.clearListbox(this.bean.getPartyListbox());
				this.bean.getPartyListboxPaging().setTotalSize(0);
			} else {
				PageInfo pageInfo = partyManager.forQuertyParty(partyQu, bean
						.getPartyListboxPaging().getActivePage() + 1, bean
						.getPartyListboxPaging().getPageSize());
				ListModel dataList = new BindingListModelList(
						pageInfo.getDataList(), true);
				this.bean.getPartyListbox().setModel(dataList);
				this.bean.getPartyListboxPaging().setTotalSize(
						NumericUtil.nullToZero(pageInfo.getTotalCount()));
			}
		}
		Events.postEvent(SffOrPtyCtants.ON_PARTY_MANAGE_QUERY, this, null);
		this.setPartyButtonValid(true, false, false, false, false);
	}

	/**
	 * 新增参与人 .
	 * 
	 * @author Wong 2013-5-25 Wong
	 */
	public void onPartyAdd() {
		openPartyEditWin(SffOrPtyCtants.ADD);
	}

	/**
	 * 修改参与人 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-28 Wong
	 */
	public void onPartyEdit() throws Exception {
		openPartyEditWin(SffOrPtyCtants.MOD);
	}

	public void onView() {
		try {
			openPartyEditWin(SffOrPtyCtants.VIEW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除参与人.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPartyDel() throws Exception {
		Messagebox.show("您确定要删除吗？", "提示信息", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.INFORMATION, new EventListener() {
					public void onEvent(Event event) throws Exception {
						Integer result = (Integer) event.getData();
						if (result == Messagebox.OK) {
							setStaffButtonValid(true, false, false, false);
							if (null != party) {
								Staff tempStaff = staffManager
										.getStaffByPartyRoleId(Long
												.parseLong(party
														.getPartyRoleId()));
								if (!StrUtil.isNullOrEmpty(tempStaff)) {
									Messagebox.show("该参与人有对应的员工信息，不能删除。");
									return;
								}

								Staff staff = staffManager
										.queryStaffByPartyId(party.getPartyId());

								partyManager.delParty(party);
								Messagebox.show("参与人删除成功！");

								if (staff != null && staff.getStaffId() != null) {

									if (!StrUtil.isNullOrEmpty(staff
											.getWorkProp())
											&& (staff
													.getWorkProp()
													.startsWith(
															SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
													.getWorkProp()
													.startsWith(
															SffOrPtyCtants.WORKPROP_N_P_PRE))) {

										PartyContactInfo partyContactInfo = partyManager
												.getDefaultPartyContactInfo(party
														.getPartyId());

										if (partyContactInfo != null
												&& !StrUtil
														.isEmpty(partyContactInfo
																.getGrpUnEmail())) {

											party.setPartyContactInfo(partyContactInfo);

											boolean groupMailInterFaceSwitch = UomClassProvider
													.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

											if (groupMailInterFaceSwitch) {
												String msg = groupMailManager
														.groupMailPackageInfo(
																GroupMailConstant.GROUP_MAIL_BIZ_ID_5,
																party, null);
												if (!StrUtil.isEmpty(msg)) {
													ZkUtil.showError(msg,
															"提示信息");
													// return;
												}
											}
										}
									}
								}

							} else {
								Messagebox.show("请选择参与人信息！");
							}
							PubUtil.reDisplayListbox(bean.getPartyListbox(),
									party, "del");
							cleanTabs();
						}
					}
				});
	}

	/**
	 * 编辑页面 .
	 * 
	 * @param opType
	 * @author Wong 2013-5-30 Wong
	 */
	private void openPartyEditWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			arg.put("variablePageLocation", variablePageLocation);
			arg.put("portletInfoProvider", portletInfoProvider);
			if (opType.equals(SffOrPtyCtants.MOD)
					|| opType.equals(SffOrPtyCtants.VIEW)) {
				arg.put("party", party);
			}
			String zul = SffOrPtyCtants.PARTY_EDIT_ZUL;
			if (opType.equals(SffOrPtyCtants.VIEW)) {
				zul = SffOrPtyCtants.PARTY_DETAIL_ZUL;
			}
			Window win = (Window) Executions.createComponents(zul, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					setPartyButtonValid(true, false, false, false, false);
					if (event.getData() != null) {
						partyQu = (Party) event.getData();
						// PubUtil.reDisplayListbox(bean.getPartyListbox(),(Party)
						// event.getData(), type);

						if (SffOrPtyCtants.ADD.equals(type)) {
							// 新增参与人后，用于显示该条记录
							if (!StrUtil.isEmpty(partyQu.getPartyName())) {
								bean.getPartyName().setValue(
										partyQu.getPartyName().trim());
							}

							if (!StrUtil.isEmpty(partyQu.getMobilePhone())) {
								bean.getMobilePhone().setValue(
										partyQu.getMobilePhone());
							}

							if (!StrUtil.isEmpty(partyQu.getPartyType())) {
								ListboxUtils.selectByCodeValue(bean
										.getPartyType(), partyQu.getPartyType()
										.trim());
							}

							if (!StrUtil.isEmpty(partyQu.getCertNumber())) {
								bean.getCertNumber().setValue(
										partyQu.getCertNumber().trim());
							}

							if (!StrUtil.isEmpty(partyQu.getStaffAccount())) {
								bean.getStaffAccount().setValue(
										partyQu.getStaffAccount().trim());
							}

						} else if (SffOrPtyCtants.MOD.equals(type)) {

							Staff staff = staffManager
									.queryStaffByPartyId(partyQu.getPartyId());

							if (staff != null && staff.getStaffId() != null) {
								if (!StrUtil.isNullOrEmpty(staff.getWorkProp())
										&& (staff
												.getWorkProp()
												.startsWith(
														SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
												.getWorkProp()
												.startsWith(
														SffOrPtyCtants.WORKPROP_N_P_PRE))) {

									StaffOrganization staffOrganization = staff
											.getStaffOrganization();

									if (staffOrganization != null) {

										Organization org = staffOrganization
												.getOrganization();

										if (org != null
												&& org.getOrgId() != null
												&& org.isUploadGroupMail()) {

											OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
											organizationExtendAttr.setOrgId(org
													.getOrgId());
											organizationExtendAttr
													.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

											organizationExtendAttr = organizationExtendAttrManager
													.queryOrganizationExtendAttr(organizationExtendAttr);

											if ((organizationExtendAttr != null && !StrUtil
													.isEmpty(organizationExtendAttr
															.getOrgAttrValue()))
													|| org.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null) {

												if (staff.getPartyRoleId() != null) {
													PartyRole partyRole = staffManager.getPartyRole(staff
															.getPartyRoleId());
													if (partyRole != null
															&& partyRole
																	.getPartyId() != null) {
														Party party = staffManager
																.getParty(partyRole
																		.getPartyId());
														PartyContactInfo partyContactInfo = partyManager
																.getDefaultPartyContactInfo(partyRole
																		.getPartyId());

														if (party != null
																&& partyContactInfo != null
																&& !StrUtil
																		.isEmpty(partyContactInfo
																				.getGrpUnEmail())) {
															party.setPartyContactInfo(partyContactInfo);
															boolean groupMailInterFaceSwitch = UomClassProvider
																	.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

															if (groupMailInterFaceSwitch) {
																String msg = groupMailManager
																		.groupMailPackageInfo(
																				GroupMailConstant.GROUP_MAIL_BIZ_ID_14,
																				party,
																				org);
/*																if (!StrUtil.isEmpty(msg)) {
																	ZkUtil.showError(msg,"提示信息");
																	return;
																}*/
															}
														}
													}
												}

											}
										}
									}

								}
							}
						}

						queryParty();
					}
				}
			});
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setPartyWindowDivVisible(boolean visible) {
		bean.getPartyWindowDiv().setVisible(visible);
	}

	/**
	 * 设置参与人按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canSelect
	 *            选择按钮
	 */
	private void setStaffButtonValid(final Boolean canAdd,
			final Boolean canEdit, final Boolean canDelete,
			final Boolean canSelect) {
		if (canAdd != null) {
			this.bean.getAddPartyButton().setDisabled(!canAdd);
		}
		this.bean.getEditPartyButton().setDisabled(!canEdit);
		this.bean.getDelPartyButton().setDisabled(!canDelete);
		this.bean.getSelectPartyButton().setDisabled(!canSelect);
	}

	/**
	 * 选择选中的员工.
	 */
	public void onSelectPartyInfo() throws Exception {
		if (bean.getPartyListbox().getSelectedItem() != null) {
			Events.postEvent(SffOrPtyCtants.ON_PARTY_CONFIRM, this, bean
					.getPartyListbox().getSelectedItem().getValue());
		} else {

		}
	}

	/**
	 * 清空选中的员工.
	 */
	public void onCleanPartyInfo() throws Exception {
		this.bean.getPartyListbox().clearSelection();
		Events.postEvent(SffOrPtyCtants.ON_PARTY_CLEAN, this, null);
	}

	/**
	 * 关闭.
	 */
	public void onClosePartyInfo() throws Exception {
		Events.postEvent(SffOrPtyCtants.ON_PARTY_CLOSE, this, null);
	}

	public void onPartyListboxPaging() throws Exception {
		queryParty();
	}

	/**
	 * 查询参与人 .
	 * 
	 * @author Wong 2013-6-3 Wong
	 */
	public void onPartyQuery() {
		cleanTabs();
		Party p = Party.newInstance();
		PubUtil.fillPoFromBean(bean, p);
		partyQu = p;
		if (isIdNoQueryPage) {// 去除对身份证页面查询的影响
			partyQu.setPartyType(null);
			partyQu.setFreeParty(null);
		}
		this.bean.getPartyListboxPaging().setActivePage(0);
		queryParty();
	}

	/**
	 * 
	 * .
	 * 
	 * @author wangyong 2013-6-22 wangyong
	 */
	public void onPartyReset() {
		bean.getPartyName().setValue(null);
		bean.getMobilePhone().setValue(null);
		if (isPartyTypePersonal) {
			bean.getPartyType().setSelectedIndex(1);
		} else {
			bean.getPartyType().setSelectedIndex(0);
		}
		bean.getCertNumber().setValue(null);
		bean.getStaffAccount().setValue(null);
		ListboxUtils.selectByCodeValue(bean.getFreeParty(),
				PartyConstant.FREE_PARTY);
	}

	public void cleanTabs() {
		partyQu = null;
		party = null;
		Events.postEvent(SffOrPtyCtants.ON_PARTY_CLEAR_TABS, this, null);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canDel = false;
		boolean canEdit = false;
		boolean canView = false;
		boolean canPartyTypePersonal = false;
		boolean canFreeParty = false;

		variablePageLocation = page;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canDel = true;
			canEdit = true;
			canView = true;
			canPartyTypePersonal = false;
			canFreeParty = false;
		} else if ("partyPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY__DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY__EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_VIEW)) {
				canView = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.PARTY_TYPE_PERSONAL)) {
				canPartyTypePersonal = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.FREE_PARTY)) {
				canFreeParty = true;
			}
		}

		bean.getAddPartyButton().setVisible(canAdd);
		bean.getDelPartyButton().setVisible(canDel);
		bean.getEditPartyButton().setVisible(canEdit);
		bean.getViewButton().setVisible(canView);
		bean.getPartyType().setDisabled(canPartyTypePersonal);
		bean.getFreeParty().setDisabled(canFreeParty);
		this.isPartyTypePersonal = canPartyTypePersonal;
		this.isFreeParty = canFreeParty;
		if (isPartyTypePersonal) {
			bean.getPartyType().setSelectedIndex(1);
		} else {
			bean.getPartyType().setSelectedIndex(0);
		}
	}
}
