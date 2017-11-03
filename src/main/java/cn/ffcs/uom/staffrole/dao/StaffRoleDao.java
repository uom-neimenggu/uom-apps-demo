package cn.ffcs.uom.staffrole.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;

public interface StaffRoleDao extends BaseDao {
	/**
	 * 
	 * 功能说明:获取角色列表 创建人:俸安琪 创建时间:2014-3-11 下午2:28:05
	 * 
	 * @param staffRole
	 * @return List<StaffRole>
	 * 
	 */
	public List<StaffRole> queryStaffRoles(StaffRole staffRole);

	/**
	 * 
	 * 功能说明:查找用户的角色关系 创建人:俸安琪 创建时间:2014-3-11 下午5:21:52
	 * 
	 * @param srr
	 * @return List<StaffRoleRela>
	 * 
	 */
	public List<StaffRoleRela> queryStaffRoleRelas(StaffRoleRela srr);

	/**
	 * 
	 * 功能说明:获取员工 创建人:俸安琪 创建时间:2014-3-12 下午2:28:05
	 * 
	 * @param staffRole
	 * @return List<StaffRole>
	 * 
	 */
	public PageInfo queryStaffRoleRela(StaffRoleRela staffRoleRela,
			int currentPage, int pageSize);

	/**
	 * 
	 * 功能说明:获取员工角色关系 创建人:俸安琪 创建时间:2014-3-13 上午11:06:44
	 * 
	 * @param staffRole
	 *            void
	 * 
	 */
	public StaffRoleRela queryStaffRoleRela(StaffRoleRela staffRoleRela);

	public List<StaffRoleRela> queryStaffRoleList(StaffRoleRela staffRoleRela);

	   /**
     * 
     * 功能说明:获取员工角色列表 创建人:方尧 创建时间:2015-11-9
     * 
     * @param staff
     * @return List<StaffRole>
     * 
     */
    public List<StaffRole> queryStaffRoles(Staff staff);
}
