package cn.ffcs.uom.activation.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

public class StaffActivationListboxBean {

	/**
	 * ListBox
	 */
	@Getter
	@Setter
	private Listbox staffActivationListbox;

	/**
	 * 分页插件
	 */
	@Getter
	@Setter
	private Paging staffActivationListboxPaging;

	/**
	 * 员工批量激活按钮.
	 */
	@Getter
	@Setter
	private Button staffActBatchButton;

	@Getter
	@Setter
	private Textbox staffCode;

	@Getter
	@Setter
	private Textbox staffAccount;

	@Getter
	@Setter
	private Textbox staffName;

}
