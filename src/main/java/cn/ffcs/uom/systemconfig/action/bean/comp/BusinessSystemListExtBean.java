package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class BusinessSystemListExtBean {
	@Getter
	@Setter
	private Panel businessSystemListPanel;
	@Getter
	@Setter
	private Textbox systemCode;
	@Getter
	@Setter
	private Textbox systemName;
	@Getter
	@Setter
	private Toolbarbutton addBusinessSystemButton;
	@Getter
	@Setter
	private Toolbarbutton editBusinessSystemButton;
	@Getter
	@Setter
	private Toolbarbutton delBusinessSystemButton;
	@Getter
	@Setter
	private Toolbarbutton thresholdSwitchButton;
	@Getter
	@Setter
	private Toolbarbutton ftpNoticeSwitchButton;
	@Getter
	@Setter
	private Toolbarbutton ftpReplacementButton;
	@Getter
	@Setter
	private Toolbarbutton smsNoticeSwitchButton;
	@Getter
	@Setter
	private Toolbarbutton maiilSendSwitchButton;
	@Getter
	@Setter
	private Toolbarbutton ftpTimerSwitchButton;
	@Getter
	@Setter
	private Listbox businessSystemListBox;
	@Getter
	@Setter
	private Paging businessSystemListPaging;

}
