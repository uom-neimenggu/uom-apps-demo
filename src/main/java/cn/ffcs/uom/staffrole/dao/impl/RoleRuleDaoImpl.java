package cn.ffcs.uom.staffrole.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.staffrole.dao.RoleRuleDao;

@Repository("roleRuleDao")
@Transactional
public class RoleRuleDaoImpl extends BaseDaoImpl implements RoleRuleDao{
    
}
