package cn.ffcs.uom.party.component;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 参与人证件Bandbox
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-7
 * @功能说明：
 *
 */
@SuppressWarnings({"unused"})
public class PartyCertificationBandboxExt extends Bandbox implements IdSpace  {

    /**
     * .
     */
    private static final long serialVersionUID = 1L;
    
    private final String zul = "/pages/party/comp/party_certification_bandbox_ext.zul";
    
    private PartyCertificationListboxExt partyCertificationListboxExt;
    
    private PartyCertification partyCertification;
    
    private Window partyCertificationMainWin;
    
    private Listbox partyCertificationListbox;
    
    public PartyCertificationBandboxExt() {
        Executions.createComponents(this.zul, this, null);
        Components.wireVariables(this, this);
        Components.addForwards(this, this, '$');
        
        partyCertificationMainWin = (Window)getFellow("partyCertificationBandList").getFellow("partyCertificationMainWin");
        partyCertificationListboxExt = (PartyCertificationListboxExt)partyCertificationMainWin.getFellow("partyCertificationListboxExt");
        partyCertificationListbox = (Listbox)partyCertificationMainWin.getFellow("partyCertificationListboxExt").getFellow("partyCertificationListboxExtPanel").getFellow("partyCertificationListbox");
   
        partyCertificationMainWin.getFellow("partyCertificationListboxExt").addForward(SffOrPtyCtants.ON_PARTYCERTIFICATION_CONFIRM, this, "onSelectPartyCertificationReponse");
        partyCertificationMainWin.getFellow("partyCertificationListboxExt").addForward(SffOrPtyCtants.ON_PARTYCERTIFICATION_CLEAN, this, "onCleanPartyCertificationReponse");
        partyCertificationMainWin.getFellow("partyCertificationListboxExt").addForward(SffOrPtyCtants.ON_PARTYCERTIFICATION_CLOSE, this, "onClosePartyCertificationReponse");

        
    }
    
    public PartyCertificationListboxExt getPartyContactInfoListboxExt(){
        return partyCertificationListboxExt;
    }
    
    public PartyCertification getPartyCertificationListboxExt() {
        return partyCertification;
    }

    public void setPartyCertification(PartyCertification partyCertification) {
        this.setValue(null == partyCertification ? "" : partyCertification.getCertNumber());
        this.partyCertification = partyCertification;
    }
    
    /**
     * 选择员工响应
     * 
     * @param event
     */
    public void onSelectPartyCertificationReponse(final ForwardEvent event) throws Exception {
        partyCertification = (PartyCertification) event.getOrigin().getData();
        if (null != partyCertification) {
            setValue(partyCertification.getCertNumber());
            Events.postEvent(SffOrPtyCtants.ON_PARTYCERTIFICATION_VIEW, this, partyCertification);
        }
        this.close();
    }
    
    /**
     * 清空响应
     * 
     * @param event
     */
    public void onCleanPartyCertificationReponse(final ForwardEvent event) throws Exception {
        this.setPartyCertification(null);
        this.close();
    }

    /**
     * 关闭响应
     * 
     * @param event
     */
    public void onClosePartyCertificationReponse(final ForwardEvent event) throws Exception {
        this.close();
    }
    
}
