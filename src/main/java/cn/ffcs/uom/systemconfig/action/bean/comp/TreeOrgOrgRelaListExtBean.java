package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Toolbarbutton;

public class TreeOrgOrgRelaListExtBean {
	@Getter
	@Setter
	private Toolbarbutton addTreeOrgOrRuleButton;
	@Getter
	@Setter
	private Toolbarbutton delTreeOrgOrRuleButton;
	@Getter
	@Setter
	private Listbox treeOrgOrRuleListBox;
	@Getter
	@Setter
	private Paging treeOrgOrRuleListPaging;

}
