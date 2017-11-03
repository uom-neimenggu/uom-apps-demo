package cn.ffcs.uom.organization.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.organization.dao.UomGroupOrgTranDao;
import cn.ffcs.uom.organization.manager.UomGroupOrgTranManager;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Service("uomGroupOrgTranManager")
@Scope("prototype")
public class UomGroupOrgTranManagerImpl implements UomGroupOrgTranManager {
    
    @Resource
    private UomGroupOrgTranDao uomGroupOrgTranDao;
    
    @Override
    public List<AttrValue> queryOrgTypeByRegexp(String regexp) {
        return uomGroupOrgTranDao.queryOrgTypeByRegexp(regexp);
    }
    
}
