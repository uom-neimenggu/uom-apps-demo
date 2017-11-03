package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class SystemMessageConfigListExtBean {
	@Getter
	@Setter
	private Panel systemMessageConfigListPanel;
	@Getter
	@Setter
	private Toolbarbutton addSystemMessageConfigButton;
	@Getter
	@Setter
	private Toolbarbutton editSystemMessageConfigButton;
	@Getter
	@Setter
	private Toolbarbutton delSystemMessageConfigButton;
	@Getter
	@Setter
	private Listbox systemMessageConfigListBox;
	@Getter
	@Setter
	private Paging systemMessageConfigListPaging;

}
