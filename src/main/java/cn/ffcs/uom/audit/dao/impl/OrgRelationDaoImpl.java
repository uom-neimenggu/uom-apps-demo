/**
 * 
 */
package cn.ffcs.uom.audit.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.audit.dao.OrgRelationDao;
import cn.ffcs.uom.audit.model.OrgRelation;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;

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
@Repository("orgRelationDao")
@Transactional
public class OrgRelationDaoImpl extends BaseDaoImpl implements OrgRelationDao {

	public PageInfo queryPageInfoByOrgRelation(OrgRelation orgRelation,
			int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM (SELECT ORG_ID AS ORGID,ORG_CODE AS ORGCODE,ORG_NAME AS ORGNAME,TELCOM_REGION_ID AS TELCOMREGIONID,REGION_NAME AS REGIONNAME,CHECK_RESULT AS CHECKRESULT,CREATE_DATE AS CREATEDATE FROM V_UN_BUS2S_RELNSHIP WHERE 1=1");

		if (orgRelation != null) {

			if (!StrUtil.isNullOrEmpty(orgRelation.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(orgRelation.getOrgId());
			}

			if (!StrUtil.isEmpty(orgRelation.getOrgCode())) {
				sb.append(" AND ORG_CODE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(orgRelation.getOrgCode()) + "%");
			}

			if (!StrUtil.isEmpty(orgRelation.getOrgName())) {
				sb.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(orgRelation.getOrgName()) + "%");
			}

			if (orgRelation.getQueryTelcomRegion() != null) {
				if (orgRelation.isQueryIncludeChildren()) {
					sb.append(" AND TELCOM_REGION_ID IN (SELECT TELCOM_REGION_ID FROM TELCOM_REGION WHERE STATUS_CD = ? START WITH TELCOM_REGION_ID = ? CONNECT BY PRIOR TELCOM_REGION_ID = UP_REGION_ID)");
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				} else {
					sb.append(" AND TELCOM_REGION_ID = ?");
				}
				params.add(orgRelation.getQueryTelcomRegion()
						.getTelcomRegionId());
			}

		}

		sb.append(") ORDER BY ORGID");
		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, OrgRelation.class);
	}
}
