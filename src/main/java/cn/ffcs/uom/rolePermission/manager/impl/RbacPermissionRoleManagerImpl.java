package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Permission;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.service.PermissionLocalServiceUtil;
import cn.ffcs.raptornuke.portal.service.RoleLocalServiceUtil;
import cn.ffcs.raptornuke.portal.service.persistence.PermissionUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionExtAttrDao;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionRoleDao;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacPermissionRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;

@Service("rbacPermissionRoleManager")
@Scope("prototype")
public class RbacPermissionRoleManagerImpl implements RbacPermissionRoleManager {

	@Resource
	private RbacPermissionRoleDao rbacPermissionRoleDao;

	@Resource
	private RbacRoleExtAttrDao rbacRoleExtAttrDao;

	@Resource
	private RbacPermissionExtAttrDao rbacPermissionExtAttrDao;

	@Override
	public PageInfo queryPageInfoRbacPermissionRole(
			RbacPermissionRole rbacPermissionRole, int currentPage, int pageSize) {
		return rbacPermissionRoleDao.queryPageInfoRbacPermissionRole(
				rbacPermissionRole, currentPage, pageSize);
	}

	@Override
	public List<RbacPermissionRole> queryRbacPermissionRoleList(
			RbacPermissionRole rbacPermissionRole) {
		return rbacPermissionRoleDao
				.queryRbacPermissionRoleList(rbacPermissionRole);
	}

	@Override
	public RbacPermissionRole queryRbacPermissionRole(
			RbacPermissionRole rbacPermissionRole) {
		return rbacPermissionRoleDao
				.queryRbacPermissionRole(rbacPermissionRole);
	}

	@Override
	public void saveRbacPermissionRole(RbacPermissionRole rbacPermissionRole) {
		rbacPermissionRole.addOnly();
	}

	@Override
	public void updateRbacPermissionRole(RbacPermissionRole rbacPermissionRole) {
		rbacPermissionRole.updateOnly();
	}

	@Override
	public void removeRbacPermissionRole(RbacPermissionRole rbacPermissionRole) {
		rbacPermissionRole.removeOnly();
	}

	@Override
	public void addRbacPermissionRoleList(
			List<RbacPermissionRole> rbacPermissionRoleList) {
		for (RbacPermissionRole rbacPermissionRole : rbacPermissionRoleList) {
			rbacPermissionRole.addOnly();
		}
	}

	@Override
	public void addRbacPermissionRoleToRaptornuke(
			RbacPermissionRole rbacPermissionRole) throws PortalException,
			SystemException {

		if (rbacPermissionRole != null
				&& rbacPermissionRole.getRbacPermissionId() != null
				&& rbacPermissionRole.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();
			RbacPermissionExtAttr rbacPermissionExtAttr = new RbacPermissionExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacPermissionRole.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacPermissionExtAttr.setRbacPermissionId(rbacPermissionRole
					.getRbacPermissionId());
			rbacPermissionExtAttr
					.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			rbacPermissionExtAttr = rbacPermissionExtAttrDao
					.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())
					&& rbacPermissionExtAttr != null
					&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacPermissionExtAttr
									.getRbacPermissionAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();
				rbacPermissionExtAttr = new RbacPermissionExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacPermissionRole
						.getRbacRoleId());
				rbacRoleExtAttr
						.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_2);

				rbacPermissionExtAttr.setRbacPermissionId(rbacPermissionRole
						.getRbacPermissionId());
				rbacPermissionExtAttr
						.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_3);

				rbacRoleExtAttr = rbacRoleExtAttrDao
						.queryRbacRoleExtAttr(rbacRoleExtAttr);

				rbacPermissionExtAttr = rbacPermissionExtAttrDao
						.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

				if (rbacRoleExtAttr != null
						&& !StrUtil.isEmpty(rbacRoleExtAttr
								.getRbacRoleAttrValue())
						&& rbacPermissionExtAttr != null
						&& !StrUtil.isEmpty(rbacPermissionExtAttr
								.getRbacPermissionAttrValue())) {

					Role role = RoleLocalServiceUtil.getRole(Long
							.parseLong(rbacRoleExtAttr.getRbacRoleAttrValue()));

					Permission permission = PermissionLocalServiceUtil
							.getPermission(Long.parseLong(rbacPermissionExtAttr
									.getRbacPermissionAttrValue()));

					if (role != null && role.getRoleId() != 0
							&& permission != null
							&& permission.getPermissionId() != 0) {
						if (!PermissionUtil.containsRole(
								permission.getPermissionId(), role.getRoleId())) {
							PermissionUtil.addRole(
									permission.getPermissionId(),
									role.getRoleId());
						}
					}

				}

			}
		}
	}

	@Override
	public void removeRbacPermissionRoleToRaptornuke(
			RbacPermissionRole rbacPermissionRole) throws PortalException,
			SystemException {

		if (rbacPermissionRole != null
				&& rbacPermissionRole.getRbacPermissionId() != null
				&& rbacPermissionRole.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();
			RbacPermissionExtAttr rbacPermissionExtAttr = new RbacPermissionExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacPermissionRole.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacPermissionExtAttr.setRbacPermissionId(rbacPermissionRole
					.getRbacPermissionId());
			rbacPermissionExtAttr
					.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			rbacPermissionExtAttr = rbacPermissionExtAttrDao
					.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())
					&& rbacPermissionExtAttr != null
					&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacPermissionExtAttr
									.getRbacPermissionAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();
				rbacPermissionExtAttr = new RbacPermissionExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacPermissionRole
						.getRbacRoleId());
				rbacRoleExtAttr
						.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_2);

				rbacPermissionExtAttr.setRbacPermissionId(rbacPermissionRole
						.getRbacPermissionId());
				rbacPermissionExtAttr
						.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_3);

				rbacRoleExtAttr = rbacRoleExtAttrDao
						.queryRbacRoleExtAttr(rbacRoleExtAttr);

				rbacPermissionExtAttr = rbacPermissionExtAttrDao
						.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

				if (rbacRoleExtAttr != null
						&& !StrUtil.isEmpty(rbacRoleExtAttr
								.getRbacRoleAttrValue())
						&& rbacPermissionExtAttr != null
						&& !StrUtil.isEmpty(rbacPermissionExtAttr
								.getRbacPermissionAttrValue())) {

					Role role = RoleLocalServiceUtil.getRole(Long
							.parseLong(rbacRoleExtAttr.getRbacRoleAttrValue()));

					Permission permission = PermissionLocalServiceUtil
							.getPermission(Long.parseLong(rbacPermissionExtAttr
									.getRbacPermissionAttrValue()));

					if (role != null && role.getRoleId() != 0
							&& permission != null
							&& permission.getPermissionId() != 0) {
						if (PermissionUtil.containsRole(
								permission.getPermissionId(), role.getRoleId())) {
							PermissionUtil.removeRole(
									permission.getPermissionId(),
									role.getRoleId());
						}
					}

				}

			}
		}
	}
}
