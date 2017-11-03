package cn.ffcs.uom.orgTreeCalc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.dao.TreeStaffOtRuleDao;
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffOtRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;

@Service("treeStaffOtRuleManager")
@Scope("prototype")
public class TreeStaffOtRuleManagerImpl implements TreeStaffOtRuleManager {
	@Resource
	private TreeStaffOtRuleDao treeStaffOtRuleDao;

	@Override
	public PageInfo queryTreeStaffOtRulePageInfo(
			TreeStaffOtRule queryTreeStaffOtRule, int currentPage, int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM TREE_STAFF_OT_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryTreeStaffOtRule != null) {
			if (queryTreeStaffOtRule.getOrgTreeId() != null) {
				sql.append(" AND ORG_TREE_ID=?");
				params.add(queryTreeStaffOtRule.getOrgTreeId());
			}
		}
		return this.treeStaffOtRuleDao.jdbcFindPageInfo(sql.toString(), params,
				currentPage, pageSize, TreeStaffOtRule.class);
	}

	@Override
	public void removeTreeStaffOtRule(TreeStaffOtRule treeStaffOtRule) {
		treeStaffOtRule.removeOnly();
	}

	@Override
	public void addTreeStaffOtRuleList(
			List<TreeStaffOtRule> treeStaffOtRuleList) {
		if (treeStaffOtRuleList != null && treeStaffOtRuleList.size() > 0) {
			for (TreeStaffOtRule temp : treeStaffOtRuleList) {
				temp.addOnly();
			}
		}
	}
}
