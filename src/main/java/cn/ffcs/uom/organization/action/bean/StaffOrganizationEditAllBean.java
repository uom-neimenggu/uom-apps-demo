package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.organization.component.OrganizationBandboxExt;
import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.staff.component.StaffExtendAttrExt;
import cn.ffcs.uom.staffrole.component.StaffRoleTreeBandboxExt;

/**
 * 组织编辑Bean
 * 
 * @author
 **/
public class StaffOrganizationEditAllBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffOrganizationEditAllWindow;

	@Getter
	@Setter
	private OrganizationBandboxExt org;

	@Getter
	@Setter
	private Listbox ralaCd;

	@Getter
	@Setter
	private Spinner staffSeq;

	@Getter
	@Setter
	private Textbox userCode;

	@Getter
	@Setter
	private Textbox note;
	
	@Getter
	@Setter
	private Textbox reason;

	/****
	 * ======================= 员工信息 =======================
	 */
	/**
	 * 员工名称.
	 */
	@Setter
	@Getter
	private Textbox staffName;

	/**
	 * 行政区域标识.
	 */
	@Setter
	@Getter
	private PoliticalLocationTreeBandbox locationId;

	/**
	 * 员工编码.
	 */
	@Setter
	@Getter
	private Textbox staffCode;

	/**
	 * 用工性质.
	 */
	@Setter
	@Getter
	private TreeChooserBandbox workProp;
	
	/**
	 * 人员属性.
	 */
	@Setter
	@Getter
	private TreeChooserBandbox staffProperty;

	/**
	 * 兼职/全职.
	 */
	@Setter
	@Getter
	private Listbox partTime;

	/**
	 * 帐号类别
	 */
	// @Setter
	// @Getter
	// private Listbox staffType;

	/**
	 * 员工职位.
	 */
	@Setter
	@Getter
	private Listbox staffPosition;

	/**
	 * 职务标注.
	 */
	@Setter
	@Getter
	private Textbox titleNote;

	/**
	 * 备注.
	 */
	@Setter
	@Getter
	private Textbox remark;

	/**
	 * 员工描述.
	 */
	@Setter
	@Getter
	private Textbox staffDesc;

	/**
	 * 人力工号.
	 */
	@Setter
	@Getter
	private Textbox hrNumber;

	/****
	 * ======================= 员工账号 =======================
	 */
	/**
	 * 员工账号.
	 */
	@Setter
	@Getter
	private Textbox staffAccount;

	@Setter
	@Getter
	private Textbox staffPassword;

	/****
	 * ======================= 员工拓展属性 =======================
	 */
	/**
	 * 员工扩展属性
	 */
	@Setter
	@Getter
	private StaffExtendAttrExt staffExtendAttrExt;

	/****
	 * ======================= 参与人信息 =======================
	 */
	/**
	 * 参与人名称.
	 */
	@Setter
	@Getter
	private Textbox partyName;

	/**
	 * 参与人曾用名.
	 */
	@Setter
	@Getter
	private Textbox partyNameFirst;

	/**
	 * 名称简拼.
	 */
	@Setter
	@Getter
	private Textbox partyAbbrname;

	/**
	 * 英文名称.
	 */
	@Setter
	@Getter
	private Textbox englishName;

	/**
	 * 参与人类型.
	 */
	@Setter
	@Getter
	private Listbox partyType;

	/**
	 * 参与人角色类型.
	 */
	@Setter
	@Getter
	private TreeChooserBandbox roleType;

	/****
	 * ======================= 参与人证件 =======================
	 */
	/**
	 * 参与人证件类型.
	 */
	@Setter
	@Getter
	private Listbox certType;
	
	/**
	 * 证件名
	 */
	@Setter
	@Getter
	private Textbox certName;
	
	/**
	 * 发证机关.
	 */
	@Setter
	@Getter
	private Textbox certOrg;

	/**
	 * 证件地址.
	 */
	@Setter
	@Getter
	private Textbox certAddress;
	/**
	 * 证件号码.
	 */
	@Setter
	@Getter
	private Textbox certNumber;
	/**
	 * 证件种类.
	 */
	@Setter
	@Getter
	private Listbox certSort;

	/**
	 * 类型
	 */
	@Getter
	@Setter
	private Listbox identityCardId;

	/****
	 * ======================= 参与人联系人 =======================
	 */
	@Setter
	@Getter
	private Listbox headFlag;

	@Setter
	@Getter
	private TreeChooserBandbox contactType;
	/**
	 * 联系人名称
	 */
	@Setter
	@Getter
	private Textbox contactName;
	/**
	 * 联系人性别
	 */
	@Setter
	@Getter
	private Listbox contactGender;

	/**
	 * 联系地址 contactAddress
	 */
	@Setter
	@Getter
	private Textbox contactAddress;

	/**
	 * 联系单位
	 */
	@Setter
	@Getter
	private Textbox contactEmployer;

	/**
	 * 家庭电话
	 */
	@Setter
	@Getter
	private Textbox homePhone;

	/**
	 * 办公电话
	 */
	@Setter
	@Getter
	private Textbox officePhone;

	/**
	 * 移动电话
	 */
	@Setter
	@Getter
	private Textbox mobilePhone;

	/**
	 * 电信内部邮箱
	 */
	@Setter
	@Getter
	private Textbox innerEmail;

	/**
	 * 集团统一邮箱
	 */
	@Setter
	@Getter
	private Textbox grpUnEmail;

	/**
	 * 移动电话(备用)
	 */
	@Setter
	@Getter
	private Textbox mobilePhoneSpare;

	/**
	 * 邮箱
	 */
	@Setter
	@Getter
	private Textbox email;

	/**
	 * 邮政编码
	 */
	@Setter
	@Getter
	private Textbox postCode;

	/**
	 * 邮件地址
	 */
	@Setter
	@Getter
	private Textbox postAddress;

	/**
	 * 传真
	 */
	@Setter
	@Getter
	private Textbox fax;

	/**
	 * QQ
	 */
	@Setter
	@Getter
	private Textbox qqNumber;

	/**
	 * 详细信息
	 */
	@Setter
	@Getter
	private Textbox contactDesc;

	/****
	 * ======================= 参与人个人信息 =======================
	 */
	/**
	 * 出生日期.
	 */
	@Setter
	@Getter
	private Datebox birthday;

	/**
	 * 婚姻状况.
	 */
	@Setter
	@Getter
	private TreeChooserBandbox marriageStatus;

	/**
	 * 政治面貌.
	 */
	@Setter
	@Getter
	private Listbox politicsStatus;

	/**
	 * 教育水平.
	 */
	@Setter
	@Getter
	private Listbox educationLevel;

	/**
	 * 性别.
	 */
	@Setter
	@Getter
	private Listbox gender;

	/**
	 * 国籍.
	 */
	@Setter
	@Getter
	private TreeChooserBandbox nationality;

	/**
	 * 民族.
	 */
	@Setter
	@Getter
	private Listbox nation;

	/**
	 * 祖籍.
	 */
	@Setter
	@Getter
	private Textbox nativePlace;
	/**
	 * 单位.
	 */
	@Setter
	@Getter
	private Textbox employer;
	/**
	 * 宗教.
	 */
	@Setter
	@Getter
	private Listbox religion;
	/**
	 * 同名编码.
	 */
	@Setter
	@Getter
	private Textbox sameNameCode;

	/****
	 * ======================= 组织或法人信息 =======================
	 */
	/**
	 * 参与人组织类型.
	 */
	@Setter
	@Getter
	private Listbox orgType;

	/**
	 * 组织简介.
	 */
	@Setter
	@Getter
	private Textbox orgContent;

	/**
	 * 组织规模.
	 */
	@Setter
	@Getter
	private Listbox orgScale;

	/**
	 * 负责人信息.
	 */
	@Setter
	@Getter
	private Textbox principal;

	/**
	 * 保存按钮 .
	 */
	@Getter
	@Setter
	private Button saveBtn;

	@Setter
	@Getter
	private Groupbox pel;

	@Setter
	@Getter
	private Groupbox orgs;

	@Setter
	@Getter
	private Groupbox panPartyCer;

	@Setter
	@Getter
	private Groupbox panPartyConInfo;
	@Getter
	@Setter
	private StaffRoleTreeBandboxExt staffRoleBandboxExt;

	@Setter
	@Getter
	private Button autogenerationBtn;
}
