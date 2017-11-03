package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class InformConfigEditBean {
	@Getter
	@Setter
	private Window informConfigEditWin;
	@Getter
	@Setter
	private Textbox informMethodName;
	@Getter
	@Setter
	private Textbox informMethodCode;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
