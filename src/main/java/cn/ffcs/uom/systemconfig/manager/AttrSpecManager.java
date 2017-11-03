package cn.ffcs.uom.systemconfig.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.systemconfig.model.AttrSpec;

public interface AttrSpecManager {
	/**
	 * 分页查询属性
	 * 
	 * @param classId
	 * @return
	 */
	public PageInfo queryPageInfoByClassId(Long classId, int currentPage,
			int pageSize);

	/**
	 * 删除属性
	 * 
	 * @param currentAttrSpec
	 */
	public void removeAttrSpec(AttrSpec attrSpec);

	/**
	 * 新增属性
	 * 
	 * @param attrSpec
	 */
	public void saveAttrSpec(AttrSpec attrSpec);

	/**
	 * 修改属性
	 * 
	 * @param attrSpec
	 */
	public void updateAttrSpec(AttrSpec attrSpec);

	/**
	 * 查询
	 * 
	 * @param queryAttrSpec
	 * @return
	 */
	public List queryAttrSpecListByQueryAttrSpec(AttrSpec attrSpec);

}
