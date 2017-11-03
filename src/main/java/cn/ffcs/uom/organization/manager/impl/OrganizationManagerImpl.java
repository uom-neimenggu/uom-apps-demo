package cn.ffcs.uom.organization.manager.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.CascadeRelationConstants;
import cn.ffcs.uom.common.manager.CascadeRelationManager;
import cn.ffcs.uom.common.model.CascadeRelation;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.IdcardValidator;
import cn.ffcs.uom.common.util.JacksonUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.grid.model.ContractRootRequest;
import cn.ffcs.uom.grid.model.ContractRootResponse;
import cn.ffcs.uom.grid.model.GridRequest;
import cn.ffcs.uom.grid.model.GridResponse;
import cn.ffcs.uom.grid.model.RootRequest;
import cn.ffcs.uom.grid.model.RootResponse;
import cn.ffcs.uom.grid.model.SvcContRequest;
import cn.ffcs.uom.grid.model.SvcContResponse;
import cn.ffcs.uom.grid.model.TcpContRequest;
import cn.ffcs.uom.grid.model.TcpContResponse;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.manager.OrganizationExtendAttrManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.OrgContactInfo;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationExtendAttr;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.organization.model.UomGridCountyLog;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.organization.vo.OrganizationHXImportVo;
import cn.ffcs.uom.party.constants.PartyConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Individual;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.model.PartyContactInfo;
import cn.ffcs.uom.party.model.PartyOrganization;
import cn.ffcs.uom.party.model.PartyRole;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.manager.SystemMessageLogManager;
import cn.ffcs.uom.webservices.model.SystemMessageLog;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;

@Service("organizationManager")
@Scope("prototype")
public class OrganizationManagerImpl implements OrganizationManager
{

    @Resource
    private OrganizationDao                 organizationDao;
    @Resource
    private PartyManager                    partyManager;
    @Resource
    private OrganizationExtendAttrManager   organizationExtendAttrManager;
    /**
     * 系统消息记录
     */
    @Resource
    private SystemMessageLogManager         systemMessageLogManager;
    
    private CascadeRelationManager cascadeRelationManager = (CascadeRelationManager) ApplicationContextUtil
        .getBean("cascadeRelationManager");
    
    public PageInfo queryPageInfoByPackAreaOrganization(Organization organization,
        int currentPage, int pageSize)
    {
        List params = new ArrayList();
        StringBuffer sb = new StringBuffer();
        if(organization != null)
        {
            sb.append("select o.* from (select t1.*, t2.rela_org_id from organization t1 join organization_relation t2 "
                + "on t1.org_id = t2.org_id where t2.rela_cd = '0404' and t1.status_cd = 1000 and t2.status_cd = 1000) o "
                + "where level = 7 and o.telcom_region_id in (SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR "
                + "WHERE TR.STATUS_CD = 1000 START WITH TR.TELCOM_REGION_ID = ? "
                + "CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID) ");
            
            if(organization.getTelcomRegionId() != null) {
            	params.add(organization.getTelcomRegionId().toString());  //安徽区域跟节点
            } else {
            	params.add("287");  //安徽区域跟节点
            }
            
            if (!StrUtil.isNullOrEmpty(organization.getOrgName()))
            {
                sb.append(" AND o.ORG_NAME like ?");
                params.add("%" + StringEscapeUtils.escapeSql(organization.getOrgName()) + "%");
            }
            
            if (!StrUtil.isNullOrEmpty(organization.getOrgCode()))
            {
                sb.append(" AND o.ORG_CODE=?");
                params.add(StringEscapeUtils.escapeSql(organization.getOrgCode()));
            }
            
            sb.append(" start with o.org_id = ?  connect by prior o.org_id = o.rela_org_id");
            //营销组织2017根节点
            params.add("9999999990");
            
        }
        
        return organization.repository().jdbcFindPageInfo(sb.toString(),
            params, currentPage, pageSize, Organization.class);
    }
    
    public PageInfo queryPageInfoByAgentChannelOrganization(Organization organization,
        int currentPage, int pageSize) {
        List params = new ArrayList();
        StringBuffer sb = new StringBuffer();
        if(organization != null)
        {
            sb.append("select t1.*, t3.attr_value_name from ");
            
            //电信区域规定
            if(organization.getTelcomRegionId() != null)
            {
                sb.append(
                    " ( SELECT * FROM (SELECT T1.* FROM ORGANIZATION T1 WHERE 1 = 1 "
                        + "INTERSECT "
                        + "SELECT T1.* FROM ORGANIZATION T1, (SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR "
                        + "WHERE TR.STATUS_CD = 1000 START WITH TR.TELCOM_REGION_ID =")
                    .append(organization.getTelcomRegionId())
                    .append(
                        "CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID) TR "
                            + "WHERE TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID) T1 WHERE T1.STATUS_CD = 1000 ) ");
            }
            else
            {
                sb.append("organization ");
            }
            
            
            sb.append(" t1 left join org_type t2 on t1.org_id = t2.org_id, "
                    + "(SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C "
                + "where C.ATTR_ID = B.ATTR_ID AND A.CLASS_ID = B.CLASS_ID AND A.JAVA_CODE = 'OrgType' "
                + "AND B.JAVA_CODE = 'orgTypeCd') t3 where t2.org_type_cd like 'N02020_0000' "
                + "and t2.org_type_cd = t3.attr_value and t1.status_cd = ? and t2.status_cd = ?");
            
            //BaseUnitConstants.ENTT_STATE_ACTIVE
            params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
            params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
            
            if (!StrUtil.isNullOrEmpty(organization.getOrgName()))
            {
                sb.append(" AND t1.ORG_NAME like ?");
                params.add("%" + StringEscapeUtils.escapeSql(organization.getOrgName()) + "%");
            }
            
            if (!StrUtil.isNullOrEmpty(organization.getOrgCode()))
            {
                sb.append(" AND t1.ORG_CODE=?");
                params.add(StringEscapeUtils.escapeSql(organization.getOrgCode()));
            }
            
        }
        
        return organization.repository().jdbcFindPageInfo(sb.toString(),
            params, currentPage, pageSize, Organization.class);
    }

