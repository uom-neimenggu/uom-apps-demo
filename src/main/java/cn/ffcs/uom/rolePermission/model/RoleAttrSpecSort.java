package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.rolePermission.dao.RoleAttrSpecSortDao;

/**
 * 组织属性规格种类实体.
 * 
 * @author
 * 
 **/
public class RoleAttrSpecSort extends UomEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 类型标识.
	 **/
	public Long getRoleAttrSpecSortId() {
		return super.getId();
	}

	public void setRoleAttrSpecSortId(Long roleAttrSpecSortId) {
		super.setId(roleAttrSpecSortId);
	}

	/**
	 * 类型名称.
	 **/
	@Getter
	@Setter
	private String roleSortName;

	/**
	 * 种类类型是否挂树上可以编辑.
	 **/
	@Getter
	@Setter
	private String roleSortType;

	/**
	 * 属性规格列表
	 */
	private List<RoleAttrSpec> roleAttrSpecList;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RoleAttrSpecSortDao repository() {
		return (RoleAttrSpecSortDao) ApplicationContextUtil
				.getBean("roleAttrSpecSortDao");
	}

	/**
	 * 获取属性规格列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RoleAttrSpec> getRoleAttrSpecList() {
		if (this.roleAttrSpecList == null) {
			if (this.getRoleAttrSpecSortId() != null) {
				String sql = "SELECT * FROM ROLE_ATTR_SPEC WHERE STATUS_CD = ? AND ROLE_ATTR_SPEC_SORT_ID = ? ORDER BY ROLE_ATTR_SPEC_ID";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getRoleAttrSpecSortId());
				this.roleAttrSpecList = RoleAttrSpec.repository().jdbcFindList(
						sql, params, RoleAttrSpec.class);
			}
		}
		return this.roleAttrSpecList;
	}
}
