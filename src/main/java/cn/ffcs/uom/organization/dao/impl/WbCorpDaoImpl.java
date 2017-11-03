package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.WbCorpDao;
import cn.ffcs.uom.organization.model.HanaOrgDept;
import cn.ffcs.uom.organization.model.HanaWbCorp;

@Repository("wbCorpDao")
public class WbCorpDaoImpl extends BaseDaoImpl implements WbCorpDao {
	@Override
	public PageInfo queryPageInfoByWbCorp(HanaWbCorp wbCorp, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();

		sb.append("select * from HANA_WB_CORP p where 1=1 ");

		if (wbCorp != null) {
			if (!StrUtil.isNullOrEmpty(wbCorp.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ?");
				params.add(wbCorp.getMonthId());
			}
		}
		
		sb.append(" order by p.month_id desc ");
		
		return HanaWbCorp.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaWbCorp.class);

	}
}
