package cn.ffcs.uom.webservices.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.model.SystemMonitorConfigFilter;

public interface SystemMonitorConfigFilterManager {
	/**
	 * 查询
	 * 
	 * @param systemMonitorConfig
	 * @return
	 */
	public SystemMonitorConfigFilter querySystemMonitorConfigFilter(SystemMonitorConfigFilter systemMonitorConfig);

	/**
	 * 查询监控配置列表
	 * 
	 * @param querySystemMonitorConfigFilter
	 * @return
	 */
	public List<SystemMonitorConfigFilter> querySystemMonitorConfigFilterList(SystemMonitorConfigFilter querySystemMonitorConfigFilter);

	/**
	 * 分页查询监控配置
	 * 
	 * @param systemMonitorConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo querySystemMonitorConfigFilterPageInfo(SystemMonitorConfigFilter systemMonitorConfig, int currentPage, int pageSize);

	public void addSystemMonitorConfigFilter(SystemMonitorConfigFilter systemMonitorConfig);

	public void removeSystemMonitorConfigFilter(SystemMonitorConfigFilter systemOrgTreeConfig);

	public void updateSystemMonitorConfigFilter(SystemMonitorConfigFilter systemMonitorConfig);
}
