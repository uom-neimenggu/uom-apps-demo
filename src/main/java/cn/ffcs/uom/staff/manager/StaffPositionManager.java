package cn.ffcs.uom.staff.manager;

import java.util.List;

import cn.ffcs.uom.staff.model.StaffPosition;

public interface StaffPositionManager {
	/**
	 * 删除员工岗位
	 * 
	 * @param staffPosition
	 */
	public void removeStaffPosition(StaffPosition staffPosition);

	/**
	 * 新增员工岗位
	 * 
	 * @param staffPosition
	 */
	public void addStaffPosition(StaffPosition staffPosition);

	/**
	 * 查询员工岗位 列表
	 * 
	 * @param staffPosition
	 * @return
	 */
	public List<StaffPosition> queryStaffPositionList(
			StaffPosition staffPosition);

	/**
	 * 查询员工岗位
	 * 
	 * @param staffPosition
	 * @return
	 */
	public StaffPosition queryStaffPosition(StaffPosition staffPosition);
}
