package cn.ffcs.uom.organization.manager.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;
import oracle.jdbc.OracleTypes;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.kernel.messaging.Message;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.dataPermission.manager.AroleProfessionalTreeManager;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelType;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.MdsionOrgTree;
import cn.ffcs.uom.organization.model.OrganizationRelation;
import cn.ffcs.uom.systemconfig.model.OrgTreeConfig;

@Service("mdsionOrgRelationManager")
@Scope("prototype")
public class MdsionOrgRelationManagerImpl extends BaseDaoImpl implements
		MdsionOrgRelationManager {

	@Resource(name = "aroleProfessionalTreeManager")
	private AroleProfessionalTreeManager aroleProfessionalTreeManager;

	@Setter
	@Getter
	private treeCalcAction treeCalcVo;

	@Override
	public MdsionOrgRelation queryMdsionOrgRelation(
			MdsionOrgRelation mdsionOrgRelation) {
		List<MdsionOrgRelation> list = this
				.queryMdsionOrgRelationList(mdsionOrgRelation);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<MdsionOrgRelation> queryMdsionOrgRelationList(
			MdsionOrgRelation mdsionOrgRelation) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM MDSION_ORG_RELATION MOR INNER JOIN MDSION_ORG_REL_TYPE MORT ON MOR.MDSION_ORG_REL_ID = MORT.MDSION_ORG_REL_ID WHERE MOR.STATUS_CD = ? AND MORT.STATUS_CD = ? ");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (!StrUtil.isEmpty(mdsionOrgRelation.getRelaCd())) {
			sb.append(" AND MORT.MDSION_ORG_REL_TYPE_CD = ?");
			params.add(mdsionOrgRelation.getRelaCd());
		}
		if (mdsionOrgRelation.getOrgId() != null) {
			sb.append(" AND MOR.ORG_ID = ?");
			params.add(mdsionOrgRelation.getOrgId());
		}
		if (mdsionOrgRelation.getRelaOrgId() != null) {
			sb.append(" AND MOR.RELA_ORG_ID = ?");
			params.add(mdsionOrgRelation.getRelaOrgId());
		}
		return OrganizationRelation.repository().jdbcFindList(sb.toString(),
				params, MdsionOrgRelation.class);
	}

	public MdsionOrgRelation getMdsionOrgRelation(
			MdsionOrgRelation mdsionOrgRelation) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM MDSION_ORG_RELATION MOR  WHERE MOR.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (mdsionOrgRelation.getOrgId() != null) {
			sb.append(" AND MOR.ORG_ID = ?");
			params.add(mdsionOrgRelation.getOrgId());
		}
		if (mdsionOrgRelation.getRelaOrgId() != null) {
			sb.append(" AND MOR.RELA_ORG_ID = ?");
			params.add(mdsionOrgRelation.getRelaOrgId());
		}
		List<MdsionOrgRelation> list = OrganizationRelation.repository()
				.jdbcFindList(sb.toString(), params, MdsionOrgRelation.class);
		if (list != null && list.size() > 0)
			return list.get(0);
		return mdsionOrgRelation;
	}

	@Override
	public void addMdsionOrgRelation(MdsionOrgRelation mdsionOrgRelation) {
		MdsionOrgRelation mor = this.getMdsionOrgRelation(mdsionOrgRelation);
		String batchNumber = OperateLog.gennerateBatchNumber();
		mdsionOrgRelation.setBatchNumber(batchNumber);
		mor.setBatchNumber(batchNumber);
		if (mor.getId() != null) {
			mor.update();
			mdsionOrgRelation.setMdsionOrgRelId(mor.getId());
		} else {
			mdsionOrgRelation.add();
		}

		MdsionOrgRelType mdsionOrgRelType = new MdsionOrgRelType();
		mdsionOrgRelType.setBatchNumber(batchNumber);
		mdsionOrgRelType.setMdsionOrgRelTypeCd(mdsionOrgRelation.getRelaCd());
		mdsionOrgRelType.setMdsionOrgRelId(mdsionOrgRelation.getId());
		mdsionOrgRelType.add();
	}

	/**
	 * 修改组织关系类型
	 * 
	 * @param mdsionOrgRelId
	 */
	public void updateMdsionOrgRelType(MdsionOrgRelation mdsionOrgRelation) {
		this.getJdbcTemplate().execute(
				"UPDATE MDSION_ORG_REL_TYPE SET MDSION_ORG_REL_TYPE_CD = '"
						+ mdsionOrgRelation.getRelaCd()
						+ "' WHERE MDSION_ORG_REL_ID = "
						+ mdsionOrgRelation.getId());
	}

	/**
	 * 加载内部树
	 */
	public void loadPoliticalTree(final String org_id,
			final Treeitem orgRelaRootTreeitem,
			final Map<String, List<Checkbox>> checkboxMaps) {
		if (!StrUtil.isEmpty(org_id)) {
			super.getJdbcTemplate().execute(new ConnectionCallback<Object>() {
				@Override
				public Object doInConnection(Connection conn)
						throws SQLException, DataAccessException {
					CallableStatement cstmt = null;
					List<Checkbox> checkboxList = new ArrayList<Checkbox>();
					cstmt = conn
							.prepareCall("{CALL PKG_OPERATELOG_PUBLISH.loadPoliticalTree(?,?)}");
					cstmt.setString(1, org_id);
					cstmt.registerOutParameter(2, OracleTypes.CURSOR);
					cstmt.execute();
					ResultSet rs = (ResultSet) cstmt.getObject(2);

					while (rs.next()) {
						final String orgId = rs.getString("ORG_ID");
						Long orgRelaId = Long.valueOf(rs
								.getString("RELA_ORG_ID"));
						String relaCd = rs.getString("RELA_CD");
						String orgName = rs.getString("ORG_NAME");
						String checkboxId = rs.getString("ORG_TYPE_CD") + ","
								+ java.util.UUID.randomUUID();
						MdsionOrgRelation orgRela = MdsionOrgRelation
								.newInstance();
						orgRela.setOrgId(Long.valueOf(orgId));
						orgRela.setRelaOrgId(orgRelaId);
						orgRela.setRelaCd(relaCd);

						Treechildren tchild = new Treechildren();
						final Treeitem titem = new Treeitem();
						Treerow trow = new Treerow();
						Treecell tcell = new Treecell("");

						final Checkbox checkbox = new Checkbox(orgName);
						checkbox.setId(checkboxId);
						checkbox.setValue(orgId + "&" + orgRelaId);
						checkboxList.add(checkbox);
						tcell.appendChild(checkbox);

						tcell.setParent(trow);
						trow.setParent(titem);

						TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
								orgRela);
						titem.setValue(treeNodeImpl);
						titem.setParent(tchild);

						if (orgRelaRootTreeitem != null) {
							if (orgRelaRootTreeitem.getTreechildren() != null) {
								titem.setParent(orgRelaRootTreeitem
										.getTreechildren());
							} else {
								tchild.setParent(orgRelaRootTreeitem);
							}
						}
						if (Integer.valueOf(rs.getString("CHILDNODES")) > 0) {// 有子节点的情况
							titem.addEventListener("onOpen",
									new EventListener() {
										public void onEvent(final Event e)
												throws Exception {
											Treechildren tc = titem
													.getTreechildren();
											if (tc.getChildren().isEmpty()) {
												loadPoliticalTree(orgId, titem,
														checkboxMaps);
											}
										}
									});
							checkbox.addEventListener("onCheck",
									new EventListener() {
										public void onEvent(final Event e)
												throws Exception {
											Treechildren tc = titem
													.getTreechildren();
											if (!tc.getChildren().isEmpty()
													&& checkbox.isChecked()) {
												String[] parentCheckboxIds = checkbox
														.getId().split(",");
												List<Checkbox> cbList = checkboxMaps
														.get(orgId);
												for (Checkbox cb : cbList) {
													for (String id : parentCheckboxIds) {
														if (cb.getId().indexOf(
																id) != -1) {
															cb.setChecked(true);
															break;
														}
													}
												}
											}
										}
									});
							Treechildren treechildren = new Treechildren();
							treechildren.setParent(titem);
							titem.setOpen(false);
						}
					}

					checkboxMaps.put(org_id, checkboxList);
					return null;
				}
			});
		}
	}

	/**
	 * 复制内部组织关系
	 * 
	 * @param parentTreeitem
	 * @param copyOrgIds
	 */
	public boolean copyPoliticalOrgRela(Treeitem parentTreeitem,
			String copyOrgIds) {
		boolean success = false;
		final MdsionOrgRelation mdsionOrgRelation = (MdsionOrgRelation) ((TreeNodeImpl) parentTreeitem
				.getValue()).getEntity();
		if (!StrUtil.isEmpty(copyOrgIds)) {// 解析字符串
			String sql = "insert into organization_relation (org_rel_id,org_id,rela_org_id,rela_cd,eff_date,exp_date,status_cd,create_staff,create_date) values(seq_organization_relation_id.nextval,?,?,?,sysdate,to_date('20991231','yyyyMMdd'),'1000','-1',sysdate)";
			this.getJdbcTemplate().batchUpdate(
					sql,
					new MyBatchPreparedStatementSetter(copyOrgIds,
							mdsionOrgRelation));
			success = true;
		}
		return success;
	}

	/**
	 * 处理批量插入的回调类
	 * */
	private class MyBatchPreparedStatementSetter implements
			BatchPreparedStatementSetter {
		final String[] idAndRelasArray;
		final String copyIds;
		final MdsionOrgRelation orgRela;

		/** 通过构造函数把要插入的数据传递进来处理 */
		public MyBatchPreparedStatementSetter(String copyOrgIds,
				MdsionOrgRelation mdsionOrgRelation) {
			orgRela = mdsionOrgRelation;
			copyIds = copyOrgIds;
			idAndRelasArray = copyOrgIds.split(",");
		}

		public int getBatchSize() {
			return idAndRelasArray.length;
		}

		public void setValues(PreparedStatement ps, int i) throws SQLException {
			String[] idAndRelaArray = idAndRelasArray[i].split("&");
			if (copyIds.indexOf(idAndRelaArray[1] + "&") != -1) {// 有父节点，就直接复制关系
				ps.setString(1, idAndRelaArray[0]);
				ps.setString(2, idAndRelaArray[1]);
				ps.setString(3, orgRela.getRelaCd());
			} else {// 无父节点，挂在根节点下
				ps.setString(1, idAndRelaArray[0]);
				ps.setLong(2, orgRela.getOrgId());
				ps.setString(3, orgRela.getRelaCd());
			}
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param mdsionOrgRelation
	 */
	@Override
	public void removeMdsionOrgRelation(MdsionOrgRelation mdsionOrgRelation) {
		MdsionOrgRelType mort = getMdsionOrgRelType(mdsionOrgRelation);
		// OrgTreeConfig orgTreeConfig =
		// getMdsionOrgTreeConfig(mdsionOrgRelation);
		// OrgTreeConfig orgTreeConfig = new OrgTreeConfig();
		// MdsionOrgTree mdsionOrgTree = getMdsionOrgTree(mdsionOrgRelation);
		if (mort != null) {
			mort.remove();
			// orgTreeConfig.remove();
			// orgTreeConfig.removeByOrgTreeId(Long.valueOf(mdsionOrgRelation.getOrgId()),mdsionOrgRelation.getRelaCd());
			// mdsionOrgTree.remove();
		}
	}

	public MdsionOrgRelType getMdsionOrgRelType(
			MdsionOrgRelation mdsionOrgRelation) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM MDSION_ORG_REL_TYPE MORT  WHERE MORT.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (mdsionOrgRelation.getId() != null) {
			sb.append(" AND MORT.MDSION_ORG_REL_ID = ?");
			params.add(mdsionOrgRelation.getId());
		}
		if (!StrUtil.isEmpty(mdsionOrgRelation.getRelaCd())) {
			sb.append(" AND MORT.MDSION_ORG_REL_TYPE_CD = ?");
			params.add(mdsionOrgRelation.getRelaCd());
		}
		List<MdsionOrgRelType> list = OrganizationRelation.repository()
				.jdbcFindList(sb.toString(), params, MdsionOrgRelType.class);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public OrgTreeConfig getMdsionOrgTreeConfig(
			MdsionOrgRelation mdsionOrgRelation) {
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM ORG_TREE_CONFIG MORT  WHERE MORT.STATUS_CD = ?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (mdsionOrgRelation.getId() != null) {
			sb.append(" AND MORT.ORG_TREE_ID = ?");
			params.add(mdsionOrgRelation.getOrgId());
		}
		List<OrgTreeConfig> list = MdsionOrgRelation.repository().jdbcFindList(
				sb.toString(), params, OrgTreeConfig.class);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public MdsionOrgTree getMdsionOrgTree(MdsionOrgRelation mdsionOrgRelation) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM MDSION_ORG_TREE MORT WHERE MORT.STATUS_CD = ? AND MORT.ISSHOW = 1");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (mdsionOrgRelation.getOrgId() != null) {
			sb.append(" AND MORT.ORG_ID = ?");
			params.add(mdsionOrgRelation.getOrgId());
		}
		if (mdsionOrgRelation.getRelaCd() != null) {
			sb.append("AND MORT.MDSION_ORG_REL_TYPE_CD = ?");
			params.add(mdsionOrgRelation.getRelaCd());
		}

		List<MdsionOrgTree> list = MdsionOrgRelation.repository().jdbcFindList(
				sb.toString(), params, MdsionOrgTree.class);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 加载推导树
	 * 
	 * @param orgTreeId
	 *            组织树ID
	 * @param parentOrgId
	 *            上级组织ID
	 * @param relaCd
	 *            关系类型
	 * @param parentTreeitem
	 *            上级节点
	 * @param orgTreeRootId
	 *            根节点
	 */
	public void loadDerivationTree(final String orgTreeId,
			final String parentOrgId, final String baseRelaCd,
			final Treeitem parentTreeitem, final String orgTreeRootId,
			final boolean isShowAllBaseFinal, final long[] roleIds)
			throws Exception {
		super.getJdbcTemplate().execute(new ConnectionCallback<Object>() {
			@Override
			public Object doInConnection(Connection conn) throws SQLException,
					DataAccessException {
				CallableStatement cstmt = null;
				Map<Long, Treeitem> orgMaps = new HashMap<Long, Treeitem>();
				cstmt = conn
						.prepareCall("{CALL PKG_OPERATELOG_PUBLISH.loadDerivationTree(?,?,?,?)}");
				log.equals("orgTreeId=" + orgTreeId + ",parentOrgId="
						+ parentOrgId + ",baseRelaCd=" + baseRelaCd
						+ ",orgTreeRootId=" + orgTreeRootId + ",roleIds="
						+ roleIds.toString());
				cstmt.setString(1, orgTreeId);
				cstmt.setString(2, parentOrgId);
				cstmt.setString(3, baseRelaCd);

				cstmt.registerOutParameter(4, OracleTypes.CURSOR);
				cstmt.execute();
				ResultSet rs = (ResultSet) cstmt.getObject(4);
				while (rs.next()) {
					boolean isShow = isShowAllBaseFinal, isShowAll = isShowAllBaseFinal;
					if (!isShowAllBaseFinal) {// 父亲节点下还有子权限控制
						if (PlatformUtil.isAdmin()) {// 管理员
							isShow = true;
						} else {
							final long[] roleIds = PlatformUtil
									.getCurrentUser().getRoleIds();
							AroleProfessionalTree apt = new AroleProfessionalTree();
							apt.setOrgId(Long.valueOf(rs.getString("ORG_ID")));
							apt.setOrgTreeId(Long.valueOf(orgTreeId));
							apt.setOrgRela(baseRelaCd);
							apt.setProfessionalTreeId(Long
									.valueOf(orgTreeRootId));
							for (long roleId : roleIds) {
								apt.setAroleId(roleId);
								try {
									List<AroleProfessionalTree> aptList = aroleProfessionalTreeManager
											.findProfessionalTreeAuthByNode(apt);
									if (aptList != null) {
										if (aptList.size() > 0)// 当前权限还有子权限节点
											isShow = true;
										if (aptList.size() == 1)// 当前权限是最后的权限节点
											isShowAll = true;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					if (isShow) {
						final boolean isShowAllFinal = isShowAll;
						final String orgId = rs.getString("ORG_ID");
						Long orgRelaId = Long.valueOf(rs
								.getString("RELA_ORG_ID"));
						String relaCd = rs.getString("RELA_CD");
						String orgName = rs.getString("ORG_NAME");
						MdsionOrgRelation orgRela = MdsionOrgRelation
								.newInstance();
						orgRela.setOrgId(Long.valueOf(orgId));
						orgRela.setRelaOrgId(orgRelaId);
						orgRela.setRelaCd(relaCd);

						Treechildren tchild = new Treechildren();
						final Treeitem titem = new Treeitem();
						titem.setAttribute("orgTreeRootId", orgTreeRootId);
						Treerow trow = new Treerow();
						Treecell tcell = new Treecell(orgName);
						tcell.setParent(trow);
						trow.setParent(titem);

						TreeNodeImpl<TreeNodeEntity> treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(
								orgRela);
						titem.setValue(treeNodeImpl);
						titem.setParent(tchild);

						if (parentTreeitem != null) {
							if (parentTreeitem.getTreechildren() != null) {
								titem.setParent(parentTreeitem
										.getTreechildren());
							} else {
								tchild.setParent(parentTreeitem);
							}
						}
						if (Integer.valueOf(rs.getString("CHILDNODES")) > 0) {// 有子节点的情况
							titem.addEventListener("onOpen",
									new EventListener() {
										public void onEvent(final Event e)
												throws Exception {
											Treechildren tc = titem
													.getTreechildren();
											if (tc.getChildren().isEmpty()) {
												loadDerivationTree(orgTreeId,
														orgId, baseRelaCd,
														titem, orgTreeRootId,
														isShowAllFinal, roleIds);
											}
										}
									});
							Treechildren treechildren = new Treechildren();
							treechildren.setParent(titem);
							titem.setOpen(false);
						}
					}
				}
				return null;
			}
		});
	}

	@Override
	public List<MdsionOrgRelation> querySubTreeMdsionOrgRelationList(
			Long orgId, String relaCd) {
		List params = new ArrayList();
		String sql = "SELECT T1.* FROM MDSION_ORG_RELATION T1,MDSION_ORG_REL_TYPE T2 WHERE T1.STATUS_CD = ? AND T2.STATUS_CD=? AND T1.MDSION_ORG_REL_ID=T2.MDSION_ORG_REL_ID AND T2.MDSION_ORG_REL_TYPE_CD=?)A START WITH A.RELA_ORG_ID = ? CONNECT BY PRIOR A.ORG_ID = A.RELA_ORG_ID";
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(relaCd);
		params.add(orgId);
		return MdsionOrgRelation.repository().jdbcFindList(sql, params,
				MdsionOrgRelation.class);
	}
}
