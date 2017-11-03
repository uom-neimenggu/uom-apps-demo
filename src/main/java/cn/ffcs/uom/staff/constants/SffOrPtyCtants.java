package cn.ffcs.uom.staff.constants;

import cn.ffcs.uom.common.model.UomClassProvider;

/**
 * 员工参与人 操作静态变量 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wangyong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-17
 * @Email wangyong@ffcs.cn
 * @功能说明：
 * 
 */
public class SffOrPtyCtants {

	/**
	 * 选择属性
	 */
	public static final Long ATTR_SPEC_TYPE_SELECT = 2l;
	/**
	 * 输入属性
	 */
	public static final Long ATTR_SPEC_TYPE_INPUT = 1l;
	/**
	 * 数字输入属性
	 */
	public static final Long ATTR_SPEC_TYPE_LONGBOX_INPUT = 3l;
	/**
	 * 可以输入
	 */
	public static final String ATTR_SPEC_SORT_TYPE_NORMAL = "10";
	/**
	 * 挂树上才可输入
	 */
	public static final String ATTR_SPEC_SORT_TYPE_TREE = "11";
	/**
	 * 不可以输入
	 */
	public static final String ATTR_SPEC_SORT_TYPE_ABNORMAL = "20";
	/**
	 * 个人信息
	 */
	public static final String CONST_INDIVIDUAL = "1";

	/**
	 * 添加员工信息
	 */
	public static final String CONST_ADD_STAFFINFO = "1";

	/**
	 * 不添加员工信息
	 */
	public static final String CONST_NO_ADD_STAFFINFO = "0";

	/**
	 * 组织用户
	 */
	public static final String CONST_ORGANIZATION = "2";

	public static final String CONST_ORGANIZATION_3 = "2";

	/**
	 * 员工查询.
	 */
	public static final String ON_STAFF_QUERY = "onQueryStaff";
	public static final String ON_STAFF_QUERY_RESPONSE = "onQueryStaffResponse";
	public static final String ON_STAFF_ISSUED_RESPONSE = "onQueryStaffIssuedResponse";
	/**
	 * 员工编辑
	 */
	public static final String STAFF_EDIT_ZUL = "/pages/staff/staff_edit.zul";

	public static final String STAFF_DETAIL_ZUL = "/pages/staff/staff_detail.zul";

	/**
	 * zul.参与人联系人信息编辑页面
	 */
	public static final String PARTYCONTACTINFO_EDIT_ZUL = "/pages/party/party_contactInfo_edit.zul";

	/**
	 * zul.参与人证件信息编辑页面
	 */
	public static final String PARTYCERTIFICATION_EDIT_ZUL = "/pages/party/party_certification_edit.zul";

	public static final String PARTY_ROLE_EDIT_ZUL = "/pages/party/party_role_edit.zul";

	/**
	 * zul.参与人证件
	 */
	public static final String ZUL_PARTYCERTIFICATION_LISTBOX_EXT = "/pages/party/comp/party_certification_listbox_ext.zul";

	public static final String ZUL_STAFF_ORG_TRAN_LISTBOX_EXT = "/pages/staff/comp/staffOrgTran_listbox_ext.zul";

	/**
	 * zul.参与人联系人
	 */
	public static final String ZUL_PARTYCONTACTINFO_LISTBOX_EXT = "/pages/party/comp/party_contactInfo_listbox_ext.zul";

	/**
	 * zul.参与人角色
	 */
	public static final String ZUL_PARTY_ROLE_LISTBOX_EXT = "/pages/party/comp/partyRole_Listbox_ext.zul";

	public static final String ZUL_PARTY_ROLE_EDIT = "/pages/party/party_role_edit.zul";

	public static final String ON_OK = "onOK";

	public static final String ADD = "add";
	public static final String MOD = "mod";
	public static final String MOD_INDIVIDUAL = "modIndividual";
	public static final String MODSPE = "modSpe";
	public static final String VIEW = "view";
	public static final String PWD = "pwd";
	public static final String UPLOAD = "upload";

	/**
	 * 身份证
	 */
	public static final String CERT_TYPE_ID_CARD = "1";
	/**
	 * OA证件
	 */
	public static final String CERT_TYPE_OA = "32";
	/**
	 * HR证件
	 */
	public static final String CERT_TYPE_HR = "33";

	/**
	 * 员工选择.
	 */
	public static final String ON_STAFF_SELECT = "onSelectStaff";
	public static final String ON_STAFF_NOT_SELECT = "onSelectNotStaff";

