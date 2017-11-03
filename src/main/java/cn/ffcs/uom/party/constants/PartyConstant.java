package cn.ffcs.uom.party.constants;

public class PartyConstant {
	/**
	 * 参与人类型
	 */
	public static final String ATTR_VALUE_PARTYTYPE_PERSONAL = "1";// 个人
	/**
	 * 参与人类型
	 */
	public static final String ATTR_VALUE_PARTYTYPE_ORGANIZATION = "2";// 组织
	/**
	 * 游离参与人
	 */
	public static final String FREE_PARTY = "1";// 游离参与人
	/**
	 * 非游离参与人
	 */
	public static final String NOT_FREE_PARTY = "0";// 非游离参与人

	/**
	 * 首选联系人
	 */
	public static final String ATTR_VALUE_HEADFLAG_YES = "1";// 是

	/**
	 * 联系人类型-本人
	 */
	public static final String ATTR_VALUE_CONTACTTYPE_SELF = "0105";// 本人
	/**
	 * 联系人类型-企业法人
	 */
	public static final String ATTR_VALUE_CONTACTTYPE_BUSINESS = "0201";// 企业法人
	/**
	 * 身份证默认值
	 */
	public static final String ATTR_VALUE_IDNO = "1";// 身份证
	/**
	 * 类型-正常员工
	 */
	public static final String IDENTITY_CARD_TMP = "1";// 身份证类型
	/**
	 * 类型-应用管理员
	 */
	public static final String IDENTITY_CARD_APP = "2";// 身份证类型
	/**
	 * 类型-系统接口
	 */
	public static final String IDENTITY_CARD_INT = "3";// 身份证类型
	/**
	 * 类型-自助终端
	 */
	public static final String IDENTITY_CARD_MAC = "4";// 身份证类型

	/**
	 * 身份证长度15位
	 */
	public static final int ATTR_VALUE_IDNO15 = 15;

	/**
	 * 身份证长度18位
	 */
	public static final int ATTR_VALUE_IDNO18 = 18;

	/**
	 * 参与人角色类型
	 */
	public static final String PARTY_ROLE_TYPE_AGENT = "1301";// 代理商
	public static final String PARTY_ROLE_TYPE_SUPPLIER = "1302";// 供应商
	public static final String PARTY_ROLE_TYPE_INDUSTRIES = "1303";// 实业公司
	public static final String PARTY_ROLE_TYPE_TELE = "1210";// 电信员工
	public static final String PARTY_ROLE_TYPE_WCOMP = "1220";// 外公司员工
	/**
	 * 默认证件
	 */
	public static final String DEFAULT_CERT_SORT = "1";
	/**
	 * 证件类型-身份证
	 */
	public static final String DEFAULT_CERT_TYPE_IDENTITY_CARD = "1";

	/**
	 * TMP：临时账号，APP：应用管理账号,INT:系统接口账号，ORG：部门共用账号，MAC：自助终端账号，AGT:代理商账号
	 */
	public static final String STAFF_TYPE_TMP = "TMP";
	public static final String STAFF_TYPE_APP = "APP";
	public static final String STAFF_TYPE_INT = "INT";
	public static final String STAFF_TYPE_ORG = "ORG";
	public static final String STAFF_TYPE_MAC = "MAC";
	public static final String STAFF_TYPE_AGT = "AGT";
	/**
	 * 证件已实名
	 */
	public static final String PARTY_CERTIFICATION_IS_REAL_NAME_Y = "1";
	/**
	 * 证件未实名
	 */
	public static final String PARTY_CERTIFICATION_IS_REAL_NAME_N = "0";

}
