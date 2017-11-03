package cn.ffcs.uom.accconfig.dao;

import java.util.List;

import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.accconfig.model.SysAccRela;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;

public interface AccConfigDao extends BaseDao{
	
	/**
	 * 
	 *功能说明:查找配置列表
	 *创建人:俸安琪
	 *创建时间:2014-3-28 上午9:28:29
	 *@param accConfig
	 *@return List<AccConfig>
	 *
	 */
	public List<AccConfig> queryAccConfig(AccConfig accConfig);
	
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
}
