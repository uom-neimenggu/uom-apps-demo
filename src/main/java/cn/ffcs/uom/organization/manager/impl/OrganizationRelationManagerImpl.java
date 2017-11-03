package cn.ffcs.uom.organization.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.MultidimensionalTreeManager;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.systemconfig.model.AttrValue;

@Service("organizationRelationManager")
@Scope("prototype")
public class OrganizationRelationManagerImpl extends BaseDaoImpl implements
		OrganizationRelationManager {

	@Setter
	@Getter
	private treeCalcAction treeCalcVo;

	@Resource(name = "multidimensionalTreeManager")
	private MultidimensionalTreeManager multidimensionalTreeManager;

	// 为组织关系添加搜索条件
	public PageInfo queryPageInfoByOrganizationRelation(
			Organization organization,
			OrganizationRelation organizationRelation, int currentPage,
			int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD= ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (organization != null && organizationRelation != null) {

			if (organizationRelation.getOrgId() != null) {
				sql.append(" AND ORG_ID= ?");
				params.add(organizationRelation.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationRelation.getRelaCd())) {
				sql.append(" AND RELA_CD=?");
				params.add(organizationRelation.getRelaCd().trim());
			}

			sql.append(" AND RELA_ORG_ID IN (SELECT ORG_ID FROM ORGANIZATION WHERE STATUS_CD= ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(organization.getOrgCode())) {
				sql.append(" AND ORG_CODE= ?");
				params.add(StringEscapeUtils.escapeSql(organization.getOrgCode().trim()));
			}

			if (!StrUtil.isNullOrEmpty(organization.getOrgName())) {
				sql.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(organization.getOrgName().trim()) + "%");
			}

			sql.append(")");

		}
		sql.append(" ORDER BY ORG_REL_ID");
		return organizationRelation.repository().jdbcFindPageInfo(
				sql.toString(), params, currentPage, pageSize,
				OrganizationRelation.class);
	}

	public PageInfo queryPageInfoByOrganizationRelationNoStatusCd(
			Organization organization,
			OrganizationRelation organizationRelation, int currentPage,
			int pageSize) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM ORGANIZATION_RELATION WHERE 1=1");

		List params = new ArrayList();

		if (organization != null && organizationRelation != null) {

			if (organizationRelation.getOrgId() != null) {
				sql.append(" AND ORG_ID= ?");
				params.add(organizationRelation.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(organizationRelation.getRelaCd())) {
				sql.append(" AND RELA_CD=?");
				params.add(organizationRelation.getRelaCd().trim());
			}

			sql.append(" AND RELA_ORG_ID IN (SELECT ORG_ID FROM ORGANIZATION WHERE 1=1");
			// params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(organization.getOrgCode())) {
				sql.append(" AND ORG_CODE= ?");
				params.add(StringEscapeUtils.escapeSql(organization.getOrgCode().trim()));
			}

			if (!StrUtil.isNullOrEmpty(organization.getOrgName())) {
				sql.append(" AND ORG_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(organization.getOrgName().trim()) + "%");
			}

			sql.append(")");

		}
		sql.append(" ORDER BY UPDATE_DATE DESC");
		return organizationRelation.repository().jdbcFindPageInfo(
				sql.toString(), params, currentPage, pageSize,
				OrganizationRelation.class);
	}

	@Override
	public void removeOrganizationRelation(
			OrganizationRelation organizationRelation) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		organizationRelation.setBatchNumber(batchNumber);
		organizationRelation.remove();
	}

	@Override
	public void updateOrganizationRelation(
			OrganizationRelation organizationRelation) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		organizationRelation.setBatchNumber(batchNumber);
		organizationRelation.update();
	}

	@Override
	public void addOrganizationRelation(
			OrganizationRelation organizationRelation) {
		String batchNumber = OperateLog.gennerateBatchNumber();
		organizationRelation.setBatchNumber(batchNumber);
		organizationRelation.add();
		/**
		 * 新增组织关系时时候有修改的组织的集团编码
		 */
		Organization updateOrganization = organizationRelation
				.getUpdateOrganization();
		if (updateOrganization != null) {
			updateOrganization.update();
		}
	}

	@Override
	public OrganizationRelation queryOrganizationRelation(
			OrganizationRelation organizationRelation) {
		List<OrganizationRelation> list = this
				.queryOrganizationRelationList(organizationRelation);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<OrganizationRelation> queryOrganizationRelationList(
			OrganizationRelation organizationRelation) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (!StrUtil.isEmpty(organizationRelation.getRelaCd())) {
			sb.append(" AND RELA_CD=?");
			params.add(organizationRelation.getRelaCd());
		}
		if (organizationRelation.getOrgId() != null) {
			sb.append(" AND ORG_ID=?");
			params.add(organizationRelation.getOrgId());
		}
		if (organizationRelation.getRelaOrgId() != null) {
			sb.append(" AND RELA_ORG_ID=?");
			params.add(organizationRelation.getRelaOrgId());
		}
		return OrganizationRelation.repository().jdbcFindList(sb.toString(),
				params, OrganizationRelation.class);
	}

	@Override
	public List<OrganizationRelation> queryParentTreeOrganizationRelationList(
			Long orgId, String relaCd) {
		List params = new ArrayList();
		String sql = "SELECT T.* FROM (SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD = ? AND RELA_CD = ?) T START WITH ORG_ID = ? CONNECT BY PRIOR RELA_ORG_ID = ORG_ID";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(relaCd);
		params.add(orgId);
		List<OrganizationRelation> list = OrganizationRelation.repository()
				.jdbcFindList(sql, params, OrganizationRelation.class);
		return list;
	}

	// public List<OrganizationRelation>
	// queryParentTreeOrganizationRelationList(
	// Long orgId, String orgType) {
	// List params = new ArrayList();
	// String sql =
	// "SELECT * FROM (SELECT T1.* FROM ORGANIZATION_RELATION T1, ORG_TYPE T2 WHERE T1.STATUS_CD = ? AND T2.STATUS_CD = ? AND T1.ORG_ID = T2.ORG_ID AND T2.ORG_TYPE_CD LIKE ?) START WITH ORG_ID = ? CONNECT BY PRIOR RELA_ORG_ID = ORG_ID";
	// params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
	// params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
	// params.add(orgType + "%");
	// params.add(orgId);
	// List<OrganizationRelation> list = OrganizationRelation.repository()
	// .jdbcFindList(sql, params, OrganizationRelation.class);
	// return list;
	// }

	@Override
	public String getOrgFullName(Long orgId) {
		/**
		 * 行政上级
		 */
		// List<OrganizationRelation> parentTreeList = this
		// .queryParentTreeOrganizationRelationList(orgId,
		// OrganizationConstant.MANAGER_PRE);
		List<OrganizationRelation> parentTreeList = this
				.queryParentTreeOrganizationRelationList(orgId, "0101");
		if (parentTreeList != null && parentTreeList.size() > 0) {
			StringBuffer fullName = new StringBuffer();
			for (int i = (parentTreeList.size() - 1); i >= 0; i--) {
				/**
				 * 顶层集团公司不包含
				 */
				if (parentTreeList.get(i).getOrganization() != null
						&& OrganizationConstant.ROOT_TREE_ORG_ID
								.equals(parentTreeList.get(i).getOrganization()
										.getOrgId())) {
					continue;
				}
				if (parentTreeList.get(i) != null
						&& parentTreeList.get(i).getOrganization() != null) {
					fullName.append(parentTreeList.get(i).getOrganization()
							.getOrgName());
				}
			}
			return fullName.toString();
		}
		return "";
	}

	/**
	 * 验证上级组织进驻关系
	 * 
	 * @param orgId
	 *            进驻组织ID
	 * @param relaOrgId
	 *            被进驻组织ID
	 * @return
	 */
	public boolean checkSuperiorOrgEnterRela(Long orgId, Long relaOrgId,
			String relaCd) {
		if (!relaCd.equals(OrganizationConstant.RELA_CD_JZ)) {// 非进驻关系不验证
			return true;
		}
		// 组织是营业厅
		boolean orgAsBusinessHall = checkOrgIsBusinessHall(orgId);
		// 组织是网点
		boolean orgAsNetworkPoint = checkOrgIsNetworkPoint(orgId);
		// 上级组织是营业厅
		boolean relaOrgAsBusinessHall = checkOrgIsBusinessHall(relaOrgId);
		// 上级组织是网点
		boolean relaOrgAsNetworkPoint = checkOrgIsNetworkPoint(relaOrgId);

		if (!relaOrgAsNetworkPoint && !relaOrgAsBusinessHall) {// 当前节点不是营业厅或网点或网点时不加入验证
			return true;
		}
		if (!orgAsNetworkPoint) {// 目前业务 营业厅必须包含着网点的某个组织类型 这个IF不用也可以
			ZkUtil.showError("子节点组织类型不是网点！", "提示信息");
			return false;
		}
		if (orgId.equals(relaOrgId)) {
			ZkUtil.showError("当前节点与子节点一致不能关联到自己!", "提示信息");
			return false;
		}

		if (!relaOrgAsBusinessHall) {// 上级组织只有营业厅才能挂营业厅或网点
			ZkUtil.showError("当前节点的组织类型不是营业厅！", "提示信息");
			return false;
		}

		// 一个网点只能进驻到一个营业厅（组织类型一定要是“营业厅”），一个营业厅可以被多个网点进驻
		if (!checkOrgRelaIsExist(orgId, relaCd)) {
			ZkUtil.showError("子节点已关联到其它节点下！", "提示信息");
			return false;
		}
		// 进驻网点不能是营业厅，被进驻网点B只能是营业厅类型（组织类型）
		if (orgAsBusinessHall && relaOrgAsNetworkPoint
				&& !relaOrgAsBusinessHall) {
			ZkUtil.showError("子节点组织类型为营业厅不能关联到当前网点下！", "提示信息");
			return false;
		}

		return true;
	}

	/**
	 * 验证下级组织进驻关系
	 * 
	 * @param orgId
	 *            组织ID
	 * @param relaOrgId
	 *            关联组织ID
	 * @return
	 */
	public boolean checkSubordinateOrgEnterRela(Long orgId, Long relaOrgId,
			String relaCd) {
		if (!relaCd.equals(OrganizationConstant.RELA_CD_JZ)) {// 非进驻关系不验证
			return true;
		}
		// 组织是营业厅
		boolean orgAsBusinessHall = checkOrgIsBusinessHall(orgId);
		// 组织是网点
		boolean orgAsNetworkPoint = checkOrgIsNetworkPoint(orgId);
		// 上级组织是营业厅
		boolean relaOrgAsBusinessHall = checkOrgIsBusinessHall(relaOrgId);
		// 上级组织是网点
		// boolean relaOrgAsNetworkPoint = checkOrgIsNetworkPoint(relaOrgId);

		if (!orgAsNetworkPoint && !orgAsBusinessHall) {// 不是网点且不是营业厅时不加入验证（营业厅的组织类型必须有包括网点的某个组织类型）
			return true;
		}
		if (!orgAsBusinessHall && orgAsNetworkPoint && !relaOrgAsBusinessHall) {// 组织不是营业厅且是网点时如果上级组织不是营业厅时弹出错误提示
			ZkUtil.showError("上级组织类型不是营业厅！", "提示信息");
			return false;
		}
		if (orgId.equals(relaOrgId)) {
			ZkUtil.showError("不能关联到自己!", "提示信息");
			return false;
		}

		/*
		 * if(!relaOrgAsBusinessHall){ ZkUtil.showError("当前节点的组织类型不是营业厅！",
		 * "提示信息"); return false; }
		 */

		// 一个网点只能进驻到一个营业厅（组织类型一定要是“营业厅”），一个营业厅可以被多个网点进驻
		if (!checkOrgRelaIsExist(orgId, relaCd)) {
			ZkUtil.showError("当前节点已关联到其它节点下！", "提示信息");
			return false;
		}
		// 进驻网点不能是营业厅，被进驻网点B只能是营业厅类型（组织类型）
		/*
		 * if(orgAsBusinessHall&&relaOrgAsNetworkPoint&&!relaOrgAsBusinessHall){
		 * ZkUtil.showError("子节点组织类型为营业厅不能关联到当前网点下！", "提示信息"); return false; }
		 */

		return true;
	}

	/**
	 * 验证组织是否是网点
	 * 
	 * @param orgId
	 * @return
	 */
	@Override
	public boolean checkOrgIsNetworkPoint(Long orgId) {
		List params = new ArrayList();
		String sql = "SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C  INNER JOIN ORG_TYPE OT ON(C.ATTR_VALUE = OT.ORG_TYPE_CD) WHERE  OT.ORG_ID = ? AND OT.STATUS_CD = ? AND C.STATUS_CD = ? AND A.JAVA_CODE = ? AND B.JAVA_CODE = ? AND A.CLASS_ID = B.CLASS_ID AND B.ATTR_ID = C.ATTR_ID AND ATTR_VALUE LIKE ?";
		params.add(orgId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add("OrgType");
		params.add("orgTypeCd");
		params.add("N02%");
		List<AttrValue> attrs = super
				.jdbcFindList(sql, params, AttrValue.class);
		if (attrs != null && attrs.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 验证组织是否是营业厅
	 * 
	 * @param orgId
	 * @return
	 */
	@Override
	public boolean checkOrgIsBusinessHall(Long orgId) {
		List params = new ArrayList();
		String sql = "SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C  INNER JOIN ORG_TYPE OT ON(C.ATTR_VALUE = OT.ORG_TYPE_CD) WHERE  OT.ORG_ID = ? AND OT.STATUS_CD = ? AND C.STATUS_CD = ? AND A.JAVA_CODE = ? AND B.JAVA_CODE = ? AND A.CLASS_ID = B.CLASS_ID AND B.ATTR_ID = C.ATTR_ID AND ATTR_VALUE = ? ";
		params.add(orgId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add("OrgType");
		params.add("orgTypeCd");
		params.add(OrganizationConstant.ORG_TYPE_N0102010800);
		List<AttrValue> attrs = super
				.jdbcFindList(sql, params, AttrValue.class);
		if (attrs != null && attrs.size() > 0 && checkOrgIsNetworkPoint(orgId)) {// 营业厅的判断规则是，组织类型选择了
																					// 渠道中的任何一种且
																					// 选择了“直营厅”类型
			return true;
		}

		/*
		 * List params = new ArrayList(); String sql =
		 * "SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C  INNER JOIN ORG_TYPE OT ON(C.ATTR_VALUE = OT.ORG_TYPE_CD) WHERE  OT.ORG_ID = ? AND OT.STATUS_CD = ? AND C.STATUS_CD = ? AND A.JAVA_CODE = ? AND B.JAVA_CODE = ? AND A.CLASS_ID = B.CLASS_ID AND B.ATTR_ID = C.ATTR_ID AND ATTR_VALUE in(?,?,?)"
		 * ; params.add(orgId); params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		 * params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		 * params.add("OrgType"); params.add("orgTypeCd");
		 * params.add(OrganizationConstant.ORG_TYPE_N0102010900);
		 * params.add(OrganizationConstant.ORG_TYPE_N0101020300);
		 * params.add(OrganizationConstant.ORG_TYPE_N0102010800);
		 * List<AttrValue> attrs = super.jdbcFindList(sql, params,
		 * AttrValue.class); if(attrs!=null && attrs.size()>0){ return true; }
		 */
		return false;
	}

	/**
	 * 根据组织ID验证是否有关系存在 用于 一个网点只能在一个营业厅下
	 * 
	 * @param orgId
	 * @return
	 */
	public boolean checkOrgRelaIsExist(Long orgId, String relaCd) {
		List params = new ArrayList();
		String sql = "select * from organization_relation where org_id = ? and status_cd = ? and rela_cd = ?";
		params.add(orgId);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(relaCd);
		List<OrganizationRelation> orList = super.jdbcFindList(sql, params,
				OrganizationRelation.class);
		// 一个网点只能在一个营业厅下
		if (orList != null && orList.size() > 0) {
			for (OrganizationRelation or : orList) {
				if (checkOrgIsBusinessHall(or.getRelaOrgId())) {
					return false;
				}
			}
		}

		/*
		 * // 一个网点只能有一条关系 if(orList!=null && orList.size()>0){ return false; }
		 */
		return true;
	}

	public OrganizationRelation getOrgRelByTreeId(Long treeId) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM ORGANIZATION_RELATION WHERE STATUS_CD=? AND ORG_REL_ID=(SELECT ORG_REL_ID FROM ORG_REL_ROOT_CONFIG WHERE STATUS_CD=? AND ORG_TREE_ID=?)");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(treeId);
		List<OrganizationRelation> list = OrganizationRelation
				.repository()
				.jdbcFindList(sb.toString(), params, OrganizationRelation.class);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

}
