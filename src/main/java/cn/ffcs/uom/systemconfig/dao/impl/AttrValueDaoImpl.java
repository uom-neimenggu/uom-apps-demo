package cn.ffcs.uom.systemconfig.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.dao.AttrValueDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Repository("attrValueDao")
public class AttrValueDaoImpl extends BaseDaoImpl implements AttrValueDao {

	@Override
	public List<AttrValue> queryAttrValueList(AttrValue attrValue,
			String attrValuePosition) {

		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ATTR_VALUE WHERE STATUS_CD = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (attrValue != null) {

			if (!StrUtil.isNullOrEmpty(attrValue.getAttrValueId())) {
				sql.append(" AND ATTR_VALUE_ID = ?");
				params.add(attrValue.getAttrValueId());
			}

			if (!StrUtil.isNullOrEmpty(attrValue.getAttrId())) {
				sql.append(" AND ATTR_ID = ?");
				params.add(attrValue.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(attrValue.getAttrValue())) {

				sql.append(" AND ATTR_VALUE LIKE ? ");

				if (!StrUtil.isNullOrEmpty(attrValuePosition)) {

					if (attrValuePosition.equals("L")) {

						params.add("%" + attrValue.getAttrValue());

					} else if (attrValuePosition.equals("R")) {

						params.add(attrValue.getAttrValue() + "%");

					} else {

						params.add("%" + attrValue.getAttrValue() + "%");

					}

				} else {
					params.add("%" + attrValue.getAttrValue() + "%");
				}
			}

			if (!StrUtil.isNullOrEmpty(attrValue.getAttrValueName())) {
				sql.append(" AND ATTR_VALUE_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(attrValue.getAttrValueName()) + "%");
			}

		}

		sql.append(" ORDER BY ATTR_VALUE_ID");

		return super.jdbcFindList(sql.toString(), params, AttrValue.class);

	}

}
