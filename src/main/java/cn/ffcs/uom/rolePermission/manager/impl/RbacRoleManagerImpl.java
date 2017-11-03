package cn.ffcs.uom.rolePermission.manager.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;

import cn.ffcs.raptornuke.counter.service.CounterLocalServiceUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.service.RoleLocalServiceUtil;
import cn.ffcs.raptornuke.portal.util.PortalUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.constants.RolePermissionConstants;
import cn.ffcs.uom.rolePermission.dao.RbacRoleDao;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.manager.RbacRoleManager;
import cn.ffcs.uom.rolePermission.model.RbacRole;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;

@Service("rbacRoleManager")
@Scope("prototype")
public class RbacRoleManagerImpl implements RbacRoleManager {

	@Resource
	private RbacRoleDao rbacRoleDao;

	@Resource
	private RbacRoleExtAttrDao rbacRoleExtAttrDao;

	@Override
	public PageInfo queryPageInfoRbacRole(RbacRole rbacRole, int currentPage,
			int pageSize) {
		return rbacRoleDao.queryPageInfoRbacRole(rbacRole, currentPage,
				pageSize);
	}

	@Override
	public List<RbacRole> queryRbacRoleList(RbacRole rbacRole) {
		return rbacRoleDao.queryRbacRoleList(rbacRole);
	}

	@Override
	public RbacRole queryRbacRole(RbacRole rbacRole) {
		return rbacRoleDao.queryRbacRole(rbacRole);
	}

	@Override
	public void saveRbacRole(RbacRole rbacRole) {
		rbacRole.addOnly();
		List<RbacRoleExtAttr> rbacRoleExtAttrList = rbacRole
				.getRbacRoleExtAttrList();
		if (rbacRoleExtAttrList != null && rbacRoleExtAttrList.size() > 0) {
			for (RbacRoleExtAttr rbacRoleExtAttr : rbacRoleExtAttrList) {
				/**
				 * 有可能出现新增的属性
				 */
				if (rbacRoleExtAttr.getRbacRoleExtAttrId() == null) {
					rbacRoleExtAttr.setRbacRoleId(rbacRole.getRbacRoleId());
					rbacRoleExtAttr.addOnly();
				} else {
					rbacRoleExtAttr.updateOnly();
				}
			}
		}

	}

	@Override
	public void updateRbacRole(RbacRole rbacRole) {
		rbacRole.updateOnly();
		List<RbacRoleExtAttr> rbacRoleExtAttrList = rbacRole
				.getRbacRoleExtAttrList();
		if (rbacRoleExtAttrList != null && rbacRoleExtAttrList.size() > 0) {
			for (RbacRoleExtAttr rbacRoleExtAttr : rbacRoleExtAttrList) {
				/**
				 * 有可能出现新增的属性
				 */
				if (rbacRoleExtAttr.getRbacRoleExtAttrId() == null) {
					rbacRoleExtAttr.setRbacRoleId(rbacRole.getRbacRoleId());
					rbacRoleExtAttr.addOnly();
				} else {
					rbacRoleExtAttr.updateOnly();
				}
			}
		}
	}

	@Override
	public void removeRbacRole(RbacRole rbacRole) {
		rbacRole.removeOnly();
	}

	@Override
	public void updateRbacRoleToRaptornuke(RbacRole rbacRole)
			throws PortalException, SystemException {

		if (rbacRole != null && rbacRole.getRbacRoleId() != null) {

			RbacRoleExtAttr rbacRoleExtAttr = new RbacRoleExtAttr();

			rbacRoleExtAttr.setRbacRoleId(rbacRole.getRbacRoleId());
			rbacRoleExtAttr
					.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_1);

			rbacRoleExtAttr = rbacRoleExtAttrDao
					.queryRbacRoleExtAttr(rbacRoleExtAttr);

			if (rbacRoleExtAttr != null
					&& RolePermissionConstants.ROLE_ATTR_SPEC_ID_1_ATTR_VALUE_1
							.equals(rbacRoleExtAttr.getRbacRoleAttrValue())) {

				rbacRoleExtAttr = new RbacRoleExtAttr();

				rbacRoleExtAttr.setRbacRoleId(rbacRole.getRbacRoleId());
				rbacRoleExtAttr
						.setRbacRoleAttrSpecId(RolePermissionConstants.ROLE_ATTR_SPEC_ID_2);

				rbacRoleExtAttr = rbacRoleExtAttrDao
						.queryRbacRoleExtAttr(rbacRoleExtAttr);

				if (rbacRoleExtAttr != null) {

					Role role = null;
					HttpServletRequest request = (HttpServletRequest) Executions
							.getCurrent().getNativeRequest();

					if (StrUtil.isEmpty(rbacRoleExtAttr.getRbacRoleAttrValue())) {
						role = RoleLocalServiceUtil
								.createRole(CounterLocalServiceUtil.increment());
						role.setCompanyId(RolePermissionConstants.RAPTORNUKE_COMPANY_ID_10063);
						role.setClassNameId(RolePermissionConstants.RAPTORNUKE_CLASSNAME_ID_10045);
						role.setClassPK(role.getRoleId());
						role.setName(rbacRole.getRbacRoleName());
						role.setTitle(rbacRole.getRbacRoleName(),
								PortalUtil.getLocale(request));
						role.setDescription("", PortalUtil.getLocale(request));
						role.setType(RolePermissionConstants.RAPTORNUKE_TYPE_1);
						RoleLocalServiceUtil.addRole(role);
						rbacRoleExtAttr.setRbacRoleAttrValue(role.getRoleId()
								+ "");
						rbacRoleExtAttr.updateOnly();
					} else {
						role = RoleLocalServiceUtil.getRole(Long
								.parseLong(rbacRoleExtAttr
										.getRbacRoleAttrValue()));
						role.setName(rbacRole.getRbacRoleName());
						role.setTitle(rbacRole.getRbacRoleName(),
								PortalUtil.getLocale(request));
						role.setDescription("", PortalUtil.getLocale(request));
						RoleLocalServiceUtil.updateRole(role);
					}

				}

			}
		}
	}
}
