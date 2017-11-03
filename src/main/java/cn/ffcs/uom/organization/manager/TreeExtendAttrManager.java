package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.organization.model.OrgAttrSpec;

public interface TreeExtendAttrManager {
	/**
	 * 获取树组织规格
	 * 
	 * @return
	 */
	public List<OrgAttrSpec> getOrgAttrSpecList();

	/**
	 * 根据树类型和种类类型获取属性规格列表
	 * 
	 * @param treeType
	 * @param attrSpecSortTypeTree
	 */
	public List<OrgAttrSpec> getOrgAttrSpecListByTreeTypeAndSortType(
			String treeType, String sortType);
}
