package cn.ffcs.uom.systemconfig.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public interface AttrValueManager {
	/**
	 * 
	 * @param attrId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByAttrId(Long attrId, int currentPage,
			int pageSize);

	/**
	 * 删除属性值
	 * 
	 * @param attrValue
	 */
	public void removeAttrValue(AttrValue attrValue);

	/**
	 * 新增属性值
	 * 
	 * @param attrValue
	 */
	public void saveAttrValue(AttrValue attrValue);

	/**
	 * 更新属性值
	 * 
	 * @param attrValue
	 */
	public void updateAttrValue(AttrValue attrValue);

	/**
	 * 获取属性值
	 * 
	 * @param attrValue
	 * @return
	 */
	public List<AttrValue> queryAttrValueListByQueryAttrValue(
			AttrValue attrValue);

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<AttrValue> queryAttrValueList(AttrValue attrValue,
			String attrValuePosition);

}
