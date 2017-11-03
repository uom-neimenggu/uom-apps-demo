package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class IndicatorBaseInfoEditBean {
	@Getter
	@Setter
	private Window indicatorBaseInfoEditWin;
	@Getter
	@Setter
	private Textbox perOfPass;
	@Getter
	@Setter
	private Textbox salt;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
