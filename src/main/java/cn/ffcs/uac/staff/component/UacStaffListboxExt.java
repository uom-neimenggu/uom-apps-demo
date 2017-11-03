package cn.ffcs.uac.staff.component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uac.staff.component.bean.UacStaffListboxExtBean;
import cn.ffcs.uac.staff.constant.EnumUacStaffInfo;
import cn.ffcs.uac.staff.manager.UacStaffManager;
import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uac.staff.vo.DemoStaff;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;

/**
 * Title: UAC-STAFF INFO MANAGE Description: CRUD UAC-STAFF Relational INFO
 * Copyright: FFCS© 2017 Company:FFCS
 * 
 * @author Luis.Zhang
 * @version 0.0.1 date:2017-09-06
 * @since JDK1.6
 */
@Controller
@Scope("prototype")
public class UacStaffListboxExt extends Div implements IdSpace {
	/**
	 * Auto-generated serialVersionUID
	 */
	private static final long serialVersionUID = 7521481935469438254L;

	/**
	 * zul.
	 */
	private static final String zul = EnumUacStaffInfo.UAC_STAFF_LISTBOX_EXT_ZUL
			.getValue();

	@Getter
	@Setter
	private UacStaffListboxExtBean bean = new UacStaffListboxExtBean();

	private UacStaffManager staffInfoAdapter = (UacStaffManager) ApplicationContextUtil
			.getBean("staffInfoAdapter");

	/**
	 * staff.
	 */
	private UacStaff uacStaff;

	/**
	 * 查询staff.
	 */
	private UacStaff qryStaff;
	/**
	 * 查询demoStaff.
	 */
	private DemoStaff demoStaff;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public UacStaffListboxExt() {
		Executions.createComponents(zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.setUacStaffButtonValid(false, true, false, false);
	}

	/**
	 * Uac Staff Select
	 * 
	 * @throws Exception
	 */
	public void onSelectRequest() throws Exception {
		if (bean.getUacStaffListbox().getSelectedCount() > 0) {
			uacStaff = (UacStaff) bean.getUacStaffListbox().getSelectedItem()
					.getValue();
			this.setUacStaffButtonValid(true, true, true, true);
		}
	}

	/**
	 * Set UacStaff Button Valid
	 * 
	 * @param canView
	 * @param canAdd
	 * @param canEdit
	 * @param canDel
	 */
	private void setUacStaffButtonValid(boolean canView, boolean canAdd,
			boolean canEdit, boolean canDel) {
		bean.getViewUacStaffButton().setDisabled(!canView);
		bean.getAddUacStaffButton().setDisabled(!canAdd);
		bean.getEditUacStaffButton().setDisabled(!canEdit);
		bean.getDelUacStaffButton().setDisabled(!canDel);
	}

	/**
	 * Query
	 */
	public void queryUacStaff() {
/*		if (this.qryStaff != null) {
			int activePage = bean.getUacStaffListboxPaging().getActivePage();
			int pageSize = bean.getUacStaffListboxPaging().getPageSize();
			PageInfo pageInfo = staffInfoAdapter.queryDemoStaffPage(demoStaff,
					activePage + 1, pageSize);

			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getUacStaffListbox().setModel(dataList);
			bean.getUacStaffListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}*/
		if (this.qryStaff != null) {
			int activePage = bean.getUacStaffListboxPaging().getActivePage();
			int pageSize = bean.getUacStaffListboxPaging().getPageSize();
			PageInfo pageInfo = staffInfoAdapter.queryUacStaffPage(qryStaff,
					activePage + 1, pageSize);

			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getUacStaffListbox().setModel(dataList);
			bean.getUacStaffListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}

		this.setUacStaffButtonValid(false, true, false, false);
	}

	/**
	 * Paging
	 */
	public void onListboxPaging() {
		this.queryUacStaff();
	}

	/**
	 * Add
	 * 
	 * @throws PortalException
	 * @throws SystemException
	 */
	public void onAdd() throws PortalException, SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		openUacStaffEditWin(EnumUacStaffInfo.ADD.getValue());
	}

