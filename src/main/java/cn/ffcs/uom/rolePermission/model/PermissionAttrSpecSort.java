package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.PermissionAttrSpecSortDao;

/**
 * 组织属性规格种类实体.
 * 
 * @author
 * 
 **/
public class PermissionAttrSpecSort extends UomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 类型标识.
	 **/
	public Long getPermissionAttrSpecSortId() {
		return super.getId();
	}

	public void setPermissionAttrSpecSortId(Long permissionAttrSpecSortId) {
		super.setId(permissionAttrSpecSortId);
	}

	/**
	 * 类型名称.
	 **/
	@Getter
	@Setter
	private String permissionSortName;

	/**
	 * 种类类型是否挂树上可以编辑.
	 **/
	@Getter
	@Setter
	private String permissionSortType;

	/**
	 * 属性规格列表
	 */
	private List<PermissionAttrSpec> permissionAttrSpecList;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static PermissionAttrSpecSortDao repository() {
		return (PermissionAttrSpecSortDao) ApplicationContextUtil
				.getBean("permissionAttrSpecSortDao");
	}

	/**
	 * 获取属性规格列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PermissionAttrSpec> getPermissionAttrSpecList() {
		if (this.permissionAttrSpecList == null) {
			if (this.getPermissionAttrSpecSortId() != null) {
				String sql = "SELECT * FROM PERMISSION_ATTR_SPEC WHERE STATUS_CD = ? AND PERMISSION_ATTR_SPEC_SORT_ID = ? ORDER BY PERMISSION_ATTR_SPEC_ID";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getPermissionAttrSpecSortId());
				this.permissionAttrSpecList = PermissionAttrSpec.repository()
						.jdbcFindList(sql, params, PermissionAttrSpec.class);
			}
		}
		return this.permissionAttrSpecList;
	}
}
