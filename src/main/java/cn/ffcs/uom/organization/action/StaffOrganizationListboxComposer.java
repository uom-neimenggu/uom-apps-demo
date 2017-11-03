package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.action.bean.StaffOrganizationListboxBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 员工组织关系管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked", "unused" })
public class StaffOrganizationListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private StaffOrganizationListboxBean bean = new StaffOrganizationListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("staffOrganizationManager")
	private StaffOrganizationManager staffOrganizationManager = (StaffOrganizationManager) ApplicationContextUtil
			.getBean("staffOrganizationManager");
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	private GroupMailManager groupMailManager = (GroupMailManager) ApplicationContextUtil
			.getBean("groupMailManager");

	/**
	 * manager
	 */
	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/organization/staff_organization_listbox.zul";

	/**
	 * 当前组织树中选择的organization
	 */
	private Organization organization;
	/**
	 * 当前选择的staffOrganization
	 */
	private StaffOrganization staffOrganization;

	@Getter
	@Setter
	private Staff staff;

	@Autowired
	@Qualifier("staffManager")
	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	@Autowired
	@Qualifier("partyManager")
	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	/**
	 * 日志服务队列
	 */
	@Autowired
	@Qualifier("logService")
	private LogService logService = (LogService) ApplicationContextUtil
			.getBean("logService");

	/**
	 * 查询staffOrganization.
	 */
	private StaffOrganization qryStaffOrganization;
	private Staff qryStaff;
	/**
	 * 选中的员工组织关系
	 */
	private List<StaffOrganization> staffOrganizationList;
	/**
	 * 操作类型
	 */
	private String opType;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;
	/**
	 * 是否是推导树
	 */
	@Getter
	private Boolean isDuceTree = false;

	/**
	 * 是否是内部组织tab
	 */
	@Getter
	@Setter
	private Boolean isPoliticalTab = false;

	/**
	 * 是否是代理商tab
	 */
	@Getter
	@Setter
	private Boolean isAgentTab = false;
	/**
	 * 是否是内部经营实体tab
	 */
	@Getter
	@Setter
	private Boolean isIbeTab = false;
	/**
	 * 是否是组织树页面
	 */
	@Getter
	@Setter
	private Boolean isOrgTreePage = false;
	/**
	 * 是否是聚合营销2015tab
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;
	/**
	 * 组织管理区分
	 */
	@Getter
	@Setter
	private String variablePagePosition;
	/**
	 * 页面定位
	 */
	@Getter
	@Setter
	private String variablePageLocation;
	/**
	 * 要修改的组织员工关系
	 */
	private StaffOrganization updateStaffOrganization;

	/**
	 * 推导树全部按钮不让编辑
	 * 
	 * @param isDuceTree
	 */
	public void setDuceTree(boolean isDuceTree) {
		if (isDuceTree) {
			this.setStaffOrganizationButtonValid(false, false, false, false,
					false, false, false);
			/**
			 * 推导树不分页
			 */
			this.bean.getStaffOrganizationListPaging().setVisible(false);
		}
		this.isDuceTree = isDuceTree;
	}

	public StaffOrganizationListboxComposer() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');

		// 接受抛出的查询事件
		this.addEventListener(
				StaffOrganizationConstant.ON_STAFF_ORGANIZATION_QUERY,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						StaffOrganization staffOrganization = (StaffOrganization) event
								.getData();
						if (!StrUtil.isNullOrEmpty(staffOrganization)) {
							organization = new Organization();
							organization.setOrgId(staffOrganization.getOrgId());
						}

						if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
							setOrgTreeTabName(variableOrgTreeTabName);
						}

						if (!StrUtil.isEmpty(variablePagePosition)) {
							setPagePosition(variablePagePosition);
						}

						queryStaffOrganizationHandle(staffOrganization);
					}
				});

		this.addForward(SffOrPtyCtants.ON_CLEAN_STAFF_ORG, this,
				SffOrPtyCtants.ON_CLEAN_STAFF_ORG_RES);

		this.addForward(SffOrPtyCtants.ON_STAFF_Org_QUERY, this,
				SffOrPtyCtants.ON_STAFF_Org_QUERY_RESP);
		/**
		 * 组织列表查询事件响应，清除列表
		 */
		this.addForward(OrganizationConstant.ON_ORGANIZATION_QUERY, this,
				"onOrganiztionQueryResponse");
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {

		this.setStaffOrganizationButtonValid(false, false, false, false, false,
				false, false);
		this.bean.getStaffOrganizationListBox().setPageSize(10);
		/**
		 * 多选
		 */
		this.bean.getStaffOrganizationListBox().setCheckmark(true);
		this.bean.getStaffOrganizationListBox().setMultiple(true);
		/**
		 * 组织树页面
		 */
		if (isOrgTreePage) {
			this.bean.getAddStaffOrganizationButton().setLabel("添加员工");
		}
	}

	/**
	 * 组织列表查询响应，清空列表
	 * 
	 * @throws Exception
	 */
	public void onOrganiztionQueryResponse() throws Exception {
		this.qryStaffOrganization = null;
		PubUtil.clearListbox(this.bean.getStaffOrganizationListBox());
		this.setStaffOrganizationButtonValid(false, false, false, false, false,
				false, false);
	}

	/**
	 * 查询员工组织关系列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void queryStaffOrganizationHandle(
			StaffOrganization qryStaffOrganization) throws Exception {
		this.bean.getStaffName().setValue("");
		this.bean.getUserCode().setValue("");
		this.qryStaffOrganization = qryStaffOrganization;
		this.onQueryStaffOrganization();
	}

	/**
	 * 员工组织关系批量选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffOrganizationSelectRequest() throws Exception {
		int selCount = bean.getStaffOrganizationListBox().getSelectedCount();
		if (selCount > 0) {
			this.setStaffOrganizationButtonValid(true, true, true, true, true,
					true, true);
			Set set = bean.getStaffOrganizationListBox().getSelectedItems();
			Iterator it = set.iterator();
			if (it != null) {
				staffOrganizationList = new ArrayList<StaffOrganization>();
				while (it.hasNext()) {
					Listitem listitem = (Listitem) it.next();
					staffOrganizationList.add((StaffOrganization) listitem
							.getValue());
				}
			}
			/**
			 * 选择组织员工事件
			 */
			if (selCount == 1) {
				staffOrganization = staffOrganizationList.get(selCount - 1);
				Long staffId = staffOrganization.getStaffId();
				if (null != staffId) {
					Staff staff = staffManager.queryStaff(staffId);
					if (null != staff) {
						PartyRole partyRole = partyManager.getPartyRole(staff
								.getPartyRoleId());
						if (null != partyRole) {
							Party party = partyManager.queryParty(partyRole
									.getPartyId());
							party.setPartyRoleId(staff.getPartyRoleId()
									.toString());
							Events.postEvent(
									OrganizationConstant.ON_STAFFORGANIZATION_SELECT,
									this, party);
						}
					}
				}
			}
		} else {
			staffOrganizationList = null;
			this.setStaffOrganizationButtonValid(true, true, false, false,
					false, false, false);
		}
		// 当选择的员工组织不等于1时清空员工证件、员工联系人
		if (selCount != 1) {
			Events.postEvent(OrganizationConstant.ON_STAFFORGANIZATION_SELECT,
					this, null);
		}
	}

	/**
	 * 新增员工组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffOrganizationAdd() throws Exception {
		bean.getAddStaffOrganizationButton().setDisabled(true);
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		opType = "add";
		this.openStaffOrganizationEditWin();
	}

	/**
	 * 新增所有（包括员工、参与人....）.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffOrganizationAddAll() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		opType = "add";
		this.openStaffOrganizationEditAllWin();
	}

	/**
	 * 修改员工组织关系
	 * 
	 * @throws Exception
	 */
	public void onStaffOrganizationUpdate() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		opType = "mod";
		if (staffOrganizationList != null && staffOrganizationList.size() != 1) {
			ZkUtil.showError("请选择你要操作的员工,只能修改一条", "提示信息");
			return;
		} else {
			updateStaffOrganization = staffOrganizationList.get(0);
		}
		this.openStaffOrganizationEditWin();
	}

	/**
	 * 修改员工组织关系（参与人、员工..）
	 * 
	 * @throws Exception
	 */
	public void onStaffOrganizationUpdateAll() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		opType = "mod";
		if (staffOrganizationList != null && staffOrganizationList.size() != 1) {
			ZkUtil.showError("请选择你要操作的员工,只能修改一条", "提示信息");
			return;
		} else {
			updateStaffOrganization = staffOrganizationList.get(0);
		}
		this.openStaffOrganizationEditAllWin();
	}

	/**
	 * 查看员工、参与人信息
	 * 
	 * @throws Exception
	 */
	public void onStaffOrganizationView() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		opType = "view";
		if (staffOrganizationList != null && staffOrganizationList.size() != 1) {
			ZkUtil.showError("每次只能查看一条组织员工信息。", "提示信息");
			return;
		} else {
			qryStaffOrganization = staffOrganizationList.get(0);
		}
		this.openStaffOrganizationViewWin();
	}

	/**
	 * 打开员工组织关系查看窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openStaffOrganizationViewWin() throws Exception {
		Map arg = new HashMap();
		arg.put("opType", opType);
		arg.put("staffOrganization", qryStaffOrganization);

		Window win = (Window) Executions.createComponents(
				"/pages/organization/staff_organization_detail.zul", this, arg);
		win.doModal();
	}

	/**
	 * 打开导入页面 xiaof
	 */
	public void onStaffOrganizationImport() throws Exception {
		Window win = (Window) Executions
				.createComponents(
						"/pages/organization/staff_organization_import.zul",
						null, null);
		win.doModal();
	}

	/**
	 * 移动员工
	 * 
	 * @throws Exception
	 */
	public void onStaffOrganizationBatchMove() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		if (staffOrganizationList != null && staffOrganizationList.size() > 0) {
			opType = "move";
			Map arg = new HashMap();
			arg.put("opType", opType);
			arg.put("oldStaffOrganization", qryStaffOrganization);
			Window win = (Window) Executions.createComponents(
					"/pages/organization/staff_organization_move.zul", this,
					arg);
			win.doModal();
			win.addEventListener(Events.ON_OK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						if (event.getData() != null) {
							/**
							 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
							 */
							SysLog log = new SysLog();
							log.startLog(new Date(), SysLogConstrants.STAFF);
							// 获取当前操作用户
							// log.setUser(PlatformUtil.getCurrentUser());
							Organization targetOrganization = (Organization) event
									.getData();
							/**
							 * 已存在员工关系判断
							 */
							for (StaffOrganization so : staffOrganizationList) {
								StaffOrganization quertStaffOrganization = new StaffOrganization();
								quertStaffOrganization.setStaffId(so
										.getStaffId());
								/*
								 * zhulintao 2014-07-01
								 * 修复同一个员工从不同组织中移动到同一个组织中时，产生重复的员工组织关系。【
								 * 同一员工在同一组织中只能有一种关系】
								 */
								// quertStaffOrganization.setRalaCd(so.getRalaCd());
								quertStaffOrganization
										.setOrgId(targetOrganization.getOrgId());

								// 添加员工组织关系规则校验 zhulintao
								if (true) {
									Staff newStaffObj = staffManager
											.queryStaff(so.getStaffId());
									Organization sourceOrganization = so
											.getOrganization();

									if (!StrUtil.isNullOrEmpty(newStaffObj)) {

										String msg = staffOrganizationManager
												.doStaffOrgRelRule(newStaffObj,
														sourceOrganization,
														targetOrganization);
										if (!StrUtil.isNullOrEmpty(msg)) {
											ZkUtil.showError(msg, "提示信息");
											return;
										}

									}
								}

								List<StaffOrganization> soList = staffOrganizationManager
										.queryStaffOrganizationList(quertStaffOrganization);
								if (soList != null && soList.size() > 0) {
									ZkUtil.showError("员工：" + so.getStaffName()
											+ "在该组织下已存在，请确认", "提示信息");
									return;
								}

								if (so != null) {

									Organization org = targetOrganization;
									Staff staff = so.getStaff();

									OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
									organizationExtendAttr.setOrgId(org
											.getOrgId());
									organizationExtendAttr
											.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

									organizationExtendAttr = organizationExtendAttrManager
											.queryOrganizationExtendAttr(organizationExtendAttr);

									if (org != null
											&& staff != null
											&& ((organizationExtendAttr != null && !StrUtil
													.isEmpty(organizationExtendAttr
															.getOrgAttrValue())) || org
													.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null)) {
										if (!StrUtil.isNullOrEmpty(staff
												.getWorkProp())
												&& (staff
														.getWorkProp()
														.startsWith(
																SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
														.getWorkProp()
														.startsWith(
																SffOrPtyCtants.WORKPROP_N_P_PRE))
												&& org.isUploadGroupMail()) {
											if (staff.getPartyRoleId() != null) {
												PartyRole partyRole = staffManager.getPartyRole(staff
														.getPartyRoleId());
												if (partyRole != null
														&& partyRole
																.getPartyId() != null) {
													Party party = staffManager.getParty(partyRole
															.getPartyId());
													PartyContactInfo partyContactInfo = partyManager
															.getDefaultPartyContactInfo(partyRole
																	.getPartyId());

													Individual individual = partyManager
															.getIndividual(partyRole
																	.getPartyId());

													party.setIndividual(individual);
													party.setStaff(staff);
													party.setStaffOrganization(staffOrganization);

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
																			GroupMailConstant.GROUP_MAIL_BIZ_ID_2,
																			party,
																			org);

															if (!StrUtil
																	.isEmpty(msg)) {
																if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_TRUE
																		.equals(msg)) {
																	msg = groupMailManager
																			.groupMailPackageInfo(
																					GroupMailConstant.GROUP_MAIL_BIZ_ID_14,
																					party,
																					org);
																} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_FALSE
																		.equals(msg)) {

																	msg = groupMailManager
																			.groupMailPackageInfo(
																					GroupMailConstant.GROUP_MAIL_BIZ_ID_13,
																					party,
																					org);
																}

																/*
																 * if
																 * (!StrUtil.isEmpty
																 * (msg)) {
																 * ZkUtil
																 * .showError(
																 * msg, "提示信息");
																 * return; }
																 */

															}
														}
													}
												}
											}

										}

									}

								}

							}
							staffOrganizationManager
									.updateBatchMoveStaffOrganization(
											staffOrganizationList,
											targetOrganization);
							/**
							 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
							 */
							Class clazz[] = { StaffOrganization.class };
							log.endLog(logService, clazz,
									SysLogConstrants.MOVE,
									SysLogConstrants.INFO, "员工组织关系移动记录日志");
							onQueryStaffOrganization();
						}
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要操作的员工", "提示信息");
		}
	}

	/**
	 * 打开员工组织关系编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openStaffOrganizationEditWin() throws Exception {
		Map arg = new HashMap();
		arg.put("opType", opType);
		arg.put("isOrgTreePage", isOrgTreePage);
		arg.put("oldStaffOrganization", qryStaffOrganization);
		if ("mod".equals(opType)) {
			arg.put("updateStaffOrganization", updateStaffOrganization);
		}

		if (SffOrPtyCtants.ADD.equals(opType)) {
			arg.put("staff", staff); // add wangy 选定员工新增员工组织关系
			arg.put("isAgentTab", isAgentTab);// 传入代理商管理标记
			arg.put("isIbeTab", isIbeTab);// 传入内部经营实体管理标记
		}
		Window win = (Window) Executions.createComponents(
				"/pages/organization/staff_organization_edit.zul", this, arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				qryStaffOrganization = (StaffOrganization) event.getData();
				if (qryStaffOrganization != null) {
					// if (event.getData() != null) {
					setStaffOrganizationButtonValid(true, true, false, false,
							false, false, false);

					if (SffOrPtyCtants.ADD.equals(opType)) {
						// 新增员工组织关系后，用于显示该条记录
						bean.getStaffName().setValue(
								qryStaffOrganization.getStaffName());
						bean.getUserCode().setValue(
								qryStaffOrganization.getUserCode());
					}

					onQueryStaffOrganization();

					StaffOrganization staffOrganization = qryStaffOrganization
							.getStaffOrganization();

					if (staffOrganization != null) {

						Organization org = staffOrganization.getOrganization();
						Staff staff = staffOrganization.getStaff();

						OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
						organizationExtendAttr.setOrgId(org.getOrgId());
						organizationExtendAttr
								.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

						organizationExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(organizationExtendAttr);

						if (org != null
								&& staff != null
								&& ((organizationExtendAttr != null && !StrUtil
										.isEmpty(organizationExtendAttr
												.getOrgAttrValue())) || org
										.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null)) {
							if (!StrUtil.isNullOrEmpty(staff.getWorkProp())
									&& (staff.getWorkProp().startsWith(
											SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
											.getWorkProp()
											.startsWith(
													SffOrPtyCtants.WORKPROP_N_P_PRE))
									&& org.isUploadGroupMail()) {
								if (staff.getPartyRoleId() != null) {
									PartyRole partyRole = staffManager
											.getPartyRole(staff
													.getPartyRoleId());
									if (partyRole != null
											&& partyRole.getPartyId() != null) {
										Party party = staffManager
												.getParty(partyRole
														.getPartyId());
										PartyContactInfo partyContactInfo = partyManager
												.getDefaultPartyContactInfo(partyRole
														.getPartyId());

										Individual individual = partyManager
												.getIndividual(partyRole
														.getPartyId());

										party.setIndividual(individual);
										party.setStaff(staff);
										party.setStaffOrganization(staffOrganization);

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
																GroupMailConstant.GROUP_MAIL_BIZ_ID_2,
																party, org);

												if (!StrUtil.isEmpty(msg)) {
													if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_TRUE
															.equals(msg)) {
														msg = groupMailManager
																.groupMailPackageInfo(
																		GroupMailConstant.GROUP_MAIL_BIZ_ID_14,
																		party,
																		org);
													} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_FALSE
															.equals(msg)) {
														msg = groupMailManager
																.groupMailPackageInfo(
																		GroupMailConstant.GROUP_MAIL_BIZ_ID_13,
																		party,
																		org);
													}
													if (!StrUtil.isEmpty(msg)) {
														ZkUtil.showError(msg,
																"提示信息");
														return;
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
			}
		});
	}

	/**
	 * 打开员工组织关系编辑窗口，包括（员工、参与人）.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openStaffOrganizationEditAllWin() throws Exception {
		Map arg = new HashMap();
		arg.put("opType", opType);
		arg.put("variablePageLocation", variablePageLocation);
		arg.put("portletInfoProvider", portletInfoProvider);
		arg.put("isOrgTreePage", isOrgTreePage);
		arg.put("oldStaffOrganization", qryStaffOrganization);
		arg.put("isAgentTab", isAgentTab);// 传入代理商管理标记
		arg.put("isIbeTab", isIbeTab);// 传入内部经营实体管理标记
		arg.put("isPoliticalTab", isPoliticalTab);// 传入代理商管理标记
		if ("mod".equals(opType)) {
			arg.put("updateStaffOrganization", updateStaffOrganization);
		}
		// if ("add".equals(opType)) {
		// arg.put("isAgentTab", isAgentTab);// 传入代理商管理标记
		// }
		Window win = (Window) Executions.createComponents(
				"/pages/organization/staff_organization_edit_all.zul", this,
				arg);
		win.doModal();
		win.addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event event) throws Exception {
				qryStaffOrganization = (StaffOrganization) event.getData();
				if (qryStaffOrganization != null) {
					// if (event.getData() != null) {
					setStaffOrganizationButtonValid(true, true, false, false,
							false, false, false);

					if (SffOrPtyCtants.ADD.equals(opType)) {
						// 新增员工组织关系后，用于显示该条记录
						bean.getStaffName().setValue(
								qryStaffOrganization.getStaffName());
						bean.getUserCode().setValue(
								qryStaffOrganization.getUserCode());
					}

					onQueryStaffOrganization();

					StaffOrganization staffOrganization = qryStaffOrganization
							.getStaffOrganization();

					if (staffOrganization != null) {

						Organization org = staffOrganization.getOrganization();
						Staff staff = staffOrganization.getStaff();

						OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
						organizationExtendAttr.setOrgId(org.getOrgId());
						organizationExtendAttr
								.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

						organizationExtendAttr = organizationExtendAttrManager
								.queryOrganizationExtendAttr(organizationExtendAttr);

						if (org != null
								&& staff != null
								&& ((organizationExtendAttr != null && !StrUtil
										.isEmpty(organizationExtendAttr
												.getOrgAttrValue())) || org
										.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null)) {
							if (!StrUtil.isNullOrEmpty(staff.getWorkProp())
									&& (staff.getWorkProp().startsWith(
											SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
											.getWorkProp()
											.startsWith(
													SffOrPtyCtants.WORKPROP_N_P_PRE))
									&& org.isUploadGroupMail()) {
								if (staff.getPartyRoleId() != null) {
									PartyRole partyRole = staffManager
											.getPartyRole(staff
													.getPartyRoleId());
									if (partyRole != null
											&& partyRole.getPartyId() != null) {
										Party party = staffManager
												.getParty(partyRole
														.getPartyId());
										PartyContactInfo partyContactInfo = partyManager
												.getDefaultPartyContactInfo(partyRole
														.getPartyId());

										Individual individual = partyManager
												.getIndividual(partyRole
														.getPartyId());

										party.setIndividual(individual);
										party.setStaff(staff);
										party.setStaffOrganization(staffOrganization);

										if (party != null
												&& partyContactInfo != null
												&& !StrUtil
														.isEmpty(partyContactInfo
																.getGrpUnEmail())) {
											party.setPartyContactInfo(partyContactInfo);
											boolean groupMailInterFaceSwitch = UomClassProvider
													.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

											if (groupMailInterFaceSwitch) {
												if (SffOrPtyCtants.ADD
														.equals(opType)) {

													String msg = groupMailManager
															.groupMailPackageInfo(
																	GroupMailConstant.GROUP_MAIL_BIZ_ID_2,
																	party, org);

													if (!StrUtil.isEmpty(msg)) {
														if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_TRUE
																.equals(msg)) {
															ZkUtil.showError(
																	"该用户集团邮箱账号存在",
																	"集团邮箱提示信息");
															return;
														} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_FALSE
																.equals(msg)) {

															msg = groupMailManager
																	.groupMailPackageInfo(
																			GroupMailConstant.GROUP_MAIL_BIZ_ID_13,
																			party,
																			org);
															if (!StrUtil
																	.isEmpty(msg)) {
																ZkUtil.showError(
																		msg,
																		"集团邮箱提示信息");
																return;
															}

														}

													}

												} else if (SffOrPtyCtants.MOD
														.equals(opType)) {
													String msg = groupMailManager
															.groupMailPackageInfo(
																	GroupMailConstant.GROUP_MAIL_BIZ_ID_14,
																	party, org);
													if (!StrUtil.isEmpty(msg)) {
														ZkUtil.showError(msg,
																"集团邮箱提示信息");
														return;
													}
												}
											}
										}
									}
								}

							}

						}

					}
					// }
				}
			}
		});
	}

	/**
	 * 删除员工组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffOrganizationDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		if (staffOrganizationList != null && staffOrganizationList.size() > 0) {
			int selCount = staffOrganizationList.size();
			if (selCount > 1) {
				ZkUtil.showError("每次只能删除一个员工组织关系", "提示信息");
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
						final StaffOrganization delSo = staffOrganizationList
								.get(0);
						delSo.setReason(reason);
						StaffOrganization queryStaffOrganization = new StaffOrganization();
						queryStaffOrganization.setStaffId(delSo.getStaffId());
						List<StaffOrganization> listStaffOrganization = staffOrganizationManager
								.queryStaffOrganizationList(queryStaffOrganization);
						Staff queryStaff = queryStaffOrganization.getStaff();
						queryStaff.setReason(reason);
						if (listStaffOrganization != null
								&& listStaffOrganization.size() == 1) {
							// 如果只有一条直接删除
							// staffManager.delStaff(queryStaffOrganization.getStaff());
							// 获取对应组织的类型
							Organization targetOrganization = listStaffOrganization
									.get(0).getOrganization();
							List<OrgType> orgTypeList = targetOrganization
									.getOrgTypeList();
							boolean isIntNetwork = false;// 内部网点
							boolean isExtNetwork = false;// 外部网点

							// 判断该组织是内部网点或外部网点
							if (orgTypeList != null && orgTypeList.size() > 0) {
								for (OrgType orgType : orgTypeList) {

									if (orgType
											.getOrgTypeCd()
											.equals(OrganizationConstant.ORG_TYPE_N0202010000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202030000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202040000)) {
										isIntNetwork = true;// 该组织是内部网点【目前不做区分内外部网点】
									}

									if (orgType
											.getOrgTypeCd()
											.equals(OrganizationConstant.ORG_TYPE_N0202020000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202050000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202060000)) {
										isExtNetwork = true;// 该组织是外部网点【目前不做区分内外部网点】
									}

								}
							}

							// 判断是网点还是其他，如果是网点就直接删人，如果不是网点就只删关系
							if (isIntNetwork) {
								// 内部网点,全删
								staffManager.delStaff(queryStaff);
							} else if (isExtNetwork) {
								// 内部网点,全删
								staffManager.delStaff(queryStaff);
							} else {
								staffOrganizationManager
										.removeStaffOrganization(delSo);
							}

							cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(
									bean.getStaffOrganizationListBox(), delSo,
									"del");
						} else if (null != listStaffOrganization
								&& listStaffOrganization.size() == 2) {
							// 如果删除后只剩下一条其他关系，则将其设置为归属关系（先删除旧的兼职关系新增一条新的归属关系）
							StaffOrganization needUpdateStaffOrganization = null;
							for (StaffOrganization temp : listStaffOrganization) {
								if (temp.getStaffOrgId() != null
										&& delSo.getStaffOrgId() != null
										&& !temp.getStaffOrgId().equals(
												delSo.getStaffOrgId())) {
									needUpdateStaffOrganization = temp;
								}
							}
							if (needUpdateStaffOrganization != null) {
								delSo.setRemoveNeedUpdateStaffOrganization(needUpdateStaffOrganization);
							}
							staffOrganizationManager
									.removeStaffOrganization(delSo);
							onQueryStaffOrganization();
						} else if (null != listStaffOrganization
								&& listStaffOrganization.size() > 2) {
							boolean isExistRala_CD_1 = false;
							for (StaffOrganization so : listStaffOrganization) {
								// 除本身外还存在直属关系
								if (BaseUnitConstants.RALA_CD_1.equals(so
										.getRalaCd())
										&& !so.getStaffOrgId().equals(
												delSo.getStaffOrgId())) {
									isExistRala_CD_1 = true;
									break;
								}
							}
							// 如果剩下不止一条关系且不存在归属关系，则弹出对话框让用户设置一条记录为归属关系
							if (!isExistRala_CD_1) {
								// 不存在归属，弹框
								try {
									Map arg = new HashMap();
									arg.put("oldStaffOrganization", delSo);
									Window win = (Window) Executions
											.createComponents(
													"/pages/organization/staff_organization_relation_listbox.zul",
													StaffOrganizationListboxComposer.this,
													arg);
									win.doModal();
									win.addEventListener(Events.ON_OK,
											new EventListener() {
												@Override
												public void onEvent(Event event)
														throws Exception {
													if (null != event.getData()) {
														String batchNumber = OperateLog
																.gennerateBatchNumber();
														StaffOrganization chooseSo = (StaffOrganization) event
																.getData();
														if (chooseSo != null) {
															delSo.setRemoveNeedUpdateStaffOrganization(chooseSo);
															staffOrganizationManager
																	.removeStaffOrganization(delSo);
															staffOrganizationList = null;
															onQueryStaffOrganization();
														}
													}
												}
											});
								} catch (SuspendNotAllowedException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} else {
								staffOrganizationManager
										.removeStaffOrganization(delSo);

								staffOrganizationList = null;
								cn.ffcs.uom.common.zul.PubUtil
										.reDisplayListbox(bean
												.getStaffOrganizationListBox(),
												delSo, "del");
							}
						}
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要操作的员工", "提示信息");
		}
	}

	/**
	 * 设置员工组织关系按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canAddAll
	 *            新增组织按钮
	 * @param canEdit
	 *            修改按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canMove
	 *            移动按钮
	 * @param canView
	 *            查看组织按钮
	 * @param canUpdateAll
	 *            修改组织按钮
	 */
	private void setStaffOrganizationButtonValid(final Boolean canAdd,
			final Boolean canAddAll, final Boolean canEdit,
			final Boolean canDelete, final Boolean canMove,
			final Boolean canView, final Boolean canUpdateAll) {
		/**
		 * 推导树默认不让编辑且不然修改
		 */
		if (isDuceTree) {
			return;
		}
		this.bean.getAddStaffOrganizationButton().setDisabled(!canAdd);
		this.bean.getAddStaffOrganizationButtonAll().setDisabled(!canAddAll);
		this.bean.getUpdateStaffOrganizationButton().setDisabled(!canEdit);
		this.bean.getDelStaffOrganizationButton().setDisabled(!canDelete);
		this.bean.getMoveStaffOrganizationButton().setDisabled(!canMove);
		this.bean.getViewStaffOrganizationButton().setDisabled(!canView);
		this.bean.getUpdateStaffOrganizationButtonAll().setDisabled(
				!canUpdateAll);
	}

	/**
	 * 查询员工组织关系.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onQueryStaffOrganization() throws Exception {
		qryStaff = new Staff();
		qryStaff.setStaffName(this.bean.getStaffName().getValue());
		qryStaff.setStaffAccount(this.bean.getStaffAccount().getValue());

		if (this.qryStaffOrganization != null) {
			if (qryStaffOrganization.getOrganization() != null
					&& qryStaffOrganization.getOrganization().getOrgCode() != null) {
				if (organizationManager.getAgentByOrgCode(qryStaffOrganization
						.getOrganization().getOrgCode()) != null
						|| organizationManager
								.getIbeByOrgCode(qryStaffOrganization
										.getOrganization().getOrgCode()) != null) {// 代理商或内部经营实体
					setStaffOrganizationButtonValid(false, false, false, false,
							false, false, false);
				} else {// 非代理商且非内部经营实体
					setStaffOrganizationButtonValid(true, true, false, false,
							false, false, false);
				}
			} else {
				setStaffOrganizationButtonValid(true, true, false, false,
						false, false, false);
			}
			qryStaffOrganization
					.setUserCode(this.bean.getUserCode().getValue());
			if (!isDuceTree) {
				ListboxUtils.clearListbox(bean.getStaffOrganizationListBox());
				PageInfo pageInfo = staffOrganizationManager
						.queryPageInfoByStaffOrganization(qryStaff,
								qryStaffOrganization, this.bean
										.getStaffOrganizationListPaging()
										.getActivePage() + 1, this.bean
										.getStaffOrganizationListPaging()
										.getPageSize());
				ListModel dataList = new BindingListModelList(
						pageInfo.getDataList(), true);
				this.bean.getStaffOrganizationListBox().setModel(dataList);
				this.bean.getStaffOrganizationListPaging().setTotalSize(
						NumericUtil.nullToZero(pageInfo.getTotalCount()));
			} else {
				treeCalcAction treeCalcVo = this.qryStaffOrganization
						.getTreeCalcVo();
				if (treeCalcVo != null) {
					Map<Long, Set<StaffOrganization>> map = treeCalcVo
							.statisticStaff();
					if (map != null && qryStaffOrganization.getOrgId() != null) {
						Set<StaffOrganization> set = map
								.get(qryStaffOrganization.getOrgId());
						ArrayList<StaffOrganization> list = new ArrayList<StaffOrganization>(
								set);
						ListModel dataList = new BindingListModelList(list,
								true);
						this.bean.getStaffOrganizationListBox().setModel(
								dataList);
					}
				}
			}
		} else {
			/**
			 * 组织树未选择组织添加清理操作
			 */
			ListboxUtils.clearListbox(bean.getStaffOrganizationListBox());
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryOrganizationStaff() throws Exception {
		this.bean.getStaffOrganizationListPaging().setActivePage(0);
		this.onQueryStaffOrganization();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetStaffOrganization() throws Exception {

		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");
		this.bean.getUserCode().setValue("");

	}

	/**
	 * 控件初始化 .
	 * 
	 * @throws Exception
	 *             异常
	 * @author
	 */
	public void init() throws Exception {
		if (this.getStaff() != null) {
			if (null == qryStaffOrganization) {
				qryStaffOrganization = new StaffOrganization();
			}
			qryStaffOrganization.setStaffId(staff.getStaffId());
			onQueryStaffOrganization();
		}
	}

	/**
	 * 清理 .
	 * 
	 * @param event
	 * @author wangyong 2013-6-11 wangyong
	 */
	public void onCleanStaffOrgRespons(final ForwardEvent event) {
		this.bean.getStaffName().setValue("");
		this.bean.getUserCode().setValue("");
		qryStaffOrganization = null;
		staff = null;
		ListboxUtils.clearListbox(bean.getStaffOrganizationListBox());
	}

	/**
	 * 员工管理模块，员工选择后的组织列表展示
	 * 
	 * @param event
	 */
	public void onStaffOrgQueryResponse(final ForwardEvent event) {
		try {
			staff = (Staff) event.getOrigin().getData();
			if (null != staff && !StrUtil.isNullOrEmpty(staff.getStaffId())) {
				qryStaffOrganization = new StaffOrganization();
				qryStaffOrganization.setStaffId(staff.getStaffId());
				setStaffOrganizationButtonValid(true, true, true, true, true,
						false, true);
				this.bean.getStaffName().setValue("");
				this.bean.getUserCode().setValue("");
				onQueryStaffOrganization();
			}
			this.bean.getAddStaffOrganizationButtonAll().setVisible(false);
			this.bean.getViewStaffOrganizationButton().setVisible(false);
			this.bean.getUpdateStaffOrganizationButtonAll().setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canAddAll = false;
		boolean canUpdate = false;
		boolean canUpdateAll = false;
		boolean canDel = false;
		boolean canMove = false;
		boolean canView = false;
		boolean canBatch = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canAddAll = true;
			canUpdate = true;
			canUpdateAll = true;
			canDel = true;
			canMove = true;
			canView = true;
			canBatch = true;
		} else if ("orgTreePage".equals(page)) {

			this.bean.getStaffAccountDiv().setVisible(true);

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_ADDALL)) {
				canAddAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_UPDATEALL)) {
				canUpdateAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_BATCH)) {
				canBatch = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_MOVE)) {
				canMove = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ORG_TREE_STAFF_ORG_VIEW)) {
				canView = true;
			}
		} else if ("organizationPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_ADDALL)) {
				canAddAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_UPDATEALL)) {
				canUpdateAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_MOVE)) {
				canMove = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_VIEW)) {
				canView = true;
			}
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_ADDALL)) {
				canAddAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_UPDATEALL)) {
				canUpdateAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_DEL)) {
				canDel = true;
			}
			// 判断批量导入员工组织关系
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_BATCH)) {
				canBatch = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_MOVE)) {
				canMove = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_VIEW)) {
				canView = true;
			}
		} else if ("marketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_DEL)) {
					canDel = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_MOVE)) {
					canMove = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_VIEW)) {
					canView = true;
				}

			}

		} else if ("newMarketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0403);

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_DEL)) {
					canDel = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_MOVE)) {
					canMove = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_VIEW)) {
					canView = true;
				}

			}

		} else if ("newSeventeenMarketingUnitPage".equals(page)) {

			aroleOrganizationLevel = new AroleOrganizationLevel();
			aroleOrganizationLevel
					.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
			aroleOrganizationLevel
					.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);

			if (!StrUtil.isNullOrEmpty(organization)
					&& aroleOrganizationLevelManager
							.aroleOrganizationLevelValid(
									aroleOrganizationLevel, organization)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_DEL)) {
					canDel = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_MOVE)) {
					canMove = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.MARKETING_UNIT_STAFF_ORG_VIEW)) {
					canView = true;
				}

			}

		} else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_ADDALL)) {
				canAddAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_UPDATEALL)) {
				canUpdateAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_MOVE)) {
				canMove = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_VIEW)) {
				canView = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_ADDALL)) {
				canAddAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_UPDATEALL)) {
				canUpdateAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_MOVE)) {
				canMove = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_VIEW)) {
				canView = true;
			}
		} else if ("costUnitPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_STAFF_ORG_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_STAFF_ORG_ADDALL)) {
				canAddAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_STAFF_ORG_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_STAFF_ORG_UPDATEALL)) {
				canUpdateAll = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_STAFF_ORG_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_STAFF_ORG_MOVE)) {
				canMove = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_UNIT_STAFF_ORG_VIEW)) {
				canView = true;
			}
		}

		this.bean.getAddStaffOrganizationButton().setVisible(canAdd);
		this.bean.getAddStaffOrganizationButtonAll().setVisible(canAddAll);
		this.bean.getUpdateStaffOrganizationButton().setVisible(canUpdate);
		this.bean.getUpdateStaffOrganizationButtonAll()
				.setVisible(canUpdateAll);
		this.bean.getDelStaffOrganizationButton().setVisible(canDel);
		this.bean.getMoveStaffOrganizationButton().setVisible(canMove);
		this.bean.getViewStaffOrganizationButton().setVisible(canView);
		this.bean.getImportStaffOrganizationButton().setVisible(canBatch);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canAddAll = false;
		boolean canUpdate = false;
		boolean canUpdateAll = false;
		boolean canDel = false;
		boolean canMove = false;
		boolean canView = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;
		variablePageLocation = orgTreeTabName;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canAddAll = true;
			canUpdate = true;
			canUpdateAll = true;
			canDel = true;
			canMove = true;
			canView = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("politicalTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_MOVE)) {
					canMove = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_POLITICAL_STAFF_ORG_VIEW)) {
					canView = true;
				}
			} else if ("agentTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_MOVE)) {
					canMove = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_AGENT_STAFF_ORG_VIEW)) {
					canView = true;
				}
			} else if ("ibeTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_MOVE)) {
					canMove = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_IBE_STAFF_ORG_VIEW)) {
					canView = true;
				}
			} else if ("supplierTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_MOVE)) {
					canMove = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_SUPPLIER_STAFF_ORG_VIEW)) {
					canView = true;
				}
			} else if ("ossTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_MOVE)) {
					canMove = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_OSS_STAFF_ORG_VIEW)) {
					canView = true;
				}
			} else if ("edwTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_MOVE)) {
					canMove = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_EDW_STAFF_ORG_VIEW)) {
					canView = true;
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
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL)) {
						canAddAll = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_UPDATEALL)) {
						canUpdateAll = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_DEL)) {
						canDel = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_MOVE)) {
						canMove = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_VIEW)) {
						canView = true;
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
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL)) {
						canAddAll = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_UPDATEALL)) {
						canUpdateAll = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_DEL)) {
						canDel = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_MOVE)) {
						canMove = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_VIEW)) {
						canView = true;
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
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_ADDALL)) {
						canAddAll = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_UPDATE)) {
						canUpdate = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_UPDATEALL)) {
						canUpdateAll = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_DEL)) {
						canDel = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_MOVE)) {
						canMove = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_VIEW)) {
						canView = true;
					}

				}

			} else if ("costTab".equals(orgTreeTabName)) {
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_ADD)) {
					canAdd = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_ADDALL)) {
					canAddAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_UPDATE)) {
					canUpdate = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_UPDATEALL)) {
					canUpdateAll = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_DEL)) {
					canDel = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_MOVE)) {
					canMove = true;
				}
				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_COST_STAFF_ORG_VIEW)) {
					canView = true;
				}
			}
		}

		this.bean.getAddStaffOrganizationButton().setVisible(canAdd);
		this.bean.getAddStaffOrganizationButtonAll().setVisible(canAddAll);
		this.bean.getUpdateStaffOrganizationButton().setVisible(canUpdate);
		this.bean.getUpdateStaffOrganizationButtonAll()
				.setVisible(canUpdateAll);
		this.bean.getDelStaffOrganizationButton().setVisible(canDel);
		this.bean.getMoveStaffOrganizationButton().setVisible(canMove);
		this.bean.getViewStaffOrganizationButton().setVisible(canView);
	}
}
