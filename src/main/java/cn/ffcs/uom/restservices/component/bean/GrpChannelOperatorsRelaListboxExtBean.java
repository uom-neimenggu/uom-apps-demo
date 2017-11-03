package cn.ffcs.uom.restservices.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class GrpChannelOperatorsRelaListboxExtBean {
	@Getter
	@Setter
	private Panel grpChannelOperatorsRelaListboxExtPanel;
	@Getter
	@Setter
	private Listbox grpChannelOperatorsRelaListbox;
	@Getter
	@Setter
	private Paging grpChannelOperatorsRelaListboxPaging;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div grpChannelOperatorsRelaSearchDiv;
	/**
	 * 经营主体编码.
	 **/
	@Getter
	@Setter
	private Textbox channelNbr;
	/**
	 * 经营主体名称.
	 **/
	@Getter
	@Setter
	private Textbox channelName;
}
