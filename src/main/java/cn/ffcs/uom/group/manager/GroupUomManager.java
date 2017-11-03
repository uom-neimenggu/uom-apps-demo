package cn.ffcs.uom.group.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.model.GroupUomOrg;
import cn.ffcs.uom.staff.model.StaffPosition;

public interface GroupUomManager {
	/**
	 * 分页取类信息
	 * 
	 * @param group
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByGroupUomOrg(GroupUomOrg groupUomOrg,
			int currentPage, int pageSize);

	/**
	 * 更新记录
	 * 
	 * @param group
	 */
	public void updateGroupUomOrg(GroupUomOrg groupUomOrg);

	public void updateGroupUomOrgIsNull(GroupUomOrg groupUomOrg);

	public List<GroupUomOrg> queryGroupUomOrgList(GroupUomOrg groupUomOrg);

	/**
	 * 新增
	 * 
	 * @param groupUomOrg
	 */
	public void addGroupUomOrg(GroupUomOrg groupUomOrg);

	/**
	 * 删除
	 * 
	 * @param groupUomOrg
	 */
	public void removeGroupUomOrg(GroupUomOrg groupUomOrg);

}