	/**
	 * 员工EXT状态，单击查询.
	 */
	public static final String ON_STAFF_EXT_QUERY = "onStaffExtQuery";
	/**
	 * 员工EXT状态，分页查询.
	 */
	public static final String ON_STAFF_EXT_PAGE_QUERY = "onStaffExtPageQuery";

	/**
	 * 员工管理点击查询.
	 */
	public static final String ON_STAFF_MANAGE_QUERY = "onStaffManageQuery";

	public static final String ON_STAFF_ORG_SELECT = "onSelectOrgStaff";

	public static final String ON_STAFF_ORG_RESPONSE = "onSelectOrgStaffResponse";

	/**
	 * 员工选择.
	 */
	public static final String ON_STAFF_CONFIRM = "onConfirmStaff";
	/**
	 * 清空员工.
	 */
	public static final String ON_STAFF_CLEAN = "onCleanStaff";
	/**
	 * 员工关闭.
	 */
	public static final String ON_STAFF_CLOSE = "onCloseStaff";

	/**
	 * 员工保存.
	 */
	public static final String ON_STAFF_SAVE = "onSaveStaff";
	/**
	 * 员工删除.
	 */
	public static final String ON_STAFF_DELETE = "onDeleteStaff";

	/**
	 * 参与人查询.
	 */
	public static final String ON_PARTY_QUERY = "onQueryParty";
	/**
	 * 参与人
	 */
	public static final String ON_PARTY_QUERY_RESPONSE = "onQueryPartyResponse";

	public static final String PARTY_EDIT_ZUL = "/pages/party/party_edit.zul";

	public static final String PARTY_DETAIL_ZUL = "/pages/party/party_detail.zul";

	/**
	 * 参与人选择.
	 */
	public static final String ON_PARTY_SELECT = "onSelectParty";
	/**
	 * 参与人管理查询.
	 */
	public static final String ON_PARTY_MANAGE_QUERY = "onPartyManageQuery";
	/**
	 * 参与人选择.
	 */
	public static final String ON_PARTY_CONFIRM = "onConfirmParty";
	/**
	 * 清空参与人.
	 */
	public static final String ON_PARTY_CLEAN = "onCleanParty";
	/**
	 * 参与人关闭.
	 */
	public static final String ON_PARTY_CLOSE = "onCloseParty";

	/**
	 * 参与人保存.
	 */
	public static final String ON_PARTY_SAVE = "onSaveParty";

	/**
	 * 首选联系人
	 */
	public static final String HEADFLAG = "1";
	/**
	 * 非首选联系人
	 */
	public static final String NO_HEADFLAG = "2";

	/**
	 * 员工删除.
	 */
	public static final String ON_PARTY_DELETE = "onDeleteParty";

	/**
	 * 参与人选择.PartyContactInfo PARTYCONTACTINFO
	 */

	public static final String ON_PARTYCONTACTINFO_QUERY = "onQueryPartyContactInfo";

	public static final String ON_PARTYCONTACTINFO_QUERY_RESPONSE = "onQueryPartyContactInfoResponse";

	public static final String ON_PARTYCONTACTINFO_CONFIRM = "onConfirmPartyContactInfo";

	public static final String ON_PARTYCONTACTINFO_CLEAN = "onCleanPartyContactInfo";

	public static final String ON_PARTY_CONTACTINFO_CLOSE = "onClosePartyContactInfo";

	public static final String ON_PARTYCONTACTINFO_SELECT = "onSelectPartyContactInfo";
	public static final String ON_PARTYCONTACTINFO_SELECT_RESPONSE = "onSelectPartyContactInfoResponse";

	public static final String ON_PARTYCONTACTINFO_DELETE = "onDeletePartyContactInfo";

	public static final String ON_PARTYCONTACTINFO_VIEW = "onViewPartyContactInfo";

	public static final String ON_PARTYCONTACTINFO_VIEW_RESPONSE = "onViewPartyContactInfoResponse";
	// /////////////////////////////////////////////////////////////////////////////////////////////////
	public static final String ON_PARTYCERTIFICATION_QUERY = "onQueryPartyCertification";
	public static final String ON_PARTYCERTIFICATION_QUERY_RESPONSE = "onQueryPartyCertificationResponse";

	public static final String ON_PARTYCERTIFICATION_CLOSE = "onClosePartyCertification";

	public static final String ON_PARTYCERTIFICATION_CLEAN = "onCleanPartyCertification";

