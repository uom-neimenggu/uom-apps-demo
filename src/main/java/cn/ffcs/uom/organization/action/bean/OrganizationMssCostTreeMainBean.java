package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import cn.ffcs.uom.organization.action.OrganizationInfoEditExt;
import cn.ffcs.uom.organization.component.MssCostTreeExt;

public class OrganizationMssCostTreeMainBean {
    /**
     * tabBox
     */
    @Getter
    @Setter
    private Tabbox                  tabBox;
    /**
     * 当前选中的tab
     */
    @Getter
    @Setter
    private Tab                     selectTab;
    /**
     * 左边tabBox
     */
    @Getter
    @Setter
    private Tabbox                  leftTabbox;
    /**
     * 左边树tab页选中的tab
     */
    @Getter
    @Setter
    private Tab                     leftSelectTab;
    /**
     * 组织属性
     */
    @Getter
    @Setter
    private Tab                     orgAttrTab;
    
    /**
     * Cost树
     */
    @Getter
    @Setter
    private MssCostTreeExt          mssCostTreeExt;
    
    /**
     * 组织信息
     */
    @Getter
    @Setter
    private OrganizationInfoEditExt organizationInfoEditExt;
    
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
    private Tab mssCostTab;
    @Getter
    @Setter
    private Textbox subSetName;
    @Getter
    @Setter
    private Textbox subName;
}
