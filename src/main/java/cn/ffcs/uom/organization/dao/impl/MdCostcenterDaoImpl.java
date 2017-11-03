package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdCostcenterDao;
import cn.ffcs.uom.organization.model.MdCostcenter;

@Repository("mdCostcenterDao")
public class MdCostcenterDaoImpl extends BaseDaoImpl implements MdCostcenterDao {
	@Override
	public PageInfo queryPageInfoByMdCostcenter(MdCostcenter mdCostcenter, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from ucms.md_costcenter p where 1=1 ");
		
		if (mdCostcenter != null) {
			if (!StrUtil.isNullOrEmpty(mdCostcenter.getKtext())) {
				sb.append(" AND p.ktext like ?");
				params.add("%" + StringEscapeUtils.escapeSql(mdCostcenter.getKtext()) + "%");
			}
		}

		return MdCostcenter.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, MdCostcenter.class);

	}
}
