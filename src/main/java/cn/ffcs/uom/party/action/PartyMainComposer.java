package cn.ffcs.uom.party.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.party.action.bean.PartyMainBean;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

@Controller
@Scope("prototype")
@SuppressWarnings({"unused"})
public class PartyMainComposer extends BasePortletComposer implements IPortletInfoProvider{

    private static final long serialVersionUID = 1L;
    
    /**
     * bean.
     */
    private PartyMainBean bean = new PartyMainBean();
    
    private Party party ;
    @Override
	public String getPortletId() {
		// TODO Auto-generated method stub
		return super.getPortletId();
	}
	
	@Override
	public ThemeDisplay getThemeDisplay() {
		// TODO Auto-generated method stub
		return super.getThemeDisplay();
	}
	
    @Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		this.bean.getPartyListboxExt().setPortletInfoProvider(this);
		this.bean.getPartyContactInfoListboxExt().setPortletInfoProvider(this);
		this.bean.getPartyCertificationListboxExt()
				.setPortletInfoProvider(this);
		this.bean.getPartyRoleListboxExt().setPortletInfoProvider(this);
		bean.getPartyListboxExt().addForward(SffOrPtyCtants.ON_PARTY_SELECT,
				comp, "onSelectPartyResponse");
		bean.getPartyListboxExt().addForward(
				SffOrPtyCtants.ON_PARTY_MANAGE_QUERY, comp,
				"onSelectPartyResponse");
		bean.getPartyListboxExt().addForward(SffOrPtyCtants.ON_PARTY_DELETE,
				comp, "onDeletePartyResponse");
		bean.getPartyListboxExt().addForward(SffOrPtyCtants.ON_PARTY_SAVE,
				comp, "onSavePartyResponse");
		bean.getPartyListboxExt().addForward(
				SffOrPtyCtants.ON_PARTY_CLEAR_TABS, comp,
				SffOrPtyCtants.ON_PARTY_CLEAR_TABS_RESPONS);
		bean.getPartyListboxExt().setPartyWindowDivVisible(true);
		initPage();
	}
    
    /**
     * 选择员工列表的响应处理.
     * .
     * @param event
     * @throws Exception
     * @author Wong
     * 2013-5-28 Wong
     */
    public void onSelectPartyResponse(final ForwardEvent event)
            throws Exception {
        party = (Party) event.getOrigin().getData();
        callTab();
    }
    
    /**
     * tab响应
     * @throws Exception
     */
    public void callTab() throws Exception {
        if (this.bean.getTab() == null) {
            this.bean.setTab(this.bean.getTabBox().getSelectedTab());
        }
        if (party != null) {
            String tab = this.bean.getTab().getId();
            if ("partyContactInfoTab".equals(tab)) {
                bean.getPartyContactInfoListboxExt().setParty(party);
                bean.getPartyContactInfoListboxExt().init();
            } else if("partyCertificationTab".equals(tab)){
                bean.getPartyCertificationListboxExt().setParty(party);
                bean.getPartyCertificationListboxExt().init();
            } else if("partyRoleTab".equals(tab)){
                Events.postEvent(SffOrPtyCtants.ON_PARTY_ROLE_QUERY,this.bean.getPartyRoleListboxExt(), party);
            }
        } else {
        	bean.getPartyContactInfoListboxExt().onCleaningPartyContactInfo();
        	bean.getPartyCertificationListboxExt().onCleaningPartyCertification();
        	bean.getPartyRoleListboxExt().onCleanPartyRole();
        }
    }
    
    /**
     * 直接点击Tab页.
     * .
     * @param event
     * @throws Exception
     * @author Wong
     * 2013-6-8 Wong
     */
    public void onClickTab(ForwardEvent event) throws Exception {
        Event origin = event.getOrigin();
        if (origin != null) {
            Component comp = origin.getTarget();
            if (comp != null && comp instanceof Tab) {
                bean.setTab((Tab) comp);
                callTab();
            }
        }
    }
    
    /**
     * 选择参与人列表的响应处理.
     * .
     * @param event
     * @throws Exception
     * @author Wong
     * 2013-5-28 Wong
     */
    public void onDeletePartyResponse(final ForwardEvent event)
            throws Exception {
        party = null;
        Events.postEvent(SffOrPtyCtants.ON_PARTY_DELETE, self.getRoot(),null);
    }
    
    /**
     * 处理点击后Tabs
     * .
     * @param event
     * @author wangyong
     * 2013-6-11 wangyong
     */
    public void onPartyClearTabsRespons(final ForwardEvent event){
        Events.postEvent(SffOrPtyCtants.ON_CLEAN_PARTY_CER, bean.getPartyCertificationListboxExt(), null);
        Events.postEvent(SffOrPtyCtants.ON_CLEAN_PARTY_CON, bean.getPartyContactInfoListboxExt(), null);
    }
    
    /**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getPartyListboxExt().setPagePosition("partyPage");
		this.bean.getPartyContactInfoListboxExt().setPagePosition("partyPage");
		this.bean.getPartyCertificationListboxExt().setPagePosition("partyPage");
		this.bean.getPartyRoleListboxExt().setPagePosition("partyPage");
	}
}
