package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.service.RoleLocalServiceUtil;
import cn.ffcs.raptornuke.portal.service.UserLocalServiceUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.dao.RbacUserRoleDao;
import cn.ffcs.uom.rolePermission.manager.RbacUserRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacUserRole;

@Service("rbacUserRoleManager")
@Scope("prototype")
public class RbacUserRoleManagerImpl implements RbacUserRoleManager {

	@Resource
	private RbacUserRoleDao rbacUserRoleDao;

	@Resource
	private RbacRoleExtAttrDao rbacRoleExtAttrDao;

	@Override
	public PageInfo queryPageInfoRbacUserRole(RbacUserRole rbacUserRole,
			int currentPage, int pageSize) {
		return rbacUserRoleDao.queryPageInfoRbacUserRole(rbacUserRole,
				currentPage, pageSize);
	}

	@Override
	public List<RbacUserRole> queryRbacUserRoleList(RbacUserRole rbacUserRole) {
		return rbacUserRoleDao.queryRbacUserRoleList(rbacUserRole);
	}

	@Override
	public RbacUserRole queryRbacUserRole(RbacUserRole rbacUserRole) {
		return rbacUserRoleDao.queryRbacUserRole(rbacUserRole);
	}

	@Override
	public void saveRbacUserRole(RbacUserRole rbacUserRole) {
		rbacUserRole.addOnly();
	}

	@Override
	public void updateRbacUserRole(RbacUserRole rbacUserRole) {
		rbacUserRole.updateOnly();
	}

	@Override
	public void removeRbacUserRole(RbacUserRole rbacUserRole) {
		rbacUserRole.removeOnly();
	}

	@Override
	public void addRbacUserRoleList(List<RbacUserRole> rbacUserRoleList) {
		for (RbacUserRole rbacUserRole : rbacUserRoleList) {
			rbacUserRole.addOnly();
		}
	}

	@Override
	public void addRbacUserRoleToRaptornuke(RbacUserRole rbacUserRole)
			throws PortalException, SystemException {

		if (rbacUserRole != null && rbacUserRole.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacUserRole.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacUserRole.getRbacRoleId());
				rbacRoleExtAttr
						.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_2);

				rbacRoleExtAttr = rbacRoleExtAttrDao
						.queryRbacRoleExtAttr(rbacRoleExtAttr);

				if (rbacRoleExtAttr != null
						&& !StrUtil.isEmpty(rbacRoleExtAttr
								.getRbacRoleAttrValue())) {

					Role role = RoleLocalServiceUtil.getRole(Long
							.parseLong(rbacRoleExtAttr.getRbacRoleAttrValue()));

					if (role != null && role.getRoleId() != 0) {

						if (!RoleLocalServiceUtil.hasUserRole(
								rbacUserRole.getRbacUserId(), role.getRoleId())) {
							RoleLocalServiceUtil.addUserRoles(
									rbacUserRole.getRbacUserId(),
									new long[] { role.getRoleId() });
						}
					}

				}

			}
		}
	}

	@Override
	public void removeRbacUserRoleToRaptornuke(RbacUserRole rbacUserRole)
			throws PortalException, SystemException {

		if (rbacUserRole != null && rbacUserRole.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacUserRole.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacUserRole.getRbacRoleId());
				rbacRoleExtAttr
						.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_2);

				rbacRoleExtAttr = rbacRoleExtAttrDao
						.queryRbacRoleExtAttr(rbacRoleExtAttr);

				if (rbacRoleExtAttr != null
						&& !StrUtil.isEmpty(rbacRoleExtAttr
								.getRbacRoleAttrValue())) {

					Role role = RoleLocalServiceUtil.getRole(Long
							.parseLong(rbacRoleExtAttr.getRbacRoleAttrValue()));

					if (role != null && role.getRoleId() != 0) {

						if (UserLocalServiceUtil.hasRoleUser(role.getRoleId(),
								rbacUserRole.getRbacUserId())) {
							UserLocalServiceUtil.deleteRoleUser(
									role.getRoleId(),
									rbacUserRole.getRbacUserId());
						}
					}

				}

			}
		}
	}
}
