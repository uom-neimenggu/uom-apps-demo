package cn.ffcs.uom.position.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.model.Staff;

public interface OrgPositonManager {
	/**
	 * 分页取组织岗位
	 * 
	 * @param queryVo
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByQueryOrgPosition(Position queryPosition,OrgPosition queryVo,
			int currentPage, int pageSize);

	/**
	 * 删除组织岗位关系
	 * 
	 * @param orgPosition
	 */
	public void removeOrganizationPosition(OrgPosition orgPosition);

	/**
	 * 新增组织岗位关系返回id对象
	 * 
	 * @param orgPosition
	 * @return
	 */
	public void addOrganizationPosition(OrgPosition orgPosition);

	/**
	 * 查询员工组织岗位列表
	 * 
	 * @param staff
	 * @param i
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaff(Staff staff, int currentPage,
			int pageSize);

	/**
	 * 查询岗位
	 * 
	 * @param op
	 * @return
	 */
	public OrgPosition queryOrganizationPosition(OrgPosition orgPosition);

	/**
	 * 查询岗位列表
	 * 
	 * @param op
	 * @return
	 */
	public List<OrgPosition> queryOrganizationPositionList(
			OrgPosition orgPosition);
	/**
	 * 批量新增岗位
	 * @param orgPositionList
	 */
	public void addOrganizationPositionList(List<OrgPosition> orgPositionList);
}
