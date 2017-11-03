package cn.ffcs.uom.systemIssuedQuery.action.bean;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.organization.action.OrganizationListboxComposer;
import cn.ffcs.uom.staff.component.StaffListboxExt;
import cn.ffcs.uom.systemIssuedQuery.action.SystemIssuedListboxExt;


/**
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author fangy
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015年1月5日
 * @功能说明：下发系统查询bean
 * 
 */
public class SystemIssuedQueryBean {
    
    @Getter
    @Setter
    private SystemIssuedListboxExt systemIssuedListboxExt;
    
    @Getter
    @Setter
    private StaffListboxExt staffListboxExt;
    
    @Getter
    @Setter
    private OrganizationListboxComposer organizationListbox;
}
