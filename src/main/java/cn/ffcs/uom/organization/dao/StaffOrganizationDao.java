package cn.ffcs.uom.organization.dao;

import cn.ffcs.uom.common.dao.BaseDao;

public interface StaffOrganizationDao extends BaseDao {
	/**
	 * 获取OA账号序列
	 */
    public String getSeqOrgUserCode();
}
