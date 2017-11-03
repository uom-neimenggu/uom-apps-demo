package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacRoleExtAttrDao;
import cn.ffcs.uom.rolePermission.model.RbacRoleExtAttr;

@Repository("rbacRoleExtAttrDao")
@Transactional
public class RbacRoleExtAttrDaoImpl extends BaseDaoImpl implements
		RbacRoleExtAttrDao {

	@Override
	public PageInfo queryPageInfoRbacRoleExtAttr(
			RbacRoleExtAttr rbacRoleExtAttr, int currentPage, int pageSize) {

		if (null != rbacRoleExtAttr) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_EXT_ATTR WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleExtAttr.getRbacRoleExtAttrId() != null) {
				sb.append(" AND RBAC_ROLE_EXT_ATTR_ID = ?");
				params.add(rbacRoleExtAttr.getRbacRoleExtAttrId());
			}

			if (rbacRoleExtAttr.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleExtAttr.getRbacRoleId());
			}

			if (rbacRoleExtAttr.getRbacRoleAttrSpecId() != null) {
				sb.append(" AND RBAC_ROLE_ATTR_SPEC_ID = ?");
				params.add(rbacRoleExtAttr.getRbacRoleAttrSpecId());
			}

			if (!StrUtil.isEmpty(rbacRoleExtAttr.getRbacRoleAttrValue())) {
				sb.append(" AND RBAC_ROLE_ATTR_VALUE = ?");
				params.add(rbacRoleExtAttr.getRbacRoleAttrValue());
			}

			sb.append(" ORDER BY RBAC_ROLE_EXT_ATTR_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacRoleExtAttr.class);
		}

		return null;

	}

	@Override
	public List<RbacRoleExtAttr> queryRbacRoleExtAttrList(
			RbacRoleExtAttr rbacRoleExtAttr) {

		if (null != rbacRoleExtAttr) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_ROLE_EXT_ATTR WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacRoleExtAttr.getRbacRoleExtAttrId() != null) {
				sb.append(" AND RBAC_ROLE_EXT_ATTR_ID = ?");
				params.add(rbacRoleExtAttr.getRbacRoleExtAttrId());
			}

			if (rbacRoleExtAttr.getRbacRoleId() != null) {
				sb.append(" AND RBAC_ROLE_ID = ?");
				params.add(rbacRoleExtAttr.getRbacRoleId());
			}

			if (rbacRoleExtAttr.getRbacRoleAttrSpecId() != null) {
				sb.append(" AND RBAC_ROLE_ATTR_SPEC_ID = ?");
				params.add(rbacRoleExtAttr.getRbacRoleAttrSpecId());
			}

			if (!StrUtil.isEmpty(rbacRoleExtAttr.getRbacRoleAttrValue())) {
				sb.append(" AND RBAC_ROLE_ATTR_VALUE = ?");
				params.add(rbacRoleExtAttr.getRbacRoleAttrValue());
			}

			sb.append(" ORDER BY RBAC_ROLE_EXT_ATTR_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacRoleExtAttr.class);
		}

		return null;

	}

	@Override
	public RbacRoleExtAttr queryRbacRoleExtAttr(RbacRoleExtAttr rbacRoleExtAttr) {
		List<RbacRoleExtAttr> rbacRoleExtAttrList = this
				.queryRbacRoleExtAttrList(rbacRoleExtAttr);
		if (null != rbacRoleExtAttrList && rbacRoleExtAttrList.size() > 0) {
			return rbacRoleExtAttrList.get(0);
		}
		return null;
	}

}