	public static final String ON_PARTYCERTIFICATION_CONFIRM = "onConfirmPartyCertification";

	public static final String ON_PARTYCERTIFICATION_VIEW = "onViewPartyCertification";
	public static final String ON_PARTYCERTIFICATION_VIEW_RESPONSE = "onViewPartyCertificationResponse";

	public static final String ON_PARTYCERTIFICATION_SELECT = "onSelectPartyCertification";
	public static final String ON_PARTYCERTIFICATION_SELECT_RESPONSE = "onSelectPartyCertificationResponse";
	// ////////////////////////
	public static final String ZUL_STAFF_BANDBOX_EXT = "/pages/staff/comp/staff_bandbox_ext.zul";

	public static final String ZUL_STAFF_ORG_EXT = "/pages/staff/comp/staffOrg_list_ext.zul";

	/**
	 * 员工岗位查询.
	 */
	public static final String ON_STAFF_POSITION_QUERY = "onStaffPositionQuery";
	
	public static final String ON_STAFF_CT_POSITION_QUERY = "onStaffCtPositionQuery";

	public static final String ON_STAFF_Org_QUERY = "onStaffOrgQuery";
	public static final String ON_STAFF_Org_QUERY_RESP = "onStaffOrgQueryResponse";

	public static final String ON_STAFF_GROUP_QUERY = "onStaffGroupQuery";
	public static final String ON_STAFF_GROUP_QUERY_RESP = "onStaffGroupQueryResponse";

	public static final String ON_STAFF_POSI_SELECT = "onSelectStaffPosi";
	public static final String ON_STAFF_ORG_TRAN_SELECT = "onPostStaffOrgTran";
	public static final String ON_STAFF_ORG_TRAN_SELECT_RES = "onSelectStaffOrgTranResponse";
	public static final String ON_STAFF_GRP_STAFF_QUERY = "onStaffGrpStaffQuery";
	public static final String ON_STAFF_GRID_UNIT_TRAN_SELECT = "onPostStaffGridUnitTran";
	public static final String ON_STAFF_GRID_UNIT_TRAN_SELECT_RES = "onSelectStaffGridUnitTranResponse";
	public static final String ON_ORGANIZATION_SELECT_RES = "onSelectOrganizationResponse";  //在组织树种选中组织，抛出事件，然后员工组织关系中回馈事件

	public static final String ON_STAFF_CLEAR_TABS = "onStaffClearTabs";

	public static final String ON_STAFF_CLEAR_TABS_RESPONS = "onStaffClearTabsRespons";

	public static final String ON_CLEAN_STAFF_ORG = "onCleanStaffOrg";
	public static final String ON_CLEAN_STAFF_ORG_RES = "onCleanStaffOrgRespons";
	public static final String ON_CLEAN_STAFF_POSITI = "onCleanStaffPositi";
	public static final String ON_CLEAN_STAFF_CT_POSITI = "onCleanStaffCtPositi";
	public static final String ON_CLEAN_STAFF_POSITI_RES = "onCleanStaffPositiRespons";
	public static final String ON_CLEAN_STAFFORG_TRAN = "onCleanStaffOrgTran";
	public static final String ON_CLEAN_STAFFORG_TRAN_RES = "onCleanStaffOrgTranRespons";
	public static final String ON_CLEAN_STAFF_GRID_UNIT_TRAN = "onCleanStaffGridUnitTran";
	public static final String ON_CLEAN_STAFF_GRID_UNIT_TRAN_RES = "onCleanStaffGridUnitTranRespons";

	public static final String ON_CLEAN_STAFF_GRP_STAFF = "onCleanStaffGrpStaff";
	public static final String ON_CLEAN_STAFF_GRP_STAFF_RES = "onCleanStaffGrpStaffRespons";
	// ///////////////////////////////////////////
	public static final String MSG_SHOW_INFO = "提示信息";
	public static final String MSG_NO_SELECT_STAFF = "请选择相应的员工";

	public static final String ON_PARTY_CLEAR_TABS = "onPartyClearTabs";

	public static final String ON_PARTY_CLEAR_TABS_RESPONS = "onPartyClearTabsRespons";

	public static final String ON_CLEAN_PARTY_CER = "onCleanPartyCer";
	public static final String ON_CLEAN_PARTY_CER_RES = "onCleanPartyCerRespons";

	public static final String ON_CLEAN_PARTY_CON = "onCleanPartyCon";
	public static final String ON_CLEAN_PARTY_CON_RES = "onCleanonCleanPartyConRespons";

