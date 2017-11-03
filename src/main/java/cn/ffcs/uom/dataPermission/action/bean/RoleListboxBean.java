package cn.ffcs.uom.dataPermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;

public class RoleListboxBean {
	/**
	 *panel.
	 **/
	@Getter
	@Setter
	private Panel roleListboxComp;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox roleListBox;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging roleListPaging;
}
