package cn.ffcs.uom.staffrole.action;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.util.RolePermissionUtil;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.action.bean.StaffRoleListboxBean;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.manager.StaffRoleManager;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;

@Controller
@Scope("prototype")
public class StaffRoleListboxComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;
	private StaffRoleListboxBean bean = new StaffRoleListboxBean();
	private StaffRoleRela staffRoleRela;
	private StaffRoleRela qryStaffRoleRela;
	private StaffRole staffRole;
	private Staff staff;
	/**
	 * 存放上传数据
	 */
	private List<Long> staffRoleList = new ArrayList<Long>();
	/**
	 * 存放上传数据
	 */
	private List<StaffRoleRela> staffRoleRelaList = new ArrayList<StaffRoleRela>();
	/**
	 * 存放上传待删除数据
	 */
	private List<StaffRoleRela> delStaffRoleRelaList = new ArrayList<StaffRoleRela>();
	/**
	 * 存放显示上传待删除数据
	 */
	private List<StaffRoleRela> showDelStaffRoleRelaList = new ArrayList<StaffRoleRela>();
	/**
	 * 上传文件
	 */
	private Media media;

	/**
	 * 文件列数
	 */
	private static final int totalColumn = 5;

	@Autowired
	private StaffManager staffManager;

	@Autowired
	private StaffRoleManager staffRoleManager;

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		this.setButtonValid(true, false);
		bean.getStaffRoleListbox().setPageSize(10);
		/**
		 * 点击树控件查询出员工角色关系列表
		 */
		bean.getStaffRoleMainWin().addForward(
				StaffRoleConstants.ON_SELECT_TREE_ROLE,
				bean.getStaffRoleMainWin(),
				StaffRoleConstants.ON_SELECT_TREE_ROLE_RESPONSE);
		/**
		 * 删除树节点事件
		 */
		bean.getStaffRoleMainWin().addForward(
				StaffRoleConstants.ON_DEL_NODE_OK, bean.getStaffRoleMainWin(),
				StaffRoleConstants.ON_SELECT_TREE_ROLE_RESPONSE);
		/**
		 * 员工页面选择角色tab事件
		 */
		bean.getStaffRoleMainWin().addForward(
				SffOrPtyCtants.ON_STAFF_PAGE_SELECT_FOR_ROLE,
				bean.getStaffRoleMainWin(), "onSelectStaffResponse");
		/**
		 * 员工页面选择角色tab事件
		 */
		bean.getStaffRoleMainWin().addForward(
				SffOrPtyCtants.ON_STAFF_ROLE_PAGE_POSITION,
				bean.getStaffRoleMainWin(), "onStaffRolePagePositionResponse");
		List<StaffRole> staffRoleListDb = staffRoleManager
				.queryStaffRoles(null);
		if (null != staffRoleListDb && staffRoleListDb.size() > 0) {
			for (StaffRole staffRole : staffRoleListDb) {
				staffRoleList.add(staffRole.getRoleId());
			}
		}
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffRoleMainWin() throws Exception {
		// setPagePosition("staffRolePage");
	}

	public void onSelectStaffResponse(final ForwardEvent event)
			throws Exception {
		this.staff = (Staff) event.getOrigin().getData();
		this.queryStaffRoleRela();
		this.setButtonValid(true, false);
	}

	public void onStaffRolePagePositionResponse(final ForwardEvent event)
			throws Exception {
		String page = (String) event.getOrigin().getData();
		this.setPagePosition(page);
	}

	public void onSelectTreeRoleResponse(final ForwardEvent event)
			throws Exception {
		this.staffRole = (StaffRole) event.getOrigin().getData();
		this.queryStaffRoleRelaForTree();
		this.setButtonValid(true, false);
	}

	public void onStaffRoleSelectRequest() throws Exception {
		if (bean.getStaffRoleListbox().getSelectedCount() > 0) {
			staffRoleRela = (StaffRoleRela) bean.getStaffRoleListbox()
					.getSelectedItem().getValue();
			this.setButtonValid(true, true);
		}
	}

	public void onStaffRoleListPaging() throws Exception {
		this.queryStaffRoleRela();
	}

	public void onQuery() throws Exception {
		this.bean.getStaffRoleListboxPaging().setActivePage(0);
		this.queryStaffRoleRela();
	}

	public void onReset() throws Exception {
		this.bean.getStaffName().setValue("");
		this.bean.getStaffCode().setValue("");
		this.bean.getStaffAccount().setValue("");
	}

	private void queryStaffRoleRela() throws Exception {
		Staff staffTemp = Staff.newInstance();
		if (null == qryStaffRoleRela) {
			qryStaffRoleRela = new StaffRoleRela();
		}
		/*
		 * if(null != staffRole && null != staffRole.getRoleId()){
		 * qryStaffRoleRela.setRoleId(staffRole.getRoleId()); }
		 */
		if (null != staff && null != staff.getStaffId()) {
			qryStaffRoleRela.setStaffId(staff.getStaffId());
			BeanUtils.copyProperties(staff, staffTemp);
		}
		staffTemp.setStaffName(this.bean.getStaffName().getValue());
		staffTemp.setStaffCode(this.bean.getStaffCode().getValue());
		staffTemp.setStaffAccount(this.bean.getStaffAccount().getValue());
		qryStaffRoleRela.setQryStaff(staffTemp);
		qryStaffRoleRela.setRoleId(null);
		ListboxUtils.clearListbox(bean.getStaffRoleListbox());
		PageInfo pageInfo = staffRoleManager.queryStaffRoleRela(
				qryStaffRoleRela, this.bean.getStaffRoleListboxPaging()
						.getActivePage() + 1, this.bean
						.getStaffRoleListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getStaffRoleListbox().setModel(dataList);
		this.bean.getStaffRoleListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	private void queryStaffRoleRelaForTree() throws Exception {
		Staff staffTemp = Staff.newInstance();
		if (null == qryStaffRoleRela) {
			qryStaffRoleRela = new StaffRoleRela();
		}
		if (null != staffRole && null != staffRole.getRoleId()) {
			qryStaffRoleRela.setRoleId(staffRole.getRoleId());
		}
		if (null != staff && null != staff.getStaffId()) {
			qryStaffRoleRela.setStaffId(staff.getStaffId());
			BeanUtils.copyProperties(staffTemp, staff);
		}

		qryStaffRoleRela.setQryStaff(staffTemp);
		ListboxUtils.clearListbox(bean.getStaffRoleListbox());
		PageInfo pageInfo = staffRoleManager.queryStaffRoleRela(
				qryStaffRoleRela, this.bean.getStaffRoleListboxPaging()
						.getActivePage() + 1, this.bean
						.getStaffRoleListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getStaffRoleListbox().setModel(dataList);
		this.bean.getStaffRoleListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onStaffRoleAdd() throws Exception {
		if (!PlatformUtil
				.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		Map map = new HashMap();
		map.put("opType", "add");
		if (null == staffRoleRela) {
			staffRoleRela = new StaffRoleRela();
		}
		if (null != staffRole) {
			staffRoleRela.setRoleId(staffRole.getRoleId());
		}
		if (null != staff) {
			staffRoleRela.setStaffId(staff.getStaffId());
		}
		map.put("staffRoleRela", staffRoleRela);
		//map.put("staffRoleList", staffRoleList);
		Window window = (Window) Executions.createComponents(
				"/pages/staff_role/staff_role_edit.zul", this.self, map);
		window.doModal();
		window.addEventListener("onOK", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getData() != null) {
					staff = (Staff) event.getData();
					queryStaffRoleRela();
				}
			}
		});
	}

	/**
	 * 文件上传 导入员工角色关系
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$uploadButton(ForwardEvent event) throws Exception {
		this.staffRoleRelaList.clear();
		this.delStaffRoleRelaList.clear();
		this.showDelStaffRoleRelaList.clear();
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
	 * 下载模版
	 */
	@SuppressWarnings("deprecation")
	public void onDownloadTemplate() {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "staff_role_template.xls";
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
			ZkUtil.showError("下载员工角色关系导入模版失败。", "系统提示");
		}
	}

	public void onStaffRoleDel() throws Exception {
		if (!PlatformUtil
				.checkPermissionDialog(this, ActionKeys.DATA_OPERATING))
			return;
		if (this.staffRoleRela != null && this.staffRoleRela != null) {
			ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {
						staffRoleManager.removeStaffRoleRela(staffRoleRela);
						queryStaffRoleRela();
						setButtonValid(true, false);
					}
				}
			});
		} else {
			ZkUtil.showError("请选择你要删除的记录。", "提示信息");
			return;
		}
	}

	/**
	 * 读取员工角色导入文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void readsUpLoadFile() throws Exception {

		String[][] objArray = FileUtil.readExcel(media, 1, 1);

		if (objArray == null || objArray.length == 0) {
			ZkUtil.showError("导入文件没有数据！", "错误信息");
			return;
		}

		int errCount = 0;
		List<String> infoList = new ArrayList<String>();
		List<Map<String, String>> staffRoleListMap = new ArrayList<Map<String, String>>();

		for (int i = 0; i < objArray.length; i++) {

			boolean impListExist = false;
			Staff staff = new Staff();
			StaffRoleRela staffRoleRela = new StaffRoleRela();

			String strOperation = objArray[i][0].trim().split("-")[1];

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
				case 0: // 操作类型，非空，且指定操作方式
					if (!StrUtil.isNullOrEmpty(str)) {
						if (strs.length == 1) {
							// 未知操作类型,FIELD_ERROR_STR = "格式不正确"
							errCount++;
							infoList.add(getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR));
							break;
						}

						// 给出的s必须是规定的add,del否则就是未知操作类型
						if (!"add".equals(strs[1]) && !"del".equals(strs[1])) {
							// 未知操作类型,FIELD_ERROR_STR = "格式不正确"
							errCount++;
							infoList.add(getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR));
						}
					} else {
						// 为空
						errCount++;
						infoList.add(getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					}
					break;
				case 1:// 员工姓名 -必填
					if (StrUtil.isNullOrEmpty(str)) {
						errCount++;
						infoList.add(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staff.setStaffName(str);
						staffRoleRela.setStaffName(str);
					}
					break;
				case 2:// 员工账号 --必填
					if (StrUtil.isNullOrEmpty(str)) {
						errCount++;
						infoList.add(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staff.setStaffAccount(str);
						staffRoleRela.setStaffRoleStaffAccount(str);
					}
					break;
				case 3:// 员工编码 --必填
						// 2016年12月8日 xiaof 新需求，这个改为非必填
					if (StrUtil.isNullOrEmpty(str)) {
						// errCount++;
						// infoList.add(this.getValidateMsg(i, j,
						// SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staff.setStaffCode(str);
						staffRoleRela.setStaffCode(str);
					}
					break;
				case 4:// 员工角色 --必填
					if (null != strs && strs.length > 1
							&& IdcardValidator.isDigital(strs[1])
							&& staffRoleList.contains(Long.parseLong(strs[1]))) {
						staffRoleRela.setRoleId(Long.parseLong(strs[1]));
						staffRoleRela.setStaffRoleName(strs[0]);
					} else {
						errCount++;
						infoList.add(this.getValidateMsg(i, j,
								SffOrPtyCtants.FIELD_ERROR));
					}
					break;
				case 5:// 会话保持数 --选填
					if (StrUtil.isNullOrEmpty(str)) {
					} else {
						if (IdcardValidator.isDigital(str)) {
							staffRoleRela.setStaffAttrValue(str);
						} else {
							errCount++;
							infoList.add(this
									.getValidateMsg(
											i,
											j,
											SffOrPtyCtants.FIELD_ERROR_SESSION_HOLDING_NUBER));
						}
					}
					break;
				default:
					break;
				}
			}

			/*
			 * if (staffRoleRelaList != null && staffRoleRelaList.size() > 0) {
			 * for (StaffRoleRela staffRoleRelaCache : staffRoleRelaList) { if
			 * (!StrUtil.isNullOrEmpty(staffRoleRela.getStaffCode()) &&
			 * !StrUtil.isNullOrEmpty(staffRoleRela .getStaffRoleStaffAccount())
			 * && !StrUtil .isNullOrEmpty(staffRoleRela.getRoleId())) { if
			 * (staffRoleRela.getStaffCode().equals(
			 * staffRoleRelaCache.getStaffCode()) &&
			 * staffRoleRela.getStaffRoleStaffAccount()
			 * .equals(staffRoleRelaCache .getStaffRoleStaffAccount()) &&
			 * staffRoleRela.getRoleId().equals(
			 * staffRoleRelaCache.getRoleId())) { errCount++;
			 * infoList.add(this.getValidateMsg(i, 4,
			 * SffOrPtyCtants.FIELD_REPEAT)); } } } }
			 */

			List<Staff> staffList = staffManager.queryStaffListByStaff(staff);
			if (staffList != null && staffList.size() > 0) {
				staffRoleRela.setStaffId(staffList.get(0).getStaffId());
			} else {
				errCount++;
				infoList.add(this.getValidateMsg(i, 3,
						SffOrPtyCtants.FIELD_ERROR_OR_STAFF_NOT_EXIST));
			}

			StaffRoleRela staffRoleRelaExist = staffRoleManager
					.queryStaffRoleRela(staffRoleRela);

			if ("add".equals(strOperation)) {
				if (staffRoleRelaExist != null
						&& staffRoleRelaExist.getStaffRoleRelaId() != null) {
					errCount++;
					infoList.add(this.getValidateMsg(i, 3,
							SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_EXIST));
				}

				this.staffRoleRelaList.add(staffRoleRela);
			} else if ("del".equals(strOperation)) {
				if (staffRoleRelaExist == null
						|| staffRoleRelaExist.getStaffRoleRelaId() == null) {
					errCount++;
					infoList.add(this
							.getValidateMsg(
									i,
									3,
									SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_NOT_EXIST));
				}
				
				this.showDelStaffRoleRelaList.add(staffRoleRela);
				this.delStaffRoleRelaList.add(staffRoleRelaExist);
			}

		}

		HashSet<String> staffRoleIdSet = new HashSet<String>();

		for (StaffRoleRela s : staffRoleRelaList) {
			staffRoleIdSet.add(s.getStaffAccount()+s.getRoleId());
		}

		if (!(staffRoleIdSet.size() == staffRoleRelaList.size())) {
			errCount++;
			infoList.add(this.getValidateMsg(-1, 3,
					SffOrPtyCtants.FIELD_REPEAT));
		}

		if (infoList.size() > 0) {
			infoList.add("导入文件错误条数共：" + errCount + "条，请修改以上错误后再导入。");
			Map arg = new HashMap();
			arg.put("opType", "view");
			arg.put("infoList", infoList);
			Window win = (Window) Executions.createComponents(
					"/pages/staff/staff_import.zul", null, arg);
			win.doModal();
		} else {
			staffRoleManager.saveStaffRoleRelaList(staffRoleRelaList);
			staffRoleManager.removeStaffRoleRelaList(delStaffRoleRelaList);
			Map arg = new HashMap();
			arg.put("opType", "view");
			arg.put("staffRoleRelaList", staffRoleRelaList);
			arg.put("showDelStaffRoleRelaList", showDelStaffRoleRelaList);
			Window win = (Window) Executions.createComponents(
					"/pages/staff/staff_role_import_result.zul", null, arg);
			win.doModal();
		}

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
		} else if (SffOrPtyCtants.FIELD_ERROR_SESSION_HOLDING_NUBER
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_SESSION_HOLDING_NUBER_STR
					+ "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_EXIST
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_EXIST_STR
					+ "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_NOT_EXIST
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_STAFF_ROLE_RELA_NOT_EXIST_STR
					+ "的信息； ";
		} else {
			if (!StrUtil.isNullOrEmpty(validateType)) {
				return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
						+ validateType;
			}
		}
		return "";
	}

	private void setButtonValid(final Boolean canAdd, final Boolean canDelete) {
		if (canAdd != null) {
			this.bean.getAddStaffRoleButton().setDisabled(!canAdd);
		}
		this.bean.getDelStaffRoleButton().setDisabled(!canDelete);
	}

	/**
	 * 设置页面坐标
	 * 
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		boolean canUpload = false;
		boolean canDownload = false;
		boolean canDel = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpload = true;
			canDownload = true;
			canDel = true;
		} else if (RolePermissionUtil.isAllPermission(PlatformUtil
				.getCurrentUserId())) {
			canAdd = true;
			canUpload = true;
			canDownload = true;
			canDel = true;
		} else if (RolePermissionUtil.isLocalAgencyAdmin(PlatformUtil
				.getCurrentUserId())) {
			canAdd = true;
			canUpload = false;
			canDownload = false;
			canDel = true;
		} /*
		 * else { if (page.equals("staffRolePage")) { if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_ADD)) { canAdd = true; } if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_UP_LOAD)) { canUpload = true; } if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_DOWN_LOAD)) { canDownload = true; } if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_DEL)) { canDel = true; } } else if
		 * (page.equals("staffPage")) { if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_ADD)) { canAdd = true; } if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_UP_LOAD)) { canUpload = true; } if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_DOWN_LOAD)) { canDownload = true; } if
		 * (PlatformUtil.checkPermission(getThemeDisplay(), getPortletId(),
		 * ActionKeys.STAFF_ROLE_RELA_DEL)) { canDel = true; } } }
		 */
		this.bean.getAddStaffRoleButton().setVisible(canAdd);
		this.bean.getUploadButton().setVisible(canUpload);
		this.bean.getDownloadButton().setVisible(canDownload);
		this.bean.getDelStaffRoleButton().setVisible(canDel);
	}
}
