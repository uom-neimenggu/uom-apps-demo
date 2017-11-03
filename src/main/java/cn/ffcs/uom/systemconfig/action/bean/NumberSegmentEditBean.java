package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class NumberSegmentEditBean {
	@Getter
	@Setter
	private Window numberSegmentEditWin;
	@Getter
	@Setter
	private Listbox operationBusiness;
	@Getter
	@Setter
	private Textbox numberSegment;
	@Getter
	@Setter
	private Textbox remarks;
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
