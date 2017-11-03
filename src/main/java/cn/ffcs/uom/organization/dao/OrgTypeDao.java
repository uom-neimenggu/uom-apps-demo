package cn.ffcs.uom.organization.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.model.OrgType;

public interface OrgTypeDao extends BaseDao {

	/**
	 * 
	 * ����˵��:��������͹����ȡ��֯���� ������:ٺ���� ����ʱ��:2014-6-16 ����9:42:26
	 * 
	 * @param totr
	 * @return List<OrgType>
	 * 
	 */
	public List<OrgType> getOrgTypeList(TreeOrgTypeRule totr);

	public List<OrgType> queryOrgTypeList(OrgType orgType);

}
