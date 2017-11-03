package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacBusinessSystemEditExt;
import cn.ffcs.uom.rolePermission.component.RbacBusinessSystemRelationTreeExt;
import cn.ffcs.uom.rolePermission.component.RbacBusinessSystemResourceListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacRoleBusinessSystemListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacUserRoleBusinessSystemListboxExt;

public class RbacBusinessSystemTreeMainBean {

	@Getter
	@Setter
	private Window rbacBusinessSystemTreeMainWindow;

	@Getter
	@Setter
	private RbacBusinessSystemRelationTreeExt rbacBusinessSystemRelationTreeExt;

	@Getter
	@Setter
	private RbacBusinessSystemEditExt rbacBusinessSystemEditExt;

	@Getter
	@Setter
	private RbacBusinessSystemResourceListboxExt rbacBusinessSystemResourceListboxExt;

	@Getter
	@Setter
	private RbacRoleBusinessSystemListboxExt rbacRoleBusinessSystemListboxExt;

	@Getter
	@Setter
	private RbacUserRoleBusinessSystemListboxExt rbacUserRoleBusinessSystemListboxExt;

	@Getter
	@Setter
	private Tab rbacBusinessSystemRelationTab;

	@Getter
	@Setter
	private Tab rbacBusinessSystemTab;

	@Getter
	@Setter
	private Tab rbacBusinessSystemResourceTab;

	@Getter
	@Setter
	private Tab rbacRoleBusinessSystemTab;

	@Getter
	@Setter
	private Tab rbacUserRoleBusinessSystemTab;

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
