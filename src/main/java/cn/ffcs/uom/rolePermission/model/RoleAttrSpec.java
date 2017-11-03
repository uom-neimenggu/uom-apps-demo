package cn.ffcs.uom.rolePermission.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.rolePermission.dao.RoleAttrSpecDao;

/**
 * 角色属性规格实体.
 * 
 * @author
 * 
 **/
public class RoleAttrSpec extends UomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 属性规格标识.
	 **/
	public Long getRoleAttrSpecId() {
		return super.getId();
	}

	public void setRoleAttrSpecId(Long roleAttrSpecId) {
		super.setId(roleAttrSpecId);
	}

	/**
	 * 属性种类.
	 **/
	@Getter
	@Setter
	private Long roleAttrSpecSortId;

	/**
	 * 属性名称.
	 **/
	@Getter
	@Setter
	private String roleAttrName;

	/**
	 * 属性类型.
	 **/
	@Getter
	@Setter
	private Long roleAttrSpecType;

	/**
	 * 下拉值
	 */
	private List<RoleAttrValue> roleAttrValueList;

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static RoleAttrSpecDao repository() {
		return (RoleAttrSpecDao) ApplicationContextUtil
				.getBean("roleAttrSpecDao");
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RoleAttrValue> getRoleAttrValueList() {
		if (this.roleAttrValueList == null) {
			if (this.getRoleAttrSpecId() != null
					&& roleAttrSpecType == OrganizationConstant.ATTR_SPEC_TYPE_SELECT) {
				String sql = "SELECT * FROM ROLE_ATTR_VALUE WHERE STATUS_CD =? AND ROLE_ATTR_SPEC_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getRoleAttrSpecId());
				roleAttrValueList = RoleAttrValue.repository().jdbcFindList(
						sql, params, RoleAttrValue.class);
			}
		}
		return roleAttrValueList;
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<NodeVo> getRoleAttrValueNodeVoList() {
		List<NodeVo> list = new ArrayList<NodeVo>();
		List<RoleAttrValue> roleAttrValueList = this.getRoleAttrValueList();
		if (roleAttrValueList != null) {
			for (RoleAttrValue roleAttrValue : roleAttrValueList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(roleAttrValue.getRoleAttrValue());
				nodeVo.setName(roleAttrValue.getRoleAttrValueName());
				list.add(nodeVo);
			}
		}
		return list;
	}

	/**
	 * 获取属性值列表
	 * 
	 * @return
	 */
	public List<NodeVo> getRoleAttrValueNodeVoList(
			List<RoleAttrValue> roleAttrValueList) {
		List<NodeVo> list = new ArrayList<NodeVo>();
		if (roleAttrValueList != null) {
			for (RoleAttrValue roleAttrValue : roleAttrValueList) {
				NodeVo nodeVo = new NodeVo();
				nodeVo.setId(roleAttrValue.getRoleAttrValue());
				nodeVo.setName(roleAttrValue.getRoleAttrValueName());
				list.add(nodeVo);
			}
		}
		return list;
	}
}