	public static final String ON_PARTY_ROLE_QUERY = "onPartyRoleQuery";
	public static final String ON_PARTY_ROLE_QUERY_RES = "onPartyRoleQueryRespons";
	/**
	 * 默认证件
	 */
	public static final String CERT_SORT_1 = "1";
	/**
	 * 非默认证件
	 */
	public static final String CERT_SORT_2 = "2";

	public static final String SYS_JAVA_CODE_PARTY_CON_INFO = "PartyContactInfo";

	public static final String SPC_JAVA_CODE_CONTACTTYPE = "contactType";

	/**
	 * zul.参与人证件信息编辑页面
	 */
	public static final String STAFF_ORG_TRAN_EDIT_ZUL = "/pages/staff/staffOrgTran_edit.zul";

	public static final String TITLE_MSG = "提示信息";
	public static final String PASSWORD_DO_NOT_MATCH = "两次输入的密码不一致";

	public static final String CON_PERSONAL = "0105"; // 本人信息

	public static final String ON_EVENT_CONTACT_TYPE = "onEventContactType";
	public static final String ON_EVENT_CONTACT_TYPE_RESP = "onEventContactTypeRespons";
	public static final String CLSS_PARTY_CON_INFO = "PartyContactInfo";
	public static final String CLSS_CONTACT_TYPE = "contactType";

	/**
	 * 处理结果
	 */
	public static final String RESULT_0 = "0";
	public static final String RESULT_1 = "1";
	public static final String RESULT_2 = "2";
	public static final String RESULT_9 = "9";

	/**
	 * 校验字段
	 */
	public static final String NULL_OR_EMPTY = "0";// 空或空字符串
	public static final String NULL_OR_EMPTY_STR = "空";
	public static final String LENGTH_LIMIT = "1";// 长度有误
	public static final String LENGTH_LIMIT_STR = "长度有误";
	public static final String FIELD_REPEAT = "2";// 重复
	public static final String FIELD_REPEAT_STR = "重复";
	public static final String FIELD_ERROR = "3";
	public static final String FIELD_ERROR_STR = "格式不正确";
	public static final String FIELD_NOT_EXIST = "4";// 不存在
	public static final String FIELD_NOT_EXIST_STR = "不存在";
	public static final String FIELD_ERROR_OR_NOT_EXIST = "5";// 移动电话格式不对,或不存在此号码段。
	public static final String FIELD_ERROR_OR_NOT_EXIST_STR = "移动电话格式不对,或不存在此号码段。";
	public static final String FIELD_ERROR_OR_NOT_EXIST_SPARE = "6";// 备用移动电话格式不对,或不存在此号码段。
	public static final String FIELD_ERROR_OR_NOT_EXIST_SPARE_STR = "备用移动电话格式不对,或不存在此号码段。";
	public static final String FIELD_ERROR_OR_NOT_EXIST_CERT = "7";
	public static final String FIELD_ERROR_OR_NOT_EXIST_CERT_STR = "类型不存在。";
	public static final String FIELD_ERROR_IMP_LIST_EXIST = "8";
	public static final String FIELD_ERROR_IMP_LIST_EXIST_STR = "身份证在导入列表中重复;";
	public static final String FIELD_ERROR_CERT_ALREADY_USE = "9";
	public static final String FIELD_ERROR_CERT_ALREADY_USE_STR = "身份证已经被使用次数超过%次上限";
	public static final String FIELD_ERROR_CERT_ALREADY_USE_RELOAD = "10";
	public static final String FIELD_ERROR_CERT_ALREADY_USE_RELOAD_STR = "身份证已经使用,请生成临时证件导入;";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_NULL = "11";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_NULL_STR = "侦测到该身份证对应的人力信息,但人力中间表对应的工号为空;";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO = "12";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_TWO_STR = "侦测到该身份证对应的人力信息,但人力中间表对应的工号长度小于2位;";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE = "13";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_LESS_THREE_STR = "侦测到该身份证对应的人力信息,但人力中间表对应的工号长度小于3位;";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM = "14";
	public static final String FIELD_ERROR_OPERATE_HR_TABLE_01_EXIST_UOM_STR = "侦测到该身份证对应的人力信息,但人力中间表对应的工号在主数据中已经存在;";
	public static final String FIELD_ERROR_OR_STAFF_NOT_EXIST = "15";
	public static final String FIELD_ERROR_OR_STAFF_NOT_EXIST_STR = "信息填写错误或该员工不存在";
	public static final String FIELD_ERROR_SESSION_HOLDING_NUBER = "16";
	public static final String FIELD_ERROR_SESSION_HOLDING_NUBER_STR = "员工会话保持数包含非数字字符";
	public static final String FIELD_ERROR_STAFF_ROLE_RELA_EXIST = "17";
	public static final String FIELD_ERROR_STAFF_ROLE_RELA_EXIST_STR = "员工角色关系已经存在";
	public static final String FIELD_ERROR_WORKPROP = "18";// 空或空字符串
	public static final String FIELD_ERROR_WORKPROP_STR = "用工性质非内部_合同制、内部_派遣制,集团统一邮箱无需填写";
	public static final String FIELD_ERROR_STAFF_SORT_NUBER = "19";
	public static final String FIELD_ERROR_STAFF_SORT_NUBER_STR = "员工排序包含非数字字符";
	public static final String FIELD_ERROR_ORG_CODE_OR_MME_FID_NUBER = "20";
	public static final String FIELD_ERROR_ORG_CODE_OR_MME_FID_NUBER_STR = "组织编码或全息网格标识包含非数字字符";
	public static final String FIELD_ERROR_ID_REALNAME = "21";// 空或空字符串
	public static final String FIELD_ERROR_ID_REALNAME_STR = "实名认证未通过";
	public static final String FIELD_ERROR_STAFF_ROLE_RELA_NOT_EXIST = "22";
	public static final String FIELD_ERROR_STAFF_ROLE_RELA_NOT_EXIST_STR = "员工角色关系不存在，无法删除";

