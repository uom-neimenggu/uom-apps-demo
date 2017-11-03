package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdmContractDao;
import cn.ffcs.uom.organization.model.MdmContract;

@Repository("mdmContractDao")
public class MdmContractDaoImpl extends BaseDaoImpl implements MdmContractDao {
	@Override
	public PageInfo queryPageInfoByMdmContract(MdmContract mdmContract, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT P.CONTRACTID,P.CONTRACTCODE,P.SIGNCODE,"
				+ "DECODE(P.OPERATETYPE, 0, '新建', 1, '变更', 2, '解除', '其他') OPERATETYPE_NAME,"
				+ "DECODE(P.FRAMEFLAG, 1, '合同', 2, '框架', 3, '订单', '其他') FRAMEFLAG_NAME,"
				+ "DECODE(P.FUNDFLAG, 1, '支出', 2, '收入', 3, '无收支', '其他') FUNDFLAG_NAME,"
				+ "P.CONTRACTNAME,P.TRADESUM,P.CONCODE3CLASS,P.PARTANAME,P.APPLYDEPT,"
				+ "P.APPLYUSERID,P.APPLYUSERNAME,P.CREADATE "
				+ " FROM UCMS.MDM_CONTRACT P where 1=1 ");
		
		if (mdmContract != null) {
			if (!StrUtil.isNullOrEmpty(mdmContract.getContractname())) {
				sb.append(" AND p.contractname like ?");
				params.add("%" + StringEscapeUtils.escapeSql(mdmContract.getContractname()) + "%");
			}
		}

		return MdmContract.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, MdmContract.class);

	}
}
