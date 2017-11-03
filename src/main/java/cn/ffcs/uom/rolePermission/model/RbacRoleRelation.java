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
import cn.ffcs.uom.rolePermission.dao.RbacRoleRelationDao;

/**
 * 角色关系实体.
 * 
 * @author
 * 
 **/
public class RbacRoleRelation extends UomEntity implements TreeNodeEntity,
		Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色关系标识.
	 **/
	public Long getRbacRoleRelaId() {
		return super.getId();
	}

	public void setRbacRoleRelaId(Long rbacRoleRelaId) {
		super.setId(rbacRoleRelaId);
	}

	/**
	 * 角色标识.
	 **/
	@Getter
	@Setter
	private Long rbacRoleId;

	/**
	 * 父角色标识.
	 **/
	@Getter
	@Setter
	private Long rbacParentRoleId;

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
	public RbacRoleRelation() {
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

		RbacRole rbacRole = this.getRbacRole();

		if (rbacRole != null) {
			return rbacRole.getRbacRoleName();
		}

		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getRoot() {

		if (rootId != null) {

			StringBuffer sb = new StringBuffer(
					"SELECT A.RBAC_ROLE_NAME TREE_LABEL,B.* FROM RBAC_ROLE A, RBAC_ROLE_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_ROLE_ID = B.RBAC_ROLE_ID AND B.RBAC_PARENT_ROLE_ID = ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.rootId);

			return (ArrayList<TreeNodeEntity>) RbacRoleRelation
					.repository()
					.jdbcFindList(sb.toString(), params, RbacRoleRelation.class);

		}

		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {

		StringBuffer sb = new StringBuffer(
				"SELECT A.RBAC_ROLE_NAME TREE_LABEL,B.* FROM RBAC_ROLE A, RBAC_ROLE_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_ROLE_ID = B.RBAC_ROLE_ID AND B.RBAC_PARENT_ROLE_ID = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getRbacRoleId());

		return (ArrayList<TreeNodeEntity>) RbacRoleRelation.repository()
				.jdbcFindList(sb.toString(), params, RbacRoleRelation.class);

	}

	/**
	 * 创建对象实例.
	 * 
	 * @return RbacRoleRelation
	 */
	public static RbacRoleRelation newInstance() {
		return new RbacRoleRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRoleRelationDao repository() {
		return (RbacRoleRelationDao) ApplicationContextUtil
				.getBean("rbacRoleRelationDao");
	}

	/**
	 * 获取角色
	 * 
	 * @return
	 */
	public RbacRole getRbacRole() {
		if (this.rbacRoleId != null) {
			return (RbacRole) RbacRole.repository().getObject(RbacRole.class,
					this.rbacRoleId);
		}
		return null;
	}

}
