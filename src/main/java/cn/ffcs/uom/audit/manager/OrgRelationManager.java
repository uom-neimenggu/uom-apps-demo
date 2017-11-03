package cn.ffcs.uom.audit.manager;

import cn.ffcs.uom.audit.model.OrgRelation;
import cn.ffcs.uom.common.vo.PageInfo;

/**
 * 组织结构查询 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-01-10
 * @功能说明：
 * 
 */
public interface OrgRelationManager {

	/**
	 * 分页查询组织结构
	 * 
	 * @param orgRelation
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgRelation(OrgRelation orgRelation,
			int currentPage, int pageSize);

}
