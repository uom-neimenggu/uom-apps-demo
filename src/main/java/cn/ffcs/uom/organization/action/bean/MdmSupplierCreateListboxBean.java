package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 * Bean
 * 
 * @author
 **/
public class MdmSupplierCreateListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel mdmSupplierCreateListboxPnl;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox mdmSupplierCreateListbox;
	/**
	 * 名称.
	 **/
	@Getter
	@Setter
	private Textbox name;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging mdmSupplierCreateListPaging;
}
