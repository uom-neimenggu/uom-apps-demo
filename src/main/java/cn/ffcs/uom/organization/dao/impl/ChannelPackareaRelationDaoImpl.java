package cn.ffcs.uom.organization.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.ChannelPackareaRelationDao;
import cn.ffcs.uom.organization.vo.ChannelPackareaRelationVo;

@Repository("channelPackareaRelationDao")
public class ChannelPackareaRelationDaoImpl extends BaseDaoImpl implements
		ChannelPackareaRelationDao {

	@Override
	public PageInfo queryPageInfoByChannelPackareaRelationVo(
			ChannelPackareaRelationVo channelPackareaRelationVo,
			int currentPage, int pageSize) {

		if (channelPackareaRelationVo != null) {

			StringBuffer sql = new StringBuffer(
					"select t1.org_tran_id as id, t2.org_id as agentChannelId, t1.store_area_flag as storeAreaFlag, t2.org_code as agentChannelNbr, "
							+ "t2.org_name as agentChannelName, t5.org_attr_value as agentChannelGroupCode,"
							+ "t3.org_id as custPackareaId, t3.org_name as custPackareaName, t3.org_code as custPackareaCode,"
							+ " t3.eda_code as custPackareaEdaCode, t1.status_cd, t1.update_date from UOM_GROUP_ORGANIZATION_TRAN t1 left join organization_extend_attr t5 on t5.org_attr_spec_id=18 and t5.status_cd='1000' and t1.org_id = t5.org_id "
							+ "LEFT JOIN organization t2 ON t2.STATUS_CD = '1000' AND t1.ORG_ID = t2.ORG_ID LEFT JOIN organization t3 ON t3.STATUS_CD = '1000' AND t1.TRAN_ORG_ID = TO_CHAR(t3.ORG_ID) ");

			sql.append("where t1.TRAN_RELA_TYPE = '100300' ");

			List<Object> params = new ArrayList<Object>();
			
			if(!StrUtil.isNullOrEmpty(channelPackareaRelationVo.getStatusCd())) {
				sql.append(" and t1.status_cd = ?");
				params.add(channelPackareaRelationVo.getStatusCd());
			} else {
				sql.append(" and t1.status_cd <> ?");
				params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getAgentChannelName())) {
				sql.append(" and t2.org_name like ?");
				params.add("%"
						+ StringEscapeUtils.escapeSql(channelPackareaRelationVo.getAgentChannelName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getAgentChannelNbr())) {
				sql.append(" AND t2.org_code = ?");
				params.add(StringEscapeUtils.escapeSql(channelPackareaRelationVo.getAgentChannelNbr()));
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getCustPackareaCode())) {
				sql.append(" and t3.org_code = ?");
				params.add(StringEscapeUtils.escapeSql(channelPackareaRelationVo.getCustPackareaCode()));
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getCustPackareaName())) {
				sql.append(" and t3.org_name like ?");
				params.add("%"
						+ StringEscapeUtils.escapeSql(channelPackareaRelationVo.getCustPackareaName()) + "%");
			}

			/**
			 * 限定查询范围，避免跨区查询
			 */
			if (channelPackareaRelationVo.getTelcomRegionId() != null) {
				sql.append(" and t2.org_id in (");
				sql.append("SELECT T1.org_id FROM ORGANIZATION T1  ,( ");
				sql.append("SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR ");
				sql.append("WHERE TR.STATUS_CD= ").append(
						BaseUnitConstants.ENTT_STATE_ACTIVE);
				sql.append(" START WITH TR.TELCOM_REGION_ID=")
						.append(channelPackareaRelationVo.getTelcomRegionId())
						.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID=TR.UP_REGION_ID ");
				sql.append(") TR ");
				sql.append("WHERE ");
				sql.append("TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID ");
				sql.append(" )");
			}

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, ChannelPackareaRelationVo.class);

		}

		return null;
	}

	@Override
	public List<Map<String, Object>> queryListByChannelPackareaRelationVo(
			ChannelPackareaRelationVo channelPackareaRelationVo) {
		if (channelPackareaRelationVo != null) {

			StringBuffer sql = new StringBuffer(
					"select t2.org_id 渠道标识,t2.org_code 渠道编码,t2.org_name 渠道名称,ltrim(t2.ALLORGNAME,'->') 渠道全路径,"
							+ "t5.org_attr_value 集团渠道编码,t3.org_id 包区标识,t3.org_name 包区名称,"
							+ "t3.org_code 包区编码,t3.eda_code 包区EDACODE,ltrim(t3.ALLORGNAME,'->') 包区全路径,"
							+ "decode(t1.store_area_flag,1,'是',0,'否','其他') 以店包区,"
							+ "decode(t1.status_cd, 1000, '生效', 1100, '失效', 1320, '审批中', '其他') 状态,"
							+ "to_char(t1.update_date, 'yyyy-mm-dd hh24:mi:ss') 修改时间 "
							+ " from UOM_GROUP_ORGANIZATION_TRAN t1 left join organization_extend_attr t5"
							+ " on t5.org_attr_spec_id=18 and t5.status_cd=1000 and t1.org_id = t5.org_id "
							+ " left join (SELECT A.ORG_ID,A.ORG_CODE,A.ORG_NAME,A.RELA_ORG_ID,A.EDA_CODE,LEVEL LV,"
							+ "SYS_CONNECT_BY_PATH(A.ORG_NAME, '->') ALLORGNAME FROM (SELECT o1.*, o2.RELA_ORG_ID "
							+ " FROM ORGANIZATION o1, ORGANIZATION_RELATION o2 WHERE o1.ORG_ID = o2.ORG_ID "
							+ " AND o1.STATUS_CD = 1000 AND o2.STATUS_CD = 1000 AND o2.RELA_CD = 0102) A "
							+ " CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID START WITH A.ORG_ID = 9999999998) t2 "
							+ " ON (t1.ORG_ID = t2.ORG_ID) "
							+ " LEFT JOIN (SELECT A.ORG_ID, A.ORG_CODE,A.ORG_NAME,A.RELA_ORG_ID,A.EDA_CODE,"
							+ "LEVEL LV,SYS_CONNECT_BY_PATH(A.ORG_NAME, '->') ALLORGNAME FROM (SELECT o1.*, o2.RELA_ORG_ID"
							+ " FROM ORGANIZATION o1, ORGANIZATION_RELATION o2 WHERE o1.ORG_ID = o2.ORG_ID "
							+ " AND o1.STATUS_CD = 1000 AND o2.STATUS_CD = 1000 AND o2.RELA_CD = 0404) A "
							+ " CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID START WITH A.ORG_ID = 9999999990) t3 "
							+ " ON t1.TRAN_ORG_ID = TO_CHAR(t3.ORG_ID) ");
			sql.append("where t1.TRAN_RELA_TYPE = '100300' ");

			List<Object> params = new ArrayList<Object>();
			
			if(!StrUtil.isNullOrEmpty(channelPackareaRelationVo.getStatusCd())) {
				sql.append(" and t1.status_cd = ?");
				params.add(channelPackareaRelationVo.getStatusCd());
			} else {
				sql.append(" and t1.status_cd <> ?");
				params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getAgentChannelName())) {
				sql.append(" and t2.org_name like ?");
				params.add("%"
						+ StringEscapeUtils.escapeSql(channelPackareaRelationVo.getAgentChannelName()) + "%");
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getAgentChannelNbr())) {
				sql.append(" AND t2.org_code = ?");
				params.add(StringEscapeUtils.escapeSql(channelPackareaRelationVo.getAgentChannelNbr()));
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getCustPackareaCode())) {
				sql.append(" and t3.org_code = ?");
				params.add(StringEscapeUtils.escapeSql(channelPackareaRelationVo.getCustPackareaCode()));
			}

			if (!StrUtil.isNullOrEmpty(channelPackareaRelationVo
					.getCustPackareaName())) {
				sql.append(" and t3.org_name like ?");
				params.add("%"
						+ StringEscapeUtils.escapeSql(channelPackareaRelationVo.getCustPackareaName()) + "%");
			}

			/**
			 * 限定查询范围，避免跨区查询
			 */
			if (channelPackareaRelationVo.getTelcomRegionId() != null) {
				sql.append(" and t2.org_id in (");
				sql.append("SELECT T1.org_id FROM ORGANIZATION T1  ,( ");
				sql.append("SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR ");
				sql.append("WHERE TR.STATUS_CD= ").append(
						BaseUnitConstants.ENTT_STATE_ACTIVE);
				sql.append(" START WITH TR.TELCOM_REGION_ID=")
						.append(channelPackareaRelationVo.getTelcomRegionId())
						.append(" CONNECT BY PRIOR TR.TELCOM_REGION_ID=TR.UP_REGION_ID ");
				sql.append(") TR ");
				sql.append("WHERE ");
				sql.append("TR.TELCOM_REGION_ID = T1.TELCOM_REGION_ID ");
				sql.append(" )");
			}

			return this.getJdbcTemplate().queryForList(sql.toString(),
					params.toArray());
		}

		return null;
	}

}
