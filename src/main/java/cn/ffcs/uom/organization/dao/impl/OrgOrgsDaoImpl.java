package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.OrgOrgsDao;
import cn.ffcs.uom.organization.model.HanaOrgOrgs;

@Repository("orgOrgsDao")
public class OrgOrgsDaoImpl extends BaseDaoImpl implements OrgOrgsDao {
	@Override
	public PageInfo queryPageInfoByOrgOrgs(HanaOrgOrgs orgOrgs,
			int currentPage, int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();

		sb.append("select * from HANA_ORG_ORGS p where 1=1 ");

		if (orgOrgs != null) {
			if (!StrUtil.isNullOrEmpty(orgOrgs.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ?");
				params.add(orgOrgs.getMonthId());
			}
		}

		sb.append(" order by p.month_id desc ");

		return HanaOrgOrgs.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaOrgOrgs.class);

	}
}
