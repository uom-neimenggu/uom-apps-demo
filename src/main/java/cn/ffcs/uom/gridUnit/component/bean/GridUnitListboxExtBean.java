package cn.ffcs.uom.gridUnit.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

public class GridUnitListboxExtBean {

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div gridUnitSearchDiv;

	/**
	 * 网格单元ID.
	 **/
	@Getter
	@Setter
	private Longbox mmeFid;

	/**
	 * 网格单元名称.
	 **/
	@Getter
	@Setter
	private Textbox gridName;

	@Getter
	@Setter
	private Listbox gridUnitListbox;

	@Getter
	@Setter
	private Paging gridUnitListboxPaging;
	/**
	 * bandbox使用
	 */
	@Getter
	@Setter
	private Div gridUnitBandboxDiv;

}
