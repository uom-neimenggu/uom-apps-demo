/**
 * 
 */
package cn.ffcs.uom.position.dao;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.position.model.Position;

/**
 * @author yahui
 * 
 */
public interface PositionDao extends BaseDao {

	public Position jdbcFindPosition(Position position);

	public String getSeqPositionCode();
	
	/**
	 * 移动员工组织关系时，删除原来的员工岗位关系
	 * @param staffOrganization
	 */
	public void removeStaffPostionByOrganization(StaffOrganization staffOrganization);
}
