/**
 * 
 */
package cn.ffcs.uom.hisQuery.manager;

import java.util.Map;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.position.model.Position;


/**
 * @author yahui
 *
 */
public interface PositionHisManager {
	/**
	 * 查询历史记录vo
	 * 
	 * @param parmsMap
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPositionHisPageInfoByParams(Map paramsMap,
			int currentPage, int pageSize);
	
	public Position queryPositionDetail(Map paramsMap);

}
