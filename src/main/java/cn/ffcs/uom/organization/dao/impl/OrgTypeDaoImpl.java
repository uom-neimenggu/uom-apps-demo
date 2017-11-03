package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.orgTreeCalc.model.TreeOrgTypeRule;
import cn.ffcs.uom.organization.dao.OrgTypeDao;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Repository("orgTypeDao")
public class OrgTypeDaoImpl extends BaseDaoImpl implements OrgTypeDao {

	@Override
	public List<OrgType> getOrgTypeList(TreeOrgTypeRule totr) {
		if (null == totr) {
			return null;
		}
		List<OrgType> orgTypes = new ArrayList<OrgType>();
		StringBuffer sb = new StringBuffer();
		sb.append("select t3.attr_value, t3.attr_value_name from attr_value t3")
				.append(" where t3.status_cd = ? and t3.attr_id in (select attr_id from attr_spec t2")
				.append(" where t2.status_cd = ? and t2.java_code = 'orgTypeCd' and t2.class_id in")
				.append(" (select class_id from sys_class t1 where t1.java_code = 'OrgType' and t1.status_cd = ?))")
				.append(" and t3.attr_value in (select ref_type_value from TREE_ORG_TYPE_RULE where status_cd = ? and org_tree_id = ?)");
		List<Object> params = new ArrayList<Object>();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(totr.getOrgTreeId());
		List<AttrValue> attrs = super.jdbcFindList(sb.toString(), params,
				AttrValue.class);
		if (null != attrs && attrs.size() > 0) {
			for (AttrValue attrValue : attrs) {
				OrgType orgType = new OrgType();
				orgType.setOrgTypeCd(attrValue.getAttrValue());
				orgTypes.add(orgType);
			}
		}
		return orgTypes;
	}

	/**
	 * 查询某个组织有哪些组织类型
	 * 
	 * @param orgType
	 * @return
	 */
	@Override
	public List<OrgType> queryOrgTypeList(OrgType orgType) {

		List params = new ArrayList();

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM ORG_TYPE WHERE STATUS_CD = ?");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (!StrUtil.isNullOrEmpty(orgType.getOrgTypeId())) {
			sb.append(" AND ORG_TYPE_ID = ?");
			params.add(orgType.getOrgTypeId());
		}
		if (!StrUtil.isNullOrEmpty(orgType.getOrgId())) {
			sb.append(" AND ORG_ID = ?");
			params.add(orgType.getOrgId());
		}
		if (!StrUtil.isNullOrEmpty(orgType.getOrgTypeCd())) {
			sb.append(" AND ORG_TYPE_CD = ?");
			params.add(orgType.getOrgTypeCd());
		}
		return OrgType.repository().jdbcFindList(sb.toString(), params,
				OrgType.class);
	}

}
