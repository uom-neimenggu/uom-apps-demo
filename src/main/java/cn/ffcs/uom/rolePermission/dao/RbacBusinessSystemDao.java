package cn.ffcs.uom.rolePermission.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.rolePermission.model.RbacBusinessSystem;

public interface RbacBusinessSystemDao extends BaseDao {

	/**
	 * 分页取类信息
	 * 
	 * @param rbacBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem, int currentPage, int pageSize);

	public List<RbacBusinessSystem> queryRbacBusinessSystemList(
			RbacBusinessSystem rbacBusinessSystem);

	public RbacBusinessSystem queryRbacBusinessSystem(
			RbacBusinessSystem rbacBusinessSystem);

}
