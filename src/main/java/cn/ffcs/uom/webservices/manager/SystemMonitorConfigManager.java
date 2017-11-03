package cn.ffcs.uom.webservices.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.model.SystemMonitorConfig;

public interface SystemMonitorConfigManager {
	/**
	 * 查询
	 * 
	 * @param systemMonitorConfig
	 * @return
	 */
	public SystemMonitorConfig querySystemMonitorConfig(SystemMonitorConfig systemMonitorConfig);

	/**
	 * 查询监控配置列表
	 * 
	 * @param querySystemMonitorConfig
	 * @return
	 */
	public List<SystemMonitorConfig> querySystemMonitorConfigList(SystemMonitorConfig querySystemMonitorConfig);

	/**
	 * 分页查询监控配置
	 * 
	 * @param systemMonitorConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo querySystemMonitorConfigPageInfo(SystemMonitorConfig systemMonitorConfig, int currentPage, int pageSize);

	public void addSystemMonitorConfig(SystemMonitorConfig systemMonitorConfig);

	public void removeSystemMonitorConfig(SystemMonitorConfig systemOrgTreeConfig);

	public void updateSystemMonitorConfig(SystemMonitorConfig systemMonitorConfig);
	
	
	public boolean checkDataIsExist(String systemCode,String eventName);
}
