package cn.ffcs.uom.dataOperatorQuery.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;

public class OrganizationRelationOperatorListboxBean {
    
    @Getter
    @Setter
    private Listbox organizationRelationOperatorListBox;
    
    @Getter
    @Setter
    private Paging organizationRelationOperatorListBoxPaging;
}
