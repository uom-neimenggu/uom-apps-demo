package cn.ffcs.uom.staff.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.staff.dao.StaffExtendAttrDao;
import cn.ffcs.uom.staff.model.StaffExtendAttr;

@Repository("staffExtendAttrDao")
public class StaffExtendAttrDaoImpl extends BaseDaoImpl implements
		StaffExtendAttrDao {

	/**
	 * 获取扩展属性
	 * 
	 * @return
	 */
	@Override
	public StaffExtendAttr queryStaffExtendAttr(StaffExtendAttr staffExtendAttr) {

		if (staffExtendAttr != null) {
			StringBuffer sb = new StringBuffer(
					"SELECT * FROM STAFF_EXTEND_ATTR A WHERE A.STATUS_CD = ?");
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (staffExtendAttr.getStaffExtendAttrId() != null) {
				sb.append(" AND STAFF_EXTEND_ATTR_ID = ?");
				params.add(staffExtendAttr.getStaffExtendAttrId());
			}

			if (staffExtendAttr.getStaffId() != null) {
				sb.append(" AND STAFF_ID = ?");
				params.add(staffExtendAttr.getStaffId());
			}

			if (staffExtendAttr.getStaffAttrSpecId() != null) {
				sb.append(" AND STAFF_ATTR_SPEC_ID = ?");
				params.add(staffExtendAttr.getStaffAttrSpecId());
			}

			if (staffExtendAttr.getStaffAttrValue() != null) {
				sb.append(" AND STAFF_ATTR_VALUE = ?");
				params.add(StringEscapeUtils.escapeSql(staffExtendAttr.getStaffAttrValue()));
			}

			List<StaffExtendAttr> staffExtendAttrList = super.jdbcFindList(
					sb.toString(), params, StaffExtendAttr.class);

			if (staffExtendAttrList != null && staffExtendAttrList.size() > 0) {
				return staffExtendAttrList.get(0);
			}

		}

		return null;
	}

	/**
	 * 获取扩展属性
	 * 
	 * @return
	 */
	@Override
	public List<StaffExtendAttr> queryStaffExtendAttrList(
			StaffExtendAttr staffExtendAttr) {

		if (staffExtendAttr != null) {
			StringBuffer sb = new StringBuffer(
					"SELECT * FROM STAFF_EXTEND_ATTR A WHERE A.STATUS_CD = ?");
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (staffExtendAttr.getStaffExtendAttrId() != null) {
				sb.append(" AND STAFF_EXTEND_ATTR_ID = ?");
				params.add(staffExtendAttr.getStaffExtendAttrId());
			}

			if (staffExtendAttr.getStaffId() != null) {
				sb.append(" AND STAFF_ID = ?");
				params.add(staffExtendAttr.getStaffId());
			}

			if (staffExtendAttr.getStaffAttrSpecId() != null) {
				sb.append(" AND STAFF_ATTR_SPEC_ID = ?");
				params.add(staffExtendAttr.getStaffAttrSpecId());
			}

			if (staffExtendAttr.getStaffAttrValue() != null) {
				sb.append(" AND STAFF_ATTR_VALUE = ?");
				params.add(StringEscapeUtils.escapeSql(staffExtendAttr.getStaffAttrValue()));
			}

			return super.jdbcFindList(sb.toString(), params,
					StaffExtendAttr.class);
		}

		return null;
	}

}
