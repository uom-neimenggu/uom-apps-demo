package cn.ffcs.uom.staff.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.staff.dao.StaffAccountDao;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

@Repository("staffAccountDao")
public class StaffAccountDaoImpl extends BaseDaoImpl implements StaffAccountDao {

	@Override
	public StaffAccount queryStaffAccountByStaffUuid(Staff staff) {

		if (staff.getUuid() == null) {
			return null;
		}

		StaffAccount staffAccount = new StaffAccount();
		// select * from staff_account t where t.staff_id = ;
		StringBuffer sql = new StringBuffer(
				"select * from staff_account t1, staff t2 where t1.status_cd = ?  and t2.status_cd = ? ");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		// 根据UUID查询数据
		sql.append(" and t2.uuid = ?");
		params.add(StringEscapeUtils.escapeSql(staff.getUuid()));
		// 有可能查出多个账号，
		List<StaffAccount> list = (List<StaffAccount>) super.getJdbcTemplate()
				.query(sql.toString(),
						params.toArray(),
						new BeanPropertyRowMapper<StaffAccount>(
								StaffAccount.class));

		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	
}
