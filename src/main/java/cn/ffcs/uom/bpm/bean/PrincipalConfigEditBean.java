package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class PrincipalConfigEditBean {
	@Getter
	@Setter
	private Window principalConfigEditWin;
	@Getter
	@Setter
	private Textbox principalName;
	@Getter
	@Setter
	private Textbox cellNum;
	@Getter
	@Setter
	private Textbox email;
	@Getter
	@Setter
	private Textbox sort;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
