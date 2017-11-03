package cn.ffcs.uom.roleauth.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.roleauth.model.RoleAuthorityRela;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.roleauth.model.SysAuthorityRela;
import cn.ffcs.uom.staffrole.model.StaffRole;
import cn.ffcs.uom.syslist.model.SysList;

public interface AuthorityManager {

	/**
	 * 
	 *功能说明:存储角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-28 下午6:03:34
	 *@param authority
	 *@param staffRole void
	 *
	 */
	public void saveRoleAuthorityRela(List<StaffAuthority> authoritys, List<StaffRole> staffRoles);
	
	/**
	 * 
	 *功能说明:保存权限
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param staffRole void
	 *
	 */
	public void saveAuthority(StaffAuthority authority);
	
	/**
	 * 
	 *功能说明:修改权限
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param staffRole void
	 *
	 */
	public void updateAuthority(StaffAuthority authority);
	
	/**
	 * 
	 *功能说明:删除权限
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param staffRole void
	 *
	 */
	public void removeAuthority(StaffAuthority authority);

	/**
	 * 
	 *功能说明:获取角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffRole
	 *@return List<StaffRole>
	 *
	 */
	public PageInfo queryRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela, int currentPage, int pageSize);
	
	/**
	 * 
	 *功能说明:删除角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param staffRoleRela void
	 *
	 */
	public void removeRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela);

	/**
	 * 
	 *功能说明:保存角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param staffRole void
	 *
	 */
	public void saveRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela);
	
	/**
	 * 
	 *功能说明:获取角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param staffRole void
	 *
	 */
	public RoleAuthorityRela queryRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela);
	
	/**
	 * 
	 *功能说明:存储系统权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-28 下午6:03:34
	 *@param authority
	 *@param sysList void
	 *
	 */
	public void saveSysAuthorityRela(List<StaffAuthority> authoritys, List<SysList> sysLists);

	/**
	 * 
	 *功能说明:获取系统权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param sysAuthorityRela void
	 *
	 */
	public SysAuthorityRela querySysAuthorityRela(SysAuthorityRela sysAuthorityRela);
	
	/**
	 * 
	 *功能说明:删除系统权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param sysAuthorityRela void
	 *
	 */
	public void removeSysAuthorityRela(SysAuthorityRela sysAuthorityRela);
	
	/**
	 * 
	 *功能说明:获取系统权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffRole
	 *@return List<StaffRole>
	 *
	 */
	public PageInfo querySysAuthorityRela(SysAuthorityRela sysAuthorityRela, int currentPage, int pageSize);
	
}
