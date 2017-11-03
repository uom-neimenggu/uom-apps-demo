package cn.ffcs.uom.party.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * 参与人证件Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-8
 * @功能说明：
 * 
 */
public class PartyCertificationBean {

	@Getter
	@Setter
	private Window partyCertificationEditWin;
	
	/**
	 * 参与人证件名.
	 */
	@Setter
	@Getter
	private Textbox certName;

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
	 * 证件号码.
	 */
	@Setter
	@Getter
	private Textbox certNumber;
	
	/**
	 * 变更原因.
	 */
	@Setter
	@Getter
	private Textbox reason;
	
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

}
