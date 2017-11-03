package cn.ffcs.uom.systemconfig.manager;

import java.util.List;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.model.SystemBusiUser;

public interface SysBusiUserManager {
	/**
	 * 新增
	 * 
	 * @param sysBusiUser
	 * @return
	 */
	public void addSysBusiUser(SystemBusiUser sysBusiUser);
	
	/**
	 * 查询
	 * @param businessSystemId
	 * @param systemConfigUserId
	 * @return
	 */
	public SystemBusiUser querySysBusiUserByUserSys(long businessSystemId,long systemConfigUserId);

	/**
	 * 分页查询人员系统关系信息
	 * 
	 * @param queryBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryBusinessSystemPageInfo(
			SystemBusiUser sysBusiUser, int currentPage, int pageSize);

	/**
	 * 删除业务系统信息
	 * 
	 * @param businessSystem
	 */
	public void removeBusinessSystem(BusinessSystem businessSystem,SystemBusiUser querySysBusiUser);
	
	/**
	 * 查询当前用户没有配置用户系统关系的系统节点列表
	 * @param systemConfigUserId
	 * @return
	 */
	public List<NodeVo> queryBusinessSystemListByUserId(long systemConfigUserId);
}
