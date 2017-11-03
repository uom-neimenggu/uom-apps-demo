package cn.ffcs.uom.financialCenter.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.action.OrganizationListboxComposer;
import cn.ffcs.uom.organization.action.OrganizationPositionExt;
import cn.ffcs.uom.organization.action.OrganizationRelationListboxComposer;
import cn.ffcs.uom.organization.action.StaffOrganizationListboxComposer;

/**
 *组织管理Bean.
 * 
 * @author
 **/
public class FinancialCenterOrgMainBean {
	/**
	 *window.
	 **/
	@Getter
	@Setter
	private Window FinancialCenterOrgMainWin;
	/**
	 * 组织列表
	 */
	@Getter
	@Setter
	private OrganizationListboxComposer organizationListbox;
	/**
	 * 组织关系
	 */
	@Getter
	@Setter
	private OrganizationRelationListboxComposer organizationRelationListbox;
	/**
	 * 员工组织
	 */
	@Getter
	@Setter
	private StaffOrganizationListboxComposer staffOrganizationListbox;
	/**
	 * 组织岗位
	 */
	@Getter
	@Setter
	private OrganizationPositionExt organizationPositionExt;
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
}
