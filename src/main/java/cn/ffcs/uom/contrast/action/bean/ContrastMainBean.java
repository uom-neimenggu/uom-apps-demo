package cn.ffcs.uom.contrast.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ContrastMainBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window contrastMainWin;

	/**
	 * Panel.
	 **/
	@Getter
	@Setter
	private Panel contrastListboxPanel;

	/**
	 * ListBox
	 */
	@Getter
	@Setter
	private Listbox contrastListbox;

	/**
	 * 分页插件
	 */
	@Getter
	@Setter
	private Paging contrastListboxPaging;

	@Getter
	@Setter
	private Textbox uomNbr;

	@Getter
	@Setter
	private Textbox uomAccount;

	@Getter
	@Setter
	private Textbox ossNbr;

	@Getter
	@Setter
	private Textbox ossAccount;

	@Getter
	@Setter
	private Textbox ossName;

	@Getter
	@Setter
	private Textbox ossCertNumber;

}
