package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 * Bean
 * 
 * @author
 **/
public class MdmContractListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel mdmContractListboxPnl;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox mdmContractListbox;
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
	private Paging mdmContractListPaging;
}
