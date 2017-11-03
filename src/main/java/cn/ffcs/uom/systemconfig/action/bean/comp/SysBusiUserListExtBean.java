package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class SysBusiUserListExtBean {
	@Getter
	@Setter
	private Panel businessSystemListPanel;
	@Getter
	@Setter
	private Toolbarbutton addBusinessSystemButton;
	@Getter
	@Setter
	private Toolbarbutton delBusinessSystemButton;
	@Getter
	@Setter
	private Listbox businessSystemListBox;
	@Getter
	@Setter
	private Paging businessSystemListPaging;

}