	/**
	 * 用工性质
	 */
	public static final String WORKPROP_W_AGENT = "40001";// 40001-外部_代理商员工
	public static final String WORKPROP_W_PROVIDER = "40002";// 40002-外部_供应商员工
	public static final String WORKPROP_W_OTHER = "40009";// 40009-外部_其他
	public static final String WORKPROP_N_W = "30000";// 30000-内部_外包制
	public static final String WORKPROP_N_H_PRE = "1000";// 10000-内部_合同制前缀
	public static final String WORKPROP_N_H = "10000";// 1000-内部_合同制
	public static final String WORKPROP_N_P_PRE = "2000";// 2000-内部_派遣制前缀
	public static final String WORKPROP_N_P = "20000";// 20000-内部_派遣制
	public static final String WORKPROP_P_SRS = "20001";// 20001-实业公司人事派遣
	public static final String WORKPROP_P_LW = "20002";// 20002-实业公司劳务派遣
	public static final String WORKPROP_P_QRS = "20003";// 20003-其他中介公司人事派遣
	public static final String WORKPROP_P_QLW = "20004";// 20004-其他中介公司劳务派遣

	/**
	 * 外部代理商页面
	 */
	public static final String ON_ENTER_AGENT_PAGE = "onEnterAgentPage";
	public static final String ON_AGENT_STAFF_LISTBOX_PAGE = "onAgentStaffListBoxPage";
	/**
	 * 外部内部经营实体页面
	 */
	public static final String ON_ENTER_IBE_PAGE = "onEnterIbePage";
	public static final String ON_IBE_STAFF_LISTBOX_PAGE = "onIbeStaffListBoxPage";

	/**
	 * 设置员工角色
	 */
	public static final String STAFF_ROLE_ZUL = "/pages/staff_role/staff_role_edit.zul";

	public static final String ON_STAFF_PAGE_SELECT_FOR_ROLE = "onStffPageSelectForRole";
	public static final String ON_STAFF_ROLE_PAGE_POSITION = "onStaffRolePagePosition";
	public static final String ON_STAFF_PAGE_SELECT_FOR_SYS = "onStffPageSelectForSys";

	public static final String ON_SET_STAFF_RELA = "onSetStaffRela";

	public static final String STAFF_TYPE_AGT = "6";
	/**
	 * 员工扩展属性-员工会话保持数
	 */
	public static final Long STAFF_ATTR_SPEC_ID_2 = 2L;
	/**
	 * 员工扩展属性-集团4G门户编码
	 */
	public static final Long STAFF_ATTR_SPEC_ID_3 = 3L;

	public static String SQL_QUERY_ADMIN = "SELECT Z.*, V2.ORG_ID FROM VIEW_STAFFS Z LEFT JOIN STAFF_ORGANIZATION V2 ON(V2.Status_Cd=1000 AND V2.Rala_Cd=1 AND Z.STAFF_ID=V2.STAFF_ID) WHERE 1=1 ";
}
