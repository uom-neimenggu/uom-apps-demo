package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.rolePermission.dao.RbacResourceRelationDao;

/**
 * 资源关系实体.
 * 
 * @author
 * 
 **/
public class RbacResourceRelation extends UomEntity implements TreeNodeEntity,
		Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 资源关系标识.
	 **/
	public Long getRbacResourceRelaId() {
		return super.getId();
	}

	public void setRbacResourceRelaId(Long rbacResourceRelaId) {
		super.setId(rbacResourceRelaId);
	}

	/**
	 * 资源标识.
	 **/
	@Getter
	@Setter
	private Long rbacResourceId;

	/**
	 * 父资源标识.
	 **/
	@Getter
	@Setter
	private Long rbacParentResourceId;

	/**
	 * 是否是根节点
	 */
	@Setter
	private Boolean isRoot = false;

	public Boolean getIsRoot() {
		return this.isRoot;
	}

	/**
	 * 根节点
	 */
	@Getter
	@Setter
	private Long rootId = 0L;

	/**
	 * 树label
	 */
	@Getter
	@Setter
	private String treeLabel;

	/**
	 * 构造方法
	 */
	public RbacResourceRelation() {
		super();
	}

	@Override
	public boolean isGetRoot() {
		return this.isRoot;
	}

	@Override
	public String getLabel() {

		if (!StrUtil.isEmpty(this.treeLabel)) {
			return this.treeLabel;
		}

		RbacResource rbacResource = this.getRbacResource();

		if (rbacResource != null) {
			return rbacResource.getRbacResourceName();
		}

		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getRoot() {

		if (rootId != null) {

			StringBuffer sb = new StringBuffer(
					"SELECT A.RBAC_RESOURCE_NAME TREE_LABEL,B.* FROM RBAC_RESOURCE A, RBAC_RESOURCE_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_RESOURCE_ID = B.RBAC_RESOURCE_ID AND B.RBAC_PARENT_RESOURCE_ID = ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.rootId);

			return (ArrayList<TreeNodeEntity>) RbacResourceRelation
					.repository().jdbcFindList(sb.toString(), params,
							RbacResourceRelation.class);

		}

		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {

		StringBuffer sb = new StringBuffer(
				"SELECT A.RBAC_RESOURCE_NAME TREE_LABEL,B.* FROM RBAC_RESOURCE A, RBAC_RESOURCE_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_RESOURCE_ID = B.RBAC_RESOURCE_ID AND B.RBAC_PARENT_RESOURCE_ID = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getRbacResourceId());

		return (ArrayList<TreeNodeEntity>) RbacResourceRelation
				.repository()
				.jdbcFindList(sb.toString(), params, RbacResourceRelation.class);

	}

	/**
	 * 创建对象实例.
	 * 
	 * @return RbacResourceRelation
	 */
	public static RbacResourceRelation newInstance() {
		return new RbacResourceRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacResourceRelationDao repository() {
		return (RbacResourceRelationDao) ApplicationContextUtil
				.getBean("rbacResourceRelationDao");
	}

	/**
	 * 获取资源
	 * 
	 * @return
	 */
	public RbacResource getRbacResource() {
		if (this.rbacResourceId != null) {
			return (RbacResource) RbacResource.repository().getObject(
					RbacResource.class, this.rbacResourceId);
		}
		return null;
	}

}
