package cn.ffcs.uom.report.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.report.component.FullStaffQuarterReportExt;
import cn.ffcs.uom.report.component.FullStaffUnitReportExt;

public class FullCaliberStaffReportMainBean {
	@Getter
	@Setter
	private Window fullCaliberStaffReportMainWin;
	/**
	 * tabBox
	 */
	@Getter
	@Setter
	private Tabbox tabBox;
	/**
	 * 当前选中的tab
	 */
	@Getter
	@Setter
	private Tab selectTab;
	@Getter
	@Setter
	private Tab quarterTab;
	@Getter
	@Setter
	private Tab unitTab;
	@Getter
	@Setter
	private Tab tempTab;
	@Getter
	@Setter
	private Tabpanel quarterTabpanel;
	@Getter
	@Setter
	private Tabpanel unitTabpanel;
	@Getter
	@Setter
	private FullStaffQuarterReportExt fullStaffQuarterReportExt;
	@Getter
	@Setter
	private FullStaffUnitReportExt fullStaffUnitReportExt;

}
