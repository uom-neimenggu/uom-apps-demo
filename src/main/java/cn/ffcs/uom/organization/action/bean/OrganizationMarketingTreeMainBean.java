package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.action.OrganizationInfoEditExt;
import cn.ffcs.uom.organization.action.OrganizationPartyAttrExt;
import cn.ffcs.uom.organization.action.OrganizationPositionExt;
import cn.ffcs.uom.organization.action.OrganizationRelationListboxComposer;
import cn.ffcs.uom.organization.action.StaffOrganizationListboxComposer;
import cn.ffcs.uom.organization.component.EdwTreeExt;
import cn.ffcs.uom.organization.component.MarketingTreeExt;
import cn.ffcs.uom.organization.component.OrganizationTranListboxExt;
import cn.ffcs.uom.organization.component.UomGroupOrgTranListboxExt;
import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;
import cn.ffcs.uom.staff.component.StaffOrgTranListboxExt;
import cn.ffcs.uom.staff.component.StaffPositionListboxExt;

public class OrganizationMarketingTreeMainBean {

	@Getter
	@Setter
	private Window organizationMarketingTreeMainWindow;
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
	/**
	 * 左边tabBox
	 */
	@Getter
	@Setter
	private Tabbox leftTabbox;
	/**
	 * 左边树tab页选中的tab
	 */
	@Getter
	@Setter
	private Tab leftSelectTab;
	/**
	 * 组织属性
	 */
	@Getter
	@Setter
	private Tab orgAttrTab;
	/**
	 * 组织关系
	 */
	@Getter
	@Setter
	private Tab orgRelaTab;

	/**
	 * 组织员工
	 */
	@Getter
	@Setter
	private Tab orgStaffTab;

	/**
	 * 员工组织业务关系
	 */
	@Getter
	@Setter
	private Tab staffOrgTranTab;

	/**
	 * 员工岗位
	 */
	@Getter
	@Setter
	private Tab orgPositionTab;
	/**
	 * 组织参与人
	 */
	// @Getter
	// @Setter
	// private Tab orgPartyTab;
	/**
	 * 组织关系
	 */
	@Getter
	@Setter
	private OrganizationRelationListboxComposer organizationRelationListbox;
	/**
	 * 组织业务关系
	 */
	@Getter
	@Setter
	private OrganizationTranListboxExt organizationTranListboxExt;
	/**
	 * 跨域内外业务关系
	 */
	@Getter
	@Setter
	private UomGroupOrgTranListboxExt uomGroupOrgTranListboxExt;
	/**
	 * 组织员工关系
	 */
	@Getter
	@Setter
	private StaffOrganizationListboxComposer staffOrganizationListbox;

	/**
	 * 组织员工业务关系
	 */
	@Getter
	@Setter
	private StaffOrgTranListboxExt staffOrgTranListboxExt;

	/**
	 * 参与人证件
	 */
	@Getter
	@Setter
	private PartyCertificationListboxExt partyCertificationListboxExt;

	/**
	 * 参与人联系人
	 */
	@Getter
	@Setter
	private PartyContactInfoListboxExt partyContactInfoListboxExt;

	/**
	 * 员工岗位
	 */
	@Getter
	@Setter
	private StaffPositionListboxExt staffPositionListboxExt;

	/**
	 * 组织岗位关系
	 */
	@Getter
	@Setter
	private OrganizationPositionExt organizationPositionListbox;
	/**
	 * Marketing树
	 */
	@Getter
	@Setter
	private MarketingTreeExt marketingTreeExt;
	/**
	 * EDW树
	 */
	@Getter
	@Setter
	private EdwTreeExt edwTreeExt;
	/**
	 * 组织信息
	 */
	@Getter
	@Setter
	private OrganizationInfoEditExt organizationInfoEditExt;
	/**
	 * 参与人联系人、证件.
	 */
	@Getter
	@Setter
	private Tabbox tabBoxParty;
	/**
	 * 当前选中的Tab页.
	 */
	@Getter
	@Setter
	private Tab tab;
	/**
	 * 组织参与人
	 */
	@Getter
	@Setter
	private OrganizationPartyAttrExt organizationPartyAttrExt;
	/**
	 * 组织参与人tab
	 */
	@Getter
	@Setter
	private Tab orgPartyAttrTab;
	/**
	 * 内部管理tab
	 */
	@Getter
	@Setter
	private Tab politicalTab;
	@Getter
	@Setter
	private Tabpanel politicalTabpanel;
	/**
	 * 代理商tab
	 */
	@Getter
	@Setter
	private Tab agentTab;
	@Getter
	@Setter
	private Tabpanel agentTabpanel;
	/**
	 * 供应商tab
	 */
	@Getter
	@Setter
	private Tab supplierTab;
	@Getter
	@Setter
	private Tabpanel supplierTabpanel;
	/**
	 * marketingTab
	 */
	@Getter
	@Setter
	private Tab marketingTab;
	@Getter
	@Setter
	private Tabpanel marketingTabpanel;
	/**
	 * EDWtab
	 */
	@Getter
	@Setter
	private Tab edwTab;
	@Getter
	@Setter
	private Tabpanel edwTabpanel;
	/**
	 * 中通服tab
	 */
	@Getter
	@Setter
	private Tab ossTab;
	@Getter
	@Setter
	private Tabpanel ossTabpanel;
	@Getter
	@Setter
	private Tab tempTab;
}
