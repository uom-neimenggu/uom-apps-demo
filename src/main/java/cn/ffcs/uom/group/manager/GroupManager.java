package cn.ffcs.uom.group.manager;

import java.util.List;

import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.staff.model.Staff;

public interface GroupManager {
	/**
	 * 分页取类信息
	 * 
	 * @param group
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByGroup(Group group, int currentPage,
			int pageSize);

	/**
	 * 更新记录
	 * 
	 * @param group
	 */
	public void updateGroup(Group group);

	public void updateGroupIsNull(Group group);

	public List<Group> queryGroupList(Group group);

	public void updateGroupProofread(Group group, Staff staff);

}
