package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.organization.model.OrgAttrSpecSort;

public interface OrgAttrSpecSortManager {
	/**
	 * 获取组织属性规格类型列表
	 * 
	 * @return
	 */
	public List<OrgAttrSpecSort> getOrgAttrSpecSortList();
}
