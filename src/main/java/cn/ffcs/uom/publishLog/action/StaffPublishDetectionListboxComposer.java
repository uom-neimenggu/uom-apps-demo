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
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.common.zul.PubUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.publishLog.action.bean.StaffPublishDetectionListboxBean;
import cn.ffcs.uom.publishLog.manager.PublishDetectionManager;
import cn.ffcs.uom.publishLog.manager.StaffPublishDetectionManager;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
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
public class StaffPublishDetectionListboxComposer extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private StaffPublishDetectionListboxBean bean = new StaffPublishDetectionListboxBean();

	/**
	 * zul.
	 */
	private final String zul = "/pages/publishLog/staff_publish_detection_listbox.zul";


	@Getter
	@Setter
	private Staff staff;

	private Map arg = new HashMap();

	@Autowired
	@Qualifier("staffManager")
	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");


	
	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;
	
	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;
	

	public StaffPublishDetectionListboxComposer() throws Exception {
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
		setStaffPublishDetectionButtonValid(false);
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
	private void setStaffPublishDetectionButtonValid(final Boolean canView) {
		bean.getViewButton().setDisabled(!canView);
	}


	/**
	 * 查询按钮
	 * 
	 * @throws Exception
	 */
	public PageInfo onQueryStaffPublishDetection() throws Exception {
		staff = Staff.newInstance();
		PubUtil.fillPoFromBean(bean, staff);
		if (this.permissionOrganizationList != null&& this.permissionOrganizationList.size() > 0) {
			staff.setPermissionOrganizationList(permissionOrganizationList);
		}
		/**
		 * 默认数据权最大电信管理区域
		 */
		if (StrUtil.isNullOrEmpty(permissionTelcomRegion)) {
			permissionTelcomRegion = new TelcomRegion();
			permissionTelcomRegion.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
		}
		staff.setPermissionTelcomRegion(permissionTelcomRegion);

		PageInfo pageInfo = null;
		if (this.staff != null) {
			pageInfo = staffManager.forQuertyStaff(staff, bean.getStaffPublishDetectionListPaging().getActivePage() + 1, bean.getStaffPublishDetectionListPaging().getPageSize());
			ListModel dataList = new BindingListModelList(pageInfo.getDataList(), true);
			bean.getStaffPublishDetectionListBox().setModel(dataList);
			bean.getStaffPublishDetectionListPaging().setTotalSize(NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}
		return pageInfo;
	}
	
	/**
	 * 查询条件重置按钮
	 * @throws Exception
	 */
	public void onResetStaffPublishDetection() throws Exception {
		this.bean.getStaffAccount().setValue("");
		this.bean.getStaffCode().setValue("");
		this.bean.getStaffName().setValue("");
	}
	
	
	/**
	 * 选择数据事件
	 * @throws Exception
	 */
	public void onStaffPublishDetectionSelectRequest() throws Exception{
		if(bean.getStaffPublishDetectionListBox().getSelectedIndex() != -1){
			staff = (Staff)(bean.getStaffPublishDetectionListBox().getSelectedItem().getValue());
			this.setStaffPublishDetectionButtonValid(true);
			arg.put("staffId", staff.getStaffId());
			arg.put("orgCode", "");
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
