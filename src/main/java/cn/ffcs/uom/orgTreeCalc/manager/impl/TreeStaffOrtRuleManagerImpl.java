package cn.ffcs.uom.orgTreeCalc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.dao.TreeStaffOrtRuleDao;
import cn.ffcs.uom.orgTreeCalc.manager.TreeStaffOrtRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;

@Service("treeStaffOrtRuleManager")
@Scope("prototype")
public class TreeStaffOrtRuleManagerImpl implements TreeStaffOrtRuleManager {
	@Resource
	private TreeStaffOrtRuleDao treeStaffOrtRuleDao;

	@Override
	public PageInfo queryTreeStaffOrRulePageInfo(
			TreeStaffOrtRule queryTreeStaffOrRule, int currentPage, int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM TREE_STAFF_ORT_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryTreeStaffOrRule != null) {
			if (queryTreeStaffOrRule.getOrgTreeId() != null) {
				sql.append(" AND ORG_TREE_ID=?");
				params.add(queryTreeStaffOrRule.getOrgTreeId());
			}
		}
		return this.treeStaffOrtRuleDao.jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, TreeStaffOrtRule.class);
	}

	@Override
	public void removeTreeStaffOrRule(TreeStaffOrtRule treeStaffOrRule) {
		treeStaffOrRule.removeOnly();
	}

	@Override
	public void addTreeStaffOrRuleList(List<TreeStaffOrtRule> list) {
		if (list != null && list.size() > 0) {
			for (TreeStaffOrtRule temp : list) {
				temp.addOnly();
			}
		}
	}

}
