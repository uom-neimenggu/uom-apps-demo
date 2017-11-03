package cn.ffcs.uom.gridUnit.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;

public class GridUnitRefListboxExtBean {

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div gridUnitRefSearchDiv;

	/**
	 * 全息网格标识.
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

	/**
	 * 网格组织编码.
	 **/
	@Getter
	@Setter
	private Textbox orgCode;
	/**
	 * 网格名称.
	 **/
	@Getter
	@Setter
	private Textbox orgName;

	@Getter
	@Setter
	private Listbox gridUnitRefListbox;

	@Getter
	@Setter
	private Paging gridUnitRefListboxPaging;

}
