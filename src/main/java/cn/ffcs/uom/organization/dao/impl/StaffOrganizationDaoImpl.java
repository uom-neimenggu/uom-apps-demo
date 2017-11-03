package cn.ffcs.uom.organization.dao.impl;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.organization.dao.StaffOrganizationDao;

@Repository("staffOrganizationDao")
public class StaffOrganizationDaoImpl extends BaseDaoImpl implements StaffOrganizationDao {
	/**
	 * 获取OA账号序列
	 */
    public String getSeqOrgUserCode(){
        String sql = "SELECT SEQ_ORG_USER_CODE.NEXTVAL FROM DUAL";
        return String.valueOf(getJdbcTemplate().queryForInt(sql));    	
    }
}
