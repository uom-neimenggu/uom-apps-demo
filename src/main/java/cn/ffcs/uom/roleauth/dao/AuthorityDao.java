package cn.ffcs.uom.roleauth.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.roleauth.model.StaffAuthority;
import cn.ffcs.uom.roleauth.model.RoleAuthorityRela;
import cn.ffcs.uom.roleauth.model.SysAuthorityRela;

public interface AuthorityDao extends BaseDao{
	/**
	 * 
	 *功能说明:查找权限列表
	 *创建人:俸安琪
	 *创建时间:2014-3-28 上午9:28:29
	 *@param authority
	 *@return List<Authority>
	 *
	 */
	public List<StaffAuthority> queryAuthority(StaffAuthority authority);

	/**
	 * 
	 *功能说明:获取角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param RoleAuthorityRela void
	 *
	 */
	public RoleAuthorityRela queryRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela);
	
	/**
	 * 
	 *功能说明:获取系统权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param RoleAuthorityRela void
	 *
	 */
	public SysAuthorityRela querySysAuthorityRela(SysAuthorityRela sysAuthorityRela);

	/**
	 * 
	 *功能说明:获取角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffRole
	 *@return List<roleAuthorityRela>
	 *
	 */
	public PageInfo queryRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela, int currentPage, int pageSize);

	/**
	 * 
	 *功能说明:获取角色权限关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param staffRole
	 *@return List<roleAuthorityRela>
	 *
	 */
	public PageInfo querySysAuthorityRela(SysAuthorityRela sysAuthorityRela, int currentPage, int pageSize);

}
