package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.WbPsnMsgDao;
import cn.ffcs.uom.organization.model.HanaWbCorp;
import cn.ffcs.uom.organization.model.HanaWbPsnMsg;

@Repository("wbPsnMsgDao")
public class WbPsnMsgDaoImpl extends BaseDaoImpl implements WbPsnMsgDao {
	@Override
	public PageInfo queryPageInfoByWbPsnMsg(HanaWbPsnMsg wbPsnMsg,
			int currentPage, int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();

		sb.append("select * from HANA_WB_PSN_MSG p where 1=1 ");

		if (wbPsnMsg != null) {
			if (!StrUtil.isNullOrEmpty(wbPsnMsg.getMonthId())) {
				sb.append(" AND substr(p.month_id,0,6) = ?");
				params.add(wbPsnMsg.getMonthId());
			}
		}

		sb.append(" order by p.month_id desc ");

		return HanaWbPsnMsg.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, HanaWbPsnMsg.class);

	}
}
