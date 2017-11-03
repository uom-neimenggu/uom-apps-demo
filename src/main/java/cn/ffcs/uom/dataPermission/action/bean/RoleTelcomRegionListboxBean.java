package cn.ffcs.uom.dataPermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;

public class RoleTelcomRegionListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel roleTelcomRegionListboxComp;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox RoleTelcomRegionListBox;
	/**
	 * 组织新增按钮.
	 */
	@Getter
	@Setter
	private Button addRoleTelcomRegionButton;
	/**
	 * 组织删除按钮.
	 */
	@Getter
	@Setter
	private Button delRoleTelcomRegionButton;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging RoleTelcomRegionListPaging;
}
