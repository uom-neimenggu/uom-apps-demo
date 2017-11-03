package cn.ffcs.uom.staffrole.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.staffrole.model.StaffRoleRela;

public interface StaffRoleManager {
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
	 * 功能说明:存储员工关系 创建人:俸安琪 创建时间:2014-3-11 下午5:17:12
	 * 
	 * @param staffRoles
	 * @param staff
	 *            void
	 * 
	 */
	public void saveStaffRoleRela(List<StaffRole> staffRoles, List<Staff> staffs);

	/**
	 * 
	 * 功能说明:保存员工角色 创建人:俸安琪 创建时间:2014-3-13 上午11:06:44
	 * 
	 * @param staffRole
	 *            void
	 * 
	 */
	public void saveStaffRole(StaffRole staffRole);

	/**
	 * 
	 * 功能说明:修改员工角色 创建人:俸安琪 创建时间:2014-3-13 上午11:06:44
	 * 
	 * @param staffRole
	 *            void
	 * 
	 */
	public void updateStaffRole(StaffRole staffRole);

	/**
	 * 
	 * 功能说明:删除员工角色 创建人:俸安琪 创建时间:2014-3-13 上午11:06:44
	 * 
	 * @param staffRole
	 *            void
	 * 
	 */
	public void removeStaffRole(StaffRole staffRole);

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
	 * 功能说明:删除员工角色关系 创建人:俸安琪 创建时间:2014-3-14 上午11:47:19
	 * 
	 * @param staffRoleRela
	 *            void
	 * 
	 */
	public void removeStaffRoleRela(StaffRoleRela staffRoleRela);
	public void removeStaffRoleRela(List<StaffRole> staffRoles, Staff staff);

	/**
	 * 
	 * 功能说明:保存员工角色关系 创建人:俸安琪 创建时间:2014-3-13 上午11:06:44
	 * 
	 * @param staffRole
	 *            void
	 * 
	 */
	public void saveStaffRoleRela(StaffRoleRela staffRoleRela);

	/**
	 * 
	 * 功能说明:获取员工角色关系 创建人:俸安琪 创建时间:2014-3-13 上午11:06:44
	 * 
	 * @param staffRole
	 *            void
	 * 
	 */
	public StaffRoleRela queryStaffRoleRela(StaffRoleRela staffRoleRela);

	public void saveStaffRoleRelaList(List<StaffRoleRela> staffRoleRelaList);

	public void updateBatchStaffRoleRela(StaffOrganization staffOrganization);
	
	public List<StaffRole> queryStaffaRoles(Staff staff);

	public void removeStaffRoleRelaList(List<StaffRoleRela> delStaffRoleRelaList);
}
