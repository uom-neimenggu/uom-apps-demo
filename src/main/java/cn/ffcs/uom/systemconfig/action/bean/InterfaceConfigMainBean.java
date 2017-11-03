package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.systemconfig.action.comp.BusinessSystemListExt;
import cn.ffcs.uom.systemconfig.action.comp.SystemIntfInfoConfigListExt;
import cn.ffcs.uom.systemconfig.action.comp.SystemMessageConfigListExt;
import cn.ffcs.uom.systemconfig.action.comp.SystemMonitorConfigListExt;
import cn.ffcs.uom.systemconfig.action.comp.SystemOrgTreeConfigListExt;

public class InterfaceConfigMainBean {
	@Getter
	@Setter
	private Window interfaceConfigMainWin;
	@Getter
	@Setter
	private BusinessSystemListExt businessSystemListExt;
	@Getter
	@Setter
	private Tabbox tabbox;
	@Getter
	@Setter
	private Tab selectTab;
	@Getter
	@Setter
	private SystemIntfInfoConfigListExt systemIntfInfoConfigListExt;
	@Getter
	@Setter
	private SystemOrgTreeConfigListExt systemOrgTreeConfigListExt;
	@Getter
	@Setter
	private SystemMessageConfigListExt systemMessageConfigListExt;
	@Getter
	@Setter
	private SystemMonitorConfigListExt systemMonitorConfigListExt;
}
