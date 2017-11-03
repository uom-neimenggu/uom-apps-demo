package cn.ffcs.uom.dataOperatorQuery.action;

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
import cn.ffcs.uom.dataOperatorQuery.action.bean.StaffOrganizationOperatorListboxBean;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 员工组织关系管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked", "unused" })
public class StaffOrganizationOperatorListboxExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private StaffOrganizationOperatorListboxBean bean = new StaffOrganizationOperatorListboxBean();

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("staffOrganizationManager")
	private StaffOrganizationManager staffOrganizationManager = (StaffOrganizationManager) ApplicationContextUtil
			.getBean("staffOrganizationManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/dataOperatorQuery/staff_organization_operator_listbox.zul";

	/**
	 * 当前选择的staffOrganization
	 */
	private StaffOrganization staffOrganization;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 查询staffOrganization.
	 */
	private StaffOrganization qryStaffOrganization;
	private Staff qryStaff;

	public StaffOrganizationOperatorListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(SffOrPtyCtants.ON_STAFF_Org_QUERY,this,SffOrPtyCtants.ON_STAFF_Org_QUERY_RESP);
	}


	/**
	 * 查询员工组织关系.
	 * 
	 * @throws Exception
	 */
	public void onQueryStaffOrganization() throws Exception {
		if (this.qryStaffOrganization != null) {
			//qryStaffOrganization.setUserCode(this.bean.getUserCode().getValue());
			ListboxUtils.clearListbox(bean.getStaffOrganizationListBox());
			PageInfo pageInfo = staffOrganizationManager.queryPageInfoByStaffOrganizationNoStatusCd(qryStaff,qryStaffOrganization, this.bean.getStaffOrganizationListPaging().getActivePage() + 1, this.bean.getStaffOrganizationListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
			this.bean.getStaffOrganizationListBox().setModel(dataList);
			this.bean.getStaffOrganizationListPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
		    ZkUtil.showInformation("请先选择要查询的员工！", "系统提示");
		}
	}
	/**
	 * 员工组织关系查询响应
	 * 
	 * @param event
	 */
	public void onStaffOrgQueryResponse(final ForwardEvent event) {
		try {
		    qryStaff = (Staff) event.getOrigin().getData();
			if (null != qryStaff && !StrUtil.isNullOrEmpty(qryStaff.getStaffId())) {
				qryStaffOrganization = new StaffOrganization();
				qryStaffOrganization.setStaffId(qryStaff.getStaffId());
	            this.bean.getStaffOrganizationListPaging().setActivePage(0);
				onQueryStaffOrganization();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onQueryStaffOrganizationPaging() throws Exception{
	    this.onQueryStaffOrganization();
	}
}
