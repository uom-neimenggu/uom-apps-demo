package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdmSupplierCreateDao;
import cn.ffcs.uom.organization.model.MdmSupplierCreate;

@Repository("mdmSupplierCreateDao")
public class MdmSupplierCreateDaoImpl extends BaseDaoImpl implements MdmSupplierCreateDao {
	@Override
	public PageInfo queryPageInfoByMdmSupplierCreate(MdmSupplierCreate mdmSupplierCreate, int currentPage,
			int pageSize) {

		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		
		sb.append("select decode(p.status,'C','创建','M','修改','其他') status,"
				+ "p.zsqd_id,p.lifnr,p.ktokk,p.name1,p.city1,p.telf1,"
				+ "p.zprop,p.zcont,p.zdxhzywlxfw from ucms.mdm_supplier_create p where 1=1 ");
		
		if (mdmSupplierCreate != null) {
			if (!StrUtil.isNullOrEmpty(mdmSupplierCreate.getName1())) {
				sb.append(" AND p.name1 like ?");
				params.add("%" + StringEscapeUtils.escapeSql(mdmSupplierCreate.getName1()) + "%");
			}
		}

		return MdmSupplierCreate.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, MdmSupplierCreate.class);

	}
}
