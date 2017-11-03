package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.model.OrgType;

public interface OrgTypeManager {

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
