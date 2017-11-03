package cn.ffcs.uom.dataPermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;

public class RoleOrganizationListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel roleOrganizationListboxComp;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox roleOrganizationListBox;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging roleOrganizationListPaging;
	/**
	 * 组织新增按钮.
	 */
	@Getter
	@Setter
	private Button addRoleOrganizationButton;
	/**
	 * 组织删除按钮.
	 */
	@Getter
	@Setter
	private Button delRoleOrganizationButton;

}
