package cn.ffcs.uom.systemconfig.action.bean.comp;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Toolbarbutton;

import cn.ffcs.uom.systemconfig.action.comp.SystemMonitorConfigFilterListExt;

public class SystemMonitorConfigListExtBean {
	@Getter
	@Setter
	private Panel systemMonitorConfigListPanel;
	@Getter
	@Setter
	private Toolbarbutton addSystemMonitorConfigButton;
	@Getter
	@Setter
	private Toolbarbutton editSystemMonitorConfigButton;
	@Getter
	@Setter
	private Toolbarbutton delSystemMonitorConfigButton;
	@Getter
	@Setter
	private Listbox systemMonitorConfigListBox;
	@Getter
	@Setter
	private Paging systemMonitorConfigListPaging;

	@Getter
	@Setter
	private SystemMonitorConfigFilterListExt systemMonitorConfigFilterListExt;
}