    public PageInfo queryPageInfoByOrganization(Organization organization,
            int currentPage, int pageSize)
    {
        List params = new ArrayList();
        // ////////////////////////////////////////////////////////////////////////////////////
        StringBuffer sb = new StringBuffer();
        if (organization != null)
        {
            sb.append("SELECT * FROM ( ");
            if (organization.getIsExcluseAgent()
                    || organization.getIsExcluseIbe())
            {// 全部树页面排除代理商组织和内部经营实体组织
                if (organization.getTelcomRegionId() != null)
                {// 管理区域
                    sb.append("SELECT T1.* FROM ORGANIZATION T1  ,( ");
                    sb.append("SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR ");
                    sb.append("WHERE TR.STATUS_CD= ").append(
                            BaseUnitConstants.ENTT_STATE_ACTIVE);
                    sb.append("START WITH TR.TELCOM_REGION_ID=")
                            .append(organization.getTelcomRegionId())
                            .append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID=TR.UP_REGION_ID ");
                    sb.append(") TR ");
                    sb.append("WHERE ");
                    sb.append("TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID ");
                    sb.append("MINUS ");
                }
            }
            sb.append("SELECT T1.* FROM  ORGANIZATION T1 ");

            if (organization.getQueryOrgTypeList() != null
                    && organization.getQueryOrgTypeList().size() > 0)
            {// 组织类型
                sb.append("LEFT JOIN ORG_TYPE OT ON T1.ORG_ID = OT.ORG_ID ");
            }
            if (organization.getIsChooseAgentRoot()
                    || organization.getIsChooseIbeRoot())
            {// 是否是只查询挂在代理商根节点或挂在内部经营实体根节点的组织(包含查询代理商类型或内部经营实体类型)
                sb.append("LEFT JOIN ORGANIZATION_RELATION ORGR ON T1.ORG_ID = ORGR.ORG_ID ");
                sb.append("LEFT JOIN PARTY_ROLE PR ON T1.PARTY_ID = PR.PARTY_ID ");
            }
            else if (organization.getIsAgent()
                    || organization.getIsExcluseAgent()
                    || organization.getIsIbe()
                    || organization.getIsExcluseIbe())
            {// 是否是只查询代理商或者是全部树页面排除代理商,是否是只查询内部经营实体或者是全部树页面排除内部经营实体
                sb.append("LEFT JOIN PARTY_ROLE PR ON T1.PARTY_ID = PR.PARTY_ID ");
            }

            sb.append("WHERE 1=1 ");
            if (organization.getQueryOrgTypeList() != null
                    && organization.getQueryOrgTypeList().size() > 0)
            {
                sb.append("AND OT.STATUS_CD= ").append(
                        BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND ORG_TYPE_CD IN ( ");
                for (int i = 0; i < organization.getQueryOrgTypeList().size(); i++)
                {
                    if (i != 0)
                    {
                        sb.append(",");
                    }
                    sb.append("'")
                            .append(organization.getQueryOrgTypeList().get(i)
                                    .getOrgTypeCd()).append("'");
                }
                sb.append(" )");
            }

            if (organization.getIsChooseAgentRoot())
            {
                sb.append(" AND ORGR.STATUS_CD= ").append(
                        BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND ORGR.RELA_ORG_ID= ").append(
                        OrganizationConstant.ROOT_AGENT_ORG_ID);
                sb.append(" AND PR.STATUS_CD = ").append(
                        BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND PR.ROLE_TYPE = ").append(
                        OrganizationConstant.ROLE_TYPE_AGENT);
            }
            else if (organization.getIsChooseIbeRoot())
            {
                sb.append(" AND ORGR.STATUS_CD= ").append(
                        BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND ORGR.RELA_ORG_ID= ").append(
                        OrganizationConstant.ROOT_IBE_ORG_ID);
                sb.append(" AND PR.STATUS_CD = ").append(
                        BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND PR.ROLE_TYPE = ").append(
                        OrganizationConstant.ROLE_TYPE_AGENT);// 内部经营实体参与人角色类型暂定为代理商
            }
            else if (organization.getIsAgent()
                    || organization.getIsExcluseAgent()
                    || organization.getIsIbe()
                    || organization.getIsExcluseIbe())
            {
                sb.append(" AND PR.STATUS_CD = ").append(
                        BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND PR.ROLE_TYPE = ").append(
                        OrganizationConstant.ROLE_TYPE_AGENT);
            }

            // 目的不区分内部网点或外部网点【使用时再用下面的代码】
            /**
             * 代理商页面或内部经营实体页面添加下级节点除了代理商外或内部经营实体还要包含营业网点
             */
            if (organization.getIsContainSalesNetwork()
                    || organization.getIsContainIbeSalesNetwork())
            {
                sb.append(
                        " UNION (SELECT A.* FROM ORGANIZATION A, ORG_TYPE C WHERE A.STATUS_CD = ")
                        .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
                        .append(" AND C.STATUS_CD=")
                        .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
                        .append(" AND A.ORG_ID = C.ORG_ID AND C.ORG_TYPE_CD LIKE '")
                        .append(OrganizationConstant.SALES_NETWORK_PRE)
                        .append("%') ");
            }
            // 目的不区分内部网点或外部网点【使用时再用上面的代码】

            // 目的区分内部网点或外部网点【使用时再用下面的代码】

            // 代理商页面添加下级节点除了代理商外还要包含营业网点

            // if (organization.isContainSalesNetwork()) {
            // sb.append(
            // " UNION (SELECT A.* FROM ORGANIZATION A, ORG_TYPE C WHERE A.STATUS_CD = ")
            // .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
            // .append(" AND C.STATUS_CD=")
            // .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
            // .append(" AND A.ORG_ID = C.ORG_ID AND C.ORG_TYPE_CD IN ( ")
            // .append("'")
            // .append(OrganizationConstant.ORG_TYPE_N0202020000)
            // .append("',").append("'")
            // .append(OrganizationConstant.ORG_TYPE_N0202050000)
            // .append("',").append("'")
            // .append(OrganizationConstant.ORG_TYPE_N0202060000)
            // .append("'").append(" )) ");
            // }

            // 内部经营实体页面添加下级节点除了内部经营实体外还要包含营业网点

            // if (organization.isContainIbeSalesNetwork()) {
            // sb.append(
            // " UNION (SELECT A.* FROM ORGANIZATION A, ORG_TYPE C WHERE A.STATUS_CD = ")
            // .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
            // .append(" AND C.STATUS_CD=")
            // .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
            // .append(" AND A.ORG_ID = C.ORG_ID AND C.ORG_TYPE_CD IN ( ")
            // .append("'")
            // .append(OrganizationConstant.ORG_TYPE_N0202010000)
            // .append("',").append("'")
            // .append(OrganizationConstant.ORG_TYPE_N0202030000)
            // .append("',").append("'")
            // .append(OrganizationConstant.ORG_TYPE_N0202040000)
            // .append("'").append(" )) ");
            // }

            // 目的区分内部网点或外部网点【使用时再用上面的代码】
            // ////////////////////////////////////////////////////////////////////////////////////

            if (!organization.getIsExcluseAgent()
                    || !organization.getIsExcluseIbe())
            {// 非全部树页面排除代理商组织,非全部树页面排除内部经营实体组织
                // 数据权：管理区域
                if (organization.getTelcomRegionId() != null)
                {
                    sb.append(" INTERSECT ");

                    sb.append("SELECT T1.* FROM ORGANIZATION T1  ,( ");
                    sb.append("SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR ");
                    sb.append("WHERE TR.STATUS_CD= ").append(
                            BaseUnitConstants.ENTT_STATE_ACTIVE);
                    sb.append(" START WITH TR.TELCOM_REGION_ID=")
                            .append(organization.getTelcomRegionId())
                            .append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID=TR.UP_REGION_ID ");
                    sb.append(") TR ");
                    sb.append("WHERE ");
                    sb.append("TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID ");
                }
                else
                {
                    sb.append(" AND 1 = 2 ");
                }
            }
            sb.append(") T1 WHERE T1.STATUS_CD =  ").append(
                    BaseUnitConstants.ENTT_STATE_ACTIVE);

            if (!StrUtil.isNullOrEmpty(organization.getOrgType()))
            {
                sb.append(" AND T1.ORG_TYPE=?");
                params.add(organization.getOrgType());
            }
            if (!StrUtil.isNullOrEmpty(organization.getExistType()))
            {
                sb.append(" AND T1.EXIST_TYPE=?");
                params.add(organization.getExistType());
            }
            if (!StrUtil.isNullOrEmpty(organization.getOrgName()))
            {
                sb.append(" AND T1.ORG_NAME like ?");
                params.add("%" + StringEscapeUtils.escapeSql(organization.getOrgName()) + "%");
            }
            if (!StrUtil.isNullOrEmpty(organization.getOrgCode()))
            {
                sb.append(" AND T1.ORG_CODE=?");
                params.add(StringEscapeUtils.escapeSql(organization.getOrgCode()));
            }
            /**
             * 查询需要排除的组织
             */
            if (organization.getQueryOrgIdList() != null
                    && organization.getQueryOrgIdList().size() > 0)
            {
                sb.append(" AND T1.ORG_ID NOT IN(");
                for (int i = 0; i < organization.getQueryOrgIdList().size(); i++)
                {
                    if (i == 0)
                    {
                        sb.append("?");
                    }
                    else
                    {
                        sb.append(",?");
                    }
                    params.add(organization.getQueryOrgIdList().get(i));
                }
                sb.append(")");
            }
            /**
             * 组织模块现在组织数据权的话，游离的组织无法展示
             * 
             * if (organization.getPermissionOrganizationList() != null &&
             * organization.getPermissionOrganizationList().size() > 0) { sql
             * .append(
             * " AND T1.ORG_ID IN (SELECT DISTINCT (ORG_ID) FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? START WITH A.RELA_ORG_ID IN ("
             * ); params.add(BaseUnitConstants.ENTT_STATE_ACTIVE); for (int i =
             * 0; i < organization .getPermissionOrganizationList().size(); i++)
             * { if (i == 0) { sql.append("?"); } else { sql.append(",?"); }
             * params.add(organization.getPermissionOrganizationList()
             * .get(i).getOrgId()); } sql.append(
             * ") CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID UNION SELECT DISTINCT (RELA_ORG_ID) ORG_ID FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? START WITH A.RELA_ORG_ID IN ("
             * ); params.add(BaseUnitConstants.ENTT_STATE_ACTIVE); for (int i =
             * 0; i < organization .getPermissionOrganizationList().size(); i++)
             * { if (i == 0) { sql.append("?"); } else { sql.append(",?"); }
             * params.add(organization.getPermissionOrganizationList()
             * .get(i).getOrgId()); }
             * sql.append(") CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID)"); }
             * else { // 未配置数据区组织 sql.append(" AND 1=2"); }
             * 
             */
        }
        
        return organization.repository().jdbcFindPageInfo(sb.toString(),
                params, currentPage, pageSize, Organization.class);
    }

    public PageInfo queryPageInfoByOrgTreeRootId(String orgTreeRootId,
            Organization organization, int currentPage, int pageSize)
    {
        List params = new ArrayList();
        // ////////////////////////////////////////////////////////////////////////////////////
        StringBuffer sb = new StringBuffer();
        if (organization.getTelcomRegionId() != null)
        {
            sb.append("SELECT T1.* FROM ORGANIZATION T1  ,( ");
            sb.append("SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR ");
            sb.append(" START WITH TR.TELCOM_REGION_ID=")
                    .append(organization.getTelcomRegionId())
                    .append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID=TR.UP_REGION_ID ");
            sb.append(") TR ");
            sb.append(
                    ",(select ot.org_id from org_type ot,org_tree_config otc where otc.org_tree_id = ")
                    .append(StringEscapeUtils.escapeSql(orgTreeRootId))
                    .append(" and otc.status_cd = 1000 and ot.status_cd = 1000 and ot.org_type_cd = otc.org_type_cd) ot ");
            sb.append("WHERE ");
            sb.append("t1.status_cd = 1000 and t1.org_id = ot.org_id ");
            sb.append("and TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID ");
        }
        else
        {
            return null;
        }
        return organization.repository().jdbcFindPageInfo(sb.toString(),
                params, currentPage, pageSize, Organization.class);
    }

    public PageInfo queryPageInfoByOrganizationNoStatusCd(
        Organization organization, int currentPage, int pageSize){
    	
		StringBuffer sb = new StringBuffer(
			"SELECT S.* FROM organization S WHERE 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (organization != null) {
			
			if (!StrUtil.isNullOrEmpty(organization.getOrgId())) {
				sb.append(" AND S.org_ID = ?");
				params.add(organization.getOrgId());
			}
			
			if (!StrUtil.isNullOrEmpty(organization.getOrgName())) {
				sb.append(" AND S.org_NAME = ?");
				params.add(StringEscapeUtils.escapeSql(organization.getOrgName()));
			}
			
			if (!StrUtil.isNullOrEmpty(organization.getOrgCode())) {
				sb.append(" AND S.org_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(organization.getOrgCode()));
			}
			
			sb.append(" ORDER BY s.org_id desc");
			
		}
		return organization.repository().jdbcFindPageInfo(sb.toString(), params, currentPage, pageSize, Organization.class);
    }
/*    public PageInfo queryPageInfoByOrganizationNoStatusCd(
            Organization organization, int currentPage, int pageSize)
    {
        List params = new ArrayList();
        // ////////////////////////////////////////////////////////////////////////////////////
        StringBuffer sb = new StringBuffer();
        if (organization != null)
        {
            sb.append("SELECT * FROM ( ");
            if (organization.getIsExcluseAgent()
                    || organization.getIsExcluseIbe())
            {// 全部树页面排除代理商组织和内部经营实体组织
                if (organization.getTelcomRegionId() != null)
                {// 管理区域
                    sb.append("SELECT T1.* FROM ORGANIZATION T1  ,( ");
                    sb.append("SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR ");
                    // sb.append("WHERE TR.STATUS_CD= ").append(
                    // BaseUnitConstants.ENTT_STATE_ACTIVE);
                    sb.append("START WITH TR.TELCOM_REGION_ID=")
                            .append(organization.getTelcomRegionId())
                            .append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID=TR.UP_REGION_ID ");
                    sb.append(") TR ");
                    sb.append("WHERE ");
                    sb.append("TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID ");
                    sb.append("MINUS ");
                }
            }
            sb.append("SELECT T1.* FROM  ORGANIZATION T1 ");

            if (organization.getQueryOrgTypeList() != null
                    && organization.getQueryOrgTypeList().size() > 0)
            {// 组织类型
                sb.append("LEFT JOIN ORG_TYPE OT ON T1.ORG_ID = OT.ORG_ID ");
            }
            if (organization.getIsChooseAgentRoot()
                    || organization.getIsChooseIbeRoot())
            {// 是否是只查询挂在代理商根节点或挂在内部经营实体根节点的组织(包含查询代理商类型或内部经营实体类型)
                sb.append("LEFT JOIN ORGANIZATION_RELATION ORGR ON T1.ORG_ID = ORGR.ORG_ID ");
                sb.append("LEFT JOIN PARTY_ROLE PR ON T1.PARTY_ID = PR.PARTY_ID ");
            }
            else if (organization.getIsAgent()
                    || organization.getIsExcluseAgent()
                    || organization.getIsIbe()
                    || organization.getIsExcluseIbe())
            {// 是否是只查询代理商或者是全部树页面排除代理商,是否是只查询内部经营实体或者是全部树页面排除内部经营实体
                sb.append("LEFT JOIN PARTY_ROLE PR ON T1.PARTY_ID = PR.PARTY_ID ");
            }

            sb.append("WHERE 1=1 ");
            if (organization.getQueryOrgTypeList() != null
                    && organization.getQueryOrgTypeList().size() > 0)
            {
                // sb.append("AND OT.STATUS_CD= ").append(
                // BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND ORG_TYPE_CD IN ( ");
                for (int i = 0; i < organization.getQueryOrgTypeList().size(); i++)
                {
                    if (i != 0)
                    {
                        sb.append(",");
                    }
                    sb.append("'")
                            .append(organization.getQueryOrgTypeList().get(i)
                                    .getOrgTypeCd()).append("'");
                }
                sb.append(" )");
            }

            if (organization.getIsChooseAgentRoot())
            {
                // sb.append(" AND ORGR.STATUS_CD= ").append(
                // BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND ORGR.RELA_ORG_ID= ").append(
                        OrganizationConstant.ROOT_AGENT_ORG_ID);
                // sb.append(" AND PR.STATUS_CD = ").append(
                // .ENTT_STATE_ACTIVE);
                sb.append(" AND PR.ROLE_TYPE = ").append(
                        OrganizationConstant.ROLE_TYPE_AGENT);
            }
            else if (organization.getIsChooseIbeRoot())
            {
                // sb.append(" AND ORGR.STATUS_CD= ").append(
                // BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND ORGR.RELA_ORG_ID= ").append(
                        OrganizationConstant.ROOT_IBE_ORG_ID);
                // sb.append(" AND PR.STATUS_CD = ").append(
                // BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND PR.ROLE_TYPE = ").append(
                        OrganizationConstant.ROLE_TYPE_AGENT);// 内部经营实体参与人角色类型暂定为代理商
            }
            else if (organization.getIsAgent()
                    || organization.getIsExcluseAgent()
                    || organization.getIsIbe()
                    || organization.getIsExcluseIbe())
            {
                // sb.append(" AND PR.STATUS_CD = ").append(
                // BaseUnitConstants.ENTT_STATE_ACTIVE);
                sb.append(" AND PR.ROLE_TYPE = ").append(
                        OrganizationConstant.ROLE_TYPE_AGENT);
            }

            // 目的不区分内部网点或外部网点【使用时再用下面的代码】
            *//**
             * 代理商页面或内部经营实体页面添加下级节点除了代理商外或内部经营实体还要包含营业网点
             *//*
            if (organization.getIsContainSalesNetwork()
                    || organization.getIsContainIbeSalesNetwork())
            {
                sb.append(
                        " UNION (SELECT A.* FROM ORGANIZATION A, ORG_TYPE C WHERE 1 = 1")
                        // .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
                        // .append(" AND C.STATUS_CD=")
                        // .append(BaseUnitConstants.ENTT_STATE_ACTIVE)
                        .append(" AND A.ORG_ID = C.ORG_ID AND C.ORG_TYPE_CD LIKE '")
                        .append(OrganizationConstant.SALES_NETWORK_PRE)
                        .append("%') ");
            }

            if (!organization.getIsExcluseAgent()
                    || !organization.getIsExcluseIbe())
            {// 非全部树页面排除代理商组织,非全部树页面排除内部经营实体组织
                // 数据权：管理区域
                if (organization.getTelcomRegionId() != null)
                {
                    sb.append(" INTERSECT ");

                    sb.append("SELECT T1.* FROM ORGANIZATION T1  ,( ");
                    sb.append("SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR ");
                    // sb.append("WHERE TR.STATUS_CD= ").append(
                    // BaseUnitConstants.ENTT_STATE_ACTIVE);
                    sb.append(" START WITH TR.TELCOM_REGION_ID=")
                            .append(organization.getTelcomRegionId())
                            .append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID=TR.UP_REGION_ID ");
                    sb.append(") TR ");
                    sb.append("WHERE ");
                    sb.append("TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID ");
                }
                else
                {
                    sb.append(" AND 1 = 2 ");
                }
            }
            sb.append(") T1 WHERE 1=1  ");

            if (!StrUtil.isNullOrEmpty(organization.getOrgType()))
            {
                sb.append(" AND T1.ORG_TYPE=?");
                params.add(organization.getOrgType());
            }
            if (!StrUtil.isNullOrEmpty(organization.getExistType()))
            {
                sb.append(" AND T1.EXIST_TYPE=?");
                params.add(organization.getExistType());
            }
            if (!StrUtil.isNullOrEmpty(organization.getOrgName()))
            {
                sb.append(" AND T1.ORG_NAME like ?");
                params.add("%" + organization.getOrgName() + "%");
            }
            if (!StrUtil.isNullOrEmpty(organization.getOrgCode()))
            {
                sb.append(" AND T1.ORG_CODE=?");
                params.add(organization.getOrgCode());
            }
            *//**
             * 查询需要排除的组织
             *//*
            if (organization.getQueryOrgIdList() != null
                    && organization.getQueryOrgIdList().size() > 0)
            {
                sb.append(" AND T1.ORG_ID NOT IN(");
                for (int i = 0; i < organization.getQueryOrgIdList().size(); i++)
                {
                    if (i == 0)
                    {
                        sb.append("?");
                    }
                    else
                    {
                        sb.append(",?");
                    }
                    params.add(organization.getQueryOrgIdList().get(i));
                }
                sb.append(")");
            }
            *//**
             * 组织模块现在组织数据权的话，游离的组织无法展示
             * 
             * if (organization.getPermissionOrganizationList() != null &&
             * organization.getPermissionOrganizationList().size() > 0) { sql
             * .append(
             * " AND T1.ORG_ID IN (SELECT DISTINCT (ORG_ID) FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? START WITH A.RELA_ORG_ID IN ("
             * ); params.add(BaseUnitConstants.ENTT_STATE_ACTIVE); for (int i =
             * 0; i < organization .getPermissionOrganizationList().size(); i++)
             * { if (i == 0) { sql.append("?"); } else { sql.append(",?"); }
             * params.add(organization.getPermissionOrganizationList()
             * .get(i).getOrgId()); } sql.append(
             * ") CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID UNION SELECT DISTINCT (RELA_ORG_ID) ORG_ID FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? START WITH A.RELA_ORG_ID IN ("
             * ); params.add(BaseUnitConstants.ENTT_STATE_ACTIVE); for (int i =
             * 0; i < organization .getPermissionOrganizationList().size(); i++)
             * { if (i == 0) { sql.append("?"); } else { sql.append(",?"); }
             * params.add(organization.getPermissionOrganizationList()
             * .get(i).getOrgId()); }
             * sql.append(") CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID)"); }
             * else { // 未配置数据区组织 sql.append(" AND 1=2"); }
             * 
             *//*
        }
        return organization.repository().jdbcFindPageInfo(sb.toString(),
                params, currentPage, pageSize, Organization.class);
    }*/

    @Override
    public void removeOrganization(Organization organization)
    {
        String batchNumber = OperateLog.gennerateBatchNumber();
        organization.setBatchNumber(batchNumber);
        organization.remove();

        List<OrganizationRelation> organizationRelationList = organization
                .getOrganizationRelationList();
        if (organizationRelationList != null
                && organizationRelationList.size() > 0)
        {
            for (OrganizationRelation organizationRelation : organizationRelationList)
            {
                organizationRelation.setBatchNumber(batchNumber);
                organizationRelation.remove();
            }
        }

        OrgContactInfo organizationContactInfo = organization
                .getOrganizationContactInfo();
        if (organizationContactInfo != null)
        {
            organizationContactInfo.setBatchNumber(batchNumber);
            organizationContactInfo.remove();
        }

        List<OrganizationExtendAttr> organizationExtendAttrList = organization
                .getOrganizationExtendAttrList();
        if (organizationExtendAttrList != null
                && organizationExtendAttrList.size() > 0)
        {
            for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList)
            {
                organizationExtendAttr.setBatchNumber(batchNumber);
                organizationExtendAttr.remove();
            }
        }

        List<OrgType> orgTypeList = organization.getOrgTypeList();
        if (orgTypeList != null && orgTypeList.size() > 0)
        {
            for (OrgType orgType : orgTypeList)
            {
                orgType.setBatchNumber(batchNumber);
                orgType.remove();
            }
        }

        List<OrganizationTran> organizationTranListByOrgId = organization
                .getOrganizationTranListByOrgId();
        if (organizationTranListByOrgId != null
                && organizationTranListByOrgId.size() > 0)
        {
            for (OrganizationTran organizationTran : organizationTranListByOrgId)
            {
                organizationTran.setBatchNumber(batchNumber);
                organizationTran.remove();
            }
        }

        List<OrganizationTran> organizationTranListByTranOrgId = organization
                .getOrganizationTranListByTranOrgId();
        if (organizationTranListByTranOrgId != null
                && organizationTranListByTranOrgId.size() > 0)
        {
            for (OrganizationTran organizationTran : organizationTranListByTranOrgId)
            {
                organizationTran.setBatchNumber(batchNumber);
                organizationTran.remove();
            }
        }

        List<UomGroupOrgTran> uomGroupOrgTranList = organization
                .getUomGroupOrgTranList();
        if (uomGroupOrgTranList != null && uomGroupOrgTranList.size() > 0)
        {
            for (UomGroupOrgTran uomGroupOrgTran : uomGroupOrgTranList)
            {
                uomGroupOrgTran.setBatchNumber(batchNumber);
                uomGroupOrgTran.remove();
            }
        }

    }

    @Override
    public void updateOrganization(Organization organization)
    {
        String batchNumber = OperateLog.gennerateBatchNumber();
        String orgCode = organization.generateOrgCode();
        if (!StrUtil.isEmpty(orgCode))
        {
            organization.setOrgCode(orgCode);
        }
        organization.setBatchNumber(batchNumber);
        // 修改组织名称
        if (organization.getIsChangeOrgName())
        {
            this.updateOrgFullNameOnCascade(organization);
        }
        organization.update();
        OrgContactInfo organizationContactInfo = organization
                .getOrganizationContactInfo();
        /**
         * 组织信息新增的情况
         */
        if (organizationContactInfo != null)
        {
            if (organizationContactInfo.getOrganizationContactInfoId() != null)
            {
                organizationContactInfo.setBatchNumber(batchNumber);
                organizationContactInfo.update();
            }
            else
            {
                organizationContactInfo.setBatchNumber(batchNumber);
                organizationContactInfo.setOrgId(organization.getOrgId());// 修复组织联系信息新增时，没有保存组织ID的BUG
                organizationContactInfo.add();
            }
        }
        List<OrganizationExtendAttr> organizationExtendAttrList = organization
                .getOrganizationExtendAttrList();
        if (organizationExtendAttrList != null
                && organizationExtendAttrList.size() > 0)
        {
            for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList)
            {
                /**
                 * 有可能出现新增的属性
                 */
                if (organizationExtendAttr.getOrgExtendAttrId() == null)
                {
                    organizationExtendAttr.setOrgId(organization.getOrgId());
                    organizationExtendAttr.setBatchNumber(batchNumber);
                    organizationExtendAttr.add();
                }
                else
                {
                    organizationExtendAttr.setBatchNumber(batchNumber);
                    organizationExtendAttr.update();
                }
            }
        }
        List<OrgType> addOrgTypeList = organization.getAddOrgTypeList();
        if (addOrgTypeList != null && addOrgTypeList.size() > 0)
        {
            for (OrgType orgType : addOrgTypeList)
            {
                orgType.setOrgId(organization.getOrgId());
                orgType.setBatchNumber(batchNumber);
                orgType.add();
            }
        }
        List<OrgType> delOrgTypeList = organization.getDelOrgTypeList();
        if (delOrgTypeList != null && delOrgTypeList.size() > 0)
        {
            for (OrgType orgType : delOrgTypeList)
            {
                orgType.setBatchNumber(batchNumber);
                orgType.remove();
            }
        }
    }

    @Override
    public void addOrganization(Organization organization)
    {
        String batchNumber = OperateLog.gennerateBatchNumber();
        /**
         * 代理商组织新增有参与人信息
         */
        Party party = organization.getAgentAddParty();
        if (party != null)
        {
            party.setBatchNumber(batchNumber);
            party.add();
            PartyRole partyRole = party.getPartyRole();
            if (partyRole != null)
            {
                partyRole.setPartyId(party.getPartyId());
                partyRole.setBatchNumber(batchNumber);
                partyRole.add();
            }
            Individual individual = party.getIndividual();
            if (individual != null)
            {
                individual.setPartyId(party.getPartyId());
                individual.setBatchNumber(batchNumber);
                individual.add();
            }
            PartyOrganization ptyOrg = party.getPartyOrganization();
            if (ptyOrg != null)
            {
                ptyOrg.setPartyId(party.getPartyId());
                ptyOrg.setBatchNumber(batchNumber);
                ptyOrg.add();
            }
            PartyCertification partyCertification = party
                    .getPartyCertification();
            if (partyCertification != null)
            {
                partyCertification.setPartyId(party.getPartyId());
                partyCertification.setBatchNumber(batchNumber);
                partyCertification.add();
            }
            PartyContactInfo partyContactInfo = party.getPartyContactInfo();
            if (partyContactInfo != null)
            {
                partyContactInfo.setPartyId(party.getPartyId());
                partyContactInfo.setBatchNumber(batchNumber);
                partyContactInfo.add();
            }
        }
        if (party != null && party.getPartyId() != null)
        {
            organization.setPartyId(party.getPartyId());
        }
        organization.setOrgCode(organization.generateOrgCode());
        organization.setUuid(StrUtil.getUUID());
        organization.setBatchNumber(batchNumber);
        organization.add();
        OrgContactInfo organizationContactInfo = organization
                .getOrganizationContactInfo();
        if (organizationContactInfo != null)
        {
            organizationContactInfo.setOrgId(organization.getOrgId());
            organizationContactInfo.setBatchNumber(batchNumber);
            organizationContactInfo.add();
        }
        List<OrganizationExtendAttr> organizationExtendAttrList = organization
                .getOrganizationExtendAttrList();
        if (organizationExtendAttrList != null
                && organizationExtendAttrList.size() > 0)
        {
            for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList)
            {
                organizationExtendAttr.setOrgId(organization.getOrgId());
                organizationExtendAttr.setBatchNumber(batchNumber);
                organizationExtendAttr.add();
            }
        }
        List<OrgType> addOrgTypeList = organization.getAddOrgTypeList();
        if (addOrgTypeList != null && addOrgTypeList.size() > 0)
        {
            for (OrgType orgType : addOrgTypeList)
            {
                orgType.setOrgId(organization.getOrgId());
                orgType.setBatchNumber(batchNumber);
                orgType.add();
            }
        }
    }

