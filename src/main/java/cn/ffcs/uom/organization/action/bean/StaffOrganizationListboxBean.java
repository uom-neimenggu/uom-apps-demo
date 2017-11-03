package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 *员工组织关系Bean
 * 
 * @author
 **/
public class StaffOrganizationListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel staffOrganizationListboxComp;
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div staffOrganizationSearchDiv;
	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div staffAccountDiv;
	/**
	 * 员工姓名.
	 **/
	@Getter
	@Setter
	private Textbox staffName;
	/**
	 * 员工账号.
	 **/
	@Getter
	@Setter
	private Textbox staffAccount;
	/**
	 * OA账号.
	 **/
	@Getter
	@Setter
	private Textbox userCode;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox StaffOrganizationListBox;
	/**
	 * 组织新增按钮.
	 */
	@Getter
	@Setter
	private Button addStaffOrganizationButton;
	/**
	 * 组织修改按钮.
	 */
	@Getter
	@Setter
	private Button updateStaffOrganizationButton;
	/**
	 * 组织删除按钮.
	 */
	@Getter
	@Setter
	private Button delStaffOrganizationButton;
	/**
	 * 员工移动按钮.
	 */
	@Getter
	@Setter
	private Button moveStaffOrganizationButton;
	/**
	 * 员工查看按钮.
	 */
	@Getter
	@Setter
	private Button viewStaffOrganizationButton;
	/**
	 * 新增员工组织按钮.
	 */
	@Getter
	@Setter
	private Button addStaffOrganizationButtonAll;
	
	/**
	 * 批量导入员工组织关系，增删改 xiaof
	 */
	@Getter
	@Setter
	private Button importStaffOrganizationButton;
	/**
	 * 修改员工组织按钮.
	 */
	@Getter
	@Setter
	private Button updateStaffOrganizationButtonAll;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging StaffOrganizationListPaging;
}
