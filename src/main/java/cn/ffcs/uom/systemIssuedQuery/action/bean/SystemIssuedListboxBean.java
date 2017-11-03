package cn.ffcs.uom.systemIssuedQuery.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;

import cn.ffcs.uom.dataOperatorQuery.action.OrganizationRelationOperatorListboxExt;
import cn.ffcs.uom.dataOperatorQuery.action.PartyCertificationListboxExt;
import cn.ffcs.uom.dataOperatorQuery.action.PartyContactInfoListboxExt;
import cn.ffcs.uom.dataOperatorQuery.action.StaffOrganizationOperatorListboxExt;

/**
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author fangy
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015年1月5日
 * @功能说明：员工下发系统查询bean
 * 
 */
public class SystemIssuedListboxBean {
    
    /**
     * 分页插件
     */
    @Getter
    @Setter
    private Paging systemIssuedListPaging;
    
    /**
     * ListBox
     */
    @Getter
    @Setter
    private Listbox systemIssuedListBox;
}
