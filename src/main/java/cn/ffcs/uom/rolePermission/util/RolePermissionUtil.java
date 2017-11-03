package cn.ffcs.uom.rolePermission.util;

import java.util.List;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRoleManager;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;

public class RolePermissionUtil {
	private static RbacUserRoleManager rbacUserRoleManager = (RbacUserRoleManager) ApplicationContextUtil
			.getBean("rbacUserRoleManager");

	private static RbacPermissionRoleManager rbacPermissionRoleManager = (RbacPermissionRoleManager) ApplicationContextUtil
			.getBean("rbacPermissionRoleManager");

	/**
	 * 是否是所有权限
	 * 
	 * @return
	 */
	public static boolean isAllPermission(Long userId) {
		RbacUserRole rbacUserRole = new RbacUserRole();
		rbacUserRole.setRbacUserId(userId);

		List<RbacUserRole> list = rbacUserRoleManager
				.queryRbacUserRoleList(rbacUserRole);

		if (list != null && list.size() > 0) {
			for (RbacUserRole r : list) {
				if (r.getRbacRoleId() == 64) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static boolean isLocalAgencyAdmin(Long userId) {
		RbacUserRole rbacUserRole = new RbacUserRole();
		rbacUserRole.setRbacUserId(userId);

		List<RbacUserRole> list = rbacUserRoleManager
				.queryRbacUserRoleList(rbacUserRole);

		if (list != null && list.size() > 0) {
			for (RbacUserRole r : list) {
				if (r.getRbacRoleId() == 48) {
					return true;
				}
			}
		}
		
		return false;
	}

	public static boolean checkHasPermission(Long userId, String permissionCode) {
		RbacUserRole rbacUserRole = new RbacUserRole();
		rbacUserRole.setRbacUserId(userId);

		List<RbacUserRole> list = rbacUserRoleManager
				.queryRbacUserRoleList(rbacUserRole);

		for (RbacUserRole r : list) {
			RbacPermissionRole rbacPermissionRole = new RbacPermissionRole();
			rbacPermissionRole.setRbacRoleId(r.getRbacRoleId());

			List<RbacPermissionRole> rbacPermissionRoleList = rbacPermissionRoleManager
					.queryRbacPermissionRoleList(rbacPermissionRole);

			for (RbacPermissionRole pr : rbacPermissionRoleList) {
				RbacPermission rbacPermission = (RbacPermission) RbacPermission
						.repository().getObject(RbacPermission.class,
								pr.getRbacPermissionId());
				if(permissionCode.equals(rbacPermission.getRbacPermissionCode())) {
					return true;
				}
			}
		}
		
		return false;
	}

}
