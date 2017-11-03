package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.treechooser.component.TreeChooserBandbox;

public class TreeStaffSftEditBean {
	@Getter
	@Setter
	private Window treeStaffSftEditWin;
	@Getter
	@Setter
	private TreeChooserBandbox workProp;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
