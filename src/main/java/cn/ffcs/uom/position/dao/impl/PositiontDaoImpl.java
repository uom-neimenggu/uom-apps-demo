/**
 * 
 */
package cn.ffcs.uom.position.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.position.dao.PositionDao;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.model.StaffPosition;

/**
 * @author yahui
 * 
 */
@Repository("positionDao")
public class PositiontDaoImpl extends BaseDaoImpl implements PositionDao {

	/**
	 * 查询单个对象。
	 * 
	 * @param <E>
	 *            数据对象
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param elementType
	 *            对象类型
	 * @return 对象数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Position jdbcFindPosition(Position position) {
		List params = new ArrayList();

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM POSITION WHERE POSITION_ID = ( SELECT MAX(POSITION_ID) FROM POSITION WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (position.getPositionId() != null) {
			sb.append(" AND PARENT_POSITION_ID = ?");
			params.add(position.getPositionId());
		}

		if (!StrUtil.isEmpty(position.getPositionType())) {
			sb.append(" AND POSITION_TYPE= ?");
			params.add(position.getPositionType().trim());
		}
		sb.append(" )");

		return this.jdbcFindObject(sb.toString(), params, Position.class);
	}

	public String getSeqPositionCode() {
		String sql = "SELECT SEQ_POSITION_CODE.NEXTVAL FROM DUAL";
		return String.valueOf(getJdbcTemplate().queryForInt(sql));
	}
	
	public void removeStaffPostionByOrganization(StaffOrganization staffOrganization){
		String batchNumber = OperateLog.gennerateBatchNumber();
		if(null != staffOrganization){
			String sql = "SELECT T.* FROM ORG_POSITION T WHERE T.ORG_ID = ? AND T.STATUS_CD = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(staffOrganization.getOrgId());
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			List<OrgPosition> orgPositions = jdbcFindList(sql,params,OrgPosition.class);
	    	if(null != orgPositions && orgPositions.size() > 0){
	    		for (OrgPosition orgPosition : orgPositions) {
					Long orgOrgPositionId = orgPosition.getOrgPositionId();
					Long staffId = staffOrganization.getStaffId();
					String sqlStr = "SELECT * FROM STAFF_POSITION WHERE STAFF_ID = ? AND ORG_POSITION_RELA_ID = ? AND STATUS_CD = ?";
					List<Object> paramsStr = new ArrayList<Object>();
					paramsStr.add(staffId);
					paramsStr.add(orgOrgPositionId);
					paramsStr.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					List<StaffPosition> sps = jdbcFindList(sqlStr,paramsStr,StaffPosition.class);
					if(null != sps && sps.size() > 0){
						for (StaffPosition staffPosition : sps) {
							staffPosition.setBatchNumber(batchNumber);
							staffPosition.remove();
						}
					}
	    		}
	    	}
		}		
	}
}
