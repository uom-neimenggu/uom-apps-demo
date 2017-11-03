package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.organization.model.OrgAttrValue;

public interface OrgAttrValueManager {
	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<OrgAttrValue> queryOrgAttrValueList(OrgAttrValue orgAttrValue,
			String orgAttrValuePosition);
}
