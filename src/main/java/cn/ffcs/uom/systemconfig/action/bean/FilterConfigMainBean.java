package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class FilterConfigMainBean {
	@Getter
	@Setter
	private Window filterConfigMainWin;
	@Getter
	@Setter
	private Textbox filterConfig;
	@Getter
	@Setter
	private Listbox filterConfigListBox;
	@Getter
	@Setter
	private Paging filterConfigPaging;
	@Getter
	@Setter
	private Button addFilterConfigButton;
	@Getter
	@Setter
	private Button editFilterConfigButton;
	@Getter
	@Setter
	private Button delFilterConfigButton;
}
