package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

/**
 * 记录更新Bean.
 * 
 * @author zhulintao
 **/
public class StaffOrgTranResultMainBean {
	/**
	 * Window.
	 **/
	@Getter
	@Setter
	private Window staffOrgTranResultMainWin;
	/**
	 * Panel
	 */
	@Getter
	@Setter
	private Panel staffOrgTranResultListboxPanel;
	/**
	 * Listbox
	 */
	@Getter
	@Setter
	private Listbox staffOrgTranResultListbox;
	/**
	 * Listitem
	 */
	@Getter
	@Setter
	private Listitem staffOrgTranResultListitem;
	/**
	 * Div
	 */
	@Getter
	@Setter
	private Div staffOrgTranResultDiv;
	/**
	 * Div
	 */
	@Getter
	@Setter
	private Div staffOrgTranResultListboxDiv;

}
