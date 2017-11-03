package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdProfitcenterDao;
import cn.ffcs.uom.organization.model.MdProfitcenter;

@Repository("mdProfitcenterDao")
public class MdProfitcenterDaoImpl extends BaseDaoImpl implements MdProfitcenterDao {
	@Override
	public PageInfo queryPageInfoByMdProfitcenter(MdProfitcenter mdProfitcenter, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from ucms.md_profitcenter p where 1=1 ");
		
		if (mdProfitcenter != null) {
			if (!StrUtil.isNullOrEmpty(mdProfitcenter.getKtext())) {
				sb.append(" AND p.ktext like ?");
				params.add("%" + StringEscapeUtils.escapeSql(mdProfitcenter.getKtext()) + "%");
			}
		}

		return MdProfitcenter.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, MdProfitcenter.class);

	}
}
