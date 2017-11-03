package cn.ffcs.uom.staffrole.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class StaffRoleListboxBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffRoleMainWin;
	/**
	 * listbox.
	 **/
	@Getter
	@Setter
	private Listbox staffRoleListbox;
	/**
	 * paging.
	 **/
	@Getter
	@Setter
	private Paging staffRoleListboxPaging;
	/**
	 * 角色新增按钮.
	 */
	@Getter
	@Setter
	private Button addStaffRoleButton;
	/**
	 * 导入
	 */
	@Getter
	@Setter
	private Button uploadButton;
	/**
	 * 模板下载
	 */
	@Getter
	@Setter
	private Button downloadButton;
	/**
	 * 角色查看按钮.
	 */
	@Getter
	@Setter
	private Button viewStaffRoleButton;
	/**
	 * 角色编辑按钮.
	 */
	@Getter
	@Setter
	private Button editStaffRoleButton;
	/**
	 * 角色删除按钮.
	 */
	@Getter
	@Setter
	private Button delStaffRoleButton;
	/**
	 * staffRoleSearchWindowDiv.
	 */
	@Getter
	@Setter
	private Div staffRoleSearchWindowDiv;
	/**
	 * staffRoleWindowDiv.
	 */
	@Getter
	@Setter
	private Div staffRoleWindowDiv;
	/**
	 * 角色名称.
	 **/
	@Getter
	@Setter
	private Textbox staffRoleName;
	/**
	 * 角色编码.
	 **/
	@Getter
	@Setter
	private Textbox staffRoleCode;
	/**
	 * 角色类型.
	 **/
	@Getter
	@Setter
	private Listbox staffRoleType;
	/**
	 * 查询按钮.
	 */
	@Getter
	@Setter
	private Button queryStaffRole;
	/**
	 * staffRoleWindowDiv.
	 */
	@Getter
	@Setter
	private Div staffRoleBandboxDiv;
	/**
	 * 关闭按钮.
	 */
	@Getter
	@Setter
	private Button closePosiitonButton;
	/**
	 * 清空按钮.
	 */
	@Getter
	@Setter
	private Button cleanStaffRoleButton;
	/**
	 * 员工角色.
	 **/
	@Getter
	@Setter
	private Textbox staffCode;
	/**
	 * 员工名称.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
	/**
	 * 员工帐号.
	 **/
	@Getter
	@Setter
	private Textbox staffAccount;
}
