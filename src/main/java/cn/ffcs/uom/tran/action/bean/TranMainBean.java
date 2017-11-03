package cn.ffcs.uom.tran.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.component.OrganizationTranListboxExt;

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
public class TranMainBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window tranMainWin;

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
	private Tab organizationTranTab;

	@Getter
	@Setter
	private Tab staffTranTab;

	@Getter
	@Setter
	private Tabpanel tempTabpanel;

	@Getter
	@Setter
	private Tabpanel organizationTranTabpanel;

	@Getter
	@Setter
	private Tabpanel staffTranTabpanel;

	/**
	 * organizationTranListboxExt.
	 */
	@Getter
	@Setter
	private OrganizationTranListboxExt organizationTranListboxExt;

}
