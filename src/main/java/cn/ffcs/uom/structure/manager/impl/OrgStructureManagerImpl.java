package cn.ffcs.uom.structure.manager.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.structure.dao.OrgStructureDao;
import cn.ffcs.uom.structure.manager.OrgStructureManager;
import cn.ffcs.uom.structure.model.OrgStructure;

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
@Service("orgStructureManager")
@Scope("prototype")
public class OrgStructureManagerImpl implements OrgStructureManager {

	@Resource(name = "orgStructureDao")
	private OrgStructureDao orgStructureDao;

	/**
	 * 分页查询组织结构
	 * 
	 * @param orgStructure
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByOrgStructure(OrgStructure orgStructure,
			int currentPage, int pageSize) {
		return orgStructureDao.queryPageInfoByOrgStructure(orgStructure,
				currentPage, pageSize);
	}

}
