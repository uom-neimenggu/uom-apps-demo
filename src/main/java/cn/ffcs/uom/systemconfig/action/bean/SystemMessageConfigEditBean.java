package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.systemconfig.action.comp.MessageConfigBandboxExt;

public class SystemMessageConfigEditBean {
	@Getter
	@Setter
	private Window sysMsgConfigEditWin;
	@Getter
	@Setter
	private MessageConfigBandboxExt messageConfigBandboxExt;
	@Getter
	@Setter
	private Textbox systemCode;
	@Getter
	@Setter
	private Listbox systemMessageSwitch;
	@Getter
	@Setter
	private Textbox contactName;
	@Getter
	@Setter
	private Textbox telephoneNumber;
	@Getter
	@Setter
	private Longbox noticeOrder;
	@Getter
	@Setter
	private Textbox emailAddress;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
