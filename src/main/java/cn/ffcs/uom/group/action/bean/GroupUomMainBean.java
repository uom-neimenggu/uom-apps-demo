package cn.ffcs.uom.group.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.group.component.GroupUomOrgListboxExt;
import cn.ffcs.uom.organization.action.StaffOrganizationListboxComposer;
import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;
import cn.ffcs.uom.party.component.PartyRoleListboxExt;
import cn.ffcs.uom.staff.component.StaffOrgTranListboxExt;
import cn.ffcs.uom.staff.component.StaffPositionListboxExt;

/**
 * 员工管理Bean. .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-27
 * @功能说明：
 * 
 */
public class GroupUomMainBean {

	/**
	 * window.
	 **/
	@Getter
	@Setter
	private Window groupUomMainWin;

	@Getter
	@Setter
	private Tabbox tabBox;

	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab tab;

	@Getter
	@Setter
	private Tab tempTab;

	@Getter
	@Setter
	private Tab groupUomOrgTab;

	@Getter
	@Setter
	private Tab groupUomStaffTab;

	@Getter
	@Setter
	private Tabpanel tempTabpanel;

	@Getter
	@Setter
	private Tabpanel groupUomOrgTabpanel;

	@Getter
	@Setter
	private Tabpanel groupUomStaffTabpanel;

	/**
	 * groupUomOrgListboxExt.
	 */
	@Getter
	@Setter
	private GroupUomOrgListboxExt groupUomOrgListboxExt;

}
