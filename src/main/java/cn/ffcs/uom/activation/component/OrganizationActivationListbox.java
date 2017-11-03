package cn.ffcs.uom.activation.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.activation.component.bean.OrganizationActivationListboxBean;
import cn.ffcs.uom.activation.constants.ActivationConstant;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

/**
 * 员工激活管理显示列表控件 .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-09-13
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class OrganizationActivationListbox extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private OrganizationActivationListboxBean bean = new OrganizationActivationListboxBean();

	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	private CascadeRelationManager cascadeRelationManager = (CascadeRelationManager) ApplicationContextUtil
			.getBean("cascadeRelationManager");

	/**
	 * zul.
	 */
	private final String zul = "/pages/activation/comp/organization_activation_listbox.zul";

	/**
	 * organization.
	 */
	private Organization organization;

	/**
	 * 查询Organization.
	 */
	private Organization qryOrganization;

	/**
	 * OrganizationList.
	 */
	private List<Organization> organizationList;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 数据权限：区域
	 */
	private TelcomRegion permissionTelcomRegion;

	/**
	 * 数据权限：组织
	 */
	private Organization permissionOrganization;

	public OrganizationActivationListbox() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		if (PlatformUtil.getCurrentUser() != null) {
			/**
			 * 数据权限：电信管理区域
			 */
			permissionTelcomRegion = PermissionUtil
					.getPermissionTelcomRegion(PlatformUtil.getCurrentUser()
							.getRoleIds());
			bean.getTelcomRegionBandbox().setTelcomRegion(
					permissionTelcomRegion);
			permissionOrganization = PermissionUtil
					.getPermissionOrganization(PlatformUtil.getCurrentUser()
							.getRoleIds());
		}
		this.addForward(OrganizationConstant.ON_ORGANIZATION_QUERY, this,
				OrganizationConstant.ON_ORGANIZATION_QUERY_RESPONSE);
		// 去除默认查询this.onOrganizationActivationQuery();
		this.setOrganizationButtonValid(false);
	}

	public void onQueryOrganizationResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getOrganizationActivationListboxPaging().setActivePage(0);
		qryOrganization = (Organization) event.getOrigin().getData();
		this.onQueryOrganization();
		this.setOrganizationButtonValid(false);
	}

	/**
	 * 员工批量选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onOrganizationActivationSelectRequest() throws Exception {

		if (bean.getOrganizationActivationListbox().getSelectedCount() > 0) {

			this.setOrganizationButtonValid(true);

			Set set = bean.getOrganizationActivationListbox()
					.getSelectedItems();
			Iterator it = set.iterator();

			if (it != null) {
				organizationList = new ArrayList<Organization>();
				while (it.hasNext()) {
					Listitem listitem = (Listitem) it.next();
					organizationList.add((Organization) listitem.getValue());
				}
			}
		}
	}

	public void onOrganizationActivation() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		if (organizationList != null && organizationList.size() > 0) {
			ZkUtil.showQuestion("确定要激活这些记录?", "提示信息", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Integer result = (Integer) event.getData();
					if (result == Messagebox.OK) {

						String msg = null;

						if (!PlatformUtil.isAdmin()
								&& !cascadeRelationManager.isPermissions(
										PlatformUtil.getCurrentUser()
												.getRoleIds(),
										CascadeRelationConstants.RELA_CD_8)) {
							for (Organization organization : organizationList) {
								if (!PlatformUtil.getCurrentUserId().equals(
										organization.getUpdateStaff())) {
									msg = "该组织是由"
											+ organization
													.getUpdateStaffName2()
											+ "删除，请使用该员工账号进行激活。";
									break;
								}
							}
						}

						if (!StrUtil.isEmpty(msg)) {
							ZkUtil.showError(msg, "提示信息");
							return;
						}

						msg = organizationManager
								.updateOrganizationList(organizationList);
						if (!StrUtil.isEmpty(msg)) {
							ZkUtil.showError(msg, "提示信息");
						} else {
							ZkUtil.showError("激活成功！", "提示信息");
						}
						organizationList = null;
						onQueryOrganization();
					}
				}
			});

		} else {
			ZkUtil.showError("请选择你想要激活的组织", "提示信息");
		}
	}

	/**
	 * 设置员工按钮的状态.
	 * 
	 * @param canAct
	 *            激活按钮
	 */
	private void setOrganizationButtonValid(final Boolean canAct) {
		if (canAct != null) {
			bean.getOrganizationActBatchButton().setDisabled(!canAct);
		}
	}

	/**
	 * 查询实体 .
	 * 
	 * @author Wong 2013-5-27 Wong
	 */
	public void onQueryOrganization() {

		qryOrganization = new Organization();
		qryOrganization.setOrgCode(this.bean.getOrgCode().getValue());
		qryOrganization.setOrgName(this.bean.getOrgName().getValue());
		/**
		 * 电信管理查询条件
		 */
		if (this.bean.getTelcomRegionBandbox().getTelcomRegion() != null
				&& this.bean.getTelcomRegionBandbox().getTelcomRegion()
						.getTelcomRegionId() != null) {
			qryOrganization.setTelcomRegionId(this.bean
					.getTelcomRegionBandbox().getTelcomRegion()
					.getTelcomRegionId());
		}
		/**
		 * 数据权限：上级
		 */
		if (this.permissionOrganization != null
				&& permissionOrganization.getOrgId() != null) {
			qryOrganization.setOrgId(permissionOrganization.getOrgId());
		}

		if (this.qryOrganization != null) {

			this.setOrganizationButtonValid(false);

			PageInfo pageInfo = organizationManager
					.forQuertyOrganizationActivation(qryOrganization, bean
							.getOrganizationActivationListboxPaging()
							.getActivePage() + 1, bean
							.getOrganizationActivationListboxPaging()
							.getPageSize());

			ListModel dataList = new BindingListModelList(
					pageInfo.getDataList(), true);
			bean.getOrganizationActivationListbox().setModel(dataList);
			bean.getOrganizationActivationListboxPaging().setTotalSize(
					NumericUtil.nullToZero(pageInfo.getTotalCount()));
		}

	}

	/**
	 * 分页 .
	 * 
	 * @author Wong 2013-6-4 Wong
	 */
	public void onOrganizationActivationListboxPaging() {
		this.onQueryOrganization();
	}

	/**
	 * 查询员工. .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onOrganizationActivationQuery() {
		try {
			// cleanTabs();
			this.bean.getOrganizationActivationListboxPaging().setActivePage(0);
			this.onQueryOrganization();
			this.qryOrganization = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cleanTabs() {
		qryOrganization = null;
		organization = null;
		Events.postEvent(ActivationConstant.ON_ACTIVATION_CLEAR_TABS, this,
				null);
	}

	/**
	 * .重置查询内容 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-27 Wong
	 */
	public void onOrganizationActivationReset() throws Exception {
		bean.getOrgCode().setValue(null);
		bean.getOrgName().setValue(null);
		this.bean.getTelcomRegionBandbox().setTelcomRegion(
				permissionTelcomRegion);
	}

	/**
	 * 分配组织数据权限时设置数据区域
	 * 
	 * @param permissionTelcomRegion
	 */
	public void setPermissionTelcomRegion(TelcomRegion permissionTelcomRegion) {
		if (permissionTelcomRegion != null) {
			bean.getTelcomRegionBandbox().setTelcomRegion(
					permissionTelcomRegion);
			this.permissionTelcomRegion = permissionTelcomRegion;
			bean.getTelcomRegionBandbox().setDisabled(true);
		}
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canAct = false;

		if (PlatformUtil.isAdmin()) {
			canAct = true;
		} else if ("activationPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.ACTIVATION_ORGANIZATION)) {
				canAct = true;
			}
		}
		this.bean.getOrganizationActBatchButton().setVisible(canAct);
	}

}
