package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RbacRoleDao;
import cn.ffcs.uom.systemconfig.model.AttrValue;

public class RbacRole extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public Long getRbacRoleId() {
		return super.getId();
	}

	public void setRbacRoleId(Long rbacRoleId) {
		super.setId(rbacRoleId);
	}

	@Getter
	@Setter
	private String rbacRoleCode;

	@Getter
	@Setter
	private String rbacRoleName;

	@Getter
	@Setter
	private String rbacRoleType;

	@Getter
	@Setter
	private String rbacRoleDesc;

	/**
	 * 扩展属性
	 */
	@Setter
	private List<RbacRoleExtAttr> rbacRoleExtAttrList;

	/**
	 * 获取角色类型名称
	 */
	public String getRbacRoleTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"RbacRole", "rbacRoleType", this.getRbacRoleType(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 获取扩展属性
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public List<RbacRoleExtAttr> getRbacRoleExtAttrList() {
		if (this.rbacRoleExtAttrList == null || rbacRoleExtAttrList.size() <= 0) {
			if (this.getRbacRoleId() != null) {
				String sql = "SELECT * FROM RBAC_ROLE_EXT_ATTR A WHERE A.STATUS_CD = ? AND A.RBAC_ROLE_ID = ?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getRbacRoleId());
				rbacRoleExtAttrList = this.repository().jdbcFindList(sql,
						params, RbacRoleExtAttr.class);
			}
		}
		return rbacRoleExtAttrList;
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RbacRoleDao repository() {
		return (RbacRoleDao) ApplicationContextUtil.getBean("rbacRoleDao");
	}

}