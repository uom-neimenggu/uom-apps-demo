package cn.ffcs.uom.group.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.model.Group;

public interface GroupDao extends BaseDao {
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

}
