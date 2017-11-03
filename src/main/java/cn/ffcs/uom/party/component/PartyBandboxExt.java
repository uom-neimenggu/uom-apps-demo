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

import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 参与人Band插件
 * .
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-3
 * @功能说明：
 *
 */
@Controller
@Scope("prototype")
@SuppressWarnings({"unused"})
public class PartyBandboxExt extends Bandbox implements IdSpace {

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
 
    private final String zul = "/pages/party/comp/party_bandbox_ext.zul";
    
    private PartyListboxExt partyListboxExt;
    
    private Listbox partyListbox;
    
    private Party party;
    

    public PartyBandboxExt() {
        Executions.createComponents(this.zul, this, null);
        Components.wireVariables(this, this);
        Components.addForwards(this, this, '$');
        /***
         * 
         */
        partyListboxExt = (PartyListboxExt)this.getFellow("partyBandListboxExt");
        partyListbox = (Listbox)partyListboxExt.getFellow("partyListboxExtPanel").getFellow("partyListbox");
        partyListbox.getFellow("partyWindowDiv").setVisible(false);
        partyListbox.getFellow("partyBandboxDiv").setVisible(true);
//        partyMainWin.getFellow("tabBox").setVisible(false);
        /**
         * 改成单击
         */
//        partyListboxExt.addForward(SffOrPtyCtants.ON_PARTY_CONFIRM, this, "onSelectPartyReponse");

		partyListboxExt.addForward(SffOrPtyCtants.ON_PARTY_SELECT, this,
				"onSelectPartyReponse");
        partyListboxExt.addForward(SffOrPtyCtants.ON_PARTY_CLEAN, this, "onCleanPartyReponse");
        partyListboxExt.addForward(SffOrPtyCtants.ON_PARTY_CLOSE, this, "onClosePartyReponse");
    }
        
    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.setValue(null == party ? "" : party.getPartyName());
        this.party = party;
    }

    /**
     * 清空响应
     * 
     * @param event
     */
    public void onCleanPartyReponse(final ForwardEvent event) throws Exception {
        this.setParty(null);
        this.close();
    }

    /**
     * 关闭响应
     * 
     * @param event
     */
    public void onClosePartyReponse(final ForwardEvent event) throws Exception {
        this.close();
    }

    /**
     * 选择员工响应
     * 
     * @param event
     */
    public void onSelectPartyReponse(final ForwardEvent event) throws Exception {
        party = (Party) event.getOrigin().getData();
        if (null != party) {
            setValue(party.getPartyName());
            Events.postEvent("onBandChang", this, party);
        }
        this.close();
    }
}
