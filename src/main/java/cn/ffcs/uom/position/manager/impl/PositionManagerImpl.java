/**
 * 
 */
package cn.ffcs.uom.position.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.position.dao.PositionDao;
import cn.ffcs.uom.position.manager.PositionManager;
import cn.ffcs.uom.position.model.Position;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * @author yahui
 * 
 */
@Service("positionManager")
@Scope("prototype")
public class PositionManagerImpl implements PositionManager {

	@Resource
	private PositionDao positionDao;

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public PageInfo queryPageInfoByQuertPosition(Position queryPosition,
			int currentPage, int pageSize) {
		List params = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM POSITION A WHERE A.STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (queryPosition != null) {
			if (!StrUtil.isEmpty(queryPosition.getPositionCode())) {
				sql.append(" AND A.POSITION_CODE LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(queryPosition.getPositionCode()) + "%");
			}
			if (!StrUtil.isEmpty(queryPosition.getPositionName())) {
				sql.append(" AND A.POSITION_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(queryPosition.getPositionName()) + "%");
			}
			if (!StrUtil.isEmpty(queryPosition.getPositionType())) {
				sql.append(" AND A.POSITION_TYPE = ?");
				params.add(queryPosition.getPositionType());
			}
		}
		return queryPosition.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, Position.class);

	}

	@SuppressWarnings({ "unchecked", "static-access" })
	@Override
	public PageInfo queryPageInfoByPositionId(Organization queryOrganization,
			Position queryPosition, int currentPage, int pageSize) {

		List params = new ArrayList();
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ORGANIZATION WHERE STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (queryOrganization != null && queryPosition != null) {

			if (!StrUtil.isEmpty(queryOrganization.getOrgCode())) {
				sql.append(" AND ORG_CODE = ?");
				params.add(StringEscapeUtils.escapeSql(queryOrganization.getOrgCode().trim()));
			}

			if (!StrUtil.isEmpty(queryOrganization.getOrgName())) {
				sql.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(queryOrganization.getOrgName().trim()) + "%");
			}

			sql.append(" AND ORG_ID IN (SELECT ORG_ID FROM ORG_POSITION WHERE STATUS_CD = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (queryPosition.getPositionId() != null) {
				sql.append(" AND POSITION_ID = ?");
				params.add(queryPosition.getPositionId());
			}

			sql.append(")");
			sql.append(" ORDER BY ORG_ID");
		}
		return queryPosition.repository().jdbcFindPageInfo(sql.toString(),
				params, currentPage, pageSize, Organization.class);

	}

	@Override
	public void savePosition(Position position) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		position.setBatchNumber(batchNumber);
		position.add();
	}

	@Override
	public void updatePosition(Position position) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		position.setBatchNumber(batchNumber);
		position.update();
	}

	@Override
	public void removePosition(Position position) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		position.setBatchNumber(batchNumber);
		position.remove();
	}

	@Override
	public Position jdbcFindPosition(Position position) {
		return positionDao.jdbcFindPosition(position);
	}

	@Override
	public String getSeqPositionCode() {
		return positionDao.getSeqPositionCode();
	}

	/**
	 * 方法功能: 获取positionType属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NodeVo> getValuesList() {

		List params = new ArrayList();
		String sql = "SELECT * FROM ATTR_VALUE WHERE STATUS_CD = ?"
				+ " AND ATTR_ID IN (SELECT ATTR_ID FROM ATTR_SPEC WHERE STATUS_CD = ?"
				+ " AND CLASS_ID = ( SELECT CLASS_ID FROM SYS_CLASS WHERE STATUS_CD = ?"
				+ " AND JAVA_CODE='Position' ) AND JAVA_CODE='positionType')"
				+ " AND ATTR_VALUE NOT IN (SELECT DISTINCT POSITION_TYPE FROM POSITION WHERE STATUS_CD = ?)";

		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		List<AttrValue> lstAttrValue = AttrValue.repository().jdbcFindList(sql,
				params, AttrValue.class);

		List<NodeVo> retAttrValues = new ArrayList();

		if (lstAttrValue != null) {
			for (AttrValue av : lstAttrValue) {
				if (av != null) {
					NodeVo vo = new NodeVo();
					vo.setId(av.getAttrValue());
					vo.setName(av.getAttrValueName());
					vo.setDesc(av.getAttrDesc());
					retAttrValues.add(vo);
				}
			}
		}

		return retAttrValues;
	}
	
	/**
	 * 移动员工组织关系时，删除原来的员工岗位关系
	 * @param staffOrganization
	 */
	public void removeStaffPostionByOrganization(StaffOrganization staffOrganization){
		positionDao.removeStaffPostionByOrganization(staffOrganization);
	}
}
