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
import cn.ffcs.uom.rolePermission.dao.RbacBusinessSystemRelationDao;

/**
 * 系统关系实体.
 * 
 * @author
 * 
 **/
public class RbacBusinessSystemRelation extends UomEntity implements
		TreeNodeEntity, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统关系标识.
	 **/
	public Long getRbacBusinessSystemRelaId() {
		return super.getId();
	}

	public void setRbacBusinessSystemRelaId(Long rbacBusinessSystemRelaId) {
		super.setId(rbacBusinessSystemRelaId);
	}

	/**
	 * 系统标识.
	 **/
	@Getter
	@Setter
	private Long rbacBusinessSystemId;

	/**
	 * 父系统标识.
	 **/
	@Getter
	@Setter
	private Long rbacParentBusinessSystemId;

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
	public RbacBusinessSystemRelation() {
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

		RbacBusinessSystem rbacBusinessSystem = this.getRbacBusinessSystem();

		if (rbacBusinessSystem != null) {
			return rbacBusinessSystem.getRbacBusinessSystemName();
		}

		return "";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getRoot() {

		if (rootId != null) {

			StringBuffer sb = new StringBuffer(
					"SELECT A.RBAC_BUSINESS_SYSTEM_NAME TREE_LABEL,B.* FROM RBAC_BUSINESS_SYSTEM A, RBAC_BUSINESS_SYSTEM_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_BUSINESS_SYSTEM_ID = B.RBAC_BUSINESS_SYSTEM_ID AND B.RBAC_PARENT_BUSINESS_SYSTEM_ID = ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(this.rootId);

			return (ArrayList<TreeNodeEntity>) RbacBusinessSystemRelation
					.repository().jdbcFindList(sb.toString(), params,
							RbacBusinessSystemRelation.class);

		}

		return null;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ArrayList<TreeNodeEntity> getChildren() {

		StringBuffer sb = new StringBuffer(
				"SELECT A.RBAC_BUSINESS_SYSTEM_NAME TREE_LABEL,B.* FROM RBAC_BUSINESS_SYSTEM A, RBAC_BUSINESS_SYSTEM_RELATION B WHERE A.STATUS_CD = ? AND B.STATUS_CD = ? AND A.RBAC_BUSINESS_SYSTEM_ID = B.RBAC_BUSINESS_SYSTEM_ID AND B.RBAC_PARENT_BUSINESS_SYSTEM_ID = ?");

		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(this.getRbacBusinessSystemId());

		return (ArrayList<TreeNodeEntity>) RbacBusinessSystemRelation
				.repository().jdbcFindList(sb.toString(), params,
						RbacBusinessSystemRelation.class);

	}

	/**
	 * 创建对象实例.
	 * 
	 * @return RbacBusinessSystemRelation
	 */
	public static RbacBusinessSystemRelation newInstance() {
		return new RbacBusinessSystemRelation();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacBusinessSystemRelationDao repository() {
		return (RbacBusinessSystemRelationDao) ApplicationContextUtil
				.getBean("rbacBusinessSystemRelationDao");
	}

	/**
	 * 获取系统
	 * 
	 * @return
	 */
	public RbacBusinessSystem getRbacBusinessSystem() {
		if (this.rbacBusinessSystemId != null) {
			return (RbacBusinessSystem) RbacBusinessSystem.repository()
					.getObject(RbacBusinessSystem.class,
							this.rbacBusinessSystemId);
		}
		return null;
	}

}
