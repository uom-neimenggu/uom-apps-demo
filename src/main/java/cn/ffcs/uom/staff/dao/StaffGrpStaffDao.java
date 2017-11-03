package cn.ffcs.uom.staff.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.staff.model.StaffGrpStaff;
import cn.ffcs.uom.staff.model.StaffPosition;

public interface StaffGrpStaffDao extends BaseDao {
	/**
	 * 新增员工岗位
	 * 
	 * @param staffPosition
	 * @return
	 */
	public Long addStaffPosition(StaffPosition staffPosition);

	public StaffGrpStaff queryStaffGrpStaff(StaffGrpStaff staffGrpStaff);

	public List<StaffGrpStaff> queryStaffGrpStaffList(
			StaffGrpStaff staffGrpStaff);

	public PageInfo queryPageInfoByStaffGrpStaff(StaffGrpStaff staffGrpStaff,
			int currentPage, int pageSize);

}
