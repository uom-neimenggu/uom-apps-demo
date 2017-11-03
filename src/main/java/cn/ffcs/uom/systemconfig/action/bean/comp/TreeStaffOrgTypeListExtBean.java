package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class TreeStaffOrgTypeListExtBean {
	@Getter
	@Setter
	private Panel treeStaffOtRuleListPanel;
	@Getter
	@Setter
	private Toolbarbutton addTreeStaffOtRuleButton;
	@Getter
	@Setter
	private Toolbarbutton delTreeStaffOtRuleButton;
	@Getter
	@Setter
	private Listbox treeStaffOtRuleListBox;
	@Getter
	@Setter
	private Paging treeStaffOtRuleListPaging;

}
