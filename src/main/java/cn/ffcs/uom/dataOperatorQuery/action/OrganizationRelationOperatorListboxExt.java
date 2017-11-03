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
import cn.ffcs.uom.dataOperatorQuery.action.bean.OrganizationRelationOperatorListboxBean;
import cn.ffcs.uom.dataOperatorQuery.action.bean.StaffOrganizationOperatorListboxBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 员工组织关系管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked", "unused" })
public class OrganizationRelationOperatorListboxExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private OrganizationRelationOperatorListboxBean bean = new OrganizationRelationOperatorListboxBean();

    /**
     * Manager.
     */
    @Autowired
    @Qualifier("organizationRelationManager")
    private OrganizationRelationManager organizationRelationManager = (OrganizationRelationManager) ApplicationContextUtil
            .getBean("organizationRelationManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/dataOperatorQuery/organization_relation_operator_listbox.zul";

    /**
     * 当前选择的organization
     */
    private OrganizationRelation organizationRelation;
    private Organization qryOrganization;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	public OrganizationRelationOperatorListboxExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward(OrganizationConstant.ON_SELECT_ORGANIZATION,this,"onSelectOrganizationResponse");
	}


	/**
	 * 查询组织关系.
	 * 
	 * @throws Exception
	 */
	public void onQueryOrganizationRelation() throws Exception {
		if (this.organizationRelation != null) {
			//qryStaffOrganization.setUserCode(this.bean.getUserCode().getValue());
	        qryOrganization.setOrgCode("");
	        qryOrganization.setOrgName("");
		    ListboxUtils.clearListbox(bean.getOrganizationRelationOperatorListBox());
			PageInfo pageInfo = organizationRelationManager.queryPageInfoByOrganizationRelationNoStatusCd(qryOrganization,organizationRelation, this.bean.getOrganizationRelationOperatorListBoxPaging().getActivePage() + 1, this.bean.getOrganizationRelationOperatorListBoxPaging().getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
			this.bean.getOrganizationRelationOperatorListBox().setModel(dataList);
			this.bean.getOrganizationRelationOperatorListBoxPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
		} else {
		    ZkUtil.showInformation("请先选择要查询的组织！", "系统提示");
		}
	}
	/**
	 * 员工组织关系查询响应
	 * 
	 * @param event
	 */
	public void onSelectOrganizationResponse(final ForwardEvent event) {
		try {
		    qryOrganization = (Organization) event.getOrigin().getData();
			if (null != qryOrganization && !StrUtil.isNullOrEmpty(qryOrganization.getOrgId())) {
			    organizationRelation = new OrganizationRelation();
			    organizationRelation.setOrgId(qryOrganization.getOrgId());
		        this.bean.getOrganizationRelationOperatorListBoxPaging().setActivePage(0);
			    onQueryOrganizationRelation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onQueryOrganizationRelationListBoxPaging() throws Exception {
        this.onQueryOrganizationRelation();
    }
}
