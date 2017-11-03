package cn.ffcs.uom.activation.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

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
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.activation.component.bean.StaffActivationListboxBean;
import cn.ffcs.uom.activation.constants.ActivationConstant;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.Md5Util;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

/**
 * 员工激活管理显示列表控件 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-09-13
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffActivationListbox extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private StaffActivationListboxBean bean = new StaffActivationListboxBean();

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private CascadeRelationManager cascadeRelationManager = (CascadeRelationManager) ApplicationContextUtil
			.getBean("cascadeRelationManager");

	private Md5Util md5Util = (Md5Util) ApplicationContextUtil
			.getBean("md5Util");

	/**
	 * zul.
	 */
	private final String zul = "/pages/activation/comp/staff_activation_listbox.zul";

	/**
	 * staff.
	 */
	private Staff staff;

	/**
	 * 查询staff.
	 */
	private Staff qryStaff;

	/**
	 * staffList.
	 */
	private List<Staff> staffList;

	private Party party;

	private PartyCertification partyCertification;

	/**
	 * 存放上传数据
	 */
	private List<Party> partyList = new ArrayList<Party>();

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public StaffActivationListbox() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_QUERY, this,
				SffOrPtyCtants.ON_STAFF_QUERY_RESPONSE);
		// 去除默认查询this.onStaffActivationQuery();
		this.setStaffButtonValid(false);
	}

	public void onQueryStaffResponse(final ForwardEvent event) throws Exception {
		this.bean.getStaffActivationListboxPaging().setActivePage(0);
		qryStaff = (Staff) event.getOrigin().getData();
		this.onQueryStaff();
		if (this.bean.getStaffActivationListbox().getItemCount() == 0) {
			this.setStaffButtonValid(false);
		}
	}

	/**
	 * 员工批量选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffActivationSelectRequest() throws Exception {

		if (bean.getStaffActivationListbox().getSelectedCount() > 0) {

			this.setStaffButtonValid(true);

			Set set = bean.getStaffActivationListbox().getSelectedItems();
			Iterator it = set.iterator();

			if (it != null) {
				staffList = new ArrayList<Staff>();
				while (it.hasNext()) {
					Listitem listitem = (Listitem) it.next();
					staffList.add((Staff) listitem.getValue());
				}
			}
		}
	}

	public void onStaffActivation() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		if (staffList != null && staffList.size() > 0) {
			ZkUtil.showQuestion("确定要激活这些记录?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {

						String msg = null;

						/*if (!PlatformUtil.isAdmin()
								&& !cascadeRelationManager.isPermissions(
										PlatformUtil.getCurrentUser()
												.getRoleIds(),
										CascadeRelationConstants.RELA_CD_8)) {
							for (Staff staff : staffList) {
								if (!PlatformUtil.getCurrentUserId().equals(
										staff.getUpdateStaff())) {
									msg = "该员工是由" + staff.getUpdateStaffName2()
											+ "删除，请使用该员工账号进行激活。";
									break;
								}
							}
						}

						if (!StrUtil.isEmpty(msg)) {
							ZkUtil.showError(msg, "提示信息");
							return;
						}*/

						msg = staffManager.updateStaffList(staffList);
						if (!StrUtil.isEmpty(msg)) {
							if (msg.contains("未实名")){
								onPartyCertificationEdit();
							}
							if (msg.contains("关联的证件号重复")) {
								onPartyCertificationEdit();
							} else {
								ZkUtil.showError(msg, "提示信息");
							}

						} else {
							ZkUtil.showError("激活成功！", "提示信息");
						}
						staffList = null;
						onQueryStaff();
					}
				}
			});

		} else {
			ZkUtil.showError("请选择你想要激活的员工", "提示信息");
		}
	}

	/**
	 * 修改 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 * @throws Exception
	 */
	public void onPartyCertificationEdit() throws Exception {
		openPartyCertificationEditWin(SffOrPtyCtants.MODSPE);
	}

	/**
	 * 打开界面
	 * 
	 * @param opType
	 */
	private void openPartyCertificationEditWin(String opType) throws Exception {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);

			if (null != staffList && staffList.size() > 0) {

				Staff staff = staffList.get(0);

				if (null != staff && null != staff.getPartyRoleId()) {

					PartyRole partyRole = staffManager.getPartyRole(staff
							.getPartyRoleId());

					if (null != partyRole && null != partyRole.getPartyId()) {

						party = staffManager.getParty(partyRole.getPartyId());

						List<PartyCertification> partyCertificationList = (List<PartyCertification>) staffManager
								.getActivationObjNoStatusCd(
										PartyCertification.class,
										partyRole.getPartyId());
						for (PartyCertification partyCertification : partyCertificationList) {

							if (!StrUtil.isEmpty(partyCertification
									.getCertType())
									&& partyCertification.getCertType().equals(
											"1")) {
								this.partyCertification = partyCertification;
							}
						}
					}
				}
			}
			if (opType.equals(SffOrPtyCtants.MODSPE)) {
				arg.put("partyCertification", partyCertification);
			}
			arg.put("party", party);
			Window win = (Window) Executions.createComponents(
					SffOrPtyCtants.PARTYCERTIFICATION_EDIT_ZUL, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					ZkUtil.showError("证件实名成功,请重新激活该员工!", "提示信息");

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
	 * 设置员工按钮的状态.
	 * 
	 * @param canAct
	 *            激活按钮
	 */
	private void setStaffButtonValid(final Boolean canAct) {
		if (canAct != null) {
			bean.getStaffActBatchButton().setDisabled(!canAct);
		}
	}

	/**
	 * 查询实体 .
	 * 
	 * @author Wong 2013-5-27 Wong
	 */
	public void onQueryStaff() {

		qryStaff = new Staff();
		qryStaff.setStaffCode(this.bean.getStaffCode().getValue());
		qryStaff.setStaffAccount(this.bean.getStaffAccount().getValue());
		qryStaff.setStaffName(this.bean.getStaffName().getValue());
		StaffAccount staffAccount = new StaffAccount();
		staffAccount.setStaffAccount(this.bean.getStaffAccount().getValue());
		qryStaff.setObjStaffAccount(staffAccount);

		if (this.qryStaff != null) {

			this.setStaffButtonValid(false);

			PageInfo pageInfo = staffManager.forQuertyStaffActivation(qryStaff,
					bean.getStaffActivationListboxPaging().getActivePage() + 1,
					bean.getStaffActivationListboxPaging().getPageSize());
			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getStaffActivationListbox().setModel(dataList);
			bean.getStaffActivationListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}

	}

	/**
	 * 分页 .
	 * 
	 * @author Wong 2013-6-4 Wong
	 */
	public void onStaffActivationListboxPaging() {
		this.onQueryStaff();
	}

	/**
	 * 查询员工. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onStaffActivationQuery() {
		try {
			// cleanTabs();
			this.bean.getStaffActivationListboxPaging().setActivePage(0);
			this.onQueryStaff();
			this.qryStaff = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cleanTabs() {
		qryStaff = null;
		staff = null;
		Events.postEvent(ActivationConstant.ON_ACTIVATION_CLEAR_TABS, this,
				null);
	}

	/**
	 * .重置查询内容 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onStaffActivationReset() throws Exception {
		bean.getStaffCode().setValue(null);
		bean.getStaffAccount().setValue(null);
		bean.getStaffName().setValue(null);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAct = false;

		if (PlatformUtil.isAdmin()) {
			canAct = true;
		} else if ("activationPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ACTIVATION_STAFF)) {
				canAct = true;
			}
		}
		this.bean.getStaffActBatchButton().setVisible(canAct);
	}

}
