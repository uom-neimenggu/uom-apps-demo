package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.organization.model.OrgAttrSpec;

public interface OrgAttrSpecManager {
	/**
	 * 获取属性规列表
	 * 
	 * @return
	 */
	public List<OrgAttrSpec> queryOrgAttrSpecList();
}
