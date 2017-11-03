package cn.ffcs.uom.report.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;

public class RealnameDataReportExtBean {
	@Getter
	@Setter
	private Panel realnameDataReportExtPanel;
	@Setter
	@Getter
	private Datebox date;
	@Setter
	@Getter
	private Button exportButton;
	@Getter
	@Setter
	private Listbox reportListbox;
	@Getter
	@Setter
	private Paging reportListboxPaging;
}
