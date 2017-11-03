package cn.ffcs.uom.restservices.component;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.restservices.component.bean.GrpStaffListboxExtBean;
import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.GrpStaff;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

/**
 * 组织管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class GrpStaffListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	@Getter
	private GrpStaffListboxExtBean bean = new GrpStaffListboxExtBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("channelInfoManager")
	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/restservices/comp/grp_staff_listbox_ext.zul";

	/**
	 * 当前选择的grpStaff
	 */
	private GrpStaff grpStaff;

	/**
	 * 查询queryGrpStaff.
	 */
	private GrpStaff queryGrpStaff;

	/**
	 * 选中的员工
	 */
	private Staff staff;

	/**
	 * 查询员工账号
	 */
	private StaffAccount queryStaffAccount;

	List<StaffAccount> newStaffAccountList;

	List<StaffAccount> repeatStaffAccountList;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 是否是绑定框【默认非绑定框】
	 */
	@Getter
	@Setter
	private Boolean isBandbox = false;

	public GrpStaffListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_GRP_STAFF_QUERY, this,
				"onSelectStaffResponse");
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {
		this.setGroupButtonValid(false);
	}

	/**
	 * 获取选择的员工
	 */
	public void onSelectStaffResponse(ForwardEvent event) throws Exception {
		staff = (Staff) event.getOrigin().getData();
	}

	/**
	 * 查询组织列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onQueryGrpStaff() throws Exception {
		queryGrpStaff = new GrpStaff();
		PubUtil.fillPoFromBean(bean, queryGrpStaff);
		this.bean.getGrpStaffListPaging().setActivePage(0);
		this.queryGrpStaff();
	}

	/**
	 * 组织选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGrpStaffSelect() throws Exception {
		if (bean.getGrpStaffListBox().getSelectedCount() > 0) {
			grpStaff = (GrpStaff) bean.getGrpStaffListBox().getSelectedItem()
					.getValue();
			this.setGroupButtonValid(true);
			/**
			 * 抛出组织选择事件
			 */
			Events.postEvent(ChannelInfoConstant.ON_SELECT_GRP_STAFF, this,
					grpStaff);
		}
	}

	/**
	 * 集团渠道工号校对.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onGroupProofread() throws Exception {

		ZkUtil.showQuestion("确定要校对数据吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {

					if (!StrUtil.isNullOrEmpty(staff)
							&& !StrUtil.isNullOrEmpty(grpStaff)) {

						if (staff.getStaffId() != null) {

							if (!staff.getStaffName().equals(
									grpStaff.getStaffName())) {

								ZkUtil.showInformation(
										"员工姓名与集团渠道姓名信息不匹配，请修改后较正!", "提示信息");
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
											grpStaff.getCertNumber())) {

										if (pc.getCertNumber().length() == 15) {
											if (!(IdcardValidator
													.convertIdcarBy15bit(pc
															.getCertNumber())
													.equals(grpStaff
															.getCertNumber()))) {
												ZkUtil.showInformation(
														"员工身份证与集团渠道身份证信息不匹配，请修改后较正!",
														"提示信息");
												return;
											}
										} else {
											ZkUtil.showInformation(
													"员工身份证与集团渠道身份证信息不匹配，请修改后较正!",
													"提示信息");
											return;
										}

									}

								} else {
									ZkUtil.showInformation(
											"员工身份证信息为空，请添加员工身份证信息后较正!", "提示信息");
									return;
								}

							} else {

								ZkUtil.showInformation(
										"查询不到员工身份证信息，请添加员工身份证信息后较正!", "提示信息");
								return;
							}

							if (!StrUtil.isEmpty(grpStaff.getStaffCode())) {

								String staffNbr = grpStaff.getStaffCode();
								String staffAccount = null;

								if (!StrUtil.isNullOrEmpty(staffNbr)) {

									staffNbr = staffNbr.trim();
									queryStaffAccount = new StaffAccount();
									queryStaffAccount.setStaffAccount(staffNbr);

									// if (staffNbr.startsWith("34")) {
									//
									// if (staffNbr.length() < 2) {
									// Messagebox
									// .show("从人力中间表获取的工号长度小于2位。");
									// return;
									// }
									//
									// String staffAccStr = staffNbr
									// .substring(2, staffNbr.length());
									//
									// queryStaffAccount
									// .setStaffAccount(staffAccStr);
									//
									// } else if (staffNbr.startsWith("W34")
									// || staffNbr.startsWith("w34")) {
									//
									// if (staffNbr.length() < 3) {
									// Messagebox
									// .show("从人力中间表获取的工号长度小于3位。");
									// return;
									// }
									//
									// String staffAccStr = "W9"
									// + staffNbr.substring(3,
									// staffNbr.length());
									//
									// queryStaffAccount
									// .setStaffAccount(staffAccStr);
									//
									// }

									repeatStaffAccountList = staffManager
											.queryStaffAccountListByStaffAccount(queryStaffAccount);

									if (repeatStaffAccountList != null
											&& repeatStaffAccountList.size() > 0) {

										for (StaffAccount oldStaffAccount : repeatStaffAccountList) {

											if (!staff.getStaffId().equals(
													oldStaffAccount
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
											"集团渠道用户人力号为空，请重新选择集团渠道数据!", "提示信息");
									return;
								}

								queryStaffAccount = new StaffAccount();
								queryStaffAccount.setStaffId(staff.getStaffId());
								newStaffAccountList = staffManager
										.queryStaffAccountList(queryStaffAccount);

								if (newStaffAccountList != null
										&& newStaffAccountList.size() > 0) {

									for (StaffAccount newStaffAccount : newStaffAccountList) {
										newStaffAccount
												.setStaffAccount(staffAccount);
										newStaffAccount.update();// 更新员工账号
									}
								}

								staff.setStaffNbr(staffNbr);
								staff.update();// 更新员工人力号

							} else {
								ZkUtil.showInformation("集团用户帐号为空，请重新选择集团渠道数据!",
										"提示信息");
								return;
							}

						} else {

							ZkUtil.showInformation("主数据员工ID为空，请重新选择主数据员工!",
									"提示信息");
							return;

						}

						setGroupButtonValid(false);
						PubUtil.fillBeanFromPo(grpStaff, bean);
						onQueryGrpStaff();// 查询
						ZkUtil.showInformation("数据较正成功!", "提示信息");
					} else {
						ZkUtil.showError("请选择主数据员工或集团渠道数据!\n", "提示信息");
						return;
					}
				}

			}
		});

	}

	/**
	 * 查询组织.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryGrpStaff() throws Exception {
		if (this.queryGrpStaff != null) {
			ListboxUtils.clearListbox(bean.getGrpStaffListBox());
			PageInfo pageInfo = channelInfoManager.queryPageInfoByGrpStaff(
					queryGrpStaff, this.bean.getGrpStaffListPaging()
							.getActivePage() + 1, this.bean
							.getGrpStaffListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			this.bean.getGrpStaffListBox().setModel(dataList);
			this.bean.getGrpStaffListPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
			grpStaff = null;
		}
	}

	/**
	 * 分页
	 * 
	 * @throws Exception
	 */
	public void onGrpStaffListboxExtPaging() throws Exception {
		this.queryGrpStaff();
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetGrpStaff() throws Exception {
		PubUtil.fillBeanFromPo(new GrpStaff(), bean);
		// bean.getSalesCode().setValue(null);
		// bean.getStaffCode().setValue(null);
		// bean.getStaffName().setValue(null);
		// bean.getCertNumber().setValue(null);
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canProofread
	 *            校对按钮
	 */
	public void setGroupButtonValid(final Boolean canProofread) {
		if (canProofread != null) {
			this.bean.getProofreadButton().setDisabled(!canProofread);
		}
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

		if (PlatformUtil.isAdmin()) {
			canProofread = true;
		} else if ("groupPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRP_STAFF_PROOFREAD)) {
				canProofread = true;
			}
		} else if ("grpStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRP_STAFF_PROOFREAD)) {
				canProofread = true;
			}
		}
		this.bean.getProofreadButton().setVisible(canProofread);
	}
}
