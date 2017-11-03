package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class SystemIntfInfoConfigListExtBean {
	@Getter
	@Setter
	private Panel systemIntfInfoConfigListPanel;
	@Getter
	@Setter
	private Toolbarbutton addSystemIntfInfoConfigButton;
	@Getter
	@Setter
	private Toolbarbutton editSystemIntfInfoConfigButton;
	@Getter
	@Setter
	private Toolbarbutton delSystemIntfInfoConfigButton;
	@Getter
	@Setter
	private Listbox systemIntfInfoConfigListBox;
	@Getter
	@Setter
	private Paging systemIntfInfoConfigListPaging;

}
