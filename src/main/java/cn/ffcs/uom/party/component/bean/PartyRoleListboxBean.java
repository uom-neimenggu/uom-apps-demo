package cn.ffcs.uom.party.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;

public class PartyRoleListboxBean {
    
    @Getter
    @Setter
    private Div partyRoleBandboxDiv;
    
    @Getter
    @Setter
    private Button addPartyRoleButton;
    
    @Getter
    @Setter
    private Button editPartyRoleButton;
    
    @Getter
    @Setter
    private Button delPartyRoleButton;
    
    @Getter
    @Setter
    private Listbox partyRoleListbox;
    
    @Getter
    @Setter
    private Paging partyRoleListboxPaging;
    
    /**
	 * Div.
	 **/
	@Getter
	@Setter
	private Div partyRoleSearchDiv;
    
	/**
	 * 角色类型.
	 **/
    @Getter
    @Setter
    private Listbox roleType;
    
}
