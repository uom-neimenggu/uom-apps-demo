package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

public class TreeBindingEditBean {
	@Getter
	@Setter
	private Window treeBindingEditWin;
	@Getter
	@Setter
	private Listbox treeListbox;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
