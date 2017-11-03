package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

public class SysClassBaseInfoBean {
	@Getter
	@Setter
	private Window sysClassBaseInfoWin;
	@Getter
	@Setter
	private Toolbarbutton addSysClassButton;
	@Getter
	@Setter
	private Toolbarbutton editSysClassButton;
	@Getter
	@Setter
	private Toolbarbutton saveSysClassButton;
	@Getter
	@Setter
	private Toolbarbutton recoverSysClassButton;
	@Getter
	@Setter
	private Toolbarbutton deleteSysClassButton;
	@Getter
	@Setter
	private Textbox className;
	@Getter
	@Setter
	private Textbox javaCode;
	@Getter
	@Setter
	private Textbox tableName;
	@Getter
	@Setter
	private Combobox tableType;
	@Getter
	@Setter
	private Textbox seqName;
	@Getter
	@Setter
	private Textbox hisTableName;
	@Getter
	@Setter
	private Textbox hisSeqName;
	@Getter
	@Setter
	private Checkbox isEntity;
	@Getter
	@Setter
	private Textbox classDesc;
}
