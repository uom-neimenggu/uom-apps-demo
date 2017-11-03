package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class FilterConfigEditBean {
	@Getter
	@Setter
	private Window filterConfigEditWin;
	@Getter
	@Setter
	private Textbox filterChar;
	@Getter
	@Setter
	private Listbox statusCd;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;

}
