package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.service.RoleLocalServiceUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.dao.AroleOrganizationDao;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.dao.RbacRoleOrganizationDao;
import cn.ffcs.uom.rolePermission.manager.RbacRoleOrganizationManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganization;

@Service("rbacRoleOrganizationManager")
@Scope("prototype")
public class RbacRoleOrganizationManagerImpl implements
		RbacRoleOrganizationManager {

	@Resource
	private RbacRoleExtAttrDao rbacRoleExtAttrDao;

	@Resource
	private AroleOrganizationDao aroleOrganizationDao;

	@Resource
	private RbacRoleOrganizationDao rbacRoleOrganizationDao;

	@Override
	public PageInfo queryPageInfoRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization, int currentPage,
			int pageSize) {
		return rbacRoleOrganizationDao.queryPageInfoRbacRoleOrganization(
				rbacRoleOrganization, currentPage, pageSize);
	}

	@Override
	public List<RbacRoleOrganization> queryRbacRoleOrganizationList(
			RbacRoleOrganization rbacRoleOrganization) {
		return rbacRoleOrganizationDao
				.queryRbacRoleOrganizationList(rbacRoleOrganization);
	}

	@Override
	public RbacRoleOrganization queryRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization) {
		return rbacRoleOrganizationDao
				.queryRbacRoleOrganization(rbacRoleOrganization);
	}

	@Override
	public void saveRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization) {
		rbacRoleOrganization.addOnly();
	}

	@Override
	public void updateRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization) {
		rbacRoleOrganization.updateOnly();
	}

	@Override
	public void removeRbacRoleOrganization(
			RbacRoleOrganization rbacRoleOrganization) {
		rbacRoleOrganization.removeOnly();
	}

	@Override
	public void addRbacRoleOrganizationList(
			List<RbacRoleOrganization> rbacRoleOrganizationList) {
		for (RbacRoleOrganization rbacRoleOrganization : rbacRoleOrganizationList) {
			rbacRoleOrganization.addOnly();
		}
	}

	@Override
	public void addRbacRoleOrganizationToRaptornuke(
			RbacRoleOrganization rbacRoleOrganization) throws PortalException,
			SystemException {

		if (rbacRoleOrganization != null
				&& rbacRoleOrganization.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganization.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganization
						.getRbacRoleId());
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
						AroleOrganization aroleOrganization = new AroleOrganization();
						aroleOrganization.setAroleId(Long
								.parseLong(rbacRoleExtAttr
										.getRbacRoleAttrValue()));
						aroleOrganization.setOrgId(rbacRoleOrganization
								.getRbacOrgId());
						aroleOrganization = aroleOrganizationDao
								.queryAroleOrganization(aroleOrganization);

						if (aroleOrganization == null) {
							aroleOrganization = new AroleOrganization();
							aroleOrganization.setAroleId(Long
									.parseLong(rbacRoleExtAttr
											.getRbacRoleAttrValue()));
							aroleOrganization.setOrgId(rbacRoleOrganization
									.getRbacOrgId());
							aroleOrganization.addOnly();
						}
					}

				}

			}
		}
	}

	@Override
	public void removeRbacRoleOrganizationToRaptornuke(
			RbacRoleOrganization rbacRoleOrganization) throws PortalException,
			SystemException {

		if (rbacRoleOrganization != null
				&& rbacRoleOrganization.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganization.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganization
						.getRbacRoleId());
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
						AroleOrganization aroleOrganization = new AroleOrganization();
						aroleOrganization.setAroleId(Long
								.parseLong(rbacRoleExtAttr
										.getRbacRoleAttrValue()));
						aroleOrganization.setOrgId(rbacRoleOrganization
								.getRbacOrgId());
						aroleOrganization = aroleOrganizationDao
								.queryAroleOrganization(aroleOrganization);

						if (aroleOrganization != null
								&& aroleOrganization.getAroleOrganizationId() != null) {
							aroleOrganization.removeOnly();
						}
					}

				}

			}
		}
	}

}
