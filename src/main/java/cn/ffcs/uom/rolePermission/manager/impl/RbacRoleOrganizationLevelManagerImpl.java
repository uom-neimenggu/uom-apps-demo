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
import cn.ffcs.uom.dataPermission.dao.AroleOrganizationLevelDao;
import cn.ffcs.uom.dataPermission.model.AroleOrganizationLevel;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.dao.RbacRoleOrganizationLevelDao;
import cn.ffcs.uom.rolePermission.manager.RbacRoleOrganizationLevelManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacRoleOrganizationLevel;

@Service("rbacRoleOrganizationLevelManager")
@Scope("prototype")
public class RbacRoleOrganizationLevelManagerImpl implements
		RbacRoleOrganizationLevelManager {

	@Resource
	private RbacRoleExtAttrDao rbacRoleExtAttrDao;

	@Resource
	private AroleOrganizationLevelDao aroleOrganizationLevelDao;

	@Resource
	private RbacRoleOrganizationLevelDao rbacRoleOrganizationLevelDao;

	@Override
	public PageInfo queryPageInfoRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel,
			int currentPage, int pageSize) {
		return rbacRoleOrganizationLevelDao
				.queryPageInfoRbacRoleOrganizationLevel(
						rbacRoleOrganizationLevel, currentPage, pageSize);
	}

	@Override
	public List<RbacRoleOrganizationLevel> queryRbacRoleOrganizationLevelList(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel) {
		return rbacRoleOrganizationLevelDao
				.queryRbacRoleOrganizationLevelList(rbacRoleOrganizationLevel);
	}

	@Override
	public RbacRoleOrganizationLevel queryRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel) {
		return rbacRoleOrganizationLevelDao
				.queryRbacRoleOrganizationLevel(rbacRoleOrganizationLevel);
	}

	@Override
	public void saveRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel) {
		rbacRoleOrganizationLevel.addOnly();
	}

	@Override
	public void updateRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel) {
		rbacRoleOrganizationLevel.updateOnly();
	}

	@Override
	public void removeRbacRoleOrganizationLevel(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel) {
		rbacRoleOrganizationLevel.removeOnly();
	}

	@Override
	public void addRbacRoleOrganizationLevelList(
			List<RbacRoleOrganizationLevel> rbacRoleOrganizationLevelList) {
		for (RbacRoleOrganizationLevel rbacRoleOrganizationLevel : rbacRoleOrganizationLevelList) {
			rbacRoleOrganizationLevel.addOnly();
		}
	}

	@Override
	public void updateRbacRoleOrganizationLevelList(
			List<RbacRoleOrganizationLevel> rbacRoleOrganizationLevelList) {
		for (RbacRoleOrganizationLevel rbacRoleOrganizationLevel : rbacRoleOrganizationLevelList) {
			rbacRoleOrganizationLevel.updateOnly();
		}
	}

	@Override
	public void updateRbacRoleOrganizationLevelToRaptornuke(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel)
			throws PortalException, SystemException {

		if (rbacRoleOrganizationLevel != null
				&& rbacRoleOrganizationLevel.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganizationLevel
					.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganizationLevel
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
						AroleOrganizationLevel aroleOrganizationLevel = new AroleOrganizationLevel();
						aroleOrganizationLevel.setAroleId(Long
								.parseLong(rbacRoleExtAttr
										.getRbacRoleAttrValue()));
						aroleOrganizationLevel
								.setOrgId(rbacRoleOrganizationLevel
										.getRbacOrgId());
						aroleOrganizationLevel
								.setRelaCd(rbacRoleOrganizationLevel
										.getRelaCd());
						aroleOrganizationLevel = aroleOrganizationLevelDao
								.queryAroleOrganizationLevel(aroleOrganizationLevel);

						if (aroleOrganizationLevel == null) {
							aroleOrganizationLevel = new AroleOrganizationLevel();
							aroleOrganizationLevel.setAroleId(Long
									.parseLong(rbacRoleExtAttr
											.getRbacRoleAttrValue()));
							aroleOrganizationLevel
									.setOrgId(rbacRoleOrganizationLevel
											.getRbacOrgId());
							aroleOrganizationLevel
									.setRelaCd(rbacRoleOrganizationLevel
											.getRelaCd());
							aroleOrganizationLevel
									.setLowerLevel(rbacRoleOrganizationLevel
											.getRbacLowerLevel());
							aroleOrganizationLevel
									.setHigherLevel(rbacRoleOrganizationLevel
											.getRbacHigherLevel());
							aroleOrganizationLevel.addOnly();
						} else {
							aroleOrganizationLevel
									.setLowerLevel(rbacRoleOrganizationLevel
											.getRbacLowerLevel());
							aroleOrganizationLevel
									.setHigherLevel(rbacRoleOrganizationLevel
											.getRbacHigherLevel());
							aroleOrganizationLevel.updateOnly();

						}
					}

				}

			}
		}
	}

	@Override
	public void removeRbacRoleOrganizationLevelToRaptornuke(
			RbacRoleOrganizationLevel rbacRoleOrganizationLevel)
			throws PortalException, SystemException {

		if (rbacRoleOrganizationLevel != null
				&& rbacRoleOrganizationLevel.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganizationLevel
					.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacRoleOrganizationLevel
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
						AroleOrganizationLevel aroleOrganizationLevel = new AroleOrganizationLevel();
						aroleOrganizationLevel.setAroleId(Long
								.parseLong(rbacRoleExtAttr
										.getRbacRoleAttrValue()));
						aroleOrganizationLevel
								.setOrgId(rbacRoleOrganizationLevel
										.getRbacOrgId());
						aroleOrganizationLevel
								.setRelaCd(rbacRoleOrganizationLevel
										.getRelaCd());
						aroleOrganizationLevel = aroleOrganizationLevelDao
								.queryAroleOrganizationLevel(aroleOrganizationLevel);

						if (aroleOrganizationLevel != null
								&& aroleOrganizationLevel.getAroleOrgLevelId() != null) {
							aroleOrganizationLevel.removeOnly();
						}
					}

				}

			}
		}
	}

}
