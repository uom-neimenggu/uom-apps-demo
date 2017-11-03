package cn.ffcs.uom.dataOperatorQuery.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;

public class StaffOrganizationOperatorListboxBean {
    
    @Getter
    @Setter
    private Listbox staffOrganizationListBox;
    
    @Getter
    @Setter
    private Paging staffOrganizationListPaging;
}
