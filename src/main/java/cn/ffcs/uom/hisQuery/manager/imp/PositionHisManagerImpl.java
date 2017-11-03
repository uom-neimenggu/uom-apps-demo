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
import cn.ffcs.uom.hisQuery.manager.PositionHisManager;

import cn.ffcs.uom.position.model.Position;

/**
 * @author yahui
 *
 */
@Service("positionHisManager")
@Scope("prototype")
public class PositionHisManagerImpl implements PositionHisManager {
	private Position position;

	@Override
	public PageInfo queryPositionHisPageInfoByParams(Map paramsMap,
			int currentPage, int pageSize) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM (SELECT POSITION_ID,POSITION_CODE,POSITION_NAME,STATUS_CD,EFF_DATE,EXP_DATE FROM POSITION  WHERE 1=1 ");
		if (paramsMap.get("queryDate") != null) {
			sql.append(" AND EFF_DATE<=?");
			params.add(paramsMap.get("queryDate"));

			if (paramsMap.get("orgId") != null) {
				sql.append(" AND POSITION_ID IN (SELECT POSITION_ID FROM ORG_POSITION WHERE EFF_DATE <= ? AND ORG_ID = ?");
				params.add(paramsMap.get("queryDate"));
				params.add(paramsMap.get("orgId"));
			}
			if(paramsMap.get("orgId") != null){
				sql.append("UNION SELECT POSITION_ID FROM ORG_POSITION_HIS WHERE ORG_ID = ? AND EFF_DATE<= ? AND EXP_DATE> ? )");
				params.add(paramsMap.get("orgId"));
				params.add(paramsMap.get("queryDate"));
				params.add(paramsMap.get("queryDate"));
			}

			sql
					.append("UNION SELECT POSITION_ID,POSITION_CODE, POSITION_NAME,STATUS_CD,EFF_DATE,EXP_DATE FROM POSITION_HIS WHERE 1=1");
			if (paramsMap.get("orgId") != null) {
				sql.append(" AND POSITION_ID IN (SELECT POSITION_ID FROM ORG_POSITION WHERE EFF_DATE< ? AND ORG_ID =?");
				params.add(paramsMap.get("queryDate"));
				params.add(paramsMap.get("orgId"));
			}
			if(paramsMap.get("orgId") != null){
				sql.append("UNION SELECT POSITION_ID FROM ORG_POSITION_HIS WHERE ORG_ID = ? AND EFF_DATE<= ? AND EXP_DATE> ? )");
				params.add(paramsMap.get("orgId"));
				params.add(paramsMap.get("queryDate"));
				params.add(paramsMap.get("queryDate"));
			}
			sql.append(" AND EFF_DATE<=? AND EXP_DATE>?");
			params.add(paramsMap.get("queryDate"));
			params.add(paramsMap.get("queryDate"));
		} else {
			sql.append(" AND STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (paramsMap.get("orgId") != null) {
				sql.append(" AND POSITION_ID IN (SELECT POSITION_ID FROM ORG_POSITION WHERE ORG_ID =? AND STATUS_CD = ?)");
				params.add(paramsMap.get("orgId"));
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			}
		}
		sql.append(")");
		sql.append("ORDER BY POSITION_ID");
		return this.position.repository().jdbcFindPageInfo(sql.toString(), params, currentPage, pageSize, Position.class);
	}

	@Override
	public Position queryPositionDetail(Map paramsMap) {
		List<Position> positions = null;
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM position WHERE 1=1 ");
		if(paramsMap.get("positionId") != null){
			sql.append("AND position_id = ?");
			params.add(paramsMap.get("positionId"));			
		}
		if(paramsMap.get("effDate") != null){
			sql.append(" AND eff_date <= ?");
			params.add(paramsMap.get("effDate"));
		}
		positions  = this.position.repository().jdbcFindList(sql.toString(), params,Position.class);
		if(positions != null && positions.size()>0){
			return positions.get(0);
		}
		else{
			StringBuffer sqlLog = new StringBuffer();
			List paramsLog = new ArrayList();
			sqlLog.append("SELECT * FROM POSITION_HIS WHERE 1=1");
			if(paramsMap.get("positionId") != null){
				sqlLog.append(" AND position_id = ?");
				paramsLog.add(paramsMap.get("positionId"));
			}
			if(paramsMap.get("effDate") != null){
				sqlLog.append(" AND eff_date <= ?");
				paramsLog.add(paramsMap.get("effDae"));
			}
			if(paramsMap.get("expDate") != null){
				sqlLog.append(" AND exp_date > ?");
				paramsLog.add(paramsMap.get("expDate"));
			}
			positions = this.position.repository().jdbcFindList(sqlLog.toString(),paramsLog,Position.class);
			if(positions != null && positions.size()>0){
				return positions.get(0);
			}
		}
		return null;
	}

}
