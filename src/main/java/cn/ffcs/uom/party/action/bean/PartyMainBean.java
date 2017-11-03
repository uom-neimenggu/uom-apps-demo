package cn.ffcs.uom.party.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.party.component.PartyCertificationListboxExt;
import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;
import cn.ffcs.uom.party.component.PartyListboxExt;
import cn.ffcs.uom.party.component.PartyRoleListboxExt;

/**
 * 参与人
 * .
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-30
 * @功能说明：
 *
 */
public class PartyMainBean {
    
    
    @Getter
    @Setter
    private Window partyMainWin;
    
    /**
     * 
     */
    @Getter
    @Setter
    private PartyListboxExt partyListboxExt;
    
    @Getter
    @Setter  
    private PartyCertificationListboxExt partyCertificationListboxExt;
    
    @Getter
    @Setter  
    private PartyContactInfoListboxExt partyContactInfoListboxExt;
    
    @Getter
    @Setter  
    private PartyRoleListboxExt partyRoleListboxExt;
    
    /**
     * TabBox.
     */
    @Getter
    @Setter
    private Tabbox tabBox;
    /**
     * 当前选中的Tab页.
     */
    @Getter
    @Setter
    private Tab tab;
}
