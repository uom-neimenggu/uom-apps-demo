package cn.ffcs.uom.accconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.accconfig.action.AccConfigEditDiv;
import cn.ffcs.uom.accconfig.component.AccConfigTreeExt;

public class AccConfigMainBean {
	@Getter
	@Setter
	private Window accConfigMainWin;
	@Getter
	@Setter
	private AccConfigEditDiv accConfigEditDiv;
	@Getter
	@Setter
	private AccConfigTreeExt accConfigTreeExt;
	@Getter
	@Setter
	private Tabbox leftTabbox;
	@Getter
	@Setter
	private Tabbox accTabBox;
	@Getter
	@Setter
	private Tabbox sysAccTabBox;
	
}
