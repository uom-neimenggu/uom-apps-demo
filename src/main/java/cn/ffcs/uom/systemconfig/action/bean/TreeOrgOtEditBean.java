package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;

public class TreeOrgOtEditBean {
	@Getter
	@Setter
	private Window treeOrgOtEditWin;
	@Getter
	@Setter
	private TreeChooserBandbox orgTypeCd;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
