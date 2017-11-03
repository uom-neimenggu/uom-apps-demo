package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacPermissionEditExt;
import cn.ffcs.uom.rolePermission.component.RbacPermissionRelationTreeExt;
import cn.ffcs.uom.rolePermission.component.RbacPermissionResourceListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacPermissionRoleListboxExt;

public class RbacPermissionTreeMainBean {

	@Getter
	@Setter
	private Window rbacPermissionTreeMainWindow;

	@Getter
	@Setter
	private RbacPermissionRelationTreeExt rbacPermissionRelationTreeExt;

	@Getter
	@Setter
	private RbacPermissionEditExt rbacPermissionEditExt;

	@Getter
	@Setter
	private RbacPermissionRoleListboxExt rbacPermissionRoleListboxExt;

	@Getter
	@Setter
	private RbacPermissionResourceListboxExt rbacPermissionResourceListboxExt;

	@Getter
	@Setter
	private Tab rbacPermissionRelationTab;

	@Getter
	@Setter
	private Tab rbacPermissionTab;

	@Getter
	@Setter
	private Tab rbacPermissionRoleTab;

	@Getter
	@Setter
	private Tab rbacPermissionReourceTab;

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
}
