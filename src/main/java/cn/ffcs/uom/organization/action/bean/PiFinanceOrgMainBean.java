package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

public class PiFinanceOrgMainBean {
	@Getter
	@Setter
	private Window piFinanceOrgMainWin;
	/**
	 * tabBox
	 */
	@Getter
	@Setter
	private Tabbox tabbox;
	/**
	 * 当前选中的tab
	 */
	@Getter
	@Setter
	private Tab selectTab;
}
