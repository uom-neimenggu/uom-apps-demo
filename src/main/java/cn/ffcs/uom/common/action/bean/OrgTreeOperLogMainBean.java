package cn.ffcs.uom.common.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

/**
 * 操作日志管理Bean.
 * 
 * @author
 **/
public class OrgTreeOperLogMainBean {
	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window orgTreeOperLogMainWin;
	/**
	 * panel
	 */
	@Getter
	@Setter
	private Panel orgTreeOperLogListboxPanel;
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
	private Listbox orgTreeOperLogListbox;

	/**
	 * 日志查看按钮.
	 */
	@Getter
	@Setter
	private Button viewOrgTreeOperLogButton;
	/**
	 * 日志发布按钮.
	 */
	@Getter
	@Setter
	private Button publishOrgTreeOperLogButton;
	/**
	 * 发布日志按钮.
	 */
	@Getter
	@Setter
	private Button publishOperLogButton;

	/**
	 * div
	 */
	@Getter
	@Setter
	private Div orgTreeOperLogWindowDiv;
	@Getter
	@Setter
	private Div orgTreeOperLogBandboxDiv;

	/**
	 * paging
	 */
	@Getter
	@Setter
	private Paging organizationListPaging;
}
