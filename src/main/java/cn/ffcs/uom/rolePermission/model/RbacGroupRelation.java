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
import cn.ffcs.uom.rolePermission.dao.RbacGroupRelationDao;

/**
 * 组关系实体.
 * 
 * @author
 * 
 **/
public class RbacGroupRelation extends UomEntity implements TreeNodeEntity,
		Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 组关系标识.
	 **/
	public Long getRbacGroupRelaId() {
		return super.getId();
	}

	public void setRbacGroupRelaId(Long rbacGroupRelaId) {
		super.setId(rbacGroupRelaId);
	}

	/**
	 * 组标识.
	 **/
	@Getter
	@Setter
	private Long rbacGroupId;

	/**
	 * 父组标识.
	 **/
	@Getter
	@Setter
	private Long rbacParentGroupId;

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
	public RbacGroupRelation() {
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

		RbacGroup rbacGroup = this.getRbacGroup();

		if (rbacGroup != null) {
			return rbacGroup.getRbacGroupName();
		}

		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getRoot() {

		if (rootId != null) {

			StringBuffer sb = new StringBuffer(
					"SELECT A.RBAC_GROUP_NAME TREE_LABEL,B.* FROM RBAC_GROUP A, RBAC_GROUP_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_GROUP_ID = B.RBAC_GROUP_ID AND B.RBAC_PARENT_GROUP_ID = ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.rootId);

			return (ArrayList<TreeNodeEntity>) RbacGroupRelation.repository()
					.jdbcFindList(sb.toString(), params,
							RbacGroupRelation.class);

		}

		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {

		StringBuffer sb = new StringBuffer(
				"SELECT A.RBAC_GROUP_NAME TREE_LABEL,B.* FROM RBAC_GROUP A, RBAC_GROUP_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_GROUP_ID = B.RBAC_GROUP_ID AND B.RBAC_PARENT_GROUP_ID = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getRbacGroupId());

		return (ArrayList<TreeNodeEntity>) RbacGroupRelation.repository()
				.jdbcFindList(sb.toString(), params, RbacGroupRelation.class);

	}

	/**
	 * 创建对象实例.
	 * 
	 * @return RbacGroupRelation
	 */
	public static RbacGroupRelation newInstance() {
		return new RbacGroupRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacGroupRelationDao repository() {
		return (RbacGroupRelationDao) ApplicationContextUtil
				.getBean("rbacGroupRelationDao");
	}

	/**
	 * 获取组
	 * 
	 * @return
	 */
	public RbacGroup getRbacGroup() {
		if (this.rbacGroupId != null) {
			return (RbacGroup) RbacGroup.repository().getObject(
					RbacGroup.class, this.rbacGroupId);
		}
		return null;
	}

}
