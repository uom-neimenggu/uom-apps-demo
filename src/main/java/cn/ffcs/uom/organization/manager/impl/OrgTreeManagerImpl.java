package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgRelaTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOrtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffOtRule;
import cn.ffcs.uom.orgTreeCalc.model.TreeStaffSftRule;
import cn.ffcs.uom.organization.dao.OrgTreeDao;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;

@Service("orgTreeManager")
@Scope("prototype")
public class OrgTreeManagerImpl implements OrgTreeManager {
	@Resource(name = "orgTreeDao")
	private OrgTreeDao orgTreeDao;

	@Override
	public void addOrgTree(OrgTree orgTree) {
		if (orgTree != null) {
			orgTree.addOnly();
		}
	}

	@Override
	public void updateOrgTree(OrgTree orgTree) {
		if (orgTree != null) {
			orgTree.updateOnly();
		}
	}

	@Override
	public List<OrgTree> queryOrgTreeList(OrgTree orgTree) {
		StringBuffer sb = new StringBuffer("From OrgTree where statusCd=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (orgTree != null) {
			if (orgTree.getOrgTreeId() != null) {
				sb.append(" and orgTreeId=?");
				params.add(orgTree.getOrgTreeId());
			}
			if (orgTree.getIsPublishing() != null) {
				sb.append(" and Is_Publishing=?");
				params.add(orgTree.getIsPublishing());
			}
			/**
			 * 其他查询拼接，因为暂时使用不到不写
			 */
		}
		sb.append(" ORDER BY orgTreeId");
		return orgTreeDao.findListByHQLAndParams(sb.toString(), params);
	}

	@Override
	public OrgTree getOrgTreeByOrgTreeId(Long orgTreeId) {
		OrgTree orgTree = new OrgTree();
		orgTree.setOrgTreeId(orgTreeId);
		List<OrgTree> list = this.queryOrgTreeList(orgTree);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public PageInfo queryOrgTreePageInfoByOrgTree(OrgTree queryOrgTree,
			int currentPage, int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ORG_TREE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryOrgTree != null) {

		}
		return this.orgTreeDao.jdbcFindPageInfo(sql.toString(), params,
				currentPage, pageSize, OrgTree.class);
	}

	@Override
	public void removeOrgTree(OrgTree orgTree) {
		if (orgTree != null && orgTree.getOrgTreeId() != null) {
			orgTree.removeOnly();
			List<TreeOrgRelaTypeRule> treeOrgRelaTypeRuleList = orgTree
					.getTreeOrgOrgRelaList();
			if (treeOrgRelaTypeRuleList != null
					&& treeOrgRelaTypeRuleList.size() > 0) {
				for (TreeOrgRelaTypeRule temp : treeOrgRelaTypeRuleList) {
					temp.removeOnly();
				}
			}
			List<TreeOrgTypeRule> treeOrgTypeRuleList = orgTree
					.getTreeOrgOrgTypeList();
			if (treeOrgTypeRuleList != null && treeOrgTypeRuleList.size() > 0) {
				for (TreeOrgTypeRule temp : treeOrgTypeRuleList) {
					temp.removeOnly();
				}
			}
			List<TreeStaffOrtRule> treeStaffOrtRuleList = orgTree
					.getTreeStaffOrgRelaList();
			if (treeStaffOrtRuleList != null && treeStaffOrtRuleList.size() > 0) {
				for (TreeStaffOrtRule temp : treeStaffOrtRuleList) {
					temp.removeOnly();
				}
			}
			List<TreeStaffOtRule> treeStaffOtRuleList = orgTree
					.getTreeStaffOrgTypeList();
			if (treeStaffOtRuleList != null && treeStaffOtRuleList.size() > 0) {
				for (TreeStaffOtRule temp : treeStaffOtRuleList) {
					temp.removeOnly();
				}
			}
			List<TreeStaffSftRule> treeStaffSftRuleList = orgTree
					.getTreeStaffStaffTypeList();
			if (treeStaffSftRuleList != null && treeStaffSftRuleList.size() > 0) {
				for (TreeStaffSftRule temp : treeStaffSftRuleList) {
					temp.removeOnly();
				}
			}
		}
	}
}
