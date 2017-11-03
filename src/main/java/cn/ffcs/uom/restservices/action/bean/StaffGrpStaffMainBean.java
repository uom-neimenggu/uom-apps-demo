package cn.ffcs.uom.restservices.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.restservices.component.GrpStaffListboxExt;
import cn.ffcs.uom.staff.component.StaffListboxExt;

/**
 * 集团渠道数据管理Bean. .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-09-22
 * @功能说明：
 * 
 */
public class StaffGrpStaffMainBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffGrpStaffMainWin;
	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab tab;

	@Getter
	@Setter
	private Tabbox tabBox;

	/**
	 * staffListboxExt.
	 */
	@Getter
	@Setter
	private StaffListboxExt staffListboxExt;

	@Getter
	@Setter
	private GrpStaffListboxExt grpStaffListboxExt;
}
