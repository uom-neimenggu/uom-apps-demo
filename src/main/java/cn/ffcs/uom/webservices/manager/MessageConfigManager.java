package cn.ffcs.uom.webservices.manager;

import java.util.List;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.webservices.model.SystemConfigUser;
import cn.ffcs.uom.webservices.model.SystemMessageConfig;

public interface MessageConfigManager {
	/**
	 * 查询
	 * 
	 * @param systemConfigUser
	 * @return
	 */
	public SystemConfigUser queryMessageConfig(
			SystemConfigUser systemConfigUser);

	/**
	 * 查询短信信息配置列表
	 * 
	 * @param querySystemMessageConfig
	 * @return
	 */
	public List<SystemConfigUser> queryMessageConfigList(
			SystemConfigUser querySystemMessageConfig);

	/**
	 * 分页查询短信信息配置
	 * 
	 * @param systemMessageConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryMessageConfigPageInfo(
			SystemConfigUser systemMessageConfig, int currentPage,
			int pageSize);

	public void addMessageConfig(SystemConfigUser systemConfigUser);

	public void removeMessageConfig(
			SystemConfigUser systemOrgTreeConfig);

	public void updateMessageConfig(
			SystemConfigUser systemConfigUser);

	public String getTelephoneNumberInfo(
			List<SystemConfigUser> systemMessageConfigList);

	public List<SystemConfigUser> querySystemConfigUserListByBusiSys(
			BusinessSystem businessSystem);

	PageInfo queryMessageConfigPageInfo(BusinessSystem businessSystem,
			int currentPage, int pageSize);
}
