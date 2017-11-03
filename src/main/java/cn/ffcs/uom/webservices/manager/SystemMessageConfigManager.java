package cn.ffcs.uom.webservices.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

public interface SystemMessageConfigManager {
	/**
	 * 查询
	 * 
	 * @param systemMessageConfig
	 * @return
	 */
	public SystemMessageConfig querySystemMessageConfig(
			SystemMessageConfig systemMessageConfig);

	/**
	 * 查询短信信息配置列表
	 * 
	 * @param querySystemMessageConfig
	 * @return
	 */
	public List<SystemMessageConfig> querySystemMessageConfigList(
			SystemMessageConfig querySystemMessageConfig);

	/**
	 * 分页查询短信信息配置
	 * 
	 * @param systemMessageConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo querySystemMessageConfigPageInfo(
			SystemMessageConfig systemMessageConfig, int currentPage,
			int pageSize);

	public void addSystemMessageConfig(SystemMessageConfig systemMessageConfig);

	public void removeSystemMessageConfig(
			SystemMessageConfig systemOrgTreeConfig);

	public void updateSystemMessageConfig(
			SystemMessageConfig systemMessageConfig);

	public String getTelephoneNumberInfo(
			List<SystemMessageConfig> systemMessageConfigList);
}
