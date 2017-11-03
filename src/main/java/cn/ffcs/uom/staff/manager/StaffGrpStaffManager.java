package cn.ffcs.uom.staff.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.StaffGrpStaff;

public interface StaffGrpStaffManager {
	/**
	 * 删除员工业务
	 * 
	 * @param staffGrpStaff
	 */
	public void removeStaffGrpStaff(StaffGrpStaff staffGrpStaff);

	/**
	 * 新增员工业务
	 * 
	 * @param staffGrpStaff
	 */
	public void addStaffGrpStaff(StaffGrpStaff staffGrpStaff);

	/**
	 * 查询员工业务 列表
	 * 
	 * @param staffGrpStaff
	 * @return
	 */
	public List<StaffGrpStaff> queryStaffGrpStaffList(
			StaffGrpStaff staffGrpStaff);

	/**
	 * 查询员工业务
	 * 
	 * @param staffGrpStaff
	 * @return
	 */
	public StaffGrpStaff queryStaffGrpStaff(StaffGrpStaff staffGrpStaff);

	public PageInfo queryPageInfoByStaffGrpStaff(StaffGrpStaff staffGrpStaff,
			int currentPage, int pageSize);
}
