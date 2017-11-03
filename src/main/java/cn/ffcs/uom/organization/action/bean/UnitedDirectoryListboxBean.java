package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

/**
 *组织管理Bean
 * 
 * @author
 **/
public class UnitedDirectoryListboxBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Panel unitedDirectoryListboxComp;
	/**
	 *Listbox.
	 **/
	@Getter
	@Setter
	private Listbox unitedDirectoryListBox;
	/**
	 * 组织名称.
	 **/
	@Getter
	@Setter
	private Textbox deptname;
	/**
	 * 组织编码.
	 **/
	@Getter
	@Setter
	private Textbox ctou;
	/**
	 * 分页控件
	 */
	@Getter
	@Setter
	private Paging unitedDirectoryListPaging;
}
