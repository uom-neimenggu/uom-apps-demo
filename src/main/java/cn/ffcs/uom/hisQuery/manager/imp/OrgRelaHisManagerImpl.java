/**
 * 
 */
package cn.ffcs.uom.hisQuery.manager.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.hisQuery.manager.OrgRelaHisManager;
import cn.ffcs.uom.hisQuery.model.OrgRelaHisVo;

/**
 * @author yahui
 *
 */
@Service("orgRelaHisManager")
@Scope("prototype")
public class OrgRelaHisManagerImpl implements OrgRelaHisManager {
	private OrgRelaHisVo orgRelaHisVo;

	@Override
	public PageInfo queryOrgRelaPageInfoByParams(Map paramsMap,
			int currentPage, int pageSize) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM (SELECT ORG_REL_ID,ORG_ID,RELA_ORG_ID,STATUS_CD,EFF_DATE,EXP_DATE FROM ORGANIZATION_RELATION  WHERE 1=1 ");
		if (paramsMap.get("queryDate") != null) {
			sql.append(" AND EFF_DATE<=?");
			params.add(paramsMap.get("queryDate"));

			if (paramsMap.get("orgId") != null) {
				sql.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}

			sql
					.append("UNION SELECT ORG_REL_ID,ORG_ID, RELA_ORG_ID,STATUS_CD,EFF_DATE,EXP_DATE FROM ORGANIZATION_RELATION_HIS WHERE 1=1");
			if (paramsMap.get("orgId") != null) {
				sql.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}
			sql.append(" AND EFF_DATE<? AND EXP_DATE>?");
			params.add(paramsMap.get("queryDate"));
			params.add(paramsMap.get("queryDate"));
		} else {
			sql.append(" AND STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (paramsMap.get("orgId") != null) {
				sql.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}
		}
		sql.append(")");
		sql.append("ORDER BY ORG_REL_ID");
		return this.orgRelaHisVo.repository().jdbcFindPageInfo(sql.toString(), params, currentPage, pageSize, OrgRelaHisVo.class);
	}

}
