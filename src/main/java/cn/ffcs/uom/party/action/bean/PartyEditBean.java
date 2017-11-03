package cn.ffcs.uom.party.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;
import cn.ffcs.uom.politicallocation.component.PoliticalLocationTreeBandbox;
import cn.ffcs.uom.staff.component.StaffExtendAttrExt;

/**
 * 参与人、参与人证件类型、参与人联系方式 ZUL Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-30
 * @功能说明：
 * 
 */
public class PartyEditBean {

	/**
	 * Window
	 */
	@Setter
	@Getter
	private Window partyEditWindow;
	
	/**
	 * Div
	 */
	@Setter
	@Getter
	private Div partyEditDiv;
	/**
	 * 参与人信息
	 */
	@Setter
	@Getter
	private Groupbox partyInfo;
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
	 * ======================= =======================
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

	@Setter
	@Getter
	private Groupbox pel;

	@Setter
	@Getter
	private Groupbox orgs;

	@Setter
	@Getter
	private Groupbox staffInfo;

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
	 * 保存.
	 */
	@Setter
	@Getter
	private Button saveStaff;
	/**
	 * 取消.
	 */
	@Setter
	@Getter
	private Button cancelStaff;
	/**
	 * 编辑按钮.
	 **/
	@Getter
	@Setter
	private Toolbarbutton editButton;
	/**
	 * 保存按钮.
	 **/
	@Getter
	@Setter
	private Toolbarbutton saveButton;
	/**
	 * 回复按钮.
	 **/
	@Getter
	@Setter
	private Toolbarbutton recoverButton;

	@Getter
	@Setter
	private Toolbar btnToolBar;

	@Getter
	@Setter
	private Toolbar viewBtnTB;
	// --------------------------参与人证件信息

	@Setter
	@Getter
	private Groupbox panPartyCer;
	/**
	 * 参与人证件类型.
	 */
	@Setter
	@Getter
	private Listbox certType;

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
	 * 证件名.
	 */
	@Setter
	@Getter
	private Textbox certName;
	
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

	// -----------------------------参与人联系人信息

	@Setter
	@Getter
	private Groupbox panPartyConInfo;

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
	// ---------------------------------

	/**
	 * 添加员工信息lab
	 */
	@Setter
	@Getter
	private Label addStaffInfoLab;

	/**
	 * 是否添加员工.
	 */
	@Setter
	@Getter
	private Listbox addStaffInfo;

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
	 * 兼职/全职.
	 */
	@Setter
	@Getter
	private Listbox partTime;

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
	 * 员工账号.
	 */
	@Setter
	@Getter
	private Textbox staffAcct;

	@Setter
	@Getter
	private Textbox staffPassword;

	/**
	 * 员工扩展属性
	 */
	@Setter
	@Getter
	private StaffExtendAttrExt staffExtendAttrExt;

	@Setter
	@Getter
	private Button autogenerationBtn;

}
