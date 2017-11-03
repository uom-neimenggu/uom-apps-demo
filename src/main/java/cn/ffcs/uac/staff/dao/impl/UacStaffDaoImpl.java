package cn.ffcs.uac.staff.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uac.staff.dao.UacStaffDao;
import cn.ffcs.uac.staff.model.UacStaff;
import cn.ffcs.uac.staff.vo.DemoStaff;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;

@Repository("uacStaffDao")
public class UacStaffDaoImpl extends BaseDaoImpl implements UacStaffDao {
	@Override
	public PageInfo queryUacStaffPage(UacStaff uacStaff, int currentPage,
			int pageSize) {
		if (uacStaff != null) {
			StringBuilder sql = new StringBuilder(
					"SELECT * FROM UAC_STAFF u WHERE 1=1 ");

			List<Object> params = new ArrayList<Object>();

			sql.append(" and u.STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(uacStaff.getEcode())) {
				sql.append(" AND u.ECODE = ?");
				params.add(StringEscapeUtils.escapeSql(uacStaff.getEcode()));
			}

			if (!StrUtil.isNullOrEmpty(uacStaff.getAccount())) {
				sql.append(" AND u.ACCOUNT = ?");
				params.add(StringEscapeUtils.escapeSql(uacStaff.getAccount()));
			}

			if (!StrUtil.isNullOrEmpty(uacStaff.getStaffName())) {
				sql.append(" and u.STAFF_NAME like ?");
				params.add("%"
						+ StringEscapeUtils.escapeSql(uacStaff.getStaffName())
						+ "%");
			}

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, UacStaff.class);
		}

		return null;
	}
	
	@Override
	public PageInfo queryDemoStaffPage(DemoStaff demoStaff, int currentPage,
			int pageSize) {
		if(demoStaff == null){
			StringBuilder sql = new StringBuilder(
					"SELECT * FROM DEMO_STAFF u WHERE 1=1 ");
			return super.jdbcFindPageInfo(sql.toString(), null, currentPage,
					pageSize, DemoStaff.class);
		}
		return null;
	}

	@Override
	public String getSeqEcode() {
		String sql = "SELECT SEQ_STAFF_CODE.NEXTVAL FROM DUAL";
		return String.valueOf(getJdbcTemplate().queryForInt(sql));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UacStaff> queryUacStaffList(UacStaff uacStaff) {
		if (uacStaff != null) {
			StringBuilder hql = new StringBuilder(
					"from UacStaff u where 1=1 ");

			List<Object> params = new ArrayList<Object>();

			hql.append(" and u.statusCd = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(uacStaff.getAccount())) {
				hql.append(" and u.account = ?");
				params.add(StringEscapeUtils.escapeSql(uacStaff.getAccount()));
			}

			return getHibernateTemplate().find(hql.toString(),params.toArray());
		}

		return null;
	}
	
	@Override
	public String getSeqStaffNbr() {
		String sql = "SELECT SEQ_STAFF_NBR.NEXTVAL FROM DUAL";
		return String.valueOf(getJdbcTemplate().queryForInt(sql));
	}
	
}
