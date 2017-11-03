package cn.ffcs.uom.party.component;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

@Controller
@Scope("prototype")
@SuppressWarnings({"unused"})
public class PartyContactInfoBandboxExt extends Bandbox implements IdSpace {

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    
    private final String zul = "/pages/party/comp/party_contactInfo_bandbox_ext.zul";
    
    private PartyContactInfoListboxExt partyContactInfoListboxExt;
    
    private Window partyContactInfoMainWin;
    
    private Listbox partyContactInfoListbox;
    
    private PartyContactInfo partyContactInfo;
    
    public PartyContactInfoBandboxExt() {
        Executions.createComponents(this.zul, this, null);
        Components.wireVariables(this, this);
        Components.addForwards(this, this, '$');
        
        partyContactInfoMainWin = (Window)getFellow("partyContactInfoBandList").getFellow("partyContactInfoMainWin");
        partyContactInfoListboxExt = (PartyContactInfoListboxExt)partyContactInfoMainWin.getFellow("partyContactInfoListboxExt");
        partyContactInfoListbox = (Listbox)partyContactInfoMainWin.getFellow("partyContactInfoListboxExt").getFellow("partyContactInfoListboxExtPanel").getFellow("partyContactInfoListbox");
   
        partyContactInfoMainWin.getFellow("partyContactInfoListboxExt").addForward(SffOrPtyCtants.ON_PARTYCONTACTINFO_CONFIRM, this, "onSelectPartyContactInfoReponse");
        partyContactInfoMainWin.getFellow("partyContactInfoListboxExt").addForward(SffOrPtyCtants.ON_PARTYCONTACTINFO_CLEAN, this, "onCleanPartyContactInfoReponse");
        partyContactInfoMainWin.getFellow("partyContactInfoListboxExt").addForward(SffOrPtyCtants.ON_PARTY_CONTACTINFO_CLOSE, this, "onClosePartyContactInfoReponse");
        
    }
    
    public PartyContactInfoListboxExt getPartyContactInfoListboxExt(){
        return partyContactInfoListboxExt;
    }
    
    public PartyContactInfo getPartyContactInfo() {
        return partyContactInfo;
    }

    public void setPartyContactInfo(PartyContactInfo partyContactInfo) {
        this.setValue(null == partyContactInfo ? "" : partyContactInfo.getContactName());
        this.partyContactInfo = partyContactInfo;
    }
    
    /**
     * 选择员工响应
     * 
     * @param event
     */
    public void onSelectPartyContactInfoReponse(final ForwardEvent event) throws Exception {
        partyContactInfo = (PartyContactInfo) event.getOrigin().getData();
        if (null != partyContactInfo) {
            setValue(partyContactInfo.getContactName());
//            Events.postEvent(StaffOrPartyConstants.ON_PARTYCONTACTINFO_VIEW, partyContInfoWindow.getFellow("partyEditWindow"), partyContactInfo);
            Events.postEvent(SffOrPtyCtants.ON_PARTYCONTACTINFO_VIEW, this, partyContactInfo);

        }
        this.close();
    }
    
    /**
     * 清空响应
     * 
     * @param event
     */
    public void onCleanPartyContactInfoReponse(final ForwardEvent event) throws Exception {
        this.setPartyContactInfo(null);
        this.close();
    }

    /**
     * 关闭响应
     * 
     * @param event
     */
    public void onClosePartyContactInfoReponse(final ForwardEvent event) throws Exception {
        this.close();
    }
    
}
