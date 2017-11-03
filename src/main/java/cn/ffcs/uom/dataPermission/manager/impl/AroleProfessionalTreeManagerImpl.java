package cn.ffcs.uom.dataPermission.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.dataPermission.manager.AroleProfessionalTreeManager;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;

@Service("aroleProfessionalTreeManager")
@Scope("prototype")
public class AroleProfessionalTreeManagerImpl implements
		AroleProfessionalTreeManager {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public PageInfo queryPageInfoByRoleProfessionalTree(
			AroleProfessionalTree aroleProfessionalTree, int currentPage,
			int pageSize) throws Exception {
		StringBuffer hql = new StringBuffer(
				" From AroleProfessionalTree where statusCd=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (aroleProfessionalTree != null) {
			if (aroleProfessionalTree.getAroleId() != null) {
				hql.append(" and AROLE_ID=?");
				params.add(aroleProfessionalTree.getAroleId());
			}
		}
		hql.append(" order by AROLE_PROFESSIONAL_TREE_ID");
		return aroleProfessionalTree.repository().findPageInfoByHQLAndParams(
				hql.toString(), params, currentPage, pageSize);
	}

	public void removeRoleProfessionalTree(
			AroleProfessionalTree aroleProfessionalTree) {
		aroleProfessionalTree.removeOnly();
	}

	public void addRoleProfessionalTree(
			AroleProfessionalTree aroleProfessionalTree) {
		aroleProfessionalTree.addOnly();
	}

	@Override
	public List<AroleProfessionalTree> queryRoleProfessionalTreeList(
			AroleProfessionalTree aroleProfessionalTree) throws Exception {
		StringBuffer hql = new StringBuffer(
				" From AroleProfessionalTree where statusCd=?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (aroleProfessionalTree != null) {
			if (aroleProfessionalTree.getAroleId() != null) {
				hql.append(" and AROLE_ID=?");
				params.add(aroleProfessionalTree.getAroleId());
			}
			if (aroleProfessionalTree.getOrgTreeId() != null) {
				hql.append(" and org_tree_id=?");
				params.add(aroleProfessionalTree.getOrgTreeId());
			}
		}
		hql.append(" order by AROLE_PROFESSIONAL_TREE_ID");
		return aroleProfessionalTree.repository().findListByHQLAndParams(
				hql.toString(), params);
	}

	/**
	 * 获取专业树某个节点下的角色权限列表
	 * 
	 * @param aroleProfessionalTree
	 * @return
	 * @throws Exception
	 */
	public List<AroleProfessionalTree> findProfessionalTreeAuthByNode(
			AroleProfessionalTree aroleProfessionalTree) throws Exception {
		// StringBuffer sql = new
		// StringBuffer("select * from table(PKG_OPERATELOG_PUBLISH.findProfessionalTreeAuthByNode(?,?,?,?,?))");
		List params = new ArrayList();
		String sql = "WITH ORG AS"
				+ " ("
				+ "  SELECT MAX(ORG_NAME) ORG_NAME, ORG_ID, MAX(ORG_PRIORITY) ORG_PRIORITY"
				+ "    FROM V_ORGANIZATION"
				+ "   WHERE STATUS_CD = '1000'"
				+ "     AND EXP_DATE > SYSDATE"
				+ "   GROUP BY ORG_ID),"
				+ "TOR AS"
				+ " ("
				+ "  SELECT ORG_ID, RELA_ORG_ID, RELA_CD"
				+ "    FROM v_mdsion_organization_relation"
				+ "   WHERE STATUS_CD = '1000'"
				+ "     AND RELA_CD IN (SELECT mdsion_org_rel_type_cd"
				+ "                       FROM mdsion_org_tree"
				+ "                      WHERE mdsion_org_tree_id = ?"
				+ "                        AND STATUS_CD = '1000')),"
				+ "ORG_TYPE_TEMP AS"
				+ " ("
				+ "  SELECT ORG_TYPE_CD"
				+ "    FROM ORG_TREE_CONFIG"
				+ "   WHERE ORG_TREE_ID IN (SELECT mdsion_org_tree_id"
				+ "                           FROM mdsion_org_tree"
				+ "                          WHERE mdsion_org_tree_id = ?"
				+ "                            AND STATUS_CD = '1000')"
				+ "     AND STATUS_CD = '1000'),"
				+ "DEDUCTION_ORG_RELA AS"
				+ " ("
				+ "  SELECT TOR.ORG_ID, TOR.RELA_ORG_ID, TOR.RELA_CD"
				+ "    FROM TOR"
				+ "   INNER JOIN V_ORG_TYPE TOT"
				+ "      ON TOR.ORG_ID = TOT.ORG_ID"
				+ "   WHERE TOT.STATUS_CD = '1000'"
				+ "     AND EXISTS"
				+ "   (SELECT 1"
				+ "            FROM ORG_TYPE_TEMP"
				+ "           WHERE TOT.ORG_TYPE_CD = ORG_TYPE_TEMP.ORG_TYPE_CD)"
				+ "   GROUP BY TOR.ORG_ID, TOR.RELA_ORG_ID, TOR.RELA_CD),"
				+ "ORG_IDS AS"
				+ " ("
				+ "  SELECT DISTINCT DECODE(n, 1, ORG_ID, RELA_ORG_ID) ORG_ID"
				+ "    FROM (SELECT ORG_ID, RELA_ORG_ID FROM DEDUCTION_ORG_RELA),"
				+ "          (SELECT LEVEL n FROM DUAL CONNECT BY LEVEL <= 2)),"
				+ "ALL_PUBLISH_DATA AS"
				+ " ("
				+ "  SELECT TOR.ORG_ID, TOR.RELA_ORG_ID, TOR.RELA_CD, ORG_NAME, ORG.ORG_PRIORITY"
				+ "    FROM TOR, ORG, ORG_IDS OI"
				+ "   WHERE TOR.ORG_ID = ORG.ORG_ID"
				+ "     AND TOR.ORG_ID = OI.ORG_ID)," + "ORG_RELA_TREE AS"
				+ " (" + "  SELECT ORG_ID," + "          RELA_ORG_ID,"
				+ "          RELA_CD,"
				+ "          SYS_CONNECT_BY_PATH(ORG_ID, '>') LEVEL_PATH"
				+ "    FROM DEDUCTION_ORG_RELA" + "   WHERE RELA_CD = ?"
				+ "   START WITH RELA_ORG_ID = 0"
				+ "  CONNECT BY NOCYCLE PRIOR ORG_ID = RELA_ORG_ID)"
				+ " SELECT APT.arole_professional_tree_id,"
				+ "       APT.arole_id," + "       APT.org_tree_id,"
				+ "       APT.org_id," + "       APT.professional_tree_id,"
				+ "       APT.org_rela," + "       APT.eff_date,"
				+ "       APT.exp_date," + "       APT.status_cd,"
				+ "       APT.status_date," + "       APT.create_date,"
				+ "       APT.create_staff," + "       APT.update_date,"
				+ "       APT.update_staff," + "       ORT.LEVEL_PATH"
				+ "  FROM AROLE_PROFESSIONAL_TREE APT, ORG_RELA_TREE ORT"
				+ " WHERE AROLE_ID = ?" + "   AND PROFESSIONAL_TREE_ID = ?"
				+ "   AND ORG_TREE_ID = ?" + "   AND STATUS_CD = '1000'"
				+ "   AND APT.ORG_ID = ORT.ORG_ID"
				+ "   AND ORT.LEVEL_PATH LIKE '%' || ? || '%'"
				+ "   AND ORT.LEVEL_PATH LIKE '>' || ? || '%'";
		params.add(aroleProfessionalTree.getOrgTreeId());
		params.add(aroleProfessionalTree.getOrgTreeId());
		params.add(aroleProfessionalTree.getOrgRela());
		params.add(aroleProfessionalTree.getAroleId());
		params.add(aroleProfessionalTree.getProfessionalTreeId());
		params.add(aroleProfessionalTree.getOrgTreeId());
		params.add(aroleProfessionalTree.getOrgId());
		params.add(aroleProfessionalTree.getProfessionalTreeId());
		return aroleProfessionalTree.repository().jdbcFindList(sql.toString(),
				params, AroleProfessionalTree.class);
	}

	/**
	 * 保存专业树权限
	 * 
	 * @param orgTreeRootNode
	 * @param aroleProfessionalTree
	 * @param aroleProfessionalTreeList
	 */
	@Override
	public void saveRoleProfessionalTree(Tree orgTreeRootNode,
			AroleProfessionalTree aroleProfessionalTree,
			List<AroleProfessionalTree> aroleProfessionalTreeList) {
		try {
			/*
			 * StringBuffer sql = new
			 * StringBuffer("delete AROLE_PROFESSIONAL_TREE where arole_id ='"
			 * ).append
			 * (aroleProfessionalTree.getAroleId()).append("' and org_tree_id = '"
			 * ).append(aroleProfessionalTree.getOrgTreeId()).append("'");
			 * jdbcTemplate.execute(sql.toString());
			 */
			Collection<Treeitem> treeitems = orgTreeRootNode.getTreechildren()
					.getItems();
			for (Treeitem treeitem : treeitems) {
				boolean isExists = false;
				Checkbox cb = (Checkbox) treeitem.getTreerow().getFirstChild()
						.getFirstChild();
				if (cb.isChecked()) {
					for (AroleProfessionalTree aptHis : aroleProfessionalTreeList) {// 历史数据校验
						if (cb.getValue().equals(aptHis.getOrgId().toString())) {
							isExists = true;
							break;
						}
					}
					if (!isExists) {// 数据库中不存在的做新增操作
						String[] valArr = cb.getValue().split("&");
						AroleProfessionalTree apt = new AroleProfessionalTree();
						apt.setAroleId(aroleProfessionalTree.getAroleId());
						apt.setOrgTreeId(aroleProfessionalTree.getOrgTreeId());
						apt.setOrgId(Long.valueOf(valArr[0]));
						apt.setOrgRela(valArr[1]);
						apt.setProfessionalTreeId(Long.valueOf(cb.getAttribute(
								"professionalTreeId").toString()));
						apt.addOnly();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
