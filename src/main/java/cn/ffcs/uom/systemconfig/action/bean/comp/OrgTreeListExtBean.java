package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class OrgTreeListExtBean {
	@Getter
	@Setter
	private Panel orgTreeListPanel;
	@Getter
	@Setter
	private Toolbarbutton addOrgTreeButton;
	@Getter
	@Setter
	private Toolbarbutton editOrgTreeButton;
	@Getter
	@Setter
	private Toolbarbutton delOrgTreeButton;
	@Getter
	@Setter
	private Toolbarbutton ftpIncreaseTimerSwitchButton;
	@Getter
	@Setter
	private Toolbarbutton ftpAllTimerSwitchButton;
	@Getter
	@Setter
	private Listbox orgTreeListBox;
	@Getter
	@Setter
	private Paging orgTreeListPaging;

}
