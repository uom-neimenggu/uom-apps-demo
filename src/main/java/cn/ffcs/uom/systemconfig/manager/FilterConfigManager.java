package cn.ffcs.uom.systemconfig.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.model.FilterConfig;

public interface FilterConfigManager {
	/**
	 * 分页取类信息
	 * 
	 * @param queryFilterConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByQuertFilterConfig(FilterConfig queryFilterConfig,
			int currentPage, int pageSize);
	
	/**
	 * 获取所有生效的过滤字符信息
	 * @return
	 */
	public List<FilterConfig> findAllByActive();
	/**
	 * 过滤所有生效的配置信息
	 * @return
	 */
	public String filterAllByActive(String content); 
	/**
	 * 删除记录
	 * 
	 * @param filterConfig
	 */
	public void removeFilterConfig(FilterConfig filterConfig);

	/**
	 * 更新记录
	 * 
	 * @param filterConfig
	 */
	public void updateFilterConfig(FilterConfig filterConfig);

	/**
	 * 保存记录
	 * 
	 * @param filterConfig
	 */
	public void saveFilterConfig(FilterConfig filterConfig);

}
