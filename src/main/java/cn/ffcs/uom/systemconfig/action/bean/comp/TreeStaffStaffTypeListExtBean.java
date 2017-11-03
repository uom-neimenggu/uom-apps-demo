package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class TreeStaffStaffTypeListExtBean {
	@Getter
	@Setter
	private Panel treeStaffSftRuleListPanel;
	@Getter
	@Setter
	private Toolbarbutton addTreeStaffSftRuleButton;
	@Getter
	@Setter
	private Toolbarbutton delTreeStaffSftRuleButton;
	@Getter
	@Setter
	private Listbox treeStaffSftRuleListBox;
	@Getter
	@Setter
	private Paging treeStaffSftRuleListPaging;
}
