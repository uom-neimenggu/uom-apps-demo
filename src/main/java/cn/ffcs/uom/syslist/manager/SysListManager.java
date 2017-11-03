package cn.ffcs.uom.syslist.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.model.StaffSysRela;
import cn.ffcs.uom.syslist.model.SysList;
import cn.ffcs.uom.syslist.model.SysRoleRela;

public interface SysListManager {

	/**
	 * 
	 *功能说明:删除系统
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param sysList void
	 *
	 */
	public void removeSysList(SysList sysList);

	/**
	 * 
	 *功能说明:修改系统
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param sysList void
	 *
	 */
	public void updateSysList(SysList sysList);

	/**
	 * 
	 *功能说明:保存系统
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param sysList void
	 *
	 */
	public void saveSysList(SysList sysList);

	/**
	 * 
	 *功能说明:删除员工系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param staffSysRela void
	 *
	 */
	public void removeStaffSysRela(StaffSysRela staffSysRela);

	/**
	 * 
	 *功能说明:获取员工系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffSysRela
	 *@return List<StaffSysRela>
	 *
	 */
	public PageInfo queryStaffSysRela(StaffSysRela staffSysRela, int currentPage, int pageSize);

	/**
	 * 
	 *功能说明:保存员工系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param staffSysRela void
	 *
	 */
	public void saveStaffSysRela(StaffSysRela staffSysRela);
	
	/**
	 * 
	 *功能说明:保存员工系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param staffSysRela void
	 *
	 */
	public void saveStaffSysRelas(List<Staff> staffs, List<SysList> sysList);

	/**
	 * 
	 *功能说明:查找员工系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param staffSysRela void
	 *
	 */
	public StaffSysRela queryStaffSysRela(StaffSysRela staffSysRela);
	
	/**
	 * 
	 *功能说明:获取角色系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffSysRela
	 *@return List<StaffSysRela>
	 *
	 */
	public PageInfo querySysRoleRela(SysRoleRela sysRoleRela, int currentPage, int pageSize);

	/**
	 * 
	 *功能说明:删除角色系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param staffSysRela void
	 *
	 */
	public void removeSysRoleRela(SysRoleRela sysRoleRela);
	
	/**
	 * 
	 *功能说明:保存角色系统关系
	 *创建人:俸安琪
	 *创建时间:2014-4-23 下午4:32:23
	 *@param staffRole
	 *@param sysList void
	 *
	 */
	public void saveSysRoleRelas(List<StaffRole> staffRoles, List<SysList> sysLists);

	/**
	 * 
	 *功能说明:获取角色系统关系
	 *创建人:俸安琪
	 *创建时间:2014-4-23 下午4:32:23
	 *@param staffRole
	 *@param sysList void
	 *
	 */
	public SysRoleRela querySysRoleRela(SysRoleRela sysRoleRela);

	
}
