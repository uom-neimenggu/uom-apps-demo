package cn.ffcs.uom.contrast.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class StaffContrastMainBean {
	@Getter
	@Setter
	private Window staffContrastMainWin;
	@Getter
	@Setter
	private Panel staffContrastListboxPanel;
	@Getter
	@Setter
	private Textbox oldStaffAccount;
	@Getter
	@Setter
	private Textbox ossAccount;
	@Getter
	@Setter
	private Textbox staffName;
	@Getter
	@Setter
	private Listbox staffContrastListbox;
	@Getter
	@Setter
	private Paging staffContrastListboxPaging;

}
