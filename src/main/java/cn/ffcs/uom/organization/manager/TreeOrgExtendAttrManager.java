package cn.ffcs.uom.organization.manager;

import java.util.List;
import java.util.Map;

import cn.ffcs.uom.organization.model.OrgAttrSpec;
@SuppressWarnings({"unchecked"})
public interface TreeOrgExtendAttrManager {
	/**
	 * 获取组织规格列表
	 * 
	 * @return
	 */
	public List<OrgAttrSpec> getOrgAttrSpecList();

	/**
	 * 获取树组织列表
	 * 
	 * @param treeType
	 * @param orgId
	 * @param attrSpecSortTypeTree
	 * @return
	 */
    public List<OrgAttrSpec> getOrgAttrSpecListByParams(Map params);
}
