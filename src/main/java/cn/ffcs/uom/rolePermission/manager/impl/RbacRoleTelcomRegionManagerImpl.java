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
import cn.ffcs.uom.dataPermission.dao.AroleTelcomRegionDao;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.dao.RbacRoleTelcomRegionDao;
import cn.ffcs.uom.rolePermission.manager.RbacRoleTelcomRegionManager;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;
import cn.ffcs.uom.rolePermission.model.RbacRoleTelcomRegion;

@Service("rbacRoleTelcomRegionManager")
@Scope("prototype")
public class RbacRoleTelcomRegionManagerImpl implements
		RbacRoleTelcomRegionManager {

	@Resource
	private RbacRoleExtAttrDao rbacRoleExtAttrDao;

	@Resource
	private AroleTelcomRegionDao aroleTelcomRegionDao;

	@Resource
	private RbacRoleTelcomRegionDao rbacRoleTelcomRegionDao;

	@Override
	public PageInfo queryPageInfoRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion, int currentPage,
			int pageSize) {
		return rbacRoleTelcomRegionDao.queryPageInfoRbacRoleTelcomRegion(
				rbacRoleTelcomRegion, currentPage, pageSize);
	}

	@Override
	public List<RbacRoleTelcomRegion> queryRbacRoleTelcomRegionList(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) {
		return rbacRoleTelcomRegionDao
				.queryRbacRoleTelcomRegionList(rbacRoleTelcomRegion);
	}

	@Override
	public RbacRoleTelcomRegion queryRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) {
		return rbacRoleTelcomRegionDao
				.queryRbacRoleTelcomRegion(rbacRoleTelcomRegion);
	}

	@Override
	public void saveRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) {
		rbacRoleTelcomRegion.addOnly();
	}

	@Override
	public void updateRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) {
		rbacRoleTelcomRegion.updateOnly();
	}

	@Override
	public void removeRbacRoleTelcomRegion(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) {
		rbacRoleTelcomRegion.removeOnly();
	}

	@Override
	public void addRbacRoleTelcomRegionList(
			List<RbacRoleTelcomRegion> rbacRoleTelcomRegionList) {
		for (RbacRoleTelcomRegion rbacRoleTelcomRegion : rbacRoleTelcomRegionList) {
			rbacRoleTelcomRegion.addOnly();
		}
	}

	@Override
	public void updateRbacRoleTelcomRegionList(
			List<RbacRoleTelcomRegion> rbacRoleTelcomRegionList) {
		for (RbacRoleTelcomRegion rbacRoleTelcomRegion : rbacRoleTelcomRegionList) {
			rbacRoleTelcomRegion.updateOnly();
		}
	}

	@Override
	public void updateRbacRoleTelcomRegionToRaptornuke(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) throws PortalException,
			SystemException {

		if (rbacRoleTelcomRegion != null
				&& rbacRoleTelcomRegion.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacRoleTelcomRegion.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacRoleTelcomRegion
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
						AroleTelcomRegion aroleTelcomRegion = new AroleTelcomRegion();
						aroleTelcomRegion.setAroleId(Long
								.parseLong(rbacRoleExtAttr
										.getRbacRoleAttrValue()));
						aroleTelcomRegion
								.setTelcomRegionId(rbacRoleTelcomRegion
										.getRbacTelcomRegionId());
						aroleTelcomRegion = aroleTelcomRegionDao
								.queryAroleTelcomRegion(aroleTelcomRegion);

						if (aroleTelcomRegion == null) {
							aroleTelcomRegion = new AroleTelcomRegion();
							aroleTelcomRegion.setAroleId(Long
									.parseLong(rbacRoleExtAttr
											.getRbacRoleAttrValue()));
							aroleTelcomRegion
									.setTelcomRegionId(rbacRoleTelcomRegion
											.getRbacTelcomRegionId());
							aroleTelcomRegion
									.setFlag(RolePermissionConstants.AROLE_TELCOM_REGION_FLAY_1);
							aroleTelcomRegion.addOnly();
						} else {
							aroleTelcomRegion.updateOnly();
						}
					}

				}

			}
		}
	}

	@Override
	public void removeRbacRoleTelcomRegionToRaptornuke(
			RbacRoleTelcomRegion rbacRoleTelcomRegion) throws PortalException,
			SystemException {

		if (rbacRoleTelcomRegion != null
				&& rbacRoleTelcomRegion.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacRoleTelcomRegion.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacRoleTelcomRegion
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
						AroleTelcomRegion aroleTelcomRegion = new AroleTelcomRegion();
						aroleTelcomRegion.setAroleId(Long
								.parseLong(rbacRoleExtAttr
										.getRbacRoleAttrValue()));
						aroleTelcomRegion
								.setTelcomRegionId(rbacRoleTelcomRegion
										.getRbacTelcomRegionId());
						aroleTelcomRegion = aroleTelcomRegionDao
								.queryAroleTelcomRegion(aroleTelcomRegion);

						if (aroleTelcomRegion != null
								&& aroleTelcomRegion.getAroleTelcomRegionId() != null) {
							aroleTelcomRegion.removeOnly();
						}
					}

				}

			}
		}
	}

}
