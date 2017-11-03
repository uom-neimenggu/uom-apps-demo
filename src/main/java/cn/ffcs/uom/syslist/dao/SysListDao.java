package cn.ffcs.uom.syslist.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.syslist.model.StaffSysRela;
import cn.ffcs.uom.syslist.model.SysList;
import cn.ffcs.uom.syslist.model.SysRoleRela;

public interface SysListDao extends BaseDao{
	/**
	 * 
	 *功能说明:获取系统列表
	 *创建人:俸安琪
	 *创建时间:2014-3-11 下午2:28:05
	 *@param staffRole
	 *@return List<StaffRole>
	 *
	 */
	public List<SysList> querySysLists(SysList sysList);

	/**
	 * 
	 *功能说明:获取员工系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param staffSysRela void
	 *
	 */
	public StaffSysRela queryStaffSysRela(StaffSysRela staffSysRela);

	/**
	 * 
	 *功能说明:获取员工系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffRole
	 *@return List<StaffRoleRela>
	 *
	 */
	public PageInfo queryStaffSysRela(StaffSysRela staffSysRela, int currentPage, int pageSize);
	
	/**
	 * 
	 *功能说明:获取角色系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffRole
	 *@return List<StaffRoleRela>
	 *
	 */
	public PageInfo querySysRoleRela(SysRoleRela sysRoleRela, int currentPage, int pageSize);
	
	/**
	 * 
	 *功能说明:获取角色系统关系
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param staffSysRela void
	 *
	 */
	public SysRoleRela querySysRoleRela(SysRoleRela sysRoleRela);
	
}
