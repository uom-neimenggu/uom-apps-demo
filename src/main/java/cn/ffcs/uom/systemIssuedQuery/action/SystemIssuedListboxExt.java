package cn.ffcs.uom.systemIssuedQuery.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.businesssystem.manager.SystemOrgTreeConfigManager;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.systemIssuedQuery.action.bean.SystemIssuedListboxBean;

/**
 * 员工下发系统查询.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked", "unused" })
public class SystemIssuedListboxExt extends Div implements IdSpace {

	private static final long serialVersionUID = 1L;

	private final String zul = "/pages/systemIssuedQuery/system_issued_listbox.zul";

	private SystemIssuedListboxBean bean = new SystemIssuedListboxBean();

    private SystemOrgTreeConfigManager systemOrgTreeConfigManager = (SystemOrgTreeConfigManager) ApplicationContextUtil
        .getBean("systemOrgTreeConfigManagerImpl");
    /**
     * 当前选择的Staff
     */
    private Staff qryStaff;
    private Organization qryOrg;
    /**
     * 分页查询用，1标识员工下发系统分页，2标识组织下发系统分页
     */
    private int flag = 1;
	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public SystemIssuedListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_ISSUED_RESPONSE,this,"onSelectStaffResponse");
		this.addForward(OrganizationConstant.ON_ORGANIZATION_ISSUED_QUERY,this,"onSelectOrganizationResponse");
	}

    /**
     * 员工选中响应
     * 
     * @param event
     */
    public void onSelectStaffResponse(final ForwardEvent event) {
        try {
            qryStaff = (Staff) event.getOrigin().getData();
            if (null != qryStaff && !StrUtil.isNullOrEmpty(qryStaff.getStaffId())) {
                queryStaffIssued();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 组织选中响应
     * 
     * @param event
     */
    public void onSelectOrganizationResponse(final ForwardEvent event) {
        try {
            qryOrg = (Organization) event.getOrigin().getData();
            if (null != qryOrg && !StrUtil.isNullOrEmpty(qryOrg.getOrgId())) {
                queryOrgIssued();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * .
     * @param staff
     * @author fangy
     * 2015年1月7日
     */
    public PageInfo queryStaffIssued(){
        PageInfo pageInfo = null;
        if (qryStaff != null) {
            pageInfo = systemOrgTreeConfigManager.queryStaffIssued(qryStaff, bean
                    .getSystemIssuedListPaging().getActivePage() + 1, bean
                    .getSystemIssuedListPaging().getPageSize());

            ListModel dataList = new BindingListModelList(
                    pageInfo.getDataList(), true);
            bean.getSystemIssuedListBox().setModel(dataList);
            bean.getSystemIssuedListPaging().setTotalSize(
                    NumericUtil.nullToZero(pageInfo.getTotalCount()));
        }
        flag = 1;
        return pageInfo;
    }
    
    /**
     * 
     * .
     * @param organization
     * @author fangy
     * 2015年1月7日
     */
    public PageInfo queryOrgIssued(){
        PageInfo pageInfo = null;
        if (qryOrg != null) {
            pageInfo = systemOrgTreeConfigManager.queryOrganizationIssued(qryOrg, bean
                    .getSystemIssuedListPaging().getActivePage() + 1, bean
                    .getSystemIssuedListPaging().getPageSize());

            ListModel dataList = new BindingListModelList(
                    pageInfo.getDataList(), true);
            bean.getSystemIssuedListBox().setModel(dataList);
            bean.getSystemIssuedListPaging().setTotalSize(
                    NumericUtil.nullToZero(pageInfo.getTotalCount()));
        }
        flag = 2;
        return pageInfo;
    }
    
    public void onQuerySystemIssuedPaging(){
        if(flag == 1){
            this.queryStaffIssued();
        }else if(flag == 2){
            this.queryOrgIssued();
        }else{
            ZkUtil.showError("错误，请重新查询！", "系统提示");
        }
    }
}
