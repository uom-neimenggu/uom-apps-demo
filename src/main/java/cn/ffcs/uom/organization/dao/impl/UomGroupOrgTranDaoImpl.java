package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.UomGroupOrgTranDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Repository("uomGroupOrgTranDao")
public class UomGroupOrgTranDaoImpl extends BaseDaoImpl implements UomGroupOrgTranDao {

    @Override
    public List<AttrValue> queryOrgTypeByRegexp(String regexp) {
        List<AttrValue> result = null;
        StringBuffer sqlBuffer = new StringBuffer("SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C "
            + "WHERE C.STATUS_CD = ? AND A.JAVA_CODE = 'OrgType' AND B.JAVA_CODE = 'orgTypeCd' and a.class_id = b.class_id"
            + "  and b.attr_id = c.attr_id");
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        if(!StrUtil.isNullOrEmpty(regexp))
        {
            sqlBuffer.append(" and regexp_like(c.attr_value, ?) ");
            params.add(StringEscapeUtils.escapeSql(regexp));
        }
        
        result = this.getJdbcTemplate().query(sqlBuffer.toString(), params.toArray(), new BeanPropertyRowMapper(AttrValue.class));
        
        return result;
    }
    
}
