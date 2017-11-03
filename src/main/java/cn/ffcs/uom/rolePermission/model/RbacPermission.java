package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacPermissionDao;

public class RbacPermission extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacPermissionId() {
		return super.getId();
	}

	public void setRbacPermissionId(Long rbacPermissionId) {
		super.setId(rbacPermissionId);
	}

	@Getter
	@Setter
	private String rbacPermissionCode;

	@Getter
	@Setter
	private String rbacPermissionName;

	@Getter
	@Setter
	private String rbacPermissionBean;

	@Getter
	@Setter
	private String rbacPermissionType;

	@Getter
	@Setter
	private String rbacPermissionDesc;

	/**
	 * 扩展属性
	 */
	@Setter
	private List<RbacPermissionExtAttr> rbacPermissionExtAttrList;

	/**
	 * 获取扩展属性
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public List<RbacPermissionExtAttr> getRbacPermissionExtAttrList() {
		if (this.rbacPermissionExtAttrList == null
				|| rbacPermissionExtAttrList.size() <= 0) {
			if (this.getRbacPermissionId() != null) {
				String sql = "SELECT * FROM RBAC_PERMISSION_EXT_ATTR A WHERE A.STATUS_CD = ? AND A.RBAC_PERMISSION_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getRbacPermissionId());
				rbacPermissionExtAttrList = this.repository().jdbcFindList(sql,
						params, RbacPermissionExtAttr.class);
			}
		}
		return rbacPermissionExtAttrList;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacPermissionDao repository() {
		return (RbacPermissionDao) ApplicationContextUtil
				.getBean("rbacPermissionDao");
	}

}