	/**
	 * Edit
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		openUacStaffEditWin(EnumUacStaffInfo.MOD.getValue());
	}

	public void onView() throws Exception {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;
			if (uacStaff != null) {
				openUacStaffEditWin(EnumUacStaffInfo.VIEW.getValue());
			} else {
				Messagebox.show("请选择要查看的人员");
				return;
			}

		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * del
	 * 
	 * @throws PortalException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void onDel() throws PortalException, SystemException, Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		try {

			Messagebox.show("您确定要删除吗？", "提示信息", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.INFORMATION,
					new EventListener() {
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {
								setUacStaffButtonValid(true, true, false, false);
								EnumUacStaffInfo showEnum = staffInfoAdapter
										.delUacStaffAllInfo(uacStaff);
								String staffAccount = uacStaff.getAccount();
								SimpleDateFormat df = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");// 设置日期格式
								System.out.println(df.format(new Date()));// new
																			// Date()为获取当前系统时间

								String msg1 = "变更提醒：" + df.format(new Date())
										+ "账号：" + staffAccount
										+ " 信息发生变更，系统以短信通知该账号归属主管";
								Messagebox.show(msg1);
								staffInfoAdapter.saveRemindInfo(msg1);

								if (showEnum == EnumUacStaffInfo.OPERATE_SUCCESS) {
									Messagebox.show(showEnum.getValue());
									PubUtil.reDisplayListbox(
											bean.getUacStaffListbox(),
											uacStaff, "del");
									clearVariable();

								} else {
									Messagebox.show(showEnum.getValue());
								}

							}
						}
					});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 页面查询方法
	 */
	public void onQuery() {
		clearVariable();
		UacStaff objSff = UacStaff.newInstance();
		PubUtil.fillPoFromBean(bean, objSff);
		qryStaff = objSff;
		this.bean.getUacStaffListboxPaging().setActivePage(0);
		queryUacStaff();
		uacStaff = null;
	}

	public void clearVariable() {
		qryStaff = null;
		uacStaff = null;
	}

	/**
	 * openUacStaffInfoWin
	 * 
	 * @param opType
	 */
	private void openUacStaffEditWin(String opType) {

		Map<String, Object> arg = new HashMap<String, Object>();
		arg.put("opType", opType);
		if (opType.equals(EnumUacStaffInfo.MOD.getValue())
				|| opType.equals(EnumUacStaffInfo.VIEW.getValue())) {
			arg.put("uacStaff", uacStaff);
		}

		String zul = EnumUacStaffInfo.UAC_STAFF_EDIT_ZUL.getValue();

		Window win = (Window) Executions.createComponents(zul, this, arg);
		try {
			win.doModal();
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final String type = opType;
		win.addEventListener(EnumUacStaffInfo.ON_OK.getValue(),
				new EventListener() {
					@Override
					public void onEvent(Event event) {
						setUacStaffButtonValid(true, true, false, false);
						if (event.getData() != null) {
							qryStaff = (UacStaff) event.getData();

							if (type.equals(EnumUacStaffInfo.ADD.getValue())) {
								// Set value to query the add result
								if (!StrUtil.isEmpty(qryStaff.getEcode())) {
									bean.getEcode().setValue(
											qryStaff.getEcode().trim());
								}

								if (!StrUtil.isEmpty(qryStaff.getAccount())) {
									bean.getAccount().setValue(
											qryStaff.getAccount().trim());
								}

								if (!StrUtil.isEmpty(qryStaff.getStaffName())) {
									bean.getStaffName().setValue(
											qryStaff.getStaffName().trim());
								}

							}

							queryUacStaff();
						}
					}
				});
	}

	/**
	 * Set Div Visible
	 * 
	 * @param visible
	 */
	public void setUacStaffDivVisible(boolean visible) {
		bean.getUacStaffDiv().setVisible(visible);
	}

	/**
	 * Reset query condition
	 * 
	 * @throws Exception
	 */
	public void onReset() {
		bean.getEcode().setValue(null);
		bean.getAccount().setValue(null);
		bean.getStaffName().setValue(null);

		clearVariable();
		onCleanUacStaffInfo();
		setUacStaffButtonValid(false, true, false, false);
		ListboxUtils.clearListbox(this.bean.getUacStaffListbox());
		this.bean.getUacStaffListboxPaging().setTotalSize(1);

	}

	/**
	 * 清空选中的员工.
	 */
	public void onCleanUacStaffInfo() {
		this.bean.getUacStaffListbox().clearSelection();
	}

	/**
	 * Set Button Permission
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void setPagePosition(String page) throws PortalException,
			SystemException {
		boolean canView = false;
		boolean canAdd = false;
		boolean canEdit = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canView = true;
			canAdd = true;
			canDel = true;
			canEdit = true;
		} else if ("uacStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.UAC_STAFF_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.UAC_STAFF_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.UAC_STAFF_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.UAC_STAFF_VIEW)) {
				canView = true;
			}
		}
		bean.getViewUacStaffButton().setVisible(canView);
		bean.getAddUacStaffButton().setVisible(canAdd);
		bean.getEditUacStaffButton().setVisible(canEdit);
		bean.getDelUacStaffButton().setVisible(canDel);
	}
}
