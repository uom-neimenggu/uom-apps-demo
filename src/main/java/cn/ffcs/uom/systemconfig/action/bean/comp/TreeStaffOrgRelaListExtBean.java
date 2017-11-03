package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class TreeStaffOrgRelaListExtBean {
	@Getter
	@Setter
	private Panel treeStaffOrRuleListPanel;
	@Getter
	@Setter
	private Toolbarbutton addTreeStaffOrRuleButton;
	@Getter
	@Setter
	private Toolbarbutton delTreeStaffOrRuleButton;
	@Getter
	@Setter
	private Listbox treeStaffOrRuleListBox;
	@Getter
	@Setter
	private Paging treeStaffOrRuleListPaging;

}
