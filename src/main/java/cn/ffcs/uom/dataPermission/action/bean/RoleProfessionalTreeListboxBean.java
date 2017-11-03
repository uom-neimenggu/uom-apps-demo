package cn.ffcs.uom.dataPermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;

public class RoleProfessionalTreeListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel roleProfessionalTreeListboxComp;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox roleProfessionalTreeListBox;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging roleProfessionalTreeListPaging;
	/**
	 * 组织新增按钮.
	 */
	@Getter
	@Setter
	private Button addRoleProfessionalTreeButton;
	/**
	 * 组织删除按钮.
	 */
	@Getter
	@Setter
	private Button delRoleProfessionalTreeButton;

}
