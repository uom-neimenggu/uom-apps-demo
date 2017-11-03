/**
 * 
 */
package cn.ffcs.uom.structure.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.structure.dao.OrgStructureDao;
import cn.ffcs.uom.structure.model.OrgStructure;

/**
 * 员工对照表接口 .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-10-28
 * @功能说明：
 * 
 */
@Repository("orgStructureDao")
@Transactional
public class OrgStructureDaoImpl extends BaseDaoImpl implements OrgStructureDao {

	public PageInfo queryPageInfoByOrgStructure(OrgStructure orgStructure,
			int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"WITH W_ORG_REL AS (SELECT T.ORG_ID ,T0.ORG_NAME,T0.ORG_CODE,T0.TELCOM_REGION_ID,")
				.append(" TT.REGION_CODE,T0.ORG_FULL_NAME,T0.ORG_PRIORITY,T0.UUID,T0.ORG_FIX_ID,")
				.append(" T.RELA_ORG_ID FROM TELCOM_REGION TT,ORGANIZATION T0,ORGANIZATION_RELATION T")
				.append(" WHERE T.STATUS_CD = ? AND TT.STATUS_CD = ? AND TT.TELCOM_REGION_ID")
				.append(" = T0.TELCOM_REGION_ID AND T0.STATUS_CD = ? AND T.ORG_ID = T0.ORG_ID")
				.append(" AND T.RELA_CD = ?)SELECT T1.ORG_ID ORGID,T1.ORG_NAME ORGNAME,T1.ORG_CODE")
				.append(" ORGCODE,T1.TELCOM_REGION_ID TELCOMREGIONID,T1.ORG_FULL_NAME,T1.UUID ORGUUID,")
				.append(" T1.ORG_FIX_ID ORGFIXID,T1.RELA_ORG_ID RELAORGID  FROM W_ORG_REL T1 START WITH")
				.append(" T1.ORG_ID IN(SELECT U.ORG_ID FROM ORGANIZATION U WHERE U.STATUS_CD = ? ");

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(OrganizationConstant.RELA_CD_INNER);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (orgStructure != null) {
			if (orgStructure.getQueryTelcomRegion() != null) {
				if (!StrUtil.isNullOrEmpty(orgStructure
						.isQueryIncludeChildren())
						&& orgStructure.isQueryIncludeChildren()) {
					sb.append("  AND U.TELCOM_REGION_ID IN (SELECT TELCOM_REGION_ID");
					sb.append(" FROM TELCOM_REGION WHERE STATUS_CD = ? START WITH");
					sb.append(" TELCOM_REGION_ID = ? CONNECT BY PRIOR TELCOM_REGION_ID = UP_REGION_ID)");
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				} else {
					sb.append("  AND U.TELCOM_REGION_ID = ?");
				}
				params.add(orgStructure.getQueryTelcomRegion()
						.getTelcomRegionId());
			}
			if (!StrUtil.isNullOrEmpty(orgStructure.getOrgId())) {
				sb.append(" AND U.org_Id = ?");
				params.add(orgStructure.getOrgId());
			}

			if (!StrUtil.isEmpty(orgStructure.getOrgCode())) {
				sb.append(" AND U.org_code LIKE ?");
				params.add("%" + orgStructure.getOrgCode() + "%");
			}

			if (!StrUtil.isEmpty(orgStructure.getOrgName())) {
				sb.append(" AND U.Org_Name LIKE ?");
				params.add("%" + orgStructure.getOrgName() + "%");
			}

			if (!StrUtil.isEmpty(orgStructure.getOrgUuId())) {
				sb.append(" AND U.uuid LIKE ?");
				params.add("%" + orgStructure.getOrgUuId() + "%");
			}
		}
		sb.append(" ) CONNECT BY PRIOR T1.ORG_ID = T1.RELA_ORG_ID ORDER SIBLINGS BY T1.ORG_PRIORITY");
		return this.jdbcFindPageInfo2(sb.toString(), params, currentPage,
				pageSize, OrgStructure.class);
	}
}
