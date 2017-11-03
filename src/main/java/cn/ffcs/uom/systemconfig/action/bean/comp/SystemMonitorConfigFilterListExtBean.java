package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

public class SystemMonitorConfigFilterListExtBean {
	@Getter
	@Setter
	private Panel systemMonitorConfigFilterListPanel;
	@Getter
	@Setter
	private Toolbarbutton addSystemMonitorConfigFilterButton;
	@Getter
	@Setter
	private Toolbarbutton editSystemMonitorConfigFilterButton;
	@Getter
	@Setter
	private Toolbarbutton delSystemMonitorConfigFilterButton;
	@Getter
	@Setter
	private Listbox systemMonitorConfigFilterListBox;
	@Getter
	@Setter
	private Paging systemMonitorConfigFilterListPaging;

}
