package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionExtAttrDao;
import cn.ffcs.uom.rolePermission.model.RbacPermissionExtAttr;

@Repository("rbacPermissionExtAttrDao")
@Transactional
public class RbacPermissionExtAttrDaoImpl extends BaseDaoImpl implements
		RbacPermissionExtAttrDao {

	@Override
	public PageInfo queryPageInfoRbacPermissionExtAttr(
			RbacPermissionExtAttr rbacPermissionExtAttr, int currentPage,
			int pageSize) {

		if (null != rbacPermissionExtAttr) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION_EXT_ATTR WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermissionExtAttr.getRbacPermissionExtAttrId() != null) {
				sb.append(" AND RBAC_PERMISSION_EXT_ATTR_ID = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionExtAttrId());
			}

			if (rbacPermissionExtAttr.getRbacPermissionId() != null) {
				sb.append(" AND RBAC_PERMISSION_ID = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionId());
			}

			if (rbacPermissionExtAttr.getRbacPermissionAttrSpecId() != null) {
				sb.append(" AND RBAC_PERMISSION_ATTR_SPEC_ID = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionAttrSpecId());
			}

			if (!StrUtil.isEmpty(rbacPermissionExtAttr
					.getRbacPermissionAttrValue())) {
				sb.append(" AND RBAC_PERMISSION_ATTR_VALUE = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionAttrValue());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_EXT_ATTR_ID");

			return super.jdbcFindPageInfo(sb.toString(), params, currentPage,
					pageSize, RbacPermissionExtAttr.class);
		}

		return null;

	}

	@Override
	public List<RbacPermissionExtAttr> queryRbacPermissionExtAttrList(
			RbacPermissionExtAttr rbacPermissionExtAttr) {

		if (null != rbacPermissionExtAttr) {

			StringBuilder sb = new StringBuilder(
					"SELECT * FROM RBAC_PERMISSION_EXT_ATTR WHERE STATUS_CD = ?");

			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (rbacPermissionExtAttr.getRbacPermissionExtAttrId() != null) {
				sb.append(" AND RBAC_PERMISSION_EXT_ATTR_ID = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionExtAttrId());
			}

			if (rbacPermissionExtAttr.getRbacPermissionId() != null) {
				sb.append(" AND RBAC_PERMISSION_ID = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionId());
			}

			if (rbacPermissionExtAttr.getRbacPermissionAttrSpecId() != null) {
				sb.append(" AND RBAC_PERMISSION_ATTR_SPEC_ID = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionAttrSpecId());
			}

			if (!StrUtil.isEmpty(rbacPermissionExtAttr
					.getRbacPermissionAttrValue())) {
				sb.append(" AND RBAC_PERMISSION_ATTR_VALUE = ?");
				params.add(rbacPermissionExtAttr.getRbacPermissionAttrValue());
			}

			sb.append(" ORDER BY RBAC_PERMISSION_EXT_ATTR_ID");

			return super.jdbcFindList(sb.toString(), params,
					RbacPermissionExtAttr.class);
		}

		return null;

	}

	@Override
	public RbacPermissionExtAttr queryRbacPermissionExtAttr(
			RbacPermissionExtAttr rbacPermissionExtAttr) {
		List<RbacPermissionExtAttr> rbacPermissionExtAttrList = this
				.queryRbacPermissionExtAttrList(rbacPermissionExtAttr);
		if (null != rbacPermissionExtAttrList
				&& rbacPermissionExtAttrList.size() > 0) {
			return rbacPermissionExtAttrList.get(0);
		}
		return null;
	}

}
