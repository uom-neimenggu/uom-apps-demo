package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.TelDeptCostConDao;
import cn.ffcs.uom.organization.model.HanaTelDeptCostCon;

@Repository("TelDeptCostConDao")
public class TelDeptCostConDaoImpl extends BaseDaoImpl implements TelDeptCostConDao {
	@Override
	public PageInfo queryPageInfoByTelDeptCostCon(HanaTelDeptCostCon telDeptCostCon, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from Hana_Tel_Dept_Cost_Con p where 1=1 ");
		
		if (telDeptCostCon != null) {
			if (!StrUtil.isNullOrEmpty(telDeptCostCon.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ? ");
				params.add(telDeptCostCon.getMonthId());
			}
		}
		
		sb.append(" order by p.month_id desc ");

		return HanaTelDeptCostCon.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaTelDeptCostCon.class);

	}
}
