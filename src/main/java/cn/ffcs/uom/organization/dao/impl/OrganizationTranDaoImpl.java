package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.dao.OrganizationTranDao;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;

@Repository("organizationTranDao")
public class OrganizationTranDaoImpl extends BaseDaoImpl implements
		OrganizationTranDao {

	@Override
	public PageInfo queryPageInfoByOrganizationTran(
			OrganizationTran organizationTran, int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		if (organizationTran != null) {
			sb.append("SELECT OT.ORG_TRAN_ID,ORG.ORG_ID,ORG.ORG_NAME,ORG.ORG_CODE,TORG.ORG_ID AS TRAN_ORG_ID,TORG.ORG_NAME AS TRAN_ORG_NAME,");
			sb.append("TORG.ORG_CODE AS TRAN_ORG_CODE,OT.TRAN_RELA_TYPE,OT.STATUS_CD,OT.EFF_DATE,OT.EXP_DATE FROM (");

			sb.append(" SELECT ORG.* FROM ( SELECT ORG.* FROM ORGANIZATION ORG");
			sb.append(" WHERE ORG.STATUS_CD = ?");
			sb.append(" AND EXISTS ( SELECT DISTINCT OT.ORG_ID FROM ORG_TYPE OT");
			sb.append(" WHERE OT.STATUS_CD = ?");
			sb.append(" AND ORG.ORG_ID = OT.ORG_ID");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (organizationTran.getQueryOrgTypeList() != null
					&& organizationTran.getQueryOrgTypeList().size() > 0) {
				sb.append(" AND OT.ORG_TYPE_CD IN ( ");
				for (int i = 0; i < organizationTran.getQueryOrgTypeList()
						.size(); i++) {
					if (i != 0) {
						sb.append(",");
					}
					sb.append("'")
							.append(organizationTran.getQueryOrgTypeList()
									.get(i).getOrgTypeCd()).append("'");
				}
				sb.append(" )");
			}

			sb.append(" ) ) ORG");

			if (organizationTran.getTelcomRegionId() != null) {// 管理区域
				sb.append(" ,( SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR");
				sb.append(" WHERE TR.STATUS_CD = ?");
				sb.append(" START WITH TR.TELCOM_REGION_ID = ?");
				sb.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID ) TR");
				sb.append(" WHERE ORG.TELCOM_REGION_ID = TR.TELCOM_REGION_ID");

				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(organizationTran.getTelcomRegionId());

			}

			sb.append(" ) ORG");

			sb.append(" FULL JOIN ( SELECT OT.* FROM ORGANIZATION_TRAN OT WHERE OT.STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(organizationTran.getTranRelaType())) {
				sb.append(" AND OT.TRAN_RELA_TYPE = ?");
				params.add(organizationTran.getTranRelaType());
			}

			if (organizationTran.getTelcomRegionId() != null) {// 管理区域
				sb.append(" AND OT.ORG_ID IN ( SELECT ORG.ORG_ID FROM ( SELECT ORG.* FROM ORGANIZATION ORG");
				sb.append(" WHERE STATUS_CD = ? ) ORG");
				sb.append(" ,( SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR");
				sb.append(" WHERE TR.STATUS_CD = ?");
				sb.append(" START WITH TR.TELCOM_REGION_ID = ?");
				sb.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID ) TR");
				sb.append(" WHERE ORG.TELCOM_REGION_ID = TR.TELCOM_REGION_ID )");

				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(organizationTran.getTelcomRegionId());

				sb.append(" AND OT.TRAN_ORG_ID IN ( SELECT ORG.ORG_ID FROM ( SELECT ORG.* FROM ORGANIZATION ORG");
				sb.append(" WHERE STATUS_CD = ? ) ORG");
				sb.append(" ,( SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR");
				sb.append(" WHERE TR.STATUS_CD = ?");
				sb.append(" START WITH TR.TELCOM_REGION_ID = ?");
				sb.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID ) TR");
				sb.append(" WHERE ORG.TELCOM_REGION_ID = TR.TELCOM_REGION_ID )");

				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(organizationTran.getTelcomRegionId());

			}

			sb.append(" ) OT");
			sb.append(" ON ORG.ORG_ID = OT.ORG_ID");

			sb.append(" FULL JOIN (");
			sb.append(" SELECT TORG.* FROM ( SELECT TORG.* FROM ORGANIZATION TORG");
			sb.append(" WHERE TORG.STATUS_CD = ?");
			sb.append(" AND EXISTS ( SELECT DISTINCT OT.ORG_ID FROM ORG_TYPE OT");
			sb.append(" WHERE OT.STATUS_CD = ?");
			sb.append(" AND TORG.ORG_ID = OT.ORG_ID");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (organizationTran.getQueryTranOrgTypeList() != null
					&& organizationTran.getQueryTranOrgTypeList().size() > 0) {
				sb.append(" AND OT.ORG_TYPE_CD IN ( ");
				for (int i = 0; i < organizationTran.getQueryTranOrgTypeList()
						.size(); i++) {
					if (i != 0) {
						sb.append(",");
					}
					sb.append("'")
							.append(organizationTran.getQueryTranOrgTypeList()
									.get(i).getOrgTypeCd()).append("'");
				}
				sb.append(" )");
			}

			sb.append(" ) ) TORG");

			if (organizationTran.getTelcomRegionId() != null) {// 管理区域

				sb.append(" ,( SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR");
				sb.append(" WHERE TR.STATUS_CD = ?");
				sb.append(" START WITH TR.TELCOM_REGION_ID = ?");
				sb.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID ) TR");
				sb.append(" WHERE TORG.TELCOM_REGION_ID = TR.TELCOM_REGION_ID");

				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(organizationTran.getTelcomRegionId());

			}

			sb.append(" ) TORG");

			sb.append(" ON OT.TRAN_ORG_ID = TORG.ORG_ID");

			sb.append(" WHERE 1 = 1");

			if (!StrUtil.isNullOrEmpty(organizationTran.getOrgId())) {
				sb.append(" AND ORG.ORG_ID = ?");
				params.add(organizationTran.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getOrgName())) {
				sb.append(" AND ORG.ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(organizationTran.getOrgName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getOrgCode())) {
				sb.append(" AND ORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(organizationTran.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getOrgTranId())) {
				sb.append(" AND OT.ORG_TRAN_ID = ?");
				params.add(organizationTran.getOrgTranId());
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getTranOrgId())) {
				sb.append(" AND TORG.ORG_ID = ?");
				params.add(organizationTran.getTranOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getTranOrgName())) {
				sb.append(" AND TORG.ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(organizationTran.getTranOrgName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getTranOrgCode())) {
				sb.append(" AND TORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(organizationTran.getTranOrgCode()));
			}

			sb.append(" ORDER BY OT.ORG_TRAN_ID ASC");

		}

		return OrganizationTran.repository().jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, OrganizationTran.class);

	}

	@Override
	public List<OrganizationTran> queryOrganizationTranList(
			OrganizationTran organizationTran, String pattern) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		if (organizationTran != null) {

			sb.append("SELECT * FROM ORGANIZATION_TRAN WHERE STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(organizationTran.getOrgTranId())) {
				sb.append(" AND ORG_TRAN_ID = ?");
				params.add(organizationTran.getOrgTranId());
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(organizationTran.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getTranOrgId())) {
				sb.append(" AND TRAN_ORG_ID = ?");
				params.add(organizationTran.getTranOrgId());
			}

			if (!StrUtil.isEmpty(organizationTran.getTranRelaType())) {

				sb.append(" AND TRAN_RELA_TYPE LIKE ?");

				if (!StrUtil.isEmpty(pattern)) {

					if (pattern.equals("L")) {

						params.add("%" + StringEscapeUtils.escapeSql(organizationTran.getTranRelaType()));

					} else if (pattern.equals("R")) {

						params.add(StringEscapeUtils.escapeSql(organizationTran.getTranRelaType()
								.substring(0, 4)) + "%");

					} else {

						params.add(organizationTran.getTranRelaType());

					}

				} else {
					params.add(organizationTran.getTranRelaType());
				}
			}

			sb.append(" ORDER BY ORG_TRAN_ID ASC");

		}

		return super
				.jdbcFindList(sb.toString(), params, OrganizationTran.class);

	}

	/**
	 * 跨域内外业务关系分页查询
	 */
	@Override
	public PageInfo queryPageInfoByUomGroupOrgTran(
			UomGroupOrgTran uomGroupOrgTran, int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		if (uomGroupOrgTran != null) {
			sb.append("SELECT OT.ORG_TRAN_ID,ORG.ORG_ID,ORG.ORG_NAME,ORG.ORG_CODE,TORG.ORG_CODE AS TRAN_ORG_ID,TORG.ORG_NAME AS TRAN_ORG_NAME,");
			sb.append("TORG.ORG_CODE AS TRAN_ORG_CODE,TORG.SUPPLIER_NAME,TORG.SUPPLIER_CODE,OT.TRAN_RELA_TYPE,OT.STATUS_CD,OT.EFF_DATE,OT.EXP_DATE FROM (");

			sb.append(" SELECT ORG.* FROM ( SELECT ORG.* FROM ORGANIZATION ORG");
			sb.append(" WHERE ORG.STATUS_CD = ?");
			sb.append(" AND EXISTS ( SELECT DISTINCT OT.ORG_ID FROM ORG_TYPE OT");
			sb.append(" WHERE OT.STATUS_CD = ?");
			sb.append(" AND ORG.ORG_ID = OT.ORG_ID");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (uomGroupOrgTran.getQueryOrgTypeList() != null
					&& uomGroupOrgTran.getQueryOrgTypeList().size() > 0) {
				sb.append(" AND OT.ORG_TYPE_CD IN ( ");
				for (int i = 0; i < uomGroupOrgTran.getQueryOrgTypeList()
						.size(); i++) {
					if (i != 0) {
						sb.append(",");
					}
					sb.append("'")
							.append(uomGroupOrgTran.getQueryOrgTypeList()
									.get(i).getOrgTypeCd()).append("'");
				}
				sb.append(" )");
			}

			sb.append(" ) ) ORG");

			if (uomGroupOrgTran.getTelcomRegionId() != null) {// 管理区域
				sb.append(" ,( SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR");
				sb.append(" WHERE TR.STATUS_CD = ?");
				sb.append(" START WITH TR.TELCOM_REGION_ID = ?");
				sb.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID ) TR");
				sb.append(" WHERE ORG.TELCOM_REGION_ID = TR.TELCOM_REGION_ID");

				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(uomGroupOrgTran.getTelcomRegionId());

			}

			sb.append(" ) ORG");

			sb.append(" FULL JOIN ( SELECT OT.* FROM UOM_GROUP_ORGANIZATION_TRAN OT WHERE OT.STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {

				if (uomGroupOrgTran.getTranRelaType().length() == OrganizationTranConstant.ORGANIZATION_TRAN_PRE_LENGTH) {

					sb.append(" AND OT.TRAN_RELA_TYPE LIKE ?");

					params.add(uomGroupOrgTran.getTranRelaType() + "%");

				} else {

					sb.append(" AND OT.TRAN_RELA_TYPE = ?");

					params.add(uomGroupOrgTran.getTranRelaType());

				}

			}

			if (uomGroupOrgTran.getTelcomRegionId() != null) {// 管理区域
				sb.append(" AND OT.ORG_ID IN ( SELECT ORG.ORG_ID FROM ( SELECT ORG.* FROM ORGANIZATION ORG");
				sb.append(" WHERE STATUS_CD = ? ) ORG");
				sb.append(" ,( SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR");
				sb.append(" WHERE TR.STATUS_CD = ?");
				sb.append(" START WITH TR.TELCOM_REGION_ID = ?");
				sb.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID ) TR");
				sb.append(" WHERE ORG.TELCOM_REGION_ID = TR.TELCOM_REGION_ID )");

				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(uomGroupOrgTran.getTelcomRegionId());

			}

			sb.append(" AND OT.TRAN_ORG_ID IN ( SELECT VGO.ORG_CODE FROM V_GROUP_ORGANIZATION VGO");
			sb.append(" WHERE 1 = 1");

			if (uomGroupOrgTran.getQueryTranOrgTypeList() != null
					&& uomGroupOrgTran.getQueryTranOrgTypeList().size() > 0) {

				sb.append(" AND VGO.ORG_TYPE IN ( ");

				for (int i = 0; i < uomGroupOrgTran.getQueryTranOrgTypeList()
						.size(); i++) {

					if (i != 0) {

						sb.append(",");

					}

					sb.append("'")
							.append(uomGroupOrgTran.getQueryTranOrgTypeList()
									.get(i)).append("'");
				}

				sb.append(" )");

			}

			sb.append(" )");

			sb.append(" ) OT");
			sb.append(" ON ORG.ORG_ID = OT.ORG_ID");

			sb.append(" FULL JOIN (");

			sb.append(" SELECT VGO.* FROM V_GROUP_ORGANIZATION VGO");
			sb.append(" WHERE 1 = 1");

			if (uomGroupOrgTran.getQueryTranOrgTypeList() != null
					&& uomGroupOrgTran.getQueryTranOrgTypeList().size() > 0) {

				sb.append(" AND VGO.ORG_TYPE IN ( ");

				for (int i = 0; i < uomGroupOrgTran.getQueryTranOrgTypeList()
						.size(); i++) {

					if (i != 0) {

						sb.append(",");

					}

					sb.append("'")
							.append(uomGroupOrgTran.getQueryTranOrgTypeList()
									.get(i)).append("'");
				}

				sb.append(" )");

			}

			sb.append(" ) TORG");

			sb.append(" ON OT.TRAN_ORG_ID = TORG.ORG_CODE");

			sb.append(" WHERE 1 = 1");

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgId())) {
				sb.append(" AND ORG.ORG_ID = ?");
				params.add(uomGroupOrgTran.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgName())) {
				sb.append(" AND ORG.ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(uomGroupOrgTran.getOrgName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgCode())) {
				sb.append(" AND ORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(uomGroupOrgTran.getOrgCode()));
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgTranId())) {
				sb.append(" AND OT.ORG_TRAN_ID = ?");
				params.add(uomGroupOrgTran.getOrgTranId());
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getTranOrgId())) {
				sb.append(" AND TORG.ORG_CODE = ?");
				params.add(uomGroupOrgTran.getTranOrgId());
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getTranOrgName())) {
				sb.append(" AND TORG.ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(uomGroupOrgTran.getTranOrgName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getTranOrgCode())) {
				sb.append(" AND TORG.ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(uomGroupOrgTran.getTranOrgCode()));
			}

			sb.append(" ORDER BY OT.ORG_TRAN_ID ASC");

		}

		return UomGroupOrgTran.repository().jdbcFindPageInfo(sb.toString(),
				params, currentPage, pageSize, UomGroupOrgTran.class);

	}

	/**
	 * 跨域内外业务关系查询
	 */
	@Override
	public List<UomGroupOrgTran> queryUomGroupOrgTranList(
			UomGroupOrgTran uomGroupOrgTran, String pattern) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer();

		if (uomGroupOrgTran != null) {

			sb.append("SELECT * FROM UOM_GROUP_ORGANIZATION_TRAN WHERE STATUS_CD = ?");

			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgTranId())) {
				sb.append(" AND ORG_TRAN_ID = ?");
				params.add(uomGroupOrgTran.getOrgTranId());
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgId())) {
				sb.append(" AND ORG_ID = ?");
				params.add(uomGroupOrgTran.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getTranOrgId())) {
				sb.append(" AND TRAN_ORG_ID = ?");
				params.add(uomGroupOrgTran.getTranOrgId());
			}

			if (!StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {

				sb.append(" AND TRAN_RELA_TYPE LIKE ?");

				if (!StrUtil.isEmpty(pattern)) {

					if (pattern.equals("L")) {

						params.add("%" + uomGroupOrgTran.getTranRelaType());

					} else if (pattern.equals("R")) {

						params.add(uomGroupOrgTran.getTranRelaType().substring(
								0, 4)
								+ "%");

					} else {

						params.add(uomGroupOrgTran.getTranRelaType());

					}

				} else {

					params.add(uomGroupOrgTran.getTranRelaType());

				}
			}

			sb.append(" ORDER BY ORG_TRAN_ID ASC");

		}

		return super.jdbcFindList(sb.toString(), params, UomGroupOrgTran.class);

	}

    @Override
    public List<UomGroupOrgTran> queryUomGroupOrgTranNotStatusCd1100List(UomGroupOrgTran uomGroupOrgTran) {
        
        List<Object> params = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();
        
        if (uomGroupOrgTran != null) {

            sb.append("SELECT * FROM UOM_GROUP_ORGANIZATION_TRAN WHERE STATUS_CD <> ?");

            params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);

            if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgTranId())) {
                sb.append(" AND ORG_TRAN_ID = ?");
                params.add(uomGroupOrgTran.getOrgTranId());
            }

            if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgId())) {
                sb.append(" AND ORG_ID = ?");
                params.add(uomGroupOrgTran.getOrgId());
            }

            if (!StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {

                sb.append(" AND TRAN_RELA_TYPE = ?");
                params.add(uomGroupOrgTran.getTranRelaType());
            }
        }

        return super.jdbcFindList(sb.toString(), params, UomGroupOrgTran.class);
        
    }
    
    @Override
    public List<UomGroupOrgTran> queryUomGroupOrgTranList(UomGroupOrgTran uomGroupOrgTran) {
        
        List<Object> params = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();
        
        if (uomGroupOrgTran != null) {

            sb.append("SELECT * FROM UOM_GROUP_ORGANIZATION_TRAN WHERE STATUS_CD = ?");

            params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

            if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgTranId())) {
                sb.append(" AND ORG_TRAN_ID = ?");
                params.add(uomGroupOrgTran.getOrgTranId());
            }

            if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgId())) {
                sb.append(" AND ORG_ID = ?");
                params.add(uomGroupOrgTran.getOrgId());
            }

            if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getTranOrgId())) {
                sb.append(" AND TRAN_ORG_ID = ?");
                params.add(uomGroupOrgTran.getTranOrgId());
            }

            if (!StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {

                sb.append(" AND TRAN_RELA_TYPE = ?");
                params.add(uomGroupOrgTran.getTranRelaType());
            }
        }

        return super.jdbcFindList(sb.toString(), params, UomGroupOrgTran.class);
        
    }
    
    @Override
    public List<UomGroupOrgTran> queryUomGroupOrgTranStoreAreaNot1100List(UomGroupOrgTran uomGroupOrgTran) {
        
        List<Object> params = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();
        
        if (uomGroupOrgTran != null) {

            sb.append("SELECT * FROM UOM_GROUP_ORGANIZATION_TRAN WHERE STATUS_CD <> ?");

            params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);

            if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getTranOrgId())) {
                sb.append(" AND tran_org_id = ?");
                params.add(uomGroupOrgTran.getTranOrgId());
            }

            if (!StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {

                sb.append(" AND TRAN_RELA_TYPE = ?");
                params.add(uomGroupOrgTran.getTranRelaType());
            }
            
            if (!StrUtil.isEmpty(uomGroupOrgTran.getStoreAreaFlag())) {

                sb.append(" AND STORE_AREA_FLAG = ?");
                params.add(uomGroupOrgTran.getStoreAreaFlag());
            }
        }

        return super.jdbcFindList(sb.toString(), params, UomGroupOrgTran.class);
        
    }

}
