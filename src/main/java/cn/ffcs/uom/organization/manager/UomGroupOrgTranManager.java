package cn.ffcs.uom.organization.manager;

import java.util.List;

import cn.ffcs.uom.systemconfig.model.AttrValue;

public interface UomGroupOrgTranManager {
    
    /**
     * 根据正则表达式，查询对应的组织类型属性值
     * .
     * 
     * @param regexp
     * @return
     * @author xiaof
     * 2017年3月17日 xiaof
     */
    public List<AttrValue> queryOrgTypeByRegexp(String regexp);
}
