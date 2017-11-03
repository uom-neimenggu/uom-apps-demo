package cn.ffcs.uom.group.component;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.component.bean.GroupListboxExtBean;
import cn.ffcs.uom.group.manager.GroupManager;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

/**
 * 集团数据管理.
 * 
 * @author Zhu Lintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class GroupListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private GroupListboxExtBean bean = new GroupListboxExtBean();

	private GroupManager groupManager = (GroupManager) ApplicationContextUtil
			.getBean("groupManager");

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	/**
	 * 选中的集团数据
	 */
	private Group group;
	/**
	 * 查询集团数据
	 */
	private Group queryGroup;
	/**
	 * 选中的员工
	 */
	private Staff staff;
	/**
	 * 查询员工账号
	 */
	private StaffAccount queryStaffAccount;

	@Getter
	@Setter
	private String variablePagePosition;

	List<Group> oldGroupList;
	List<StaffAccount> oldStaffAccountList;
	List<StaffAccount> newStaffAccountList;
	List<StaffAccount> repeatStaffAccountList;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public GroupListboxExt() {
		Executions.createComponents("/pages/group/comp/group_listbox_ext.zul",
				this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_GROUP_QUERY, this,
				"onSelectStaffResponse");
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.setGroupButtonValid(false, false);
		// 去除首次加载
		// onGroupRequest();
		if (variablePagePosition.equals("groupPage")) {
			this.bean.getProofreadButton().setLabel("校正");
		} else {
			this.bean.getProofreadButton().setLabel("关联");
		}
	}

	/**
	 * 获取选择的员工
	 */
	public void onSelectStaffResponse(ForwardEvent event) throws Exception {
		staff = (Staff) event.getOrigin().getData();
	}

	/**
	 * 点击查询
	 * 
	 * @throws Exception
	 */
	public void onGroupRequest() throws Exception {

		this.bean.getGroupListPaging().setActivePage(0);

		this.queryGroup = new Group();

		if (!StrUtil.isEmpty(this.bean.getUserName().getValue())) {
			this.queryGroup.setUserName(this.bean.getUserName().getValue());
		}

		if (!StrUtil.isEmpty(this.bean.getCtHrUserCode().getValue())) {
			this.queryGroup.setCtHrUserCode(this.bean.getCtHrUserCode()
					.getValue());
		}

		if (!StrUtil.isEmpty(this.bean.getLoginName().getValue())) {
			this.queryGroup.setLoginName(this.bean.getLoginName().getValue());
		}

		if (!StrUtil.isEmpty(this.bean.getCtIdentityNumber().getValue())) {
			this.queryGroup.setCtIdentityNumber(this.bean.getCtIdentityNumber()
					.getValue());
		}

		// if (!StrUtil.isNullOrEmpty(this.bean.getCtStatus().getSelectedItem())
		// && !StrUtil.isNullOrEmpty(this.bean.getCtStatus()
		// .getSelectedItem().getValue())) {
		// this.queryGroup.setCtStatus((String) this.bean.getCtStatus()
		// .getSelectedItem().getValue());
		// }

		this.onGroupListboxPaging();
	}

	/**
	 * .重置查询内容 .
	 */
	public void onGroupReset() {
		bean.getUserName().setValue(null);
		bean.getCtHrUserCode().setValue(null);
		bean.getLoginName().setValue(null);
		bean.getCtIdentityNumber().setValue(null);
		// bean.getCtStatus().getSelectedItem().setValue(null);
	}

	/**
	 * 分页查询
	 * 
	 * @throws Exception
	 */
	public void onGroupListboxPaging() throws Exception {
		PageInfo pageInfo = this.groupManager.queryPageInfoByGroup(queryGroup,
				this.bean.getGroupListPaging().getActivePage() + 1, this.bean
						.getGroupListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getGroupListBox().setModel(dataList);
		this.bean.getGroupListPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 省内工号校正和GUID关联.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGroupProofread() throws Exception {
		String alterText = "";
		if (variablePagePosition.equals("groupPage")) {
			alterText = "确定要校对数据吗?";
		} else {
			queryStaffAccount = new StaffAccount();
			queryStaffAccount.setGuid(group.getLoginName());
			oldStaffAccountList = staffManager.queryStaffAccountList(queryStaffAccount);
			if (oldStaffAccountList != null
				&& oldStaffAccountList.size() > 0) {
				String accountStr = "";
				for (int i = 0; i < oldStaffAccountList.size(); i++) {
					accountStr = accountStr + oldStaffAccountList.get(i).getStaffAccount();
					if (i!= (oldStaffAccountList.size()-1)) {
						accountStr += ",";
					}
				}
				alterText = "该HR工号已经与省内工号"+accountStr+"关联,确定要强制关联HR工号到新的省内工号?";
			}else{
				alterText = "确定要关联省主数据和HR工号?";
			}
			
		}
		
		ZkUtil.showQuestion(alterText, "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {

					if (!StrUtil.isNullOrEmpty(staff)
							&& !StrUtil.isNullOrEmpty(group)) {

						if (staff.getStaffId() != null) {

							if (!StrUtil.isEmpty(variablePagePosition)) {

								if (variablePagePosition.equals("groupPage")) {

									if (!staff.getStaffName().equals(
											group.getUserName())) {

										ZkUtil.showInformation(
												"员工姓名与人力姓名信息不匹配，请修改后较正!",
												"提示信息");
										return;

									}

									PartyCertification pc = new PartyCertification();
									pc.setCertType(SffOrPtyCtants.CERT_TYPE_ID_CARD);
									pc.setCertSort(SffOrPtyCtants.CERT_SORT_1);

									List<PartyCertification> pcList = partyManager
											.queryPartyCertificationList(staff,
													pc);

									if (null != pcList && pcList.size() > 0) {

										pc = pcList.get(0);

										if (!StrUtil.isEmpty(pc.getCertNumber())) {

											if (!pc.getCertNumber()
													.equals(group
															.getCtIdentityNumber())) {

												if (pc.getCertNumber().length() == 15) {
													if (!(IdcardValidator
															.convertIdcarBy15bit(pc
																	.getCertNumber()).equals(group
															.getCtIdentityNumber()))) {
														ZkUtil.showInformation(
																"员工身份证与人力身份证信息不匹配，请修改后较正!",
																"提示信息");
														return;
													}
												} else {
													ZkUtil.showInformation(
															"员工身份证与人力身份证信息不匹配，请修改后较正!",
															"提示信息");
													return;
												}

											}

										} else {
											ZkUtil.showInformation(
													"员工身份证信息为空，请添加员工身份证信息后较正!",
													"提示信息");
											return;
										}

									} else {

										ZkUtil.showInformation(
												"查询不到员工身份证信息，请添加员工身份证信息后较正!",
												"提示信息");
										return;
									}

								}

								if (!StrUtil.isEmpty(group.getLoginName())) {

									String staffNbr = group.getCtHrUserCode();
									String staffAccount = null;

									if (variablePagePosition
											.equals("groupPage")) {

										if (!StrUtil.isNullOrEmpty(staffNbr)) {

											staffNbr = staffNbr.trim();
											queryStaffAccount = new StaffAccount();
											queryStaffAccount
													.setStaffAccount(staffNbr);

											if (staffNbr.startsWith("34")) {

												if (staffNbr.length() < 2) {
													Messagebox
															.show("从人力中间表获取的工号长度小于2位。");
													return;
												}

												String staffAccStr = staffNbr
														.substring(2, staffNbr
																.length());

												queryStaffAccount
														.setStaffAccount(staffAccStr);

											} else if (staffNbr
													.startsWith("W34")
													|| staffNbr
															.startsWith("w34")) {

												if (staffNbr.length() < 3) {
													Messagebox
															.show("从人力中间表获取的工号长度小于3位。");
													return;
												}

												String staffAccStr = "W9"
														+ staffNbr
																.substring(
																		3,
																		staffNbr.length());

												queryStaffAccount
														.setStaffAccount(staffAccStr);

											}

											repeatStaffAccountList = staffManager
													.queryStaffAccountListByStaffAccount(queryStaffAccount);

											if (repeatStaffAccountList != null
													&& repeatStaffAccountList
															.size() > 0) {

												for (StaffAccount oldStaffAccount : repeatStaffAccountList) {

													if (!staff
															.getStaffId()
															.equals(oldStaffAccount
																	.getStaffId())) {

														ZkUtil.showInformation(
																"该员工账号已经被使用，请修正后再操作!",
																"提示信息");
														return;
													}

												}
											}

											staffAccount = queryStaffAccount
													.getStaffAccount();

										} else {
											ZkUtil.showInformation(
													"集团用户人力号为空，请重新选择集团数据!",
													"提示信息");
											return;
										}
									}

									if (variablePagePosition
											.equals("groupGuidPage")) {

										queryGroup = new Group();

										//queryStaffAccount = new StaffAccount();

										oldGroupList = new ArrayList<Group>();

										oldStaffAccountList = new ArrayList<StaffAccount>();

										queryGroup.setReserv_Col2(staff
												.getStaffId().toString());
										oldGroupList = groupManager
												.queryGroupList(queryGroup);

										if (oldGroupList != null
												&& oldGroupList.size() > 0) {
											groupManager
													.updateGroupIsNull(queryGroup);// 删除旧的集团员工对应关系
										}

/*										queryStaffAccount.setGuid(group
												.getLoginName());
										oldStaffAccountList = staffManager
												.queryStaffAccountList(queryStaffAccount);*/

										if (oldStaffAccountList != null
												&& oldStaffAccountList.size() > 0) {
											for (StaffAccount oldStaffAccount : oldStaffAccountList) {// 删除旧的集团员工账号对应关系
												oldStaffAccount.setGuid("");
												oldStaffAccount.update();
											}
										}

										group.setReserv_Col2(staff.getStaffId()
												.toString());
										groupManager.updateGroup(group);// 创建新的集团员工对应关系
									}

									newStaffAccountList = new ArrayList<StaffAccount>();
									queryStaffAccount = new StaffAccount();
									queryStaffAccount.setStaffId(staff
											.getStaffId());
									newStaffAccountList = staffManager
											.queryStaffAccountList(queryStaffAccount);

									if (newStaffAccountList != null
											&& newStaffAccountList.size() > 0) {

										for (StaffAccount newStaffAccount : newStaffAccountList) {
											if (variablePagePosition
													.equals("groupPage")) {
												newStaffAccount
														.setStaffAccount(staffAccount);
											} else if (variablePagePosition
													.equals("groupGuidPage")) {
												newStaffAccount.setGuid(group
														.getLoginName());
											}
											newStaffAccount.update();// 更新员工账号
										}
									}

									if (variablePagePosition
											.equals("groupPage")) {
										staff.setStaffNbr(staffNbr);
										staff.update();// 更新员工人力号
									}

								} else {
									ZkUtil.showInformation(
											"集团用户帐号为空，请重新选择集团数据!", "提示信息");
									return;
								}

							}
						} else {

							ZkUtil.showInformation("主数据员工ID为空，请重新选择主数据员工!",
									"提示信息");
							return;

						}

						bean.getUserName().setValue(group.getUserName());
						bean.getCtHrUserCode()
								.setValue(group.getCtHrUserCode());
						bean.getLoginName().setValue(group.getLoginName());
						bean.getCtIdentityNumber().setValue(
								group.getCtIdentityNumber());

						setGroupButtonValid(false, false);
						onGroupRequest();// 查询
						ZkUtil.showInformation("数据较正成功!", "提示信息");
					} else {
						ZkUtil.showError("请选择主数据员工或集团数据!\n", "提示信息");
						return;
					}
				}

			}
		});

	}

	/**
	 * 本地网专用GUID关联-添加姓名加身份证校验.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGroupLocalProofread() throws Exception {

		ZkUtil.showQuestion("确定要校对数据吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {

					if (!StrUtil.isNullOrEmpty(staff)
							&& !StrUtil.isNullOrEmpty(group)) {

						if (staff.getStaffId() != null) {

							if (!StrUtil.isEmpty(variablePagePosition)
									&& variablePagePosition
											.equals("groupGuidPage")) {

								if (!staff.getStaffName().equals(
										group.getUserName())) {

									ZkUtil.showInformation(
											"员工姓名与人力姓名信息不匹配，请修改后较正!", "提示信息");
									return;

								}

								PartyCertification pc = new PartyCertification();
								pc.setCertType(SffOrPtyCtants.CERT_TYPE_ID_CARD);
								pc.setCertSort(SffOrPtyCtants.CERT_SORT_1);

								List<PartyCertification> pcList = partyManager
										.queryPartyCertificationList(staff, pc);

								if (null != pcList && pcList.size() > 0) {

									pc = pcList.get(0);

									if (!StrUtil.isEmpty(pc.getCertNumber())) {

										if (!pc.getCertNumber().equals(
												group.getCtIdentityNumber())) {

											if (pc.getCertNumber().length() == 15) {
												if (!(IdcardValidator.convertIdcarBy15bit(pc
														.getCertNumber()).equals(group
														.getCtIdentityNumber()))) {
													ZkUtil.showInformation(
															"员工身份证与人力身份证信息不匹配，请修改后较正!",
															"提示信息");
													return;
												}
											} else {
												ZkUtil.showInformation(
														"员工身份证与人力身份证信息不匹配，请修改后较正!",
														"提示信息");
												return;
											}

										}

									} else {
										ZkUtil.showInformation(
												"员工身份证信息为空，请添加员工身份证信息后较正!",
												"提示信息");
										return;
									}

								} else {

									ZkUtil.showInformation(
											"查询不到员工身份证信息，请添加员工身份证信息后较正!",
											"提示信息");
									return;
								}

								if (!StrUtil.isEmpty(group.getLoginName())) {

									queryGroup = new Group();

									queryStaffAccount = new StaffAccount();

									oldGroupList = new ArrayList<Group>();

									oldStaffAccountList = new ArrayList<StaffAccount>();

									queryGroup.setReserv_Col2(staff
											.getStaffId().toString());
									oldGroupList = groupManager
											.queryGroupList(queryGroup);

									if (oldGroupList != null
											&& oldGroupList.size() > 0) {
										groupManager
												.updateGroupIsNull(queryGroup);// 删除旧的集团员工对应关系
									}

									queryStaffAccount.setGuid(group
											.getLoginName());
									oldStaffAccountList = staffManager
											.queryStaffAccountList(queryStaffAccount);

									if (oldStaffAccountList != null
											&& oldStaffAccountList.size() > 0) {
										for (StaffAccount oldStaffAccount : oldStaffAccountList) {// 删除旧的集团员工账号对应关系
											oldStaffAccount.setGuid("");
											oldStaffAccount.update();
										}
									}

									group.setReserv_Col2(staff.getStaffId()
											.toString());
									groupManager.updateGroup(group);// 创建新的集团员工对应关系

									newStaffAccountList = new ArrayList<StaffAccount>();
									queryStaffAccount = new StaffAccount();
									queryStaffAccount.setStaffId(staff
											.getStaffId());
									newStaffAccountList = staffManager
											.queryStaffAccountList(queryStaffAccount);

									if (newStaffAccountList != null
											&& newStaffAccountList.size() > 0) {
										for (StaffAccount newStaffAccount : newStaffAccountList) {
											newStaffAccount.setGuid(group
													.getLoginName());
											newStaffAccount.update();// 更新员工账号
										}
									}

								} else {
									ZkUtil.showInformation(
											"集团用户帐号为空，请重新选择集团数据!", "提示信息");
									return;
								}

							}
						} else {

							ZkUtil.showInformation("主数据员工ID为空，请重新选择主数据员工!",
									"提示信息");
							return;

						}

						bean.getUserName().setValue(group.getUserName());
						bean.getCtHrUserCode()
								.setValue(group.getCtHrUserCode());
						bean.getLoginName().setValue(group.getLoginName());
						bean.getCtIdentityNumber().setValue(
								group.getCtIdentityNumber());

						setGroupButtonValid(false, false);
						onGroupRequest();// 查询
						ZkUtil.showInformation("数据较正成功!", "提示信息");
					} else {
						ZkUtil.showError("请选择主数据员工或集团数据!\n", "提示信息");
						return;
					}
				}

			}
		});

	}

	/**
	 * 集团数据选中请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectonGroupRequest() throws Exception {
		if (this.bean.getGroupListBox().getSelectedIndex() != -1) {
			group = (Group) bean.getGroupListBox().getSelectedItem().getValue();
			this.setGroupButtonValid(true, true);
		}
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canProofread
	 *            校对按钮
	 */
	public void setGroupButtonValid(final Boolean canProofread,
			final Boolean canLocalProofread) {
		this.bean.getProofreadButton().setDisabled(!canProofread);
		this.bean.getLocalProofreadButton().setDisabled(!canLocalProofread);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canProofread = false;
		boolean canLocalProofread = false;

		if (PlatformUtil.isAdmin()) {
			canProofread = true;
		} else if ("groupPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GROUP_PROOFREAD)) {
				canProofread = true;
			}
		} else if ("groupGuidPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GROUP_PROOFREAD)) {
				canProofread = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GROUP_LOCAL_PROOFREAD)) {
				canLocalProofread = true;
			}
		}
		this.bean.getProofreadButton().setVisible(canProofread);
		this.bean.getLocalProofreadButton().setVisible(canLocalProofread);
	}

}
