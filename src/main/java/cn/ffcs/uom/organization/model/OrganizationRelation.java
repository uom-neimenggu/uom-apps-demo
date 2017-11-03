package cn.ffcs.uom.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.dataPermission.util.PermissionUtil;
import cn.ffcs.uom.orgTreeCalc.TreeNode;
import cn.ffcs.uom.orgTreeCalc.treeCalcAction;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.dao.OrganizationDao;
import cn.ffcs.uom.organization.dao.OrganizationRelationDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;

/**
 * 组织关系实体.
 * 
 * @author
 * 
 **/
public class OrganizationRelation extends UomEntity implements TreeNodeEntity,
		Serializable {
	/**
	 * 组织标识.
	 **/
	public Long getOrgRelId() {
		return super.getId();
	}

	public void setOrgRelId(Long orgRelId) {
		super.setId(orgRelId);
	}

	/**
	 * 组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	/**
	 * 关联组织标识.
	 **/
	@Getter
	@Setter
	private Long relaOrgId;
	/**
	 * 关系类型.
	 **/
	@Getter
	@Setter
	private String relaCd;
	/**
	 * 树类型
	 */
	@Getter
	@Setter
	private String orgTreeId;

	/**
	 * 各个页面的可配置根id是不一样的：内部是999999999以下的组织，中通服是9999999996以下的组织；
	 */
	@Getter
	@Setter
	private Long perPageDataPermissionRootOrgId;
	/**
	 * 关系类型值，从OrganizationRelationTree传入，内部-0101，营销-0401.
	 **/
	@Getter
	@Setter
	private String relaCdStr;
	
	/**
	 * 原因
	 */
	@Setter
	@Getter
	private String reason;

	/**
	 * 是否是根节点
	 */
	@Setter
	private Boolean isRoot = false;

	public Boolean getIsRoot() {
		return this.isRoot;
	}

	/**
	 * 内部树是否显示网点
	 */
	@Getter
	@Setter
	public Boolean isShowWd = true;
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
	 * 新增组织关系时需要修改的组织集团编码的组织
	 */
	@Getter
	@Setter
	private Organization updateOrganization;
	/**
	 * 集团组织
	 */
	@Getter
	@Setter
	private GroupOrganization groupOrganization;

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
	public OrganizationRelation() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OrganizationRelation
	 */
	public static OrganizationRelation newInstance() {
		return new OrganizationRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OrganizationRelationDao repository() {
		return (OrganizationRelationDao) ApplicationContextUtil
				.getBean("organizationRelationDao");
	}

	/**
	 * 获取组织
	 * 
	 * @return
	 */
	public Organization getOrganization() {
		if (this.orgId != null) {
			return (Organization) Organization.repository().getObject(
					Organization.class, this.orgId);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {
		if (this.orgId != null) {
			if (treeCalcVo == null) {
				List params = new ArrayList();
				StringBuilder sb = new StringBuilder(
						"SELECT C.ORG_NAME TREE_LABEL,A.* FROM ORGANIZATION C, ORGANIZATION_RELATION A WHERE C.STATUS_CD = ? AND C.ORG_ID = A.ORG_ID AND A.STATUS_CD = ? AND A.RELA_ORG_ID = ? ");
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.orgId);
				if (!StrUtil.isNullOrEmpty(this.relaCd)) {
					sb.append(" AND A.RELA_CD = ?");
					params.add(this.relaCd);
				}
				if (!isShowWd) {
					sb.append(" AND NOT EXISTS( SELECT T.ORG_ID FROM ORG_TYPE T WHERE T.ORG_TYPE_CD IN(?,?,?,?,?,?) AND T.ORG_ID=C.ORG_ID AND T.STATUS_CD=?)");
					params.add(OrganizationConstant.ORG_TYPE_N0202010000);
					params.add(OrganizationConstant.ORG_TYPE_N0202020000);
					params.add(OrganizationConstant.ORG_TYPE_N0202030000);
					params.add(OrganizationConstant.ORG_TYPE_N0202040000);
					params.add(OrganizationConstant.ORG_TYPE_N0202050000);
					params.add(OrganizationConstant.ORG_TYPE_N0202060000);
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				}
				sb.append(" ORDER BY C.ORG_PRIORITY");
				List<OrganizationRelation> list = OrganizationRelation
						.repository().jdbcFindList(sb.toString(), params,
								OrganizationRelation.class);
				if (list != null && list.size() > 0) {
					for (OrganizationRelation organizationRelation : list) {
						organizationRelation.setIsShowWd(isShowWd);
					}
				}
				Object o = list;
				return (ArrayList<TreeNodeEntity>) o;
			} else {
				return adaptTreeNode(treeCalcVo.getTreeNode(orgId));
			}
		}
		return null;
	}

	public static void main(String[] args) {
		OrganizationRelation or = new OrganizationRelation();
		or.setOrgId(9999999999L);
		or.setRelaCd("0101");
		or.setIsShowWd(false);
		or.getChildren();
	}

	@Override
	public String getLabel() {
		if (!StrUtil.isEmpty(this.treeLabel)) {
			return this.treeLabel;
		}
		Organization org = this.getOrganization();
		if (org != null) {
			return org.getOrgName();
		}
		return "";
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
							"SELECT C.ORG_NAME TREE_LABEL,A.* FROM ORGANIZATION C, ORGANIZATION_RELATION A WHERE C.STATUS_CD = ? AND C.ORG_ID = A.ORG_ID AND A.STATUS_CD = ? AND A.ORG_ID = ?");
					List params = new ArrayList();
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
					params.add(this.rootId);
					if (!StrUtil.isNullOrEmpty(this.relaCdStr)) {
						sb.append(" AND A.RELA_CD = ?");
						params.add(this.relaCdStr);
					}
					sb.append(" ORDER BY C.ORG_PRIORITY");
					List<OrganizationRelation> list = OrganizationRelation
							.repository().jdbcFindList(sb.toString(), params,
									OrganizationRelation.class);
					if (list != null && list.size() > 0) {
						for (OrganizationRelation organizationRelation : list) {
							organizationRelation.setIsShowWd(isShowWd);
						}
					}
					Object o = list;
					return (ArrayList<TreeNodeEntity>) o;
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
									"SELECT C.ORG_NAME TREE_LABEL,A.* FROM ORGANIZATION C, ORGANIZATION_RELATION A, (SELECT TR.TELCOM_REGION_ID FROM TELCOM_REGION TR WHERE TR.STATUS_CD = ? START WITH TR.TELCOM_REGION_ID = ? CONNECT BY NOCYCLE  PRIOR TR.TELCOM_REGION_ID = TR.UP_REGION_ID) TR  WHERE C.STATUS_CD = ? AND C.ORG_ID = A.ORG_ID AND A.STATUS_CD = ? AND C.TELCOM_REGION_ID = TR.TELCOM_REGION_ID");
						}

						// sb.append(" AND C.TELCOM_REGION_ID IN (SELECT TELCOM_REGION_ID FROM TELCOM_REGION  WHERE STATUS_CD = ? START WITH TELCOM_REGION_ID = ? CONNECT BY PRIOR TELCOM_REGION_ID = UP_REGION_ID)");
						List params = new ArrayList();
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
						params.add(telecomRegionId);
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
						params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
						if (!StrUtil.isNullOrEmpty(this.relaCdStr)) {
							sb.append(" AND A.RELA_CD = ?");
							params.add(this.relaCdStr);
						}
						/**
						 * 数据权限：组织
						 */
						Long orgId = null;
						if (PlatformUtil.isAdmin()) {// 如果是admin
							orgId = this.perPageDataPermissionRootOrgId;
						} else {
							orgId = PermissionUtil.getPermissionOrganizationId(
									PlatformUtil.getCurrentUser().getRoleIds(),
									this.perPageDataPermissionRootOrgId);
						}
						if (orgId != null) {
							sb.append(" AND A.ORG_ID = ?");
							params.add(orgId);
						} else {// 未配置数据权的组织
							return null;
						}
						sb.append(" ORDER BY C.ORG_PRIORITY");
						List<OrganizationRelation> list = OrganizationRelation
								.repository().jdbcFindList(sb.toString(),
										params, OrganizationRelation.class);
						if (list != null && list.size() > 0) {
							for (OrganizationRelation organizationRelation : list) {
								organizationRelation.setIsShowWd(isShowWd);
							}
						}
						Object o = list;
						return (ArrayList<TreeNodeEntity>) o;
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

	/**
	 * 关系类型
	 * 
	 * @return
	 */
	public String getRelaCdName() {
		if (!StrUtil.isEmpty(this.getRelaCd())) {
			List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
					"OrganizationRelation", "relaCd", this.getRelaCd(),
					BaseUnitConstants.ENTT_STATE_ACTIVE);
			if (list != null && list.size() > 0) {
				return list.get(0).getAttrValueName();
			}
		}
		return "";
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
					OrganizationRelation entity = new OrganizationRelation();
					String orgId = nodes.get(i).getValue();
					try {
						Organization organization = treeCalcVo
								.getOrganizationMap().get(Long.valueOf(orgId));
						if (organization != null) {
							entity.setTreeOrganization(organization);
							entity.setTreeLabel(organization.getOrgName());
							entity.setOrgTreeId(orgId);
							entity.setOrgId(organization.getOrgId());
							entity.setTreeCalcVo(treeCalcVo);
							entities.add(entity);
						}
					} catch (Exception e) {

					}
				}
			}
		}
		Collections.sort(entities, new Comparator<TreeNodeEntity>() {
			public int compare(TreeNodeEntity entity1, TreeNodeEntity entity2) {
				OrganizationRelation or1 = (OrganizationRelation) entity1;
				OrganizationRelation or2 = (OrganizationRelation) entity2;
				if (or1.getTreeOrganization().getOrgPriority() == null) {
					return -1;
				}
				if (or2.getTreeOrganization().getOrgPriority() == null) {
					return 1;
				}
				return or1.getTreeOrganization().getOrgPriority()
						.compareTo(or2.getTreeOrganization().getOrgPriority());
			}
		});
		return entities;
	}

	/**
	 * 获取组织类型
	 * 
	 * @return
	 */
	public List<OrgType> getOrgTypeList() {
		if (this.orgTypeList == null || this.orgTypeList.size() <= 0) {
			if (this.getOrgId() != null) {
				String sql = "SELECT * FROM ORG_TYPE A WHERE A.STATUS_CD=? AND A.ORG_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getOrgId());
				orgTypeList = this.repository().jdbcFindList(sql, params,
						OrgType.class);
			}
		}
		return orgTypeList;
	}

	/**
	 * 是否是管理部门
	 * 
	 * @return
	 */
	public boolean isManager() {
		List<OrgType> list = this.getOrgTypeList();
		if (list != null && list.size() > 0) {
			for (OrgType orgType : list) {
				if (!StrUtil.isEmpty(orgType.getOrgTypeCd())) {
					if (orgType.getOrgTypeCd().startsWith(
							OrganizationConstant.MANAGER_PRE)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 是否是单位
	 * 
	 * @return
	 */
	public boolean isCompany() {
		List<OrgType> list = this.getOrgTypeList();
		if (list != null && list.size() > 0) {
			for (OrgType orgType : list) {
				if (!StrUtil.isEmpty(orgType.getOrgTypeCd())) {
					if (orgType.getOrgTypeCd().startsWith(
							OrganizationConstant.COMPANY_PRE)) {
						return true;
					}
				}
			}
		}
		return false;
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
		List<OrganizationRelation> list = OrganizationRelation.repository()
				.findListByJDBCSQLAndParams(sql, params);
		if (list != null && list.size() > 0) {
			/**
			 * 添加内部组织时会有两个根节点：9999999999【内部组织树】和9999999995【营销树】
			 * 用于排除添加内部组织节点时，受营销树的影响
			 * 如果是添加营销树根节点下面的子节点时，此方法不在适用，此时行排除内部组织树的影响，得重新写。
			 */
			if (OrganizationConstant.ROOT_EDW_ORG_ID.equals(parentOrgId)
					|| OrganizationConstant.ROOT_MARKETING_ORG_ID
							.equals(parentOrgId)
					|| OrganizationConstant.ROOT_COST_ORG_ID
							.equals(parentOrgId)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取对应组织的组织编码，用以组织树组织关系界面显示
	 * .
	 * 
	 * @return
	 * @author xiaof
	 * 2017年2月14日 xiaof
	 */
	public String getOrgCode()
	{
	    //1、获取对应organization的dao
	    OrganizationDao organizationDao = Organization.repository();
	    //2、根据当前组织的id编码查询对应的组织信息
	    Organization organization = organizationDao.getById(orgId);
	    //3、获取当前组织的组织编码返回
	    if(organization != null)
	        return organization.getOrgCode();
	    
	    return null;
	}
	
	public String getRelaOrgCode()
	{
	    //1、获取对应organization的dao
	    OrganizationDao organizationDao = Organization.repository();
	    //2、根据当前组织的id编码查询对应的组织信息
	    Organization organization = organizationDao.getById(relaOrgId);
	    //3、获取当前组织的组织编码返回
	    if(organization != null)
	        return organization.getOrgCode();
	    
	    return null;
	}
	
	public String getRelaOrgName()
	{
	    //1、获取对应organization的dao
	    OrganizationDao organizationDao = Organization.repository();
	    //2、根据当前组织的id编码查询对应的组织信息
	    Organization organization = organizationDao.getById(relaOrgId);
	    //3、获取当前组织的组织编码返回
	    if(organization != null)
	        return organization.getOrgName();
	    return null;
	}
}
