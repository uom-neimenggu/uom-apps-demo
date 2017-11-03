package cn.ffcs.uom.staff.component;

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
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationLevelManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.gridUnit.manager.GridUnitManager;
import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.gridUnit.model.GridUnitRef;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.component.bean.StaffOrgTranListboxExtBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffOrgTranTemp;
import cn.ffcs.uom.staff.model.StaffOrgTranTempHis;
import cn.ffcs.uom.staff.model.StaffOrganizationTran;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;

/**
 * 员工组织业务关系 .
 */
public class StaffOrgTranListboxExt extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private StaffOrgTranListboxExtBean bean = new StaffOrgTranListboxExtBean();

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private GridUnitManager gridUnitManager = (GridUnitManager) ApplicationContextUtil
			.getBean("gridUnitManager");

	private AroleOrganizationLevelManager aroleOrganizationLevelManager = (AroleOrganizationLevelManager) ApplicationContextUtil
			.getBean("aroleOrganizationLevelManager");

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
	 * 选中的员工组织业务关系
	 */
	@Getter
	@Setter
	private StaffOrganizationTran staffOrgTran;

	/**
	 * 查询
	 */
	@Getter
	@Setter
	private StaffOrganizationTran queryStaffOrganizationTran;

	/**
	 * 查询
	 */
	@Getter
	@Setter
	private Staff queryStaff;

	/**
	 * 组织树中当前选择的organization
	 */
	@Getter
	@Setter
	private Organization organization;
	/**
	 * 组织关系中当前选择的gridUnitRef
	 */
	@Getter
	@Setter
	private GridUnitRef gridUnitRef;

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

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public StaffOrgTranListboxExt() {
		Executions.createComponents(
				SffOrPtyCtants.ZUL_STAFF_ORG_TRAN_LISTBOX_EXT, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		setStaffOrgTrantButtonValid(false, false, false);
		/**
		 * 选择组织响应事件
		 * */
		this.addEventListener(
				OrganizationConstant.ON_STAFF_ORGANIZATION_TRAN_QUERY,
				new EventListener() {
					public void onEvent(Event event) throws Exception {

						StaffOrganizationTran staffOrganizationTran = (StaffOrganizationTran) event
								.getData();

						if (!StrUtil.isNullOrEmpty(staffOrganizationTran)) {
							if (!StrUtil.isEmpty(variableOrgTreeTabName)
									&& variableOrgTreeTabName
											.equals("gridUnitTreeTab")) {

							} else {
								organization = new Organization();
								organization.setOrgId(staffOrganizationTran
										.getOrgId());
							}
						}

						if (!StrUtil.isEmpty(variableOrgTreeTabName)) {
							setOrgTreeTabName(variableOrgTreeTabName);
						}

						if (!StrUtil.isEmpty(variablePagePosition)) {
							setPagePosition(variablePagePosition);
						}

						queryStaffOrganizationTranHandle(staffOrganizationTran);

					}
				});

		this.addForward(SffOrPtyCtants.ON_STAFF_ORG_TRAN_SELECT, this,
				SffOrPtyCtants.ON_STAFF_ORG_TRAN_SELECT_RES);
		this.addForward(SffOrPtyCtants.ON_CLEAN_STAFFORG_TRAN, this,
				SffOrPtyCtants.ON_CLEAN_STAFFORG_TRAN_RES);
		//相应组织树页面点击组织查询员工组织关系
        this.addForward(OrganizationConstant.ON_SELECT_ORG_STAFF_ORGANIZATION_TRAN_QUERY, this,
            SffOrPtyCtants.ON_ORGANIZATION_SELECT_RES);
		/**
		 * 组织列表查询事件响应，清除列表
		 */
		this.addForward(OrganizationConstant.ON_ORGANIZATION_QUERY, this,
				"onOrganiztionQueryResponse");
	}

	public void onStaffOrgTranSelectRequest() {
		if (bean.getStaffOrgTranListbox().getSelectedCount() > 0) {
			staffOrgTran = (StaffOrganizationTran) bean
					.getStaffOrgTranListbox().getSelectedItem().getValue();
			setStaffOrgTrantButtonValid(true, true, false);
		}
	}

	/**
	 * 清空选中的员工. .
	 * 
	 * @throws Exception
	 * @author zhulitao
	 */
	public void onCleaningStaffOrgTran() throws Exception {
		ListboxUtils.clearListbox(bean.getStaffOrgTranListbox());
	}

	/**
	 * 查询员工组织业务关系列表的响应处理.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void queryStaffOrganizationTranHandle(
			StaffOrganizationTran staffOrganizationTran) throws Exception {
		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");
		this.queryStaffOrganizationTran = staffOrganizationTran;
		this.queryStaffOrgTranListboxForPaging();
	}

	/**
	 * 分页显示 .
	 * 
	 */
	public void onStaffOrgTranListboxPaging() {
	    //如果是组织树的查询的话，那么分页就走按照组织查询，否则按照员工查询
        if (organization != null) {
            queryStaffOrgTranByOrganizationListboxForPaging();
        } else {
            queryStaffOrgTranListboxForPaging();
        }
	}
	
	/**
	 * 根据选中的组织，查询组织与员工的对应的员工组织业务关系
	 * .
	 * 
	 * @author xiaof
	 * 2017年3月3日 xiaof
	 */
    public void queryStaffOrgTranByOrganizationListboxForPaging() {
        // 根据员工与组织关系，查询对应的员工组织业务关系
        // StaffOrganization staffOrganization = new StaffOrganization();
        // staffOrganization.setOrgId(organization.getId());
        if (organization != null) {
            // 选中组织不为空
            // 获取分页组件
            Paging lPaging = bean.getStaffOrgTranListboxPaging();
            // 查询分页数据
            PageInfo pageInfo = staffManager.queryPageInfoStaffOrgTran(organization, null,
                lPaging.getActivePage() + 1, lPaging.getPageSize());
            // 获取页面展示数据模型
            ListModel list = new BindingListModelList(pageInfo.getDataList(), true);
            // 设置数据进入页面组件
            bean.getStaffOrgTranListbox().setModel(list);
            // 设置分页，页数，总量等信息
            bean.getStaffOrgTranListboxPaging().setTotalSize(pageInfo.getTotalCount());
        } else {
            // 清空信息
            ListboxUtils.clearListbox(bean.getStaffOrgTranListbox());
        }
    }

	public void queryStaffOrgTranListboxForPaging() {

		try {

			queryStaff = new Staff();
			queryStaff.setStaffName(this.bean.getStaffName().getValue());
			queryStaff.setStaffAccount(this.bean.getStaffAccount().getValue());
			setStaffOrgTrantButtonValid(true, false, false);

			if (this.queryStaffOrganizationTran != null) {
				Paging lPaging = bean.getStaffOrgTranListboxPaging();
				PageInfo pageInfo = staffManager.queryPageInfoStaffOrgTran(
						queryStaff, queryStaffOrganizationTran,
						lPaging.getActivePage() + 1, lPaging.getPageSize());
				ListModel list = new BindingListModelList(
						pageInfo.getDataList(), true);
				bean.getStaffOrgTranListbox().setModel(list);
				bean.getStaffOrgTranListboxPaging().setTotalSize(
						pageInfo.getTotalCount());
			} else {
				/**
				 * 组织树未选择组织添加清理操作
				 */
				ListboxUtils.clearListbox(bean.getStaffOrgTranListbox());
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
	public void onQueryStaffOrgTran() throws Exception {
		this.bean.getStaffOrgTranListboxPaging().setActivePage(0);
		this.queryStaffOrgTranListboxForPaging();

	}

	/**
	 * 重置按钮
	 * 
	 * @throws Exception
	 */
	public void onResetStaffOrgTran() throws Exception {

		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");

	}

	public void onSelectStaffOrgTranResponse(final ForwardEvent event) {
		staff = (Staff) event.getOrigin().getData();
		if (null != staff && !StrUtil.isNullOrEmpty(staff.getStaffId())) {
			queryStaffOrganizationTran = new StaffOrganizationTran();
			queryStaffOrganizationTran.setStaffId(staff.getStaffId());
			this.bean.getStaffName().setValue("");
			this.bean.getStaffAccount().setValue("");
			queryStaffOrgTranListboxForPaging();
		}
	}
	
    public void onSelectOrganizationResponse(final ForwardEvent event) {
        // 获取数据
        organization = (Organization) event.getOrigin().getData();
        // 判断数据不为空，创建新的查询数据对象
        if (organization != null && !StrUtil.isNullOrEmpty(organization.getOrgId())) {
//            queryStaffOrganizationTran = new StaffOrganizationTran();
//            queryStaffOrganizationTran.setOrgId(organization.getOrgId());
            this.bean.getStaffName().setValue("");
            this.bean.getStaffAccount().setValue("");
            this.bean.getStaffOrgTranListboxPaging().setPageSize(10);
            //这里根据组织id查询对应的员工组织关系
            queryStaffOrgTranByOrganizationListboxForPaging();
        } else {
            // 调用查询
            queryStaffOrgTranByOrganizationListboxForPaging();
        }
    }

	/**
	 * 新增 .
	 * 
	 * @author Wong 2013-6-8 Wong
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onStaffOrgTranAdd() throws PortalException, SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		openStaffOrgTranEditWin(SffOrPtyCtants.ADD);
	}

	/**
	 * 修改 .
	 * 
	 * @author zhulintao
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onStaffOrgTranEdit() throws PortalException, SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		openStaffOrgTranEditWin(SffOrPtyCtants.MOD);
	}

	private void openStaffOrgTranEditWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			arg.put("isOrgTreePage", isOrgTreePage);
			arg.put("variableOrgTreeTabName", variableOrgTreeTabName);
			arg.put("staffOrganizationTran", queryStaffOrganizationTran);

			Window win = (Window) Executions.createComponents(
					SffOrPtyCtants.STAFF_ORG_TRAN_EDIT_ZUL, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					setStaffOrgTrantButtonValid(true, false, false);
					if (event.getData() != null) {
						StaffOrganizationTran staffOrganizationTran = (StaffOrganizationTran) event
								.getData();
						if (staffOrganizationTran != null
								&& staffOrganizationTran.getStaffId() != null) {

							Staff staff = staffOrganizationTran.getStaff();

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
						}

						queryStaffOrgTranListboxForPaging();
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
	public void setStaffOrgTrantButtonValid(boolean add, boolean del,
			boolean update) {
		bean.getAddStaffOrgTranButton().setDisabled(!add);
		bean.getDelStaffOrgTranButton().setDisabled(!del);
	}

	/**
	 * 组织列表查询响应，清空列表
	 * 
	 * @throws Exception
	 */
	public void onOrganiztionQueryResponse() throws Exception {
		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");
		staff = null;
		queryStaffOrganizationTran = null;
		ListboxUtils.clearListbox(bean.getStaffOrgTranListbox());
		setStaffOrgTrantButtonValid(false, false, false);
	}

	public void onCleanStaffOrgTranRespons() {
		this.bean.getStaffName().setValue("");
		this.bean.getStaffAccount().setValue("");
		staff = null;
		queryStaffOrganizationTran = null;
		ListboxUtils.clearListbox(bean.getStaffOrgTranListbox());
		setStaffOrgTrantButtonValid(false, false, false);
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
	@SuppressWarnings("unchecked")
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
				case 2:// 员工姓名 -必填 改为非必填 2017年2月20日 by xiaof
					if (StrUtil.isNullOrEmpty(str)) {
//						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
//								SffOrPtyCtants.NULL_OR_EMPTY));
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
/*				case 4:// 员工编码 --必填 改为非必填  2017年2月20日 by xiaof
					if (StrUtil.isNullOrEmpty(str)) {
						
//						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
//								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staffOrgTranTemp.setStaffCode(str);
					}
					break;*/
				case 4:// 组织编码 --必填
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
				case 5:// 组织全称--必填
					if (StrUtil.isNullOrEmpty(str)) {
						staffOrgTranTemp.setErrMsg(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staffOrgTranTemp.setOrgFullName(str);
					}
					break;
				case 6:// 关联类型--新增时为必填，删除时为选填
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
				case 7:// 员工排序 --新增时为必填，删除时为选填
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

			if (staffOrgTranTempList != null && staffOrgTranTempList.size() > 0) {

				for (StaffOrgTranTemp staffOrgTranTempCache : staffOrgTranTempList) {

					if (!StrUtil.isEmpty(staffOrgTranTemp.getOperationType())
//							&& !StrUtil
//									.isEmpty(staffOrgTranTemp.getStaffCode())  //员工编码为非必填  2017年2月20日 xiaof
							&& !StrUtil.isEmpty(staffOrgTranTemp.getOrgCode())) {

						if (staffOrgTranTemp.getOperationType().equals(
								staffOrgTranTempCache.getOperationType())
//								&& staffOrgTranTemp.getStaffCode().equals(
//										staffOrgTranTempCache.getStaffCode())  //员工编码为非必填  2017年2月20日 xiaof
								&& staffOrgTranTemp.getOrgCode().equals(
										staffOrgTranTempCache.getOrgCode())) {
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
					//staffOrgTran.setRalaCd(staffOrgTranTempDb.getRalaCd());

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

	public void onStaffOrgTranDel() {
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
								setStaffOrgTrantButtonValid(true, false, false);
								staffManager.deleteStaffOrgTran(staffOrgTran);
								Messagebox.show("员工组织业务关系删除成功！");
								PubUtil.reDisplayListbox(
										bean.getStaffOrgTranListbox(),
										staffOrgTran, "del");
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
		boolean canDel = false;
		AroleOrganizationLevel aroleOrganizationLevel = null;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpload = true;
			canDownload = true;
			canDel = true;
		} else if ("organizationPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ORG_TRAN_ADD)) {
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
					ActionKeys.STAFF_ORG_TRAN_DEL)) {
				canDel = true;
			}
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_STAFF_ORG_TRAN_ADD)) {
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
					ActionKeys.STAFF_STAFF_ORG_TRAN_DEL)) {
				canDel = true;
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
						ActionKeys.MARKETING_UNIT_STAFF_ORG_TRAN_ADD)) {
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
						ActionKeys.MARKETING_UNIT_STAFF_ORG_TRAN_DEL)) {
					canDel = true;
				}
			}
		} else if ("newMarketingUnitPage".equals(page)) {
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
						ActionKeys.MARKETING_UNIT_STAFF_ORG_TRAN_ADD)) {
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
						ActionKeys.MARKETING_UNIT_STAFF_ORG_TRAN_DEL)) {
					canDel = true;
				}
			}
        } else if ("newSeventeenMarketingUnitPage".equals(page)) {
            aroleOrganizationLevel = new AroleOrganizationLevel();
            aroleOrganizationLevel.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
            aroleOrganizationLevel.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
            
            if (!StrUtil.isNullOrEmpty(organization)
                && aroleOrganizationLevelManager.aroleOrganizationLevelValid(
                    aroleOrganizationLevel, organization)) {
                if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                    ActionKeys.MARKETING_UNIT_STAFF_ORG_TRAN_ADD)) {
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
                    ActionKeys.MARKETING_UNIT_STAFF_ORG_TRAN_DEL)) {
                    canDel = true;
                }
            }
        } else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ORG_TRAN_ADD)) {
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
					ActionKeys.MARKETING_STAFF_ORG_TRAN_DEL)) {
				canDel = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ORG_TRAN_ADD)) {
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
					ActionKeys.COST_STAFF_ORG_TRAN_DEL)) {
				canDel = true;
			}
		}

		this.bean.getAddStaffOrgTranButton().setVisible(canAdd);
		this.bean.getUploadButton().setVisible(canUpload);
		this.bean.getDownloadButton().setVisible(canDownload);
		this.bean.getDelStaffOrgTranButton().setVisible(canDel);
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
		AroleOrganizationLevel aroleOrganizationLevel = null;
		int orgLevel;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			canUpload = true;
			canDownload = true;
			canDel = true;
		} else if (!StrUtil.isNullOrEmpty(orgTreeTabName)) {
			if ("marketingTab".equals(orgTreeTabName)) {

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
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_TRAN_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.STAFF_ORG_TRAN_UP_LOAD)) {
						canUpload = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.STAFF_ORG_TRAN_DOWN_LOAD)) {
						canDownload = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_TRAN_DEL)) {
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
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_TRAN_ADD)) {
						canAdd = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.STAFF_ORG_TRAN_UP_LOAD)) {
						canUpload = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.STAFF_ORG_TRAN_DOWN_LOAD)) {
						canDownload = true;
					}

					if (PlatformUtil.checkHasPermission(
							getPortletInfoProvider(),
							ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_TRAN_DEL)) {
						canDel = true;
					}

				}

            } else if ("newSeventeenMarketingTab".equals(orgTreeTabName)) {
                
                aroleOrganizationLevel = new AroleOrganizationLevel();
                aroleOrganizationLevel.setOrgId(OrganizationConstant.ROOT_NEW_SEVENTEEN_MARKETING_ORG_ID);
                aroleOrganizationLevel
                    .setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE_0404);
                
                if (!StrUtil.isNullOrEmpty(organization)
                    && aroleOrganizationLevelManager.aroleOrganizationLevelValid(
                        aroleOrganizationLevel, organization)) {
                    
                    if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
                        ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_TRAN_ADD)) {
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
                        ActionKeys.ORG_TREE_MARKETING_STAFF_ORG_TRAN_DEL)) {
                        canDel = true;
                    }
                    
                }
                
            } else if ("gridUnitTreeTab".equals(orgTreeTabName)) {

				aroleOrganizationLevel = new AroleOrganizationLevel();
				aroleOrganizationLevel
						.setOrgId(OrganizationConstant.ROOT_MARKETING_ORG_ID);
				aroleOrganizationLevel
						.setRelaCd(OrganizationConstant.RELA_CD_MARKETING_CONVERGENCE);

				if (!StrUtil.isNullOrEmpty(organization)) {

					orgLevel = organization
							.getOrganizationLevel(aroleOrganizationLevel
									.getRelaCd());

					if (orgLevel == 7) {// 全息网格单元管理树只允许第七层进行添加或删除员工组织业务关系

						if (PlatformUtil
								.checkHasPermission(
										getPortletInfoProvider(),
										ActionKeys.ORG_TREE_GRID_UNIT_STAFF_ORG_TRAN_ADD)) {
							canAdd = true;
						}

						if (PlatformUtil.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.STAFF_ORG_TRAN_UP_LOAD)) {
							canUpload = true;
						}

						if (PlatformUtil.checkHasPermission(
								getPortletInfoProvider(),
								ActionKeys.STAFF_ORG_TRAN_DOWN_LOAD)) {
							canDownload = true;
						}

						if (PlatformUtil
								.checkHasPermission(
										getPortletInfoProvider(),
										ActionKeys.ORG_TREE_GRID_UNIT_STAFF_ORG_TRAN_DEL)) {
							canDel = true;
						}

					}
				}

			}
		}
		this.bean.getAddStaffOrgTranButton().setVisible(canAdd);
		this.bean.getUploadButton().setVisible(canUpload);
		this.bean.getDownloadButton().setVisible(canDownload);
		this.bean.getDelStaffOrgTranButton().setVisible(canDel);
	}
}
