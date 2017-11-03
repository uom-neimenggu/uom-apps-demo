package cn.ffcs.uom.gridUnit.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.gridUnit.component.StaffGridUnitTranListboxExt;

/**
 * 网格单元管理Bean. .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-09-22
 * @功能说明：
 * 
 */
public class StaffGridUnitTranMainBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window staffGridUnitTranMainWin;

	@Getter
	@Setter
	private StaffGridUnitTranListboxExt staffGridUnitTranListboxExt;

}
