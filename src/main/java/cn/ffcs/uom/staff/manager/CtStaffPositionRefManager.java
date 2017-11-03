package cn.ffcs.uom.staff.manager;

import java.util.List;

import cn.ffcs.uom.staff.model.CtStaffPositionRef;

public interface CtStaffPositionRefManager {
	/**
	 * 删除人员岗位关系
	 * 
	 * @param ctStaffPositionRef
	 */
	public void removeCtStaffPositionRef(CtStaffPositionRef ctStaffPositionRef);

	/**
	 * 新增员工岗位
	 * 
	 * @param staffPosition
	 */
	public void addCtStaffPositionRef(CtStaffPositionRef ctStaffPositionRef);
	
	/**
	 * 查询员工岗位 列表
	 * 
	 * @param ctStaffPositionRef
	 * @return
	 */
	public List<CtStaffPositionRef> queryCtStaffPositionRefList(
			CtStaffPositionRef ctStaffPositionRef);

	/**
	 * 查询员工岗位
	 * 
	 * @param ctStaffPositionRef
	 * @return
	 */
	public CtStaffPositionRef queryCtStaffPositionRef(CtStaffPositionRef ctStaffPositionRef);
}
