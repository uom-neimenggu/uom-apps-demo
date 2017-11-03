package cn.ffcs.uom.orgTreeCalc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.dao.TreeStaffSftRuleDao;
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffSftRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;

@Service("treeStaffSftRuleManager")
@Scope("prototype")
public class TreeStaffSftRuleManagerImpl implements TreeStaffSftRuleManager {
	@Resource
	private TreeStaffSftRuleDao treeStaffSftRuleDao;

	@Override
	public PageInfo queryTreeStaffSftRulePageInfoByTreeStaffSftRule(
			TreeStaffSftRule queryTreeStaffSftRule, int currentPage,
			int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM TREE_STAFF_SFT_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryTreeStaffSftRule != null) {
			if (queryTreeStaffSftRule.getOrgTreeId() != null) {
				sql.append(" AND ORG_TREE_ID=?");
				params.add(queryTreeStaffSftRule.getOrgTreeId());
			}
		}
		return this.treeStaffSftRuleDao.jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, TreeStaffSftRule.class);
	}

	@Override
	public void removeTreeStaffSftRule(TreeStaffSftRule treeStaffSftRule) {
		treeStaffSftRule.removeOnly();
	}

	@Override
	public void addTreeStaffSftRule(List<TreeStaffSftRule> treeStaffSftRuleList) {
		if (treeStaffSftRuleList != null && treeStaffSftRuleList.size() > 0) {
			for (TreeStaffSftRule temp : treeStaffSftRuleList) {
				temp.addOnly();
			}
		}
	}
}
