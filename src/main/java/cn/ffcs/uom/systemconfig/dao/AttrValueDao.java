package cn.ffcs.uom.systemconfig.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public interface AttrValueDao extends BaseDao {
	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<AttrValue> queryAttrValueList(AttrValue attrValue,
			String attrValuePosition);
}
