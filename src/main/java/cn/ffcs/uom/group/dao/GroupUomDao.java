package cn.ffcs.uom.group.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.model.GroupUomOrg;

public interface GroupUomDao extends BaseDao {
	/**
	 * 分页取类信息
	 * 
	 * @param groupUomOrg
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByGroupUomOrg(GroupUomOrg groupUomOrg,
			int currentPage, int pageSize);

	/**
	 * 更新记录
	 * 
	 * @param groupUomOrg
	 */
	public void updateGroupUomOrg(GroupUomOrg groupUomOrg);

	public void updateGroupUomOrgIsNull(GroupUomOrg groupUomOrg);

	public List<GroupUomOrg> queryGroupUomOrgList(GroupUomOrg groupUomOrg);

}
