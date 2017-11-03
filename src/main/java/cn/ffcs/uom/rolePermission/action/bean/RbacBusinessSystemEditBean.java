package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class RbacBusinessSystemEditBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window rbacBusinessSystemEditWindow;

	/**
	 * 系统编码.
	 **/
	@Getter
	@Setter
	private Textbox rbacBusinessSystemCode;

	/**
	 * 系统名称.
	 **/
	@Getter
	@Setter
	private Textbox rbacBusinessSystemName;

	/**
	 * 系统URL.
	 **/
	@Getter
	@Setter
	private Textbox rbacBusinessSystemUrl;

	/**
	 * 系统域.
	 **/
	@Getter
	@Setter
	private Listbox rbacBusinessSystemDomain;

	/**
	 * 系统描述.
	 **/
	@Getter
	@Setter
	private Textbox rbacBusinessSystemDesc;

	/**
	 * 确定按钮.
	 */
	@Getter
	@Setter
	private Button okButton;

	/**
	 * 取消按钮.
	 */
	@Getter
	@Setter
	private Button cancelButton;

}
