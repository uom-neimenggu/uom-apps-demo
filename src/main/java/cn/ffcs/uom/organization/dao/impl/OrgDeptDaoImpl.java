package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.OrgDeptDao;
import cn.ffcs.uom.organization.model.HanaOrgDept;

@Repository("orgDeptDao")
public class OrgDeptDaoImpl extends BaseDaoImpl implements OrgDeptDao {
	@Override
	public PageInfo queryPageInfoByOrgDept(HanaOrgDept orgDept, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from HANA_ORG_DEPT p where 1=1 ");
		
		if (orgDept != null) {
			if (!StrUtil.isNullOrEmpty(orgDept.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ?");
				params.add(orgDept.getMonthId());
			}
		}
		
		sb.append(" order by p.month_id desc ");

		return HanaOrgDept.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaOrgDept.class);

	}
}
