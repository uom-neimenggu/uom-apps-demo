package cn.ffcs.uom.webservices.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.model.SystemIntfInfoConfig;

public interface SystemIntfInfoConfigManager {
	/**
	 * 根据系统编码或者系统配置
	 * 
	 * @param systemCode
	 * @return
	 */
	public SystemIntfInfoConfig querySystemIntfInfoConfigBySystemCode(
			String systemCode);

	/**
	 * 查询系统接口配置列表
	 * 
	 * @param querySystemIntfInfoConfig
	 * @return
	 */
	public List<SystemIntfInfoConfig> querySystemIntfInfoConfigList(
			SystemIntfInfoConfig querySystemIntfInfoConfig);

	/**
	 * 分页查询业务系统信息配置
	 * 
	 * @param systemOrgTreeConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo querySystemIntfInfoConfigPageInfo(
			SystemIntfInfoConfig systemIntfInfoConfig, int currentPage,
			int pageSize);

	public void addSystemIntfInfoConfig(
			SystemIntfInfoConfig systemIntfInfoConfig);

	public void removeSystemIntfInfoConfig(
			SystemIntfInfoConfig systemOrgTreeConfig);

	public void updateSystemIntfInfoConfig(
			SystemIntfInfoConfig systemIntfInfoConfig);
	
	/**
	 * 修改AttrValue 中接口状态值
	*@param objs 
	*@author wongs 
	*@date 2014-8-25 下午3:05:11 
	*@comment
	 */
	public void interfaceStatus(Object[] objs);
}
