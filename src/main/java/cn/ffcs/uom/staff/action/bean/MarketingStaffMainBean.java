package cn.ffcs.uom.staff.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.action.StaffOrganizationListboxComposer;
import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;
import cn.ffcs.uom.party.component.PartyRoleListboxExt;
import cn.ffcs.uom.staff.component.StaffListboxExt;
import cn.ffcs.uom.staff.component.StaffOrgTranListboxExt;
import cn.ffcs.uom.staff.component.StaffPositionListboxExt;

/**
 * 员工管理Bean.
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-27
 * @功能说明：
 *
 */
public class MarketingStaffMainBean {
	
    /**
     *window.
     **/
    @Getter
    @Setter
    private Window marketingStaffMainWin;
    
    /**
     * staffListboxExt.
     */
    @Getter
    @Setter
    private StaffListboxExt staffListboxExt;
    
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
    private StaffOrganizationListboxComposer staffOrganizationListbox;
    
    @Getter
    @Setter
    private StaffPositionListboxExt staffPositionListboxExt;
    
    @Getter
    @Setter
    private StaffOrgTranListboxExt staffOrgTranListboxExt;
    
    @Getter
    @Setter  
    private PartyCertificationListboxExt partyCertificationListboxExt;
    
    @Getter
    @Setter  
    private PartyContactInfoListboxExt partyContactInfoListboxExt;
    
    @Getter
    @Setter  
    private PartyRoleListboxExt partyRoleListboxExt;

    @Getter
    @Setter 
    private Component staffRoleListbox;
    @Getter
    @Setter 
    private Component staffSysListbox;
}
