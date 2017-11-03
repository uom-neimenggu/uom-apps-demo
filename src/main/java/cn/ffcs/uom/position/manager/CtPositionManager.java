package cn.ffcs.uom.position.manager;

import java.util.List;

import cn.ffcs.uom.position.model.CtPosition;

public interface CtPositionManager {
	/**
	 * 查询岗位列表
	 * @param ctPosition
	 * @return
	 */
	public List<CtPosition> queryCtPositionList(CtPosition ctPosition);
	
	/**
	 * 查询岗位
	 * @param ctPosition
	 * @return
	 */
	public CtPosition queryCtPosition(CtPosition ctPosition);
}
