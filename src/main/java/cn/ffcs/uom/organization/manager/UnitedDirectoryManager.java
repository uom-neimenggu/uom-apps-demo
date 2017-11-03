package cn.ffcs.uom.organization.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.UnitedDirectory;

public interface UnitedDirectoryManager {
	/**
	 * 分页取类信息
	 * 
	 * @param unitedDirectory
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByUnitedDirectory(UnitedDirectory unitedDirectory, int currentPage,
		int pageSize);
	
}
