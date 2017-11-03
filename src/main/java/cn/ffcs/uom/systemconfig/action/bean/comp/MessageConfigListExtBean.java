package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class MessageConfigListExtBean {
	@Getter
	@Setter
	private Panel messageConfigListPanel;
	@Getter
	@Setter
	private Toolbarbutton addSystemMessageConfigButton;
	@Getter
	@Setter
	private Toolbarbutton editSystemMessageConfigButton;
	@Getter
	@Setter
	private Toolbarbutton delSystemMessageConfigButton;
	@Getter
	@Setter
	private Toolbarbutton closeMessageConfigButton;
	@Getter
	@Setter
	private Toolbarbutton cleanMessageConfigButton;
	@Getter
	@Setter
	private Listbox systemMessageConfigListBox;
	@Getter
	@Setter
	private Paging systemMessageConfigListPaging;
	/**
	 * 查询使用
	 */
	@Getter
	@Setter
	private Div messageConfigWindowDiv;
	/**
	 * bandbox使用
	 */
	@Getter
	@Setter
	private Div messageConfigBandboxDiv;
	
	/**
	 * 姓名
	 **/
	@Getter
	@Setter
	private Textbox userName;
	
	/**
	 * 手机号码
	 **/
	@Getter
	@Setter
	private Textbox telephoneNumber;

}
