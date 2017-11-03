package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacPermissionRoleListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleBusinessSystemListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleEditExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleOrganizationLevelListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleOrganizationListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRolePolitLocationListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleRelationTreeExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleTelcomRegionListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacUserRoleBusinessSystemListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacUserRoleListboxExt;

public class RbacRoleTreeMainBean {

	@Getter
	@Setter
	private Window rbacRoleTreeMainWindow;

	@Getter
	@Setter
	private RbacRoleRelationTreeExt rbacRoleRelationTreeExt;

	@Getter
	@Setter
	private RbacRoleEditExt rbacRoleEditExt;

	@Getter
	@Setter
	private RbacRoleBusinessSystemListboxExt rbacRoleBusinessSystemListboxExt;

	@Getter
	@Setter
	private RbacPermissionRoleListboxExt rbacPermissionRoleListboxExt;

	@Getter
	@Setter
	private RbacUserRoleListboxExt rbacUserRoleListboxExt;

	@Getter
	@Setter
	private RbacUserRoleBusinessSystemListboxExt rbacUserRoleBusinessSystemListboxExt;

	@Getter
	@Setter
	private RbacRoleOrganizationListboxExt rbacRoleOrganizationListboxExt;

	@Getter
	@Setter
	private RbacRoleOrganizationLevelListboxExt rbacRoleOrganizationLevelListboxExt;

	@Getter
	@Setter
	private RbacRoleTelcomRegionListboxExt rbacRoleTelcomRegionListboxExt;

	@Getter
	@Setter
	private RbacRolePolitLocationListboxExt rbacRolePolitLocationListboxExt;

	@Getter
	@Setter
	private Tab rbacRoleRelationTab;

	@Getter
	@Setter
	private Tab rbacRoleTab;

	@Getter
	@Setter
	private Tab rbacUserRoleTab;

	@Getter
	@Setter
	private Tab rbacRoleBusinessSystemTab;

	@Getter
	@Setter
	private Tab rbacPermissionRoleTab;

	@Getter
	@Setter
	private Tab rbacRoleOrganizationTab;

	@Getter
	@Setter
	private Tab rbacRoleOrganizationLevelTab;

	@Getter
	@Setter
	private Tab rbacRoleTelcomRegionTab;

	@Getter
	@Setter
	private Tab rbacRolePolitLocationTab;

	/**
	 * 右边tabBox
	 */
	@Getter
	@Setter
	private Tabbox rightTabbox;

	/**
	 * 右边tabBox页选中的tab
	 */
	@Getter
	@Setter
	private Tab rightSelectTab;

	@Getter
	@Setter
	private Tabbox rbacUserRoleTabBox;

	@Getter
	@Setter
	private Tab rbacUserRoleSelectTab;

	@Getter
	@Setter
	private Tab rbacUserRoleBusinessSystemTab;
}
