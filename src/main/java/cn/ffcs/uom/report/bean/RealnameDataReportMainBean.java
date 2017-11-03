package cn.ffcs.uom.report.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.report.component.RealnameDataReportExt;

/**
 *实名制数据统计报表Bean.
 * 
 * @author
 **/
public class RealnameDataReportMainBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window realnameDataReportMainWin;
	/**
	 * 列表
	 */
	@Getter
	@Setter
	private RealnameDataReportExt realnameDataReportExt;
}
