package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.systemconfig.action.comp.BusinessSystemListExt;

public class BmpSystemConfigMainBean {
	@Getter
	@Setter
	private Window bmpSystemConfigMainWin;
	@Getter
	@Setter
	private BusinessSystemListExt businessSystemListExt;
	@Getter
	@Setter
	private Tab informMehthodTab;
	@Getter
	@Setter
	private Tab pricipalTab;
	@Getter
	@Setter
	private Listbox pricipalListBox;
	@Getter
	@Setter
	private Listbox systemInformListBox;
	@Getter
	@Setter
	private Button addPricipalButton;
	@Getter
	@Setter
	private Button delPricipalButton;
	@Getter
	@Setter
	private Button addSystemInformButton;
	@Getter
	@Setter
	private Button delSystemInformButton;
}
