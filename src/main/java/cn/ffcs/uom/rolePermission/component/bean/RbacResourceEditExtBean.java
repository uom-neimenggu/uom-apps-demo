package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class RbacResourceEditExtBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacResourceEditExtPanel;

	/**
	 * 资源标识.
	 **/
	@Getter
	@Setter
	private Longbox rbacResourceId;

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
