package cn.ffcs.uom.publishLog.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Window;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.publishLog.action.bean.OrgPublishDetectionListboxBean;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 员工组织关系管理.
 * 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unchecked", "unused" })
public class OrgPublishDetectionListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private OrgPublishDetectionListboxBean bean = new OrgPublishDetectionListboxBean();

	/**
	 * zul.
	 */
	private final String zul = "/pages/publishLog/org_publish_detection_listbox.zul";


	@Getter
	@Setter
	private Organization organization;

	private Map arg = new HashMap();

	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil.getBean("organizationManager");


	
	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;
	
	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;
	

	public OrgPublishDetectionListboxComposer() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		setOrgPublishDetectionButtonValid(false);
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				permissionOrganizationList = new ArrayList<Organization>();
				Organization rootParentOrg = new Organization();
				rootParentOrg.setOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
				permissionOrganizationList.add(rootParentOrg);
				/**
				 * admin默认中国
				 */
				permissionTelcomRegion = new TelcomRegion();
				permissionTelcomRegion.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			} else {
				permissionOrganizationList = PermissionUtil.getPermissionOrganizationList(PlatformUtil.getCurrentUser().getRoleIds());
				permissionTelcomRegion = PermissionUtil.getPermissionTelcomRegion(PlatformUtil.getCurrentUser().getRoleIds());
			}
		}
	}

	
	/**
	 * 设置员工组织关系按钮的状态.
	 * @param canView 查看组织按钮
	 */
	private void setOrgPublishDetectionButtonValid(final Boolean canView) {
		bean.getViewButton().setDisabled(!canView);
	}


	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public PageInfo onQueryOrgPublishDetection() throws Exception {
		organization = new Organization();
		PubUtil.fillPoFromBean(bean, organization);
		
		organization.setTelcomRegionId(permissionTelcomRegion != null ? permissionTelcomRegion.getTelcomRegionId() : null);
		/**
		 * 数据权限：上级
		 */
		if (this.permissionOrganizationList != null&& permissionOrganizationList.size() != 0) {
			organization.setPermissionOrganizationList(permissionOrganizationList);
		}
		PageInfo pageInfo = organizationManager.queryPageInfoByOrganization(organization, this.bean.getOrgPublishDetectionListPaging().getActivePage() + 1,this.bean.getOrgPublishDetectionListPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
		this.bean.getOrgPublishDetectionListBox().setModel(dataList);
		this.bean.getOrgPublishDetectionListPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
		return pageInfo;
	}
	
	/**
	 * 查询条件重置按钮
	 * @throws Exception
	 */
	public void onResetOrgPublishDetection() throws Exception {
		this.bean.getOrgCode().setValue("");
		this.bean.getOrgName().setValue("");
	}
	
	
	/**
	 * 选择数据事件
	 * @throws Exception
	 */
	public void onOrgPublishDetectionSelectRequest() throws Exception{
		if(bean.getOrgPublishDetectionListBox().getSelectedIndex() != -1){
			organization = (Organization)(bean.getOrgPublishDetectionListBox().getSelectedItem().getValue());
			this.setOrgPublishDetectionButtonValid(true);
			arg.put("orgCode", organization.getOrgCode());
			arg.put("staffId", "");
		}
	}
	/**
	 * 查看事件
	 * @throws Exception
	 */
	public void onView() throws Exception{
		Window win = (Window) Executions.createComponents("/pages/publishLog/publish_detection_info.zul", this, arg);
		win.doModal();
	}
	
	
}
