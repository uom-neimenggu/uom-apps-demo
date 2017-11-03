package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.TelSyncCostcenterDao;
import cn.ffcs.uom.organization.model.HanaTelSyncCostcenter;

@Repository("telSyncCostcenterDao")
public class TelSyncCostcenterDaoImpl extends BaseDaoImpl implements TelSyncCostcenterDao {
	@Override
	public PageInfo queryPageInfoByTelSyncCostcenter(HanaTelSyncCostcenter telSyncCostcenter, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select * from HANA_TEL_SYNC_COSTCENTER p where 1=1 ");
		
		if (telSyncCostcenter != null) {
			if (!StrUtil.isNullOrEmpty(telSyncCostcenter.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ? ");
				params.add(telSyncCostcenter.getMonthId());
			}
		}
		
		sb.append(" order by p.month_id desc ");

		return HanaTelSyncCostcenter.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaTelSyncCostcenter.class);

	}
}
