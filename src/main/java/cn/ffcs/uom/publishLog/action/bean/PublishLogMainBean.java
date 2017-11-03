package cn.ffcs.uom.publishLog.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

/**
 * 发布日志管理Bean.
 * 
 * @author faq
 **/
public class PublishLogMainBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window publishLogMainWin;
	/**
	 * panel
	 */
	@Getter
	@Setter
	private Panel publishLogListboxPanel;
	/**
	 * 组织树名称
	 */
	@Getter
	@Setter
	private Listbox orgTreeNameListbox;
	/**
	 * listbox
	 */
	@Getter
	@Setter
	private Listbox publishLogListbox;

	/**
	 * paging
	 */
	@Getter
	@Setter
	private Paging publishLogListPaging;
}
