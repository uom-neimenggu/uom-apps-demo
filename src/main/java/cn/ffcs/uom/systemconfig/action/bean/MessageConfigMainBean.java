package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.systemconfig.action.comp.MessageConfigListboxComposer;
import cn.ffcs.uom.systemconfig.action.comp.SysBusiUserListboxComposer;

public class MessageConfigMainBean {
	@Getter
	@Setter
	private Window messageConfigMainWin;
	@Getter
	@Setter
	private MessageConfigListboxComposer messageConfigListbox;
	@Getter
	@Setter
	private SysBusiUserListboxComposer sysBusiUserListbox;
}
