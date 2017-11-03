package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class RbacResourceEditBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window rbacResourceEditWindow;

	/**
	 * 资源编码.
	 **/
	@Getter
	@Setter
	private Textbox rbacResourceCode;

	/**
	 * 资源名称.
	 **/
	@Getter
	@Setter
	private Textbox rbacResourceName;

	/**
	 * 叶子节点.
	 **/
	@Getter
	@Setter
	private Listbox rbacResourceLeaf;

	/**
	 * 资源URL.
	 **/
	@Getter
	@Setter
	private Textbox rbacResourceUrl;

	/**
	 * 资源描述.
	 **/
	@Getter
	@Setter
	private Textbox rbacResourceDesc;

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
