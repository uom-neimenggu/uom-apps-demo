package cn.ffcs.uom.publishLog.action.bean;


import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Window;

/**
 * @author wenyaopeng
 *
 */
public class PublishQueryMainBean {
	
	@Getter
    @Setter
	private Window publishQueryMainWin;
	
	@Getter
    @Setter
	private Panel queryMainWinListboxPanel;
	
	@Getter
    @Setter
	private Toolbar toolbarId;
	
	@Getter
    @Setter
	private Listbox orgTreeId;
	
	@Getter
    @Setter
	private Datebox lastTime;
	
	@Getter
    @Setter
	private Datebox thisTime;
	
	@Getter
    @Setter
	private Listbox syncType;
	
	@Getter
    @Setter
	private Listbox tableName;
	
	@Getter
    @Setter
	private Textbox publishQueryResults;
	
	@Getter
    @Setter
	private Textbox publishKeyWordQuery;
	
	@Getter
    @Setter
	private Listbox businessSystemListbox;
	
	@Getter
    @Setter
	private Listbox businessSystemQueryResults;
	
	@Getter
    @Setter
	private Paging businessSystemPaging;
	
	@Getter
    @Setter
	private Button publishAgainToolbarbutton;
	
	@Getter
    @Setter
	private Button publishLogToolbarbutton;
	
	@Getter
    @Setter
	private Button ftpTaskInstanceInfoToolbarbutton;
	
//	@Getter
//    @Setter
//	private Listbox publishQueryListbox; 
//	
//	@Getter
//    @Setter
//	private Paging publishQueryListPaging;

	
}
