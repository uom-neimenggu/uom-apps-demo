package cn.ffcs.uom.rolePermission.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.rolePermission.dao.RoleAttrValueDao;
import cn.ffcs.uom.rolePermission.model.RoleAttrValue;

@Repository("roleAttrValueDao")
public class RoleAttrValueDaoImpl extends BaseDaoImpl implements
		RoleAttrValueDao {

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<RoleAttrValue> queryRoleAttrValueList(
			RoleAttrValue roleAttrValue, String roleAttrValuePosition) {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ROLE_ATTR_VALUE WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (roleAttrValue != null) {

			if (!StrUtil.isNullOrEmpty(roleAttrValue.getRoleAttrValueId())) {
				sql.append(" AND ROLE_ATTR_VALUE_ID = ?");
				params.add(roleAttrValue.getRoleAttrValueId());
			}

			if (!StrUtil.isNullOrEmpty(roleAttrValue.getRoleAttrSpecId())) {
				sql.append(" AND ROLE_ATTR_SPEC_ID = ?");
				params.add(roleAttrValue.getRoleAttrSpecId());
			}

			if (!StrUtil.isNullOrEmpty(roleAttrValue.getRoleAttrValue())) {

				sql.append(" AND ROLE_ATTR_VALUE LIKE ? ");

				if (!StrUtil.isNullOrEmpty(roleAttrValuePosition)) {

					if (roleAttrValuePosition.equals("L")) {

						params.add("%" + roleAttrValue.getRoleAttrValue());

					} else if (roleAttrValuePosition.equals("R")) {

						params.add(roleAttrValue.getRoleAttrValue() + "%");

					} else {

						params.add("%" + roleAttrValue.getRoleAttrValue() + "%");

					}

				} else {
					params.add("%" + roleAttrValue.getRoleAttrValue() + "%");
				}
			}

			if (!StrUtil.isNullOrEmpty(roleAttrValue.getRoleAttrValueName())) {
				sql.append(" AND ROLE_ATTR_VALUE_NAME LIKE ?");
				params.add("%" + roleAttrValue.getRoleAttrValueName() + "%");
			}

		}

		sql.append(" ORDER BY ROLE_ATTR_VALUE_ID");

		return super.jdbcFindList(sql.toString(), params, RoleAttrValue.class);

	}

}
