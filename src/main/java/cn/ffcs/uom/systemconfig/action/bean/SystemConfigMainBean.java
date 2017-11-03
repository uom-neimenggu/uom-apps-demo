package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class SystemConfigMainBean {
	@Getter
	@Setter
	private Window systemConfigMainWin;
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
	private Button findSysClassButton;
	@Getter
	@Setter
	private Listbox sysClassListBox;
	@Getter
	@Setter
	private Paging sysClassPaging;
	@Getter
	@Setter
	private Tab baseInfoTab;
	@Getter
	@Setter
	private Tab attrInfoTab;
}
