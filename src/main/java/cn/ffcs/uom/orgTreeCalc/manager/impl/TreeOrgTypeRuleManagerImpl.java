package cn.ffcs.uom.orgTreeCalc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.dao.TreeOrgTypeRuleDao;
import cn.ffcs.uom.orgTreeCalc.manager.TreeOrgTypeRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;

@Service("treeOrgTypeRuleManager")
@Scope("prototype")
public class TreeOrgTypeRuleManagerImpl implements TreeOrgTypeRuleManager {
	@Resource
	private TreeOrgTypeRuleDao treeOrgTypeRuleDao;

	@Override
	public PageInfo queryTreeOrgOrgTypeRulePageInfo(
			TreeOrgTypeRule queryTreeOrgTypeRule, int currentPage, int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM TREE_ORG_TYPE_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryTreeOrgTypeRule != null) {
			if (queryTreeOrgTypeRule.getOrgTreeId() != null) {
				sql.append(" AND ORG_TREE_ID=?");
				params.add(queryTreeOrgTypeRule.getOrgTreeId());
			}
		}
		return this.treeOrgTypeRuleDao.jdbcFindPageInfo(sql.toString(), params,
				currentPage, pageSize, TreeOrgTypeRule.class);

	}

	@Override
	public void removeTreeOrgTypeRule(TreeOrgTypeRule treeOrgTypeRule) {
		treeOrgTypeRule.removeOnly();
	}

	@Override
	public void addTreeOrgTypeRuleList(List<TreeOrgTypeRule> treeOrgTypeRuleList) {
		if (treeOrgTypeRuleList != null && treeOrgTypeRuleList.size() > 0) {
			for (TreeOrgTypeRule temp : treeOrgTypeRuleList) {
				temp.addOnly();
			}
		}
	}

}
