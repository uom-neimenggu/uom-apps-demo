package cn.ffcs.uom.businesssystem.manager;

import java.util.List;

import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.businesssystem.model.SystemOrgTreeConfig;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.staff.model.Staff;

public interface SystemOrgTreeConfigManager {
	/**
	 * 获取系统列表
	 * 
	 * @param querySystemOrgTreeConfig
	 * @return
	 */
	public List<BusinessSystem> queryBusinessSystemListByTreeId(Long treeId);

	/**
	 * 分页查询业务系统组织树配置
	 * 
	 * @param systemOrgTreeConfig
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo querySystemOrgTreeConfigPageInfo(
			SystemOrgTreeConfig systemOrgTreeConfig, int currentPage,
			int pageSize);

	/**
	 * 业务系统组织树删除功能
	 * 
	 * @param systemOrgTreeConfig
	 */
	public void removeSystemOrgTreeConfig(
			SystemOrgTreeConfig systemOrgTreeConfig);

	public List<NodeVo> getOrgTreeListbox();

	public List<NodeVo> getBusinessSystemListbox();

	public List<SystemOrgTreeConfig> querySystemOrgTreeConfigList(
			SystemOrgTreeConfig systemOrgTreeConfig);

	public void addSystemOrgTreeConfig(SystemOrgTreeConfig systemOrgTreeConfig);

	public void updateSystemOrgTreeConfig(
			SystemOrgTreeConfig systemOrgTreeConfig);

	public PageInfo queryStaffIssued(Staff staff, int currentPage, int pageSize);

	public PageInfo queryOrganizationIssued(Organization organization,
			int currentPage, int pageSize);
}
