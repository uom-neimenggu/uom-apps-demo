package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.telcomregion.component.TelcomRegionTreeBandbox;

public class ChannelPackareaRelationListboxBean {

	@Setter
	@Getter
	private Panel channelPackareaRelationListboxComp;

	@Getter
	@Setter
	private Button addChannelPackareaRelationButton;
	
	@Getter
	@Setter
	private Button editChannelPackareaRelationButton;

	@Getter
	@Setter
	private Button viewChannelPackareaRelationButton;

	@Getter
	@Setter
	private Button approveChannelPackareaRelationButton;
	
	@Getter
	@Setter
	private Button exportChannelPackareaRelationButton;

	@Getter
	@Setter
	private Button delChannelPackareaRelationButton;
	
	/**
	 * 电信管理区域
	 */
	@Getter
	@Setter
	private TelcomRegionTreeBandbox telcomRegion;

	/**
	 * 渠道组织编码
	 */
	@Setter
	@Getter
	private Textbox agentChannelNbr;

	/**
	 * 渠道组织名称
	 */
	@Setter
	@Getter
	private Textbox agentChannelName;

	/**
	 * 营销包区组织编码
	 */
	@Setter
	@Getter
	private Textbox custPackareaCode;

	/**
	 * 营销包区组织名称
	 */
	@Setter
	@Getter
	private Textbox custPackareaName;

	/**
	 * 分页控件 channelPackareaRelationListPaging
	 */
	@Getter
	@Setter
	private Paging channelPackareaRelationListPaging;

	/**
	 * 展示数据
	 */
	@Getter
	@Setter
	private Listbox channelPackareaRelationListBox;
}