    public Organization getById(Long id)
    {
        return organizationDao.getById(id);
    }

    @Override
    public Organization getByIdStatusCd1100(Long id)
    {
        return organizationDao.getByIdStatusCd1100(id);
    }

    @Override
    public List<OrganizationRelation> queryOrganizationRelationList(
            OrganizationRelation or)
    {
        List params = new ArrayList();
        StringBuffer sb = new StringBuffer(
                "SELECT * FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD=?");
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        if (!StrUtil.isEmpty(or.getRelaCd()))
        {
            sb.append(" AND A.RELA_CD=?");
            params.add(or.getRelaCd());
        }
        if (or.getOrgId() != null)
        {
            sb.append(" AND A.ORG_ID=?");
            params.add(or.getOrgId());
        }
        if (or.getRelaOrgId() != null)
        {
            sb.append(" AND A.RELA_ORG_ID=?");
            params.add(or.getRelaOrgId());
        }
        return OrganizationRelation.repository().jdbcFindList(sb.toString(),
                params, OrganizationRelation.class);
    }

    @Override
    public List<OrganizationRelation> querySubTreeOrganizationRelationList(
            Long orgId)
    {
        List params = new ArrayList();
        String sql = "SELECT * FROM (SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD = ?) A START WITH A.RELA_ORG_ID=? CONNECT BY PRIOR A.ORG_ID =A.RELA_ORG_ID";
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(orgId);
        return OrganizationRelation.repository().jdbcFindList(sql, params,
                OrganizationRelation.class);
    }

    @Override
    public List<Organization> queryParentTreeOrganizationList(Long orgId,
            String orgType)
    {
        List params = new ArrayList();
        String sql = "SELECT ORG.* FROM ORGANIZATION ORG, (SELECT * FROM (SELECT T1.* FROM ORGANIZATION_RELATION T1, ORG_TYPE T2 WHERE T1.STATUS_CD = ? AND T2.STATUS_CD = ? AND T1.ORG_ID = T2.ORG_ID AND T2.ORG_TYPE_CD LIKE ?) START WITH ORG_ID = ? CONNECT BY PRIOR RELA_ORG_ID = ORG_ID) ORG_REL WHERE ORG.STATUS_CD = ? AND ORG.ORG_ID = ORG_REL.ORG_ID";
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(orgType + "%");
        params.add(orgId);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        List<Organization> list = Organization.repository().jdbcFindList(sql,
                params, Organization.class);
        return list;
    }

    @Override
    public Organization queryOrganizationByOrgCode(Organization organization)
    {
        return organizationDao.queryOrganizationByOrgCode(organization);
    }

    /**
     * 根据参与人ID查找组织
     * 
     * @param partyId
     */
    @Override
    public Organization queryOrganizationByPartyId(Long partyId)
    {
        return organizationDao.queryOrganizationByPartyId(partyId);
    }

    /**
     * 查询组织信息
     * 
     * @param organization
     * @return
     */
    @Override
    public List<Organization> quertyOrganizationList(Organization organization)
    {
        return organizationDao.quertyOrganizationList(organization);
    }

    /**
     * 分页查询失效组织信息
     * 
     * @param staff
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo forQuertyOrganizationActivation(Organization organization,
            int currentPage, int pageSize)
    {
        return organizationDao.forQuertyOrganizationActivation(organization,
                currentPage, pageSize);
    }

    /**
     * 激活失效的组织
     * 
     * @param staff
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public String updateOrganizationList(List<Organization> organizationList)
    {
        String msg = organizationDao.updateOrganizationList(organizationList);
        return msg;
    }

    /**
     * 级联更新组织全称（只更新归属关系的组织）
     * 
     * @param organization
     */
    public void updateOrgFullNameOnCascade(Organization organization)
    {
        List<Organization> list = organization
                .getRelacd0101SubOrganizationList();
        if (list != null && list.size() > 0)
        {
            for (Organization org : list)
            {
                org.setBatchNumber(organization.getBatchNumber());
                org.setOrgFullName(organization.getOrgFullName()
                        + org.getOrgName());
                org.update();
                this.updateOrgFullNameOnCascade(org);
            }
        }
    }

    @Override
    public Integer getOrgTreeLevel(Long orgId)
    {
        String sql = "SELECT L FROM (SELECT LEVEL L, A.ORG_ID FROM ORGANIZATION_RELATION A WHERE A.STATUS_CD = ? START WITH A.RELA_ORG_ID = ? CONNECT BY PRIOR ORG_ID = RELA_ORG_ID) T WHERE T.ORG_ID = ?";
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(OrganizationConstant.ROOT_TREE_PARENT_ORG_ID);
        params.add(orgId);
        String level = Organization.repository()
                .getSingleValueByJDBCAndParamsSQL(sql, params);

        if (!StrUtil.isEmpty(level))
        {
            return new Integer(level);
        }
        return null;
    }

