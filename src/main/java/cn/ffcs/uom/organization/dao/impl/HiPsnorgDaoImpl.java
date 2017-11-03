package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.HiPsnorgDao;
import cn.ffcs.uom.organization.model.HanaHiPsnorg;

@Repository("hiPsnorgDao")
public class HiPsnorgDaoImpl extends BaseDaoImpl implements HiPsnorgDao {
	@Override
	public PageInfo queryPageInfoByHiPsnorg(HanaHiPsnorg hiPsnorg, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from HANA_HI_PSNORG p where 1=1 ");
		
		if (hiPsnorg != null) {
			if (!StrUtil.isNullOrEmpty(hiPsnorg.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ?");
				params.add(hiPsnorg.getMonthId());
			}
		}
		
		sb.append(" order by p.month_id desc ");

		return HanaHiPsnorg.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaHiPsnorg.class);

	}
}
