package cn.ffcs.uom.position.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.position.manager.OrgPositonManager;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.staff.model.Staff;

@Service("orgPositonManager")
@Scope("prototype")
public class OrgPositonManagerImpl implements OrgPositonManager {
	@Override
	public PageInfo queryPageInfoByQueryOrgPosition(Position queryPosition,
			OrgPosition queryVo, int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM ORG_POSITION WHERE STATUS_CD= ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (queryVo.getOrgId() != null) {
			sb.append(" AND ORG_ID= ?");
			params.add(queryVo.getOrgId());
		}

		sb.append(" AND POSITION_ID IN (SELECT POSITION_ID FROM POSITION WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (!StrUtil.isEmpty(queryPosition.getPositionCode())) {
			sb.append(" AND POSITION_CODE = ?");
			params.add(StringEscapeUtils.escapeSql(queryPosition.getPositionCode().trim()));
		}

		if (!StrUtil.isEmpty(queryPosition.getPositionName())) {
			sb.append(" AND POSITION_NAME LIKE ?");
			params.add("%" + StringEscapeUtils.escapeSql(queryPosition.getPositionName().trim()) + "%");
		}

		sb.append(")");
		sb.append(" ORDER BY POSITION_ID");

		return OrgPosition.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, OrgPosition.class);
	}

	@Override
	public void removeOrganizationPosition(OrgPosition orgPosition) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		orgPosition.setBatchNumber(batchNumber);
		orgPosition.remove();
		/**
		@SuppressWarnings("unchecked")
		OrgPosition oldOrgPosition = new OrgPosition();

		if (OrgPosition.repository().jdbcFindOrgPosition(orgPosition) != null) {
			oldOrgPosition = OrgPosition.repository().jdbcFindOrgPosition(
					orgPosition);
			Date nowDate = DateUtil.getNowDate();
			oldOrgPosition.setExpDate(nowDate);
			oldOrgPosition.setUpdateDate(nowDate);
			oldOrgPosition.setStatusDate(nowDate);
			oldOrgPosition.setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
			OrgPosition.repository().updateObject(oldOrgPosition);
		} else {
			OrgPosition.repository().updateObject(orgPosition);
		}
		*/
	}

	@Override
	public void addOrganizationPosition(OrgPosition orgPosition) {
		orgPosition.repository().addOrganizationPosition(orgPosition);
	}

	@Override
	public PageInfo queryPageInfoByStaff(Staff staff, int currentPage,
			int pageSize) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT A.* FROM ORG_POSITION A, STAFF_ORGANIZATION B WHERE A.STATUS_CD =? AND A.ORG_ID = B.ORG_ID AND B.STATUS_CD = ? AND B.STAFF_ID = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(staff.getStaffId());
		return OrgPosition.repository().jdbcFindPageInfo(sb.toString(), params,
				currentPage, pageSize, OrgPosition.class);
	}

	@Override
	public OrgPosition queryOrganizationPosition(OrgPosition orgPosition) {
		List<OrgPosition> list = this
				.queryOrganizationPositionList(orgPosition);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<OrgPosition> queryOrganizationPositionList(
			OrgPosition orgPosition) {
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
		return OrgPosition.repository().jdbcFindList(sb.toString(), params,
				OrgPosition.class);
	}

	@Override
	public void addOrganizationPositionList(List<OrgPosition> orgPositionList) {
		if (orgPositionList != null) {
			String batchNumber = OperateLog.gennerateBatchNumber();
			for (OrgPosition orgPosition : orgPositionList) {
				orgPosition.setBatchNumber(batchNumber);
				orgPosition.add();
			}
		}
	}
}
