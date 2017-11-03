package cn.ffcs.uom.staff.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.staff.dao.StaffAttrSpecSortDao;
import cn.ffcs.uom.staff.model.StaffAttrSpecSort;

@Repository("staffAttrSpecSortDao")
@SuppressWarnings({"unchecked","rawtypes"})
public class StaffAttrSpecSortDaoImpl extends BaseDaoImpl implements
		StaffAttrSpecSortDao {

	
    @Override
	public List<StaffAttrSpecSort> queryStaffAttrSpecSortList() {
		String sql = "SELECT * FROM STAFF_ATTR_SPEC_SORT WHERE STATUS_CD =?";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		return StaffAttrSpecSort.repository().jdbcFindList(sql, params,
				StaffAttrSpecSort.class);
	}

}
