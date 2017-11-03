package cn.ffcs.uom.restservices.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class GrpChannelListboxExtBean {

	/**
	 * ListBox
	 */
	@Getter
	@Setter
	private Listbox grpChannelListbox;

	/**
	 * 分页插件
	 */
	@Getter
	@Setter
	private Paging grpChannelListboxPaging;

	/**
	 * 渠道选择按钮.
	 */
	@Getter
	@Setter
	private Button selectGrpChannelButton;

	@Getter
	@Setter
	private Div grpChannelWindowDiv;

	@Getter
	@Setter
	private Div grpChannelBandboxDiv;

	@Getter
	@Setter
	private Textbox channelNbr;

	@Getter
	@Setter
	private Textbox channelName;

	/**
	 * 更新
	 */
	@Getter
	@Setter
	private Button updateGrpChannelButton;

}
