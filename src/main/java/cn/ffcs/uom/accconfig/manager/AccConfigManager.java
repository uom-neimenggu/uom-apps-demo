package cn.ffcs.uom.accconfig.manager;

import java.util.List;

import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.accconfig.model.SysAccRela;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.syslist.model.SysList;

public interface AccConfigManager {

	/**
	 * 
	 *功能说明:删除配置
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param sysList void
	 *
	 */
	public void removeAccConfig(AccConfig accConfig);
	
	/**
	 * 
	 *功能说明:修改配置
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param accConfig void
	 *
	 */
	public void updateAccConfig(AccConfig accConfig);

	/**
	 * 
	 *功能说明:保存配置
	 *创建人:俸安琪
	 *创建时间:2014-3-13 上午11:06:44
	 *@param accConfig void
	 *
	 */
	public void saveAccConfig(AccConfig accConfig);
	
	/**
	 * 
	 *功能说明:删除配置配置关系
	 *创建人:俸安琪
	 *创建时间:2014-3-14 上午11:47:19
	 *@param staffSysRela void
	 *
	 */
	public void removeSysAccRela(SysAccRela sysAccRela);
	
	/**
	 * 
	 *功能说明:获取配置配置关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param sysAccRela
	 *@return List<SysAccRela>
	 *
	 */
	public PageInfo querySysAccRela(SysAccRela sysAccRela, int currentPage, int pageSize);
	
	/**
	 * 
	 *功能说明:获取配置配置关系
	 *创建人:俸安琪
	 *创建时间:2014-3-12 下午2:28:05
	 *@param sysAccRela
	 *@return sysAccRela
	 *
	 */
	public SysAccRela querySysAccRela(SysAccRela sysAccRela);
	
	/**
	 * 
	 *功能说明:保存配置配置关系
	 *创建人:俸安琪
	 *创建时间:2014-4-23 下午4:32:23
	 *@param staffRole
	 *@param sysList void
	 *
	 */
	public void saveSysAccRelas(List<AccConfig> accConfigs, List<SysList> sysLists);

}
