package cn.ffcs.uom.party.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class PartyRoleEditBean {
    
    @Getter
    @Setter
    private Window partyRoleEditWin;
    
    
    @Getter
    @Setter
    private Textbox partyName;
    
    @Getter
    @Setter
    private Listbox roleType;
    
}
