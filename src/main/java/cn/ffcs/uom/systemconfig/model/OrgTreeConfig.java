package cn.ffcs.uom.systemconfig.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 *类实体.
 * 
 * @author
 * 
 **/
public class OrgTreeConfig extends UomEntity implements Serializable {
	/**
	 * 组织树配置ID
	 */
	@Getter
	@Setter
	private Long orgTreeConfigId;
	/**
	 *组织树ID
	 **/
	@Getter
	@Setter
	private Long orgTreeId;
	/**
	 *组织树关联的类型
	 **/
	@Getter
	@Setter
	private String orgTypeCd;
	
    public static BaseDao repository() {
        return (BaseDao) ApplicationContextUtil
                .getBean("baseDao");
    }
    
    public void removeByOrgTreeId(long rootId,String relaCd){
        List params = new ArrayList();
        StringBuffer sb = new StringBuffer("update ORG_TREE_CONFIG MORT set mort.STATUS_CD = ? WHERE MORT.ORG_TREE_ID = (select distinct t.mdsion_org_tree_id from mdsion_org_tree t where t.org_id = ? and t.mdsion_org_rel_type_cd = ?)");
        params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
        params.add(orgTreeId);
        params.add(relaCd);
        this.repository().getJdbcTemplate().update(sb.toString(), params.toArray());
    }
}
