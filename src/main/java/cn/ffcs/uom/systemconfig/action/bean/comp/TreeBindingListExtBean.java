package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Toolbarbutton;

public class TreeBindingListExtBean {
	@Getter
	@Setter
	private Toolbarbutton addTreeBindingButton;
	@Getter
	@Setter
	private Toolbarbutton delTreeBindingButton;
	@Getter
	@Setter
	private Listbox treeBindingListBox;
	@Getter
	@Setter
	private Paging treeBindingListPaging;

}
