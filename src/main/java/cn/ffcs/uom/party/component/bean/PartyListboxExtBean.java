package cn.ffcs.uom.party.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

/**
 * 参与人信息的页面Bean显示ZUL .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-30
 * @功能说明：
 * 
 */
public class PartyListboxExtBean {

	@Getter
	@Setter
	private Listbox partyListbox;

	@Getter
	@Setter
	private Paging partyListboxPaging;

	@Getter
	@Setter
	private Button addPartyButton;

	@Getter
	@Setter
	private Button viewButton;

	@Getter
	@Setter
	private Button editPartyButton;

	@Getter
	@Setter
	private Button delPartyButton;

	@Getter
	@Setter
	private Button selectPartyButton;

	@Getter
	@Setter
	private Div partyWindowDiv;

	@Getter
	@Setter
	private Div partyBandboxDiv;

	@Getter
	@Setter
	private Div partyBatchDiv;

	@Getter
	@Setter
	private Label partyNameLab;
	/**
	 * 参与人名称
	 */
	@Getter
	@Setter
	private Textbox partyName;

	/**
	 * 参与人简拼
	 */
	@Getter
	@Setter
	private Textbox partyAbbrname;

	/**
	 * 参与人英文名
	 */
	@Getter
	@Setter
	private Textbox englishName;

	@Getter
	@Setter
	private Label partyTypeLab;
	/**
	 * 参与人类型
	 */
	@Getter
	@Setter
	private Listbox partyType;

	/**
	 * 参与人证件号码
	 */
	@Getter
	@Setter
	private Textbox certNumber;

	@Getter
	@Setter
	private Label mobilePhoneLab;
	/**
	 * 联系号码-电话号码
	 */
	@Getter
	@Setter
	private Textbox mobilePhone;

	@Getter
	@Setter
	private Label staffAccountLab;
	/**
	 * 员工账号
	 */
	@Getter
	@Setter
	private Textbox staffAccount;

	@Getter
	@Setter
	private Label freePartyLab;
	/**
	 * 是否游离
	 */
	@Getter
	@Setter
	private Listbox freeParty;
}
