package cn.ffcs.uom.activation.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.activation.component.OrganizationActivationListbox;
import cn.ffcs.uom.activation.component.StaffActivationListbox;

/**
 * 激活管理Bean. .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-09-13
 * @功能说明：
 * 
 */
public class ActivationMainBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window activationMainWin;

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
	 * staffActivationListboxExt.
	 */
	@Getter
	@Setter
	private StaffActivationListbox staffActivationListboxExt;

	/**
	 * organizationActivationListboxExt.
	 */
	@Getter
	@Setter
	private OrganizationActivationListbox organizationActivationListboxExt;

}
