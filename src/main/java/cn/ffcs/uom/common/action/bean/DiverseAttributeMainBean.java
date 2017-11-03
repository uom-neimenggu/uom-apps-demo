package cn.ffcs.uom.common.action.bean;

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
public class DiverseAttributeMainBean {
	/**
	 * Window.
	 **/
	@Getter
	@Setter
	private Window diverseAttributeMainWin;
	/**
	 * Panel
	 */
	@Getter
	@Setter
	private Panel diverseAttributeListboxPanel;
	/**
	 * Listbox
	 */
	@Getter
	@Setter
	private Listbox diverseAttributeListbox;
	/**
	 * Listitem
	 */
	@Getter
	@Setter
	private Listitem diverseAttributeListitem;
	/**
	 * Div
	 */
	@Getter
	@Setter
	private Div systemInfoDiv;
	/**
	 * Div
	 */
	@Getter
	@Setter
	private Div diverseAttributeListboxDiv;

}
