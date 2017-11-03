package cn.ffcs.uom.rolePermission.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.rolePermission.component.RbacBusinessSystemResourceListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacPermissionResourceListboxExt;
import cn.ffcs.uom.rolePermission.component.RbacResourceEditExt;
import cn.ffcs.uom.rolePermission.component.RbacResourceRelationTreeExt;

public class RbacResourceTreeMainBean {

	@Getter
	@Setter
	private Window rbacResourceTreeMainWindow;

	@Getter
	@Setter
	private RbacResourceRelationTreeExt rbacResourceRelationTreeExt;

	@Getter
	@Setter
	private RbacResourceEditExt rbacResourceEditExt;

	@Getter
	@Setter
	private RbacPermissionResourceListboxExt rbacPermissionResourceListboxExt;
	
	@Getter
	@Setter
	private RbacBusinessSystemResourceListboxExt rbacBusinessSystemResourceListboxExt;

	@Getter
	@Setter
	private Tab rbacResourceRelationTab;

	@Getter
	@Setter
	private Tab rbacResourceTab;

	@Getter
	@Setter
	private Tab rbacPermissionReourceTab;

	@Getter
	@Setter
	private Tab rbacBusinessSystemResourceTab;

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
