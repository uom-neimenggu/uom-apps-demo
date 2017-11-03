package cn.ffcs.uom.orgTreeCalc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.dao.TreeOrgRelaTypeRuleDao;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgRelaTypeRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;

@Service("treeOrgRelaTypeRuleManager")
@Scope("prototype")
public class TreeOrgRelaTypeRuleManagerImpl implements
		TreeOrgRelaTypeRuleManager {
	@Resource
	private TreeOrgRelaTypeRuleDao treeOrgRelaTypeRuleDao;

	@Override
	public PageInfo queryTreeOrgOrgRelaRulePageInfo(
			TreeOrgRelaTypeRule queryTreeOrgOrRule, int currentPage,
			int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM TREE_ORG_RELA_TYPE_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryTreeOrgOrRule != null) {
			if (queryTreeOrgOrRule.getOrgTreeId() != null) {
				sql.append(" AND ORG_TREE_ID=?");
				params.add(queryTreeOrgOrRule.getOrgTreeId());
			}
		}
		return this.treeOrgRelaTypeRuleDao.jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, TreeOrgRelaTypeRule.class);
	}

	@Override
	public void removeTreeOrgRelaTypeRule(
			TreeOrgRelaTypeRule treeOrgRelaTypeRule) {
		treeOrgRelaTypeRule.removeOnly();
	}

	@Override
	public void addTreeOrgRelaTypeRuleList(List<TreeOrgRelaTypeRule> list) {
		if (list != null && list.size() > 0) {
			for (TreeOrgRelaTypeRule temp : list) {
				temp.addOnly();
			}
		}
	}

}
