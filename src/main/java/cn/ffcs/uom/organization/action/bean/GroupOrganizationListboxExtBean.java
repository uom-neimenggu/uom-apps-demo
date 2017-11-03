package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 * 组织管理Bean
 * 
 * @author
 **/
public class GroupOrganizationListboxExtBean {
	/**
	 * panel.
	 **/
	@Getter
	@Setter
	private Panel groupOrganizationListboxExtPanel;
	/**
	 * Listbox.
	 **/
	@Getter
	@Setter
	private Listbox groupOrganizationListBox;
	/**
	 * 组织来源.
	 **/
	@Getter
	@Setter
	private Listbox orgType;
	/**
	 * 组织名称.
	 **/
	@Getter
	@Setter
	private Textbox orgName;
	/**
	 * 组织编码.
	 **/
	@Getter
	@Setter
	private Textbox orgCode;
	/**
	 * 备注.
	 **/
	@Getter
	@Setter
	private Textbox tDesc2;

	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging groupOrganizationListPaging;
	/**
	 * bandbox使用
	 */
	@Getter
	@Setter
	private Div groupOrganizationBandboxDiv;
}
