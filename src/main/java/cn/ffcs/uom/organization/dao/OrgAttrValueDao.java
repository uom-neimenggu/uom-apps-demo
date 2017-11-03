package cn.ffcs.uom.organization.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.organization.model.OrgAttrValue;

public interface OrgAttrValueDao extends BaseDao {
	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<OrgAttrValue> queryOrgAttrValueList(OrgAttrValue orgAttrValue,
			String orgAttrValuePosition);
}
