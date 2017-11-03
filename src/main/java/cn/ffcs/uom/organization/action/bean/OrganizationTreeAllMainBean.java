package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import cn.ffcs.uom.organization.action.OrganizationInfoEditExt;
import cn.ffcs.uom.organization.action.OrganizationPartyAttrExt;
import cn.ffcs.uom.organization.action.OrganizationPositionExt;
import cn.ffcs.uom.organization.action.OrganizationRelationListboxComposer;
import cn.ffcs.uom.organization.action.StaffOrganizationListboxComposer;
import cn.ffcs.uom.organization.component.CommonTreeExt;
import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;
import cn.ffcs.uom.staff.component.StaffPositionListboxExt;

public class OrganizationTreeAllMainBean {
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
     * 组织关系
     */
    @Getter
    @Setter
    private OrganizationRelationListboxComposer organizationRelationListbox;
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
     * 代理树页面
     */
    @Getter
    @Setter
    private CommonTreeExt commonTreeExt;

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
}