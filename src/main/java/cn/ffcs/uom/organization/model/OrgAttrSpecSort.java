package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.organization.dao.OrgAttrSpecSortDao;

/**
 * 组织属性规格种类实体.
 * 
 * @author
 * 
 **/
public class OrgAttrSpecSort extends UomEntity implements Serializable {
	/**
	 * 类型标识.
	 **/
	public Long getOrgAttrSpecSortId() {
		return super.getId();
	}

	public void setOrgAttrSpecSortId(Long orgAttrSpecSortId) {
		super.setId(orgAttrSpecSortId);
	}

	/**
	 * 类型名称.
	 **/
	@Getter
	@Setter
	private String sortName;
	/**
	 * 种类类型是否挂树上可以编辑.
	 **/
	@Getter
	@Setter
	private String sortType;
	/**
	 * 属性规格列表
	 */
	private List<OrgAttrSpec> orgAttrSpecList;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgAttrSpecSortDao repository() {
		return (OrgAttrSpecSortDao) ApplicationContextUtil
				.getBean("orgAttrSpecSortDao");
	}

	/**
	 * 获取属性规格列表
	 * 
	 * @return
	 */
	public List<OrgAttrSpec> getOrgAttrSpecList() {
		if (this.orgAttrSpecList == null) {
			if (this.getOrgAttrSpecSortId() != null) {
				String sql = "SELECT * FROM ORG_ATTR_SPEC WHERE STATUS_CD = ? AND ORG_ATTR_SPEC_SORT_ID = ? ORDER BY ORG_ATTR_SPEC_ID";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgAttrSpecSortId());
				this.orgAttrSpecList = OrgAttrSpec.repository().jdbcFindList(
						sql, params, OrgAttrSpec.class);
			}
		}
		return this.orgAttrSpecList;
	}
}
