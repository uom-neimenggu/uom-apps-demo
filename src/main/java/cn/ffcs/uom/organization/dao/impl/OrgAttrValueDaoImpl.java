package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.OrgAttrValueDao;
import cn.ffcs.uom.organization.model.OrgAttrValue;

@Repository("orgAttrValueDao")
public class OrgAttrValueDaoImpl extends BaseDaoImpl implements OrgAttrValueDao {

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@Override
	public List<OrgAttrValue> queryOrgAttrValueList(OrgAttrValue orgAttrValue,
			String orgAttrValuePosition) {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ORG_ATTR_VALUE WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (orgAttrValue != null) {

			if (!StrUtil.isNullOrEmpty(orgAttrValue.getOrgAttrSpecId())) {
				sql.append(" AND ORG_ATTR_SPEC_ID = ?");
				params.add(orgAttrValue.getOrgAttrSpecId());
			}

			if (!StrUtil.isNullOrEmpty(orgAttrValue.getOrgAttrValue())) {

				sql.append(" AND ORG_ATTR_VALUE LIKE ? ");

				if (!StrUtil.isNullOrEmpty(orgAttrValuePosition)) {

					if (orgAttrValuePosition.equals("L")) {

						params.add("%" + orgAttrValue.getOrgAttrValue());

					} else if (orgAttrValuePosition.equals("R")) {

						params.add(orgAttrValue.getOrgAttrValue() + "%");

					} else {

						params.add("%" + orgAttrValue.getOrgAttrValue() + "%");

					}

				} else {
					params.add("%" + orgAttrValue.getOrgAttrValue() + "%");
				}
			}

			if (!StrUtil.isNullOrEmpty(orgAttrValue.getOrgAttrValueName())) {
				sql.append(" AND ORG_ATTR_VALUE_NAME LIKE ?");
				params.add("%" + orgAttrValue.getOrgAttrValueName() + "%");
			}

		}

		sql.append(" ORDER BY ORG_ATTR_VALUE_ID");

		return super.jdbcFindList(sql.toString(), params, OrgAttrValue.class);

	}

}
