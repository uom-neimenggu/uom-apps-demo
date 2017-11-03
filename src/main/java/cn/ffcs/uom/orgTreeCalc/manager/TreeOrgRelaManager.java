package cn.ffcs.uom.orgTreeCalc.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRela;
import cn.ffcs.uom.organization.model.OrganizationRelation;

public interface TreeOrgRelaManager {
	void batchInsert(List<TreeOrgRela> list);
	void batchUpdate(List<TreeOrgRela> list);
	void batchRemove(List<TreeOrgRela> list);

	/**
	 * 获取该时间的组织树结构
	 * @param orgTreeId
	 * @param thisDate
	 * @return
	 */
	public List<OrganizationRelation> getThisDateTreeOrgRelaList(Long orgTreeId,
			Date thisDate);
	
	/**
	 * 获取该时间的组织树结构更新情况，包括新增和失效的组织关系
	 * @param list 该时间的生效的组织关系列表
	 * @param orgTreeId
	 * @param thisDate
	 * @return
	 */
	public Map<String, List<TreeOrgRela>> getNeedUpdateTreeOrgRelaDataMap(List<OrganizationRelation> list,
			Long orgTreeId, Date thisDate);

	/**
	 * 更新组织关系中间表数据
	 * 
	 * @param orgTreeId
	 * @param thisDate
	 */
	public void updateTreeOrgRelaData(Long orgTreeId, Date thisDate);
	
	/**
	 * 更新组织关系中间表数据
	 * 
	 * @param orgTreeId
	 * @param thisDate
	 */
	public void updateTreeOrgRelaData(Long orgTreeId, Date preDate,
			Date thisDate);

}
