package cn.ffcs.uom.systemconfig.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.model.SysClass;

public interface SysClassManager {
	/**
	 * 分页取类信息
	 * 
	 * @param querySysClass
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByQuertSysClass(SysClass querySysClass,
			int currentPage, int pageSize);

	/**
	 * 删除记录
	 * 
	 * @param sysClass
	 */
	public void removeSysClass(SysClass sysClass);

	/**
	 * 更新记录
	 * 
	 * @param sysClass
	 */
	public void updateSysClass(SysClass sysClass);

	/**
	 * 保存记录
	 * 
	 * @param sysClass
	 */
	public void saveSysClass(SysClass sysClass);

}
