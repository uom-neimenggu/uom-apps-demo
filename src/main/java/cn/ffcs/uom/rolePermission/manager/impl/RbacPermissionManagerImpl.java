package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.counter.service.CounterLocalServiceUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Permission;
import cn.ffcs.raptornuke.portal.service.PermissionLocalServiceUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionDao;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionExtAttrDao;
import cn.ffcs.uom.rolePermission.manager.RbacPermissionManager;
import cn.ffcs.uom.rolePermission.model.RbacPermission;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;

@Service("rbacPermissionManager")
@Scope("prototype")
public class RbacPermissionManagerImpl implements RbacPermissionManager {

	@Resource
	private RbacPermissionDao rbacPermissionDao;

	@Resource
	private RbacPermissionExtAttrDao rbacPermissionExtAttrDao;

	@Override
	public PageInfo queryPageInfoRbacPermission(RbacPermission rbacPermission,
			int currentPage, int pageSize) {
		return rbacPermissionDao.queryPageInfoRbacPermission(rbacPermission,
				currentPage, pageSize);
	}

	@Override
	public List<RbacPermission> queryRbacPermissionList(
			RbacPermission rbacPermission) {
		return rbacPermissionDao.queryRbacPermissionList(rbacPermission);
	}

	@Override
	public RbacPermission queryRbacPermission(RbacPermission rbacPermission) {
		return rbacPermissionDao.queryRbacPermission(rbacPermission);
	}

	@Override
	public void saveRbacPermission(RbacPermission rbacPermission) {
		rbacPermission.addOnly();
		List<RbacPermissionExtAttr> rbacPermissionExtAttrList = rbacPermission
				.getRbacPermissionExtAttrList();
		if (rbacPermissionExtAttrList != null
				&& rbacPermissionExtAttrList.size() > 0) {
			for (RbacPermissionExtAttr rbacPermissionExtAttr : rbacPermissionExtAttrList) {
				/**
				 * 有可能出现新增的属性
				 */
				if (rbacPermissionExtAttr.getRbacPermissionExtAttrId() == null) {
					rbacPermissionExtAttr.setRbacPermissionId(rbacPermission
							.getRbacPermissionId());
					rbacPermissionExtAttr.addOnly();
				} else {
					rbacPermissionExtAttr.updateOnly();
				}
			}
		}

	}

	@Override
	public void updateRbacPermission(RbacPermission rbacPermission) {
		rbacPermission.updateOnly();
		List<RbacPermissionExtAttr> rbacPermissionExtAttrList = rbacPermission
				.getRbacPermissionExtAttrList();
		if (rbacPermissionExtAttrList != null
				&& rbacPermissionExtAttrList.size() > 0) {
			for (RbacPermissionExtAttr rbacPermissionExtAttr : rbacPermissionExtAttrList) {
				/**
				 * 有可能出现新增的属性
				 */
				if (rbacPermissionExtAttr.getRbacPermissionExtAttrId() == null) {
					rbacPermissionExtAttr.setRbacPermissionId(rbacPermission
							.getRbacPermissionId());
					rbacPermissionExtAttr.addOnly();
				} else {
					rbacPermissionExtAttr.updateOnly();
				}
			}
		}

	}

	@Override
	public void removeRbacPermission(RbacPermission rbacPermission) {
		rbacPermission.removeOnly();
	}

	@Override
	public void updateRbacPermissionToRaptornuke(RbacPermission rbacPermission,
			RbacPermissionExtAttr rbacPermissionExtAttr)
			throws PortalException, SystemException {

		if (rbacPermission != null
				&& rbacPermission.getRbacPermissionId() != null) {

			String rbacPermissionAttrValue = rbacPermissionExtAttr
					.getRbacPermissionAttrValue();

			rbacPermissionExtAttr = new RbacPermissionExtAttr();

			rbacPermissionExtAttr.setRbacPermissionId(rbacPermission
					.getRbacPermissionId());
			rbacPermissionExtAttr
					.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1);

			rbacPermissionExtAttr = rbacPermissionExtAttrDao
					.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

			if (rbacPermissionExtAttr != null
					&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacPermissionExtAttr
									.getRbacPermissionAttrValue())) {

				rbacPermissionExtAttr = new RbacPermissionExtAttr();

				rbacPermissionExtAttr.setRbacPermissionId(rbacPermission
						.getRbacPermissionId());
				rbacPermissionExtAttr
						.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_2);

				rbacPermissionExtAttr = rbacPermissionExtAttrDao
						.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

				if (rbacPermissionExtAttr != null
						&& RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_2_ATTR_VALUE_2
								.equals(rbacPermissionExtAttr
										.getRbacPermissionAttrValue())) {

					rbacPermissionExtAttr = new RbacPermissionExtAttr();

					rbacPermissionExtAttr.setRbacPermissionId(rbacPermission
							.getRbacPermissionId());
					rbacPermissionExtAttr
							.setRbacPermissionAttrSpecId(RolePermissionConstants.PERMISSION_ATTR_SPEC_ID_3);

					rbacPermissionExtAttr = rbacPermissionExtAttrDao
							.queryRbacPermissionExtAttr(rbacPermissionExtAttr);

					if (rbacPermissionExtAttr != null) {

						Permission permission = null;

						if (StrUtil.isEmpty(rbacPermissionExtAttr
								.getRbacPermissionAttrValue())) {

							List<Permission> permissionList = PermissionLocalServiceUtil
									.getPermissions(
											RolePermissionConstants.RAPTORNUKE_COMPANY_ID_10063,
											new String[] { rbacPermission
													.getRbacPermissionCode() },
											Long.parseLong(rbacPermissionAttrValue));
							if (permissionList != null
									&& permissionList.size() > 0) {
								permission = permissionList.get(0);
							} else {
								permission = PermissionLocalServiceUtil
										.createPermission(CounterLocalServiceUtil
												.increment());
								permission
										.setCompanyId(RolePermissionConstants.RAPTORNUKE_COMPANY_ID_10063);
								permission.setActionId(rbacPermission
										.getRbacPermissionCode());
								permission.setResourceId(Long
										.parseLong(rbacPermissionAttrValue));
								PermissionLocalServiceUtil
										.addPermission(permission);
							}
							rbacPermissionExtAttr
									.setRbacPermissionAttrValue(permission
											.getPermissionId() + "");
							rbacPermissionExtAttr.updateOnly();
						} else {
							permission = PermissionLocalServiceUtil
									.getPermission(Long.parseLong(rbacPermissionExtAttr
											.getRbacPermissionAttrValue()));
							permission
									.setCompanyId(RolePermissionConstants.RAPTORNUKE_COMPANY_ID_10063);
							permission.setActionId(rbacPermission
									.getRbacPermissionCode());
							PermissionLocalServiceUtil
									.updatePermission(permission);
						}

					}
				}

			}
		}
	}
}
