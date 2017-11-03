package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class RbacBusinessSystemEditExtBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacBusinessSystemEditExtPanel;

	/**
	 * 系统标识.
	 **/
	@Getter
	@Setter
	private Longbox rbacBusinessSystemId;

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
	 * 编辑按钮.
	 */
	@Getter
	@Setter
	private Button editButton;

	/**
	 * 保存按钮.
	 */
	@Getter
	@Setter
	private Button saveButton;

	/**
	 * 恢复按钮.
	 */
	@Getter
	@Setter
	private Button recoverButton;

}
