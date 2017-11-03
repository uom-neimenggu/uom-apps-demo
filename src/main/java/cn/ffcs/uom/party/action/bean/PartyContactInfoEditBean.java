package cn.ffcs.uom.party.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;

public class PartyContactInfoEditBean {

	@Getter
	@Setter
	private Window partyContactInfoEditWin;

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
	
	/**
	 * 变更原因
	 */
	@Setter
	@Getter
	private Textbox reason;

	@Setter
	@Getter
	private Button autogenerationBtn;

	@Setter
	@Getter
	private Button savePartyContactInfo;

	@Setter
	@Getter
	private Button cancelPartyContactInfo;

}
