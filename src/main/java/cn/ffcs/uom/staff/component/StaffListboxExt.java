package cn.ffcs.uom.staff.component;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.group.manager.GroupManager;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.mail.constants.GroupMailConstant;
import cn.ffcs.uom.mail.manager.GroupMailManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.component.bean.StaffListboxExtBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.manager.StaffPositionManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;
import cn.ffcs.uom.staff.model.StaffPosition;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 员工管理显示列表控件 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-25
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffListboxExt extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

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

	@Getter
	@Setter
	private StaffListboxExtBean bean = new StaffListboxExtBean();

	private StaffOrganizationManager staffOrganizationManager = (StaffOrganizationManager) ApplicationContextUtil
			.getBean("staffOrganizationManager");

	private StaffPositionManager staffPositionManager = (StaffPositionManager) ApplicationContextUtil
			.getBean("staffPositionManager");

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	private GroupManager groupManager = (GroupManager) ApplicationContextUtil
			.getBean("groupManager");

	private GroupMailManager groupMailManager = (GroupMailManager) ApplicationContextUtil
			.getBean("groupMailManager");

	private OrganizationExtendAttrManager organizationExtendAttrManager = (OrganizationExtendAttrManager) ApplicationContextUtil
			.getBean("organizationExtendAttrManager");

	/**
	 * 日志服务队列
	 */
	private LogService logService = (LogService) ApplicationContextUtil
			.getBean("logService");

	/**
	 * zul.
	 */
	private final String zul = "/pages/staff/comp/staff_listbox_ext.zul";

	/**
	 * staff.
	 */
	private Staff staff;

	@Getter
	private List<Staff> staffs;

	/**
	 * 查询staff.
	 */
	private Staff qryStaff;

	/**
	 * 存放上传数据
	 */
	private List<Party> partyList = new ArrayList<Party>();

	private List<Staff> delStaffList = new ArrayList<Staff>();

	private List<Party> delPartyList = new ArrayList<Party>();

	private List<Party> updatePartyList = new ArrayList<Party>();
	/**
	 * 记录上传的工号
	 */
	private List<String> addStaffAccountList = new ArrayList<String>();
	private List<String> editStaffAccountList = new ArrayList<String>();
	private List<String> delStaffAccountList = new ArrayList<String>();
	
	

	/**
	 * 上传文件
	 */
	private Media media;

	/**
	 * 文件列数
	 */
	private static final int totalColumn = 21;

	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 性别列表
	 */
	private List<String> genderList = new ArrayList<String>();

	/**
	 * 用工性质列表
	 */
	private List<String> workPropList = new ArrayList<String>();
	/**
	 * 人员属性列表
	 */
	private List<String> staffPropertyList = new ArrayList<String>();
	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;
	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;

	@Getter
	@Setter
	private Boolean isCustmsListbox = false;
	@Getter
	@Setter
	private Boolean isMarketingListbox = false;
	@Getter
	@Setter
	private Boolean isCostListbox = false;

	/**
	 * 是否是绑定框【默认非绑定框】
	 */
	@Getter
	@Setter
	private Boolean isBandbox = false;

	public StaffListboxExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_QUERY, this,
				SffOrPtyCtants.ON_STAFF_QUERY_RESPONSE);
		this.addForward(SffOrPtyCtants.ON_AGENT_STAFF_LISTBOX_PAGE, this,
				"onAgentStaffListBoxPageResponse");
		this.addForward(SffOrPtyCtants.ON_IBE_STAFF_LISTBOX_PAGE, this,
				"onIbeStaffListBoxPageResponse");
		this.setStaffButtonValid(true, false, false, false, false, false,
				false, false);

		List<NodeVo> genderVo = UomClassProvider.getValuesList("Individual",
				"gender");
		if (null != genderVo && genderVo.size() > 0) {
			for (NodeVo nodeVo : genderVo) {
				String id = nodeVo.getId();
				genderList.add(id);
			}
		}

		List<NodeVo> workPropVo = UomClassProvider.getValuesList("Staff",
				"workProp");
		if (null != workPropVo && workPropVo.size() > 0) {
			for (NodeVo nodeVo : workPropVo) {
				String id = nodeVo.getId();
				workPropList.add(id);
			}
		}

		List<NodeVo> staffPropVo = UomClassProvider.getValuesList("Staff",
				"staffProperty");
		if (null != staffPropVo && staffPropVo.size() > 0) {
			for (NodeVo nodeVo : staffPropVo) {
				String id = nodeVo.getId();
				staffPropertyList.add(id);
			}
		}
	}

	public void onCreate() throws Exception {
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				permissionOrganizationList = new ArrayList<Organization>();
				Organization rootParentOrg = new Organization();
				rootParentOrg
						.setOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
				permissionOrganizationList.add(rootParentOrg);
				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			} else {
				permissionOrganizationList = PermissionUtil
						.getPermissionOrganizationList(PlatformUtil
								.getCurrentUser().getRoleIds());
				permissionTelcomRegion = PermissionUtil
						.getPermissionTelcomRegion(PlatformUtil
								.getCurrentUser().getRoleIds());
			}
		}
		// 去除默认查询 if (!isBandbox)
		// 去除默认查询 onStaffQuery();
		if(isBandbox) {
			this.bean.getStaffListbox().setHeight("180px");
		}
	}

	public void onQueryStaffResponse(final ForwardEvent event) throws Exception {
		this.bean.getStaffListboxPaging().setActivePage(0);
		qryStaff = (Staff) event.getOrigin().getData();
		if (!isBandbox)
			this.queryStaff();
		if (this.bean.getStaffListbox().getItemCount() == 0) {
			this.setStaffButtonValid(true, false, false, false, false, false,
					false, false);
		}
	}

	public void onAgentStaffListBoxPageResponse(final ForwardEvent event)
			throws Exception {
		boolean isAgentTab = (Boolean) event.getOrigin().getData();
		this.isAgentTab = isAgentTab;
		if (isAgentTab) {
			// qryStaff = Staff.newInstance();
			// qryStaff.setWorkProp(SffOrPtyCtants.WORKPROP_W_AGENT);
			// this.queryStaff();
			// if (this.bean.getStaffListbox().getItemCount() == 0) {
			// this.setStaffButtonValid(true,false, false, false, false, false,
			// false);
			// }
			if (!isBandbox)
				this.onStaffQuery();
		}
	}

	public void onIbeStaffListBoxPageResponse(final ForwardEvent event)
			throws Exception {
		boolean isIbeTab = (Boolean) event.getOrigin().getData();
		this.isIbeTab = isIbeTab;
		if (isIbeTab) {
			// qryStaff = Staff.newInstance();
			// qryStaff.setWorkProp(SffOrPtyCtants.WORKPROP_W_AGENT);
			// this.queryStaff();
			// if (this.bean.getStaffListbox().getItemCount() == 0) {
			// this.setStaffButtonValid(true,false, false, false, false, false,
			// false);
			// }
			if (!isBandbox)
				this.onStaffQuery();
		}
	}

	/**
	 * 员工选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffSelectRequest() throws Exception {
		if (bean.getStaffListbox().getSelectedCount() > 0) {
			staff = (Staff) bean.getStaffListbox().getSelectedItem().getValue();
			this.setStaffButtonValid(true, true, true, true, true, true, true,
					true);
			Events.postEvent(SffOrPtyCtants.ON_STAFF_SELECT, this, staff);
		}
		if (bean.getStaffListbox().getSelectedCount() == 0) {
			Events.postEvent(SffOrPtyCtants.ON_STAFF_NOT_SELECT, this, null);
		}
	}

	/**
	 * 设置员工按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canUpdate
	 *            更新按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 * @param canSelect
	 *            选择按钮
	 */
	private void setStaffButtonValid(final Boolean canAdd,
			final Boolean canUpdate, final Boolean blView,
			final Boolean canEdit, final Boolean canIndividualEdit,
			final Boolean canDelete, final Boolean canSelect,
			final boolean canRestPwd) {
		if (canAdd != null) {
			bean.getAddStaffButton().setDisabled(!canAdd);
		}
		bean.getUpdateStaffButton().setDisabled(!canUpdate);
		bean.getEditStaffButton().setDisabled(!canEdit);
		bean.getEditIndividualButton().setDisabled(!canIndividualEdit);
		bean.getDelStaffButton().setDisabled(!canDelete);
		bean.getSelectStaffButton().setDisabled(!canSelect);
		// bean.getResetPwdButton().setDisabled(!canRestPwd);
	}

	/**
	 * 查询实体 .
	 * 
	 * @author Wong 2013-5-27 Wong
	 */
	public PageInfo queryStaff() {
		PageInfo pageInfo = null;
		if (this.qryStaff != null) {
			pageInfo = staffManager.forQuertyStaff(qryStaff, bean
					.getStaffListboxPaging().getActivePage() + 1, bean
					.getStaffListboxPaging().getPageSize());

			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getStaffListbox().setModel(dataList);
			bean.getStaffListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		Events.postEvent(SffOrPtyCtants.ON_STAFF_MANAGE_QUERY, this, null);
		this.setStaffButtonValid(true, false, false, false, false, false,
				false, false);
		return pageInfo;
	}

	public void onPageSizeSelect() throws Exception {
		if (this.bean.getPageListbox() != null
				&& this.bean.getPageListbox().getSelectedItem().getValue() != null) {
			this.bean.getStaffListboxPaging().setActivePage(0);
			this.bean.getStaffListboxPaging().setPageSize(
					Integer.parseInt(this.bean.getPageListbox()
							.getSelectedItem().getValue().toString()));
		}
		this.onStaffQuery();
	}

	/**
	 * 分页 .
	 * 
	 * @author Wong 2013-6-4 Wong
	 */
	public void onStaffListboxPaging() {
		PageInfo pageInfo = this.queryStaff();
		/**
		 * 查询获取该页面的员工，保存到list里面，供staffBandboxExt使用
		 */
		if (this.qryStaff != null) {
			Events.postEvent(SffOrPtyCtants.ON_STAFF_EXT_PAGE_QUERY, this,
					pageInfo.getDataList());
		}
	}

	/**
	 * 新增员工 .
	 * 
	 * @author Wong 2013-5-25 Wong
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onStaffAdd() throws PortalException, SystemException {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		openStaffEditWin(SffOrPtyCtants.ADD);
	}

	/**
	 * 文件上传 导入员工
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$uploadButton(ForwardEvent event) throws Exception {
		this.partyList.clear();
		this.delPartyList.clear();
		this.delStaffList.clear();
		this.updatePartyList.clear();
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
			String fileName = "staff_template.xls";
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
			ZkUtil.showError("下载员工导入模版失败。", "系统提示");
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
		String partyCertificationUsedMax = UomClassProvider
				.getSystemConfig("partyCertificationUsedMax");
		String fieldErrorCertAlreadyUseStr = SffOrPtyCtants.FIELD_ERROR_CERT_ALREADY_USE_STR
				.replace("%", partyCertificationUsedMax);

		if (SffOrPtyCtants.NULL_OR_EMPTY.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.NULL_OR_EMPTY_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_REPEAT.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_REPEAT_STR + "的信息 ；";
		} else if (SffOrPtyCtants.LENGTH_LIMIT.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.LENGTH_LIMIT_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_ID_REALNAME.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_ID_REALNAME_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_NOT_EXIST.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_NOT_EXIST_STR + "的归属部门编码信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_OR_NOT_EXIST.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_OR_NOT_EXIST_STR + "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_OR_NOT_EXIST_CERT
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误：导入"
					+ SffOrPtyCtants.FIELD_ERROR_OR_NOT_EXIST_CERT_STR
					+ "的信息； ";
		} else if (SffOrPtyCtants.FIELD_ERROR_IMP_LIST_EXIST
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_IMP_LIST_EXIST_STR;
		} else if (SffOrPtyCtants.FIELD_ERROR_CERT_ALREADY_USE
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
					+ fieldErrorCertAlreadyUseStr;
		} else if (SffOrPtyCtants.FIELD_ERROR_CERT_ALREADY_USE_RELOAD
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_CERT_ALREADY_USE_RELOAD_STR;
		} else if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_NULL
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_NULL_STR;
		} else if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO
				.equals(validateType)) {
			return "文件第"
					+ (i + 2)
					+ "行，"
					+ "第"
					+ (j + 1)
					+ "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO_STR;
		} else if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE
				.equals(validateType)) {
			return "文件第"
					+ (i + 2)
					+ "行，"
					+ "第"
					+ (j + 1)
					+ "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE_STR;
		} else if (SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM
				.equals(validateType)) {
			return "文件第"
					+ (i + 2)
					+ "行，"
					+ "第"
					+ (j + 1)
					+ "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM_STR;
		} else if (SffOrPtyCtants.FIELD_ERROR_WORKPROP.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_WORKPROP_STR;
		} else if (SffOrPtyCtants.FIELD_ERROR_OR_STAFF_NOT_EXIST
				.equals(validateType)) {
			return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
					+ SffOrPtyCtants.FIELD_ERROR_OR_STAFF_NOT_EXIST_STR;
		} else {
			if (!StrUtil.isNullOrEmpty(validateType)) {
				return "文件第" + (i + 2) + "行，" + "第" + (j + 1) + "列，出现错误："
						+ validateType;
			}
		}
		return "";
	}

	/**
	 * 读取员工导入文件
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public void readsUpLoadFile() throws Exception {
		String[][] objArray = FileUtil.readExcel(media, 1, 1);
		if (objArray == null || objArray.length == 0) {
			ZkUtil.showError("导入文件没有数据！", "错误信息");
			return;
		}
		/**
		 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
		 */
		SysLog log = new SysLog();
		log.startLog(new Date(), SysLogConstrants.STAFF);

		int errCount = 0;
		List<String> infoList = new ArrayList<String>();
		List<Map<String, String>> partyCertListMap = new ArrayList<Map<String, String>>();
		List<Map<String, String>> grpUnEmailListMap = new ArrayList<Map<String, String>>();
		for (int i = 0; i < objArray.length; i++) {
			Long identityCardId = 1L;
			String partyName = null;
			boolean impListExist = false;
			Party editParty = Party.newInstance();
			Party party = Party.newInstance();
			PartyRole partyRole = PartyRole.newInstance();
			Staff staff = Staff.newInstance();
			Staff editStaff = Staff.newInstance();
			StaffAccount sa = new StaffAccount();
			Group group = new Group();
			/*
			 * 统一认证接入，系统上线前把生产数据库员工密码字段置为空，程序新增员工的时候，密码设置为空字符串。
			 */
			// sa.setStaffPassword(md5Util.getMD5(""));
			sa.setStaffPassword("");
			staff.setObjStaffAccount(sa);
			staff.setStaffCode(staffManager.gennerateStaffCode());
			Individual indivi = Individual.newInstance();
			Individual editIndivi = Individual.newInstance();
			PartyCertification pc = new PartyCertification();
			PartyContactInfo pci = PartyContactInfo.newInstance();
			PartyContactInfo editPci = PartyContactInfo.newInstance();
			Organization organization = Organization.newInstance();
			StaffOrganization staffOrganization = StaffOrganization
					.newInstance();
			party.setStaff(staff);
			party.setIndividual(indivi);
			party.setPartyCertification(pc);
			party.setPartyContactInfo(pci);
			party.setPartyRole(partyRole);
			party.setStaffOrganization(staffOrganization);
			party.setGroup(group);

			editParty.setStaff(editStaff);
			editParty.setIndividual(editIndivi);
			editParty.setPartyContactInfo(editPci);

			String strOperation = objArray[i][0].trim().split("-")[1];

			for (int j = 0, flag = 0; j < totalColumn && flag == 0; j++) {
				String str = "";

				if (null != objArray[i][j]) {
					str = objArray[i][j].trim();
				}

				String[] strs = null;

				if (!StrUtil.isNullOrEmpty(str)) {
					strs = str.split("-");
				}

				if ("del".equals(strOperation) && j > 2) {
					continue;
				}
				if ("edit".equals(strOperation) && j > 8) {
					continue;
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
						if (!"add".equals(strs[1]) && !"del".equals(strs[1])
								&& !"edit".equals(strs[1])) {
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
				case 1:// 变更原因 -- 必填
					if (StrUtil.isNullOrEmpty(str)) {
						errCount++;
						infoList.add(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						staff.setReason(str);
						pci.setReason(str);
						staffOrganization.setReason(str);
						if ("edit".equals(strOperation)) {
							editStaff.setReason(str);
							editPci.setReason(str);
						}
					}
					break;
				case 2:// 员工账号 --操作类型是del时必填
					if (StrUtil.isNullOrEmpty(str) && ("del".equals(strOperation) || "edit".equals(strOperation))) {
						errCount++;
						infoList.add(this.getValidateMsg(i, j,
								SffOrPtyCtants.NULL_OR_EMPTY));
					} else {
						sa.setStaffAccount(str);
						if ("edit".equals(strOperation)) {
							editStaff = staffManager.getStaffByStaffId(sa);
							if (editStaff != null) {
								if (editStaff.getPartyRoleId() != null) {
									PartyRole editPartyRole = staffManager
											.getPartyRole(editStaff
													.getPartyRoleId());
									if (editPartyRole != null
											&& editPartyRole.getPartyId() != null) {
										editParty = staffManager
												.getParty(editPartyRole
														.getPartyId());
										editIndivi = partyManager
												.getIndividual(editParty
														.getPartyId());
										editPci = partyManager
												.getDefaultPartyContactInfo(editParty
														.getPartyId());
										editParty.setStaff(editStaff);
										editParty.setIndividual(editIndivi);
										editParty.setPartyContactInfo(editPci);
									}

								}
							} else {
								errCount++;
								infoList.add(this
										.getValidateMsg(
												i,
												1,
												SffOrPtyCtants.FIELD_ERROR_OR_STAFF_NOT_EXIST));
								flag = 1;
							}
						}

					}
					break;
				case 3:// 姓名 -- 必填
					if (StrUtil.isNullOrEmpty(str)) {
						if (!"edit".equals(strOperation)) {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.NULL_OR_EMPTY));
						}
					} else {
						party.setPartyName(str);
						staff.setStaffName(str);
						pci.setContactName(str);
						pci.setContactType("0105");// 联系人类型
						pci.setHeadFlag("1");// 是首选联系人
						staffOrganization.setUserCode(staffOrganizationManager
								.getOrgUserCode());
						partyName = str;
						if ("edit".equals(strOperation)) {
							editParty.setPartyName(str);
							editStaff.setStaffName(str);
							editPci.setContactName(str);
						}
					}
					break;
				case 4:// 性别 -- 必填
					if ("edit".equals(strOperation)) {
						if (null != strs && strs.length > 1
								&& genderList.contains(strs[0])) {
							str = strs[0];
							editIndivi.setGender(str);
							editPci.setContactGender(str);
						}
					} else {
						if (null != strs && strs.length > 1
								&& genderList.contains(strs[0])) {
							str = strs[0];
							indivi.setGender(str);
							pci.setContactGender(str);
						} else {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR));
						}
					}
					break;
				case 5:// 人力工号
					if (StrUtil.isNullOrEmpty(strs)) {
						// staff.setStaffNbr(sa.getStaffAccount());
					} else {
						if ("无".equals(str)
								|| "null".equals(str)
								|| "NULL".equals(str)
								|| (!StrUtil.isNullOrEmpty(str) && str.length() < 6)) {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR));
						} else {

							staff.setStaffNbr(str);
							// 添加员工账号生成规则
							if (str.startsWith("34")) {
								sa.setStaffAccount(str.substring(2));
							} else if (str.startsWith("W34")
									|| str.startsWith("w34")) {
								sa.setStaffAccount(str.replace("W34", "W9"));
								sa.setStaffAccount(str.replace("w34", "W9"));
							} else {
								sa.setStaffAccount(str);
							}

						}
					}
					break;
				case 6:// 员工类别 -- 必填
					if (StrUtil.isNullOrEmpty(str)) {
						if (!"edit".equals(strOperation)) {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.NULL_OR_EMPTY));
						}
					} else {
						if ("内部员工".equals(str)) {
							partyRole.setRoleType("1210");
						} else {
							partyRole.setRoleType("1220");
						}
					}
					break;
				case 7:// 用工性质 -- 必填
					if ("edit".equals(strOperation)) {
						if (null != strs && strs.length > 1
								&& workPropList.contains(strs[0])) {
							str = strs[0];
							editStaff.setWorkProp(str);
						}
					} else {
						if (null != strs && strs.length > 1
								&& workPropList.contains(strs[0])) {
							str = strs[0];
							staff.setWorkProp(str);
						} else {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR));
						}
					}
					break;
				case 8:// 人员属性 -- 必填
					if ("edit".equals(strOperation)) {
						if (null != strs && strs.length > 1
								&& staffPropertyList.contains(strs[0])) {
							str = strs[0];
							editStaff.setStaffProperty(str);
						}
					} else {
						if (null != strs && strs.length > 1
								&& staffPropertyList.contains(strs[0])) {
							str = strs[0];
							staff.setStaffProperty(str);
						} else {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.NULL_OR_EMPTY));

						}
					}
					break;
				case 9:// 身份证 -- 必填
					if (StrUtil.isNullOrEmpty(str)) {// 证件不能为空
						if (!"edit".equals(strOperation)) {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.NULL_OR_EMPTY));
						}
					} else {
						if (IdcardValidator.isValidatedAllIdcard(str)) {

							if (CertUtil.checkIdCard(str, staff.getStaffName())) {
								pc.setIsRealName(PartyConstant.PARTY_CERTIFICATION_IS_REAL_NAME_Y);
								pc.setCertName(staff.getStaffName());
								for (Map<String, String> map : partyCertListMap) {
									if (map != null
											&& !StrUtil.isNullOrEmpty(map
													.get(str))
											&& !map.get(str).equals(partyName)) {
										impListExist = true;
									}
								}

								Map<String, String> map = new HashMap<String, String>();
								map.put(str, partyName);
								partyCertListMap.add(map);

								if (!impListExist) {// 不在导入列表中

									/*
									 * boolean certIsNotExist = partyManager
									 * .checkIsExistCertificate(
									 * PartyConstant.ATTR_VALUE_IDNO, str);
									 * 
									 * if (certIsNotExist && str.length() == 15)
									 * {
									 * 
									 * certIsNotExist = partyManager
									 * .checkIsExistCertificate(
									 * PartyConstant.ATTR_VALUE_IDNO,
									 * IdcardValidator
									 * .convertIdcarBy15bit(str)); }
									 * 
									 * if (!certIsNotExist && (partyManager
									 * .checkCertNumberAndParytNameIsExist( str,
									 * partyName) || partyManager
									 * .checkCertNumberAndParytNameIsExist(
									 * IdcardValidator
									 * .convertIdcarBy15bit(str), partyName))) {
									 * // 证件号存在且同名 // 或者 errCount++;
									 * infoList.add(this .getValidateMsg( i, j,
									 * SffOrPtyCtants
									 * .FIELD_ERROR_CERT_ALREADY_USE_RELOAD)); }
									 * else if (certIsNotExist) {// 证件号不存在
									 */
									boolean certIsNotExist = partyManager
											.checkIsExistCertificate(str);
									if (certIsNotExist) {
										// 证件号没有超过上限
										pc.setCertType(PartyConstant.ATTR_VALUE_IDNO);
										pc.setCertNumber(str);
										pc.setCertSort("1");
										pc.setIdentityCardId(identityCardId);
										indivi.setBirthday(CertUtil
												.getBirthFromCard(str));
									} else {
										infoList.add(this
												.getValidateMsg(
														i,
														j,
														SffOrPtyCtants.FIELD_ERROR_CERT_ALREADY_USE));
									}
									// 老的人力工号较正
									// String msg = partyManager
									// .checkStaffAccount(pc);

									/*
									 * String msg =
									 * partyManager.checkStaffAccountNew( pc,
									 * partyName);
									 * 
									 * if (!StrUtil.isNullOrEmpty(msg)) {
									 * 
									 * if (SffOrPtyCtants.
									 * FIELD_ERROR_OPERATE_HR_TABLE_01_NULL
									 * .equals(msg)) { errCount++;
									 * infoList.add(this .getValidateMsg( i, j,
									 * SffOrPtyCtants
									 * .FIELD_ERROR_OPERATE_HR_TABLE_01_NULL));
									 * }
									 * 
									 * if (SffOrPtyCtants.
									 * FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO
									 * .equals(msg)) { errCount++;
									 * infoList.add(this .getValidateMsg( i, j,
									 * SffOrPtyCtants
									 * .FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO
									 * )); }
									 * 
									 * if (SffOrPtyCtants.
									 * FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE
									 * .equals(msg)) { errCount++;
									 * infoList.add(this .getValidateMsg( i, j,
									 * SffOrPtyCtants
									 * .FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE
									 * )); }
									 * 
									 * if (SffOrPtyCtants.
									 * FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM
									 * .equals(msg)) { errCount++;
									 * infoList.add(this .getValidateMsg( i, j,
									 * SffOrPtyCtants
									 * .FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM
									 * )); } }
									 */

									/*
									 * } else {// 证件已使用 errCount++;
									 * infoList.add(this .getValidateMsg( i, j,
									 * SffOrPtyCtants
									 * .FIELD_ERROR_CERT_ALREADY_USE)); }
									 */
								} else {// 证件号已经在列表中存在且不同员工使用
									errCount++;
									infoList.add(this
											.getValidateMsg(
													i,
													j,
													SffOrPtyCtants.FIELD_ERROR_IMP_LIST_EXIST));
								}

							} else {
								errCount++;
								infoList.add(this.getValidateMsg(i, j,
										SffOrPtyCtants.FIELD_ERROR_ID_REALNAME));
								break;
							}
						} else {// 证件格式不正确
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR));
						}

						/*
						 * if
						 * (!partyManager.checkIsExistCertificate(PartyConstant
						 * .ATTR_VALUE_IDNO, str)) { // 证件已使用 errCount++;
						 * infoList.add(this.getValidateMsg(i, j,
						 * SffOrPtyCtants.FIELD_REPEAT)); } else {
						 * if(str.length() == PartyConstant.ATTR_VALUE_IDNO15 ||
						 * str.length() == PartyConstant.ATTR_VALUE_IDNO18){
						 * pc.setCertType(PartyConstant.ATTR_VALUE_IDNO);
						 * pc.setCertNumber(str); pc.setCertSort("1");
						 * indivi.setBirthday(CertUtil.getBirthFromCard(str));
						 * }else{ errCount++;
						 * infoList.add(this.getValidateMsg(i, j,
						 * SffOrPtyCtants.FIELD_ERROR)); } }
						 */
					}
					break;
				case 10:// 身份证地址
					pc.setCertAddress(str);
					break;
				case 11:// 联系地址
					pci.setContactAddress(str);
					break;
				case 12:// 移动电话 -- 必填
					if (StrUtil.isNullOrEmpty(str)) {
						if (!"edit".equals(strOperation)) {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.NULL_OR_EMPTY));
						}
					} else {
						if (!StrUtil.checkTelephoneNumber(str)) {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR_OR_NOT_EXIST));
						} else {
							pci.setMobilePhone(str);
						}
					}
					break;
				case 13:// 备用移动电话
					if (!StrUtil.isNullOrEmpty(str)) {
						if (!StrUtil.checkTelephoneNumber(str)) {
							errCount++;
							infoList.add(this.getValidateMsg(i, j,
									SffOrPtyCtants.FIELD_ERROR_OR_NOT_EXIST));
						} else {
							pci.setMobilePhoneSpare(str);
						}
					}
					break;
				case 14:// 电信内部邮箱
					pci.setInnerEmail(pci.getMobilePhone()
							+ "@anhuitelecom.com");
					break;
				// case 12:// 集团统一邮箱--当用工性质为内部-合同制或内部-派遣制时，必填
				// if (SffOrPtyCtants.WORKPROP_N_H.equals(staff.getWorkProp())
				// || SffOrPtyCtants.WORKPROP_N_P.equals(staff
				// .getWorkProp())) {
				// if (!StrUtil.isNullOrEmpty(str)) {
				// if (!InputFieldUtil.isGrpUnEmail(str)) {
				// errCount++;
				// infoList.add(this.getValidateMsg(i, j,
				// SffOrPtyCtants.FIELD_ERROR));
				// } else {
				// List<PartyContactInfo> partyContactInfoList = null;
				//
				// PartyContactInfo partyContactInfo = new PartyContactInfo();
				// partyContactInfo
				// .setHeadFlag(SffOrPtyCtants.HEADFLAG);
				// partyContactInfo.setGrpUnEmail(str);
				// partyContactInfoList = partyManager
				// .queryDefaultPartyContactInfo(partyContactInfo);
				// if (partyContactInfoList != null
				// && partyContactInfoList.size() > 0) {
				// errCount++;
				// infoList.add(this.getValidateMsg(i, j,
				// SffOrPtyCtants.FIELD_REPEAT));
				// } else {
				//
				// for (Map<String, String> map : grpUnEmailListMap) {
				// if (map != null
				// && !StrUtil.isNullOrEmpty(map
				// .get(str))
				// && map.get(str).equals(str)) {
				// grpUnEmailListExist = true;
				// }
				// }
				//
				// Map<String, String> map = new HashMap<String, String>();
				// map.put(str, str);
				// grpUnEmailListMap.add(map);
				//
				// if (grpUnEmailListExist) {
				// errCount++;
				// infoList.add(this.getValidateMsg(i, j,
				// SffOrPtyCtants.FIELD_REPEAT));
				// } else {
				// pci.setGrpUnEmail(str);
				// }
				// }
				// }
				// } else {
				// errCount++;
				// infoList.add(this.getValidateMsg(i, j,
				// SffOrPtyCtants.NULL_OR_EMPTY));
				// }
				// } else {
				// if (!StrUtil.isNullOrEmpty(str)) {
				// errCount++;
				// infoList.add(this.getValidateMsg(i, j,
				// SffOrPtyCtants.FIELD_ERROR_WORKPROP));
				// }
				// }
				// break;
				case 15:// 电子邮箱
					pci.setEmail(str);
					break;
				case 16:// QQ号
					pci.setQqNumber(str);
					break;
				case 17:// MSN号
					pci.setMsn(str);
					break;
				case 18:// MAC地址
					staff.setMac(str);
					break;
				case 19:// 归属部门编码
					organization.setOrgCode(str);
					Organization organ = organizationManager
							.queryOrganizationByOrgCode(organization);
					/*
					 * if (null == organ) { infoList.add(this.getValidateMsg(i,
					 * j, SffOrPtyCtants.FIELD_NOT_EXIST)); } else
					 */
					if (organ != null) {
						// 添加员工组织关系规则校验 zhulintao
						if (true) {
							String msg = staffOrganizationManager
									.doStaffOrgRelRule(staff, null, organ);
							if (!StrUtil.isEmpty(msg)) {
								infoList.add(this.getValidateMsg(i, j, msg));
							}
						}
						organization = organ;
						staffOrganization.setOrgId(organ.getOrgId());
					}
					break;
				case 20:// 归属部门全称
					organization.setOrgFullName(str);
					break;
				default:
					break;
				}
			}

			if ("add".equals(strOperation)) {
				if (!StrUtil.isEmpty(pc.getCertNumber())) {

					group.setUserName(partyName);
					group.setCtIdentityNumber(pc.getCertNumber());

					List<Group> groupList = null;

					groupList = groupManager.queryGroupList(group);

					if ((groupList == null || groupList.size() == 0)
							&& pc.getCertNumber().length() == 15) {

						group.setCtIdentityNumber(IdcardValidator
								.convertIdcarBy15bit(pc.getCertNumber()));

						groupList = groupManager.queryGroupList(group);

					}

					if (groupList != null && groupList.size() > 0) {

						group = groupList.get(0);

						String staffNbr = group.getCtHrUserCode();

						if (!StrUtil.isNullOrEmpty(staffNbr)) {

							staffNbr = staffNbr.trim();
							sa.setStaffAccount(staffNbr);

							if (staffNbr.startsWith("34")) {

								String staffAccStr = staffNbr.substring(2,
										staffNbr.length());

								sa.setStaffAccount(staffAccStr);

							} else if (staffNbr.startsWith("W34")
									|| staffNbr.startsWith("w34")) {

								String staffAccStr = "W9"
										+ staffNbr.substring(3,
												staffNbr.length());

								sa.setStaffAccount(staffAccStr);

							}

						}

						staff.setStaffNbr(staffNbr);

					}
				}
				// 设置员工工号，若有输入判断是否重复，若有则自动生成
				if (!StrUtil.isNullOrEmpty(sa.getStaffAccount())) {
					// 输入的员工工号在数据库存在，自动生成
					if (staffManager.getStaffAccountList(sa).size() > 0) {// 修改员工账号是否存在的判断逻辑
						staff.setStaffNbr(staffManager
								.gennerateStaffNumber(staff.getWorkProp()));
						sa.setStaffAccount(staffManager
								.gennerateStaffAccount(staff));
					}
				} else {
					// 没有输入员工工号，自动生成
					staff.setStaffNbr(staffManager.gennerateStaffNumber(staff
							.getWorkProp()));
					sa.setStaffAccount(staffManager
							.gennerateStaffAccount(staff));
				}
				// 自动生成集团统一邮箱
				pci.setGrpUnEmail(partyManager.generateGrpUnEmail(party,
						grpUnEmailListMap, null));
				Map<String, String> map = new HashMap<String, String>();
				map.put(pci.getGrpUnEmail(), pci.getGrpUnEmail());
				grpUnEmailListMap.add(map);
				OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
				organizationExtendAttr.setOrgId(organization.getOrgId());
				organizationExtendAttr
						.setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);
				organizationExtendAttr = organizationExtendAttrManager
						.queryOrganizationExtendAttr(organizationExtendAttr);
				if (organization != null
						&& staff != null
						&& ((organizationExtendAttr != null && !StrUtil
								.isEmpty(organizationExtendAttr
										.getOrgAttrValue())) || organization
								.getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER) != null)) {
					if (!StrUtil.isNullOrEmpty(staff.getWorkProp())
							&& (staff.getWorkProp().startsWith(
									SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
									.getWorkProp().startsWith(
											SffOrPtyCtants.WORKPROP_N_P_PRE))
							&& organization.isUploadGroupMail()
							&& !StrUtil.isEmpty(pci.getGrpUnEmail())) {

						boolean groupMailInterFaceSwitch = UomClassProvider
								.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

						if (groupMailInterFaceSwitch) {

							String msg = groupMailManager.groupMailPackageInfo(
									GroupMailConstant.GROUP_MAIL_BIZ_ID_2,
									party, organization);

							if (!StrUtil.isEmpty(msg)) {
								if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_TRUE
										.equals(msg)) {
									infoList.add(this.getValidateMsg(i, 0,
											"该员工生成的集团统一邮箱账号存在"));
								} else if (GroupMailConstant.GROUP_MAIL_BIZ_ID_2_FALSE
										.equals(msg)) {
									msg = groupMailManager
											.groupMailPackageInfo(
													GroupMailConstant.GROUP_MAIL_BIZ_ID_13,
													party, organization);
									if (!StrUtil.isEmpty(msg)) {
										infoList.add(this.getValidateMsg(i, 0,
												msg));
									}
								}

							}
						}
					}

				}
				this.partyList.add(party);
			} else if ("del".equals(strOperation)) {
				Party delParty = null;
				Staff delStaff = staffManager.getStaffByStaffId(sa);
				if (delStaff != null) {
					if (delStaff.getPartyRoleId() != null) {
						PartyRole delPartyRole = staffManager
								.getPartyRole(delStaff.getPartyRoleId());
						if (delPartyRole != null
								&& delPartyRole.getPartyId() != null) {
							delParty = staffManager.getParty(delPartyRole
									.getPartyId());
						}
					}
				} else {
					errCount++;
					infoList.add(this.getValidateMsg(i, 1,
							SffOrPtyCtants.FIELD_ERROR_OR_STAFF_NOT_EXIST));
				}
				if (this.delStaffAccountList.contains(sa.getStaffAccount())) {
					errCount++;
					infoList.add("工号重复导入：" + sa.getStaffAccount());
				}
				this.delStaffAccountList.add(sa.getStaffAccount());
				
				this.delPartyList.add(delParty);
				this.delStaffList.add(delStaff);
			} else if ("edit".equals(strOperation)) {
				if (this.editStaffAccountList.contains(sa.getStaffAccount())) {
					errCount++;
					infoList.add("工号重复导入：" + sa.getStaffAccount());
				}
				this.editStaffAccountList.add(sa.getStaffAccount());
				this.updatePartyList.add(editParty);
			}

		}
		if (infoList.size() > 0) {
			infoList.add("导入文件错误条数共：" + errCount + "条，请修改以上错误后再导入。");
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", "view");
			arg.put("infoList", infoList);
			Window win = (Window) Executions.createComponents(
					"/pages/staff/staff_import.zul", null, arg);
			win.doModal();
		} else {
			if (partyList != null && partyList.size() > 0) {
				partyManager.addPartyStaffOrganizations(partyList);
			}
			if (updatePartyList != null && updatePartyList.size() > 0) {
				partyManager.updatePartyStaffOrganizations(updatePartyList);
			}
			if (delStaffList != null && delStaffList.size() > 0) {
				partyManager.delPartyStaffList(delPartyList, delStaffList);
			}
			
			
			/**
			 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
			 */
			Class clazz[] = { Staff.class, Party.class };
			log.endLog(logService, clazz, SysLogConstrants.IMPORT,
					SysLogConstrants.INFO, "员工添加记录日志");
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", "view");
			arg.put("partyList", partyList);
			arg.put("updatePartyList", updatePartyList);
			arg.put("delStaffList", delStaffList);
			Window win = (Window) Executions.createComponents(
					"/pages/staff/staff_import_result.zul", null, arg);
			win.doModal();
		}

	}

	/**
	 * 修改员工 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-28 Wong
	 */
	public void onStaffEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		openStaffEditWin(SffOrPtyCtants.MOD);
	}

	/**
	 * 修改个人信息.
	 * 
	 * @throws Exception
	 * @author 朱林涛 2016-01-28
	 */
	public void onIndividualEdit() throws Exception {
		openIndividualEditWin(SffOrPtyCtants.MOD_INDIVIDUAL);
	}

	/**
	 * 更新选择的员工.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onStaffUpdate() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("确定要更新员工吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (staff == null || staff.getStaffId() == null) {
						ZkUtil.showError("请选择你要更新的员工", "提示信息");
						return;
					} else {

						String batchNumber = OperateLog.gennerateBatchNumber();

						staff.setBatchNumber(batchNumber);
						staff.update();

						StaffAccount staffAccount = staff
								.getStaffAccountFromDB();
						if (staffAccount != null) {
							staffAccount.setBatchNumber(batchNumber);
							staffAccount.update();
						}

						List<StaffOrganization> staffOrganizationList = staff
								.getStaffOrganizationList();
						if (staffOrganizationList != null
								&& staffOrganizationList.size() > 0) {
							for (StaffOrganization staffOrg : staffOrganizationList) {
								staffOrg.setBatchNumber(batchNumber);
								staffOrg.update();
							}
						}

						StaffPosition staffPosition = new StaffPosition();
						staffPosition.setStaffId(staff.getStaffId());
						List<StaffPosition> staffPositionList = staffPositionManager
								.queryStaffPositionList(staffPosition);
						if (staffPositionList != null
								&& staffPositionList.size() > 0) {
							for (StaffPosition oldsStaffPosition : staffPositionList) {
								oldsStaffPosition.setBatchNumber(batchNumber);
								oldsStaffPosition.update();
							}
						}

						Long partyRoleId = staff.getPartyRoleId();

						if (!StrUtil.isNullOrEmpty(partyRoleId)) {

							PartyRole partyRole = partyManager
									.getPartyRole(partyRoleId);

							if (partyRole != null) {

								Long partyId = partyRole.getPartyId();

								partyRole.setBatchNumber(batchNumber);
								partyRole.update();

								Individual individual = partyManager
										.getIndividual(partyId);

								if (individual != null) {
									individual.setBatchNumber(batchNumber);
									individual.update();
								}

								Party party = partyManager.queryParty(partyId);
								if (party != null) {
									party.setBatchNumber(batchNumber);
									party.update();
								}

								List<PartyCertification> partyCertificationList = partyManager
										.getPartyCerfion(partyId);
								if (partyCertificationList != null
										&& partyCertificationList.size() > 0) {
									for (PartyCertification partyCertification : partyCertificationList) {
										partyCertification
												.setBatchNumber(batchNumber);
										partyCertification.update();
									}
								}

								List<PartyContactInfo> partyContactInfoList = partyManager
										.getPartyContInfo(partyId);
								if (partyContactInfoList != null
										&& partyContactInfoList.size() > 0) {
									for (PartyContactInfo partyContactInfo : partyContactInfoList) {
										partyContactInfo
												.setBatchNumber(batchNumber);
										partyContactInfo.update();
									}
								}

							}
						}

						queryStaff();

						Messagebox.show("更新员工成功！");

						if (!StrUtil.isNullOrEmpty(staff.getWorkProp())
								&& (staff.getWorkProp().startsWith(
										SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
										.getWorkProp()
										.startsWith(
												SffOrPtyCtants.WORKPROP_N_P_PRE))) {

							StaffOrganization staffOrganization = staff
									.getStaffOrganization();

							if (staffOrganization != null) {

								Organization org = staffOrganization
										.getOrganization();

								if (org != null && org.getOrgId() != null
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

															if (!StrUtil
																	.isEmpty(msg)) {
																ZkUtil.showError(
																		msg,
																		"提示信息");
																// return;
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
				}
			}
		});

	}

	public void onView() {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;

			openStaffEditWin(SffOrPtyCtants.VIEW);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除员工. .
	 * 
	 * @author Wong 2013-5-28 Wong
	 * @throws SystemException
	 * @throws PortalException
	 */
	public void onStaffDel() throws PortalException, SystemException {
		try {
			if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
					ActionKeys.DATA_OPERATING))
				return;

			Window window = (Window) Executions.createComponents(
					"/pages/common/del_reason_input.zul", this, null);
			window.doModal();
			window.addEventListener("onOK", new EventListener() {
				@SuppressWarnings("rawtypes")
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getData() != null) {
						String reason = (String) event.getData();
						staff.setReason(reason);
						setStaffButtonValid(true, false, false, false, false,
								false, false, false);
						/**
						 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
						 */
						SysLog log = new SysLog();
						log.startLog(new Date(), SysLogConstrants.STAFF);
						Party delParty = new Party();
						if (staff.getPartyRoleId() != null) {
							PartyRole partyRole = staffManager
									.getPartyRole(staff.getPartyRoleId());
							if (partyRole != null
									&& partyRole.getPartyId() != null) {
								delParty = staffManager.getParty(partyRole
										.getPartyId());
							}
						}
						staffManager.delStaff(staff);
						partyManager.delParty(delParty);
						Class clazz[] = { Staff.class };
						log.endLog(logService, clazz, SysLogConstrants.DEL,
								SysLogConstrants.INFO, "员工删除记录日志");
						Messagebox.show("员工删除成功！");

						if (!StrUtil.isNullOrEmpty(staff.getWorkProp())
								&& (staff.getWorkProp().startsWith(
										SffOrPtyCtants.WORKPROP_N_H_PRE) || staff
										.getWorkProp()
										.startsWith(
												SffOrPtyCtants.WORKPROP_N_P_PRE))) {
							if (staff.getPartyRoleId() != null) {
								PartyRole partyRole = staffManager
										.getPartyRole(staff.getPartyRoleId());
								if (partyRole != null
										&& partyRole.getPartyId() != null) {
									Party party = staffManager
											.getParty(partyRole.getPartyId());
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
															GroupMailConstant.GROUP_MAIL_BIZ_ID_5,
															party, null);
											if (!StrUtil.isEmpty(msg)) {
												ZkUtil.showError(msg, "提示信息");
												// return;
											}
										}
									}
								}
							}
						}

						PubUtil.reDisplayListbox(bean.getStaffListbox(), staff,
								"del");
						cleanTabs();
					} else {
						Messagebox.show("删除失败，删除原因为空");
					}
				}
			});

			/*
			 * Messagebox.show("您确定要删除吗？", "提示信息", Messagebox.OK |
			 * Messagebox.CANCEL, Messagebox.INFORMATION, new EventListener() {
			 * public void onEvent(Event event) throws Exception { Integer
			 * result = (Integer) event.getData(); if (result == Messagebox.OK)
			 * { setStaffButtonValid(true, false, false, false, false, false,
			 * false, false);
			 *//**
			 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
			 */
			/*
			 * SysLog log = new SysLog(); log.startLog(new Date(),
			 * SysLogConstrants.STAFF); Party delParty = new Party(); if
			 * (staff.getPartyRoleId() != null) { PartyRole partyRole =
			 * staffManager .getPartyRole(staff .getPartyRoleId()); if
			 * (partyRole != null && partyRole.getPartyId() != null) { delParty
			 * = staffManager .getParty(partyRole .getPartyId()); } }
			 * staffManager.delStaff(staff); partyManager.delParty(delParty);
			 * Class clazz[] = { Staff.class }; log.endLog(logService, clazz,
			 * SysLogConstrants.DEL, SysLogConstrants.INFO, "员工删除记录日志");
			 * Messagebox.show("员工删除成功！");
			 * 
			 * if (!StrUtil.isNullOrEmpty(staff.getWorkProp()) && (staff
			 * .getWorkProp() .startsWith( SffOrPtyCtants.WORKPROP_N_H_PRE) ||
			 * staff .getWorkProp() .startsWith(
			 * SffOrPtyCtants.WORKPROP_N_P_PRE))) { if (staff.getPartyRoleId()
			 * != null) { PartyRole partyRole = staffManager .getPartyRole(staff
			 * .getPartyRoleId()); if (partyRole != null &&
			 * partyRole.getPartyId() != null) { Party party = staffManager
			 * .getParty(partyRole .getPartyId()); PartyContactInfo
			 * partyContactInfo = partyManager
			 * .getDefaultPartyContactInfo(partyRole .getPartyId());
			 * 
			 * if (party != null && partyContactInfo != null && !StrUtil
			 * .isEmpty(partyContactInfo .getGrpUnEmail())) {
			 * party.setPartyContactInfo(partyContactInfo); boolean
			 * groupMailInterFaceSwitch = UomClassProvider
			 * .isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关 if
			 * (groupMailInterFaceSwitch) { String msg = groupMailManager
			 * .groupMailPackageInfo( GroupMailConstant.GROUP_MAIL_BIZ_ID_5,
			 * party, null); if (!StrUtil.isEmpty(msg)) { ZkUtil.showError(msg,
			 * "提示信息"); // return; } } } } } }
			 * 
			 * PubUtil.reDisplayListbox( bean.getStaffListbox(), staff, "del");
			 * cleanTabs(); } } });
			 */
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询员工. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onStaffQuery() {
		try {
			cleanTabs();
			Staff objSff = Staff.newInstance();
			PubUtil.fillPoFromBean(bean, objSff);
			StaffAccount sffAcc = new StaffAccount();
			PubUtil.fillPoFromBean(bean, sffAcc);
			objSff.setObjStaffAccount(sffAcc);
			objSff.setOrganizationRelations(bean
					.getOrganizationRelationTreeBandboxExt()
					.getOrganizationRelations());
			/**
			 * 在组织树界面的 “代理商管理界面－>组织员工”，“添加员工”只显示用工性质为“外部-代理商员工”的员工
			 */
			if (isAgentTab) {
				objSff.setWorkProp("'" + SffOrPtyCtants.WORKPROP_W_AGENT + "'");
			}
			/**
			 * 在组织树界面的 “内部经营实体管理界面－>组织员工”，“添加员工”只显示用工性质非为“外部-代理商员工”的员工
			 */
			if (isIbeTab) {
				objSff.setWorkProp(staffManager
						.getStaffWorkpropIbeStr(SffOrPtyCtants.WORKPROP_W_AGENT));
			}
			/**
			 * 营销管理-员工管理里面，显示特定性质的员工
			 */
			if (isCustmsListbox) {
				TreeStaffSftRule tssr = new TreeStaffSftRule();
				tssr.setOrgTreeId(OrganizationConstant.CUSTMS_TREE_ID);
				objSff.setWorkProp(staffManager.getStaffWorkpropStr(tssr));
			}
			/**
			 * 新营销管理-员工管理里面，显示特定性质的员工
			 */
			if (isMarketingListbox) {
				TreeStaffSftRule tssr = new TreeStaffSftRule();
				tssr.setOrgTreeId(OrganizationConstant.MARKETING_TREE_ID);
				objSff.setWorkProp(staffManager.getStaffWorkpropStr(tssr));
			}
			/**
			 * 成本树管理
			 */
			if (isCostListbox) {
				TreeStaffSftRule tssr = new TreeStaffSftRule();
				tssr.setOrgTreeId(OrganizationConstant.COST_TREE_ID);
				objSff.setWorkProp(staffManager.getStaffWorkpropStr(tssr));
			}
			qryStaff = objSff;
			if (this.permissionOrganizationList != null
					&& this.permissionOrganizationList.size() > 0) {
				qryStaff.setPermissionOrganizationList(permissionOrganizationList);
			}
			/**
			 * 默认数据权最大电信管理区域
			 */
			if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion
						.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			}
			qryStaff.setPermissionTelcomRegion(permissionTelcomRegion);

			this.bean.getStaffListboxPaging().setActivePage(0);
			/**
			 * 查询组织下的员工，保存到list里面，供staffBandboxExt使用
			 */
			staffs = staffManager.quertyStaffNoPage(qryStaff);
			if (null != staffs) {
				Events.postEvent(SffOrPtyCtants.ON_STAFF_EXT_QUERY, this,
						staffs);
			}
			queryStaff();
			staff = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cleanTabs() {
		qryStaff = null;
		staff = null;
		Events.postEvent(SffOrPtyCtants.ON_STAFF_CLEAR_TABS, this, null);
	}

	/**
	 * 打开编辑页面 .
	 * 
	 * @param opType
	 * @author Wong 2013-5-28 Wong
	 */
	private void openStaffEditWin(String opType) {
		try {
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			if (opType.equals(SffOrPtyCtants.MOD)
					|| opType.equals(SffOrPtyCtants.PWD)
					|| opType.equals(SffOrPtyCtants.VIEW)) {
				arg.put("staff", staff);
			}
			String zul = SffOrPtyCtants.STAFF_EDIT_ZUL;
			if (opType.equals(SffOrPtyCtants.VIEW)) {
				zul = SffOrPtyCtants.STAFF_DETAIL_ZUL;
			}
			Window win = (Window) Executions.createComponents(zul, this, arg);
			win.doModal();
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					setStaffButtonValid(true, false, false, false, false,
							false, false, false);
					if (event.getData() != null) {
						qryStaff = (Staff) event.getData();
						// PubUtil.reDisplayListbox(bean.getStaffListbox(),(Staff)
						// event.getData(), type);

						if (SffOrPtyCtants.ADD.equals(type)) {
							// 新增员工后，用于显示该条记录
							if (!StrUtil.isEmpty(qryStaff.getStaffCode())) {
								bean.getStaffCode().setValue(
										qryStaff.getStaffCode().trim());
							}

							if (!StrUtil.isEmpty(qryStaff.getObjStaffAccount()
									.getStaffAccount())) {
								bean.getStaffAccount().setValue(
										qryStaff.getObjStaffAccount()
												.getStaffAccount().trim());
							}

							if (!StrUtil.isEmpty(qryStaff.getStaffName())) {
								bean.getStaffName().setValue(
										qryStaff.getStaffName().trim());
							}

						} else if (SffOrPtyCtants.MOD.equals(type)) {

							if (!StrUtil.isNullOrEmpty(qryStaff.getWorkProp())
									&& (qryStaff.getWorkProp().startsWith(
											SffOrPtyCtants.WORKPROP_N_H_PRE) || qryStaff
											.getWorkProp()
											.startsWith(
													SffOrPtyCtants.WORKPROP_N_P_PRE))) {

								StaffOrganization staffOrganization = qryStaff
										.getStaffOrganization();

								if (staffOrganization != null) {

									Organization org = staffOrganization
											.getOrganization();

									if (org != null && org.getOrgId() != null
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

											if (qryStaff.getPartyRoleId() != null) {
												PartyRole partyRole = staffManager.getPartyRole(qryStaff
														.getPartyRoleId());
												if (partyRole != null
														&& partyRole
																.getPartyId() != null) {
													Party party = staffManager.getParty(partyRole
															.getPartyId());
													PartyContactInfo partyContactInfo = partyManager
															.getDefaultPartyContactInfo(partyRole
																	.getPartyId());

													if (party != null
															&& partyContactInfo != null) {
														party.setPartyContactInfo(partyContactInfo);
														boolean groupMailInterFaceSwitch = UomClassProvider
																.isOpenSwitch("groupMailInterFaceSwitch");// 集团统一邮箱开关

														if (groupMailInterFaceSwitch) {
															String msg = groupMailManager
																	.groupMailPackageInfo(
																			GroupMailConstant.GROUP_MAIL_BIZ_ID_14,
																			party,
																			org);
															if (!StrUtil
																	.isEmpty(msg)) {
																ZkUtil.showError(
																		msg,
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

						queryStaff();
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
	 * 编辑页面 .
	 * 
	 * @param opType
	 * @author 朱林涛 2016-01-28
	 */
	private void openIndividualEditWin(String opType) {
		try {
			Party party = null;
			if (staff != null) {
				party = partyManager.getPartyByStaff(staff);
				if (party != null) {
					party.setPartyRoleId(staff.getPartyRoleId().toString());
				} else {
					ZkUtil.showError("该员工没有关联参与人信息，请先关联！", "提示信息");
					return;
				}
			} else {
				ZkUtil.showError("请先选择员工，再进行个人信息修改！", "提示信息");
				return;
			}
			Map<String, Object> arg = new HashMap<String, Object>();
			arg.put("opType", opType);
			arg.put("party", party);
			String zul = SffOrPtyCtants.PARTY_EDIT_ZUL;
			Window win = (Window) Executions.createComponents(zul, this, arg);
			win.doModal();
			@SuppressWarnings("unused")
			final String type = opType;
			win.addEventListener(SffOrPtyCtants.ON_OK, new EventListener() {
				public void onEvent(Event event) {
					if (event.getData() != null) {
						// Party party = (Party) event.getData();
						try {
							Messagebox.show("修改个人信息成功！");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						ZkUtil.showError("修改个人信息失败！", "提示信息");
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
	public void setStaffWindowDivVisible(boolean visible) {
		bean.getStaffWindowDiv().setVisible(visible);
	}

	/**
	 * .重置查询内容 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onStaffReset() throws Exception {
		bean.getStaffAccount().setValue(null);
		bean.getStaffName().setValue(null);
		bean.getStaffCode().setValue(null);
		bean.getOrganizationRelationTreeBandboxExt().setValue(null);
		bean.getOrganizationRelationTreeBandboxExt().setOrganizationRelation(
				null);
		bean.getOrganizationRelationTreeBandboxExt().setOrganizationRelations(
				null);
	}

	/**
	 * 选择选中的员工.
	 */
	public void onSelectStaffInfo() throws Exception {
		Listitem litem = bean.getStaffListbox().getSelectedItem();
		if (litem != null) {
			Events.postEvent(SffOrPtyCtants.ON_STAFF_CONFIRM, this,
					litem.getValue());
		} else {
			ZkUtil.showInformation(SffOrPtyCtants.MSG_NO_SELECT_STAFF,
					SffOrPtyCtants.MSG_SHOW_INFO);
			return;
		}
	}

	/**
	 * 清空选中的员工.
	 */
	public void onCleanStaffInfo() throws Exception {
		this.bean.getStaffListbox().clearSelection();
		Events.postEvent(SffOrPtyCtants.ON_STAFF_CLEAN, this, null);
	}

	/**
	 * 关闭.
	 */
	public void onCloseStaffInfo() throws Exception {
		Events.postEvent(SffOrPtyCtants.ON_STAFF_CLOSE, this, null);
	}

	/**
	 * 将Bean控件中的内容转换位实体对象 .
	 * 
	 * @return
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public Staff fillPoFromBean() throws Exception {
		Staff objStaffTmp = new Staff();
		StaffAccount objStaffAcc = null;
		if (!StrUtil.isNullOrEmpty(bean.getStaffAccount().getValue())) {
			objStaffAcc = new StaffAccount();
			objStaffAcc.setStaffAccount(bean.getStaffAccount().getValue());
		}

		if (null != objStaffAcc) {
			objStaffTmp.setObjStaffAccount(objStaffAcc);
		}

		if (!StrUtil.isNullOrEmpty(bean.getStaffName().getValue())) {
			objStaffTmp.setStaffName(bean.getStaffName().getValue());
		}
		return objStaffTmp;
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 */
	@SuppressWarnings("unused")
	public void setPagePosition(String page) throws Exception {
		boolean canAdd = false;
		// boolean canAddBatch = false;
		boolean canDel = false;
		boolean canEdit = false;
		boolean canIndividualEdit = false;
		boolean canUpdate = false;
		boolean canView = false;
		boolean canUpload = false;
		boolean canDownload = false;
		boolean canResetPwd = false;

		if (PlatformUtil.isAdmin()) {
			canAdd = true;
			// canAddBatch = true;
			canDel = true;
			canEdit = true;
			canIndividualEdit = true;
			canUpdate = true;
			canView = true;
			canUpload = true;
			canDownload = true;
			canResetPwd = true;
		} else if ("staffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_ADD)) {
				canAdd = true;
			}
			// if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
			// ActionKeys.STAFF_ADD_BATCH)) {
			// canAddBatch = true;
			// }
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_INDIVIDUAL_EDIT)) {
				canIndividualEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_VIEW)) {
				canView = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_UPLOAD)) {
				canUpload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_DOWNLOAD)) {
				canDownload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_RESET_PWD)) {
				canResetPwd = true;
			}
		} else if ("marketingStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_INDIVIDUAL_EDIT)) {
				canIndividualEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_VIEW)) {
				canView = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_UPLOAD)) {
				canUpload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_DOWNLOAD)) {
				canDownload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.MARKETING_STAFF_RESET_PWD)) {
				canResetPwd = true;
			}
		} else if ("costStaffPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_ADD)) {
				canAdd = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_DEL)) {
				canDel = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_INDIVIDUAL_EDIT)) {
				canIndividualEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_UPDATE)) {
				canUpdate = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_VIEW)) {
				canView = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_UPLOAD)) {
				canUpload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_DOWNLOAD)) {
				canDownload = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.COST_STAFF_RESET_PWD)) {
				canResetPwd = true;
			}
		}
		this.bean.getAddStaffButton().setVisible(canAdd);
		// this.bean.getStaffAddBatchButton().setVisible(canAddBatch);
		this.bean.getDelStaffButton().setVisible(canDel);
		this.bean.getEditStaffButton().setVisible(canEdit);
		this.bean.getEditIndividualButton().setVisible(canIndividualEdit);
		this.bean.getUpdateStaffButton().setVisible(canUpdate);
		this.bean.getViewButton().setVisible(canView);
		this.bean.getUploadButton().setVisible(canUpload);
		this.bean.getDownloadButton().setVisible(canDownload);
		// this.bean.getResetPwdButton().setVisible(canResetPwd);
	}

	/**
	 * 重置换密码
	 * 
	 * @throws Exception
	 */
	public void onResetPwd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING)) {
			return;
		}
		/**
		 * 选择的员工
		 */
		if (staff != null) {
			StaffAccount staffAccount = staff.getStaffAccountFromDB();
			if (staffAccount == null) {
				ZkUtil.showError("该员工不存在账号！", "提示信息");
				return;
			}
			staff.setObjStaffAccount(staffAccount);
			staffManager.updateStaffPwd(staff);
			// 实时同步到ldap
			staffManager.updateStaffToLdap(staffAccount.getStaffAccount());
			ZkUtil.showInformation("密码重置成功!新密码为：工号+123",
					SffOrPtyCtants.MSG_SHOW_INFO);
			this.queryStaff();
		}
	}

}
