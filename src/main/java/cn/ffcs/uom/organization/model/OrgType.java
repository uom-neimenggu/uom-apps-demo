package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.dao.OrgTypeDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 
 * 
 * @author
 * 
 **/
public class OrgType extends UomEntity implements Serializable {

	/**
	 * 组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	/**
	 * 关系类型.
	 **/
	@Getter
	@Setter
	private String orgTypeCd;

	public Long getOrgTypeId() {
		return super.getId();
	}

	public void setOrgTypeId(Long orgTypeId) {
		super.setId(orgTypeId);
	}

	public String getOrgTypeCdName() {
		if (!StrUtil.isEmpty(this.getOrgTypeCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"OrgType", "orgTypeCd", this.getOrgTypeCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgTypeDao repository() {
		return (OrgTypeDao) ApplicationContextUtil.getBean("orgTypeDao");
	}
    public boolean equals(Object obj) {   
        if (obj instanceof OrgType) {   
            OrgType u = (OrgType) obj;   
            return this.getOrgTypeCd().equals(u.getOrgTypeCd());   
        }   
        return super.equals(obj); 
    }	
}
