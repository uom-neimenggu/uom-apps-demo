package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import cn.ffcs.uom.organization.action.OrganizationInfoEditExt;
import cn.ffcs.uom.organization.action.OrganizationPartyAttrExt;
import cn.ffcs.uom.organization.action.OrganizationPositionExt;
import cn.ffcs.uom.organization.action.DwOrganizationRelationListboxComposer;
import cn.ffcs.uom.organization.action.StaffOrganizationListboxComposer;
import cn.ffcs.uom.organization.component.MultidimensionalTreeExt;
import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;
import cn.ffcs.uom.staff.component.StaffPositionListboxExt;

public class MultidimensionalTreeMainBean {
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
	private DwOrganizationRelationListboxComposer dwOrganizationRelationListbox;
	/**
	 * 组织员工关系
	 */
	@Getter
	@Setter
	private StaffOrganizationListboxComposer staffOrganizationListbox;

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
	 * 专业树
	 */
	@Getter
	@Setter
	private MultidimensionalTreeExt multidimensionalTreeExt;
	
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
	 * 专业树TAB
	 */
	@Getter
	@Setter
	private Tab multidimensionalTab;
	@Getter
	@Setter
	private Tabpanel multidimensionalTabpanel;
	@Getter
	@Setter
	private Tab tempTab;
}
