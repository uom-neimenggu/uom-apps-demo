package cn.ffcs.uom.common.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

/**
 * 记录更新Bean.
 * 
 * @author zhulintao
 **/
public class DiverseAttributeAddOrDelMainBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window diverseAttributeAddOrDelMainWin;
	/**
	 * panel
	 */
	@Getter
	@Setter
	private Panel diverseAttributeAddOrDelListboxPanel;
	/**
	 * list
	 */
	@Getter
	@Setter
	private Listbox diverseAttributeAddOrDelListbox;
	/**
	 * div
	 */
	@Getter
	@Setter
	private Div systemInfoAddOrDelDiv;
	/**
	 * div
	 */
	@Getter
	@Setter
	private Div diverseAttributeAddOrDelListboxDiv;

}
