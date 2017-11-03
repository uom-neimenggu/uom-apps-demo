package cn.ffcs.uom.systemIssuedQuery.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.systemIssuedQuery.action.bean.SystemIssuedQueryBean;

/**
 * 员工管理实体Bean .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015-1-6
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class SystemIssuedQueryComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private SystemIssuedQueryBean bean = new SystemIssuedQueryBean();

	/**
	 * 数据权限：组织
	 */
	private List<Organization> permissionOrganizationList;

	private Staff staff;
	private Organization organization;

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	public SystemIssuedQueryComposer() throws Exception {
		permissionOrganizationList = null;
		loadPermission();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getStaffListboxExt().setPortletInfoProvider(this);
		bean.getSystemIssuedListboxExt().setPortletInfoProvider(this);
		bean.getStaffListboxExt().addForward(SffOrPtyCtants.ON_STAFF_SELECT,
				comp, "onSelectStaffResponse");
		bean.getOrganizationListbox().addForward(
				OrganizationConstant.ON_SELECT_ORGANIZATION, comp,
				"onSelectOrganizationResponse");
	}

	/**
	 * 加载数据权
	 * 
	 * @throws Exception
	 */
	public void loadPermission() throws Exception {
		if (PlatformUtil.getCurrentUser() != null) {
			if (PlatformUtil.isAdmin()) {
				permissionOrganizationList = new ArrayList<Organization>();
				Organization rootParentOrg = new Organization();
				rootParentOrg
						.setOrgId(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
				permissionOrganizationList.add(rootParentOrg);
			} else {
				permissionOrganizationList = PermissionUtil
						.getPermissionOrganizationList(PlatformUtil
								.getCurrentUser().getRoleIds());
			}
		}
	}

	/**
	 * 选择员工列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author Wong 2013-5-28 Wong
	 */
	public void onSelectStaffResponse(final ForwardEvent event)
			throws Exception {
		staff = (Staff) event.getOrigin().getData();
		try {
			if (staff != null) {
				Events.postEvent(SffOrPtyCtants.ON_STAFF_ISSUED_RESPONSE,
						this.bean.getSystemIssuedListboxExt(), staff);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 选择组织列表的响应处理. .
	 * 
	 * @param event
	 * @throws Exception
	 * @author Wong 2013-5-28 Wong
	 */
	public void onSelectOrganizationResponse(final ForwardEvent event)
			throws Exception {
		organization = (Organization) event.getOrigin().getData();
		try {
			if (organization != null) {
				Events.postEvent(
						OrganizationConstant.ON_ORGANIZATION_ISSUED_QUERY,
						this.bean.getSystemIssuedListboxExt(), organization);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
