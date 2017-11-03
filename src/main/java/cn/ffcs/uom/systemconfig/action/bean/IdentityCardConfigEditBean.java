package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class IdentityCardConfigEditBean {
	@Getter
	@Setter
	private Window identityCardConfigEditWin;
	@Getter
	@Setter
	private Textbox identityCardId;
	@Getter
	@Setter
	private Textbox identityCardName;
	@Getter
	@Setter
	private Textbox identityCardPrefix;
	@Getter
	@Setter
	private Listbox identityCardSwitch;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
