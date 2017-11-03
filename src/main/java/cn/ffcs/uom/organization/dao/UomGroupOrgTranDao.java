package cn.ffcs.uom.organization.dao;

import java.util.List;

import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public interface UomGroupOrgTranDao extends BaseDao {
    
    /**
     * 根据正则匹配对应的组织类型
     * .
     * 
     * @param regexp
     * @return
     * @author xiaof
     * 2017年3月17日 xiaof
     */
    public List<AttrValue> queryOrgTypeByRegexp(String regexp);
    
}
