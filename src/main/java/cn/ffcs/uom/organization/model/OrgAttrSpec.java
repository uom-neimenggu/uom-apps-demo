package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.dao.OrgAttrSpecDao;

/**
 * 组织属性规格实体.
 * 
 * @author
 * 
 **/
public class OrgAttrSpec extends UomEntity implements Serializable {
	/**
	 * 属性规格标识.
	 **/
	public Long getOrgAttrSpecId() {
		return super.getId();
	}

	public void setOrgAttrSpecId(Long orgAttrSpecId) {
		super.setId(orgAttrSpecId);
	}

	/**
	 * 属性种类.
	 **/
	@Getter
	@Setter
	private Long orgAttrSpecSortId;
	/**
	 * 属性名称.
	 **/
	@Getter
	@Setter
	private String attrName;
	/**
	 * 属性类型.
	 **/
	@Getter
	@Setter
	private Long attrSpecType;
	/**
	 * 下拉值
	 */
	@Getter
	private List<OrgAttrValue> orgAttrValueList;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrgAttrSpecDao repository() {
		return (OrgAttrSpecDao) ApplicationContextUtil
				.getBean("orgAttrSpecDao");
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<OrgAttrValue> getOrgAttrValueList() {
		if (this.orgAttrValueList == null) {
			if (this.getOrgAttrSpecId() != null
					&& attrSpecType == OrganizationConstant.ATTR_SPEC_TYPE_SELECT) {
				String sql = "SELECT * FROM ORG_ATTR_VALUE WHERE STATUS_CD =? AND ORG_ATTR_SPEC_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgAttrSpecId());
				orgAttrValueList = OrgAttrValue.repository().jdbcFindList(sql,
						params, OrgAttrValue.class);
			}
		}
		return orgAttrValueList;
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<NodeVo> getOrgAttrValueNodeVoList() {
		List<NodeVo> list = new ArrayList<NodeVo>();
		List<OrgAttrValue> orgAttrValueList = this.getOrgAttrValueList();
		if (orgAttrValueList != null) {
			for (OrgAttrValue orgAttrValue : orgAttrValueList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(orgAttrValue.getOrgAttrValue());
				nodeVo.setName(orgAttrValue.getOrgAttrValueName());
				list.add(nodeVo);
			}
		}
		return list;
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<NodeVo> getOrgAttrValueNodeVoList(
			List<OrgAttrValue> orgAttrValueList) {
		List<NodeVo> list = new ArrayList<NodeVo>();
		if (orgAttrValueList != null) {
			for (OrgAttrValue orgAttrValue : orgAttrValueList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(orgAttrValue.getOrgAttrValue());
				nodeVo.setName(orgAttrValue.getOrgAttrValueName());
				list.add(nodeVo);
			}
		}
		return list;
	}
}
