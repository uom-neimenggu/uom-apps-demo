package cn.ffcs.uom.areacode.manager;

import java.util.List;

import cn.ffcs.uom.areacode.model.AreaCode;

public interface AreaCodeManager {
	/**
	 * 查询列表
	 * 
	 * @param queryAreaCode
	 * @return
	 */
	public List<AreaCode> queryAreaCodeList(AreaCode queryAreaCode);

	/**
	 * 根据区域编码查询列表
	 * 
	 * @param queryAreaCode
	 * @return
	 */
	public List<AreaCode> getAreaCodeListByAreaCode(String areaCode);
}
