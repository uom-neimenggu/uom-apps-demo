package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.orgTreeCalc.model.TreeOrgStaffRela;
import cn.ffcs.uom.organization.model.StaffOrganization;

public interface TreeOrgStaffRelaManager {
	void batchInsert(List<TreeOrgStaffRela> list);

	void batchRemove(List<TreeOrgStaffRela> list);

	/**
	 * 获取该时间的员工组织关系
	 * 
	 * @param orgTreeId
	 * @param thisDate
	 */
	public List<StaffOrganization> getThisDateTreeStaffOrgList(Long orgTreeId,
			Date thisDate);

	/**
	 * 获取该时间的员工组织更新情况，包括新增和失效的员工组织关系
	 * 
	 * @param list
	 * @param orgTreeId
	 * @param thisDate
	 * @return
	 */
	public Map<String, List<TreeOrgStaffRela>> getNeedUpdateTreeOrgStaffRelaDataMap(List<StaffOrganization> list,
			Long orgTreeId, Date thisDate);

	/**
	 * 更新员工组织关系中间表数据
	 * 
	 * @param orgTreeId
	 * @param thisDate
	 */
	public void updateTreeOrgStaffRelaData(Long orgTreeId, Date thisDate);

	/**
	 * 更新员工组织中间表数据
	 * 
	 * @param orgTreeId
	 * @param preDate
	 * @param thisDate
	 */
	public void updateTreeOrgStaffRelaData(Long orgTreeId,
			java.util.Date preDate, java.util.Date thisDate);
}
