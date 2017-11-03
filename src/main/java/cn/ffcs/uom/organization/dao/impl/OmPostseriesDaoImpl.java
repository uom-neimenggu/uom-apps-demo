package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.OmPostseriesDao;
import cn.ffcs.uom.organization.model.HanaOmPostseries;

@Repository("omPostseriesDao")
public class OmPostseriesDaoImpl extends BaseDaoImpl implements OmPostseriesDao {
	@Override
	public PageInfo queryPageInfoByOmPostseries(HanaOmPostseries omPostseries, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();

		sb.append("select * from Hana_Om_Postseries p where 1=1 ");

		if (omPostseries != null) {
			if (!StrUtil.isNullOrEmpty(omPostseries.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ?");
				params.add(omPostseries.getMonthId());
			}
		}
		
		sb.append(" order by p.month_id desc ");
		
		return HanaOmPostseries.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaOmPostseries.class);

	}
}
