package cn.ffcs.uom.restservices.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

public class GrpOperatorsListboxExtBean {

	/**
	 * ListBox
	 */
	@Getter
	@Setter
	private Listbox grpOperatorsListbox;

	/**
	 * 分页插件
	 */
	@Getter
	@Setter
	private Paging grpOperatorsListboxPaging;

	/**
	 * 经营主体选择按钮.
	 */
	@Getter
	@Setter
	private Button selectGrpOperatorsButton;

	@Getter
	@Setter
	private Div grpOperatorsWindowDiv;

	@Getter
	@Setter
	private Div grpOperatorsBandboxDiv;

	@Getter
	@Setter
	private Textbox operatorsNbr;

	@Getter
	@Setter
	private Textbox operatorsName;

	/**
	 * 更新
	 */
	@Getter
	@Setter
	private Button updateGrpOperatorsButton;

}
