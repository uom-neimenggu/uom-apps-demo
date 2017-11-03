package cn.ffcs.uom.webservices.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.NodeVo;

public interface SystemMonitorSourcesManager {
	/**
	 * 根据类型和事件名获取下拉框值 
	 * 1：事件，2：条件，3：条件值
	 * @param type
	 * @return
	 */
	public List<NodeVo> getValuesListByType(String type,String eventName);
}
