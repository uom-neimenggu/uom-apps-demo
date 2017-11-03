package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class TreeOrgOrgTypeListExtBean {
	@Getter
	@Setter
	private Panel treeOrgOtRuleListPanel;
	@Getter
	@Setter
	private Toolbarbutton addTreeOrgOtRuleButton;
	@Getter
	@Setter
	private Toolbarbutton delTreeOrgOtRuleButton;
	@Getter
	@Setter
	private Listbox treeOrgOtRuleListBox;
	@Getter
	@Setter
	private Paging treeOrgOtRuleListPaging;

}
