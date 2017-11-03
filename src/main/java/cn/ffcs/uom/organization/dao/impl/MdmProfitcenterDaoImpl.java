package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdmProfitcenterDao;
import cn.ffcs.uom.organization.model.MdmProfitcenter;

@Repository("mdmProfitcenterDao")
public class MdmProfitcenterDaoImpl extends BaseDaoImpl implements MdmProfitcenterDao {
	@Override
	public PageInfo queryPageInfoByMdmProfitcenter(MdmProfitcenter mdmProfitcenter, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from ucms.md_profitcenter p where 1=1 ");
		
		if (mdmProfitcenter != null) {
			if (!StrUtil.isNullOrEmpty(mdmProfitcenter.getKtext())) {
				sb.append(" AND p.ktext like ?");
				params.add("%" + StringEscapeUtils.escapeSql(mdmProfitcenter.getKtext()) + "%");
			}
		}

		return MdmProfitcenter.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, MdmProfitcenter.class);

	}
}
