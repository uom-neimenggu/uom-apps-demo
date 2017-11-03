package cn.ffcs.uom.hisQuery.action.bean;

import lombok.Getter;
import lombok.Setter;


import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;


public class HisQueryMainBean {
	@Getter
	@Setter
	private Window hisQueryWin;
	@Getter
	@Setter
	private Longbox orgId;
	@Getter
	@Setter
	private Datebox queryDate;
	@Getter
	@Setter
	private Listbox orgHisListbox;
	@Getter
	@Setter
	private Paging orgListboxPaging;
	@Getter
	@Setter
	private Listbox positionHisListbox;
	@Getter
	@Setter
	private Paging positionListboxPaging;
	@Getter
	@Setter
	private Listbox staffHisListbox;
	@Getter
	@Setter
	private Paging staffListboxPaging;
	@Getter
	@Setter
	private Listbox orgBsRelaHisListbox;
	@Getter
	@Setter
	private Paging orgBsRelaListboxPaging;
	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab tab;
	/**
	 * Tabbox.
	 */
	@Getter
	@Setter
	private Tabbox tabBox;
	/**
	 * 树panel
	 */
	@Getter
	@Setter
	private Panel orgRelHisTreePanel;
	/**
	 * 查询组织详情按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton showOrgDetailBtn;
	/**
	 * 查询岗位详情按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton showPositionetailBtn;
	/**
	 * 查询员工详情按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton showStaffDetailBtn;
	/**
	 * 查询组织业务关系详情按钮
	 */
	@Getter
	@Setter
	private Toolbarbutton showOrgBsRelaDetailBtn;
}
