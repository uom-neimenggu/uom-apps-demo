package cn.ffcs.uom.hisQuery.manager.imp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.hisQuery.manager.OrgHisManager;
import cn.ffcs.uom.organization.model.Organization;


@Service("orgHisManager")
@Scope("prototype")
public class OrgHisManagerImpl implements OrgHisManager {
    private Organization organization;
    
	@Override
	public PageInfo queryOrgHisPageInfoByParams(Map paramsMap, int currentPage,
			int pageSize) {		
		List params = new ArrayList();
		List paramsHis = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"(SELECT ORG_ID,ORG_NAME,ORG_CODE,STATUS_CD,EFF_DATE,EXP_DATE FROM ORGANIZATION A WHERE 1=1");
		if (paramsMap.get("queryDate") != null) {
			sql.append(" AND A.EFF_DATE <= ?");
			params.add(paramsMap.get("queryDate"));

			StringBuffer hisSql = new StringBuffer(
					" UNION SELECT ORG_ID, ORG_NAME, ORG_CODE, STATUS_CD, EFF_DATE, EXP_DATE FROM ORGANIZATION_HIS B WHERE B.EFF_DATE<? AND B.EXP_DATE>?");
			paramsHis.add(paramsMap.get("queryDate"));
			paramsHis.add(paramsMap.get("queryDate"));
			if (paramsMap.get("orgId") != null) {
				sql.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
				hisSql.append(" AND B.ORG_ID = ?");
				paramsHis.add(paramsMap.get("orgId"));
			}
			sql.append(hisSql.toString());
			params.addAll(paramsHis);
		} else {
			sql.append(" AND A.STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (paramsMap.get("orgId") != null) {
				sql.append(" AND ORG_ID = ?");
				params.add(paramsMap.get("orgId"));
			}
		}
		sql.append(")");
		// 分页转换需要
		sql = new StringBuffer("SELECT * FROM (").append(sql).append(")");
		sql.append("ORDER BY ORG_ID");
		return organization.repository().jdbcFindPageInfo(sql.toString(), params, currentPage, pageSize, Organization.class);

	}

	@Override
	public Organization queryOrgByParams(Map parmsMap) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		List<Organization> orgList =null;
		sql.append("SELECT * FROM  ORGANIZATION WHERE 1=1");
		if(parmsMap.get("orgId") != null){
			sql.append("AND ORG_ID = ?");
			params.add(parmsMap.get("orgId"));
		}
		if(parmsMap.get("effDate")!=null){
			sql.append("AND EFF_DATE <= ?");
			params.add(parmsMap.get("effDate"));
		}
		
		orgList = this.organization.repository().jdbcFindList(sql.toString(), params, Organization.class);
		if(orgList != null && orgList.size() >0){
			return (Organization) orgList.get(0);
		}
		else{
			StringBuffer sqlLog = new StringBuffer();
			List paramsLog = new ArrayList();
			sqlLog.append("SELECT * FROM  ORGANIZATION_HIS WHERE 1=1 ");
			if(parmsMap.get("orgId") != null){
				sqlLog.append(" AND ORG_ID = ?");
				paramsLog.add(parmsMap.get("orgId"));
			}
			if(parmsMap.get("effDate")!=null){
				sqlLog.append(" AND EFF_DATE <= ?");
				paramsLog.add(parmsMap.get("effDate"));
			}
			if(parmsMap.get("expDate") != null){
				sqlLog.append(" AND EXP_DATE > ?");
				paramsLog.add(parmsMap.get("expDate"));
			}
			orgList = this.organization.repository().jdbcFindList(sql.toString(), params,Organization.class);
			if(orgList != null && orgList.size()>0){
				return (Organization) orgList.get(0);
			}
		}
		return null;
	}

}
