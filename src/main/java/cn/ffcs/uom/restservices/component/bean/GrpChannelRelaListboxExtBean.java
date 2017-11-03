package cn.ffcs.uom.restservices.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class GrpChannelRelaListboxExtBean {
	@Getter
	@Setter
	private Panel grpChannelRelaListboxExtPanel;
	@Getter
	@Setter
	private Listbox grpChannelRelaListbox;
	@Getter
	@Setter
	private Paging grpChannelRelaListboxPaging;

	/**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div grpChannelRelaSearchDiv;
	/**
	 * 渠道编码.
	 **/
	@Getter
	@Setter
	private Textbox channelNbr;
	/**
	 * 渠道名称.
	 **/
	@Getter
	@Setter
	private Textbox channelName;
}
