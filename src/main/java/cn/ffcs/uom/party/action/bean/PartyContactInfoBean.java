package cn.ffcs.uom.party.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Window;

import cn.ffcs.uom.party.component.PartyContactInfoListboxExt;

public class PartyContactInfoBean {
    
    @Setter
    @Getter
    private Window partyContactInfoMainWin;
    
    @Setter
    @Getter
    private PartyContactInfoListboxExt partyContactInfoListboxExt;
}