    /**
     * 获取校验返回信息
     * 
     * @param i
     *            行
     * @param j
     *            列
     * @param validateType
     *            校验类型
     * @return
     */
    private String getValidateMsg(int rowNumber, int colNumber,
            String validateType, StringBuffer sb)
    {
        sb.setLength(0);
        if (OrganizationConstant.NULL_OR_EMPTY.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.NULL_OR_EMPTY_STR)
                    .append("的信息； ").toString();
        }
        else if (OrganizationConstant.FIELD_REPEAT.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_REPEAT_STR)
                    .append("的信息； ").toString();
        }
        else if (OrganizationConstant.LENGTH_LIMIT.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.LENGTH_LIMIT_STR)
                    .append("的信息； ").toString();
        }
        else if (OrganizationConstant.FIELD_ERROR.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_ERROR_STR)
                    .append("的信息； ").toString();
        }
        else if (OrganizationConstant.FIELD_NOT_EXIST.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_NOT_EXIST_STR)
                    .append("； ").toString();
        }
        else if (OrganizationConstant.FIELD_ERROR_VAL.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_ERROR_VAL_STR)
                    .append("； ").toString();
        }
        else if (OrganizationConstant.FIELD_EXIST_VAL.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_EXIST_VAL_STR)
                    .append("； ").toString();
        }
        else if (OrganizationConstant.FIELD_NOT_EXIST_AGENT_W
                .equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_NOT_EXIST_AGENT_W_STR)
                    .append("； ").toString();
        }
        else if (OrganizationConstant.FIELD_NOT_EXIST_AGENT_N
                .equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_NOT_EXIST_AGENT_N_STR)
                    .append("； ").toString();
        }
        else if (OrganizationConstant.FIELD_NOT_IN_REIGHT_SITE
                .equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_NOT_IN_REIGHT_SITE_STR)
                    .append("； ").toString();
        }
        else if (OrganizationConstant.FIELD_NOT_IN_SEVENT_SITE
                .equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                    .append((colNumber + 1)).append("列，出现错误：导入")
                    .append(OrganizationConstant.FIELD_NOT_IN_SEVENT_SITE_STR)
                    .append("； ").toString();
        }
        else if(OrganizationConstant.FILE_NOT_GRID.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                .append((colNumber + 1)).append("列，出现错误：")
                .append(OrganizationConstant.FILE_NOT_GRID_STR)
                .append("； ").toString();
        }
        else if(OrganizationConstant.FILE_TYPE_NOT_RIGHT.equals(validateType))
        {
            return sb.append("文件第").append((rowNumber + 2)).append("行，第")
                .append((colNumber + 1)).append("列，出现错误：")
                .append(OrganizationConstant.FILE_TYPE_NOT_RIGHT_STR)
                .append("； ").toString();
        }
        
        return "";
    }

    /**
     * 验证上传文件数据且写入待上传列表中
     */
    @Override
    public int checkUpLoadFileData(
            List<Organization> waitUpLoadOrganizationList,
            List<Organization> waitUpLoadBusinessOutletsOrgList,
            List<String> checkInfoList, String[][] objDataArray,
            int totalColumn, int[] caseIndex) throws Exception
    {
        int errorDataCount = 0;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < objDataArray.length; i++)
        {
            // 代理商组织关系列表
            List<OrganizationRelation> organizationRelationList = new ArrayList<OrganizationRelation>();
            // 代理商组织关系
            OrganizationRelation organizationRelation = OrganizationRelation
                    .newInstance();
            // 代理商组织类型列表
            List<OrgType> orgTypeList = new ArrayList<OrgType>();
            // 代理商组织类型
            OrgType orgType = new OrgType();

            // 代理商组织
            Organization org = Organization.newInstance();
            // 组织联系信息
            OrgContactInfo orgContactInfo = OrgContactInfo.newInstance();
            // 参与人
            Party party = Party.newInstance();
            // 参与人角色
            PartyRole partyRole = PartyRole.newInstance();
            // 参与人证件类型
            PartyCertification partyCertification = new PartyCertification();
            // 参与人联系人
            PartyContactInfo partyContactInfo = PartyContactInfo.newInstance();
            // 参与人个人信息
            Individual individual = Individual.newInstance();
            // 参与人组织或法人信息
            PartyOrganization partyOrganization = new PartyOrganization();

            party.setPartyRole(partyRole);

            party.setPartyCertification(partyCertification);

            party.setPartyContactInfo(partyContactInfo);

            party.setIndividual(individual);

            party.setPartyOrganization(partyOrganization);

            org.setAgentAddParty(party);

            org.setOrganizationContactInfo(orgContactInfo);

            // 营业网点组织关系列表
            List<OrganizationRelation> businessOutletsOrgRelationList = new ArrayList<OrganizationRelation>();
            // 营业网点内部上级组织关系
            OrganizationRelation businessOutletsOrgRelation = OrganizationRelation
                    .newInstance();
            // 营业网点外部上级组织关系
            OrganizationRelation businessOutletsOrgRelationOuter = OrganizationRelation
                    .newInstance();
            // 代理商组织类型列表
            List<OrgType> businessOutletsOrgTypeList = new ArrayList<OrgType>();
            // 代理商组织类型
            OrgType businessOutletsOrgType = new OrgType();

            // 营业网点组织
            Organization businessOutletsOrg = Organization.newInstance();
            // 营业网点组织联系信息
            OrgContactInfo businessOutletsOrgContactInfo = OrgContactInfo
                    .newInstance();

            businessOutletsOrg
                    .setOrganizationContactInfo(businessOutletsOrgContactInfo);

            for (int j = 0; j < totalColumn; j++)
            {
                String str = "";
                if (null != objDataArray[i][j])
                {
                    str = objDataArray[i][j].trim();
                }
                String[] strs = null;
                if (!StrUtil.isNullOrEmpty(str))
                {
                    strs = str.split("-");
                }

                switch (caseIndex[j])
                {
                    case 0:// 代理商外部上级组织ID
                        if (StringUtils.isNumeric(str))
                        {
                            if (!OrganizationConstant.ROOT_AGENT_ORG_ID
                                    .toString()
                                    .equals(str))
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_ERROR_VAL,
                                        sb));
                            }
                            else
                            {
                                Organization tempOrg = getById(Long
                                        .valueOf(str));
                                if (null != tempOrg)
                                {
                                    organizationRelation
                                            .setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
                                    organizationRelation.setRelaOrgId(Long
                                            .valueOf(str));
                                    organizationRelationList
                                            .add(organizationRelation);

                                    // 上级区域编码
                                    if (tempOrg.getAreaCodeId() != null)
                                        org.setAreaCodeId(tempOrg
                                                .getAreaCodeId());
                                    // 这里验证内部组织还是外部组织
                                    // 内部组织-1000
                                    // 外部组织-1100
                                    // 除了网点和内部组织的关系是0101 其他2个关系0102
                                    // 外部组织还要验证9XXX8的ID

                                    /*
                                     * if(objDataArray[i][2].split("-").length>0)
                                     * { if
                                     * (!StrUtil.isNullOrEmpty(objDataArray[
                                     * i][2])&& OrganizationConstant
                                     * .ORG_TYPE_N.equals(objDataArray
                                     * [i][2].split("-")[1])){//内部组织
                                     * organizationRelation
                                     * .setRelaCd(OrganizationRelationConstant
                                     * .RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS
                                     * ); }else
                                     * if(!StrUtil.isNullOrEmpty(objDataArray[i
                                     * ][2])&&
                                     * OrganizationConstant.ORG_TYPE_W.equals(
                                     * objDataArray[i][2].split("-")[1])){//外部组织
                                     * organizationRelation
                                     * .setRelaCd(OrganizationRelationConstant .
                                     * RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER
                                     * );
                                     * if(!OrganizationConstant.ROOT_AGENT_ORG_ID
                                     * . equals(str)){ errorDataCount++;
                                     * checkInfoList.add(getValidateMsg(i, j,
                                     * OrganizationConstant
                                     * .FIELD_ERROR_VAL,sb)); } }else
                                     * if(!StrUtil.isNullOrEmpty(objDataArray[i
                                     * ][2])){ errorDataCount++;
                                     * checkInfoList.add(getValidateMsg(i, j,
                                     * OrganizationConstant
                                     * .FIELD_ERROR_VAL,sb)); }
                                     * organizationRelation
                                     * .setRelaOrgId(Long.valueOf(str));
                                     * organizationRelationList
                                     * .add(organizationRelation); }
                                     */
                                }
                                else
                                {
                                    errorDataCount++;
                                    checkInfoList
                                            .add(getValidateMsg(
                                                    i,
                                                    j,
                                                    OrganizationConstant.FIELD_NOT_EXIST,
                                                    sb));
                                }
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.FIELD_ERROR, sb));
                        }
                        break;
                    case 1:// 代理商组织名称
                        if (!StrUtil.isEmpty(str))
                        {
                            if (queryOrganizationByOrgNameAndOrgType(str,
                                    OrganizationConstant.ORG_TYPE_AGENT) != null)
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_EXIST_VAL,
                                        sb));
                            }
                            else
                            {
                                org.setOrgName(str);
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 2:// 代理商组织性质
                        if (!StrUtil.isEmpty(str))
                        {
                            org.setOrgType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 3:// 代理商存在类型
                        if (!StrUtil.isEmpty(str))
                        {
                            org.setExistType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 4:// 代理商组织排序
                        if (!StrUtil.isEmpty(str))
                        {
                            org.setOrgPriority(Long.valueOf(str));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 5:// 代理商电信管理区域
                        if (!StrUtil.isEmpty(str))
                        {
                            org.setTelcomRegionId(Long.valueOf(strs[1]));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 6:// 代理商行政管理区域
                        if (!StrUtil.isEmpty(str))
                        {
                            org.setLocationId(Long.valueOf(strs[1]));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 7:// 代理商组织类型
                        if (!StrUtil.isEmpty(str))
                        {
                            if (OrganizationConstant.ORG_TYPE_AGENT
                                    .equals(strs[1]))
                            {
                                orgType.setOrgTypeCd(strs[1]);
                                orgTypeList.add(orgType);
                            }
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_ERROR_VAL,
                                        sb));
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 8:// 代理商组织简称
                        org.setOrgShortName(str);
                        break;
                    case 9:// 代理商组织英文名
                        org.setOrgNameEn(str);
                        break;
                    case 10:// 代理商组织规模
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            org.setOrgScale(strs[strs.length - 1]);
                        }
                        break;
                    case 11:// 代理商负责人信息
                        org.setPrincipal(str);
                        break;
                    case 12:// 代理商业务编码
                        org.setOrgBusinessCode(str);
                        break;
                    case 13:// 代理商城镇标识
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            org.setCityTown(strs[1]);
                        }
                        break;
                    case 14:// 代理商组织简介
                        org.setOrgContent(str);
                        break;

                    case 15:// 代理商联系号码1
                        orgContactInfo.setPhone1(str);
                        break;
                    case 16:// 代理商联系号码2
                        orgContactInfo.setPhone2(str);
                        break;
                    case 17:// 代理商联系号码3
                        orgContactInfo.setPhone3(str);
                        break;
                    case 18:// 代理商联系号码4
                        orgContactInfo.setPhone4(str);
                        break;
                    case 19:// 代理商联系邮箱1
                        orgContactInfo.setEmail1(str);
                        break;
                    case 20:// 代理商联系邮箱2
                        orgContactInfo.setEmail2(str);
                        break;
                    case 21:// 代理商联系邮箱3
                        orgContactInfo.setEmail3(str);
                        break;
                    case 22:// 代理商邮编
                        orgContactInfo.setPostCode(str);
                        break;
                    case 23:// 代理商地址
                        if (!StrUtil.isEmpty(str))
                        {
                            orgContactInfo.setAddress(str);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 24:// 参与人名称
                        if (!StrUtil.isEmpty(str))
                        {
                            party.setPartyName(str);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 25:// 参与人类型
                        if (!StrUtil.isEmpty(str))
                        {
                            party.setPartyType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 26:// 参与人角色类型
                        if (!StrUtil.isEmpty(str))
                        {
                            partyRole.setRoleType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 27:// 证件类型
                        if (!StrUtil.isEmpty(str))
                        {
                            partyCertification.setCertType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 28:// 证件号码
                        if (!StrUtil.isEmpty(str))
                        {
                            if (objDataArray[i][27].split("-").length > 1)
                            {
                                if (PartyConstant.ATTR_VALUE_IDNO
                                        .equals(objDataArray[i][27].split("-")[1]))
                                {
                                    if (IdcardValidator
                                            .isValidatedAllIdcard(str))
                                    {
                                        partyCertification.setCertNumber(str);
                                    }
                                    else
                                    {
                                        errorDataCount++;
                                        checkInfoList
                                                .add(getValidateMsg(
                                                        i,
                                                        j,
                                                        OrganizationConstant.FIELD_ERROR,
                                                        sb));
                                    }
                                }
                                else
                                {
                                    partyCertification.setCertNumber(str);
                                }
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 29:// 证件种类
                        if (!StrUtil.isEmpty(str))
                        {
                            partyCertification.setCertSort(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 30:// 首选联系人
                        if (!StrUtil.isEmpty(str))
                        {
                            partyContactInfo.setHeadFlag(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 31:// 联系人类型
                        if (!StrUtil.isEmpty(str))
                        {
                            partyContactInfo.setContactType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 32:// 联系人名称
                        if (!StrUtil.isEmpty(str))
                        {
                            partyContactInfo.setContactName(str);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 33:// 联系人性别
                        if (!StrUtil.isEmpty(str))
                        {
                            partyContactInfo.setContactGender(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 34:// 移动电话
                        if (!StrUtil.isEmpty(str))
                        {
                            partyContactInfo.setMobilePhone(str);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 35:// 曾用名
                        party.setPartyNameFirst(str);
                        break;
                    case 36:// 参与人名称简拼
                        party.setPartyAbbrname(str);
                        break;
                    case 37:// 参与人英文名称
                        party.setEnglishName(str);
                        break;
                    case 38:// 发证机关
                        partyCertification.setCertOrg(str);
                        break;
                    case 39:// 证件地址
                        partyCertification.setCertAddress(str);
                        break;
                    case 40:// 联系地址
                        partyContactInfo.setContactAddress(str);
                        break;
                    case 41:// 联系单位
                        partyContactInfo.setContactEmployer(str);
                        break;
                    case 42:// 家庭电话
                        partyContactInfo.setHomePhone(str);
                        break;
                    case 43:// 办公电话
                        partyContactInfo.setOfficePhone(str);
                        break;
                    case 44:// 电信内部邮箱
                        partyContactInfo.setInnerEmail(str);
                        break;
                    case 45:// 移动电话(备用)
                        partyContactInfo.setMobilePhoneSpare(str);
                        break;
                    case 46:// 邮箱
                        partyContactInfo.setEmail(str);
                        break;
                    case 47:// 邮政编码
                        partyContactInfo.setPostCode(str);
                        break;
                    case 48:// 邮件地址
                        partyContactInfo.setPostAddress(str);
                        break;
                    case 49:// 传真
                        partyContactInfo.setFax(str);
                        break;
                    case 50:// QQ
                        partyContactInfo.setQqNumber(str);
                        break;
                    case 51:// 详细信息
                        partyContactInfo.setContactDesc(str);
                        break;
                    case 52:// 参与人个人出生日期
                        if (StrUtil.isEmpty(str))
                        {
                            individual.setBirthday(new Date());
                        }
                        else
                        {
                            individual.setBirthday(DateUtil
                                    .convertStringToDate(str));
                        }
                        break;
                    case 53:// 参与人个人婚姻状况
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            individual.setMarriageStatus(strs[1]);
                        }
                        break;
                    case 54:// 参与人个人政治面貌
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            individual.setPoliticsStatus(strs[1]);
                        }
                        break;
                    case 55:// 参与人个人教育水平
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            individual.setEducationLevel(strs[1]);
                        }
                        break;
                    case 56:// 参与人个人性别
                        if (!StrUtil.isNullOrEmpty(objDataArray[i][25])
                                && objDataArray[i][25].split("-").length > 1)
                        {
                            if ("1".equals(objDataArray[i][25].split("-")[1]))
                            {
                                if (!StrUtil.isEmpty(str))
                                {
                                    individual.setGender(strs[1]);
                                }
                                else
                                {
                                    errorDataCount++;
                                    checkInfoList
                                            .add(getValidateMsg(
                                                    i,
                                                    j,
                                                    OrganizationConstant.NULL_OR_EMPTY,
                                                    sb));
                                }
                            }
                        }
                        break;
                    case 57:// 参与人个人国籍
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            individual.setNationality(strs[1]);
                        }
                        break;
                    case 58:// 参与人个人民族
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            individual.setNation(strs[1]);
                        }
                        break;
                    case 59:// 参与人个人 祖籍
                        individual.setNativePlace(str);
                        break;
                    case 60:// 参与人个人 单位
                        individual.setEmployer(str);
                        break;
                    case 61:// 参与人个人宗教
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            individual.setReligion(strs[1]);
                        }
                        break;
                    case 62:// 参与人个人 同名编码
                        individual.setSameNameCode(str);
                        break;
                    case 63:// 参与人组织类型
                        if (!StrUtil.isNullOrEmpty(objDataArray[i][25])
                                && objDataArray[i][25].split("-").length > 1)
                        {
                            if (!"1".equals(objDataArray[i][25].split("-")[1]))
                            {
                                if (!StrUtil.isEmpty(str))
                                {
                                    partyOrganization.setOrgType(strs[1]);
                                }
                                else
                                {
                                    errorDataCount++;
                                    checkInfoList
                                            .add(getValidateMsg(
                                                    i,
                                                    j,
                                                    OrganizationConstant.NULL_OR_EMPTY,
                                                    sb));
                                }
                            }
                        }
                        break;
                    case 64:// 参与人组织简介
                        partyOrganization.setOrgContent(str);
                        break;
                    case 65:// 参与人组织规模
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            partyOrganization
                                    .setOrgScale(strs[strs.length - 1]);
                        }
                        break;
                    case 66:// 参与人组织负责人信息
                        partyOrganization.setPrincipal(str);
                        break;
                    case 67:// 网点内部上级组织编码
                        if (!StrUtil.isEmpty(str))
                        {
                            if (StringUtils.isNumeric(str))
                            {
                                Organization tempOrg = organizationDao
                                        .queryOrganizationByOrgCode(str);
                                if (null != tempOrg)
                                {
                                    businessOutletsOrgRelation
                                            .setRelaOrgId(tempOrg
                                                    .getOrgId());
                                    businessOutletsOrgRelation
                                            .setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS);
                                    businessOutletsOrgRelationList
                                            .add(businessOutletsOrgRelation);
                                    // 上级区域编码
                                    if (tempOrg.getAreaCodeId() != null)
                                        businessOutletsOrg
                                                .setAreaCodeId(tempOrg
                                                        .getAreaCodeId());
                                    // 上级组织全称，在保存时修改成 上级组织全称+当前组织全称
                                    if (tempOrg.getOrgFullName() != null)
                                        businessOutletsOrg
                                                .setOrgFullName(tempOrg
                                                        .getOrgFullName());
                                }
                                else
                                {
                                    errorDataCount++;
                                    checkInfoList
                                            .add(getValidateMsg(
                                                    i,
                                                    j,
                                                    OrganizationConstant.FIELD_NOT_EXIST_AGENT_N,
                                                    sb));
                                }
                            }
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_ERROR, sb));
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 68:// 网点组织名称
                        if (!StrUtil.isEmpty(str))
                        {
                            if (queryOrganizationByOrgNameAndNotSearchOrgType(
                                    str,
                                    OrganizationConstant.ORG_TYPE_AGENT) != null)
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_EXIST_VAL,
                                        sb));
                            }
                            else
                            {
                                businessOutletsOrg.setOrgName(str);
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 69:// 网点组织简称
                        businessOutletsOrg.setOrgShortName(str);
                        break;
                    case 70:// 网点组织英文名
                        businessOutletsOrg.setOrgNameEn(str);
                        break;
                    case 71:// 网点组织性质
                        if (!StrUtil.isEmpty(str))
                        {
                            businessOutletsOrg.setOrgType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 72:// 网点存在类型
                        if (!StrUtil.isEmpty(str))
                        {
                            businessOutletsOrg.setExistType(strs[1]);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 73:// 网点组织规模
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            businessOutletsOrg
                                    .setOrgScale(strs[strs.length - 1]);
                        }
                        break;
                    case 74:// 网点负责人信息
                        businessOutletsOrg.setPrincipal(str);
                        break;
                    case 75:// 网点业务编码
                        businessOutletsOrg.setOrgBusinessCode(str);
                        break;
                    case 76:// 网点组织类型
                        if (!StrUtil.isEmpty(str))
                        {
                            businessOutletsOrgType.setOrgTypeCd(strs[1]);
                            businessOutletsOrgTypeList
                                    .add(businessOutletsOrgType);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 77:// 网点城镇标识
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            businessOutletsOrg.setCityTown(strs[1]);
                        }
                        break;
                    case 78:// 网点组织排序
                        if (!StrUtil.isEmpty(str))
                        {
                            businessOutletsOrg
                                    .setOrgPriority(Long.valueOf(str));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 79:// 网点电信管理区域
                        if (!StrUtil.isEmpty(str))
                        {
                            businessOutletsOrg.setTelcomRegionId(Long
                                    .valueOf(strs[1]));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 80:// 网点行政管理区域
                        if (!StrUtil.isEmpty(str))
                        {
                            businessOutletsOrg.setLocationId(Long
                                    .valueOf(strs[1]));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 81:// 网点组织简介
                        businessOutletsOrg.setOrgContent(str);
                        break;
                    case 82:// 网点联系号码1
                        businessOutletsOrgContactInfo.setPhone1(str);
                        break;
                    case 83:// 网点联系号码2
                        businessOutletsOrgContactInfo.setPhone2(str);
                        break;
                    case 84:// 网点联系号码3
                        businessOutletsOrgContactInfo.setPhone3(str);
                        break;
                    case 85:// 网点联系号码4
                        businessOutletsOrgContactInfo.setPhone4(str);
                        break;
                    case 86:// 网点联系邮箱1
                        businessOutletsOrgContactInfo.setEmail1(str);
                        break;
                    case 87:// 网点联系邮箱2
                        businessOutletsOrgContactInfo.setEmail2(str);
                        break;
                    case 88:// 网点联系邮箱3
                        businessOutletsOrgContactInfo.setEmail3(str);
                        break;
                    case 89:// 网点邮编
                        businessOutletsOrgContactInfo.setPostCode(str);
                        break;
                    case 90:// 网点地址
                        if (!StrUtil.isEmpty(str))
                        {
                            businessOutletsOrgContactInfo.setAddress(str);
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 91:// 网点外部上级组织编码
                        if (!StrUtil.isEmpty(str))
                        {
                            if (StringUtils.isNumeric(str))
                            {
                                Organization tempOrg = organizationDao
                                        .getAgentByOrgCode(str);
                                if (null != tempOrg)
                                {
                                    businessOutletsOrgRelationOuter
                                            .setRelaOrgId(tempOrg.getOrgId());
                                    businessOutletsOrgRelationOuter
                                            .setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
                                    businessOutletsOrgRelationList
                                            .add(businessOutletsOrgRelationOuter);
                                }
                                else
                                {
                                    errorDataCount++;
                                    checkInfoList
                                            .add(getValidateMsg(
                                                    i,
                                                    j,
                                                    OrganizationConstant.FIELD_NOT_EXIST_AGENT_W,
                                                    sb));
                                }
                            }
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_ERROR, sb));
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    default:
                        break;
                }
            }
            org.setOrgTypeList(orgTypeList);
            org.setOrganizationRelationList(organizationRelationList);
            waitUpLoadOrganizationList.add(org);

            businessOutletsOrg.setOrgTypeList(businessOutletsOrgTypeList);
            businessOutletsOrg
                    .setOrganizationRelationList(businessOutletsOrgRelationList);
            waitUpLoadBusinessOutletsOrgList.add(businessOutletsOrg);
        }
        return errorDataCount;
    }

    /**
     * 保存代理商和营业网点（这两个将会取消掉，所以最后只剩下划小单元） 划小单元
     */
    @Override
    public List<List<Map<String, Object>>> saveAgentOrBusinessOutlets(List<String> levelList,
            List<Organization> waitUpLoadOrganizationList,
            List<Organization> waitUpLoadBusinessOutletsOrgList,
            List<OrganizationHXImportVo> waitUpLoadHXOrganizationInfoList, int saveType)
            throws Exception
    {
        String batchNumber = OperateLog.gennerateBatchNumber();// 批次号
        List<List<Map<String, Object>>> resultList = new ArrayList<List<Map<String, Object>>>();
        List<Map<String, Object>> saveAgentResultList = null;
        List<Map<String, Object>> saveBusinessOutletsResultList = null;
        // 保存划小组织单元
        List<Map<String, Object>> saveHXResultList = null;
        switch (saveType)
        {
            case 1:// 保存代理商和营业网点
                if (null != waitUpLoadOrganizationList
                        && waitUpLoadOrganizationList.size() > 0
                        && null != waitUpLoadBusinessOutletsOrgList
                        && waitUpLoadBusinessOutletsOrgList.size() > 0)
                {
                    // 保存代理商
                    saveAgentResultList = saveAgent(waitUpLoadOrganizationList,
                            batchNumber);
                    // 保存营业网点
                    saveBusinessOutletsResultList = saveBusinessOutlets(
                            waitUpLoadBusinessOutletsOrgList, batchNumber);
                    // 新增网点与代理商关系
                    for (int i = 0; i < saveAgentResultList.size(); i++)
                    {
                        Map<String, Object> agentMap = saveAgentResultList
                                .get(i);
                        Map<String, Object> businessOutletsMap = saveBusinessOutletsResultList
                                .get(i);
                        OrganizationRelation organizationRelation = OrganizationRelation
                                .newInstance();
                        organizationRelation.setBatchNumber(batchNumber);
                        organizationRelation.setRelaOrgId(Long.valueOf(agentMap
                                .get("orgId").toString()));
                        organizationRelation
                                .setOrgId(Long.valueOf(businessOutletsMap.get(
                                        "orgId").toString()));
                        organizationRelation
                                .setRelaCd(OrganizationRelationConstant.RELA_CD_SUPERIOR_MANAGEMENT_INSTITUTIONS_OUTER);
                        organizationRelation.add();
                    }
                }
                break;
            case 2:// 保存代理商
                if (null != waitUpLoadOrganizationList
                        && waitUpLoadOrganizationList.size() > 0)
                {
                    saveAgentResultList = saveAgent(waitUpLoadOrganizationList,
                            batchNumber);
                }
                break;
            case 3:// 保存营业网点
                if (null != waitUpLoadBusinessOutletsOrgList
                        && waitUpLoadBusinessOutletsOrgList.size() > 0)
                {
                    // 保存营业网点
                    saveBusinessOutletsResultList = saveBusinessOutlets(
                            waitUpLoadBusinessOutletsOrgList, batchNumber);
                }
                break;
            case 4:// 保存划小单元
                if (null != waitUpLoadHXOrganizationInfoList
                        && waitUpLoadHXOrganizationInfoList.size() > 0)
                {
                    // 保存划小单元
                    saveHXResultList = saveHXOrganization(levelList,
                        waitUpLoadHXOrganizationInfoList, batchNumber);
                }
                break;
        }
        resultList.add(saveAgentResultList);
        resultList.add(saveBusinessOutletsResultList);
        return resultList;
    }

    public List<Map<String, Object>> saveHXOrganization(List<String> levelList,
        List<OrganizationHXImportVo> waitUpLoadHXOrganizationInfoList, String batchNumber)
    {
        List<Map<String, Object>> saveHXResultList = new ArrayList<Map<String, Object>>();
        //遍历到第几个
        int i = 0;
        for (OrganizationHXImportVo organizationHXImportVo : waitUpLoadHXOrganizationInfoList)
        {
            Organization organization = organizationHXImportVo.getHxOrganization();
            if (null != organization)
            {
                Map<String, Object> result = new LinkedHashMap<String, Object>();
                // 设置划小组织参数并保存
                // 设置批次号
                organization.setBatchNumber(batchNumber);
                // 生成组织编码
                organization.setOrgCode(organization.generateOrgCode());
                // 生成UUID
                organization.setUuid(StrUtil.getUUID());
                // 设置组织全名
                organization.setOrgFullName(organization.getOrgName());

                // 添加组织
                organization.add();
                
                
                //生成edacode，这个是上一级的
                int orgLevel = Integer.valueOf(levelList.get(i++)) + 1;
                boolean isGrid = false;
                if(orgLevel == 7)
                    isGrid = true;
                else
                    isGrid = false;

                //由于是新增，所以edacode都是空的
                if (StrUtil.isEmpty(organization.getEdaCode()))
                {
                    organization.setEdaCode(organization.generateEdaCode(
                            orgLevel,
                            isGrid));
                }
                
                
//              // 组织联系信息
                OrgContactInfo orgContactInfo = organization
                        .getOrganizationContactInfo();

                if (null != orgContactInfo)
                {
                    orgContactInfo.setOrgId(organization.getOrgId());
                    orgContactInfo.setBatchNumber(batchNumber);
                    orgContactInfo.add();
                }

                // 组织关系
                for (OrganizationRelation organizationRelation : organization
                        .getCurrOrganizationRelationList())
                {
                    organizationRelation.setOrgId(organization.getOrgId());
                    organizationRelation.setBatchNumber(batchNumber);
                    organizationRelation.add();
                }
                // 组织类型
                for (OrgType orgType : organization.getCurrOrgTypeList())
                {
                    orgType.setOrgId(organization.getOrgId());
                    orgType.setBatchNumber(batchNumber);
                    orgType.add();
                }
                
                //网格组织扩展属性保存
                if (isGrid) {
                    List<OrganizationExtendAttr> organizationExtendAttrList = organizationHXImportVo
                        .getHxOrganizationExtendAttrList();
                    // 遍历扩展属性，并存到数据库
                    if (organizationExtendAttrList != null && organizationExtendAttrList.size() > 0) {
                        for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList) {
                            organizationExtendAttr.setOrgId(organization.getOrgId());
                            organizationExtendAttr.setBatchNumber(batchNumber);
                            organizationExtendAttr.add();
                        }
                    }
                }

                result.put("orgId", organization.getOrgId());
                result.put("orgCode", organization.getOrgCode());
                result.put("orgUuid", organization.getUuid());
                result.put("orgFixId", organization.getOrgFixId());
                result.put("orgName", organization.getOrgName());
                saveHXResultList.add(result);
            }
        }
        return saveHXResultList;
    }

    /**
     * 保存代理商
     * 
     * @param organization
     * @return
     */
    public List<Map<String, Object>> saveAgent(
            List<Organization> waitUpLoadOrganizationList, String batchNumber)
    {
        List<Map<String, Object>> saveAgentResultList = new ArrayList<Map<String, Object>>();
        for (Organization organization : waitUpLoadOrganizationList)
        {
            if (null != organization)
            {
                Map<String, Object> result = new LinkedHashMap<String, Object>();
                // 参与人
                Party party = organization.getAgentAddParty();
                party.setBatchNumber(batchNumber);
                party.add();
                // 参与人个人信息
                Individual individual = party.getIndividual();
                // 参与人证件类型
                PartyCertification ptyCerfic = party.getPartyCertification();
                // 参与人联系人
                PartyContactInfo ptyConInfo = party.getPartyContactInfo();
                // 参与人角色
                PartyRole partyRole = party.getPartyRole();
                // 参与人组织或法人信息
                PartyOrganization partyOrganization = party
                        .getPartyOrganization();

                if (null != individual)
                {
                    individual.setPartyId(party.getPartyId());
                    individual.setBatchNumber(batchNumber);
                    individual.add();
                }
                if (null != ptyCerfic)
                {
                    ptyCerfic.setPartyId(party.getPartyId());
                    ptyCerfic.setBatchNumber(batchNumber);
                    ptyCerfic.add();
                }
                if (null != ptyConInfo)
                {
                    ptyConInfo.setPartyId(party.getPartyId());
                    ptyConInfo.setBatchNumber(batchNumber);
                    ptyConInfo.add();
                }
                if (null != partyRole)
                {
                    partyRole.setPartyId(party.getPartyId());
                    partyRole.setBatchNumber(batchNumber);
                    partyRole.add();
                }
                if (null != partyOrganization)
                {
                    partyOrganization.setBatchNumber(batchNumber);
                    partyOrganization.setPartyId(party.getPartyId());
                }

                // 设置参与人并保存代理商组织
                organization.setBatchNumber(batchNumber);
                organization.setPartyId(party.getPartyId());
                organization.setOrgCode(organization.generateOrgCode());
                organization.setUuid(StrUtil.getUUID());
                organization.setOrgFullName(organization.getOrgName());
                organization.add();

                // 组织联系信息
                OrgContactInfo orgContactInfo = organization
                        .getOrganizationContactInfo();

                if (null != orgContactInfo)
                {
                    orgContactInfo.setOrgId(organization.getOrgId());
                    orgContactInfo.setBatchNumber(batchNumber);
                    orgContactInfo.add();
                }

                // 组织关系
                for (OrganizationRelation organizationRelation : organization
                        .getCurrOrganizationRelationList())
                {
                    organizationRelation.setOrgId(organization.getOrgId());
                    organizationRelation.setBatchNumber(batchNumber);
                    organizationRelation.add();
                }
                // 组织类型
                for (OrgType orgType : organization.getCurrOrgTypeList())
                {
                    orgType.setOrgId(organization.getOrgId());
                    orgType.setBatchNumber(batchNumber);
                    orgType.add();
                }

                result.put("orgId", organization.getOrgId());
                result.put("orgCode", organization.getOrgCode());
                result.put("orgUuid", organization.getUuid());
                result.put("orgFixId", organization.getOrgFixId());
                result.put("orgName", organization.getOrgName());
                result.put("partyId", party.getPartyId());
                result.put("partyRoleId", partyRole.getPartyRoleId());
                saveAgentResultList.add(result);
            }
        }
        return saveAgentResultList;
    }

    /**
     * 保存营业网点
     * 
     * @param waitUpLoadBusinessOutletsOrgList
     * @return
     */
    public List<Map<String, Object>> saveBusinessOutlets(
            List<Organization> waitUpLoadBusinessOutletsOrgList,
            String batchNumber)
    {

        List<Map<String, Object>> saveBusinessOutletsResultList = new ArrayList<Map<String, Object>>();
        for (Organization organization : waitUpLoadBusinessOutletsOrgList)
        {
            if (null != organization)
            {
                Map<String, Object> result = new LinkedHashMap<String, Object>();

                // 设置参与人并保存营业网点组织
                organization.setBatchNumber(batchNumber);
                organization.setOrgCode(organization.generateOrgCode());
                organization.setUuid(StrUtil.getUUID());
                if (organization.getOrgFullName() != null)
                    organization.setOrgFullName(organization.getOrgFullName()
                            + organization.getOrgName());
                else
                    organization.setOrgFullName(organization.getOrgName());
                organization.add();

                // 组织联系信息
                OrgContactInfo orgContactInfo = organization
                        .getOrganizationContactInfo();

                if (null != orgContactInfo)
                {
                    orgContactInfo.setOrgId(organization.getOrgId());
                    orgContactInfo.setBatchNumber(batchNumber);
                    orgContactInfo.add();
                }

                // 组织关系
                for (OrganizationRelation organizationRelation : organization
                        .getCurrOrganizationRelationList())
                {
                    organizationRelation.setOrgId(organization.getOrgId());
                    organizationRelation.setBatchNumber(batchNumber);
                    organizationRelation.add();
                }
                // 组织类型
                for (OrgType orgType : organization.getCurrOrgTypeList())
                {
                    orgType.setOrgId(organization.getOrgId());
                    orgType.setBatchNumber(batchNumber);
                    orgType.add();
                }
                result.put("orgId", organization.getOrgId());
                result.put("orgCode", organization.getOrgCode());
                result.put("orgUuid", organization.getUuid());
                result.put("orgFixId", organization.getOrgFixId());
                result.put("orgName", organization.getOrgName());
                saveBusinessOutletsResultList.add(result);
            }
        }
        return saveBusinessOutletsResultList;

    }

    /**
     * 根据组织名和组织类型称查找代理商组织
     * 
     * @param orgName
     * @param orgType
     * @return
     */
    public Organization queryOrganizationByOrgNameAndOrgType(String orgName,
            String orgType)
    {
        return organizationDao.queryOrganizationByOrgNameAndOrgType(orgName,
                orgType);
    }

    /**
     * 根据组织名和组织类型称查找代理商组织
     * 
     * @param orgName
     * @param notSearchOrgType
     * @return
     */
    public Organization queryOrganizationByOrgNameAndNotSearchOrgType(
            String orgName, String notSearchOrgType)
    {
        return organizationDao.queryOrganizationByOrgNameAndNotSearchOrgType(
                orgName, notSearchOrgType);
    }

    /**
     * 根据组织编码获取代理商组织实体
     * 
     * @param orgCode
     * @return
     */
    public Organization getAgentByOrgCode(String orgCode)
    {
        return organizationDao.getAgentByOrgCode(orgCode);
    }

    /**
     * 根据组织编码获取内部经营实体
     * 
     * @param orgCode
     * @return
     */
    public Organization getIbeByOrgCode(String orgCode)
    {
        return organizationDao.getIbeByOrgCode(orgCode);
    }

    /**
     * 接口调用返回信息
     * 
     * @param newOrg
     * @param oldOrg
     * @param optType
     *            操作类型 1新增组织 2添加组织关系 3修改组织 4删除组织关系 5删除组织
     * @return msg
     */
    @Override
    public String getGridValid(Organization newOrg, Organization oldOrg,
            Organization relaOrg, String optType)
    {

        String msg = null;
        String json = null;
        String oldUrbanRuralId = null;
        String newUrbanRuralId = null;
        HttpResponse response = null;
        SystemMessageLog systemMessageLog = null;
        UomGridCountyLog uomGridCountyLog = new UomGridCountyLog();

        String msgId = EsbHeadUtil.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);
        String oipServiceCodeGrid = UomClassProvider
                .getIntfUrl("oipServiceCodeGrid");
        String oipHttpJsonUrl = UomClassProvider.getIntfUrl("oipHttpJsonUrl");

        if (StrUtil.isNullOrEmpty(oipHttpJsonUrl))
        {
            // 测试环境网格接口地址
            // oipHttpJsonUrl =
            // "http://134.64.110.172:9001/grid_service/data_service/gridinfo/gridValideService";
            // 生产环境F5网格接口地址
            oipHttpJsonUrl = "http://134.64.106.187:8000/grid_service/data_service/gridinfo/gridValideService";
        }
        else
        {
            oipHttpJsonUrl += EsbHeadUtil.getUrlParameter(true,
                    EsbHeadUtil.FTP_SENDER, oipServiceCodeGrid, msgId, msgId);
        }

        ContractRootRequest contractRootRequest = new ContractRootRequest();
        ContractRootResponse contractRootResponse = new ContractRootResponse();

        RootRequest rootRequest = new RootRequest();
        RootResponse rootResponse = new RootResponse();

        TcpContRequest tcpContRequest = new TcpContRequest();
        TcpContResponse tcpContResponse = new TcpContResponse();

        SvcContRequest svcContRequest = new SvcContRequest();
        SvcContResponse svcContResponse = new SvcContResponse();

        GridRequest gridRequest = new GridRequest();
        GridResponse gridResponse = new GridResponse();

        tcpContRequest.setSystemCode("DIC_CRM");
        tcpContRequest.setTransactionID(msgId);
        tcpContRequest.setReqTime(DateUtil.dateToStr(new Date(),
                "yyyyMMddHHmmss"));
        tcpContRequest.setAction(optType);

        if (!StrUtil.isNullOrEmpty(newOrg))
        {
            gridRequest.setLatnId(newOrg.getTelcomRegion().getAreaCode());
            gridRequest.setUomOrgId(newOrg.getOrgId());
            gridRequest.setUomUuid(newOrg.getUuid());
            gridRequest.setGridId(newOrg.getEdaCode());
            gridRequest.setOptType(optType);
            uomGridCountyLog.setOrgId(newOrg.getOrgId());

            List<OrganizationExtendAttr> organizationExtendAttrList = newOrg
                    .getOrganizationExtendAttrList();

            if (!StrUtil.isNullOrEmpty(organizationExtendAttrList)
                    && organizationExtendAttrList.size() > 0)
            {

                for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList)
                {

                    if (OrganizationConstant.ORG_ATTR_SPEC_ID_8 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        gridRequest.setGridTypeNew(organizationExtendAttr
                                .getOrgAttrValue());
                    }
                    else if (OrganizationConstant.ORG_ATTR_SPEC_ID_9 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        gridRequest.setGridSubTypeNew(organizationExtendAttr
                                .getOrgAttrValue());
                    }
                    else if (OrganizationConstant.ORG_ATTR_SPEC_ID_10 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        gridRequest.setMktChannelIdNew(organizationExtendAttr
                                .getOrgAttrValue());
                    }
                    else if (OrganizationConstant.ORG_ATTR_SPEC_ID_11 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        newUrbanRuralId = organizationExtendAttr
                                .getOrgAttrValue();
                    }

                }

            }

        }

        if (!StrUtil.isNullOrEmpty(oldOrg))
        {
            List<OrganizationExtendAttr> organizationExtendAttrList = oldOrg
                    .getOldOrganizationExtendAttrList();

            if (!StrUtil.isNullOrEmpty(organizationExtendAttrList)
                    && organizationExtendAttrList.size() > 0)
            {

                for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList)
                {

                    if (OrganizationConstant.ORG_ATTR_SPEC_ID_8 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        gridRequest.setGridType(organizationExtendAttr
                                .getOrgAttrValue());
                    }
                    else if (OrganizationConstant.ORG_ATTR_SPEC_ID_9 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        gridRequest.setGridSubType(organizationExtendAttr
                                .getOrgAttrValue());
                    }
                    else if (OrganizationConstant.ORG_ATTR_SPEC_ID_10 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        gridRequest.setMktChannelId(organizationExtendAttr
                                .getOrgAttrValue());
                    }
                    else if (OrganizationConstant.ORG_ATTR_SPEC_ID_11 == organizationExtendAttr
                            .getOrgAttrSpecId())
                    {
                        oldUrbanRuralId = organizationExtendAttr
                                .getOrgAttrValue();
                    }

                }

            }
        }

        if (!StrUtil.isNullOrEmpty(relaOrg))
        {
            gridRequest.setUomOrgId(relaOrg.getOrgId());
            gridRequest.setUomUuid(relaOrg.getUuid());
        }

        if (OrganizationConstant.OPT_TYPE_3.equals(optType))
        {

            // 检测城乡属性是否修改，如果修改且在中间稽核表中存在，则不允许修改
            if ((!StrUtil.isEmpty(oldUrbanRuralId) && StrUtil
                    .isEmpty(newUrbanRuralId))
                    || (StrUtil.isEmpty(oldUrbanRuralId) && !StrUtil
                            .isEmpty(newUrbanRuralId))
                    || ((!StrUtil.isEmpty(oldUrbanRuralId) && !StrUtil
                            .isEmpty(newUrbanRuralId)) && (!oldUrbanRuralId
                            .equals(newUrbanRuralId))))
            {

                uomGridCountyLog
                        .setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_11);

                List<UomGridCountyLog> uomGridCountyLogList = organizationDao
                        .queryUomGridCountyLogByOrgIdList(uomGridCountyLog);

                if (uomGridCountyLogList != null
                        && uomGridCountyLogList.size() > 0)
                {

                    msg = "稽核中间表存在此网格，不允许修改该网格的城乡属性。";

                    return msg;
                }
            }

            // 如果网格大类、网格小类和客户群都没有修改就不做网格接口校验
            if (gridRequest.getGridType().equals(gridRequest.getGridTypeNew())
                    && gridRequest.getGridSubType().equals(
                            gridRequest.getGridSubTypeNew())
                    && gridRequest.getMktChannelId().equals(
                            gridRequest.getMktChannelIdNew()))
            {
                return msg;
            }

        }

        svcContRequest.setParam(gridRequest);
        rootRequest.setTcpCont(tcpContRequest);
        rootRequest.setSvcCont(svcContRequest);
        contractRootRequest.setContractRoot(rootRequest);

        try
        {

            json = JacksonUtil.Object2JSon(contractRootRequest);

            response = JacksonUtil.httpPostRequest(oipHttpJsonUrl, json);

            json = JacksonUtil.httpPostResponse(response);

            if (!StrUtil.isNullOrEmpty(json))
            {

                contractRootResponse = (ContractRootResponse) JacksonUtil
                        .JSon2Object(json, ContractRootResponse.class);

                if (!StrUtil.isNullOrEmpty(contractRootResponse))
                {

                    // if (contractRootResponse
                    // .getContractRoot()
                    // .getTcpCont()
                    // .getBusCode()
                    // .equals(OrganizationConstant.TCP_CONT_RESPONSE_BUS_CODE))
                    // {

                    if (contractRootResponse
                            .getContractRoot()
                            .getSvcCont()
                            .getParam()
                            .getResultCode()
                            .equals(OrganizationConstant.GRID_RESPONSE_RESULT_CODE_0000))
                    {

                        systemMessageLog = new SystemMessageLog();
                        systemMessageLog
                                .setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);// 全息网格接口编码
                        systemMessageLog.setResult(1L);
                        systemMessageLog.setErrCode(contractRootResponse
                                .getContractRoot().getSvcCont().getParam()
                                .getResultCode());
                        systemMessageLog.setSystemMessageInfo("消息ID："
                                + msgId
                                + " 组织ID："
                                + newOrg.getOrgId()
                                + " 反馈信息："
                                + msg
                                + contractRootResponse.getContractRoot()
                                        .getSvcCont().getParam()
                                        .getResultDesc());
                        systemMessageLog.setCreateDate(new Date());
                        systemMessageLogManager
                                .saveSystemMessageLog(systemMessageLog);

                    }
                    else
                    {

                        msg = contractRootResponse.getContractRoot()
                                .getSvcCont().getParam().getResultDesc();
                        systemMessageLog = new SystemMessageLog();
                        systemMessageLog
                                .setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);// 全息网格接口编码
                        systemMessageLog.setResult(0L);
                        systemMessageLog.setErrCode(contractRootResponse
                                .getContractRoot().getSvcCont().getParam()
                                .getResultCode());
                        systemMessageLog
                                .setSystemMessageInfo("消息ID：" + msgId
                                        + " 组织ID：" + newOrg.getOrgId()
                                        + " 反馈信息：" + msg);
                        systemMessageLog.setCreateDate(new Date());
                        systemMessageLogManager
                                .saveSystemMessageLog(systemMessageLog);

                    }

                    // } else {
                    //
                    // msg = contractRootResponse.getContractRoot()
                    // .getTcpCont().getBusDesc();
                    // systemMessageLog = new SystemMessageLog();
                    // systemMessageLog
                    // .setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);//
                    // 全息网格接口编码
                    // systemMessageLog.setResult(0L);
                    // systemMessageLog.setErrCode(contractRootResponse
                    // .getContractRoot().getTcpCont().getBusCode());
                    // systemMessageLog
                    // .setSystemMessageInfo("消息ID：" + msgId
                    // + " 组织ID：" + newOrg.getOrgId()
                    // + " 反馈信息：" + msg);
                    // systemMessageLog.setCreateDate(new Date());
                    // systemMessageLogManager
                    // .saveSystemMessageLog(systemMessageLog);
                    //
                    // }

                }
                else
                {
                    msg = "JSON转换后的对象为空";
                    systemMessageLog = new SystemMessageLog();
                    systemMessageLog
                            .setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);// 全息网格接口编码
                    systemMessageLog.setResult(0L);
                    systemMessageLog.setSystemMessageInfo("消息ID：" + msgId
                            + " 组织ID：" + newOrg.getOrgId() + " 反馈信息：" + msg);
                    systemMessageLog.setCreateDate(new Date());
                    systemMessageLogManager
                            .saveSystemMessageLog(systemMessageLog);
                }

            }
            else
            {

                msg = "网格全息返回的JSON为空";
                systemMessageLog = new SystemMessageLog();
                systemMessageLog
                        .setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);// 全息网格接口编码
                systemMessageLog.setResult(0L);
                systemMessageLog.setSystemMessageInfo("消息ID：" + msgId
                        + " 组织ID：" + newOrg.getOrgId() + " 反馈信息：" + msg);
                systemMessageLog.setCreateDate(new Date());
                systemMessageLogManager.saveSystemMessageLog(systemMessageLog);

            }

        }
        catch (JsonGenerationException e)
        {

            systemMessageLog = new SystemMessageLog();
            systemMessageLog.setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);// 全息网格接口编码
            systemMessageLog.setResult(0L);
            systemMessageLog.setSystemMessageInfo("消息ID：" + msgId + " 组织ID："
                    + newOrg.getOrgId() + " 反馈信息：" + msg);
            systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
                    .getMessage() : e.getMessage().substring(0, 500));
            systemMessageLog.setCreateDate(new Date());
            systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
            return e.getMessage();

        }
        catch (JsonMappingException e)
        {

            systemMessageLog = new SystemMessageLog();
            systemMessageLog.setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);// 全息网格接口编码
            systemMessageLog.setResult(0L);
            systemMessageLog.setSystemMessageInfo("消息ID：" + msgId + " 组织ID："
                    + newOrg.getOrgId() + " 反馈信息：" + msg);
            systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
                    .getMessage() : e.getMessage().substring(0, 500));
            systemMessageLog.setCreateDate(new Date());
            systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
            return e.getMessage();

        }
        catch (IOException e)
        {

            systemMessageLog = new SystemMessageLog();
            systemMessageLog.setSystemCode(WsConstants.SYSTEM_CODE_UOM_GRID);// 全息网格接口编码
            systemMessageLog.setResult(0L);
            systemMessageLog.setSystemMessageInfo("消息ID：" + msgId + " 组织ID："
                    + newOrg.getOrgId() + " 反馈信息：" + msg);
            systemMessageLog.setErrCode(e.getMessage().length() <= 500 ? e
                    .getMessage() : e.getMessage().substring(0, 500));
            systemMessageLog.setCreateDate(new Date());
            systemMessageLogManager.saveSystemMessageLog(systemMessageLog);
            return e.getMessage();

        }

        return msg;

    }

    @Override
    public void addOrgNetwork(Organization organization)
    {
        String batchNumber = OperateLog.gennerateBatchNumber();
        organization.setOrgCode(organization.generateOrgCode());
        organization.setUuid(StrUtil.getUUID());
        organization.setBatchNumber(batchNumber);
        organization.add();
        // 保存组织类型
        List<OrgType> addOrgTypeList = organization.getAddOrgTypeList();
        if (addOrgTypeList != null && addOrgTypeList.size() > 0)
        {
            for (OrgType orgType : addOrgTypeList)
            {
                orgType.setOrgId(organization.getOrgId());
                orgType.setBatchNumber(batchNumber);
                orgType.add();
            }
        }
        // 保存组织关系
        List<OrganizationRelation> addOrgRelaList = organization
                .getOrganizationRelationList();
        if (addOrgRelaList != null && addOrgRelaList.size() > 0)
        {
            for (OrganizationRelation orgRela : addOrgRelaList)
            {
                orgRela.setOrgId(organization.getOrgId());
                orgRela.setBatchNumber(batchNumber);
                orgRela.add();
            }
        }

    }

    @Override
    public void updateOrgNetwork(Organization organization)
    {

        String batchNumber = OperateLog.gennerateBatchNumber();
        String orgCode = organization.generateOrgCode();
        if (!StrUtil.isEmpty(orgCode))
        {
            organization.setOrgCode(orgCode);
        }
        organization.setBatchNumber(batchNumber);
        // 修改组织名称
        if (organization.getIsChangeOrgName())
        {
            this.updateOrgFullNameOnCascade(organization);
        }
        organization.update();

        List<OrganizationRelation> delOrs = organization
                .getDelOrganizationRelationList();
        if (delOrs != null && delOrs.size() > 0)
        {
            for (OrganizationRelation or : delOrs)
            {
                or.setBatchNumber(batchNumber);
                or.remove();
            }
        }
        List<OrganizationRelation> addOrs = organization
                .getAddOrganizationRelationList();
        if (addOrs != null && addOrs.size() > 0)
        {
            for (OrganizationRelation or : addOrs)
            {
                or.setOrgId(organization.getOrgId());
                or.setBatchNumber(batchNumber);
                or.add();
            }
        }
        List<OrgType> delOrgTypeList = organization.getDelOrgTypeList();
        if (delOrgTypeList != null && delOrgTypeList.size() > 0)
        {
            for (OrgType orgType : delOrgTypeList)
            {
                orgType.setBatchNumber(batchNumber);
                orgType.remove();
            }
        }
        List<OrgType> addOrgTypeList = organization.getAddOrgTypeList();
        if (addOrgTypeList != null && addOrgTypeList.size() > 0)
        {
            for (OrgType orgType : addOrgTypeList)
            {
                orgType.setOrgId(organization.getOrgId());
                orgType.setBatchNumber(batchNumber);
                orgType.add();
            }
        }

    }

    @Override
    public void updateOrganizationExtendAttr(Organization organization)
    {
        String batchNumber = OperateLog.gennerateBatchNumber();
        organization.setBatchNumber(batchNumber);

        OrganizationExtendAttr organizationExtendAttr = new OrganizationExtendAttr();
        organizationExtendAttr.setOrgId(organization.getOrgId());
        organizationExtendAttr
                .setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);

        organizationExtendAttr = organizationExtendAttrManager
                .queryOrganizationExtendAttr(organizationExtendAttr);

        if (organizationExtendAttr != null)
        {
            organizationExtendAttr.setOrgAttrValue(organization
                    .getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER));
            organizationExtendAttr.update();
        }
        else
        {
            OrganizationExtendAttr newOrganizationExtendAttr = new OrganizationExtendAttr();
            newOrganizationExtendAttr.setOrgId(organization.getOrgId());
            newOrganizationExtendAttr
                    .setOrgAttrSpecId(OrganizationConstant.ORG_ATTR_SPEC_ID_19);
            newOrganizationExtendAttr.setOrgAttrValue(organization
                    .getGroupMailOrgCode(OrganizationConstant.RELA_CD_INNER));
            newOrganizationExtendAttr.add();
        }
    }

    @Override
    public void delOrgNetwork(Organization organization)
    {

        String batchNumber = OperateLog.gennerateBatchNumber();
        organization.setBatchNumber(batchNumber);
        List<OrganizationRelation> organizationRelationList = organization
                .getOrganizationRelationList();
        if (organizationRelationList != null
                && organizationRelationList.size() > 0)
        {
            for (OrganizationRelation organizationRelation : organizationRelationList)
            {
                organizationRelation.setBatchNumber(batchNumber);
                organizationRelation.remove();
            }
        }

        List<OrgType> orgTypeList = organization.getOrgTypeList();
        if (orgTypeList != null && orgTypeList.size() > 0)
        {
            for (OrgType orgType : orgTypeList)
            {
                orgType.setBatchNumber(batchNumber);
                orgType.remove();
            }
        }
        organization.remove();

    }

    @Override
    public String getDoValidOrganizationExtendAttrGrid(Organization organization)
    {

        String msg = null;

        List<OrganizationExtendAttr> organizationExtendAttrList = organization
                .getOrganizationExtendAttrList();

        if (organizationExtendAttrList != null
                && organizationExtendAttrList.size() > 0)
        {

            for (OrganizationExtendAttr organizationExtendAttr : organizationExtendAttrList)
            {

                if (organizationExtendAttr.getOrgAttrSpecId() == 8L
                        && StrUtil.isEmpty(organizationExtendAttr
                                .getOrgAttrValue()))
                {
                    msg = "网格类型不能为空";
                    return msg;
                }

                if (organizationExtendAttr.getOrgAttrSpecId() == 9L
                        && StrUtil.isEmpty(organizationExtendAttr
                                .getOrgAttrValue()))
                {
                    msg = "网格小类不能为空";
                    return msg;
                }

                if (organizationExtendAttr.getOrgAttrSpecId() == 10L
                        && StrUtil.isEmpty(organizationExtendAttr
                                .getOrgAttrValue()))
                {
                    msg = "客户群不能为空";
                    return msg;
                }

            }

        }
        else
        {
            msg = "请填写网格属性";
        }

        return msg;
    }

    @Override
    public int checkUpLoadHXData(List<String> levalList,
            List<OrganizationHXImportVo> waitUpLoadHXOrganizationInfoList,
            List<String> checkInfoList, String[][] objDataArray,
            int totalColumn, int[] caseIndex, String markingType) throws Exception
    {
        // 用来校验划小组织的数据
        int errorDataCount = 0;
        StringBuffer sb = new StringBuffer();
        
        /**
         * 网格类型-级联变动
         */
        Long orgAttrSpecId8 = 8L;

        /**
         * 网格小类-级联变动
         */
        Long orgAttrSpecId9 = 9L;

        /**
         * 客户群-级联变动
         */
        Long orgAttrSpecId10 = 10L;
        
        /**
         * 城乡
         */
        Long orgAttrSpecId11 = 11L;
        // 遍历每条数据
        for (int i = 0; i < objDataArray.length; i++)
        {
            // 划小组织关系列表
            List<OrganizationRelation> organizationHXRelationList = new ArrayList<OrganizationRelation>();
            // 划小组织关系
            OrganizationRelation organizationHXRelation = OrganizationRelation
                    .newInstance();
            // 划小组织关系类型列表
            List<OrgType> orgHXTypeList = new ArrayList<OrgType>();
            // 划小组织关系类型
            OrgType orgHXType = new OrgType();
            // 划小组织
            Organization orgHX = Organization.newInstance();
            
            //网格扩展信息
            OrganizationExtendAttr organizationExtendAttr8 = new OrganizationExtendAttr();  //网格类型
            OrganizationExtendAttr organizationExtendAttr9 = new OrganizationExtendAttr();  //网格小类
            OrganizationExtendAttr organizationExtendAttr10 = new OrganizationExtendAttr(); //客户群
            OrganizationExtendAttr organizationExtendAttr11 = new OrganizationExtendAttr(); //城乡属性
            //属性列表
            List<OrganizationExtendAttr> organizationExtendAttrList = new ArrayList<OrganizationExtendAttr>();
            
            //划小组织vo，包含网格扩展信息
            OrganizationHXImportVo organizationHXImportVo = new OrganizationHXImportVo();
            
            
            // 遍历所有的列
            //当前数据的等级
            int level = 0;
            for (int j = 0; j < totalColumn; j++)
            {
                String str = "";
                if (null != objDataArray[i][j])
                {
                    // 获取一条数据
                    str = objDataArray[i][j].trim();
                }
                String[] strs = null;
                if (!StrUtil.isNullOrEmpty(str))
                {
                    strs = str.split("-");
                }
                
                switch (caseIndex[j])
                {
                	case 0://划小单元变更原因
                	 if (!StrUtil.isNullOrEmpty(str))
                     {
                         // 设置划小单元的名字
                         orgHX.setReason(str);
                     }
                     else
                     {
                         errorDataCount++;
                         // 值位null或者空
                         checkInfoList.add(getValidateMsg(i, j,
                                 OrganizationConstant.NULL_OR_EMPTY, sb));
                     }
                     break;
                    case 1:// 划小单元名称
                        if (!StrUtil.isNullOrEmpty(str))
                        {
                            // 设置划小单元的名字
                            orgHX.setOrgName(str);
                        }
                        else
                        {
                            errorDataCount++;
                            // 值位null或者空
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 2:// 划小单元上级编码,这里注意划小组织在第五级开始
                            // 如果是数字的话，那么就执行逻辑
                        if (StringUtils.isNumeric(str))
                        {
                            // 判断数据库中是否有这个组织的编码，用这个划小作为下级，这个编码是上级的
                            Organization tempOrg = organizationDao
                                    .queryOrganizationByOrgCode(str);

                            if (tempOrg != null)
                            {
                                // 从根节点聚合营销2016根节点开始为第0层，往下层层递进
                                // 数据库中存在这个上级，判断是否是在第四级以下，上级就只能是4,5,6
                                // 那么数据深度最多只能插入到5,6,7级节点
                                Integer levelInt = getMarketingTreeLevel(tempOrg.getOrgId());
                                if (levelInt == null) {
                                    // 上级组织不存在
                                    errorDataCount++;
                                    checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_NOT_EXIST, sb));
                                } else {
                                    // 报错等级
                                    level = levelInt;
                                    levalList.add(String.valueOf(level));
                                    if (level >= 4 && level <= 6) {
                                        // 表示校验通过,那就把组织关系添加进去
                                        if(markingType.equals("NewMarketing"))
                                        {
                                            organizationHXRelation
                                            .setRelaCd(OrganizationRelationConstant.RELA_CD_MARKETING_2016);
                                        }
                                        else if(markingType.equals("NewSeventeenMarketing"))
                                        {
                                            organizationHXRelation
                                            .setRelaCd(OrganizationRelationConstant.RELA_CD_MARKETING_2017);
                                        }
                                        organizationHXRelation.setRelaOrgId(tempOrg.getOrgId());
                                        // organizationHXRelation
                                        // .setRelaCd(OrganizationRelationConstant.RELA_CD_MARKETING_2016);
                                        organizationHXRelationList.add(organizationHXRelation);
                                        
                                        // 上级区域编码
                                        if (tempOrg.getAreaCodeId() != null)
                                            orgHX.setAreaCodeId(tempOrg.getAreaCodeId());
                                    } else {
                                        errorDataCount++;
                                        checkInfoList.add(getValidateMsg(i, j,
                                            OrganizationConstant.FIELD_NOT_IN_REIGHT_SITE, sb));
                                    }
                                }
                            }
                            else
                            {
                                // 上级组织不存在
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_NOT_EXIST,
                                        sb));
                            }
                        }
                        else
                        {
                            /*
                             * "格式不正确";
                             */
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.FIELD_ERROR, sb));
                        }
                        break;
                    case 3:// 划小组织性质
                        if (!StrUtil.isEmpty(str))
                        {
                            //只能是1000，或者1100分别是内部组织和外部组织
                            if(strs[1].equals("1000") || strs[1].equals("1100"))
                                orgHX.setOrgType(strs[1]);
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_ERROR, sb));
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 4:// 划小存在类型
                        if (!StrUtil.isEmpty(str))
                        {
                            //只能是1，或者2分别是实体组织或虚拟团队
                            if(strs[1].equals("1") || strs[1].equals("2"))
                                orgHX.setExistType(strs[1]);
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_ERROR, sb));
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 5:// 划小组织类型
                        if (!StrUtil.isEmpty(str))
                        {// 这个是网格(2015)-N1101050000
                            if (OrganizationConstant.ORG_TYPE_N1101050000
                                    .equals(strs[1]) && level == 6)
                            {
                                orgHXType.setOrgTypeCd(strs[1]);
                                orgHXTypeList.add(orgHXType);
                            }
                            else if (OrganizationConstant.ORG_TYPE_N1101040000
                                    .equals(strs[1]) && level != 6)
                            {// 这个是收入单元2015
                                orgHXType.setOrgTypeCd(strs[1]);
                                orgHXTypeList.add(orgHXType);
                            }
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_NOT_IN_SEVENT_SITE,
                                        sb));
                            }
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 6:
                        if (!StrUtil.isNullOrEmpty(str) && strs.length > 1)
                        {
                            //只能是1，或者2分别是城镇和农村
                            if(strs[1].equals("1") || strs[1].equals("2"))
                                orgHX.setCityTown(strs[1]);
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FIELD_ERROR, sb));
                            }
                        }
                        break;
                    case 7:// 划小组织排序
                        if (!StrUtil.isEmpty(str))
                        {
                            orgHX.setOrgPriority(Long.valueOf(str));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 8:// 划小组织电信管理区域
                        if (!StrUtil.isEmpty(str))
                        {
                            orgHX.setTelcomRegionId(Long
                                    .valueOf(strs[1]));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 9:// 划小行政管理区域
                        if (!StrUtil.isEmpty(str))
                        {
                            orgHX.setLocationId(Long
                                    .valueOf(strs[1]));
                        }
                        else
                        {
                            errorDataCount++;
                            checkInfoList.add(getValidateMsg(i, j,
                                    OrganizationConstant.NULL_OR_EMPTY, sb));
                        }
                        break;
                    case 10: //网格类型(网格组织类型专用)网格类型spec_id=8 
                        //首先必须是网格类型才可以 
                        if(level == 6)
                        {
                            if(!StrUtil.isEmpty(str))
                            {
                                //如果不为空，那就吧值拿过来
                                organizationExtendAttr8.setOrgAttrSpecId(orgAttrSpecId8);
                                organizationExtendAttr8.setOrgAttrValue(strs[1]);
                            }
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.NULL_OR_EMPTY, sb));
                            }
                        }
                        else
                        {
                            //如果不是第七级网格，但是设定的数据，那么久报错
                            if(!StrUtil.isEmpty(str))
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FILE_NOT_GRID, sb));
                            }
                        }
                        break;
                    case 11: //网格小类(网格组织类型专用)网格小类spec_id=9
                        //首先必须是网格类型才可以
                        if(level == 6)
                        {
                            if(!StrUtil.isEmpty(str))
                            {
                                //上级组织是第6级，而且不为空，那么就可以添加了,而且要级联对应
                                //实网格11:政企-1110 校园-1120   公众-1130
                                //虚网格-12:渠道-1210  电渠-1220  全员营销-1230
                                //营业部标签产品网格-13:政企标签产品-1310  校园标签产品-1320，这还要判断id不为空
                                if (organizationExtendAttr8 != null
                                    && organizationExtendAttr8.getOrgAttrSpecId() != null
                                    && organizationExtendAttr8.getOrgAttrValue() != null) {
                                    // 如果不为空，那就吧值拿过来
                                    organizationExtendAttr9.setOrgAttrSpecId(orgAttrSpecId9);
                                    // 获取网格类型
                                    int gridType = Integer.valueOf(organizationExtendAttr8
                                        .getOrgAttrValue());
                                    switch (gridType) {
                                        case 11: // 实网格11:政企-1110 校园-1120
                                                 // 公众-1130
                                            if (strs[1].equals("1110") || strs[1].equals("1120")
                                                || strs[1].equals("1130")) {
                                                organizationExtendAttr9.setOrgAttrValue(strs[1]);
                                            } else {
                                                // 网格类型和网格小类不匹配
                                                errorDataCount++;
                                                checkInfoList.add(getValidateMsg(i, j,
                                                    OrganizationConstant.FILE_NOT_GRID, sb));
                                            }
                                            break;
                                        case 12: // 虚网格-12:渠道-1210 电渠-1220
                                                 // 全员营销-1230
                                            if (strs[1].equals("1210") || strs[1].equals("1220")
                                                || strs[1].equals("1230")) {
                                                organizationExtendAttr9.setOrgAttrValue(strs[1]);
                                            } else {
                                                // 网格类型和网格小类不匹配
                                                errorDataCount++;
                                                checkInfoList.add(getValidateMsg(i, j,
                                                    OrganizationConstant.FILE_NOT_GRID, sb));
                                            }
                                            break;
                                        case 13: // 营业部标签产品网格-13:政企标签产品-1310
                                                 // 校园标签产品-1320
                                            if (strs[1].equals("1310") || strs[1].equals("1320")) {
                                                organizationExtendAttr9.setOrgAttrValue(strs[1]);
                                            } else {
                                                // 网格类型和网格小类不匹配
                                                errorDataCount++;
                                                checkInfoList.add(getValidateMsg(i, j,
                                                    OrganizationConstant.FILE_NOT_GRID, sb));
                                            }
                                            break;
                                        default: // 没有对应的类型，只能是以上三个
                                            errorDataCount++;
                                            checkInfoList.add(getValidateMsg(i, j,
                                                OrganizationConstant.FIELD_ERROR, sb));
                                            break;
                                    }
                                    
                                    // 这里开始推导客户群类型政企-1000 公众-1100 校园-1300
                                    // 电渠-1400
                                    organizationExtendAttr10.setOrgAttrSpecId(orgAttrSpecId10);
                                    // 设定值,先从数据库查询出级联关系
                                    CascadeRelation cascadeRelation = new CascadeRelation();
                                    cascadeRelation.setRelaCd(CascadeRelationConstants.RELA_CD_1);
                                    cascadeRelation.setCascadeValue(organizationExtendAttr9
                                        .getOrgAttrValue());
                                    
                                    List<CascadeRelation> cascadeRelationList = cascadeRelationManager
                                        .queryCascadeRelationList(cascadeRelation);
                                    // 获取对应的值，类似1000政企，1100 公众， 1300 校园， 1400 电渠
                                    organizationExtendAttr10.setOrgAttrValue(cascadeRelationList
                                        .get(0).getRelaCascadeValue());
                                }
                            }
                            else
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.NULL_OR_EMPTY, sb));
                            }
                        }
                        else
                        {
                            //如果不是第七级网格，但是设定的数据，那么久报错
                            if(!StrUtil.isEmpty(str))
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FILE_NOT_GRID, sb));
                            }
                        }
                        break;
                    case 12: //城乡属性(网格组织类型专用)城乡属性spec_id=11
                        //首先必须是网格类型才可以
                        if(level == 6)
                        {
                            if(!StrUtil.isEmpty(str))
                            {
                                organizationExtendAttr11.setOrgAttrSpecId(orgAttrSpecId11);
                                organizationExtendAttr11.setOrgAttrValue(strs[1]);
                            }
                            
                        }
                        else
                        {
                            //如果不是第七级网格，但是设定的数据，那么久报错
                            if(!StrUtil.isEmpty(str))
                            {
                                errorDataCount++;
                                checkInfoList.add(getValidateMsg(i, j,
                                        OrganizationConstant.FILE_NOT_GRID, sb));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            // 由于是营销树下面的划小单元，那么relation中的rela_cd为0402
            //划小组织，的vo（包括对网格的扩展属性）
            orgHX.setOrgTypeList(orgHXTypeList);
            orgHX.setOrganizationRelationList(organizationHXRelationList);
            organizationHXImportVo.setHxOrganization(orgHX);
            organizationExtendAttrList.add(organizationExtendAttr8);
            organizationExtendAttrList.add(organizationExtendAttr9);
            organizationExtendAttrList.add(organizationExtendAttr10);
            organizationExtendAttrList.add(organizationExtendAttr11);
            organizationHXImportVo.setHxOrganizationExtendAttrList(organizationExtendAttrList);
            waitUpLoadHXOrganizationInfoList.add(organizationHXImportVo);

        }
        return errorDataCount;

    }

    @Override
    public Integer getMarketingTreeLevel(Long orgId)
    {
        String sql = "select level - 1 from ( select o.org_name, orgo.* from organization o, "
                + "organization_relation orgo where o.org_id = orgo.org_id and o.status_cd = ? "
                + "and orgo.status_cd = ? and orgo.rela_cd = ?) new_tab where new_tab.org_id = ?"
                + " start with new_tab.org_id = ? "
                + "connect by prior new_tab.org_id = new_tab.rela_org_id";
        List params = new ArrayList();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(OrganizationRelationConstant.RELA_CD_MARKETING_2016);
        params.add(orgId);
        params.add(OrganizationConstant.ROOT_NEW_MARKETING_ORG_ID);
        String level = Organization.repository()
                .getSingleValueByJDBCAndParamsSQL(sql, params);

        if (!StrUtil.isEmpty(level))
        {
            return new Integer(level);
        }
        return null;
    }

}
