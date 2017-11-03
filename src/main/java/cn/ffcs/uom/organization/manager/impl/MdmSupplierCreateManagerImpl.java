package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdmSupplierCreateDao;
import cn.ffcs.uom.organization.manager.MdmSupplierCreateManager;
import cn.ffcs.uom.organization.model.MdmSupplierCreate;

@Service("mdmSupplierCreateManager")
@Scope("prototype")
public class MdmSupplierCreateManagerImpl implements MdmSupplierCreateManager {
	@Resource
	private MdmSupplierCreateDao mdmSupplierCreateDao;

	@Override
	public PageInfo queryPageInfoByMdmSupplierCreate(
			MdmSupplierCreate mdmSupplierCreate, int currentPage, int pageSize) {
		return mdmSupplierCreateDao.queryPageInfoByMdmSupplierCreate(
				mdmSupplierCreate, currentPage, pageSize);
	}
}
