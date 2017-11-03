package cn.ffcs.uom.gridUnit.component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.util.media.Media;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.ExportExcelNew;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.gridUnit.component.bean.StaffGridUnitTranListboxExtBean;
import cn.ffcs.uom.gridUnit.manager.GridUnitManager;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffOrgTranTemp;
import cn.ffcs.uom.staff.model.StaffOrgTranTempHis;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;

/**
 * 员工网格单元业务关系 .
 */
public class StaffGridUnitTranListboxExt extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private StaffGridUnitTranListboxExtBean bean = new StaffGridUnitTranListboxExtBean();

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private GridUnitManager gridUnitManager = (GridUnitManager) ApplicationContextUtil
			.getBean("gridUnitManager");

	/**
	 * 上传文件
	 */
	private Media media;

	/**
	 * 存放上传数据
	 */
	private List<StaffOrgTranTemp> staffOrgTranTempList = new ArrayList<StaffOrgTranTemp>();

	/**
	 * 文件列数
	 */
	private static final int totalColumn = 9;

	/**
	 * 员工管理中当前选择的staff
	 */
	@Getter
	@Setter
	private Staff staff;

	/**
	 * 组织树中当前选择的gridUnit
	 */
	@Getter
	@Setter
	private GridUnit gridUnit;

	/**
	 * 选中的员工组织业务关系
	 */
	@Getter
	@Setter
	private StaffOrganizationTran staffGridUnitTran;

	/**
	 * 查询
	 */
	@Getter
	@Setter
	private Staff queryStaff;

	/**
	 * 查询
	 */
	@Getter
	@Setter
	private StaffOrganizationTran queryStaffGridUnitTran;

	/**
	 * 是否是组织树页面
	 */
	@Getter
	@Setter
	private Boolean isOrgTreePage = false;

	/**
	 * 树TAB区分
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
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public StaffGridUnitTranListboxExt() {
		Executions.createComponents(
				"/pages/gridUnit/comp/staff_grid_unit_tran_listbox_ext.zul",
				this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_GRID_UNIT_TRAN_SELECT, this,
				SffOrPtyCtants.ON_STAFF_GRID_UNIT_TRAN_SELECT_RES);
		this.addForward(SffOrPtyCtants.ON_CLEAN_STAFF_GRID_UNIT_TRAN, this,
				SffOrPtyCtants.ON_CLEAN_STAFF_GRID_UNIT_TRAN_RES);
	}

	public void onCreate() throws Exception {
		if (variablePagePosition != null
				&& variablePagePosition.equals("staffGridUnitTranPage")) {
			this.setStaffGridUnitTrantButtonValid(true, false);
		} else {
			this.setStaffGridUnitTrantButtonValid(false, false);
		}
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			} else {
				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
			}
		}
	}

	public void onStaffGridUnitTranSelectRequest() {
		if (bean.getStaffGridUnitTranListbox().getSelectedCount() > 0) {
			staffGridUnitTran = (StaffOrganizationTran) bean
					.getStaffGridUnitTranListbox().getSelectedItem().getValue();
			setStaffGridUnitTrantButtonValid(true, true);
		}
	}

	/**
	 * 清空员工组织业务关系 .
	 * 
	 * @throws Exception
	 * @author zhulitao
	 */
	public void onCleaningStaffGridUnitTran() throws Exception {
		ListboxUtils.clearListbox(bean.getStaffGridUnitTranListbox());
	}

	public void onStaffGridUnitTranListboxPaging() {

		try {

			queryStaff = new Staff();
			queryStaff.setStaffName(this.bean.getStaffName().getValue());
			queryStaff.setStaffAccount(this.bean.getStaffAccount().getValue());

			/**
			 * 默认数据权最大电信管理区域
			 */
			if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			}

			queryStaffGridUnitTran
					.setPermissionTelcomRegion(permissionTelcomRegion);

			setStaffGridUnitTrantButtonValid(true, false);

			if (this.queryStaffGridUnitTran != null) {
				queryStaffGridUnitTran.setMmeFid(bean.getMmeFid().getValue());
				queryStaffGridUnitTran.setGridName(bean.getGridName()
						.getValue());
				Paging paging = bean.getStaffGridUnitTranListboxPaging();
				PageInfo pageInfo = staffManager
						.queryPageInfoStaffGridUnitTran(queryStaff,
								queryStaffGridUnitTran,
								paging.getActivePage() + 1,
								paging.getPageSize());
				ListModel list = new BindingListModelList(
						pageInfo.getDataList(), true);
				bean.getStaffGridUnitTranListbox().setModel(list);
				bean.getStaffGridUnitTranListboxPaging().setTotalSize(
						pageInfo.getTotalCount());
			} else {
				/**
				 * 组织树未选择组织添加清理操作
				 */
				ListboxUtils.clearListbox(bean.getStaffGridUnitTranListbox());
			}
		} catch (WrongValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public void onQueryStaffGridUnitTran() throws Exception {
		this.bean.getStaffGridUnitTranListboxPaging().setActivePage(0);
		queryStaffGridUnitTran = new StaffOrganizationTran();
		if (null != gridUnit
				&& !StrUtil.isNullOrEmpty(gridUnit.getGridUnitId())) {
			queryStaffGridUnitTran.setOrgId(gridUnit.getGridUnitId());
		}
		this.onStaffGridUnitTranListboxPaging();

	}

	/**
	 * 导出无员工关系网格单元清单
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onGridUnitNoStaffTranExport() throws Exception {
		// 设置字符集
		String charset = "UTF-8";
		// 项目根目录
		HttpServletRequest httpRequest = (HttpServletRequest) Executions
				.getCurrent().getNativeRequest();

		/**
		 * 默认数据权最大电信管理区域
		 */
		if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
			permissionTelcomRegion = new TelcomRegion();
			permissionTelcomRegion
					.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
		}

		List gridUnitList = gridUnitManager.queryGridUnitNoStaffTranList(permissionTelcomRegion);

		if (gridUnitList == null || gridUnitList.size() <= 0) {
			Messagebox.show("无员工关系网格单元清单中没有数据,未下载！");
			return;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExportExcelNew.exportExcel("无员工关系网格单元清单", gridUnitList, out);
		byte[] content = out.toByteArray();

		// 清空缓冲区
		out.flush();
		// 关闭文件输出流
		out.close();

		// 编码后文件名
		String encodedName = null;
		encodedName = URLEncoder.encode("无员工关系网格单元清单.xlsx", charset);
		// 将空格替换为+号
		encodedName = encodedName.replace("%20", "+");

		// 解决ie6 bug 或者是火狐浏览器
		if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
				|| Servlets.isGecko3(httpRequest)) {
			encodedName = new String("员工网格单元关系管理清单.xlsx".getBytes(charset),
					"ISO8859-1");
		}

		Filedownload.save(content, "application/octet-stream", encodedName);
	}

	/**
	 * 导出员工网格单元关系管理清单
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onStaffGridUnitTranExport() throws Exception {
		// 设置字符集
		String charset = "UTF-8";
		// 项目根目录
		HttpServletRequest httpRequest = (HttpServletRequest) Executions
				.getCurrent().getNativeRequest();

		queryStaffGridUnitTran = new StaffOrganizationTran();
		if (null != gridUnit
				&& !StrUtil.isNullOrEmpty(gridUnit.getGridUnitId())) {
			queryStaffGridUnitTran.setOrgId(gridUnit.getGridUnitId());
		}

		queryStaff = new Staff();
		queryStaff.setStaffName(this.bean.getStaffName().getValue());
		queryStaff.setStaffAccount(this.bean.getStaffAccount().getValue());

		/**
		 * 默认数据权最大电信管理区域
		 */
		if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
			permissionTelcomRegion = new TelcomRegion();
			permissionTelcomRegion
					.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
		}

		queryStaffGridUnitTran
				.setPermissionTelcomRegion(permissionTelcomRegion);

		queryStaffGridUnitTran
				.setPermissionTelcomRegion(permissionTelcomRegion);

		setStaffGridUnitTrantButtonValid(true, false);

		queryStaffGridUnitTran.setMmeFid(bean.getMmeFid().getValue());
		queryStaffGridUnitTran.setGridName(bean.getGridName().getValue());

		List staffAuditList = staffManager.queryStaffGridUnitTranList(
				queryStaff, queryStaffGridUnitTran);

		if (staffAuditList == null || staffAuditList.size() <= 0) {
			Messagebox.show("员工网格单元关系管理清单中没有数据,未下载！");
			return;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ExportExcelNew.exportExcel("员工网格单元关系管理清单", staffAuditList, out);
		byte[] content = out.toByteArray();

		// 清空缓冲区
		out.flush();
		// 关闭文件输出流
		out.close();

		// 编码后文件名
		String encodedName = null;
		encodedName = URLEncoder.encode("员工网格单元关系管理清单.xlsx", charset);
		// 将空格替换为+号
		encodedName = encodedName.replace("%20", "+");

		// 解决ie6 bug 或者是火狐浏览器
		if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
				|| Servlets.isGecko3(httpRequest)) {
			encodedName = new String("员工网格单元关系管理清单.xlsx".getBytes(charset),
					"ISO8859-1");
		}

		Filedownload.save(content, "application/octet-stream", encodedName);
	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetStaffGridUnitTran() throws Exception {
		this.bean.getMmeFid().setValue(null);
		this.bean.getGridName().setValue("");
		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");
	}

	public void onSelectStaffGridUnitTranResponse(final ForwardEvent event) {
		gridUnit = (GridUnit) event.getOrigin().getData();
		if (null != gridUnit
				&& !StrUtil.isNullOrEmpty(gridUnit.getGridUnitId())) {
			queryStaffGridUnitTran = new StaffOrganizationTran();
			queryStaffGridUnitTran.setOrgId(gridUnit.getGridUnitId());
			this.bean.getMmeFid().setValue(null);
			this.bean.getGridName().setValue("");
			this.bean.getStaffName().setValue("");
			this.bean.getStaffAccount().setValue("");
			this.onStaffGridUnitTranListboxPaging();
		}
	}

	/**
	 * 新增 .
	 * 
	 * @author 朱林涛
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onStaffGridUnitTranAdd() throws PortalException,
			SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openStaffGridUnitTranEditWin(SffOrPtyCtants.ADD);
	}

	/**
	 * 修改 .
	 * 
	 * @author 朱林涛
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onStaffGridUnitTranEdit() throws PortalException,
			SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openStaffGridUnitTranEditWin(SffOrPtyCtants.MOD);
	}

	@SuppressWarnings("unused")
	private void openStaffGridUnitTranEditWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			arg.put("isOrgTreePage", isOrgTreePage);
			arg.put("variableOrgTreeTabName", variableOrgTreeTabName);
			arg.put("staffGridUnitTran", queryStaffGridUnitTran);

			Window win = (Window) Executions.createComponents(
					"/pages/gridUnit/comp/staff_grid_unit_tran_edit.zul", this,
					arg);
			win.doModal();
			final String type = opType;
			win.addEventListener("onStaffTranOk", new EventListener() {
				public void onEvent(Event event) {
					setStaffGridUnitTrantButtonValid(true, false);
					if (event.getData() != null) {
						StaffOrganizationTran staffGridUnitTran = (StaffOrganizationTran) event
								.getData();
						if (staffGridUnitTran != null
								&& staffGridUnitTran.getStaffId() != null
								&& staffGridUnitTran.getOrgId() != null) {

							Staff staff = staffGridUnitTran.getStaff();
							GridUnit gridUnit = staffGridUnitTran.getGridUnit();

							if (staff != null) {

								bean.getStaffName().setValue(
										staff.getStaffName());

								StaffAccount staffAccount = staff
										.getStaffAccountFromDB();

								if (staffAccount != null) {
									bean.getStaffAccount().setValue(
											staffAccount.getStaffAccount());
								}

							}

							if (gridUnit != null) {
								bean.getMmeFid().setValue(gridUnit.getMmeFid());
								bean.getGridName().setValue(
										gridUnit.getGridName());
							}
						}

						queryStaffGridUnitTran = new StaffOrganizationTran();
						onStaffGridUnitTranListboxPaging();
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
	 */
	public void setStaffGridUnitTrantButtonValid(boolean add, boolean del) {
		bean.getAddStaffGridUnitTranButton().setDisabled(!add);
		bean.getDelStaffGridUnitTranButton().setDisabled(!del);
	}

	public void onCleanStaffGridUnitTranRespons() {
		this.bean.getMmeFid().setValue(null);
		this.bean.getGridName().setValue("");
		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");
		gridUnit = null;
		queryStaffGridUnitTran = null;
		ListboxUtils.clearListbox(bean.getStaffGridUnitTranListbox());
		setStaffGridUnitTrantButtonValid(false, false);
	}

	/**
	 * 下载模版
	 */
	@SuppressWarnings("deprecation")
	public void onDownloadTemplate() {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "staff_org_tran_template.xls";
			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode(fileName, charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String(fileName.getBytes(charset),
						"ISO8859-1");
			}
			Filedownload.save(
					new FileInputStream(httpRequest
							.getRealPath("/pages/staff/doc/" + fileName)),
					"application/octet-stream", encodedName);
		} catch (Exception e) {
			ZkUtil.showError("下载员工组织业务关系导入模版失败。", "系统提示");
		}
	}

	/**
	 * 文件上传 导入员工角色关系
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$uploadButton(ForwardEvent event) throws Exception {
		this.staffOrgTranTempList.clear();
		media = ((UploadEvent) event.getOrigin()).getMedia();
		if (null == media) {
			ZkUtil.showError("请选择要上传的文件!", "系统提示");
			return;
		}
		String fileName = media.getName();
		if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
			readsUpLoadFile();
		} else {
			ZkUtil.showError("导入的文件必须是以.xls或.xlsx结尾的EXCEL文件!", "系统提示");
			return;
		}
	}

	/**
	 * 读取员工组织业务关系导入文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void readsUpLoadFile() throws Exception {

		String[][] objArray = FileUtil.readExcel(media, 1, 1);

		if (objArray == null || objArray.length == 0) {
			ZkUtil.showError("导入文件中没有数据！", "错误信息");
			return;
		} else if (objArray.length > 500) {
			ZkUtil.showInformation("导入文件中数据量不能超过500条！", "提示信息");
			return;
		}

		String transId = EsbHeadUtil
				.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);

		for (int i = 0; i < objArray.length; i++) {

			StaffOrgTranTemp staffOrgTranTemp = new StaffOrgTranTemp();
			staffOrgTranTemp.setTransId(transId);

			for (int j = 0; j < totalColumn; j++) {

				String str = "";

				if (null != objArray[i][j]) {
					str = objArray[i][j].trim();
				}

				String[] strs = null;

				if (!StrUtil.isNullOrEmpty(str)) {
					strs = str.split("-");
				}

				switch (j) {
				case 0:// 数据来源--必填
					if (null != strs && strs.length > 1) {
						staffOrgTranTemp.setDataSource(strs[1]);
					} else {
						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
								SffOrPtyCtants.FIELD_ERROR));
					}
					break;
				case 1:// 操作类型--必填
					if (null != strs && strs.length > 1) {
						staffOrgTranTemp.setOperationType(strs[1]);
					} else {
						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
								SffOrPtyCtants.FIELD_ERROR));
					}
					break;
				case 2:// 员工姓名 -必填 修改为非必填 2017年2月20日 by xiaof
					if (StrUtil.isNullOrEmpty(str)) {
						// staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
						// SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staffOrgTranTemp.setStaffName(str);
					}
					break;
				case 3:// 员工账号 --必填
					if (StrUtil.isNullOrEmpty(str)) {
						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staffOrgTranTemp.setStaffAccount(str);
					}
					break;
				case 4:// 员工编码 --必填 修改为非必填 2017年2月20日 by xiaof
					if (StrUtil.isNullOrEmpty(str)) {
						// staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
						// SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staffOrgTranTemp.setStaffCode(str);
					}
					break;
				case 5:// 组织编码 --必填
					if (IdcardValidator.isDigital(str)) {
						staffOrgTranTemp.setOrgCode(str);
					} else {
						staffOrgTranTemp
								.setErrMsg(this
										.getValidateMsg(
												i,
												j,
												SffOrPtyCtants.FIELD_ERROR_ORG_CODE_OR_MME_FID_NUBER));
					}
					break;
				case 6:// 组织全称--必填
					if (StrUtil.isNullOrEmpty(str)) {
						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staffOrgTranTemp.setOrgFullName(str);
					}
					break;
				case 7:// 关联类型--新增时为必填，删除时为选填
					if (null != strs && strs.length > 1) {
						staffOrgTranTemp.setRalaCd(strs[1]);
					} else if (!StrUtil.isEmpty(staffOrgTranTemp
							.getOperationType())
							&& BaseUnitConstants.OPERATION_TYPE_ADD
									.equals(staffOrgTranTemp.getOperationType())) {
						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
								SffOrPtyCtants.FIELD_ERROR));
					}
					break;
				case 8:// 员工排序 --新增时为必填，删除时为选填
					if (StrUtil.isEmpty(str)
							&& !StrUtil.isEmpty(staffOrgTranTemp
									.getOperationType())
							&& BaseUnitConstants.OPERATION_TYPE_ADD
									.equals(staffOrgTranTemp.getOperationType())) {
						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						if (IdcardValidator.isDigital(str)) {
							staffOrgTranTemp.setStaffSort(Long.parseLong(str));
						} else if (BaseUnitConstants.OPERATION_TYPE_ADD
								.equals(staffOrgTranTemp.getOperationType())) {
							staffOrgTranTemp
									.setErrMsg(this
											.getValidateMsg(
													i,
													j,
													SffOrPtyCtants.FIELD_ERROR_STAFF_SORT_NUBER));
						}
					}
					break;
				default:
					break;
				}
			}
			// 对execl里面的数据进行重复验证
			if (staffOrgTranTempList != null && staffOrgTranTempList.size() > 0) {

				for (StaffOrgTranTemp staffOrgTranTempCache : staffOrgTranTempList) {

					if (!StrUtil.isEmpty(staffOrgTranTemp.getOperationType())
							// &&
							// !StrUtil.isEmpty(staffOrgTranTemp.getStaffCode())
							&& !StrUtil.isEmpty(staffOrgTranTemp.getOrgCode())
							&& !StrUtil.isEmpty(staffOrgTranTemp.getRalaCd())) {
						// 后面 各种维护人员可以兼职多个
						// 获取关联类型，判断是否是1011，1021，1001分别是：线路维护人，设备维护人，装维负责人
						if (staffOrgTranTemp.getOperationType().equals(
								staffOrgTranTempCache.getOperationType())
								// && staffOrgTranTemp.getStaffCode().equals(
								// staffOrgTranTempCache.getStaffCode())
								&& staffOrgTranTemp.getOrgCode().equals(
										staffOrgTranTempCache.getOrgCode())
								&& staffOrgTranTemp.getRalaCd().equals(
										staffOrgTranTempCache.getRalaCd())) {
							staffOrgTranTemp.setErrMsg(this.getValidateMsg(i,
									0, SffOrPtyCtants.FIELD_REPEAT));
						}
					}
				}
			}

			if (StrUtil.isEmpty(staffOrgTranTemp.getErrMsg())) {
				staffOrgTranTemp.setResult(WsConstants.TASK_INIT);
			} else {
				staffOrgTranTemp.setResult(WsConstants.TASK_FAILED);
			}

			this.staffOrgTranTempList.add(staffOrgTranTemp);

		}

		if (staffOrgTranTempList != null && staffOrgTranTempList.size() > 0) {
			staffManager.saveStaffOrgTranTempList(staffOrgTranTempList);
		}

		// 员工组织业务关系删除操作
		StaffOrgTranTemp staffOrgTranTemp = new StaffOrgTranTemp();
		staffOrgTranTemp.setTransId(transId);
		staffOrgTranTemp.setOperationType(BaseUnitConstants.OPERATION_TYPE_DEL);
		staffOrgTranTemp.setResult(WsConstants.TASK_INIT);

		this.staffOrgTranTempList.clear();
		staffOrgTranTempList = staffManager
				.queryStaffOrgTranTempList(staffOrgTranTemp);

		if (staffOrgTranTempList != null && staffOrgTranTempList.size() > 0) {

			for (StaffOrgTranTemp staffOrgTranTempDb : staffOrgTranTempList) {

				Staff staff = new Staff();
				staff.setStaffAccount(staffOrgTranTempDb.getStaffAccount());
				staff.setStaffCode(staffOrgTranTempDb.getStaffCode());

				List<Staff> staffList = staffManager
						.queryStaffListByStaff(staff);

				if (staffList != null && staffList.size() > 0
						&& staffList.get(0).getStaffId() != null) {

					Long orgId = null;

					if (BaseUnitConstants.DATA_SOURCE_UOM
							.equals(staffOrgTranTempDb.getDataSource())) {

						Organization organization = new Organization();
						organization
								.setOrgCode(staffOrgTranTempDb.getOrgCode());
						organization = organizationManager
								.queryOrganizationByOrgCode(organization);

						if (organization != null
								&& organization.getOrgId() != null) {
							orgId = organization.getOrgId();
						} else {
							staffOrgTranTempDb
									.setResult(WsConstants.TASK_FAILED);
							staffOrgTranTempDb
									.setErrMsg("该组织在主数据中不存在，请检查组织编码是否正确。");
							staffOrgTranTempDb.updateOnly();
							continue;
						}

					} else if (BaseUnitConstants.DATA_SOURCE_GRID_UNIT
							.equals(staffOrgTranTempDb.getDataSource())) {

						GridUnit gridUnit = new GridUnit();
						gridUnit.setMmeFid(Long.parseLong(staffOrgTranTempDb
								.getOrgCode()));
						List<GridUnit> gridUnitList = gridUnitManager
								.queryGridUnitList(gridUnit);

						if (gridUnitList != null && gridUnitList.size() > 0
								&& gridUnitList.get(0).getGridUnitId() != null) {

							orgId = gridUnitList.get(0).getGridUnitId();

						} else {
							staffOrgTranTempDb
									.setResult(WsConstants.TASK_FAILED);
							staffOrgTranTempDb
									.setErrMsg("该组织在主数据中不存在，请检查全息网格标识是否正确。");
							staffOrgTranTempDb.updateOnly();
							continue;
						}

					}

					StaffOrganizationTran staffOrgTran = new StaffOrganizationTran();
					staffOrgTran.setStaffId(staffList.get(0).getStaffId());
					staffOrgTran.setOrgId(orgId);
					staffOrgTran.setRalaCd(staffOrgTranTempDb.getRalaCd());

					List<StaffOrganizationTran> staffOrgTranList = staffManager
							.queryStaffOrgTranList(staffOrgTran);

					for (StaffOrganizationTran staffOrgTranDb : staffOrgTranList) {
						staffManager.deleteStaffOrgTran(staffOrgTranDb);
					}

					staffOrgTranTempDb.setResult(WsConstants.TASK_SUCCESS);
					staffOrgTranTempDb.updateOnly();

				} else {
					staffOrgTranTempDb.setResult(WsConstants.TASK_FAILED);
					staffOrgTranTempDb
							.setErrMsg("该员工在主数据中不存在，请检查员工工号和员工编码是否正确。");
					staffOrgTranTempDb.updateOnly();
				}

			}
		}

		// 员工组织业务关系新增操作
		staffOrgTranTemp.setOperationType(BaseUnitConstants.OPERATION_TYPE_ADD);

		this.staffOrgTranTempList.clear();
		staffOrgTranTempList = staffManager
				.queryStaffOrgTranTempList(staffOrgTranTemp);

		if (staffOrgTranTempList != null && staffOrgTranTempList.size() > 0) {

			for (StaffOrgTranTemp staffOrgTranTempDb : staffOrgTranTempList) {

				Staff staff = new Staff();
				staff.setStaffAccount(staffOrgTranTempDb.getStaffAccount());
				staff.setStaffCode(staffOrgTranTempDb.getStaffCode());

				List<Staff> staffList = staffManager
						.queryStaffListByStaff(staff);

				if (staffList != null && staffList.size() > 0
						&& staffList.get(0).getStaffId() != null) {

					Long orgId = null;

					if (BaseUnitConstants.DATA_SOURCE_UOM
							.equals(staffOrgTranTempDb.getDataSource())) {

						Organization organization = new Organization();
						organization
								.setOrgCode(staffOrgTranTempDb.getOrgCode());
						organization = organizationManager
								.queryOrganizationByOrgCode(organization);

						if (organization != null
								&& organization.getOrgId() != null) {
							orgId = organization.getOrgId();
						} else {
							staffOrgTranTempDb
									.setResult(WsConstants.TASK_FAILED);
							staffOrgTranTempDb
									.setErrMsg("该组织在主数据中不存在，请检查组织编码是否正确。");
							staffOrgTranTempDb.updateOnly();
							continue;
						}

					} else if (BaseUnitConstants.DATA_SOURCE_GRID_UNIT
							.equals(staffOrgTranTempDb.getDataSource())) {

						GridUnit gridUnit = new GridUnit();
						gridUnit.setMmeFid(Long.parseLong(staffOrgTranTempDb
								.getOrgCode()));
						List<GridUnit> gridUnitList = gridUnitManager
								.queryGridUnitList(gridUnit);

						if (gridUnitList != null && gridUnitList.size() > 0
								&& gridUnitList.get(0).getGridUnitId() != null) {

							orgId = gridUnitList.get(0).getGridUnitId();

						} else {
							staffOrgTranTempDb
									.setResult(WsConstants.TASK_FAILED);
							staffOrgTranTempDb
									.setErrMsg("该组织在主数据中不存在，请检查全息网格标识是否正确。");
							staffOrgTranTempDb.updateOnly();
							continue;
						}

					}

					StaffOrganizationTran staffOrgTran = new StaffOrganizationTran();
					staffOrgTran.setStaffId(staffList.get(0).getStaffId());
					staffOrgTran.setOrgId(orgId);
					staffOrgTran.setRalaCd(staffOrgTranTempDb.getRalaCd());
					staffOrgTran
							.setStaffSort(staffOrgTranTempDb.getStaffSort());

					/**
					 * 同一个组织下，同一种关系类型的排序号不能重复
					 */
					StaffOrganizationTran vo = new StaffOrganizationTran();
					vo.setOrgId(staffOrgTran.getOrgId());
					vo.setRalaCd(staffOrgTran.getRalaCd());
					vo.setStaffSort(staffOrgTran.getStaffSort());
					List<StaffOrganizationTran> existlist = staffManager
							.queryStaffOrgTranList(vo);
					if (existlist != null && existlist.size() > 0) {
						staffOrgTranTempDb.setResult(WsConstants.TASK_FAILED);
						staffOrgTranTempDb.setErrMsg("相同关联类型下,员工排序号不能重复!");
						staffOrgTranTempDb.updateOnly();
						continue;
					}

					/**
					 * 一个员工只能和组织有一种业务关系
					 */
					vo = new StaffOrganizationTran();
					vo.setStaffId(staffOrgTran.getStaffId());
					vo.setOrgId(staffOrgTran.getOrgId());
					existlist = null;
					existlist = staffManager.queryStaffOrgTranList(vo);
					// 前三种1、负责人2、非负责人，3.其他互斥
					// 后面 各种维护人员可以兼职多个
					// 获取关联类型，判断是否是1011，1021，1001分别是：线路维护人，设备维护人，装维负责人
					if (staffOrgTran.getRalaCd().equals("1011")
							|| staffOrgTran.getRalaCd().equals("1021")
							|| staffOrgTran.getRalaCd().equals("1001")) {
						existlist = null;
					}

					if (existlist != null && existlist.size() > 0) {
						staffOrgTranTempDb.setResult(WsConstants.TASK_FAILED);
						staffOrgTranTempDb.setErrMsg("该员工和该组织已存在业务关系,不能重复添加.");
						staffOrgTranTempDb.updateOnly();
						continue;
					}

					/**
					 * 不同关联类型对人数的控制
					 */
					vo = new StaffOrganizationTran();
					existlist = null;

					vo.setOrgId(staffOrgTran.getOrgId());
					vo.setRalaCd(staffOrgTran.getRalaCd());
					existlist = staffManager.queryStaffOrgTranList(vo);

					if (existlist != null && existlist.size() > 0
							&& !StrUtil.isNullOrEmpty(vo.getRalaCdNumber())) {
						if (existlist.size() >= vo.getRalaCdNumber()) {
							staffOrgTranTempDb
									.setResult(WsConstants.TASK_FAILED);
							staffOrgTranTempDb.setErrMsg("关联类型是"
									+ vo.getRalaCdName() + "的，人数不能超过"
									+ vo.getRalaCdNumber() + "个!");
							staffOrgTranTempDb.updateOnly();
							continue;
						}
					}

					staffManager.saveStaffOrgTran(staffOrgTran);
					staffOrgTranTempDb.setResult(WsConstants.TASK_SUCCESS);
					staffOrgTranTempDb.updateOnly();

				} else {
					staffOrgTranTempDb.setResult(WsConstants.TASK_FAILED);
					staffOrgTranTempDb
							.setErrMsg("该员工在主数据中不存在，请检查员工工号和员工编码是否正确。");
					staffOrgTranTempDb.updateOnly();
				}

			}
		}

		// 显示结果信息
		staffOrgTranTemp.setOperationType(null);
		staffOrgTranTemp.setResult(null);
		this.staffOrgTranTempList.clear();
		staffOrgTranTempList = staffManager
				.queryStaffOrgTranTempList(staffOrgTranTemp);

		// 处理成功的数据，转移到历史表中
		staffOrgTranTemp.setResult(WsConstants.TASK_SUCCESS);
		List<StaffOrgTranTemp> staffOrgTranTempListDb = staffManager
				.queryStaffOrgTranTempList(staffOrgTranTemp);
		if (staffOrgTranTempListDb != null && staffOrgTranTempListDb.size() > 0) {
			for (StaffOrgTranTemp staffOrgTranTempDb : staffOrgTranTempListDb) {
				StaffOrgTranTempHis staffOrgTranTempHis = new StaffOrgTranTempHis();
				BeanUtils.copyProperties(staffOrgTranTempHis,
						staffOrgTranTempDb);
				staffOrgTranTempHis.addOnly();
				DefaultDaoFactory.getDefaultDao().removeObject(
						StaffOrgTranTemp.class,
						staffOrgTranTempDb.getStaffOrgTranTempId());
			}
		}

		Map arg = new HashMap();
		arg.put("staffOrgTranTempList", staffOrgTranTempList);
		Window win = (Window) Executions.createComponents(
				"/pages/staff/staff_org_tran_import_result.zul", null, arg);
		win.doModal();

	}

	/**
	 * 获取校验返回信息
	 * 
	 * @param i
	 *            行
	 * @param j
	 *            列
	 * @param validateType
	 *            校验类型
	 * @return
	 */
	private String getValidateMsg(int i, int j, String validateType) {
		if (SffOrPtyCtants.NULL_OR_EMPTY.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.NULL_OR_EMPTY_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_REPEAT.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_REPEAT_STR + "的信息 ；";
		} else if (SffOrPtyCtants.FIELD_ERROR.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_NOT_EXIST.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_NOT_EXIST_STR + "的归属部门编码信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_OR_STAFF_NOT_EXIST
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_OR_STAFF_NOT_EXIST_STR
					+ "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_STAFF_SORT_NUBER
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_STAFF_SORT_NUBER_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_ORG_CODE_OR_MME_FID_NUBER
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_ORG_CODE_OR_MME_FID_NUBER_STR
					+ "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_EXIST
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_EXIST_STR
					+ "的信息； ";
		} else {
			if (!StrUtil.isNullOrEmpty(validateType)) {
				return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
						+ validateType;
			}
		}
		return "";
	}

	public void onStaffGridUnitTranDel() {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;
			Messagebox.show("您确定要删除吗？", "提示信息", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.INFORMATION,
					new EventListener() {
						public void onEvent(Event event) throws Exception {
							Integer result = (Integer) event.getData();
							if (result == Messagebox.OK) {
								setStaffGridUnitTrantButtonValid(true, false);
								staffManager
										.deleteStaffOrgTran(staffGridUnitTran);
								Messagebox.show("员工组织业务关系删除成功！");
								PubUtil.reDisplayListbox(
										bean.getStaffGridUnitTranListbox(),
										staffGridUnitTran, "del");
							}
						}
					});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
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
		boolean canUpload = false;
		boolean canDownload = false;
		boolean canExport = false;
		boolean canNoStaffExport = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpload = true;
			canDownload = true;
			canExport = true;
			canNoStaffExport = true;
			canDel = true;
		} else if ("gridUnitPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRID_UNIT_TRAN_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_TRAN_UP_LOAD)) {
				canUpload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_TRAN_DOWN_LOAD)) {
				canDownload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRID_UNIT_TRAN_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRID_UNIT_TRAN_EXPORT)) {
				canExport = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GRID_UNIT_NO_STAFF_TRAN_EXPORT)) {
				canNoStaffExport = true;
			}
		} else if ("staffGridUnitTranPage".equals(page)) {

			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRID_UNIT_TRAN_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_TRAN_UP_LOAD)) {
				canUpload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_TRAN_DOWN_LOAD)) {
				canDownload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRID_UNIT_TRAN_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_GRID_UNIT_TRAN_EXPORT)) {
				canExport = true;
			}
			
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.GRID_UNIT_NO_STAFF_TRAN_EXPORT)) {
				canNoStaffExport = true;
			}
		}

		this.bean.getAddStaffGridUnitTranButton().setVisible(canAdd);
		this.bean.getUploadButton().setVisible(canUpload);
		this.bean.getDownloadButton().setVisible(canDownload);
		this.bean.getDelStaffGridUnitTranButton().setVisible(canDel);
		this.bean.getExportStaffGridUnitTranButton().setVisible(canExport);
		this.bean.getExportGridUnitNoStaffTranButton().setVisible(canNoStaffExport);
	}

	/**
	 * 设置组织树tab页,按tab区分权限
	 * 
	 * @param orgTreeTabName
	 */
	public void setOrgTreeTabName(String orgTreeTabName) throws Exception {
		boolean canAdd = false;
		boolean canUpload = false;
		boolean canDownload = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpload = true;
			canDownload = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("gridUnitTreeTab".equals(orgTreeTabName)) {

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_GRID_UNIT_STAFF_ORG_TRAN_ADD)) {
					canAdd = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.STAFF_ORG_TRAN_UP_LOAD)) {
					canUpload = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.STAFF_ORG_TRAN_DOWN_LOAD)) {
					canDownload = true;
				}

				if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
						ActionKeys.ORG_TREE_GRID_UNIT_STAFF_ORG_TRAN_DEL)) {
					canDel = true;
				}

			}
			this.bean.getAddStaffGridUnitTranButton().setVisible(canAdd);
			this.bean.getUploadButton().setVisible(canUpload);
			this.bean.getDownloadButton().setVisible(canDownload);
			this.bean.getDelStaffGridUnitTranButton().setVisible(canDel);
		}
	}
}
