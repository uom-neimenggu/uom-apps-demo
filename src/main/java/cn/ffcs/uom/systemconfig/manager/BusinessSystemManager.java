package cn.ffcs.uom.systemconfig.manager;

import java.util.List;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.vo.PageInfo;

public interface BusinessSystemManager {
	/**
	 * 新增
	 * 
	 * @param businessSystem
	 * @return
	 */
	public void addBusinessSystem(BusinessSystem businessSystem);

	/**
	 * 更新
	 * 
	 * @param businessSystem
	 * @return
	 */
	public void updateBusinessSystem(BusinessSystem businessSystem);

	/**
	 * 获取业务系统列表
	 * 
	 * @param businessSystem
	 * @return
	 */
	public List<BusinessSystem> queryBusinessSystemList(
			BusinessSystem businessSystem);
	
	/**
	 * 获取业务系统列表
	 * 
	 * @return
	 */
	public List<BusinessSystem> queryBusinessSystemList();

	/**
	 * 获取业务系统
	 * 
	 * @param businessSystemId
	 * @return
	 */
	public BusinessSystem getBusinessSystemByBusinessSystemId(
			Long businessSystemId);

	/**
	 * 分页查询业务系统信息
	 * 
	 * @param queryBusinessSystem
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryBusinessSystemPageInfo(
			BusinessSystem queryBusinessSystem, int currentPage, int pageSize);

	/**
	 * 删除业务系统信息
	 * 
	 * @param businessSystem
	 */
	public void removeBusinessSystem(BusinessSystem businessSystem);
}
