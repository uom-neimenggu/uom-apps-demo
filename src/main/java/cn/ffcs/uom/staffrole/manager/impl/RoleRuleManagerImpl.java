package cn.ffcs.uom.staffrole.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.staffrole.dao.RoleRuleDao;
import cn.ffcs.uom.staffrole.manager.RoleRuleManager;
import cn.ffcs.uom.staffrole.model.RoleRule;

@Service("roleRuleManager")
public class RoleRuleManagerImpl implements RoleRuleManager {
    
    @Autowired
    private RoleRuleDao roleRuleDao;
    
    @Override
    public List<RoleRule> getAllRules() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder(
            "select * from role_rule a where a.status_cd = ? ");
        List<Object> params = new ArrayList<Object>();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        return roleRuleDao.jdbcFindList(sb.toString(), params, RoleRule.class);
    }
    
}
