package cn.ffcs.uom.dataOperatorQuery.action.bean;

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
 * @版权：福富软件 版权所有 (c) 2014
 * @author fangy
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014年11月16日
 * @功能说明：数据操作人查询bean
 * 
 */
public class DataOperatorQueryBean {
    
    @Getter
    @Setter
    private Textbox staffAccount;
    
    @Getter
    @Setter
    private Textbox staffName;
    
    @Getter
    @Setter
    private Textbox staffCode;

    @Getter
    @Setter
    private Textbox orgName;
    
    @Getter
    @Setter
    private Textbox orgCode;
    
    @Getter
    @Setter
    private Textbox telcomRegionId;
    
    @Getter
    @Setter
    private Tab staffTab;
    
    @Getter
    @Setter
    private Tab staffOrganizationTab;
    
    @Getter
    @Setter
    private Tab organizationTab;
    
    @Getter
    @Setter
    private Tab organizationRelationTab;
    
    /**
     * 分页插件
     */
    @Getter
    @Setter
    private Paging staffOperatorListBoxPaging;
    
    /**
     * ListBox
     */
    @Getter
    @Setter
    private Listbox staffOperatorListBox;
    
    /**
     * 分页插件
     */
    @Getter
    @Setter
    private Paging orgOperatorListBoxPaging;
    
    /**
     * ListBox
     */
    @Getter
    @Setter
    private Listbox orgOperatorListBox;
    
    @Getter
    @Setter
    private StaffOrganizationOperatorListboxExt staffOrganizationOperatorListboxExt;
    
    /**
     * ListBox
     */
    @Getter
    @Setter
    private OrganizationRelationOperatorListboxExt organizationRelationOperatorListboxExt;
    
    /**
     * ListBox
     */
    @Getter
    @Setter
    private PartyCertificationListboxExt partyCertificationListboxExt;
    
    /**
     * ListBox
     */
    @Getter
    @Setter
    private PartyContactInfoListboxExt partyContactInfoListboxExt;
    
    /**
     * 当前选中的Tab页.
     */
    @Getter
    @Setter
    private Tab tab;
    
    /**
     * 当前选中的Tab页.
     */
    @Getter
    @Setter
    private Tabbox tabBox;
}
