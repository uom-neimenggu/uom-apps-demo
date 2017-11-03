package cn.ffcs.uom.rolePermission.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Tab;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.rolePermission.action.bean.RbacRoleTreeMainBean;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;
import cn.ffcs.uom.rolePermission.model.RbacRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleBusinessSystem;
import cn.ffcs.uom.rolePermission.model.RbacRoleRelation;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;
import cn.ffcs.uom.rolePermission.model.RbacUserRoleBusinessSystem;

@Controller
@Scope("prototype")
public class RbacRoleTreeMainComposer extends BasePortletComposer implements
		IPortletInfoProvider {

	private static final long serialVersionUID = 1L;

	private RbacRoleTreeMainBean bean = new RbacRoleTreeMainBean();

	/**
	 * 选中的角色
	 */
	private RbacRole rbacRole;

	/**
	 * 选中返回的员工角色
	 */
	private RbacUserRole rbacUserRole;

	/**
	 * 选中的角色关系
	 */
	private RbacRoleRelation rbacRoleRelation;

	@Override
	public String getPortletId() {
		return super.getPortletId();
	}

	@Override
	public ThemeDisplay getThemeDisplay() {
		return super.getThemeDisplay();
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		bean.getRbacRoleRelationTreeExt().setPortletInfoProvider(this);
		bean.getRbacRoleEditExt().setPortletInfoProvider(this);
		bean.getRbacUserRoleListboxExt().setPortletInfoProvider(this);
		bean.getRbacUserRoleBusinessSystemListboxExt().setPortletInfoProvider(
				this);
		bean.getRbacRoleBusinessSystemListboxExt().setPortletInfoProvider(this);
		bean.getRbacPermissionRoleListboxExt().setPortletInfoProvider(this);
		bean.getRbacRoleOrganizationListboxExt().setPortletInfoProvider(this);
		bean.getRbacRoleOrganizationLevelListboxExt().setPortletInfoProvider(
				this);
		bean.getRbacRoleTelcomRegionListboxExt().setPortletInfoProvider(this);
		bean.getRbacRolePolitLocationListboxExt().setPortletInfoProvider(this);

		/**
		 * 选择角色树上的角色
		 */
		this.bean.getRbacRoleRelationTreeExt().addForward(
				RolePermissionConstants.ON_SELECT_RBAC_ROLE_TREE_REQUEST,
				this.self,
				RolePermissionConstants.ON_SELECT_RBAC_ROLE_TREE_RESPONSE);

		/**
		 * 删除节点成功事件
		 */
		this.bean.getRbacRoleRelationTreeExt().addForward(
				RolePermissionConstants.ON_DEL_NODE_OK, this.self,
				"onDelNodeResponse");

		/**
		 * 角色信息保存事件
		 */
		this.bean.getRbacRoleEditExt().addForward(
				RolePermissionConstants.ON_SAVE_RBAC_ROLE_INFO, this.self,
				"onSaveRbacRoleInfoResponse");

		/**
		 * 员工角色信息选择事件
		 */
		this.bean.getRbacUserRoleListboxExt().addForward(
				RolePermissionConstants.ON_SELECT_RBAC_USER_ROLE_REQUEST,
				this.self,
				RolePermissionConstants.ON_SELECT_RBAC_USER_ROLE_RESPONSE);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$rbacRoleTreeMainWindow() throws Exception {
		initPage();
	}

	/**
	 * 角色信息保存
	 */
	public void onSaveRbacRoleInfoResponse(ForwardEvent event) throws Exception {
		if (event.getOrigin().getData() != null) {
			RbacRole rbacRole = (RbacRole) event.getOrigin().getData();
			if (rbacRole != null) {
				/**
				 * 角色信息保存可能对角色名称进行了修改
				 */
				Events.postEvent(
						RolePermissionConstants.ON_SAVE_RBAC_ROLE_INFO,
						this.bean.getRbacRoleRelationTreeExt(), rbacRole);
			}
		}
	}

	/**
	 * 删除节点事件,属性tab清空
	 */
	public void onDelNodeResponse() throws Exception {
		this.callLeftTab();
	}

	public void callLeftTab() {

		RbacRoleRelation rbacRoleRelation = this.bean
				.getRbacRoleRelationTreeExt().getSelectRbacRoleRelation();

		if (rbacRoleRelation != null
				&& rbacRoleRelation.getRbacRoleRelaId() != null) {
			rbacRole = rbacRoleRelation.getRbacRole();
		} else {
			rbacRole = null;
		}

		this.callTab();

	}

	/**
	 * 选择角色树
	 */
	public void onSelectRbacRoleTreeResponse(ForwardEvent event)
			throws Exception {

		RbacRoleRelation rbacRoleRelation = (RbacRoleRelation) event
				.getOrigin().getData();

		if (rbacRoleRelation != null
				&& rbacRoleRelation.getRbacRoleRelaId() != null) {
			this.rbacRoleRelation = rbacRoleRelation;
			rbacRole = rbacRoleRelation.getRbacRole();
			callTab();
		}

	}

	/**
	 * 点击tab
	 */
	public void onClickTab(ForwardEvent forwardEvent) throws Exception {
		Event event = forwardEvent.getOrigin();
		if (event != null) {
			Component component = event.getTarget();
			if (component != null && component instanceof Tab) {
				final Tab clickTab = (Tab) component;
				this.bean.setRightSelectTab(clickTab);
				callTab();
			}
		}
	}

	public void callTab() {

		if (this.bean.getRightSelectTab() == null) {
			bean.setRightSelectTab(this.bean.getRightTabbox().getSelectedTab());
		}

		if (rbacRole != null) {

			if ("rbacRoleTab".equals(this.bean.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_ROLE,
						this.bean.getRbacRoleEditExt(), rbacRole);
			}

			if ("rbacUserRoleTab".equals(this.bean.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_USER_ROLE_QUERY,
						this.bean.getRbacUserRoleListboxExt(), rbacRoleRelation);
			}

			if ("rbacRoleBusinessSystemTab".equals(this.bean
					.getRightSelectTab().getId())) {
				RbacRoleBusinessSystem rbacRoleBusinessSystem = new RbacRoleBusinessSystem();
				rbacRoleBusinessSystem.setRbacRoleId(rbacRoleRelation
						.getRbacRoleId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_BUSINESS_SYSTEM_QUERY,
						this.bean.getRbacRoleBusinessSystemListboxExt(),
						rbacRoleBusinessSystem);
			}

			if ("rbacPermissionRoleTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				RbacPermissionRole rbacPermissionRole = new RbacPermissionRole();
				rbacPermissionRole.setRbacRoleId(rbacRoleRelation
						.getRbacRoleId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_ROLE_QUERY,
						this.bean.getRbacPermissionRoleListboxExt(),
						rbacPermissionRole);
			}

			if ("rbacRoleOrganizationTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_QUERY,
						this.bean.getRbacRoleOrganizationListboxExt(),
						rbacRoleRelation);
			}

			if ("rbacRoleOrganizationLevelTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_LEVEL_QUERY,
						this.bean.getRbacRoleOrganizationLevelListboxExt(),
						rbacRoleRelation);
			}

			if ("rbacRoleTelcomRegionTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_TELCOM_REGION_QUERY,
						this.bean.getRbacRoleTelcomRegionListboxExt(),
						rbacRoleRelation);
			}

			if ("rbacRolePolitLocationTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_POLIT_LOCATION_QUERY,
						this.bean.getRbacRolePolitLocationListboxExt(),
						rbacRoleRelation);
			}

		} else {

			if ("rbacRoleTab".equals(this.bean.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_SELECT_TREE_RBAC_ROLE,
						this.bean.getRbacRoleEditExt(), null);
			}

			if ("rbacUserRoleTab".equals(this.bean.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_USER_ROLE_QUERY,
						this.bean.getRbacUserRoleListboxExt(), null);

			}

			if ("rbacRoleBusinessSystemTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_BUSINESS_SYSTEM_QUERY,
						this.bean.getRbacRoleBusinessSystemListboxExt(), null);

			}

			if ("rbacPermissionRoleTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_PERMISSION_ROLE_QUERY,
						this.bean.getRbacPermissionRoleListboxExt(), null);
			}

			if ("rbacRoleOrganizationTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_QUERY,
						this.bean.getRbacRoleOrganizationListboxExt(), null);

			}

			if ("rbacRoleOrganizationLevelTab".equals(this.bean
					.getRightSelectTab().getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_ORGANIZATION_LEVEL_QUERY,
						this.bean.getRbacRoleOrganizationLevelListboxExt(),
						null);

			}

			if ("rbacRoleTelcomRegionTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_TELCOM_REGION_QUERY,
						this.bean.getRbacRoleTelcomRegionListboxExt(), null);
			}

			if ("rbacRolePolitLocationTab".equals(this.bean.getRightSelectTab()
					.getId())) {
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_ROLE_POLIT_LOCATION_QUERY,
						this.bean.getRbacRolePolitLocationListboxExt(), null);
			}

		}
	}

	/**
	 * 直接点击Tab页. .
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClickRbacUserRoleTab(ForwardEvent event) throws Exception {
		Event origin = event.getOrigin();
		if (origin != null) {
			Component comp = origin.getTarget();
			if (comp != null && comp instanceof Tab) {
				bean.setRbacUserRoleSelectTab((Tab) comp);
				callRbacUserRoleTab();
			}
		}
	}

	/**
	 * 选择员工角色
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectRbacUserRoleResponse(final ForwardEvent event)
			throws Exception {
		rbacUserRole = (RbacUserRole) event.getOrigin().getData();
		callRbacUserRoleTab();
	}

	public void callRbacUserRoleTab() throws Exception {
		if (this.bean.getRbacUserRoleSelectTab() == null) {
			this.bean.setRbacUserRoleSelectTab(this.bean
					.getRbacUserRoleTabBox().getSelectedTab());
		}

		if (rbacUserRole != null) {

			String tab = this.bean.getRbacUserRoleSelectTab().getId();

			if ("rbacUserRoleBusinessSystemTab".equals(tab)) {
				RbacUserRoleBusinessSystem rbacUserRoleBusinessSystem = new RbacUserRoleBusinessSystem();
				rbacUserRoleBusinessSystem.setRbacUserRoleId(rbacUserRole
						.getRbacUserRoleId());
				Events.postEvent(
						RolePermissionConstants.ON_RBAC_USER_ROLE_BUSINESS_SYSTEM_QUERY,
						this.bean.getRbacUserRoleBusinessSystemListboxExt(),
						rbacUserRoleBusinessSystem);
			}

		} else {

			Events.postEvent(
					RolePermissionConstants.ON_RBAC_USER_ROLE_BUSINESS_SYSTEM_QUERY,
					this.bean.getRbacUserRoleBusinessSystemListboxExt(), null);
		}
	}

	/**
	 * 设置页面
	 */
	private void initPage() throws Exception {
		this.bean.getRbacRoleRelationTreeExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacRoleEditExt().setPagePosition("rbacRoleTreePage");
		this.bean.getRbacUserRoleListboxExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacUserRoleBusinessSystemListboxExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacRoleBusinessSystemListboxExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacPermissionRoleListboxExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacRoleOrganizationListboxExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacRoleOrganizationLevelListboxExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacRoleTelcomRegionListboxExt().setPagePosition(
				"rbacRoleTreePage");
		this.bean.getRbacRolePolitLocationListboxExt().setPagePosition(
				"rbacRoleTreePage");
	}

}
