package cn.ffcs.uom.gridUnit.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.gridUnit.component.GridUnitRefListboxExt;
import cn.ffcs.uom.organization.action.OrganizationInfoEditExt;
import cn.ffcs.uom.organization.component.NewSeventeenMarketingTreeExt;
import cn.ffcs.uom.staff.component.StaffOrgTranListboxExt;

public class GridSeventeenUnitTreeMainBean {

    @Getter
    @Setter
    private Window gridUnitTreeMainWindow;
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
    private Tab gridUnitRefTab;

    /**
     * 员工组织业务关系
     */
    @Getter
    @Setter
    private Tab staffOrgTranTab;

    /**
     * Marketing树
     */
    @Getter
    @Setter
    private NewSeventeenMarketingTreeExt marketingTreeExt;

    /**
     * 组织信息
     */
    @Getter
    @Setter
    private OrganizationInfoEditExt organizationInfoEditExt;

    /**
     * 组织关系
     */
    @Getter
    @Setter
    private GridUnitRefListboxExt gridUnitRefListboxExt;

    /**
     * 组织员工业务关系
     */
    @Getter
    @Setter
    private StaffOrgTranListboxExt staffOrgTranListboxExt;

    @Getter
    @Setter
    private Tabbox tabBoxGridUnit;

    /**
     * 当前选中的Tab页.
     */
    @Getter
    @Setter
    private Tab tab;

    /**
     * 全息网格单元管理tab
     */
    @Getter
    @Setter
    private Tab gridUnitTab;
    @Getter
    @Setter
    private Tabpanel gridUnitTabpanel;

    @Getter
    @Setter
    private Tab tempTab;
}
