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
import cn.ffcs.uom.rolePermission.dao.RbacPermissionRelationDao;

/**
 * 角色关系实体.
 * 
 * @author
 * 
 **/
public class RbacPermissionRelation extends UomEntity implements
		TreeNodeEntity, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色关系标识.
	 **/
	public Long getRbacPermissionRelaId() {
		return super.getId();
	}

	public void setRbacPermissionRelaId(Long rbacPermissionRelaId) {
		super.setId(rbacPermissionRelaId);
	}

	/**
	 * 角色标识.
	 **/
	@Getter
	@Setter
	private Long rbacPermissionId;

	/**
	 * 父角色标识.
	 **/
	@Getter
	@Setter
	private Long rbacParentPermissionId;

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
	public RbacPermissionRelation() {
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

		RbacPermission rbacPermission = this.getRbacPermission();

		if (rbacPermission != null) {
			return rbacPermission.getRbacPermissionName();
		}

		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getRoot() {

		if (rootId != null) {

			StringBuffer sb = new StringBuffer(
					"SELECT A.RBAC_PERMISSION_NAME TREE_LABEL,B.* FROM RBAC_PERMISSION A, RBAC_PERMISSION_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_PERMISSION_ID = B.RBAC_PERMISSION_ID AND B.RBAC_PARENT_PERMISSION_ID = ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.rootId);

			return (ArrayList<TreeNodeEntity>) RbacPermissionRelation
					.repository().jdbcFindList(sb.toString(), params,
							RbacPermissionRelation.class);
		}

		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {

		StringBuffer sb = new StringBuffer(
				"SELECT A.RBAC_PERMISSION_NAME TREE_LABEL,B.* FROM RBAC_PERMISSION A, RBAC_PERMISSION_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_PERMISSION_ID = B.RBAC_PERMISSION_ID AND B.RBAC_PARENT_PERMISSION_ID = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getRbacPermissionId());

		return (ArrayList<TreeNodeEntity>) RbacPermissionRelation.repository()
				.jdbcFindList(sb.toString(), params,
						RbacPermissionRelation.class);

	}

	/**
	 * 创建对象实例.
	 * 
	 * @return RbacPermissionRelation
	 */
	public static RbacPermissionRelation newInstance() {
		return new RbacPermissionRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacPermissionRelationDao repository() {
		return (RbacPermissionRelationDao) ApplicationContextUtil
				.getBean("rbacPermissionRelationDao");
	}

	/**
	 * 获取权限
	 * 
	 * @return
	 */
	public RbacPermission getRbacPermission() {
		if (this.rbacPermissionId != null) {
			return (RbacPermission) RbacPermission.repository().getObject(
					RbacPermission.class, this.rbacPermissionId);
		}
		return null;
	}

	/**
	 * 获取上级权限
	 * 
	 * @return
	 */
	public RbacPermission getRbacParentPermission() {
		if (this.rbacParentPermissionId != null) {
			return (RbacPermission) RbacPermission.repository().getObject(
					RbacPermission.class, this.rbacParentPermissionId);
		}
		return null;
	}

}
