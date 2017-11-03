package cn.ffcs.uom.contrast.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
/**
 * 在前端crm.zul中的各个组件的id
 * @author Wyr
 *
 */
public class StaffContrastCrmMainBean {
	@Getter
	@Setter
	private Window staffContrastCrmMainWin;
	@Getter
	@Setter
	private Panel staffContrastCrmListboxPanel;
	@Getter
	@Setter
	private Textbox empeeAcct;
	@Getter
	@Setter
	private Textbox certName;
	@Getter
	@Setter
	private Listbox staffContrastCrmListbox;
	@Getter
	@Setter
	private Paging staffContrastCrmListboxPaging;

}
