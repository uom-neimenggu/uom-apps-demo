package cn.ffcs.uom.organization.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.MdmContractDao;
import cn.ffcs.uom.organization.manager.MdmContractManager;
import cn.ffcs.uom.organization.model.MdmContract;

@Service("mdmContractManager")
@Scope("prototype")
public class MdmContractManagerImpl implements MdmContractManager {
	@Resource
	private MdmContractDao mdmContractDao;

	@Override
	public PageInfo queryPageInfoByMdmContract(
			MdmContract mdmContract, int currentPage, int pageSize) {
		return mdmContractDao.queryPageInfoByMdmContract(
				mdmContract, currentPage, pageSize);
	}
}
