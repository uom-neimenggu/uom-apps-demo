package cn.ffcs.uom.rolePermission.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class RbacResourceListboxBean {

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel rbacResourcePanel;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacResourceSearchDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacResourceOptDiv;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div rbacResourceBandboxDiv;

	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox rbacResourceListbox;

	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging rbacResourceListboxPaging;

	/**
	 * 资源新增按钮.
	 */
	@Getter
	@Setter
	private Button addRbacResourceButton;

	/**
	 * 资源查看按钮.
	 */
	@Getter
	@Setter
	private Button viewRbacResourceButton;

	/**
	 * 资源编辑按钮.
	 */
	@Getter
	@Setter
	private Button editRbacResourceButton;

	/**
	 * 资源删除按钮.
	 */
	@Getter
	@Setter
	private Button delRbacResourceButton;

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
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button queryRbacResource;

	/**
	 * 重置按钮.
	 */
	@Getter
	@Setter
	private Button resetRbacResource;

	/**
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closeRbacResourceButton;

	/**
	 * 清空按钮.
	 */
	@Getter
	@Setter
	private Button cleanRbacResourceButton;

}
