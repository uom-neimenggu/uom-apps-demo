package cn.ffcs.uom.party.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.party.action.bean.PartyContactInfoBean;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

@Controller
@Scope("prototype")
@SuppressWarnings({"unused"})
public class PartyContactInfoMainComposer extends BasePortletComposer  {

    PartyContactInfoBean bean = new PartyContactInfoBean();
    
    private PartyContactInfo partyContactInfo;
    /**
     * .
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Components.wireVariables(comp, bean);
        ///////////////////////////
        bean.getPartyContactInfoListboxExt().addForward(SffOrPtyCtants.ON_PARTYCONTACTINFO_SELECT, comp, "onSelectPartyContactInfoResponse");
        bean.getPartyContactInfoListboxExt().addForward(SffOrPtyCtants.ON_PARTYCONTACTINFO_DELETE, comp, "onDeletePartyContactInfoResponse");
    }
    
    public void onSelectPartyContactInfoResponse(final ForwardEvent event) throws Exception    {
        partyContactInfo = (PartyContactInfo)event.getOrigin().getData();
    }
    
    public void onDeletePartyContactInfoResponse(final ForwardEvent event) throws Exception    {
        partyContactInfo = null;
        Events.postEvent(SffOrPtyCtants.ON_PARTYCONTACTINFO_DELETE, self.getRoot(),null);
    }
    
}
