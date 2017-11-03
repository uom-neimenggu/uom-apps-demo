package cn.ffcs.uom.uomGroupTran.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.UomGroupOrgTranListboxExt;

/**
 * 业务关系
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-04-14
 * @功能说明：
 * 
 */
public class UomGroupTranMainBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window uomGroupTranMainWin;

	@Getter
	@Setter
	private Tabbox tabBox;

	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab selectTab;

	@Getter
	@Setter
	private Tab tempTab;

	@Getter
	@Setter
	private Tab uomGroupOrgTranTab;

	@Getter
	@Setter
	private Tab uomGroupstaffTranTab;

	@Getter
	@Setter
	private Tabpanel tempTabpanel;

	@Getter
	@Setter
	private Tabpanel uomGroupOrgTranTabpanel;

	@Getter
	@Setter
	private Tabpanel uomGroupstaffTranTabpanel;

	/**
	 * uomGroupOrgTranListboxExt.
	 */
	@Getter
	@Setter
	private UomGroupOrgTranListboxExt uomGroupOrgTranListboxExt;

}
