package cn.ffcs.uom.orgTreeCalc.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.dao.TreeBindingRuleDao;
import cn.ffcs.uom.orgTreeCalc.manager.TreeBindingRuleManager;
import cn.ffcs.uom.orgTreeCalc.model.TreeBindingRule;

@Service("treeBindingRuleManager")
@Scope("prototype")
public class TreeBindingRuleManagerImpl implements TreeBindingRuleManager {
	@Resource
	private TreeBindingRuleDao treeBindingRuleDao;

	@Override
	public PageInfo queryTreeBindingRulePageInfo(TreeBindingRule queryTreeBinding, int currentPage,int pageSize) {
		StringBuffer sql = new StringBuffer("SELECT * FROM TREE_BINDING_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryTreeBinding != null) {
			if (queryTreeBinding.getOrgTreeId() != null) {
				sql.append(" AND ORG_TREE_ID=?");
				params.add(queryTreeBinding.getOrgTreeId());
			}
		}
		return this.treeBindingRuleDao.jdbcFindPageInfo(sql.toString(),params, currentPage, pageSize, TreeBindingRule.class);
	}

	@Override
	public void removeTreeBindingRule(TreeBindingRule treeBindingRule) {
		treeBindingRule.removeOnly();
	}

	@Override
	public void addTreeBindingRule(TreeBindingRule treeBindingRule) {
		treeBindingRule.addOnly();
	}
	
	
	/**
	 * 验证绑定树配置是否存在 
	 * @param treeBindingRule
	 * @return
	 */
	@Override
	public boolean checkTreeBindingRuleIsExist(TreeBindingRule treeBindingRule){
		StringBuffer sql = new StringBuffer("SELECT * FROM TREE_BINDING_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (treeBindingRule != null) {
			if (treeBindingRule.getOrgTreeId() != null) {
				sql.append(" AND ORG_TREE_ID=?");
				params.add(treeBindingRule.getOrgTreeId());
			}
			if(treeBindingRule.getRefTreeId()!=null){
				sql.append(" AND REF_TREE_ID=?");
				params.add(treeBindingRule.getRefTreeId());
			}
			if(!StrUtil.isEmpty(treeBindingRule.getRefOrgRelaCd())){
				sql.append(" AND REF_ORG_RELA_CD=?");
				params.add(treeBindingRule.getRefOrgRelaCd());
			}
		}
		List list = this.treeBindingRuleDao.findListByJDBCSQLAndParams(sql.toString(), params);
		if(list.size()>0)
		return true;
		else
		return false;
	}
	
	
	
	/**
	 * 根据业务树ID获取绑定的专业树配置
	 * @param orgTreeId
	 * @return
	 */
	@Override
	public List<TreeBindingRule> findTreeBindingRule(Long orgTreeId){
		StringBuffer sql = new StringBuffer("SELECT * FROM TREE_BINDING_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(orgTreeId!=null){
			sql.append(" AND ORG_TREE_ID=?");
			params.add(orgTreeId);
		}
		
		return treeBindingRuleDao.jdbcFindList(sql.toString(), params, TreeBindingRule.class);
	}
	
	/**
	 * 根据组织树ID获取组织关系
	 * @param refTreeId
	 * @return
	 */
	@Override
	public String getOrgRelaByRefTreeId(Long refTreeId){
		StringBuffer sql = new StringBuffer("SELECT REF_ORG_RELA_CD FROM TREE_BINDING_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(refTreeId!=null){
			sql.append(" AND REF_TREE_ID=?");
			params.add(refTreeId);
		}
		//List<>treeBindingRuleDao.findListByJDBCSQL(sql.toString());
		return treeBindingRuleDao.getSingleValueByJDBCAndParamsSQL(sql.toString(), params);
	}
    /**
     * 根据组织树根节点ID获取组织关系
     * @param refTreeId
     * @return
     */
    @Override
    public String getOrgRelaByRefRootId(Long rootId){
        StringBuffer sql = new StringBuffer("SELECT REF_ORG_RELA_CD FROM TREE_BINDING_RULE t1,mdsion_org_tree t2 WHERE t1.STATUS_CD=? and t2.status_cd=? and t1.org_tree_id = t2.mdsion_org_tree_id");
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        if(rootId!=null){
            sql.append(" AND t2.org_id=?");
            params.add(rootId);
        }else{
            return null;
        }
        //List<>treeBindingRuleDao.findListByJDBCSQL(sql.toString());
        return treeBindingRuleDao.getSingleValueByJDBCAndParamsSQL(sql.toString(), params);
    }
	/**
	 * 根据配置
	 * @param refTreeId 
	 * @return
	 */
	@Override
	public TreeBindingRule getTreeBindingRuleByRefTreeId(Long refTreeId){
		StringBuffer sql = new StringBuffer("SELECT * FROM TREE_BINDING_RULE WHERE STATUS_CD=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if(refTreeId!=null){
			sql.append(" AND REF_TREE_ID=?");
			params.add(refTreeId);
		}
		return treeBindingRuleDao.jdbcFindObject(sql.toString(), params, TreeBindingRule.class);
	}
	
}
