package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.rolePermission.dao.PermissionAttrValueDao;
import cn.ffcs.uom.rolePermission.model.PermissionAttrValue;

@Repository("permissionAttrValueDao")
public class PermissionAttrValueDaoImpl extends BaseDaoImpl implements
		PermissionAttrValueDao {

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PermissionAttrValue> queryPermissionAttrValueList(
			PermissionAttrValue permissionAttrValue,
			String permissionAttrValuePosition) {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM PERMISSION_ATTR_VALUE WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (permissionAttrValue != null) {

			if (!StrUtil.isNullOrEmpty(permissionAttrValue
					.getPermissionAttrValueId())) {
				sql.append(" AND PERMISSION_ATTR_VALUE_ID = ?");
				params.add(permissionAttrValue.getPermissionAttrValueId());
			}

			if (!StrUtil.isNullOrEmpty(permissionAttrValue
					.getPermissionAttrSpecId())) {
				sql.append(" AND PERMISSION_ATTR_SPEC_ID = ?");
				params.add(permissionAttrValue.getPermissionAttrSpecId());
			}

			if (!StrUtil.isNullOrEmpty(permissionAttrValue
					.getPermissionAttrValue())) {

				sql.append(" AND PERMISSION_ATTR_VALUE LIKE ? ");

				if (!StrUtil.isNullOrEmpty(permissionAttrValuePosition)) {

					if (permissionAttrValuePosition.equals("L")) {

						params.add("%"
								+ permissionAttrValue.getPermissionAttrValue());

					} else if (permissionAttrValuePosition.equals("R")) {

						params.add(permissionAttrValue.getPermissionAttrValue()
								+ "%");

					} else {

						params.add("%"
								+ permissionAttrValue.getPermissionAttrValue()
								+ "%");

					}

				} else {
					params.add("%"
							+ permissionAttrValue.getPermissionAttrValue()
							+ "%");
				}
			}

			if (!StrUtil.isNullOrEmpty(permissionAttrValue
					.getPermissionAttrValueName())) {
				sql.append(" AND PERMISSION_ATTR_VALUE_NAME LIKE ?");
				params.add("%"
						+ permissionAttrValue.getPermissionAttrValueName()
						+ "%");
			}

		}

		sql.append(" ORDER BY PERMISSION_ATTR_VALUE_ID");

		return super.jdbcFindList(sql.toString(), params,
				PermissionAttrValue.class);

	}

}
