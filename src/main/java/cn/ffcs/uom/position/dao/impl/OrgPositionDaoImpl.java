package cn.ffcs.uom.position.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.position.dao.OrgPositionDao;
import cn.ffcs.uom.position.model.OrgPosition;

@Repository("orgPositionDao")
public class OrgPositionDaoImpl extends BaseDaoImpl implements OrgPositionDao {

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
	@Override
	public OrgPosition jdbcFindOrgPosition(OrgPosition orgPosition) {
		List params = new ArrayList();

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM ORG_POSITION WHERE STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (orgPosition.getOrgId() != null) {
			sb.append(" AND ORG_ID=?");
			params.add(orgPosition.getOrgId());
		}

		if (orgPosition.getPositionId() != null) {
			sb.append(" AND POSITION_ID=?");
			params.add(orgPosition.getPositionId());
		}

		return this.jdbcFindObject(sb.toString(), params, OrgPosition.class);
	}

	@Override
	public void addOrganizationPosition(OrgPosition orgPosition) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		orgPosition.setBatchNumber(batchNumber);
		orgPosition.add();
	}
}
