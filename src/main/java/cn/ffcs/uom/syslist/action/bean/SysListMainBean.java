package cn.ffcs.uom.syslist.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.syslist.action.SysListEditDiv;
import cn.ffcs.uom.syslist.component.SysListTreeExt;

public class SysListMainBean {
	@Getter
	@Setter
	private Window sysListMainWin;
	@Getter
	@Setter
	private SysListEditDiv sysListEditDiv;
	@Getter
	@Setter
	private SysListTreeExt sysListTreeExt;
	@Getter
	@Setter
	private Tabbox leftTabbox;
	@Getter
	@Setter
	private Tabbox sysTabBox;
	@Getter
	@Setter
	private Tabbox sysListTabBox;
	
}
