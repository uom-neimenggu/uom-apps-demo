package cn.ffcs.uom.restservices.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.restservices.component.GrpChannelOperatorsRelaListboxExt;
import cn.ffcs.uom.restservices.component.GrpOperatorsListboxExt;

/**
 * 经营主体管理Bean. .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-5-20
 * @功能说明：
 * 
 */
public class GrpOperatorsMainBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window grpOperatorsMainWin;

	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab tab;

	@Getter
	@Setter
	private Tabbox tabBox;

	@Getter
	@Setter
	private GrpOperatorsListboxExt grpOperatorsListboxExt;

	@Getter
	@Setter
	private GrpChannelOperatorsRelaListboxExt grpChannelOperatorsRelaListboxExt;

}
