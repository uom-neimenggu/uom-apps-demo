package cn.ffcs.uom.dataPermission.manager;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public interface AroleTelcomRegionManager {
	/**
	 * 分页取类信息
	 * 
	 * @param roleTelcomRegion
	 * @param currentPage
	 * @param pageSize
	 * @return
	 * @throws Exception 
	 */
	public PageInfo queryPageInfoByRoleTelcomRegion(AroleTelcomRegion aroleTelcomRegion,
			int currentPage, int pageSize) throws Exception;
	
	/**
	 * 删除记录
	 * 
	 * @param roleTelcomRegion
	 */
	public void removeRoleTelcomRegion(
			AroleTelcomRegion aroleTelcomRegion);

	/**
	 * 保存记录
	 * 
	 * @param roleTelcomRegion
	 */
	public void addRoleTelcomRegion(
			AroleTelcomRegion aroleTelcomRegion);
}
