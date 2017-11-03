package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.orgTreeCalc.TreeNode;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;

/**
 * 组织关系实体.
 * 
 * @author
 * 
 **/
public class JtMssCostRelation implements TreeNodeEntity,
		Serializable {

    /**
     * 组织标识.
     **/
    @Getter
    @Setter
    private Long ccId;
	/**
	 * 成本中心.
	 **/
	@Getter
	@Setter
	private String subSetName;
	/**
	 * 成本中心描述.
	 **/
	@Getter
	@Setter
	private String subName;
	/**
	 * 成本中心组.
	 **/
	@Getter
	@Setter
	private String setName;
	/**
	 * 成本中心组描述
	 */
	@Getter
	@Setter
	private String pname;

	/**
	 * 各个页面的可配置根id是不一样的：内部是999999999以下的组织，中通服是9999999996以下的组织；
	 */
	@Getter
	@Setter
	private String perPageDataPermissionRootOrgId;

	/**
	 * 是否是根节点
	 */
	@Setter
	private Boolean isRoot = false;

	public Boolean getIsRoot() {
		return this.isRoot;
	}

	/**
	 * 是否是代理商
	 */
	public Boolean isAgent = false;

	public Boolean getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(Boolean isAgent) {
		this.isAgent = isAgent;
	}

	/**
	 * 是否是内部经营实体
	 */
	public Boolean isIbe = false;

	public Boolean getIsIbe() {
		return isIbe;
	}

	public void setIsIbe(Boolean isIbe) {
		this.isIbe = isIbe;
	}

	/**
	 * 树label
	 */
	@Getter
	@Setter
	private String treeLabel;
	/**
	 * 根节点
	 */
	@Getter
	@Setter
	private Long rootId;

	@Setter
	@Getter
	private treeCalcAction treeCalcVo;


	/**
	 * 排序用
	 */
	@Getter
	@Setter
	private Organization treeOrganization;

	/**
	 * 组织类型
	 */
	@Setter
	private List<OrgType> orgTypeList;

	/**
	 * 构造方法
	 */
	public JtMssCostRelation() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OrganizationRelation
	 */
	public static JtMssCostRelation newInstance() {
		return new JtMssCostRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static BaseDao repository() {
		return (BaseDao) ApplicationContextUtil
				.getBean("baseDao");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		if (this.subSetName != null) {
			if (treeCalcVo == null) {
				List params = new ArrayList();
				StringBuilder sb = new StringBuilder(
						"SELECT A.SUBNAME TREE_LABEL,A.* FROM JTMSSCOST_RELATION A WHERE A.SETNAME = ?");
				params.add(this.subSetName);

				return (ArrayList<TreeNodeEntity>) JtMssCostRelation
						.repository().jdbcFindList(sb.toString(), params,
								JtMssCostRelation.class);
			} else {
				//return adaptTreeNode(treeCalcVo.getTreeNode(subSetName));
			    return null;
			}
		}
		return null;
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.treeLabel)) {
			return this.treeLabel;
		}
/*		Organization org = this.getOrganization();
		if (org != null) {
			return org.getOrgName();
		}*/
		return "null";
	}

	@Override
	public ArrayList<TreeNodeEntity> getRoot() {
		if (treeCalcVo == null) {
			if (PlatformUtil.getCurrentUser() != null) {
				/**
				 * 指定的根id
				 */
				if (rootId != null) {
					StringBuffer sb = new StringBuffer(
							"SELECT A.SUBNAME TREE_LABEL,A.* FROM JTMSSCOST_RELATION A WHERE A.SUBSETNAME = ?");
					List params = new ArrayList();
					params.add(this.rootId);
					return (ArrayList<TreeNodeEntity>) JtMssCostRelation
							.repository().jdbcFindList(sb.toString(), params,
									JtMssCostRelation.class);
				} else {// 数据权限：电信管理区域
					Long telecomRegionId = null;
					if (PlatformUtil.isAdmin()) {// 如果是admin
						telecomRegionId = TelecomRegionConstants.ROOT_TELECOM_REGION_ID;
					} else {
						telecomRegionId = PermissionUtil
								.getPermissionTelcomRegionId(PlatformUtil
										.getCurrentUser().getRoleIds());
					}

					if (telecomRegionId != null) {
						StringBuffer sb = null;
						if (this.isAgent || this.isIbe) {
							sb = new StringBuffer(
									"SELECT C.ORG_NAME TREE_LABEL,A.* FROM ORGANIZATION C, ORGANIZATION_RELATION A, (SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR WHERE TR.STATUS_CD = ? START WITH TR.TELCOM_REGION_ID = ? CONNECT BY NOCYCLE  PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID) TR  WHERE A.RELA_CD !=0401 AND C.STATUS_CD = ? AND C.ORG_ID = A.ORG_ID AND A.STATUS_CD = ? AND C.TELCOM_REGION_ID = TR.TELCOM_REGION_ID");
						} else {
							sb = new StringBuffer(
									"SELECT A.SUBNAME TREE_LABEL,A.* FROM JTMSSCOST_RELATION A WHERE 1 = 1");
						}

						// sb.append(" AND C.TELCOM_REGION_ID IN (SELECT TELCOM_REGION_ID FROM TELCOM_REGION  WHERE STATUS_CD = ? START WITH TELCOM_REGION_ID = ? CONNECT BY PRIOR TELCOM_REGION_ID = UP_REGION_ID)");
						List params = new ArrayList();
						/**
						 * 数据权限：组织
						 */
						Long orgId = null;
						if (PlatformUtil.isAdmin()) {// 如果是admin
						    subSetName = this.perPageDataPermissionRootOrgId;
						} else {
/*						    subSetName = PermissionUtil.getPermissionOrganizationId(
									PlatformUtil.getCurrentUser().getRoleIds(),
									this.perPageDataPermissionRootOrgId);*/
	                          subSetName = this.perPageDataPermissionRootOrgId;
						}
						if (subSetName != null) {
							sb.append(" AND A.SUBSETNAME = ?");
							params.add(subSetName);
						} else {// 未配置数据权的组织
							return null;
						}
						return (ArrayList<TreeNodeEntity>) JtMssCostRelation
								.repository().jdbcFindList(sb.toString(),
										params, JtMssCostRelation.class);
					}
				}
			}
			return null;
		} else {
			// 推导树
			return adaptTreeNode(treeCalcVo.getRoot());
		}
	}

	@Override
	public boolean isGetRoot() {
		return this.isRoot;
	}

	private ArrayList<TreeNodeEntity> adaptTreeNode(TreeNode node) {
		ArrayList<TreeNodeEntity> entities = new ArrayList<TreeNodeEntity>();
		if (treeCalcVo != null && node != null) {
			HashMap<String, List<TreeNode>> childNodes = node.getChildNodes();
			if (childNodes == null) {
				return entities;
			}
			Iterator<String> iterator = childNodes.keySet().iterator();
			while (iterator.hasNext()) {
				List<TreeNode> nodes = childNodes.get(iterator.next());
				for (int i = 0; i < nodes.size(); ++i) {
					JtMssCostRelation entity = new JtMssCostRelation();
					String orgId = nodes.get(i).getValue();
					try {
						Organization organization = treeCalcVo
								.getOrganizationMap().get(Long.valueOf(orgId));
						if (organization != null) {
							entity.setTreeOrganization(organization);
							entity.setTreeLabel(organization.getOrgName());
							//entity.setOrgTreeId(orgId);
							//entity.setOrgId(organization.getOrgId());
							entity.setTreeCalcVo(treeCalcVo);
							entities.add(entity);
						}
					} catch (Exception e) {

					}
				}
			}
		}
/*		Collections.sort(entities, new Comparator<TreeNodeEntity>() {
			public int compare(TreeNodeEntity entity1, TreeNodeEntity entity2) {
				OrganizationRelation2 or1 = (OrganizationRelation2) entity1;
				OrganizationRelation2 or2 = (OrganizationRelation2) entity2;
				if (or1.getTreeOrganization().getOrgPriority() == null) {
					return -1;
				}
				if (or2.getTreeOrganization().getOrgPriority() == null) {
					return 1;
				}
				return or1.getTreeOrganization().getOrgPriority()
						.compareTo(or2.getTreeOrganization().getOrgPriority());
			}
		});*/
		return entities;
	}

	/**
	 * 判断是否是下级的组织
	 * 
	 * @param orgId
	 * @param parentOrgId
	 * @return
	 */
	public boolean isSubOrganization(Long orgId, Long parentOrgId) {
		String sql = "select * from organization_relation a where a.status_cd = ? and org_id =?  start with a.rela_org_id = ? connect by nocycle prior a.org_id = a.rela_org_id";
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(orgId);
		params.add(parentOrgId);
		List<JtMssCostRelation> list = JtMssCostRelation.repository()
				.findListByJDBCSQLAndParams(sql, params);
		if (list != null && list.size() > 0) {
			/**
			 * 添加内部组织时会有两个根节点：9999999999【内部组织树】和9999999995【营销树】
			 * 用于排除添加内部组织节点时，受营销树的影响
			 * 如果是添加营销树根节点下面的子节点时，此方法不在适用，此时行排除内部组织树的影响，得重新写。
			 */
			if (OrganizationConstant.ROOT_EDW_ORG_ID.equals(parentOrgId)
					||OrganizationConstant.ROOT_MARKETING_ORG_ID.equals(parentOrgId)
					||OrganizationConstant.ROOT_COST_ORG_ID.equals(parentOrgId)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
}
